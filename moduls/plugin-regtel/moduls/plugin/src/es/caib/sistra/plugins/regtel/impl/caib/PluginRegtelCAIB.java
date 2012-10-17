package es.caib.sistra.plugins.regtel.impl.caib;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.redose.modelInterfaz.ReferenciaRDS;
import es.caib.sistra.plugins.regtel.OficinaRegistro;
import es.caib.sistra.plugins.regtel.PluginRegistroIntf;
import es.caib.sistra.plugins.regtel.ResultadoRegistro;
import es.caib.sistra.plugins.regtel.ServicioDestinatario;
import es.caib.sistra.plugins.regtel.TipoAsunto;
import es.caib.sistra.plugins.regtel.impl.caib.cxf.ClienteWS;
import es.caib.sistra.plugins.regtel.impl.caib.cxf.model.ErrorEntrada;
import es.caib.sistra.plugins.regtel.impl.caib.cxf.model.ErrorSalida;
import es.caib.sistra.plugins.regtel.impl.caib.cxf.model.ListaResultados;
import es.caib.sistra.plugins.regtel.impl.caib.cxf.model.ParametrosRegistroEntradaWS;
import es.caib.sistra.plugins.regtel.impl.caib.cxf.model.ParametrosRegistroSalidaWS;
import es.caib.sistra.plugins.regtel.impl.caib.cxf.services.RegwebFacade;
import es.caib.util.StringUtil;
import es.caib.xml.registro.factoria.impl.AsientoRegistral;
import es.caib.xml.registro.factoria.impl.DatosInteresado;
import es.caib.xml.registro.factoria.impl.DireccionCodificada;
import es.caib.xml.registro.factoria.impl.Justificante;

/**
 * Implementacio del plugin de registre que empra la interfi­cie
 * d'EJBs logic del registre de la CAIB.
 * 
 */
public class PluginRegtelCAIB implements PluginRegistroIntf {

	private static final Log logger = LogFactory.getLog(PluginRegtelCAIB.class);
	
	private Properties config = null;
	
	private static String CODIGO_PROVINCIA_CAIB = "7";
	private static String PAIS_ESPANYA = "ESPAÑA";
	
	public final static String CODIGO_PAIS_ESPANYA = "ESP";
	public final static String CODIGO_PAIS_ESPANYA_REDUCIDO = "ES";
	
	public final static String SEPARADOR_OFICINA_FISICA = ".";
	public final static String REGEXP_SEPARADOR_OFICINA_FISICA = "\\."; // Exp regular para hacer split
	
	
	public ResultadoRegistro registroEntrada(
			AsientoRegistral asiento,
			ReferenciaRDS refAsiento,
			Map refAnexos) throws Exception {
		
		if (isPrintPeticio())
			logParametres(asiento, refAsiento, refAnexos);
		
		
		ParametrosRegistroEntradaWS params = mapeaAsientoParametrosRegistroEntrada(asiento); 
		
		ResultadoRegistro res = realizarRegistroEntrada(params);
		
		return res;
		
	}
	
	public ResultadoRegistro registroSalida(
			AsientoRegistral asiento,
			ReferenciaRDS refAsiento,
			Map refAnexos) throws Exception {
		
		if (isPrintPeticio())
			logParametres(asiento, refAsiento, refAnexos);
		
		ParametrosRegistroSalidaWS params = mapearAsientoParametrosRegistroSalida(asiento);
		
		ResultadoRegistro res = realizarRegistroSalida(params);
		
		return res;
		
	}

	

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
		ParametrosRegistroEntradaWS params = mapeaAsientoParametrosRegistroEntrada(justificantePreregistro.getAsientoRegistral()); 
		
		// Particularizamos campos para preregistro
		if(codigoProvincia != null && CODIGO_PROVINCIA_CAIB.equals(codigoProvincia)){
			params.setBalears(codigoMunicipio);
			params.setFora("");
		}else{
			params.setBalears("");
			params.setFora(descripcionMunicipio);			
		}
		params.setComentario("Confirmació preregistre "+justificantePreregistro.getNumeroRegistro()+" - "+params.getComentario());
				
		// Registramos
		ResultadoRegistro res = realizarRegistroEntrada(params);
		
