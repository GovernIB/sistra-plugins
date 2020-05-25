package es.caib.sistra.plugins.regtel.impl.caib;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.ws.soap.SOAPFaultException;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.redose.modelInterfaz.DocumentoRDS;
import es.caib.redose.modelInterfaz.ReferenciaRDS;
import es.caib.redose.persistence.delegate.DelegateRDSUtil;
import es.caib.redose.persistence.delegate.RdsDelegate;
import es.caib.regweb3.ws.api.v3.AnexoWs;
import es.caib.regweb3.ws.api.v3.AsientoRegistralWs;
import es.caib.regweb3.ws.api.v3.DatosInteresadoWs;
import es.caib.regweb3.ws.api.v3.InteresadoWs;
import es.caib.regweb3.ws.api.v3.JustificanteReferenciaWs;
import es.caib.regweb3.ws.api.v3.JustificanteWs;
import es.caib.regweb3.ws.api.v3.LibroOficinaWs;
import es.caib.regweb3.ws.api.v3.RegWebAsientoRegistralWs;
import es.caib.regweb3.ws.api.v3.RegWebInfoWs;
import es.caib.regweb3.ws.api.v3.TipoAsuntoWs;
import es.caib.regweb3.ws.api.v3.WsI18NException;
import es.caib.sistra.plugins.firma.FirmaIntf;
import es.caib.sistra.plugins.firma.PluginFirmaIntf;
import es.caib.sistra.plugins.regtel.ConstantesPluginRegistro;
import es.caib.sistra.plugins.regtel.OficinaRegistro;
import es.caib.sistra.plugins.regtel.PluginRegistroIntf;
import es.caib.sistra.plugins.regtel.ResultadoRegistro;
import es.caib.sistra.plugins.regtel.ServicioDestinatario;
import es.caib.sistra.plugins.regtel.TipoAsunto;
import es.caib.xml.datospropios.factoria.FactoriaObjetosXMLDatosPropios;
import es.caib.xml.datospropios.factoria.ServicioDatosPropiosXML;
import es.caib.xml.datospropios.factoria.impl.DatosPropios;
import es.caib.xml.registro.factoria.ConstantesAsientoXML;
import es.caib.xml.registro.factoria.impl.AsientoRegistral;
import es.caib.xml.registro.factoria.impl.DatosAnexoDocumentacion;
import es.caib.xml.registro.factoria.impl.DatosInteresado;
import es.caib.xml.registro.factoria.impl.Justificante;

/**
 * Implementacio del plugin de registre que empra la interfi�cie
 * d'EJBs logic del registre de la CAIB.
 *
 */
public class PluginRegweb3 implements PluginRegistroIntf {

	private static final Log logger = LogFactory.getLog(PluginRegweb3.class);
	private static final String ERROR = "javax.xml.ws.soap.SOAPFaultException: Caller unauthorized";
	private static final String ORGANOS_DESTINO = "ORGANOS_DESTINO";

	private int getMaxReintentos() {
        return new Integer(Integer.parseInt(StringUtils.defaultIfEmpty(ConfiguracionRegweb3.getInstance().getProperty("regweb3.reintentos"), "0")));
	}

	private String getFechaInicioJustificante() {
        return new String(StringUtils.defaultIfEmpty(ConfiguracionRegweb3.getInstance().getProperty("regweb3.justificante.fechaInicio"), ""));
	}

	/** {@inheritDoc} */
	public ResultadoRegistro registroEntrada(
			AsientoRegistral asiento,
			ReferenciaRDS refAsiento,
			Map refAnexos) throws Exception {

		int maxIntentos = getMaxReintentos();
		String id = "" + System.currentTimeMillis();

		// Obtiene entidad
		String entidad =  UtilsRegweb3.obtenerEntidadAsiento(asiento);

		// Mapea parametros ws
		 AsientoRegistralWs paramEntrada = mapearParametrosRegistro(entidad, asiento, refAsiento, refAnexos);

		// Invoca a Regweb3
		final RegWebAsientoRegistralWs service = UtilsRegweb3.getAsientoRegistralService(entidad);

		AsientoRegistralWs result = null;
		for (int reintentos = 0; reintentos <= maxIntentos; reintentos++) {
			try{

				// TODO PENDIENTE GESTION REINTENTOS SOBRE SESION REGISTRO
				String idSesionRegistro = generarSesionRegweb(entidad);

				// Obtiene si esta configurado distribucion y generar justificante
				boolean distribuir = Boolean.parseBoolean(StringUtils.defaultIfEmpty(ConfiguracionRegweb3.getInstance().getProperty("regweb3.entrada.distribuir"),"false"));
				boolean generarJustificante = Boolean.parseBoolean(StringUtils.defaultIfEmpty(ConfiguracionRegweb3.getInstance().getProperty("regweb3.entrada.generarJustificante"),"false"));

				// Crea asiento registral
				result  = service.crearAsientoRegistral(Long.parseLong(idSesionRegistro),
						entidad, paramEntrada, null, generarJustificante, distribuir);
	            break;
			}catch (SOAPFaultException e){
				String stackTrace = es.caib.util.StringUtil.stackTraceToString(e);
				if(maxIntentos > 0 && stackTrace.indexOf(ERROR) != -1){
					logger.debug("Registro de entrada " + id + " reintento n�mero " + reintentos + " erron�o: " + e.getMessage());
		            continue;
				} else{
					logger.error("Error realizando registro de entrada: " + e.getMessage(), e);
				}
			}
		}

		// Devuelve resultado registro
		ResultadoRegistro resReg = new ResultadoRegistro();
		resReg.setFechaRegistro(result.getFechaRegistro());
		resReg.setNumeroRegistro(result.getNumeroRegistroFormateado());
		return resReg;
	}


