package es.caib.regtel.plugincaib.persistence.ejb;

import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.security.auth.login.LoginContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.redose.modelInterfaz.ReferenciaRDS;
import es.caib.regweb.logic.helper.ParametrosRegistroEntrada;
import es.caib.regweb.logic.helper.ParametrosRegistroSalida;
import es.caib.regweb.logic.interfaces.RegistroEntradaFacade;
import es.caib.regweb.logic.interfaces.RegistroEntradaFacadeHome;
import es.caib.regweb.logic.interfaces.RegistroSalidaFacade;
import es.caib.regweb.logic.interfaces.RegistroSalidaFacadeHome;
import es.caib.regweb.logic.interfaces.ValoresFacadeHome;
import es.caib.sistra.plugins.regtel.OficinaRegistro;
import es.caib.sistra.plugins.regtel.ResultadoRegistro;
import es.caib.sistra.plugins.regtel.ServicioDestinatario;
import es.caib.sistra.plugins.regtel.TipoAsunto;
import es.caib.util.EjbUtil;
import es.caib.util.StringUtil;
import es.caib.util.UsernamePasswordCallbackHandler;
import es.caib.xml.registro.factoria.impl.AsientoRegistral;
import es.caib.xml.registro.factoria.impl.DatosInteresado;
import es.caib.xml.registro.factoria.impl.DireccionCodificada;
import es.caib.xml.registro.factoria.impl.Justificante;


/**
 * Implementacion por EJB
 *  
 */
public class RegistroWebImplEjb implements RegistroWebImplInt
{	
	private static final Log logger = LogFactory.getLog(RegistroWebImplEjb.class);
	
	private String principal;
	
	private Properties config = null;

	private static String CODIGO_PROVINCIA_CAIB = "7";
	private static String PAIS_ESPANYA = "ESPAÑA";
	
	public final static String CODIGO_PAIS_ESPANYA = "ESP";
	public final static String CODIGO_PAIS_ESPANYA_REDUCIDO = "ES";
	
	public final static String SEPARADOR_OFICINA_FISICA = ".";
	public final static String REGEXP_SEPARADOR_OFICINA_FISICA = "\\."; // Exp regular para hacer split
	

	public void setPrincipal(String principal) {
		this.principal = principal;	
	}
	
    public void ejbCreate() throws CreateException {
		try
		{
				
		}
		catch( Exception exc )
		{
			//exc.printStackTrace();
			logger.error( exc );
			throw new CreateException( exc.getLocalizedMessage() );
		}
	}	
	
	/**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     */
	public ResultadoRegistro registroEntrada(
			AsientoRegistral asiento,
			ReferenciaRDS refAsiento,
			Map refAnexos) throws Exception {
		
		if (isPrintPeticio())
			logParametres(asiento, refAsiento, refAnexos);
		
		
		ParametrosRegistroEntrada params = mapeaAsientoParametrosRegistroEntrada(asiento); 
		
		ResultadoRegistro res = realizarRegistroEntrada(params);
		
		return res;
		
	}
	
	/**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     */
	public ResultadoRegistro registroSalida(
			AsientoRegistral asiento,
			ReferenciaRDS refAsiento,
			Map refAnexos) throws Exception {
		
		if (isPrintPeticio())
			logParametres(asiento, refAsiento, refAnexos);
		
		ParametrosRegistroSalida params = mapearAsientoParametrosRegistroSalida(asiento);
		
		ResultadoRegistro res = realizarRegistroSalida(params);
		
		return res;
		
	}

	
	/**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     */
	public ResultadoRegistro confirmarPreregistro(
			String oficina,
			String codigoProvincia,
			String codigoMunicipio,
			String descripcionMunicipio,
			Justificante justificantePreregistro,
			ReferenciaRDS refJustificante,
			ReferenciaRDS refAsiento,
			Map refAnexos) throws Exception {
		
		if (isPrintPeticio())
			logParametres(justificantePreregistro.getAsientoRegistral(), refAsiento, refAnexos);
		
		// Mapeamos datos asiento cambiando la oficina de registro
		justificantePreregistro.getAsientoRegistral().getDatosOrigen().setCodigoEntidadRegistralOrigen(oficina);
		ParametrosRegistroEntrada params = mapeaAsientoParametrosRegistroEntrada(justificantePreregistro.getAsientoRegistral()); 
		
		// Particularizamos campos para preregistro
		params.fijaUsuario(principal); // Usuario es el conectado a la aplicacion
		params.fijaPasswordUser(null);
		if(codigoProvincia != null && CODIGO_PROVINCIA_CAIB.equals(codigoProvincia)){
			params.setbalears(codigoMunicipio);
			params.setfora("");
		}else{
			params.setbalears("");
			params.setfora(descripcionMunicipio);			
		}
		params.setcomentario("Confirmació preregistre "+justificantePreregistro.getNumeroRegistro()+" - "+params.getComentario());
				
		// Registramos
		ResultadoRegistro res = realizarRegistroEntrada(params);
		
		return res;
				
	}

