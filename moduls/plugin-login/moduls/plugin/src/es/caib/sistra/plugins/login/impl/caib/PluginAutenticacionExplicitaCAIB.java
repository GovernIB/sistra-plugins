package es.caib.sistra.plugins.login.impl.caib;

import org.apache.commons.lang.StringUtils;

import es.caib.sistra.plugins.login.AutenticacionExplicitaInfo;
import es.caib.sistra.plugins.login.ConstantesLogin;
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
	 * @param tipoElemento Id elemento que se esta accediendo (T: tramite / D: dominio)
	 * @param idElemento Id elemento que se esta accediendo (idtramite / iddominio)
	 * @return Información de autenticación (usuario y password)
	 */
	public AutenticacionExplicitaInfo getAutenticacionInfo(char tipoElemento,
			String idElemento) {
		try{
			AutenticacionExplicitaInfo ae = new AutenticacionExplicitaInfo();
			
			// Obtenemos usr/pass por defecto
			String userDefault = ConfigurationUtil.getInstance().obtenerPropiedades().getProperty("auto.user");
			String passwdDefault = ConfigurationUtil.getInstance().obtenerPropiedades().getProperty("auto.pass");
			
			// Prefijo
			String prefijo = "TRAMITE.";
			if (ConstantesLogin.TIPO_DOMINIO == tipoElemento) {
				prefijo = "DOMINIO.";
			}
			
			// Buscamos si existe configuracion para el elemento
			String userElemento = ConfigurationUtil.getInstance().obtenerPropiedades().getProperty(prefijo + idElemento + ".user");
			String passElemento = ConfigurationUtil.getInstance().obtenerPropiedades().getProperty(prefijo + idElemento + ".pass");
			if (userElemento != null && userElemento.length() > 0) {
				// Si existe ponemos usuario elemento
				ae.setUser(userElemento);
				ae.setPassword(passElemento);
			} else {
				// Si no existe ponemos usuario por defecto
				ae.setUser(userDefault);
				ae.setPassword(passwdDefault);
			}
			
			return ae;		
		}catch(Exception ex){
			throw new RuntimeException("No se ha podido establecer informacion de autenticacion explicita");
		}
	}
	
	
	
	
	 
}
