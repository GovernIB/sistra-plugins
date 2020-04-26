package es.caib.sistra.plugins.regtel.impl.caib;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConfiguracionRegweb3 {

	private static Log log = LogFactory.getLog(ConfiguracionRegweb3.class);
	private static final String PREFIX_SAR_PLUGINS = "es.caib.sistra.configuracion.plugins.";
	private static final String PREFIX_SAR_SISTRA = "es.caib.sistra.configuracion.sistra.";
	private static ConfiguracionRegweb3 configuracion = null;
	private Properties props = null;

	private ConfiguracionRegweb3(){
		// Accedemos a propiedades
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
		props = new Properties();
		Properties propSystem = System.getProperties();
		for (Iterator it = propSystem.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			String value = propSystem.getProperty(key);
			if (key.startsWith(PREFIX_SAR_PLUGINS + "plugin-regweb3")) {
				props.put(key.substring((PREFIX_SAR_PLUGINS + "plugin-regweb3").length() + 1), value);
			}
		}
	}


	private void readPropertiesFromFilesystem() throws Exception {
		props = new Properties();
		props.load(new FileInputStream(System.getProperty("ad.path.properties") + "sistra/plugins/plugin-regweb3-v3.0.properties"));
	}

	public static ConfiguracionRegweb3 getInstance(){
		if (configuracion == null){
			configuracion = new ConfiguracionRegweb3();
		}
		return configuracion;
	}

	public String getProperty(String property){
		return props.getProperty(property);
	}

}