	/**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     */
	public List obtenerOficinasRegistro() {
		return obtenerOficinasRegistro(null);
	}

	/**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     */
	public List obtenerOficinasRegistroUsuario(String usuario) {
		if (usuario == null){
			logger.error("No se ha indicado usuario. Devolvemos lista vacía.");
			return new ArrayList();
		}
		return obtenerOficinasRegistro(usuario);
	}

	/**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     */
	public List obtenerTiposAsunto() {
		List lista = new ArrayList();
		try {
			Vector v = buscarDocumentos();
			Iterator it = v.iterator();
			while (it.hasNext()) {
				TipoAsunto of = new TipoAsunto();
				of.setCodigo((String)it.next());
				of.setDescripcion((String)it.next());
				if (of.getCodigo() != null && of.getCodigo().length() > 0)
					lista.add(of);
			}
		} catch (Exception ex) {
			// Si hi ha algun error no tornam cap tipus
			logger.error("Error al obtener los tipos de documentos", ex);
		}
		return lista;
	}

	/**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     */
	public List obtenerServiciosDestino() {
		List lista = new ArrayList();
		try{
			Vector v = buscarDestinatarios();
			// Por cada organismo añade un string con su código, otro string con nombre corto y otro con su nombre largo. 
		    // En el caso de no encontrar nada envía el vector con tres strings: <"&nbsp;","No hi ha Organismes","&nbsp;"> 
			Iterator it = v.iterator();
			while (it.hasNext()) {
				String codigo = (String)it.next();
				if ("&nbsp;".equals(codigo))
					break;
				ServicioDestinatario sd = new ServicioDestinatario();
				sd.setCodigo(codigo);
				sd.setDescripcion(sd.getCodigo() + " - " + (String)it.next());
				it.next(); // Desc larga, no la queremos
				
				// Nos quedamos solo con las consellerias
				// Formato: nn00
				if (codigo != null && codigo.length() == 4 && codigo.endsWith("00")) {					
					lista.add(sd);
				}
				
			}
		} catch (Exception ex) {
			// Si hi ha algun error no tornam cap servei
			logger.error("Error al obtener los servicios destinatarios", ex);
		}
		return lista;
		
		/*
		List lista = new ArrayList();
		try {
			Map<String, ServicioDestinatario> servicios = new HashMap<String, ServicioDestinatario>();
			List<OficinaRegistro> oficines = obtenerOficinasRegistro();
			for (OficinaRegistro oficina: oficines) {
				String codiOficina = oficina.getCodigo();
				String[] codisOficina = codiOficina.split("-");
				if (codisOficina.length == 2) {
					Vector v = buscarDestinatarios(codisOficina[0]);
					Iterator it = v.iterator();
					while (it.hasNext()) {
						String codigo = (String)it.next();
						if ("&nbsp;".equals(codigo))
							break;
						ServicioDestinatario of = new ServicioDestinatario();
						of.setCodigo(codigo);
						of.setDescripcion((String)it.next());
						it.next();
						servicios.put(of.getCodigo(), of);
					}
				}
			}
			for (String codi: servicios.keySet()) {
				lista.add(servicios.get(codi));
			}
		} catch (Exception ex) {
			// Si hi ha algun error no tornam cap servei
			logger.error("Error al obtenir els serveis destinataris", ex);
		}
		return lista;
		*/
	}
	
	/**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     */
    public void anularRegistroEntrada(String numeroRegistro, Date fechaRegistro) throws Exception {
		
		logger.debug("accediendo a anular registro entrada: " + numeroRegistro);
				
		LoginContext lc = null;
		try {
			
			// Extraemos info del num de registro
			String [] tokens = numeroRegistro.split("/");			
			String codiOficina = tokens[0];
			String numero = tokens[1];
			String ano = tokens[2];
		
			// Establecemos parametros
			ParametrosRegistroEntrada registro = new ParametrosRegistroEntrada();
			registro.fijaUsuario(getUsuarioRegistro());
			registro.setOrigenRegistro("SISTRA"); 
			registro.setoficina(codiOficina);
			registro.setNumeroEntrada(numero); 			
			registro.setAnoEntrada(ano);
			
			// Conectamos a Registro
			lc = doLogin();
			Context ctx = getInitialContext();
			Object objRef = ctx.lookup("es.caib.regweb.logic.RegistroEntradaFacade");
			RegistroEntradaFacadeHome home = (RegistroEntradaFacadeHome)javax.rmi.PortableRemoteObject.narrow(
					objRef,
					RegistroEntradaFacadeHome.class);
			RegistroEntradaFacade regent = home.create();
			
			if(regent.anular(registro,true)) {
				logger.debug("Registro anulado");
			} else { 
				throw new Exception("No se ha podido anular el registro"); 
			}

			ctx.close();						
		} finally {
			if (lc != null)
				lc.logout();
		}										

	}

