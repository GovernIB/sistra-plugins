package es.caib.sistra.plugins.regtel.impl.caib;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import javax.xml.ws.BindingProvider;

import org.apache.commons.lang.StringUtils;

import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWsService;
import es.caib.regweb3.ws.api.v3.DatosInteresadoWs;
import es.caib.regweb3.ws.api.v3.RegWebInfoWs;
import es.caib.regweb3.ws.api.v3.RegWebInfoWsService;
import es.caib.regweb3.ws.api.v3.RegWebRegistroEntradaWs;
import es.caib.regweb3.ws.api.v3.RegWebRegistroEntradaWsService;
import es.caib.regweb3.ws.api.v3.RegWebRegistroSalidaWs;
import es.caib.regweb3.ws.api.v3.RegWebRegistroSalidaWsService;
import es.caib.sistra.plugins.firma.FirmaIntf;
import es.caib.util.NifCif;
import es.caib.util.ws.client.WsClientUtil;
import es.caib.xml.registro.factoria.impl.AsientoRegistral;
import es.caib.xml.registro.factoria.impl.DatosInteresado;


/**
 * Utilidades Regweb3.
 * @author rsanz
 *
 */
public class UtilsRegweb3 {

	/**
	 * Obtiene service registro entrada.
	 * @return service registro entrada
	 * @throws Exception
	 */
	public static RegWebRegistroEntradaWs getRegistroEntradaService() throws Exception  {
	       
		final String endpoint = ConfiguracionRegweb3.getInstance().getProperty("regweb3.endpoint.entrada");
		String user = getUsuarioRegistroSistra();
		String pass = ConfiguracionRegweb3.getInstance().getProperty("regweb3.password");

		// Url WSDL: local o remoto segun haya proxy
		URL wsdl = obtenerUrlWsdl(endpoint, "RegWebRegistroEntrada");
        RegWebRegistroEntradaWsService service = new RegWebRegistroEntradaWsService(wsdl);

        RegWebRegistroEntradaWs api = service.getRegWebRegistroEntradaWs();      
               
        configurarService((BindingProvider) api, endpoint, user, pass);
        
       return api;
    }
	
	/**
	 * Obtiene service registro salida.
	 * @return service registro salida
	 * @throws Exception
	 */
	public static RegWebRegistroSalidaWs getRegistroSalidaService() throws Exception  {
	       
		final String endpoint = ConfiguracionRegweb3.getInstance().getProperty("regweb3.endpoint.salida");
		String user = getUsuarioRegistroSistra();
		String pass = ConfiguracionRegweb3.getInstance().getProperty("regweb3.password");

		// Url WSDL: local o remoto segun haya proxy
		URL wsdl = obtenerUrlWsdl(endpoint, "RegWebRegistroSalida");
        RegWebRegistroSalidaWsService service = new RegWebRegistroSalidaWsService(wsdl);

        RegWebRegistroSalidaWs api = service.getRegWebRegistroSalidaWs();      
               
        configurarService((BindingProvider) api, endpoint, user, pass);
        
       return api;
    }
	
	/**
	 * Obtiene service registro salida.
	 * @return service registro salida
	 * @throws Exception
	 */
	public static RegWebInfoWs getRegistroInfoService() throws Exception  {
	       
		final String endpoint = ConfiguracionRegweb3.getInstance().getProperty("regweb3.endpoint.info");
		String user = getUsuarioRegistroSistra();
		String pass = ConfiguracionRegweb3.getInstance().getProperty("regweb3.password");

		// Url WSDL: local o remoto segun haya proxy
		URL wsdl = obtenerUrlWsdl(endpoint, "RegWebInfo");
        RegWebInfoWsService service = new RegWebInfoWsService(wsdl);

        RegWebInfoWs api = service.getRegWebInfoWs();      
               
        configurarService((BindingProvider) api, endpoint, user, pass);
        
       return api;
    }

