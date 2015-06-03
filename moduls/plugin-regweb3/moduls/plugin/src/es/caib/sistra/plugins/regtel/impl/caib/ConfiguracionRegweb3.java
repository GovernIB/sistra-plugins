package es.caib.sistra.plugins.regtel.impl.caib;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConfiguracionRegweb3 {

	private static Log log = LogFactory.getLog(ConfiguracionRegweb3.class);
	
	private static ConfiguracionRegweb3 configuracion = null;
	private Properties props = null;
	
	private ConfiguracionRegweb3(){
		// Accedemos a propiedades
		try{
			props = new Properties();
			props.load(new FileInputStream(System.getProperty("ad.path.properties") + "sistra/plugins/plugin-regweb3.properties"));								
		}catch(Exception ex){
			log.error("Error obteniendo propiedades plugin",ex);						
		}	
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