		return res;
				
	}

	public List obtenerOficinasRegistro() {
		return obtenerOficinasRegistro(null);
	}

	public List obtenerOficinasRegistroUsuario(String usuario) {
		if (usuario == null){
			logger.error("No se ha indicado usuario. Devolvemos lista vacía.");
			return new ArrayList();
		}
		return obtenerOficinasRegistro(usuario);
	}

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
				lista.add(sd);
			}
		} catch (Exception ex) {
			// Si hi ha algun error no tornam cap servei
			logger.error("Error al obtener los servicios destinatarios", ex);
		}
		return lista;
				
	}
	
    public void anularRegistroEntrada(String numeroRegistro, Date fechaRegistro) throws Exception {
		
		logger.debug("accediendo a anular registro entrada: " + numeroRegistro);
						
		// Extraemos info del num de registro
		String [] tokens = numeroRegistro.split("/");			
		String codiOficina = tokens[0];
		String numero = tokens[1];
		String ano = tokens[2];
	
		// Establecemos parametros
		ParametrosRegistroEntradaWS registro = new ParametrosRegistroEntradaWS();
		registro.setUsuario(getUsuarioRegistro());
		registro.setPassword(getPasswdRegistro());
		registro.setOrigenRegistro("SISTRA"); 
		registro.setOficina(codiOficina);
		registro.setNumeroEntrada(numero); 			
		registro.setAnoEntrada(ano);
		
		// Conectamos a Registro
		RegwebFacade regent = obtenerClienteWS();
		if(regent.anularEntrada(registro,true)) {
			logger.debug("Registro anulado");
		} else { 
			throw new Exception("No se ha podido anular el registro"); 
		}
												

	}

	public void anularRegistroSalida(String numeroRegistro, Date fechaRegistro) throws Exception {
		logger.debug("accediendo a anular registro salida: " + numeroRegistro);
	
		// Extraemos info del num de registro
		String [] tokens = numeroRegistro.split("/");
		String codiOficina = tokens[0];
		String numero = tokens[1];
		String ano = tokens[2];
	
		// Establecemos parametros
		ParametrosRegistroSalidaWS registro = new ParametrosRegistroSalidaWS();
		registro.setUsuario(getUsuarioRegistro());
		registro.setPassword(getPasswdRegistro());
		registro.setAnoSalida(ano); 
		registro.setNumeroSalida(numero); 
		registro.setOficina(codiOficina);
		
		// Conectamos a Registro
		RegwebFacade regent = obtenerClienteWS();
		if(regent.anularSalida(registro,true)){
			logger.debug("Registro anulado"); 
		} else { 
			throw new Exception("No se ha podido anular el registro");
		}	
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
				v = buscarOficinas();
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
		for (DatosInteresado datosInteresado: (List<DatosInteresado>)asiento.getDatosInteresado()) {
			if ("RPT".equals(datosInteresado.getTipoInteresado()))
				return datosInteresado.getIdentificacionInteresado();
		}
		return null;
	}
	private DireccionCodificada getProcedenciaRepresentant(AsientoRegistral asiento) {
		for (DatosInteresado datosInteresado: (List<DatosInteresado>)asiento.getDatosInteresado()) {
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
			for (DatosInteresado datosInteresado: (List<DatosInteresado>)asiento.getDatosInteresado()) {
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
			for (Object key: refAnexos.keySet()) {
				ReferenciaRDS refRDS = (ReferenciaRDS)refAnexos.get(key);
				logger.info("  refAnexos[" + key + "].codigo: " + refRDS.getCodigo());
				logger.info("  refAnexos[" + key + "].clave: " + refRDS.getClave());
			}
		}
	}


	private ParametrosRegistroEntradaWS validarRegistroEntrada(ParametrosRegistroEntradaWS params) throws Exception {
		RegwebFacade regweb = obtenerClienteWS();
		return regweb.validarEntrada(params);
	}
	
	private ResultadoRegistro realizarRegistroSalida (
			ParametrosRegistroSalidaWS params) throws Exception {
		logger.debug("realizarRegistroSalida: inicio");
		
		ParametrosRegistroSalidaWS respostaValidacio = validarRegistroSalida(params);
		if (respostaValidacio.isValidado()) {
			ParametrosRegistroSalidaWS respostaGrabacio = grabarRegistroSalida(params);
			if (respostaGrabacio.isRegistroSalidaGrabado()) {
				ResultadoRegistro res = new ResultadoRegistro();
				res.setFechaRegistro(StringUtil.cadenaAFecha( params.getDatasalida() + " " + params.getHora(), "dd/MM/yyyy HH:mm"));
				
				// INDRA: CONCATENAMOS OFICINA A NUM REGISTRO
				//res.setNumeroRegistro(respostaGrabacio.getNumeroSalida() + "/" + respostaGrabacio.getAnoSalida());				
				res.setNumeroRegistro(respostaGrabacio.getOficina() + "/" + respostaGrabacio.getNumeroSalida() + "/" + respostaGrabacio.getAnoSalida());				
				
				return res;
			} else {
				throw new Exception("La anotación de registro no se ha guardado");
			}
		} else {
			String error = "No se indica el error";
			if (respostaValidacio.getErrores() != null && respostaValidacio.getErrores().getErrores() != null && respostaValidacio.getErrores().getErrores().size() > 0) {
				StringBuilder sb = new StringBuilder();
				for (Iterator it = respostaValidacio.getErrores().getErrores().iterator(); it.hasNext();) {
					ErrorSalida errorSalida = (ErrorSalida) it.next();
					sb.append("|[" + errorSalida.getCodigo() + "]:" + errorSalida.getDescripcion());
				}
				error = sb.toString();				
			}
			throw new Exception("Errores de validación: " + error );
		}		
	}

	private ParametrosRegistroSalidaWS mapearAsientoParametrosRegistroSalida(
			AsientoRegistral asiento) throws Exception {
		ParametrosRegistroSalidaWS params = new ParametrosRegistroSalidaWS();
		params.setUsuario(getUsuarioRegistro());
		params.setPassword(getPasswdRegistro());
		//params.setOrigenRegistro("SISTRA"); 
		Date ara = new Date();
		DateFormat dfData = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat dfHora = new SimpleDateFormat("HH:mm");
		params.setDatasalida(dfData.format(ara));
		params.setHora(dfHora.format(ara));
		params.setData(dfData.format(ara));
		
		String codiOficina = asiento.getDatosOrigen().getCodigoEntidadRegistralOrigen();
		String[] codisOficina = descomponerOficinaFisica(codiOficina);
		params.setOficina(codisOficina[0]);
		params.setOficinafisica(codisOficina[1]);
		
		params.setTipo(asiento.getDatosAsunto().getTipoAsunto());
		params.setIdioma(getIdiomaAsiento(asiento));
		if (getIdentificacioRepresentant(asiento) != null)
			params.setAltres(getIdentificacioRepresentant(asiento));
		
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
			
			params.setBalears(municipioBaleares);
			params.setFora(municipioFuera);
		}
		
		
		if (asiento.getDatosAsunto().getCodigoOrganoDestino() != null)
			params.setRemitent(asiento.getDatosAsunto().getCodigoOrganoDestino());
		params.setIdioex(getIdiomaAsiento(asiento));
		params.setComentario(asiento.getDatosAsunto().getExtractoAsunto());
		
		return params;
	}
	
	private ResultadoRegistro realizarRegistroEntrada(ParametrosRegistroEntradaWS params) throws Exception{	
		logger.debug("realizarRegistroEntrada: inicio");
		ParametrosRegistroEntradaWS respostaValidacio = validarRegistroEntrada(params);
		if (respostaValidacio.isValidado()) {
			logger.debug("realizarRegistroEntrada: pasa validación, pasamos a grabar");
			ParametrosRegistroEntradaWS respostaGrabacio = grabarRegistroEntrada(params);
			if (respostaGrabacio.isRegistroGrabado() != null && respostaGrabacio.isRegistroGrabado().booleanValue()) {
				ResultadoRegistro res = new ResultadoRegistro();
				res.setFechaRegistro(StringUtil.cadenaAFecha( params.getDataentrada() + " " + params.getHora(), "dd/MM/yyyy HH:mm"));				
				
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
			String error = "No se indica el error";
			if (respostaValidacio.getErrores() != null && respostaValidacio.getErrores().getErrores() != null && respostaValidacio.getErrores().getErrores().size() > 0) {
				StringBuilder sb = new StringBuilder();
				for (Iterator it = respostaValidacio.getErrores().getErrores().iterator(); it.hasNext();) {
					ErrorEntrada errorEntrada = (ErrorEntrada) it.next();
					sb.append("|[" + errorEntrada.getCodigo() + "]:" + errorEntrada.getDescripcion());
				}
				error = sb.toString();				
			}
			throw new Exception("Errores de validación: " + error );			
		}
	}
	
	private ParametrosRegistroEntradaWS mapeaAsientoParametrosRegistroEntrada(AsientoRegistral asiento) throws Exception{
		ParametrosRegistroEntradaWS params = new ParametrosRegistroEntradaWS();
		params.setUsuario(getUsuarioRegistro());
		params.setPassword(getPasswdRegistro());
		params.setOrigenRegistro("SISTRA"); 	
		Date ara = new Date();
		DateFormat dfData = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat dfHora = new SimpleDateFormat("HH:mm");
		params.setDataentrada(dfData.format(ara));
		params.setHora(dfHora.format(ara));
		params.setData(dfData.format(ara));
		
		String codiOficina = asiento.getDatosOrigen().getCodigoEntidadRegistralOrigen();
		String[] codisOficina = descomponerOficinaFisica(codiOficina);
		params.setOficina(codisOficina[0]);
		params.setOficinafisica(codisOficina[1]);
		
		params.setTipo(asiento.getDatosAsunto().getTipoAsunto());
		params.setIdioma(getIdiomaAsiento(asiento));
		if (getIdentificacioRepresentant(asiento) != null)
			params.setAltres(getIdentificacioRepresentant(asiento));
		
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
			
			params.setBalears(municipioBaleares);
			params.setFora(municipioFuera);
		}
		
		if (asiento.getDatosAsunto().getCodigoOrganoDestino() != null)
			params.setDestinatari(asiento.getDatosAsunto().getCodigoOrganoDestino());
		params.setIdioex(getIdiomaAsiento(asiento));
		params.setComentario(asiento.getDatosAsunto().getExtractoAsunto());
		return params;
	}
	
	
	private ParametrosRegistroEntradaWS grabarRegistroEntrada(ParametrosRegistroEntradaWS params) throws Exception {
		RegwebFacade regweb = obtenerClienteWS();
		ParametrosRegistroEntradaWS resposta = regweb.grabarEntrada(params);
		return resposta;
	}
	
	private ParametrosRegistroSalidaWS validarRegistroSalida(ParametrosRegistroSalidaWS params) throws Exception {
		RegwebFacade regweb = obtenerClienteWS();
		ParametrosRegistroSalidaWS resposta = regweb.validarSalida(params);			
		return resposta;
	}
	
	private ParametrosRegistroSalidaWS grabarRegistroSalida(ParametrosRegistroSalidaWS params) throws Exception {
		RegwebFacade regweb = obtenerClienteWS();
		ParametrosRegistroSalidaWS resposta = regweb.grabarSalida(params);			
		return resposta;		
	}
	
	private Vector buscarOficinas() throws Exception {
		
		// Comprobamos si esta configurada una unica oficina de registro telematico configurada en properties
		String oficina = getCodigoOficinaRegistroTelematica();
		Vector resposta = null;
		if (oficina != null) {
			resposta = new Vector();
			String[] cods = descomponerOficinaFisica(oficina);
			String[] desc = descomponerOficinaFisica(getDescripcionOficinaRegistroTelematica());
			resposta.add(cods[0]);
			resposta.add(cods[1]);
			resposta.add(desc[0]);
			resposta.add(desc[1]);			
		} else {
			// Si no hay configurada una unica, devolvemos todas
			RegwebFacade regweb = obtenerClienteWS();
			ListaResultados resp = regweb.buscarOficinasFisicasDescripcion(getUsuarioRegistro(), getPasswdRegistro(), "tots", "totes");
			resposta = new Vector();
			if (resp.getResultado() != null && resp.getResultado().size() > 0) { 
				for (int i = 0; i < resp.getResultado().size(); i++) {
					resposta.add(resp.getResultado().get(i));
				}
			}
		}
		return resposta;	
	}
	
	
	private Vector buscarOficinasUsuario(String usuario) throws Exception {
		RegwebFacade regweb = obtenerClienteWS();
		ListaResultados resp = regweb.buscarOficinasFisicas(getUsuarioRegistro(), getPasswdRegistro(), usuario, "AE");
		Vector resposta = new Vector();
		if (resp.getResultado() != null && resp.getResultado().size() > 0) { 
			for (int i = 0; i < resp.getResultado().size(); i++) {
				resposta.add(resp.getResultado().get(i));
			}
		}
		return resposta;
	}
	
	private Vector buscarDocumentos() throws Exception {
		RegwebFacade regweb = obtenerClienteWS();
		ListaResultados resp = regweb.buscarDocumentos(getUsuarioRegistro(), getPasswdRegistro());
		Vector resposta = new Vector();
		if (resp.getResultado() != null && resp.getResultado().size() > 0) { 
			for (int i = 0; i < resp.getResultado().size(); i++) {
				resposta.add(resp.getResultado().get(i));
			}
		}
		return resposta;				
	}	
	
	private Vector buscarDestinatarios() throws Exception {
		RegwebFacade regweb = obtenerClienteWS();
		ListaResultados resp = regweb.buscarTodosDestinatarios(getUsuarioRegistro(), getPasswdRegistro());
		Vector resposta = new Vector();
		if (resp.getResultado() != null && resp.getResultado().size() > 0) { 
			for (int i = 0; i < resp.getResultado().size(); i++) {
				resposta.add(resp.getResultado().get(i));
			}
		}
		return resposta;				
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
	
	private RegwebFacade obtenerClienteWS() throws Exception {
		return ClienteWS.generarPort(getConfig().getProperty("plugin.regweb.url"), getUsuarioRegistro(), getPasswdRegistro());		
	}
}
