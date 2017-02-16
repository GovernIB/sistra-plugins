package es.caib.pagos.persistence.util;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.pagos.model.OrganismoInfo;

public class Configuracion {

	private static Log log = LogFactory.getLog(Configuracion.class);
	private static final String PREFIX_SAR_PLUGINS = "es.caib.sistra.configuracion.plugins.";
	private static final String PREFIX_SAR_SISTRA = "es.caib.sistra.configuracion.sistra.";
	private static Configuracion configuracion = null;
	private Properties propiedades = null;
	
	private OrganismoInfo organismoInfo;
	
	private Configuracion(){
		try{
			String sar = System.getProperty(PREFIX_SAR_SISTRA + "sar");
			if (sar != null && "true".equals(sar)) {
				readPropertiesFromSAR();
			} else {
				readPropertiesFromFilesystem();
			}
		}catch(Exception ex){
			log.error("Error obteniendo propiedades plugin",ex);						
		}
	}

	/**
	 * Lee propiedades desde SAR.
	 * @throws Exception
	 */
	private void readPropertiesFromSAR() throws Exception {
		propiedades = new Properties();
		Properties propSystem = System.getProperties();
		for (Iterator it = propSystem.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			String value = propSystem.getProperty(key);
			if (key.startsWith(PREFIX_SAR_SISTRA + "global")) {
				propiedades.put(key.substring((PREFIX_SAR_SISTRA + "global").length() + 1), value);
			}
			if (key.startsWith(PREFIX_SAR_PLUGINS + "plugin-pagos")) {
				propiedades.put(key.substring((PREFIX_SAR_PLUGINS + "plugin-pagos").length() + 1), value);
			}			
		}
	}

	private void readPropertiesFromFilesystem() throws Exception{
		// Accedemos a propiedades
		
			propiedades = new Properties();
			propiedades.load(new FileInputStream(System.getProperty("ad.path.properties") + "sistra/plugins/plugin-pagos.properties"));
			propiedades.load(new FileInputStream(System.getProperty("ad.path.properties") + "sistra/global.properties"));
		
	}
	
	public static Configuracion getInstance(){
		if (configuracion == null){
			configuracion = new Configuracion();
		}
		return configuracion;
	}
	
	public String getProperty(String property){
		return propiedades.getProperty(property);
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
	    		for (Iterator it=propiedades.keySet().iterator();it.hasNext();){
	    			String key = (String) it.next();
	    			if (key.startsWith("organismo.zonapersonal.titulo.")){
	    				organismoInfo.getTituloPortal().put(key.substring(key.lastIndexOf(".") +1),propiedades.get(key));
	    			}
	    			if (key.startsWith("organismo.zonapersonal.referencia.")){
	    				organismoInfo.getReferenciaPortal().put(key.substring(key.lastIndexOf(".") +1),propiedades.get(key));
	    			}
	    		}	    		
	    }         		
		return organismoInfo;
		
	}
}