    /**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     */
	public void anularRegistroSalida(String numeroRegistro, Date fechaRegistro) throws Exception {
		logger.debug("accediendo a anular registro salida: " + numeroRegistro);
		
		LoginContext lc = null;
		try {
			
			// Extraemos info del num de registro
			String [] tokens = numeroRegistro.split("/");
			String codiOficina = tokens[0];
			String numero = tokens[1];
			String ano = tokens[2];
		
			// Establecemos parametros
			ParametrosRegistroSalida registro = new ParametrosRegistroSalida();
			registro.fijaUsuario(getUsuarioRegistro());
			//registro.setOrigenRegistro("SISTRA"); 
			registro.setAnoSalida(ano); 
			registro.setNumeroSalida(numero); 
			registro.setoficina(codiOficina);
			
			// Conectamos a Registro
			lc = doLogin();
			Context ctx = getInitialContext();
			Object objRef = ctx.lookup("es.caib.regweb.logic.RegistroSalidaFacade");
			RegistroSalidaFacadeHome home = (RegistroSalidaFacadeHome)javax.rmi.PortableRemoteObject.narrow(
					objRef,
					RegistroSalidaFacadeHome.class);
			RegistroSalidaFacade regent = home.create();
			
			// Anulamos
			if(regent.anular(registro,true)){
				logger.debug("Registro anulado"); 
			} else { 
				throw new Exception("No se ha podido anular el registro");
			}

			
			ctx.close();						
		} finally {
			if (lc != null)
				lc.logout();
		}										
		
	}


    /**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     */
	public String obtenerDescripcionSelloOficina(String codigoOficina) {
		
		String descSello = "";
		
		try {
			
			logger.debug("obtener descripcion sello para oficina: " + codigoOficina);
			
			// vector con cuadruplas: num oficina - num oficina física - desc oficina física - desc oficina
			Vector v = buscarOficinas(false);
			
			boolean enc = false;
			Iterator it = v.iterator();
			while (it.hasNext()) {
				String codiOficina = (String)it.next();
				if (codiOficina == null || codiOficina.length() <= 0) {
					return null;
				}
				String codiOficinaFisica = (String)it.next();
				String nomOficinaFisica = (String)it.next();
				String nomOficina = (String)it.next();
				
				String codOf = codiOficina + SEPARADOR_OFICINA_FISICA + codiOficinaFisica;
				
				if (codigoOficina.equals(codOf)) {
					enc = true;
					descSello = nomOficina;
				}																		
			}			
			
			if (!enc) {
				logger.debug("No se ha encontrado oficina: " + codigoOficina + ". Se devuelve descripcion para sello vacía.");
			}
						
		} catch (Exception ex) {
			// Si hi ha algun error no tornam cap oficina
			logger.error("Error al obtener las oficinas de registro", ex);			
		}
		
		return descSello;
		
		
	}
	
	// -------------------------------------------------------------------------------------------------------------------------------
	//		FUNCIONES UTILIDAD
	// -------------------------------------------------------------------------------------------------------------------------------

	private List obtenerOficinasRegistro(String usuario) {
		List lista = new ArrayList();
		try {
			Vector v;
			if (usuario == null){
				// vector con cuadruplas: num oficina - num oficina física - desc oficina física - desc oficina
				v = buscarOficinas(true);
			}else{
				// vector con tripleta:   num oficina - num oficina física - desc oficina física
				v = buscarOficinasUsuario(usuario);
			}
			Iterator it = v.iterator();
			while (it.hasNext()) {
				OficinaRegistro of = new OficinaRegistro();
				String codiOficina = (String)it.next();
				if (codiOficina == null || codiOficina.length() <= 0) {
					return lista;
				}
				String codiOficinaFisica = (String)it.next();
				String nomOficinaFisica = (String)it.next();
				if (usuario == null){
					String nomOficina = (String)it.next();
					nomOficinaFisica = nomOficinaFisica + " (" + nomOficina + ")";
				}
				of.setCodigo(codiOficina + SEPARADOR_OFICINA_FISICA + codiOficinaFisica);
				of.setDescripcion(of.getCodigo() + " - " + nomOficinaFisica);
				lista.add(of);
			}			
			
		} catch (Exception ex) {
			// Si hi ha algun error no tornam cap oficina
			logger.error("Error al obtener las oficinas de registro", ex);
		}
		return lista;
	}

	private boolean isPrintPeticio() throws Exception {
		String printPeticio = getConfig().getProperty("plugin.regweb.print.peticio");
		if (printPeticio != null && "true".equals(printPeticio))
			return true;
		return false;
	}
	