	/** {@inheritDoc} */
	public byte[] obtenerJustificanteRegistroEntrada(String entidad, String numeroRegistro,
			Date fechaRegistro) throws Exception {

		byte[] resultado = null;
		Date fechaInicioFormateada = null;

		int maxIntentos = getMaxReintentos();
		String fechaInicio = getFechaInicioJustificante();

		DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

		if (!StringUtils.isEmpty(fechaInicio)){
			try {
				fechaInicioFormateada = formatoFecha.parse(getFechaInicioJustificante());
			} catch (ParseException e) {
				logger.error("Error al formatear fecha inicio de justificante de registro: " + e.getMessage(), e);
			}

			if (!fechaRegistro.before(fechaInicioFormateada)){
				// Invoca a Regweb3
				final RegWebAsientoRegistralWs service = UtilsRegweb3.getAsientoRegistralService(entidad);

				JustificanteWs result = new JustificanteWs();

				for (int reintentos = 0; reintentos <= maxIntentos; reintentos++) {
					try{
						result = service.obtenerJustificante(entidad, numeroRegistro, ConstantesRegweb3.REGISTRO_ENTRADA);
					    break;
					}catch (SOAPFaultException e){
						String stackTrace = es.caib.util.StringUtil.stackTraceToString(e);
						if(maxIntentos > 0 && stackTrace.indexOf(ERROR) != -1){
							logger.debug("Obtenci�n de justificante de registro de entrada " + numeroRegistro + " reintento n�mero " + reintentos + " erron�o: " + e.getMessage());
					        continue;
						} else{
							logger.error("Error obteniendo justificante de registro de entrada: " + e.getMessage(), e);
						}
					}catch (WsI18NException ex){
						logger.error("Error obteniendo justificante de registro de entrada: " + ex.getMessage(), ex);
						throw new Exception ("S'ha produit un error al descarregar el justificant de registre. Per favor, tornau a provar-ho passats uns minuts.");
					}
				}

				resultado = result.getJustificante();
			}

		}


		return resultado;
	}


	/** {@inheritDoc} */
	public ResultadoRegistro registroSalida(
			AsientoRegistral asiento,
			ReferenciaRDS refAsiento,
			Map refAnexos) throws Exception {

		int maxIntentos = getMaxReintentos();
		String id = "" + System.currentTimeMillis();

		// Obtiene entidad
		String entidad = UtilsRegweb3.obtenerEntidadAsiento(asiento);

		// Mapea parametros ws
		AsientoRegistralWs paramEntrada = mapearParametrosRegistro(entidad, asiento, refAsiento, refAnexos);



		// Invoca a Regweb3
		AsientoRegistralWs result = null;
		final RegWebAsientoRegistralWs service = UtilsRegweb3.getAsientoRegistralService(entidad);

		for (int reintentos = 0; reintentos <= maxIntentos; reintentos++) {
			try{

				// TODO PENDIENTE GESTION REINTENTOS SOBRE SESION REGISTRO
				String idSesionRegistro = generarSesionRegweb(entidad);

				// Obtiene si esta configurado distribucion y generar justificante
				boolean distribuir = Boolean.parseBoolean(StringUtils.defaultIfEmpty(ConfiguracionRegweb3.getInstance().getProperty("regweb3.salida.distribuir"),"false"));
				boolean generarJustificante = Boolean.parseBoolean(StringUtils.defaultIfEmpty(ConfiguracionRegweb3.getInstance().getProperty("regweb3.salida.generarJustificante"),"false"));

				result  = service.crearAsientoRegistral(Long.parseLong(idSesionRegistro),
						entidad, paramEntrada, null, generarJustificante, distribuir);

	            break;
			}catch (SOAPFaultException e){
				String stackTrace = es.caib.util.StringUtil.stackTraceToString(e);
				if(maxIntentos > 0 && stackTrace.indexOf(ERROR) != -1){
					logger.debug("Registro de salida " + id + " reintento n�mero " + reintentos + " erroneo: " + e.getMessage());
		            continue;
				} else{
					logger.error("Error realizando registro de salida: " + e.getMessage(), e);
				}
			}
		}

		// Devuelve resultado registro
		ResultadoRegistro resReg = new ResultadoRegistro();
		resReg.setFechaRegistro(result.getFechaRegistro());
		resReg.setNumeroRegistro(result.getNumeroRegistroFormateado());
		return resReg;
	}


	/** {@inheritDoc} */
	public byte[] obtenerJustificanteRegistroSalida(String entidad, String numeroRegistro,
			Date fechaRegistro) throws Exception {

		byte[] resultado = null;
		Date fechaInicioFormateada = null;

		int maxIntentos = getMaxReintentos();
		String fechaInicio = getFechaInicioJustificante();

		DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

		if (!StringUtils.isEmpty(fechaInicio)){
			try {
				fechaInicioFormateada = formatoFecha.parse(getFechaInicioJustificante());
			} catch (ParseException e) {
				logger.error("Error al formatear fecha inicio de justificante de registro: " + e.getMessage(), e);
			}

			if (!fechaRegistro.before(fechaInicioFormateada)){
				// Invoca a Regweb3
				final RegWebAsientoRegistralWs service = UtilsRegweb3.getAsientoRegistralService(entidad);

				JustificanteWs result = new JustificanteWs();

				for (int reintentos = 0; reintentos <= maxIntentos; reintentos++) {
					try{
						result = service.obtenerJustificante(entidad, numeroRegistro, ConstantesRegweb3.REGISTRO_SALIDA);
					    break;
					}catch (SOAPFaultException e){
						String stackTrace = es.caib.util.StringUtil.stackTraceToString(e);
						if(maxIntentos > 0 && stackTrace.indexOf(ERROR) != -1){
							logger.debug("Obtenci�n de justificante de registro de salida " + numeroRegistro + " reintento n�mero " + reintentos + " erron�o: " + e.getMessage());
					        continue;
						} else{
							logger.error("Error obteniendo justificante de registro de salida: " + e.getMessage(), e);
						}
					}catch (WsI18NException ex){
						logger.error("Error obteniendo justificante de registro de entrada: " + ex.getMessage(), ex);
						throw new Exception ("S'ha produit un error al descarregar el justificant de registre de sortida. Per favor, tornau a provar-ho passats uns minuts.");
					}
				}

				resultado = result.getJustificante();
			}

		}

		return resultado;
	}

