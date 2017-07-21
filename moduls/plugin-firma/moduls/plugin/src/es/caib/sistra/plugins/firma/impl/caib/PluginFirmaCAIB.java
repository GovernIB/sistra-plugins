package es.caib.sistra.plugins.firma.impl.caib;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.signatura.api.Signature;
import es.caib.sistra.plugins.firma.FicheroFirma;
import es.caib.sistra.plugins.firma.FirmaIntf;
import es.caib.sistra.plugins.firma.PluginFirmaIntf;
import es.caib.util.FirmaUtil;

public class PluginFirmaCAIB implements PluginFirmaIntf{
	
	private static Log log = LogFactory.getLog(PluginFirmaCAIB.class);
	
	private static final String PREFIX_SAR_PLUGINS = "es.caib.sistra.configuracion.plugins.";
	private static final String PREFIX_SAR_SISTRA = "es.caib.sistra.configuracion.sistra.";
	
	/**
	 * Obtiene proveedor
	 */
	public String getProveedor() {
		// return PluginFirmaIntf.PROVEEDOR_CAIB;
		return PluginFirmaIntf.PROVEEDOR_FIRMAWEB;
	}
	
	/**
	 * Realiza firma
	 */
	public FirmaIntf firmar(InputStream datos,String nombreCertificado, Map parametros) throws Exception {
		// Recogemos parametros
		if (parametros == null) throw new Exception("No se han pasado los parametros necesarios: content type y pin");
		String contentType = (String) parametros.get(FirmaUtil.CAIB_PARAMETER_CONTENT_TYPE);
		String pinCertificado = (String) parametros.get(FirmaUtil.CAIB_PARAMETER_PIN);
		if (StringUtils.isEmpty(contentType)) throw new Exception("No se ha establecido el content type");
		if (StringUtils.isEmpty(pinCertificado)) throw new Exception("No se ha establecido el content type");
		
		// Realizamos firma
		Signature signature = null;
		try {
			UtilFirmaCAIB firmaUtil = new UtilFirmaCAIB();
			firmaUtil.iniciarDispositivo();
			String firmaStr = firmaUtil.firmaInputStream(datos,nombreCertificado,pinCertificado,contentType);
			signature = UtilFirmaCAIB.deserializaFirmaFromString(firmaStr);						
		} catch (Exception ex) {
			log.error("Excepcion firma",ex);
			throw new Exception("Excepcion realizando firma",ex);
		}
		
		// Devolvemos firma
		FirmaCAIB firma = new FirmaCAIB();
		firma.setSignature(signature);
		firma.setSmime(null);
		firma.setFormatoFirma(FirmaCAIB.FORMATO_FIRMA_SIGNATURE);
		return firma;
	}

	/**
	 * Verifica firma
	 */
	public boolean verificarFirma(InputStream datos, FirmaIntf firma) throws Exception {
		try{
		 	UtilFirmaCAIB f = new UtilFirmaCAIB();
			f.iniciarDispositivo();		
			return f.verificarFirma( datos, ((FirmaCAIB) firma).getSignature(), true );
		}catch (Exception e){
			log.error ("Error al verificar firma: " + e.getMessage(),e);
			return false;
		}
	}

	/**
	 * Parsea la firma proveniente del html
	 */
	public FirmaIntf parseFirmaFromHtmlForm(String signatureHtmlForm) throws Exception {
		// Convertimos firma a objeto signature
		Signature signature = null;
	
		try {
			UtilFirmaCAIB f = new UtilFirmaCAIB();
			f.iniciarDispositivo();
			signature = f.smimeB64UrlSafeToSignature(signatureHtmlForm);
			if (signature == null) {
				throw new Exception("No se ha podido obtener firma");
			}
		} catch (Exception ex) {
			log.error("Excepcion interpretando firma como SMIME: " + ex.getMessage(), ex);
			throw new Exception("Error interpretando firma");			
		}			
		
		// Devolvemos firma
		FirmaCAIB firma = new FirmaCAIB();
		firma.setSignature(signature);
		firma.setSmime(signatureHtmlForm);
		firma.setFormatoFirma(FirmaCAIB.FORMATO_FIRMA_SMIME);
		
		return firma;
	}

