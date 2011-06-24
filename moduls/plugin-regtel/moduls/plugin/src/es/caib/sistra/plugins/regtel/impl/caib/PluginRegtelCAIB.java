package es.caib.sistra.plugins.regtel.impl.caib;

import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

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
import es.caib.sistra.plugins.regtel.PluginRegistroIntf;
import es.caib.sistra.plugins.regtel.ResultadoRegistro;
import es.caib.sistra.plugins.regtel.ServicioDestinatario;
import es.caib.sistra.plugins.regtel.TipoAsunto;
import es.caib.util.EjbUtil;
import es.caib.xml.registro.factoria.impl.AsientoRegistral;
import es.caib.xml.registro.factoria.impl.DatosInteresado;
import es.caib.xml.registro.factoria.impl.Justificante;

/**
 * Implementacio del plugin de registre que empra la interfi�cie
 * d'EJBs logic del registre de la CAIB.
 * 
 */
@SuppressWarnings("unchecked")
public class PluginRegtelCAIB implements PluginRegistroIntf {

	private static final Log logger = LogFactory.getLog(PluginRegtelCAIB.class);
	
	private Properties config = null;



	public ResultadoRegistro registroEntrada(
			AsientoRegistral asiento,
			ReferenciaRDS refAsiento,
			Map refAnexos) throws Exception {
		if (isPrintPeticio())
			logParametres(asiento, refAsiento, refAnexos);
		ParametrosRegistroEntrada params = new ParametrosRegistroEntrada();
		params.fijaUsuario(getUsuarioSeycon(asiento));
		Date ara = new Date();
		DateFormat dfData = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat dfHora = new SimpleDateFormat("HH:mm");
		params.setdataentrada(dfData.format(ara));
		params.sethora(dfHora.format(ara));
		String codiOficina = asiento.getDatosOrigen().getCodigoEntidadRegistralOrigen();
		String[] codisOficina = codiOficina.split("-");
		if (codisOficina.length == 2) {
			params.setoficina(codisOficina[0]);
			params.setoficinafisica(codisOficina[1]);
		} else {
			throw new Exception("Codi d'oficina incorrecte");
		}
		params.setdata(dfData.format(ara));
		params.settipo(asiento.getDatosAsunto().getTipoAsunto());
		params.setidioma(getIdiomaAsiento(asiento));
		if (getIdentificacioRepresentant(asiento) != null)
			params.setaltres(getIdentificacioRepresentant(asiento));
		if (getProcedenciaRepresentant(asiento) != null)
			params.setfora(getProcedenciaRepresentant(asiento));
		if (asiento.getDatosAsunto().getCodigoOrganoDestino() != null)
			params.setdestinatari(asiento.getDatosAsunto().getCodigoOrganoDestino());
		params.setidioex(getIdiomaAsiento(asiento));
		params.setcomentario(asiento.getDatosAsunto().getExtractoAsunto());
		ParametrosRegistroEntrada respostaValidacio = validarRegistroEntrada(params);
		if (respostaValidacio.getValidado()) {
			ParametrosRegistroEntrada respostaGrabacio = grabarRegistroEntrada(params);
			if (respostaGrabacio.getGrabado()) {
				ResultadoRegistro res = new ResultadoRegistro();
				res.setFechaRegistro(ara);
				res.setNumeroRegistro(respostaGrabacio.getNumeroEntrada() + "/" + respostaGrabacio.getAnoEntrada());
				return res;
			} else {
				throw new Exception("La anotaci� de registre no s'ha guardat");
			}
		} else {
			Map<String, String> errors = respostaValidacio.getErrores();
			StringBuilder sb = new StringBuilder();
			for (String camp: errors.keySet())
				sb.append("|[" + camp + "]:" + errors.get(camp));
			throw new Exception("Errors de validaci�: " + sb.toString());
		}
	}

	public ResultadoRegistro registroSalida(
			AsientoRegistral asiento,
			ReferenciaRDS refAsiento,
			Map refAnexos) throws Exception {
		if (isPrintPeticio())
			logParametres(asiento, refAsiento, refAnexos);
		ParametrosRegistroSalida params = new ParametrosRegistroSalida();
		params.fijaUsuario(getUsuarioSeycon(asiento));
		Date ara = new Date();
		DateFormat dfData = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat dfHora = new SimpleDateFormat("HH:mm");
		params.setdatasalida(dfData.format(ara));
		params.sethora(dfHora.format(ara));
		String codiOficina = asiento.getDatosOrigen().getCodigoEntidadRegistralOrigen();
		String[] codisOficina = codiOficina.split("-");
		if (codisOficina.length == 2) {
			params.setoficina(codisOficina[0]);
			params.setoficinafisica(codisOficina[1]);
		} else {
			throw new Exception("Codi d'oficina incorrecte");
		}
		params.setdata(dfData.format(ara));
		params.settipo(asiento.getDatosAsunto().getTipoAsunto());
		params.setidioma(getIdiomaAsiento(asiento));
		if (getIdentificacioRepresentant(asiento) != null)
			params.setaltres(getIdentificacioRepresentant(asiento));
		if (getProcedenciaRepresentant(asiento) != null)
			params.setfora(getProcedenciaRepresentant(asiento));
		if (asiento.getDatosAsunto().getCodigoOrganoDestino() != null)
			params.setremitent(asiento.getDatosAsunto().getCodigoOrganoDestino());
		params.setidioex(getIdiomaAsiento(asiento));
		params.setcomentario(asiento.getDatosAsunto().getExtractoAsunto());
		ParametrosRegistroSalida respostaValidacio = validarRegistroSalida(params);
		if (respostaValidacio.getValidado()) {
			ParametrosRegistroSalida respostaGrabacio = grabarRegistroSalida(params);
			if (respostaGrabacio.getGrabado()) {
				ResultadoRegistro res = new ResultadoRegistro();
				res.setFechaRegistro(ara);
				res.setNumeroRegistro(respostaGrabacio.getNumeroSalida() + "/" + respostaGrabacio.getAnoSalida());
				return res;
			} else {
				throw new Exception("La anotaci� de registre no s'ha guardat");
			}
		} else {
			Map<String, String> errors = respostaValidacio.getErrores();
			StringBuilder sb = new StringBuilder();
			for (String camp: errors.keySet())
				sb.append("|[" + camp + "]:" + errors.get(camp));
			throw new Exception("Errors de validaci�: " + sb.toString());
		}
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
		/*ResultadoRegistro res = new ResultadoRegistro();
		Date fc = new Date();
		res.setFechaRegistro(fc);
		res.setNumeroRegistro("LP/"+fc.getTime()+"/"+ Calendar.getInstance().get(Calendar.YEAR));
		return res;*/
		throw new Exception("Operaci� no suportada pel plugin de registre");
	}