	private String getIdiomaAsiento(AsientoRegistral asiento) throws Exception {
		String idioma = asiento.getDatosAsunto().getIdiomaAsunto();
		if ("es".equalsIgnoreCase(idioma))
			return getConfig().getProperty("plugin.regweb.idioma.es");
		else if ("ca".equalsIgnoreCase(idioma))
			return getConfig().getProperty("plugin.regweb.idioma.ca");
		else
			return getConfig().getProperty("plugin.regweb.idioma.default");
	}
	private String getIdentificacioRepresentant(AsientoRegistral asiento) {
		for (Iterator it = asiento.getDatosInteresado().iterator(); it.hasNext();) {
			DatosInteresado datosInteresado = (DatosInteresado) it.next();
			if ("RPT".equals(datosInteresado.getTipoInteresado()))
				return datosInteresado.getIdentificacionInteresado();
		}
		return null;
	}
	private DireccionCodificada getProcedenciaRepresentant(AsientoRegistral asiento) {
		for (Iterator it = asiento.getDatosInteresado().iterator(); it.hasNext();) {
			DatosInteresado datosInteresado = (DatosInteresado) it.next();
			if ("RPT".equals(datosInteresado.getTipoInteresado()))
				return datosInteresado.getDireccionCodificada();
		}
		return null;
	}

	private void logParametres(
			AsientoRegistral asiento,
			ReferenciaRDS refAsiento,
			Map refAnexos) {
		logger.info("- Dades asiento:");
		if (asiento.getDatosOrigen() != null) {
			logger.info("  datosOrigen.codigoEntidadRegistralOrigen: " + asiento.getDatosOrigen().getCodigoEntidadRegistralOrigen());
			logger.info("  datosOrigen.numeroRegistro: " + asiento.getDatosOrigen().getNumeroRegistro());
			logger.info("  datosOrigen.fechaEntradaRegistro: " + asiento.getDatosOrigen().getFechaEntradaRegistro());
			logger.info("  datosOrigen.tipoRegistro: " + asiento.getDatosOrigen().getTipoRegistro());
		}
		if (asiento.getDatosDestino() != null) {
			logger.info("  datosDestino.codigoEntidadRegistralDestino: " + asiento.getDatosDestino().getCodigoEntidadRegistralDestino());
			logger.info("  datosDestino.decodificacionEntidadRegistralDestino: " + asiento.getDatosDestino().getDecodificacionEntidadRegistralDestino());
		}
		if (asiento.getDatosAsunto() != null) {
			logger.info("  datosAsunto.codigoOrganoDestino: " + asiento.getDatosAsunto().getCodigoOrganoDestino());
			logger.info("  datosAsunto.codigoUnidadAdministrativa: " + asiento.getDatosAsunto().getCodigoUnidadAdministrativa());
			logger.info("  datosAsunto.descripcionOrganoDestino: " + asiento.getDatosAsunto().getDescripcionOrganoDestino());
			logger.info("  datosAsunto.extractoAsunto: " + asiento.getDatosAsunto().getExtractoAsunto());
			logger.info("  datosAsunto.identificadorTramite: " + asiento.getDatosAsunto().getIdentificadorTramite());
			logger.info("  datosAsunto.idiomaAsunto: " + asiento.getDatosAsunto().getIdiomaAsunto());
			logger.info("  datosAsunto.tipoAsunto: " + asiento.getDatosAsunto().getTipoAsunto());
		}
		if (asiento.getDatosInteresado() != null) {
			int i = 0;
			for (Iterator it = asiento.getDatosInteresado().iterator(); it.hasNext();) {
				DatosInteresado datosInteresado = (DatosInteresado) it.next();
				logger.info("  datosInteresado[" + i + "].nivelAutenticacion: " + datosInteresado.getNivelAutenticacion());
				logger.info("  datosInteresado[" + i + "].usuarioSeycon: " + datosInteresado.getUsuarioSeycon());
				logger.info("  datosInteresado[" + i + "].tipoInteresado: " + datosInteresado.getTipoInteresado());
				logger.info("  datosInteresado[" + i + "].tipoIdentificacion: " + datosInteresado.getTipoIdentificacion());
				logger.info("  datosInteresado[" + i + "].numeroIdentificacion: " + datosInteresado.getNumeroIdentificacion());
				logger.info("  datosInteresado[" + i + "].formatoDatosInteresado: " + datosInteresado.getFormatoDatosInteresado());
				logger.info("  datosInteresado[" + i + "].identificacionInteresado: " + datosInteresado.getIdentificacionInteresado());
				logger.info("  datosInteresado[" + i + "].direccionCodificada: " + datosInteresado.getDireccionCodificada());
			}
		}
		logger.info("  version: " + asiento.getVersion());
		if (refAsiento != null) {
			logger.info("- Document refAsiento:");
			logger.info("  refAsiento.codigo: " + refAsiento.getCodigo());
			logger.info("  refAsiento.clave: " + refAsiento.getClave());
		}
		if (refAnexos != null) {
			logger.info("- Document refAnexos:");
			for (Iterator it =  refAnexos.keySet().iterator(); it.hasNext();) {
				Object key = it.next();
				ReferenciaRDS refRDS = (ReferenciaRDS)refAnexos.get(key);
				logger.info("  refAnexos[" + key + "].codigo: " + refRDS.getCodigo());
				logger.info("  refAnexos[" + key + "].clave: " + refRDS.getClave());
			}
		}
	}