	/**
	 * Obtiene service registro dir3.
	 * @return service registro dir3
	 * @throws Exception
	 */
	public static Dir3CaibObtenerUnidadesWs getDir3UnidadesService() throws Exception  {
	       
		final String endpoint = ConfiguracionRegweb3.getInstance().getProperty("regweb3.dir3.endpoint");
		String user = ConfiguracionRegweb3.getInstance().getProperty("regweb3.dir3.usuario");
		String pass = ConfiguracionRegweb3.getInstance().getProperty("regweb3.dir3.password");

		// Url WSDL: local o remoto segun haya proxy
		URL wsdl = obtenerUrlWsdl(endpoint, "Dir3CaibObtenerUnidades");

        Dir3CaibObtenerUnidadesWsService service = new Dir3CaibObtenerUnidadesWsService(wsdl);           
        Dir3CaibObtenerUnidadesWs api = service.getDir3CaibObtenerUnidadesWs();      
               
        configurarService((BindingProvider) api, endpoint, user, pass);
        
       return api;
    }

	/**
	 * Url WSDL: local o remoto segun haya proxy
	 * @param endpoint
	 * @param serviceName
	 * @return
	 * @throws MalformedURLException
	 */
	private static URL obtenerUrlWsdl(final String endpoint, String serviceName)
			throws MalformedURLException {
		URL wsdl = null;
		if (StringUtils.isNotBlank(System.getProperty("http.proxyHost"))) {
			wsdl = new URL("file://" + ConfiguracionRegweb3.getInstance().getProperty("regweb3.wsdl.dir")  + "/" + serviceName + ".wsdl");
		} else {
			wsdl = new URL(endpoint + "?wsdl");
		}
		return wsdl;
	}
	
	/**
	 * Usuario registro de SISTRA.
	 * @return Usuario registro de SISTRA.
	 */
	public static String getUsuarioRegistroSistra() {
		String user = ConfiguracionRegweb3.getInstance().getProperty("regweb3.usuario");
		return user;
	}
	
	/**
	 * Obtiene codigo aplicacion.
	 * @return codigo aplicacion
	 */
	public static String getCodigoAplicacion() {
		return ConfiguracionRegweb3.getInstance().getProperty("regweb3.aplicacion.codigo");
	}

	/**
	 * Obtiene version aplicacion.
	 * @return version aplicacion
	 */
	public static String getVersionAplicacion() {
		return ConfiguracionRegweb3.getInstance().getProperty("regweb3.aplicacion.version");
	}
	
	/**
	 * Obtiene codigo entidad.
	 * @return codigo entidad
	 */
	public static String getCodigoEntidad() {
		return ConfiguracionRegweb3.getInstance().getProperty("regweb3.entidad");
	}
	
	/**
	 * Obtiene tipo interesado.
	 * @param documentoIdentificacion documento identicacion
	 * @return tipo interesado
	 */
	public static String getTipoInteresado(String documentoIdentificacion) {
		String result = null;
		int validacion = NifCif.validaDocumento(documentoIdentificacion);
		switch (validacion) {
		case 1: // NIF
			result = ConstantesRegweb3.TIPO_INTERESADO_PERSONA_FISICA;
			break;
		case 2: // CIF
			result = ConstantesRegweb3.TIPO_INTERESADO_PERSONA_JURIDICA;
			break;
		case 3: // NIE
			result = ConstantesRegweb3.TIPO_INTERESADO_PERSONA_FISICA;
			break;
		default:
			result = null;
			break;
		}
		return result;
	}
	
	/**
	 * Obtiene tipo documento identificacion.
	 * @param documentoIdentificacion documento identicacion
	 * @return tipo documento identificacion.
	 */
	public static String getTipoDocumentoIdentificacion(String documentoIdentificacion) {
		String result = null;
		int validacion = NifCif.validaDocumento(documentoIdentificacion);
		switch (validacion) {
		case 1: // NIF
			result = ConstantesRegweb3.TIPO_DOCID_NIF;
			break;
		case 2: // CIF
			result = ConstantesRegweb3.TIPO_DOCID_CIF;
			break;
		case 3: // NIE
			result = ConstantesRegweb3.TIPO_DOCID_NIF;
			break;
		default:
			result = null;
			break;
		}
		return result;
	}

