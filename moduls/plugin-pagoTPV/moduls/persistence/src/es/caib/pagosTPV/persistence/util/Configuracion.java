package es.caib.pagosTPV.persistence.util;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.pagosTPV.model.OrganismoInfo;

public class Configuracion {

	private static Log log = LogFactory.getLog(Configuracion.class);
	
	private static Configuracion configuracion = null;
	private Properties props = null;

	private OrganismoInfo organismoInfo;
	
	private Configuracion(){
		// Accedemos a propiedades
		try{
			props = new Properties();
			props.load(new FileInputStream(System.getProperty("ad.path.properties") + "sistra/plugins/plugin-pagosTPV.properties"));
			props.load(new FileInputStream(System.getProperty("ad.path.properties") + "sistra/global.properties"));
		}catch(Exception ex){
			log.error("Error obteniendo propiedades plugin",ex);						
		}	
	}
	
	public static Configuracion getInstance(){
		if (configuracion == null){
			configuracion = new Configuracion();
		}
		return configuracion;
	}
	
	public String getProperty(String property){
		return props.getProperty(property);
	}

	public String getUrlAsistenteInicio() {
		return getProperty("tpv.urlAsistenteInicio");
	}
	
	public String getUrlTPV() {
		return getProperty("tpv.urlTPV");
	}
	
	public String getUrlTPVRetornoOK() {
		return getProperty("tpv.urlRetornoOK");
	}
	
	public String getUrlTPVRetornoKO() {
		return getProperty("tpv.urlRetornoKO");
	}
	
	public String getMerchantCurrencyTPV() {
		return getProperty("tpv.merchantCurrency");
	}
	
	public String getUrlTPVNotificacion() {
		return getProperty("tpv.urlNotificacion");
	}
	
	public String getIdiomaTPV(String idioma) {
		return getProperty("tpv.idioma." + idioma);
	}
	
	public String getMerchantNameTPV(String comercio) {
		return getProperty("tpv." + comercio + ".merchantName");
	}
	
	public String getMerchantCodeTPV(String comercio) {
		return getProperty("tpv." + comercio + ".merchantCode");
	}
	
	public String getMerchantTerminalTPV(String comercio) {
		return getProperty("tpv." + comercio + ".merchantTerminal");
	}

	public String getMerchantPasswordTPV(String comercio) {
		return getProperty("tpv." + comercio + ".merchantPassword");
	}
	
	public String getMerchantTransactionTypeAut() {
		return getProperty("tpv.merchantTransactionTypeAut");
	}
	
	public String getModeloRDSDocumentoPagoPresencial() {
		return getProperty("tpv.documentoPagoPresencial.modelo");
	}
	
	public int getVersionRDSDocumentoPagoPresencial() {
		String versStr = getProperty("tpv.documentoPagoPresencial.version");
		return Integer.parseInt(versStr);
	}
	
	public String getPlantillaRDSDocumentoPagoPresencial() {
		return getProperty("tpv.documentoPagoPresencial.plantilla");
	}
	
	
	public String getDocumentoPagoPresencialInstrucciones(String comercio, String idioma) {
		return getProperty("tpv." + comercio + ".documentoPagoPresencial.instrucciones." + idioma);
	}
	
	public String getDocumentoPagoPresencialEntidad1Nombre(String comercio) {
		return getProperty("tpv." + comercio + ".documentoPagoPresencial.entidad1.nombre");
	}
	
	public String getDocumentoPagoPresencialEntidad1Cuenta(String comercio) {
		return getProperty("tpv." + comercio + ".documentoPagoPresencial.entidad1.cuenta");
	}
	
	public String getDocumentoPagoPresencialEntidad2Nombre(String comercio) {
		return getProperty("tpv." + comercio + ".documentoPagoPresencial.entidad2.nombre");
	}
	
	public String getDocumentoPagoPresencialEntidad2Cuenta(String comercio) {
		return getProperty("tpv." + comercio + ".documentoPagoPresencial.entidad2.cuenta");
	}
	
	public String getDocumentoPagoPresencialEntidad3Nombre(String comercio) {
		return getProperty("tpv." + comercio + ".documentoPagoPresencial.entidad3.nombre");
	}
	
	public String getDocumentoPagoPresencialEntidad3Cuenta(String comercio) {
		return getProperty("tpv." + comercio + ".documentoPagoPresencial.entidad3.cuenta");
	}
	
	public String getPrefijoOrden() {
		return StringUtils.defaultString(getProperty("tpv.orderPrefix"));
	}

	/**
	 * Unifica las propiedades del organismo en un objeto
	 * @return Propiedades configuracion
	 * @throws Exception
	 */
	public OrganismoInfo obtenerOrganismoInfo() throws Exception{
		// Creamos info para el organismo
		if (organismoInfo == null){
				organismoInfo = new  OrganismoInfo();
				organismoInfo.setNombre(getProperty("organismo.nombre"));
				organismoInfo.setUrlLogo(getProperty("organismo.logo"));
				organismoInfo.setUrlLoginLogo(getProperty("organismo.logo.login"));
				organismoInfo.setUrlPortal(getProperty("organismo.portal.url"));
				organismoInfo.setPieContactoHTML(getProperty("organismo.footer.contacto"));
				organismoInfo.setTelefonoIncidencias(getProperty("organismo.soporteTecnico.telefono"));
				organismoInfo.setUrlSoporteIncidencias(getProperty("organismo.soporteTecnico.url"));
				organismoInfo.setEmailSoporteIncidencias(getProperty("organismo.soporteTecnico.email"));
				organismoInfo.setUrlCssCustom(getProperty("organismo.cssCustom"));
				organismoInfo.setUrlLoginCssCustom(getProperty("organismo.cssLoginCustom"));
				
				// Obtenemos titulo y referencia a la zona personal
	    		for (Iterator it=props.keySet().iterator();it.hasNext();){
	    			String key = (String) it.next();
	    			if (key.startsWith("organismo.zonapersonal.titulo.")){
	    				organismoInfo.getTituloPortal().put(key.substring(key.lastIndexOf(".") +1),props.get(key));
	    			}
	    			if (key.startsWith("organismo.zonapersonal.referencia.")){
	    				organismoInfo.getReferenciaPortal().put(key.substring(key.lastIndexOf(".") +1),props.get(key));
	    			}
	    		}	    		
	    }         		
		return organismoInfo;
		
	}
	
	
		
}