	/** {@inheritDoc} */
	public ResultadoRegistro confirmarPreregistro(
			String usuario,
			String entidad,
			String oficina,
			String codigoProvincia,
			String codigoMunicipio,
			String descripcionMunicipio,
			Justificante justificantePreregistro,
			ReferenciaRDS refJustificante,
			ReferenciaRDS refAsiento,
			Map refAnexos) throws Exception {

		int maxIntentos = getMaxReintentos();
		String id = "" + System.currentTimeMillis();

		// Verifica entidad
		verificarEntidad(entidad);

		// Obtenemos asiento
		AsientoRegistral asientoRegistral = justificantePreregistro.getAsientoRegistral();

		// Establecemos oficina origen
		asientoRegistral.getDatosOrigen().setCodigoEntidadRegistralOrigen(oficina);

		// Mapea parametros ws
		AsientoRegistralWs paramEntrada = mapearParametrosRegistro(entidad, asientoRegistral, refAsiento, refAnexos);

		// Establecemos como usuario que realiza el registro al usuario conectado
		paramEntrada.setCodigoUsuario(usuario);

		// Invoca a Regweb3
		RegWebAsientoRegistralWs service = UtilsRegweb3.getAsientoRegistralService(entidad);


		AsientoRegistralWs result = null;
		for (int reintentos = 0; reintentos <= maxIntentos; reintentos++) {
			try{

				// TODO PENDIENTE GESTION REINTENTOS SOBRE SESION REGISTRO
				String idSesionRegistro = generarSesionRegweb(entidad);

				// Obtiene si esta configurado distribucion y generar justificante
				boolean distribuir = Boolean.parseBoolean(StringUtils.defaultIfEmpty(ConfiguracionRegweb3.getInstance().getProperty("regweb3.entrada.distribuir"),"false"));
				boolean generarJustificante = Boolean.parseBoolean(StringUtils.defaultIfEmpty(ConfiguracionRegweb3.getInstance().getProperty("regweb3.entrada.generarJustificante"),"false"));

	            result   = service.crearAsientoRegistral(Long.parseLong(idSesionRegistro),
						entidad, paramEntrada, null, generarJustificante, distribuir);
	            break;
			}catch (SOAPFaultException e){
				String stackTrace = es.caib.util.StringUtil.stackTraceToString(e);
				if(maxIntentos > 0 && stackTrace.indexOf(ERROR) != -1){
					logger.debug("Confirmaci�n de registro " + id + " reintento n�mero " + reintentos + " erron�o: " + e.getMessage());
		            continue;
				} else{
					logger.error("Error realizando confirmaci�n de registro: " + e.getMessage(), e);
				}
			}
		}

		// Devuelve resultado registro
		ResultadoRegistro resReg = new ResultadoRegistro();
		resReg.setFechaRegistro(result.getFechaRegistro());
		resReg.setNumeroRegistro(result.getNumeroRegistroFormateado());
		return resReg;


	}

	/** {@inheritDoc} */
	public List obtenerOficinasRegistro(String entidad, char tipoRegistro) {
		return obtenerOficinasRegistroUsuario(entidad,tipoRegistro, null);
	}

	/** {@inheritDoc} */
	public List obtenerOficinasRegistroUsuario(String entidad, char tipoRegistro, String usuario) {
		List resultado = new ArrayList();
		int maxIntentos = getMaxReintentos();
		String id = "" + System.currentTimeMillis();
		try {

			verificarEntidad(entidad);

			RegWebInfoWs service = UtilsRegweb3.getRegistroInfoService(entidad);

			Long regType = null;
			if (tipoRegistro == ConstantesPluginRegistro.REGISTRO_ENTRADA) {
				regType = ConstantesRegweb3.REGISTRO_ENTRADA;
			} else if (tipoRegistro == ConstantesPluginRegistro.REGISTRO_SALIDA) {
				regType = ConstantesRegweb3.REGISTRO_SALIDA;
			} else {
				throw new Exception("Tipo registro no soportado: " + tipoRegistro);
			}


			List<LibroOficinaWs> resWs = null;

			for (int reintentos = 0; reintentos <= maxIntentos; reintentos++) {
				try{
					if (usuario != null) {
						resWs = service.obtenerLibrosOficinaUsuario(entidad, usuario, regType);
					} else {
						resWs = service.obtenerLibrosOficina(entidad, regType);
					}
					break;
				}catch (SOAPFaultException e){
					String stackTrace = es.caib.util.StringUtil.stackTraceToString(e);
					if(maxIntentos > 0 && stackTrace.indexOf(ERROR) != -1){
						logger.debug("Consulta oficinas de registro " + id + " reintento n�mero " + reintentos + " erron�o: " + e.getMessage());
			            continue;
					}
				}
			}

			for (LibroOficinaWs lo : resWs) {
				String codigoLibro = lo.getLibroWs().getCodigoLibro();
				String descLibro = lo.getLibroWs().getNombreCorto();
				String codigoOficina = lo.getOficinaWs().getCodigo();
				String descOficina = lo.getOficinaWs().getNombre();

				OficinaRegistro of = new OficinaRegistro();
				of.setCodigo(UtilsRegweb3.getOficinaAsiento(codigoLibro, codigoOficina));
				of.setDescripcion(descLibro + " - " + descOficina);
				resultado.add(of);
			}

		} catch (Exception ex) {
			logger.error("Error consultando oficinas registro: " + ex.getMessage(), ex);
			resultado = new ArrayList();
		}

		return resultado;
	}

	/** {@inheritDoc} */
	public List obtenerTiposAsunto(String entidad) {
		List resultado = new ArrayList();
		int maxIntentos = getMaxReintentos();
		String id = "" + System.currentTimeMillis();

		try {
			verificarEntidad(entidad);
			RegWebInfoWs service = UtilsRegweb3.getRegistroInfoService(entidad);
			List<TipoAsuntoWs> tiposAsunto = null;

			for (int reintentos = 0; reintentos <= maxIntentos; reintentos++) {
				try{
					tiposAsunto = service.listarTipoAsunto(entidad);
					break;
				}catch (SOAPFaultException e){
					String stackTrace = es.caib.util.StringUtil.stackTraceToString(e);
					if(maxIntentos > 0 && stackTrace.indexOf(ERROR) != -1){
						logger.debug("Consulta tipos asunto de registro " + id + " reintento n�mero " + reintentos + " erron�o: " + e.getMessage());
			            continue;
					}
				}
			}

			for (TipoAsuntoWs asuntoR : tiposAsunto) {
				TipoAsunto asunto = new TipoAsunto();
				asunto.setCodigo(asuntoR.getCodigo());
				asunto.setDescripcion(asuntoR.getNombre());
				resultado.add(asunto);
			}
		} catch (Exception ex) {
			logger.error("Error consultando tipos asunto: " + ex.getMessage(), ex);
			resultado = new ArrayList();
		}
		return resultado;
	}

