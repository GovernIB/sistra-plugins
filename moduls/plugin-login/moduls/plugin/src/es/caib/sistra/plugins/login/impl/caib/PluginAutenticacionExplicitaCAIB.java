package es.caib.sistra.plugins.login.impl.caib;

import es.caib.sistra.plugins.login.AutenticacionExplicitaInfo;
import es.caib.sistra.plugins.login.PluginAutenticacionExplicitaIntf;

/**
 *	Interfaz para establecer sistema de autenticaci�n expl�cita por usuario y password.
 *  <br/>
 *  Este plugin ser� utilizado cuando se seleccione la autenticaci�n expl�cita del organismo (p.e. en dominios).
 *  Al acceder al elemento, bien por EJB o por Webservice, se utilizar� la informaci�n de autenticaci�n suministrada por este plugin.
 *
 */
public class PluginAutenticacionExplicitaCAIB implements PluginAutenticacionExplicitaIntf {

	/**
	 * Obtiene informaci�n de autenticaci�n a utilizar cuando se utilice autenticaci�n expl�cita por usuario y password 
	 * @return Informaci�n de autenticaci�n (usuario y password)
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
