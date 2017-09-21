package es.caib.sistra.plugins.regtel.impl.caib;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

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
import es.caib.util.ws.client.WsClientSistraUtil;
import es.caib.xml.registro.factoria.impl.AsientoRegistral;
import es.caib.xml.registro.factoria.impl.DatosInteresado;


/**
 * Utilidades Regweb3.
 * @author rsanz
 *
 */
public class UtilsRegweb3 {

	/** Codigos paises INE. */
	private static final Map<String, String> CODIGOS_INE_PAIS = null;
	
	/**
	 * Obtiene service registro entrada.
	 * @return service registro entrada
	 * @throws Exception
	 */
	public static RegWebRegistroEntradaWs getRegistroEntradaService(String entidad) throws Exception  {
	       
		final String endpoint = ConfiguracionRegweb3.getInstance().getProperty("regweb3.endpoint.entrada");
		String user = obtenerUsuarioEntidad(entidad);
		String pass = obtenerPasswordEntidad(entidad);

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
	public static RegWebRegistroSalidaWs getRegistroSalidaService(String entidad) throws Exception  {
	       
		final String endpoint = ConfiguracionRegweb3.getInstance().getProperty("regweb3.endpoint.salida");
		String user = obtenerUsuarioEntidad(entidad);
		String pass = obtenerPasswordEntidad(entidad);

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
	public static RegWebInfoWs getRegistroInfoService(String entidad) throws Exception  {
	       
		final String endpoint = ConfiguracionRegweb3.getInstance().getProperty("regweb3.endpoint.info");
		String user = obtenerUsuarioEntidad(entidad);
		String pass = obtenerPasswordEntidad(entidad);

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
	 * Verifica si esta soportada la entidad.
	 * @param entidad entidad
	 * @return true si esta soportada la entidad
	 */
	public static boolean verificarEntidad(String entidad) {
		String[] entidades = ConfiguracionRegweb3.getInstance().getProperty("regweb3.entidad").split(";");
		boolean existe = false;
		for (String e : entidades) {
			if (e.equals(entidad)) {
				existe = true;
				break;
			}
		}
		return existe;
	}
	
	/**
	 * Obtiene usuario entidad.	
	 * @return usuario
	 */
	public static String obtenerUsuarioEntidad(String entidad) {
		String usuario = ConfiguracionRegweb3.getInstance().getProperty("regweb3.usuario." + entidad);		
		return usuario;
	}
	
	/**
	 * Obtiene password entidad.	
	 * @return password
	 */
	public static String obtenerPasswordEntidad(String entidad) {
		String password = ConfiguracionRegweb3.getInstance().getProperty("regweb3.password." + entidad);		
		return password;
	}
	
	/**
	 * Obtiene entidades soportadas.	
	 * @return entidades soportadas
	 */
	public static String[] obtenerEntidades() {
		String[] entidades = ConfiguracionRegweb3.getInstance().getProperty("regweb3.entidad").split(";");		
		return entidades;
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
	
	public static Integer getLongMaxDomicilio() {
		return new Integer(Integer.parseInt(ConfiguracionRegweb3.getInstance().getProperty("regweb3.longMax.domicilio")));
	}
	
	public static Integer getLongMaxCp() {
		return new Integer(Integer.parseInt(ConfiguracionRegweb3.getInstance().getProperty("regweb3.longMax.codigoPostal")));
	}
	
	public static Integer getLongMaxTelefono() {
		return new Integer(Integer.parseInt(ConfiguracionRegweb3.getInstance().getProperty("regweb3.longMax.telefono")));
	}
	
	public static Integer getLongMaxEmail() {
		return new Integer(Integer.parseInt(ConfiguracionRegweb3.getInstance().getProperty("regweb3.longMax.email")));
	}
	
	public static Integer getLongMaxNombre() {
		return new Integer(Integer.parseInt(ConfiguracionRegweb3.getInstance().getProperty("regweb3.longMax.nombre")));
	}
	
	public static Integer getLongMaxRazonSocial() {
		return new Integer(Integer.parseInt(ConfiguracionRegweb3.getInstance().getProperty("regweb3.longMax.razonSocial")));
	}
	
	public static Integer getLongMaxApellido1() {
		return new Integer(Integer.parseInt(ConfiguracionRegweb3.getInstance().getProperty("regweb3.longMax.apellido1")));
	}
	
	public static Integer getLongMaxApellido2() {
		return new Integer(Integer.parseInt(ConfiguracionRegweb3.getInstance().getProperty("regweb3.longMax.apellido2")));
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
		case 5: // Pasaporte
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
			result = ConstantesRegweb3.TIPO_DOCID_NIE;
			break;
		case 5: // Pasaporte
			result = ConstantesRegweb3.TIPO_DOCID_PSP;
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
					.setDocumento(UtilsRegweb3.normalizaNumDoc(interesadoAsiento
							.getNumeroIdentificacion()));
			interesado.setTipoDocumentoIdentificacion(UtilsRegweb3
					.getTipoDocumentoIdentificacion(interesadoAsiento
							.getNumeroIdentificacion()));
		} else {
			interesado.setTipoInteresado(new Long(
					ConstantesRegweb3.TIPO_INTERESADO_PERSONA_FISICA));
		}
		if (interesado.getTipoInteresado().longValue() == Long
				.parseLong(ConstantesRegweb3.TIPO_INTERESADO_PERSONA_JURIDICA)) {
			interesado.setRazonSocial((interesadoAsiento.getIdentificacionInteresado() != null)?truncaCampo(interesadoAsiento
					.getIdentificacionInteresado(),getLongMaxRazonSocial()):interesadoAsiento.getIdentificacionInteresado());
		} else {
			if (interesadoAsiento.getIdentificacionInteresadoDesglosada() == null) {
				throw new Exception(
						"Se requiere la identificacion del interesado de forma desglosada");
			}
			interesado.setNombre((interesadoAsiento.getIdentificacionInteresadoDesglosada().getNombre() != null)?truncaCampo(interesadoAsiento
					.getIdentificacionInteresadoDesglosada().getNombre(),getLongMaxNombre()):interesadoAsiento.getIdentificacionInteresadoDesglosada().getNombre());
			interesado.setApellido1((interesadoAsiento.getIdentificacionInteresadoDesglosada().getApellido1() != null)?truncaCampo(interesadoAsiento
					.getIdentificacionInteresadoDesglosada().getApellido1(),getLongMaxApellido1()):interesadoAsiento.getIdentificacionInteresadoDesglosada().getApellido1());
			interesado.setApellido2((interesadoAsiento.getIdentificacionInteresadoDesglosada().getApellido2() != null)?truncaCampo(interesadoAsiento
					.getIdentificacionInteresadoDesglosada().getApellido2(),getLongMaxApellido2()):interesadoAsiento.getIdentificacionInteresadoDesglosada().getApellido2());
		}
		
		if (interesadoAsiento.getDireccionCodificada() != null) {
			interesado.setPais(convertirCodigoPaisIsoAlfaToNum(interesadoAsiento.getDireccionCodificada().getPaisOrigen()));
			interesado.setProvincia(convertirLong(interesadoAsiento.getDireccionCodificada().getCodigoProvincia()));
			interesado.setLocalidad(convertirCodigoMunicipio(interesadoAsiento.getDireccionCodificada().getCodigoProvincia(), interesadoAsiento.getDireccionCodificada().getCodigoMunicipio()));
			interesado.setDireccion((interesadoAsiento.getDireccionCodificada().getDomicilio() != null)?truncaCampo(interesadoAsiento.getDireccionCodificada().getDomicilio(), getLongMaxDomicilio()):interesadoAsiento.getDireccionCodificada().getDomicilio());
			interesado.setCp((interesadoAsiento.getDireccionCodificada().getCodigoPostal() != null)?truncaCampo(interesadoAsiento.getDireccionCodificada().getCodigoPostal(), getLongMaxCp()):interesadoAsiento.getDireccionCodificada().getCodigoPostal());
			interesado.setEmail((interesadoAsiento.getDireccionCodificada().getEmail() != null)?truncaCampo(interesadoAsiento.getDireccionCodificada().getEmail(), getLongMaxEmail()):interesadoAsiento.getDireccionCodificada().getEmail());		
			interesado.setTelefono((interesadoAsiento.getDireccionCodificada().getTelefono() != null)?truncaCampo(interesadoAsiento.getDireccionCodificada().getTelefono(), getLongMaxTelefono()):interesadoAsiento.getDireccionCodificada().getTelefono());
		}
		
		return interesado;
	}
	
	// --------- Funciones auxiliares
	/**
	 * Trunca los campos del interesado a los valores máximos del REGWEB3.
	 * @param campo valor campo
	 * @param maximaLongitudCampo longitud maxima
	 * @return campo valor truncado
	 */
	private static String truncaCampo(String campo, int maximaLongitudCampo){
		if (campo.length() > maximaLongitudCampo){
			campo = campo.substring(0, maximaLongitudCampo);
		}
		return campo;
	}
	
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
	    
		WsClientSistraUtil.configurePort(bp, endpoint, user, pass);
	    
		// PARA PRUEBAS DESDE MAIN
		//WsClientUtil.configurePort(bp, endpoint, null, user, pass, "BASIC", true, true, true, true);
	    
	}

	/**
	 * Calcula codigo ISO numerico pais a partir de de codigo ISO alfanumerico. 
	 * @param codPaisIsoAlf codigo ISO pais alfanumerico. 
	 * @return codigo codigo ISO pais numerico
	 */
	private static Long convertirCodigoPaisIsoAlfaToNum(String codPaisIsoAlf) {
		Long res = null;
		if (codPaisIsoAlf != null) {
			String codPaisIsoNum = ConfiguracionRegweb3.getInstance().getProperty("regweb3.pais.iso.alf2num." + codPaisIsoAlf.toUpperCase());
			if (codPaisIsoNum != null) {
				res = new Long(codPaisIsoNum);
			}
		}
		return res;
	}
	
	/**
	 * Convierte a cod municipio en formato regweb (codprov + codmuni + dc).
	 * @param codProv cod provincia
	 * @param codMuni cod municipio
	 * @return cod municipio en formato regweb
	 */
	public static Long convertirCodigoMunicipio (String codProv, String codMuni) {
		Long res = null;
		if (StringUtils.isNotBlank(codProv) && StringUtils.isNotBlank(codMuni)){
			String cp = StringUtils.leftPad(codProv, 2, "0");
			String cm = StringUtils.leftPad(codMuni, 3, "0");
			String codIne = cm + calcularMunicipioDC(cp, cm);
			res = new Long (codIne);
		}
		return res;		
	}
	
	public static String obtenerEntidadAsiento(AsientoRegistral asiento)
			throws Exception {
		// Entidad: por compatibilidad con versiones anteriores si no existe entidad y el plugin solo soporta una entidad, se coge esa entidad
		String entidad = asiento.getDatosOrigen().getCodigoEntidad();
		if ( entidad == null) {
			String[] entidades = UtilsRegweb3.obtenerEntidades();
			if (entidades.length > 1) {
				throw new Exception("No se ha establecido codigo entidad asiento");
			}
			entidad = entidades[1];
		} else {
			verificarEntidad(entidad);
		}
		return entidad;
	}
	
	/**
	 * 
	  	De izquierda a derecha se etiquetan las columnas como C, B, A, C, B, A…
	 
		Los números de cada columna se sustituyen por otros de acuerdo a la columna a la que pertenezcan. De 0 a 9:
		
		A | 0 1 2 3 4 5 6 7 8 9 (se queda igual)
		B | 0 3 8 2 7 4 1 5 9 6
		C | 0 2 4 6 8 1 3 5 7 9
		
		Se suman los números así obtenidos y el dígito de control es lo que falta para alcanzar el siguiente múltiplo de 10 (0 si es múltiplo de 10, 10 – [suma de los dígitos mod. 10] en otro caso)
		
		Ejemplos (Verificados con el INE):
		
		17141
		CBACB
		25183
		
		2+5+1+8+3 = 19, el siguiente múltiplo de 10 es 20, luego el dígito de control es 20-19 = 1.
	  
	 * @param prov codigo provincia
	 * @param municipio codigo municipio
	 * @return  codigo control
	 */
	private static int calcularMunicipioDC(String prov, String municipio) {
		
		String colA[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
		String colB[] = {"0", "3", "8", "2", "7", "4", "1", "5", "9", "6"};
		String colC[] = {"0", "2", "4", "6", "8", "1", "3", "5", "7", "9"};
		
		String codMunicipio = prov + municipio;
		
		
		String encodeStr = "";
		for (int i=0; i < codMunicipio.length(); i++) {
			int col = i%3;
			int num = Integer.parseInt(codMunicipio.charAt(i) + "");
			switch (col) {
			case 0:
				encodeStr += colC[num];
				break;
			case 1:
				encodeStr += colB[num];
				break;
			case 2:
				encodeStr += colA[num];
				break;
			default:
				break;
			}
		}
		
		int sum = 0;
		for (int i=0; i < encodeStr.length(); i++) {
			sum += Integer.parseInt(""+ encodeStr.charAt(i));
		}
		
		int resto = sum % 10;
	    int dc = 0;
	    if (resto != 0)  {
	      dc = 10 - resto;
	    }
 
		return dc;
	}
	
	/**
	 * Convierte a long.
	 * @param num numero
	 * @return Long
	 */
	private static Long convertirLong(String num) {
		Long res = null;
		if (num != null){
			res = new Long(num);
		}
		return res;
	}
	
	private static String normalizaNumDoc(String numDoc){
		String doc = null;
		int validacion = NifCif.validaDocumento(numDoc);
		if (validacion == 5){
			doc = numDoc.substring(4);
		} else {
			doc = numDoc;
		}
		return doc;
	}
	
}
