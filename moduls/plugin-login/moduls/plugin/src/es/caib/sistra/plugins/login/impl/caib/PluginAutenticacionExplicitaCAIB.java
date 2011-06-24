package es.caib.sistra.plugins.login.impl.caib;

import es.caib.sistra.plugins.login.AutenticacionExplicitaInfo;
import es.caib.sistra.plugins.login.PluginAutenticacionExplicitaIntf;

/**
 *	Interfaz para establecer sistema de autenticación explícita por usuario y password.
 *  <br/>
 *  Este plugin será utilizado cuando se seleccione la autenticación explícita del organismo (p.e. en dominios).
 *  Al acceder al elemento, bien por EJB o por Webservice, se utilizará la información de autenticación suministrada por este plugin.
 *
 */
public class PluginAutenticacionExplicitaCAIB implements PluginAutenticacionExplicitaIntf {

	/**
	 * Obtiene información de autenticación a utilizar cuando se utilice autenticación explícita por usuario y password 
	 * @return Información de autenticación (usuario y password)
	 */
	public AutenticacionExplicitaInfo getAutenticacionInfo() {	
		try{
			AutenticacionExplicitaInfo ae = new AutenticacionExplicitaInfo();
			ae.setUser(ConfigurationUtil.getInstance().obtenerPropiedades().getProperty("auto.user"));
			ae.setPassword(ConfigurationUtil.getInstance().obtenerPropiedades().getProperty("auto.pass"));
			return ae;		
		}catch(Exception ex){
			throw new RuntimeException("No se ha podido establecer informacion de autenticacion explicita");
		}
	}
	
	
	
	
	 
}