	/**
	 * Obtener datos interesado asiento.
	 * @param asiento asiento
	 * @param tipoInteresado tipo interesado (RPT/RPD).
	 * 
	 * @return interesado
	 */
	public static DatosInteresado obtenerDatosInteresadoAsiento(
			AsientoRegistral asiento, String tipoInteresado) {
		DatosInteresado result = null;
		for (Iterator it = asiento.getDatosInteresado().iterator(); it.hasNext();) {
			DatosInteresado datosInteresado = (DatosInteresado) it.next();
			if (tipoInteresado.equals(datosInteresado.getTipoInteresado())) {
				result = datosInteresado;
				break;
			}
		}
		return result;
	}		
	
	
	/**
	 * Obtener libro.
	 * @param asiento asiento
	 * @return interesado
	 */
	public static String getLibro(String oficinaAsientoRegistral) {
		String [] codigos = oficinaAsientoRegistral.split("\\.");
		return codigos[0];
	}	
	
	
	/**
	 * Obtener interesado.
	 * @param asiento asiento
	 * @return interesado
	 */
	public static String getOficina(String oficinaAsientoRegistral) {
		String [] codigos = oficinaAsientoRegistral.split("\\.");
		return codigos[1];
	}	
	
	/**
	 * Obtener interesado.
	 * @param asiento asiento
	 * @return interesado
	 */
	public static String getOficinaAsiento(String codigoLibro, String codigoOficina) { 		
		return codigoLibro + "." + codigoOficina;
	}
	
	/**
	 * Obtiene nombre firma.
	 * @param firma
	 * @return
	 */
	public static String obtenerNombreFirma(FirmaIntf firma) {
		String extension = "sign";
		if (firma.getFormatoFirma() != null) {
			if (firma.getFormatoFirma().toLowerCase().indexOf("cades") != -1) {
				extension = "cades";
			}
			if (firma.getFormatoFirma().toLowerCase().indexOf("xades") != -1) {
				extension = "xades";
			}
			if (firma.getFormatoFirma().toLowerCase().indexOf("pades") != -1) {
				extension = "pades";
			}
		}
		return "firma." + extension;
	}
	
	/**
     * Obtiene extension fichero.
     */
    public static String getExtension(String filename){
		if(filename.lastIndexOf(".") != -1){
			return filename.substring(filename.lastIndexOf(".") + 1);
		}else{
			return "";
		}
	}
    
	/**
	 * Crear interesado a partir datos asiento.
	 * 
	 * @param interesadoAsiento
	 * @return
	 * @throws Exception
	 */
	public static DatosInteresadoWs crearInteresado(
			DatosInteresado interesadoAsiento) throws Exception {
		DatosInteresadoWs interesado = new DatosInteresadoWs();
		if (StringUtils.isNotBlank(interesadoAsiento.getNumeroIdentificacion())) {
			interesado.setTipoInteresado(new Long(UtilsRegweb3
					.getTipoInteresado(interesadoAsiento
							.getNumeroIdentificacion())));
			interesado
					.setDocumento(interesadoAsiento.getNumeroIdentificacion());
			interesado.setTipoDocumentoIdentificacion(UtilsRegweb3
					.getTipoDocumentoIdentificacion(interesadoAsiento
							.getNumeroIdentificacion()));
		} else {
			interesado.setTipoInteresado(new Long(
					ConstantesRegweb3.TIPO_INTERESADO_PERSONA_FISICA));
		}
		if (interesado.getTipoInteresado().longValue() == Long
				.parseLong(ConstantesRegweb3.TIPO_INTERESADO_PERSONA_JURIDICA)) {
			interesado.setRazonSocial(interesadoAsiento
					.getIdentificacionInteresado());
		} else {
			if (interesadoAsiento.getIdentificacionInteresadoDesglosada() == null) {
				throw new Exception(
						"Se requiere la identificacion del interesado de forma desglosada");
			}
			interesado.setNombre(interesadoAsiento
					.getIdentificacionInteresadoDesglosada().getNombre());
			interesado.setApellido1(interesadoAsiento
					.getIdentificacionInteresadoDesglosada().getApellido1());
			interesado.setApellido2(interesadoAsiento
					.getIdentificacionInteresadoDesglosada().getApellido2());
		}
		return interesado;
	}
	
	// --------- Funciones auxiliares
	
	/**
	 * Configura service.
	 * @param bp Binding Provider
	 * @param endpoint Endpoint ws
	 * @param user usuario
	 * @param pass password
	 */
	private static void configurarService(BindingProvider bp, String endpoint,
			String user, String pass) throws Exception {
		 
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
	    
		// TODO DESPUES DE LAS PRUEBAS, DEJARLO COMO CONFIG GENERAL DE SISTRA
		//WsClientSistraUtil.configurePort(bp, endpoint, user, pass);
	    WsClientUtil.configurePort(bp, endpoint, null, user, pass, "BASIC", true, true, true, true);
	    
	}

			
	
}