	/** {@inheritDoc} */
	public List obtenerServiciosDestino(String entidad) {
		List resultado = null;
		int maxIntentos = getMaxReintentos();

		String id = "" + System.currentTimeMillis();

		String cacheKey = ORGANOS_DESTINO;

        try {
        	resultado = ( List ) getFromCache(cacheKey);
        } catch (CacheException ex){
        	logger.error("Error recuperando servicios destino de cache: " + ex.getMessage(), ex);
        }

        if (resultado != null){
        	logger.debug(cacheKey + " - obtenido de cache");
        	return resultado;
        }

		try {
			verificarEntidad(entidad);
			List<UnidadTF> res = new ArrayList<UnidadTF>();

			for (int reintentos = 0; reintentos <= maxIntentos; reintentos++) {
				try{
		            res = UtilsRegweb3.getDir3UnidadesService().obtenerArbolUnidadesDestinatarias(entidad);
		            break;
				}catch (SOAPFaultException e){
					String stackTrace = es.caib.util.StringUtil.stackTraceToString(e);
					if(maxIntentos > 0 && stackTrace.indexOf(ERROR) != -1){
						logger.debug("Consulta servicios destino " + id + " reintento n�mero " + reintentos + " erron�o: " + e.getMessage());
			            continue;
					}
				}
			}

			resultado = new ArrayList();
			for (UnidadTF u : res) {
				ServicioDestinatario sd = new ServicioDestinatario();
				sd.setCodigo(u.getCodigo());
				sd.setDescripcion(u.getDenominacion());
				if (StringUtils.isNotBlank(u.getCodUnidadSuperior()) && !u.getCodUnidadRaiz().equals(u.getCodigo())) {
					sd.setCodigoPadre(u.getCodUnidadSuperior());
				}
				resultado.add(sd);
			}

			this.saveToCache( cacheKey, (Serializable) resultado );
		} catch (Exception ex) {
			logger.error("Error consultando servicios destino: " + ex.getMessage(), ex);
			resultado = new ArrayList();
		}
		return resultado;
	}

	/** {@inheritDoc} */
	public String obtenerDescServiciosDestino(String servicioDestino) {
		String result = null;
		int maxIntentos = getMaxReintentos();

		String id = "" + System.currentTimeMillis();

		try {

			for (int reintentos = 0; reintentos <= maxIntentos; reintentos++) {
				try{
		            final UnidadTF res = UtilsRegweb3.getDir3UnidadesService().obtenerUnidad(servicioDestino, null, null);
		            if (res != null) {
		            	result = res.getDenominacion();
		            }
		            break;
				}catch (SOAPFaultException e){
					String stackTrace = es.caib.util.StringUtil.stackTraceToString(e);
					if(maxIntentos > 0 && stackTrace.indexOf(ERROR) != -1){
						logger.debug("Consulta descripcion servicio destino " + servicioDestino + " reintento n�mero " + reintentos + " erron�o: " + e.getMessage());
			            continue;
					}
				}
			}

		} catch (Exception ex) {
			logger.error("Error consultando descripcion del servicio destino: " + servicioDestino + " " + ex.getMessage(), ex);
		}

		return result;

	}

	/** {@inheritDoc} */
    public void anularRegistroEntrada(String entidad, String numeroRegistro, Date fechaRegistro) throws Exception {
    	int maxIntentos = getMaxReintentos();
		String id = "" + System.currentTimeMillis();
    	verificarEntidad(entidad);
    	String user = UtilsRegweb3.obtenerUsuarioEntidad(entidad);
    	for (int reintentos = 0; reintentos <= maxIntentos; reintentos++) {
			try{
				UtilsRegweb3.getRegistroEntradaService(entidad).anularRegistroEntrada(numeroRegistro, entidad, true);
				break;
			}catch (SOAPFaultException e){
				String stackTrace = es.caib.util.StringUtil.stackTraceToString(e);
				if(maxIntentos > 0 && stackTrace.indexOf(ERROR) != -1){
					logger.debug("Anulaci�n registro de entrada " + id + " reintento n�mero " + reintentos + " erron�o: " + e.getMessage());
		            continue;
				}
			}
		}
	}

    /** {@inheritDoc} */
	public void anularRegistroSalida(String entidad, String numeroRegistro, Date fechaRegistro) throws Exception {
		int maxIntentos = getMaxReintentos();
		String id = "" + System.currentTimeMillis();
		verificarEntidad(entidad);
		String user = UtilsRegweb3.obtenerUsuarioEntidad(entidad);
    	for (int reintentos = 0; reintentos <= maxIntentos; reintentos++) {
			try{
				UtilsRegweb3.getRegistroSalidaService(entidad).anularRegistroSalida(numeroRegistro, entidad, true);
		    	break;
			}catch (SOAPFaultException e){
				String stackTrace = es.caib.util.StringUtil.stackTraceToString(e);
				if(maxIntentos > 0 && stackTrace.indexOf(ERROR) != -1){
					logger.debug("Anulaci�n registro de salida " + id + " reintento n�mero " + reintentos + " erron�o: " + e.getMessage());
		            continue;
				}
			}
		}
	}