	/**
	 * Deserializa firma y la convierte en un objeto de firma
	 */
	public FirmaIntf parseFirmaFromBytes(byte[] firmaBytes, String formatoFirma) throws Exception {
		// Convertimos firma a objeto signature
		FirmaCAIB firma = null;
		if (FirmaCAIB.FORMATO_FIRMA_SMIME.equals(formatoFirma)) {
			firma = (FirmaCAIB) parseFirmaFromHtmlForm(new String(firmaBytes, "UTF-8"));
		} else {
			Signature signature = UtilFirmaCAIB.deserializaFirmaFromBytes(firmaBytes);
			firma = new FirmaCAIB();
			firma.setSignature(signature);
			firma.setSmime(null);
			firma.setFormatoFirma(FirmaCAIB.FORMATO_FIRMA_SIGNATURE);
		}
		return firma;
	}

	/**
	 * Serializa firma para ser almacenada como un conjunto de bytes
	 */
	public byte[] parseFirmaToBytes(FirmaIntf firma)  throws Exception {
		return UtilFirmaCAIB.parseFirmaToBytes(firma);		
	}

	

	/**
	 * Genera fichero con la firma (SMIME)
	 */
	public FicheroFirma parseFirmaToFile(InputStream datosFirmados,FirmaIntf firma)  throws Exception {
		
		byte[] smime = null;
		if (FirmaCAIB.FORMATO_FIRMA_SMIME.equals(firma.getFormatoFirma())) {
			smime = ((FirmaCAIB) firma).getSmime().getBytes("UTF-8");
		} else {
			// Generamos smime
			UtilFirmaCAIB f = new UtilFirmaCAIB();
			f.iniciarDispositivo();
			ByteArrayOutputStream bos = new ByteArrayOutputStream(500);
			f.generarSMIME(datosFirmados,((FirmaCAIB) firma).getSignature() ,bos);				
			smime = bos.toByteArray();
		}
		
		// Devolvemos fichero
		FicheroFirma fic = new FicheroFirma();
		fic.setContenido(smime);
		fic.setNombreFichero("firma.smime");
		return fic;
	}
	
	/**
	 * 	Obtiene content type asociado. Esta funcion no esta en la interfaz del plugin de firma.
	 * @throws Exception 
	 */
	public String getContentType(String tipoContentType) throws Exception{
		Properties props = readProperties();
		return props.getProperty(tipoContentType);		
	}
	
	/**
	 * Lee las propiedades de los ficheros de configuracion
	 * @throws Exception 
	 *
	 */
	private Properties readProperties() throws Exception{
		String sar = System.getProperty(PREFIX_SAR_SISTRA + "sar");
		Properties result;
		if (sar != null && "true".equals(sar)) {
			result = readPropertiesFromSAR();
		} else {
			result = readPropertiesFromFilesystem();
		}		
		return result;
	}

	/**
	 * Lee propiedades desde SAR.
	 * @throws Exception
	 */
	private Properties readPropertiesFromSAR() throws Exception {
		Properties propiedades = new Properties();
		Properties propSystem = System.getProperties();
		for (Iterator it = propSystem.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			String value = propSystem.getProperty(key);			
			if (key.startsWith(PREFIX_SAR_PLUGINS + "plugin-firma")) {
				propiedades.put(key.substring((PREFIX_SAR_PLUGINS + "plugin-firma").length() + 1), value);
			}			
		}
		return propiedades;
	}
	
	private Properties readPropertiesFromFilesystem() throws Exception {
		InputStream fisModul=null; 
		 Properties propiedades = new Properties();
         try {
        	 // Path directorio de configuracion
        	 String pathConf = System.getProperty("ad.path.properties");
        	 fisModul = new FileInputStream(pathConf + "sistra/plugins/plugin-firma.properties");
    		 propiedades.load(fisModul);     
    		 return propiedades;
         } catch (Exception e) {
        	 propiedades = null;
             throw new Exception("Excepcion accediendo a las propiedadades del plugin de firma", e);
         } finally {             
             try{if (fisModul != null){fisModul.close();}}catch(Exception ex){}
         }
	}

	public FirmaIntf parseFirmaFromWS(byte[] firmaBytes, String formatoFirma) throws Exception {
		return parseFirmaFromBytes(firmaBytes, formatoFirma);
	}

	public byte[] parseFirmaToWS(FirmaIntf firma) throws Exception {
		return parseFirmaToBytes(firma);
	}
	
}