	private ParametrosRegistroEntrada validarRegistroEntrada(ParametrosRegistroEntrada params) throws Exception {
		ParametrosRegistroEntrada resposta = null;
		LoginContext lc = null;
		try {
			lc = doLogin();
			Context ctx = getInitialContext();
			Object objRef = ctx.lookup("es.caib.regweb.logic.RegistroEntradaFacade");
			RegistroEntradaFacadeHome home = (RegistroEntradaFacadeHome)javax.rmi.PortableRemoteObject.narrow(
					objRef,
					RegistroEntradaFacadeHome.class);
			resposta = home.create().validar(params);
			ctx.close();
		} finally {
			if (lc != null)
				lc.logout();
		}
		return resposta;
	}
	
	private ResultadoRegistro realizarRegistroSalida (
			ParametrosRegistroSalida params) throws Exception {
		logger.debug("realizarRegistroSalida: inicio (usuario: " + params.getUsuario()  + ")");
		
		ParametrosRegistroSalida respostaValidacio = validarRegistroSalida(params);
		if (respostaValidacio.getValidado()) {
			ParametrosRegistroSalida respostaGrabacio = grabarRegistroSalida(params);
			if (respostaGrabacio.getGrabado()) {
				ResultadoRegistro res = new ResultadoRegistro();
				res.setFechaRegistro(StringUtil.cadenaAFecha( params.getDataSalida() + " " + params.getHora(), "dd/MM/yyyy HH:mm"));
				
				// INDRA: CONCATENAMOS OFICINA A NUM REGISTRO
				//res.setNumeroRegistro(respostaGrabacio.getNumeroSalida() + "/" + respostaGrabacio.getAnoSalida());				
				res.setNumeroRegistro(respostaGrabacio.getOficina() + "/" + respostaGrabacio.getNumeroSalida() + "/" + respostaGrabacio.getAnoSalida());				
				
				return res;
			} else {
				throw new Exception("La anotación de registro no se ha guardado");
			}
		} else {
			Map errors = respostaValidacio.getErrores();
			StringBuilder sb = new StringBuilder();
			if (errors != null && errors.size() > 0) {
				for (Iterator it = errors.keySet().iterator(); it.hasNext();) {
					String camp = (String) it.next();
					sb.append("|[" + camp + "]:" + errors.get(camp));
				}
			} else {
				sb.append("No se han devuelto errores");
			}
			throw new Exception("Errores de validación: " + sb.toString());
		}		
	}

	private ParametrosRegistroSalida mapearAsientoParametrosRegistroSalida(
			AsientoRegistral asiento) throws Exception {
		ParametrosRegistroSalida params = new ParametrosRegistroSalida();
		params.fijaUsuario(getUsuarioRegistro());
		//params.setOrigenRegistro("SISTRA"); 
		Date ara = new Date();
		DateFormat dfData = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat dfHora = new SimpleDateFormat("HH:mm");
		params.setdatasalida(dfData.format(ara));
		params.sethora(dfHora.format(ara));
		params.setdata(dfData.format(ara));
		
		String codiOficina = asiento.getDatosOrigen().getCodigoEntidadRegistralOrigen();
		String[] codisOficina = descomponerOficinaFisica(codiOficina);
		params.setoficina(codisOficina[0]);
		params.setoficinafisica(codisOficina[1]);
		
		
		params.settipo(asiento.getDatosAsunto().getTipoAsunto());
		params.setidioma(getIdiomaAsiento(asiento));
		if (getIdentificacioRepresentant(asiento) != null)
			params.setaltres(getIdentificacioRepresentant(asiento));
		
		DireccionCodificada direccionCodificada = getProcedenciaRepresentant(asiento);
		if( direccionCodificada != null )
		{
			String pais 		= (direccionCodificada.getPaisOrigen()!=null?direccionCodificada.getPaisOrigen():"");
			String provincia 	= direccionCodificada.getCodigoProvincia();
			String municipio 	= direccionCodificada.getCodigoMunicipio();
			
			String municipioBaleares = "";
			String municipioFuera = "";
			
			if ( CODIGO_PROVINCIA_CAIB.equals( provincia ) )
			{
				municipioBaleares = municipio;
			}
			else
			{
				// Si pertenece a España mostramos el municipio (provincia)    				
				if (	PAIS_ESPANYA.equals( pais.toUpperCase() ) 	|| 
						CODIGO_PAIS_ESPANYA.equals( pais.toUpperCase()) ||
						CODIGO_PAIS_ESPANYA_REDUCIDO.equals(pais.toUpperCase())
					)
    	    			municipioFuera = direccionCodificada.getNombreMunicipio() + "(" + direccionCodificada.getNombreProvincia()+ ")"; 
				else
						municipioFuera = direccionCodificada.getPaisOrigen();     						
			}
			
			params.setbalears(municipioBaleares);
			params.setfora(municipioFuera);
		}
		
		
		if (asiento.getDatosAsunto().getCodigoOrganoDestino() != null)
			params.setremitent(asiento.getDatosAsunto().getCodigoOrganoDestino());
		params.setidioex(getIdiomaAsiento(asiento));
		params.setcomentario(asiento.getDatosAsunto().getExtractoAsunto());
		
		return params;
	}
	