	/** {@inheritDoc} */
	public String obtenerDescripcionSelloOficina(char tipoRegistro, String entidad, String codigoOficinaAsiento) {
		int maxIntentos = getMaxReintentos();
		String id = "" + System.currentTimeMillis();
		String resultado = "";
		try {

			verificarEntidad(entidad);

			RegWebInfoWs service = UtilsRegweb3.getRegistroInfoService(entidad);

			Long regType = null;
			if (tipoRegistro == ConstantesPluginRegistro.REGISTRO_ENTRADA) {
				regType = ConstantesRegweb3.REGISTRO_ENTRADA;
			} else if (tipoRegistro == ConstantesPluginRegistro.REGISTRO_SALIDA) {
				regType = ConstantesRegweb3.REGISTRO_SALIDA;
			} else {
				throw new Exception("Tipo registro no soportado: " + tipoRegistro);
			}


			List<LibroOficinaWs> resWs = null;

			for (int reintentos = 0; reintentos <= maxIntentos; reintentos++) {
				try{
					resWs = service.obtenerLibrosOficina(entidad, regType);
					break;
				}catch (SOAPFaultException e){
					String stackTrace = es.caib.util.StringUtil.stackTraceToString(e);
					if(maxIntentos > 0 && stackTrace.indexOf(ERROR) != -1){
						logger.debug("Obtenci�n descripci�n oficina sello de registro  " + id + " reintento n�mero " + reintentos + " erron�o: " + e.getMessage());
			            continue;
					}
				}
			}

			boolean enc = false;

			for (LibroOficinaWs lo : resWs) {
				String codigoLibro = lo.getLibroWs().getCodigoLibro();
				String codigoOficina = lo.getOficinaWs().getCodigo();

				if (codigoOficinaAsiento.equals(UtilsRegweb3.getOficinaAsiento(codigoLibro,codigoOficina))) {
					enc = true;
					resultado = lo.getLibroWs().getNombreCorto();
					break;
				}
			}

			if (!enc) {
				logger.error("Error consultando oficina registro: no se encuentra oficina " + codigoOficinaAsiento);
			}

		} catch (Exception ex) {
			logger.error("Error consultando oficinas registro: " + ex.getMessage(), ex);
			resultado = "";
		}

		return resultado;
	}

	// ----------- Funciones auxiliares

