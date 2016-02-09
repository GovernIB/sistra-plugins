package es.caib.sistra.plugins.custodia.util;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Configuracion {

	private static Log log = LogFactory.getLog(Configuracion.class);
	
	private static Configuracion configuracion = null;
	private static final String PREFIX_SAR_PLUGINS = "es.caib.sistra.configuracion.plugins.";
	private static final String PREFIX_SAR_SISTRA = "es.caib.sistra.configuracion.sistra.";
	private Properties propiedades = null;
	
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
			if (key.startsWith(PREFIX_SAR_PLUGINS + "plugin-custodia")) {
				propiedades.put(key.substring((PREFIX_SAR_PLUGINS + "plugin-custodia").length() + 1), value);
			}			
		}
	}

	private void readPropertiesFromFilesystem() throws Exception {
		// Accedemos a propiedades
		propiedades = new Properties();
		propiedades.load(new FileInputStream(System.getProperty("ad.path.properties") + "sistra/plugins/plugin-custodia.properties"));									
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
	
}