	private ResultadoRegistro realizarRegistroEntrada(ParametrosRegistroEntrada params) throws Exception{	
		logger.debug("realizarRegistroEntrada: inicio (usuario: " + params.getUsuario()  + ")");
		ParametrosRegistroEntrada respostaValidacio = validarRegistroEntrada(params);
		if (respostaValidacio.getValidado()) {
			logger.debug("realizarRegistroEntrada: pasa validación, pasamos a grabar");
			ParametrosRegistroEntrada respostaGrabacio = grabarRegistroEntrada(params);
			if (respostaGrabacio.getGrabado()) {
				ResultadoRegistro res = new ResultadoRegistro();
				res.setFechaRegistro(StringUtil.cadenaAFecha( params.getDataEntrada() + " " + params.getHora(), "dd/MM/yyyy HH:mm"));				
				
				// INDRA: CONCATENAMOS OFICINA A NUM REGISTRO
				//res.setNumeroRegistro(respostaGrabacio.getNumeroEntrada() + "/" + respostaGrabacio.getAnoEntrada());
				res.setNumeroRegistro(respostaGrabacio.getOficina() + "/" +  respostaGrabacio.getNumeroEntrada() + "/" + respostaGrabacio.getAnoEntrada());
				
				logger.debug("realizarRegistroEntrada: Num:" + res.getNumeroRegistro() + " Fecha:" + StringUtil.fechaACadena( res.getFechaRegistro(), StringUtil.FORMATO_TIMESTAMP));
				
				return res;
			} else {
				logger.debug("realizarRegistroEntrada: no se ha podido grabar");
				throw new Exception("La anotación de registro no se ha guardado");
			}
		} else {
			logger.debug("realizarRegistroEntrada: no pasa validación");
			Map errors = respostaValidacio.getErrores();
			StringBuilder sb = new StringBuilder();
			if (errors != null && errors.size() > 0) {
				for (Iterator it = errors.keySet().iterator(); it.hasNext();) {
					String camp = (String) it.next();
					sb.append("|[" + camp + "]:" + errors.get(camp));
				}
			} else {
				sb.append("No se han devuelto errores");
			}			
			throw new Exception("Errores de validación: " + sb.toString());
		}
	}
	