	/**
	 *  Mapea datos asiento a parametro ws.
	 * @param asiento asiento
	 * @param refAsiento referencia asiento
	 * @param refAnexos referencia anexos
	 * @return parametro ws
	 * @throws Exception
	 */
	private AsientoRegistralWs mapearParametrosRegistro(String entidad,
			AsientoRegistral asiento, ReferenciaRDS refAsiento, Map refAnexos) throws Exception {

		// Obtiene datos propios
		 DatosPropios datosPropios = obtenerDatosPropios(asiento, refAnexos);

		// Crea parametros segun sea registro entrada o salida
		final AsientoRegistralWs asientoWs = new AsientoRegistralWs();
		boolean esRegistroSalida = (asiento.getDatosOrigen().getTipoRegistro() == ConstantesAsientoXML.TIPO_REGISTRO_SALIDA);
		if (esRegistroSalida) {
			asientoWs.setTipoRegistro(ConstantesRegweb3.REGISTRO_SALIDA);
		} else {
			asientoWs.setTipoRegistro(ConstantesRegweb3.REGISTRO_ENTRADA);
		}

		// Datos aplicacion
		asientoWs.setAplicacionTelematica(UtilsRegweb3.getCodigoAplicacion());

        // Usuario que registra (por defecto SISTRA, excepto para confirmacion preregistro)
        String user = UtilsRegweb3.obtenerUsuarioEntidad(entidad);
        asientoWs.setCodigoUsuario(user);

		// Datos oficina registro
        String oficinaAsientoRegistral = asiento.getDatosOrigen().getCodigoEntidadRegistralOrigen();
		asientoWs.setEntidadRegistralOrigenCodigo(UtilsRegweb3.getOficina(oficinaAsientoRegistral));
		asientoWs.setLibroCodigo(UtilsRegweb3.getLibro(oficinaAsientoRegistral));
		if (esRegistroSalida) {
			asientoWs.setUnidadTramitacionOrigenCodigo(asiento.getDatosAsunto().getCodigoOrganoDestino());
		} else {
			asientoWs.setUnidadTramitacionDestinoCodigo(asiento.getDatosAsunto().getCodigoOrganoDestino());
		}

		// Datos asunto
        asientoWs.setResumen(UtilsRegweb3.truncarTexto(asiento.getDatosAsunto().getExtractoAsunto(),
				ConstantesRegweb3.MAX_SIZE_ASUNTO_RESUMEN));
        asientoWs.setTipoDocumentacionFisicaCodigo(new Long(ConstantesRegweb3.DOC_FISICA_REQUERIDA));
        asientoWs.setIdioma(UtilsRegweb3.obtenerIdiomaRegistro(asiento.getDatosAsunto().getIdiomaAsunto()));

        if (datosPropios != null && datosPropios.getInstrucciones() != null) {
        	if (datosPropios.getInstrucciones().getTramiteSubsanacion() != null && StringUtils.isNotBlank(datosPropios.getInstrucciones().getTramiteSubsanacion().getExpedienteCodigo())) {
            	asientoWs.setNumeroExpediente(datosPropios.getInstrucciones().getTramiteSubsanacion().getExpedienteCodigo());
            }
        	if (StringUtils.isNotBlank(datosPropios.getInstrucciones().getIdentificadorSIA())) {
            	asientoWs.setCodigoSia(Long.parseLong(datosPropios.getInstrucciones().getIdentificadorSIA()));
            }
        }

        // No usados
        // registroWs.setExpone(asiento.getDatosAsunto().getTextoExpone());
        // registroWs.setSolicita(asiento.getDatosAsunto().getTextoSolicita());

    	// Se indica que el asiento no se hace de forma presencial
        asientoWs.setPresencial(false);


        // Datos interesado: representante / representado
        DatosInteresado representanteAsiento = UtilsRegweb3.obtenerDatosInteresadoAsiento(asiento, "RPT");
        DatosInteresado representadoAsiento = UtilsRegweb3.obtenerDatosInteresadoAsiento(asiento, "RPD");

        DatosInteresadoWs interesado = null;
        DatosInteresadoWs representante = null;

        if (representadoAsiento != null) {
        	interesado =  UtilsRegweb3.crearInteresado(representadoAsiento);
        	representante = UtilsRegweb3.crearInteresado(representanteAsiento);
        } else {
        	interesado =  UtilsRegweb3.crearInteresado(representanteAsiento);
        }

        InteresadoWs interesadoWs = new InteresadoWs();
        interesadoWs.setInteresado(interesado);
        interesadoWs.setRepresentante(representante);
        asientoWs.getInteresados().add(interesadoWs);


        // Anexos
        if ("true".equals(ConfiguracionRegweb3.getInstance().getProperty("regweb3.insertarDocs")) && verificarFiltrosAnexadoDocumentacion(asiento)) {

        	boolean anexarInternoAsiento = "true".equals(ConfiguracionRegweb3.getInstance().getProperty("regweb3.insertarDocs.internos.asiento"));
        	boolean anexarInternoFormulario = "true".equals(ConfiguracionRegweb3.getInstance().getProperty("regweb3.insertarDocs.internos.formulario"));
        	boolean anexarInternoPago = "true".equals(ConfiguracionRegweb3.getInstance().getProperty("regweb3.insertarDocs.internos.pago"));
        	boolean anexarFormateados = "true".equals(ConfiguracionRegweb3.getInstance().getProperty("regweb3.insertarDocs.formateados"));

        	Integer origenDocumento;
	        String tipoDocumental;
	        if (esRegistroSalida) {
				origenDocumento  = ConstantesRegweb3.ORIGEN_DOCUMENTO_ADMINISTRACION;
				tipoDocumental = ConstantesRegweb3.TIPO_DOCUMENTAL_NOTIFICACION;
			} else {
				origenDocumento  = ConstantesRegweb3.ORIGEN_DOCUMENTO_CIUDADANO;
				tipoDocumental = ConstantesRegweb3.TIPO_DOCUMENTAL_SOLICITUD;
			}

	        // - Asiento registral
	        // 		- Xml de asiento
			if (anexarInternoAsiento) {
				AnexoWs anexoAsientoWs = generarAnexoWs(refAsiento, false, ConstantesRegweb3.TIPO_DOCUMENTO_FICHERO_TECNICO,
						tipoDocumental, origenDocumento, ConstantesRegweb3.VALIDEZ_DOCUMENTO_ORIGINAL);
		        asientoWs.getAnexos().add(anexoAsientoWs);
			}
			if (anexarFormateados) {
				AnexoWs anexoAsientoFWs = generarAnexoWs(refAsiento, true, ConstantesRegweb3.TIPO_DOCUMENTO_FICHERO_TECNICO,
						tipoDocumental, origenDocumento, ConstantesRegweb3.VALIDEZ_DOCUMENTO_ORIGINAL);
		        asientoWs.getAnexos().add(anexoAsientoFWs);
			}

	        // - Ficheros asiento
	        for (Iterator it = asiento.getDatosAnexoDocumentacion().iterator();it.hasNext();) {

	        	AnexoWs anexoWs = null;
	        	String tipoDocumento = ConstantesRegweb3.TIPO_DOCUMENTO_ANEXO;
	        	String validezDocumento = ConstantesRegweb3.VALIDEZ_DOCUMENTO_COPIA;
	        	boolean anexarInterno = false;

	        	DatosAnexoDocumentacion da = (DatosAnexoDocumentacion) it.next();
	        	ReferenciaRDS refRDS = (ReferenciaRDS) refAnexos.get(da.getIdentificadorDocumento());

	        	// Fichero asociados asiento
	        	if (da.getTipoDocumento().equals(ConstantesAsientoXML.DATOSANEXO_DATOS_PROPIOS) ||
	        		da.getTipoDocumento().equals(ConstantesAsientoXML.DATOSANEXO_AVISO_NOTIFICACION) ||
	        		da.getTipoDocumento().equals(ConstantesAsientoXML.DATOSANEXO_OFICIO_REMISION)) {
	        			tipoDocumento = ConstantesRegweb3.TIPO_DOCUMENTO_FICHERO_TECNICO;
	        			validezDocumento = ConstantesRegweb3.VALIDEZ_DOCUMENTO_ORIGINAL;
	        			anexarInterno = anexarInternoAsiento;
	        	}

	        	// Fichero asociado a formulario
	        	if (da.getTipoDocumento().equals(ConstantesAsientoXML.DATOSANEXO_FORMULARIO)) {
	        		tipoDocumento = ConstantesRegweb3.TIPO_DOCUMENTO_FICHERO_TECNICO;
	        		validezDocumento = ConstantesRegweb3.VALIDEZ_DOCUMENTO_ORIGINAL;
	        		anexarInterno = anexarInternoFormulario;
	        	}

	        	// Fichero asociado a pago
	        	if (da.getTipoDocumento().equals(ConstantesAsientoXML.DATOSANEXO_PAGO)) {
	        		tipoDocumento = ConstantesRegweb3.TIPO_DOCUMENTO_FICHERO_TECNICO;
	        		validezDocumento = ConstantesRegweb3.VALIDEZ_DOCUMENTO_ORIGINAL;
	        		anexarInterno = anexarInternoPago;
	        	}

	        	// Generamos anexo ws y a�adimos a lista: si tenemos que anexar fichero interno o no es un fichero interno (anexo)
	        	if (anexarInterno || tipoDocumento != ConstantesRegweb3.TIPO_DOCUMENTO_FICHERO_TECNICO) {
	        		anexoWs = generarAnexoWs(refRDS, false, tipoDocumento, tipoDocumental, origenDocumento, validezDocumento);
	        		asientoWs.getAnexos().add(anexoWs);
	        	}

	        	// Para formularios y pagos vemos si se adjunta como formateados
	        	if (anexarFormateados &&
	        			(da.getTipoDocumento().equals(ConstantesAsientoXML.DATOSANEXO_FORMULARIO) || da.getTipoDocumento().equals(ConstantesAsientoXML.DATOSANEXO_PAGO) ) ) {
	        		tipoDocumento = ConstantesRegweb3.TIPO_DOCUMENTO_FORMULARIO;
	        		validezDocumento = ConstantesRegweb3.VALIDEZ_DOCUMENTO_ORIGINAL;
	        		anexoWs = generarAnexoWs(refRDS, true, tipoDocumento, tipoDocumental, origenDocumento, validezDocumento);
	        		asientoWs.getAnexos().add(anexoWs);
	        	}

	        }
        }

        return asientoWs;

	}