	public List obtenerOficinasRegistro() {
		List lista = new ArrayList();
		try {
			Vector v = buscarOficinas();
			Iterator it = v.iterator();
			while (it.hasNext()) {
				OficinaRegistro of = new OficinaRegistro();
				String codiOficina = (String)it.next();
				String codiOficinaFisica = (String)it.next();
				String nomOficinaFisica = (String)it.next();
				String nomOficina = (String)it.next();
				of.setCodigo(codiOficina + "-" + codiOficinaFisica);
				of.setDescripcion(nomOficinaFisica + " (" + nomOficina + ")");
				lista.add(of);
			}
		} catch (Exception ex) {
			// Si hi ha algun error no tornam cap oficina
			logger.error("Error al obtenir les oficines de registre", ex);
		}
		return lista;
	}

	public List obtenerOficinasRegistroUsuario(String usuario) {
		return obtenerOficinasRegistro();
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
			logger.error("Error al obtenir els tipus de document", ex);
		}
		return lista;
	}

	public List obtenerServiciosDestino() {
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
	}



	private boolean isPrintPeticio() throws Exception {
		String printPeticio = getConfig().getProperty("plugin.regweb.print.peticio");
		if (printPeticio != null && "true".equals(printPeticio))
			return true;
		return false;
	}
	private String getUsuarioSeycon(AsientoRegistral asiento) throws Exception {
		String userName = getConfig().getProperty("plugin.regweb.auth.username");
		if (userName != null && userName.length() > 0)
			return userName;
		for (DatosInteresado datosInteresado: (List<DatosInteresado>)asiento.getDatosInteresado()) {
			if ("RPT".equals(datosInteresado.getTipoInteresado()))
				return datosInteresado.getUsuarioSeycon();
		}
		return null;
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
	private String getProcedenciaRepresentant(AsientoRegistral asiento) {
		for (DatosInteresado datosInteresado: (List<DatosInteresado>)asiento.getDatosInteresado()) {
			if ("RPT".equals(datosInteresado.getTipoInteresado()))
				return datosInteresado.getDireccionCodificada().getNombreMunicipio();
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
	private Vector buscarOficinas() throws Exception {
		Vector resposta = null;
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

	private LoginContext doLogin() throws Exception {
		LoginContext lc = null;
		String userName = getConfig().getProperty("plugin.regweb.auth.username");
		String password = getConfig().getProperty("plugin.regweb.auth.password");
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
		
		/*
		Properties props = new Properties();
		props.put(
				Context.INITIAL_CONTEXT_FACTORY,
				getConfig().getProperty("plugin.regweb.initial.context.factory"));
		props.put(
				Context.URL_PKG_PREFIXES,
				getConfig().getProperty("plugin.regweb.url.pkg.prefixes"));
		props.put(
				Context.PROVIDER_URL,
				getConfig().getProperty("plugin.regweb.provider.url"));
		String principal = getConfig().getProperty("plugin.regweb.security.principal");
		if (principal != null && principal.length() > 0)
			props.put(
					Context.SECURITY_PRINCIPAL,
					principal);
		String credentials = getConfig().getProperty("plugin.regweb.security.credentials");
		if (credentials != null && credentials.length() > 0)
			props.put(
					Context.SECURITY_CREDENTIALS,
					credentials); 
		return new InitialContext(props);
		*/
	}

	private Properties getConfig() throws Exception {
		if (config == null) {
			config = new Properties();
			config.load(new FileInputStream(
					System.getProperty("ad.path.properties") +
					"sistra/plugins/plugin-regtel.properties"));
		}
		return config; 
	}

	


	/**
	 * FUNCIONES A�ADIDAS AL PLUGIN ORIGINAL
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
			registro.fijaUsuario(getUsuarioSeycon(null));
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
			registro.fijaUsuario(getUsuarioSeycon(null));
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

}