	private ParametrosRegistroEntrada mapeaAsientoParametrosRegistroEntrada(AsientoRegistral asiento) throws Exception{
		ParametrosRegistroEntrada params = new ParametrosRegistroEntrada();
		params.fijaUsuario(getUsuarioRegistro());
		params.setOrigenRegistro("SISTRA"); 	
		Date ara = new Date();
		DateFormat dfData = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat dfHora = new SimpleDateFormat("HH:mm");
		params.setdataentrada(dfData.format(ara));
		params.sethora(dfHora.format(ara));
		params.setdata(dfData.format(ara));
		
		String codiOficina = asiento.getDatosOrigen().getCodigoEntidadRegistralOrigen();
		String[] codisOficina = descomponerOficinaFisica(codiOficina);
		params.setoficina(codisOficina[0]);
		params.setoficinafisica(codisOficina[1]);
		params.settipo(asiento.getDatosAsunto().getTipoAsunto());
		params.setidioma(getIdiomaAsiento(asiento));
		if (getIdentificacioRepresentant(asiento) != null)
			params.setaltres(getIdentificacioRepresentant(asiento));
		
		DireccionCodificada direccionCodificada = getProcedenciaRepresentant(asiento);
		if( direccionCodificada != null )
		{
			String pais 		= (direccionCodificada.getPaisOrigen()!=null?direccionCodificada.getPaisOrigen():"");
			String provincia 	= direccionCodificada.getCodigoProvincia();
			String municipio 	= direccionCodificada.getCodigoMunicipio();
			
			String municipioBaleares = "";
			String municipioFuera = "";
			
			if ( CODIGO_PROVINCIA_CAIB.equals( provincia ) )
			{
				municipioBaleares = municipio;
			}
			else
			{
				// Si pertenece a España mostramos el municipio (provincia)    				
				if (	PAIS_ESPANYA.equals( pais.toUpperCase() ) 	|| 
						CODIGO_PAIS_ESPANYA.equals( pais.toUpperCase()) ||
						CODIGO_PAIS_ESPANYA_REDUCIDO.equals(pais.toUpperCase())
					)
    	    			municipioFuera = direccionCodificada.getNombreMunicipio() + "(" + direccionCodificada.getNombreProvincia()+ ")"; 
				else
						municipioFuera = direccionCodificada.getPaisOrigen();     						
			}
			
			params.setbalears(municipioBaleares);
			params.setfora(municipioFuera);
		}
		
		if (asiento.getDatosAsunto().getCodigoOrganoDestino() != null)
			params.setdestinatari(asiento.getDatosAsunto().getCodigoOrganoDestino());
		params.setidioex(getIdiomaAsiento(asiento));
		params.setcomentario(asiento.getDatosAsunto().getExtractoAsunto());
		return params;
	}
	
	
	private ParametrosRegistroEntrada grabarRegistroEntrada(ParametrosRegistroEntrada params) throws Exception {
		ParametrosRegistroEntrada resposta = null;
		LoginContext lc = null;
		try {
			lc = doLogin();
			Context ctx = getInitialContext();
			Object objRef = ctx.lookup("es.caib.regweb.logic.RegistroEntradaFacade");
			RegistroEntradaFacadeHome home = (RegistroEntradaFacadeHome)javax.rmi.PortableRemoteObject.narrow(
					objRef,
					RegistroEntradaFacadeHome.class);
			resposta = home.create().grabar(params);
			ctx.close();
		} finally {
			if (lc != null)
				lc.logout();
		}
		return resposta;
	}
	private ParametrosRegistroSalida validarRegistroSalida(ParametrosRegistroSalida params) throws Exception {
		ParametrosRegistroSalida resposta = null;
		LoginContext lc = null;
		try {
			lc = doLogin();
			Context ctx = getInitialContext();
			Object objRef = ctx.lookup("es.caib.regweb.logic.RegistroSalidaFacade");
			RegistroSalidaFacadeHome home = (RegistroSalidaFacadeHome)javax.rmi.PortableRemoteObject.narrow(
					objRef,
					RegistroSalidaFacadeHome.class);
			resposta = home.create().validar(params);
			ctx.close();
		} finally {
			if (lc != null)
				lc.logout();
		}
		return resposta;
	}
	private ParametrosRegistroSalida grabarRegistroSalida(ParametrosRegistroSalida params) throws Exception {
		ParametrosRegistroSalida resposta = null;
		LoginContext lc = null;
		try {
			lc = doLogin();
			Context ctx = getInitialContext();
			Object objRef = ctx.lookup("es.caib.regweb.logic.RegistroSalidaFacade");
			RegistroSalidaFacadeHome home = (RegistroSalidaFacadeHome)javax.rmi.PortableRemoteObject.narrow(
					objRef,
					RegistroSalidaFacadeHome.class);
			resposta = home.create().grabar(params);
			ctx.close();
		} finally {
			if (lc != null)
				lc.logout();
		}
		return resposta;
	}
	
	private Vector buscarOficinas(boolean filtroOficinaTelematicaUnica) throws Exception {
		
		// Comprobamos si esta configurada una unica oficina de registro telematico configurada en properties
		String oficina = getCodigoOficinaRegistroTelematica();
		Vector resposta = null;
		if (oficina != null  && filtroOficinaTelematicaUnica) {
			resposta = new Vector();
			String[] cods = descomponerOficinaFisica(oficina);
			String[] desc = descomponerOficinaFisica(getDescripcionOficinaRegistroTelematica());
			resposta.add(cods[0]);
			resposta.add(cods[1]);
			resposta.add(desc[0]);
			resposta.add(desc[1]);			
		} else {
			// Si no hay configurada una unica, devolvemos todas
			LoginContext lc = null;
			try {
				lc = doLogin();
				Context ctx = getInitialContext();
				Object objRef = ctx.lookup("es.caib.regweb.logic.ValoresFacade");
				ValoresFacadeHome home = (ValoresFacadeHome)javax.rmi.PortableRemoteObject.narrow(
						objRef,
						ValoresFacadeHome.class);
				resposta = home.create().buscarOficinasFisicasDescripcion("tots", "totes");
				ctx.close();
			} finally {
				if (lc != null)
					lc.logout();
			}
		}
		return resposta;	
	}
	
	
	private Vector buscarOficinasUsuario(String usuario) throws Exception {
		Vector resposta = null;
		LoginContext lc = null;
		try {
			lc = doLogin();
			Context ctx = getInitialContext();
			Object objRef = ctx.lookup("es.caib.regweb.logic.ValoresFacade");
			ValoresFacadeHome home = (ValoresFacadeHome)javax.rmi.PortableRemoteObject.narrow(
					objRef,
					ValoresFacadeHome.class);
			resposta = home.create().buscarOficinasFisicas(usuario,"AE");
			ctx.close();
		} finally {
			if (lc != null)
				lc.logout();
		}
		return resposta;
	}
	