	/**
	 * Verifica filtros anexado documentacion.
	 * @param asiento asiento
	 * @return si se debe anexar
	 */
	private boolean verificarFiltrosAnexadoDocumentacion(
			AsientoRegistral asiento) {
		boolean res = true;
		String filtro = ConfiguracionRegweb3.getInstance().getProperty("regweb3.insertarDocs.filtro.tipo");
		String listaTramites = ConfiguracionRegweb3.getInstance().getProperty("regweb3.insertarDocs.filtro.listaTramites");

		Boolean incluir = null;
		if ("INCLUIR".equals(filtro)) {
			incluir = true;
		}
		if ("EXCLUIR".equals(filtro)) {
			incluir = false;
		}

		if (incluir != null && StringUtils.isBlank(listaTramites)) {
			boolean encontrado = false;
			String [] listaTramitesStr = listaTramites.split(";");
			for (int i = 0; i < listaTramites.length(); i++) {
				if (asiento.getDatosAsunto().getIdentificadorTramite().equals(listaTramitesStr[i])) {
					encontrado = true;
					break;
				}
			}
			res = (incluir && encontrado) || (!incluir && !encontrado);
		}

		return res;
	}

	/**
	 * Genera AnexoWS en funcion documento REDOSE
	 * @param refRDS
	 * @param tipoDocumento
	 * @param tipoDocumental
	 * @param origenDocumento
	 * @return
	 */
	private AnexoWs generarAnexoWs(ReferenciaRDS refRDS, boolean formatearDocumento, String tipoDocumento, String tipoDocumental,
			Integer origenDocumento, String validezDocumento) throws Exception {
		RdsDelegate rdsDelegate = DelegateRDSUtil.getRdsDelegate();
		DocumentoRDS docRDS = null;
		DocumentoRDS docRDSFormateado = null;

		docRDS = rdsDelegate.consultarDocumento(refRDS);

		if (docRDS.isEstructurado() && formatearDocumento) {
			docRDSFormateado = rdsDelegate.consultarDocumentoFormateadoRegistro(refRDS);
		}

		AnexoWs anexoAsiento = new AnexoWs();
        anexoAsiento.setTitulo(UtilsRegweb3.truncarTexto(docRDS.getTitulo(), ConstantesRegweb3.MAX_SIZE_ANEXO_TITULO));

        if (docRDSFormateado != null) {
	        anexoAsiento.setNombreFicheroAnexado(UtilsRegweb3.truncarFilename(docRDSFormateado.getNombreFichero(), ConstantesRegweb3.MAX_SIZE_ANEXO_FILENAME));
	        anexoAsiento.setFicheroAnexado(docRDSFormateado.getDatosFichero());
	        anexoAsiento.setTipoMIMEFicheroAnexado(MimeType.getMimeTypeForExtension(getExtension(docRDSFormateado.getNombreFichero())));
        } else {
        	anexoAsiento.setNombreFicheroAnexado(docRDS.getNombreFichero());
	        anexoAsiento.setFicheroAnexado(docRDS.getDatosFichero());
	        anexoAsiento.setTipoMIMEFicheroAnexado(MimeType.getMimeTypeForExtension(getExtension(docRDS.getNombreFichero())));
        }
        anexoAsiento.setTipoDocumental(tipoDocumental);
        anexoAsiento.setTipoDocumento(tipoDocumento);
        anexoAsiento.setOrigenCiudadanoAdmin(origenDocumento);

        // Insertamos firma
        boolean insertarFirma = false;
        if (docRDS.getFirmas() != null && docRDS.getFirmas().length > 0) {
        	// Si documento no es estructurado
        	if (!docRDS.isEstructurado()) {
        		insertarFirma = true;
        	}
        	// Si el documento es estructurado, no hay que formatearlo y no tiene documento formateado consolidado
        	if (docRDS.isEstructurado() && !formatearDocumento && docRDS.getReferenciaRDSFormateado() == null) {
        		insertarFirma = true;
        	}
        	// Si el documento es formateado y tiene documento formateado consolidado, la firma es referente al documento formateado
        	if (docRDS.isEstructurado() && formatearDocumento && docRDS.getReferenciaRDSFormateado() != null) {
        		insertarFirma = true;
        	}
        }


        // Insertamos firma
        if (insertarFirma) {

        		// Solo se puede anexar 1 firma
	        	if (docRDS.getFirmas().length > 1) {
	        		throw new Exception("El documento " + docRDS.getReferenciaRDS().getCodigo() + " tiene m�s de 1 firma. Solo se puede anexar 1 firma.");
	        	}

	        	if (tipoDocumento.equals(ConstantesRegweb3.TIPO_DOCUMENTO_ANEXO)){
	        		validezDocumento = ConstantesRegweb3.VALIDEZ_DOCUMENTO_COPIA;
	        	}

	        	FirmaIntf firma = docRDS.getFirmas()[0];

	        	// Tipo firma
	        	Integer modoFirma = null;
	        	if (PluginFirmaIntf.FORMATO_FIRMA_CADES_DETACHED.equals(firma.getFormatoFirma()) || PluginFirmaIntf.FORMATO_FIRMA_XADES_DETACHED.equals(firma.getFormatoFirma()) ) {
	        		modoFirma = ConstantesRegweb3.MODO_FIRMA_DETACHED;
	        	} else if (PluginFirmaIntf.FORMATO_FIRMA_PADES.equals(firma.getFormatoFirma()) || PluginFirmaIntf.FORMATO_FIRMA_SMIME.equals(firma.getFormatoFirma()) || PluginFirmaIntf.FORMATO_FIRMA_EXTENDED.equals(firma.getFormatoFirma())) {
	        		modoFirma = ConstantesRegweb3.MODO_FIRMA_ATTACHED;
	        	} else {
	        		throw new Exception("Formato firma no soportado: " + firma.getFormatoFirma());
	        	}

	        	anexoAsiento.setModoFirma(modoFirma);

	        	if (PluginFirmaIntf.FORMATO_FIRMA_PADES.equals(firma.getFormatoFirma())) {
	        		// Se pasa directamente la firma como datos del fichero
	        		anexoAsiento.setFicheroAnexado(firma.getContenidoFirma());
	        	} else {

	        		anexoAsiento.setFirmaAnexada(firma.getContenidoFirma());
	        		anexoAsiento.setNombreFirmaAnexada(UtilsRegweb3.obtenerNombreFirma(firma));
	        		anexoAsiento.setTipoMIMEFirmaAnexada(MimeType.getMimeTypeForExtension(UtilsRegweb3.getExtension(anexoAsiento.getNombreFirmaAnexada())));
	        	}

        } else {
        	anexoAsiento.setModoFirma(ConstantesRegweb3.MODO_FIRMA_SIN_FIRMA);
        }

        anexoAsiento.setValidezDocumento(validezDocumento);

    	return anexoAsiento;
	}