	private Vector buscarDocumentos() throws Exception {
		Vector resposta = null;
		LoginContext lc = null;
		try {
			lc = doLogin();
			Context ctx = getInitialContext();
			Object objRef = ctx.lookup("es.caib.regweb.logic.ValoresFacade");
			ValoresFacadeHome home = (ValoresFacadeHome)javax.rmi.PortableRemoteObject.narrow(
					objRef,
					ValoresFacadeHome.class);
			resposta = home.create().buscarDocumentos();
			ctx.close();
		} finally {
			if (lc != null)
				lc.logout();
		}
		return resposta;
	}
	
	/*
	private Vector buscarDestinatarios(String oficinaCodigo) throws Exception {
		Vector resposta = null;
		LoginContext lc = null;
		try {
			lc = doLogin();
			Context ctx = getInitialContext();
			Object objRef = ctx.lookup("es.caib.regweb.logic.ValoresFacade");
			ValoresFacadeHome home = (ValoresFacadeHome)javax.rmi.PortableRemoteObject.narrow(
					objRef,
					ValoresFacadeHome.class);
			resposta = home.create().buscarDestinatarios(oficinaCodigo);
			ctx.close();
		} finally {
			if (lc != null)
				lc.logout();
		}
		return resposta;
	}
	*/
	
	private Vector buscarDestinatarios() throws Exception {
		Vector resposta = null;
		LoginContext lc = null;
		try {
			lc = doLogin();
			Context ctx = getInitialContext();
			Object objRef = ctx.lookup("es.caib.regweb.logic.ValoresFacade");
			ValoresFacadeHome home = (ValoresFacadeHome)javax.rmi.PortableRemoteObject.narrow(
					objRef,
					ValoresFacadeHome.class);
			resposta = home.create().buscarTodosDestinatarios();
			ctx.close();
		} finally {
			if (lc != null)
				lc.logout();
		}
		return resposta;
	}

	private LoginContext doLogin() throws Exception {
		LoginContext lc = null;
		String userName = getUsuarioRegistro();
		String password = getPasswdRegistro();
		if (userName != null && userName.length() > 0 && password != null && password.length() > 0) {
			lc = new LoginContext(
					"client-login",
					new UsernamePasswordCallbackHandler(userName, password));
			lc.login();
		}
		return lc;
	}
	
	private Context getInitialContext() throws Exception {		
		return EjbUtil.getInitialContext(false,getConfig().getProperty("plugin.regweb.url"));		
	}

	private Properties getConfig() throws Exception {
		if (config == null) {
			config = new Properties();
			
			// Path directorio de configuracion
       	 	String pathConf = System.getProperty("ad.path.properties");
       	 	
			// Propiedades globales
       	 	config.load(new FileInputStream(pathConf + "sistra/global.properties"));
			
       	 	// Propiedades plugin
			config.load(new FileInputStream(pathConf + "sistra/plugins/plugin-regtel.properties"));
		}
		return config; 
	}
	
	private String getUsuarioRegistro() throws Exception {
		String auto = getConfig().getProperty("plugin.regweb.auth.auto");
		String userName = null;
		if ("true".equals(auto)) {
			userName = getConfig().getProperty("auto.user");			
		} else {
			userName = getConfig().getProperty("plugin.regweb.auth.username");
		}
		return userName;
	}
	
	private String getPasswdRegistro() throws Exception {
		String auto = getConfig().getProperty("plugin.regweb.auth.auto");
		String userName = null;
		if ("true".equals(auto)) {
			userName = getConfig().getProperty("auto.pass");			
		} else {
			userName = getConfig().getProperty("plugin.regweb.auth.password");
		}
		return userName;		
	}

	
	private String[] descomponerOficinaFisica(String oficinaFisica) throws Exception {
		String[] codisOficina = oficinaFisica.split(REGEXP_SEPARADOR_OFICINA_FISICA);
		if (codisOficina.length != 2) {
			throw new Exception("Codigo de oficina incorrecto: " + oficinaFisica);
		}
		return codisOficina;
	}
	
	private String getCodigoOficinaRegistroTelematica() throws Exception {
		String oficina = getConfig().getProperty("plugin.regweb.oficinaTelematicaUnica.codigo");
		if (oficina != null) {
			oficina = oficina.trim();
		}
		return oficina;		
	}
	
	private String getDescripcionOficinaRegistroTelematica() throws Exception {
		String oficina = getConfig().getProperty("plugin.regweb.oficinaTelematicaUnica.descripcion");
		if (oficina != null) {
			oficina = oficina.trim();
		}
		return oficina;		
	}

}