	/**
     * Obtiene extension fichero.
     */
	private String getExtension(String filename){
		if(filename.lastIndexOf(".") != -1){
			return filename.substring(filename.lastIndexOf(".") + 1);
		}else{
			return "";
		}
	}

	/**
	 * Verifica entidad.
	 */
	private void verificarEntidad(String entidad) throws Exception{
		if (!UtilsRegweb3.verificarEntidad(entidad)) {
			throw new Exception("Entidad " + entidad + " no soportada");
		}
	}

	private static Cache getCache() throws CacheException {
        String cacheName = PluginRegweb3.class.getName();
        CacheManager cacheManager = CacheManager.getInstance();
        Cache cache;
        if (cacheManager.cacheExists(cacheName)) {
            cache = cacheManager.getCache(cacheName);
        } else {
            cache = new Cache(cacheName, 1000, false, false, ConstantesRegweb3.TIEMPO_EN_CACHE, 300);
            cacheManager.addCache(cache);
        }
        return cache;
    }

    protected Serializable getFromCache(Serializable key) throws CacheException {
        Cache cache = getCache();
        Element element = cache.get(key);
        if (element != null && !cache.isExpired(element)) {
            return element.getValue();
        } else {
            return null;
        }
    }

    protected void saveToCache(Serializable key, Serializable value) throws CacheException {
        Cache cache = getCache();
        cache.put(new Element(key, value));
    }

	public String obtenerReferenciaJustificanteRegistroEntrada(String entidad,
			String numeroRegistro, Date fechaRegistro) throws Exception {

		String resultado = null;
		Date fechaInicioFormateada = null;

		int maxIntentos = getMaxReintentos();
		String fechaInicio = getFechaInicioJustificante();

		DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

		if (!StringUtils.isEmpty(fechaInicio)){
			try {
				fechaInicioFormateada = formatoFecha.parse(getFechaInicioJustificante());
			} catch (ParseException e) {
				logger.error("Error al formatear fecha inicio de justificante de registro: " + e.getMessage(), e);
			}

			if (!fechaRegistro.before(fechaInicioFormateada)){
				// Invoca a Regweb3
				final RegWebAsientoRegistralWs service = UtilsRegweb3.getAsientoRegistralService(entidad);

				for (int reintentos = 0; reintentos <= maxIntentos; reintentos++) {
					try{
						JustificanteReferenciaWs referencia = service.obtenerReferenciaJustificante(entidad,
								numeroRegistro);
						resultado = referencia.getUrl();
					    break;
					}catch (SOAPFaultException e){
						String stackTrace = es.caib.util.StringUtil.stackTraceToString(e);
						if(maxIntentos > 0 && stackTrace.indexOf(ERROR) != -1){
							logger.debug("Obtenci�n de referencia justificante de registro de entrada " + numeroRegistro + " reintento n�mero " + reintentos + " erron�o: " + e.getMessage());
					        continue;
						} else{
							logger.error("Error obteniendo referencia justificante de registro de entrada: " + e.getMessage(), e);
						}
					}catch (WsI18NException ex){
						logger.error("Error obteniendo referencia justificante de registro de entrada: " + ex.getMessage(), ex);
						throw new Exception ("S'ha produit un error al descarregar referencia justificant de registre. Per favor, tornau a provar-ho passats uns minuts.");
					}
				}

			}

		}

		return resultado;

	}

	public char obtenerTipoJustificanteRegistroEntrada() {
		String tipoJustificante = ConfiguracionRegweb3.getInstance().getProperty("regweb3.tipoJustificanteRegistroEntrada");
		if (StringUtils.isBlank(tipoJustificante)) {
			tipoJustificante = "" + ConstantesPluginRegistro.JUSTIFICANTE_DESCARGA;
		}
		return tipoJustificante.charAt(0);
	}

	private DatosPropios obtenerDatosPropios(AsientoRegistral asiento, Map refAnexos) throws Exception {
		DatosPropios datosPropios = null;
		for (Iterator it = asiento.getDatosAnexoDocumentacion().iterator();it.hasNext();) {
			DatosAnexoDocumentacion da = (DatosAnexoDocumentacion) it.next();
			if (da.getTipoDocumento().equals(ConstantesAsientoXML.DATOSANEXO_DATOS_PROPIOS)) {
				ReferenciaRDS refRDS = (ReferenciaRDS) refAnexos.get(da.getIdentificadorDocumento());
				RdsDelegate rdsDelegate = DelegateRDSUtil.getRdsDelegate();
				DocumentoRDS docRDS = rdsDelegate.consultarDocumento(refRDS);
				FactoriaObjetosXMLDatosPropios factoria = ServicioDatosPropiosXML.crearFactoriaObjetosXML();
				datosPropios = factoria.crearDatosPropios (new ByteArrayInputStream(docRDS.getDatosFichero()));
			}
		}
		return datosPropios;

	}

	/**
	 * Genera sesion de registro
	 * @param codigoEntidad
	 * @return sesion de registro
	 * @throws Exception
	 */
	private String generarSesionRegweb(final String codigoEntidad) throws Exception {
		try {
			final RegWebAsientoRegistralWs service = UtilsRegweb3.getAsientoRegistralService(codigoEntidad);
			final Long result = service.obtenerSesionRegistro(codigoEntidad);
			return result.toString();
		} catch (final Exception ex) {
			throw new Exception("Error generando sesion regweb : " + ex.getMessage(), ex);
		}
	}
}
