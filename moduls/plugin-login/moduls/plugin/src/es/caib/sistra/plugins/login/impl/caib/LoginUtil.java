package es.caib.sistra.plugins.login.impl.caib;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoginUtil {

	private static Log log = LogFactory.getLog(LoginUtil.class);
	
	private static String cookieAuth = null;
	
	private static String urlSistra = null;
	
	/**
	 * Obtiene nombre cookie auth 
	 */
	private static String getNombreCookieAuth(){
		if (cookieAuth == null) {
			try{
				cookieAuth =  ConfigurationUtil.getInstance().obtenerPropiedades().getProperty("auth.cookiename");									
			}catch(Exception ex){
				log.error("Error obteniendo propiedad 'auth.cookiename'",ex);
				cookieAuth = null;				
			}		
		}
		return cookieAuth;
	}	
	
	/**
    * Detecta si existe una cookie de seycon autenticada
	 * @param request
	 * @return
	 */
	public static boolean existeCookieAutenticada(HttpServletRequest request){
		boolean result = false;		
		log.debug("Comprobando si existe cookie autenticada");
		try{		
			Cookie cookies[] = request.getCookies();     					
			if (cookies != null){
	            for(int i = 0; i < cookies.length; i++)
	                if(cookies[i].getName().startsWith(getNombreCookieAuth()) && cookies[i].getValue() != null){
	                	
	                	log.debug("Cookie autenticacion: " + cookies[i].getValue() );
	                	
	                	String number = cookies[i].getValue().substring(cookies[i].getValue().lastIndexOf(":") + 1);
	                	if (number.startsWith("-")) number=number.substring(1);
	                	if (StringUtils.isNumeric(number)) result = true;
	                	break;
	                }
			}
			log.debug("Comprobando si existe cookie autenticada. Resultado: " + result);
			return result;
		}catch(Exception ex){
			log.error("Excepcion comprobando cookie de autenticacion. Devolvemos false.", ex);
			return false;
		}
	}
	
	
	/**
	 * Obtiene url donde esta sistra
	 */
	public static String getUrlSistra(){
		if (urlSistra == null) {
			try{
				urlSistra =  ConfigurationUtil.getInstance().obtenerPropiedades().getProperty("sistra.url");									
			}catch(Exception ex){
				log.error("Error obteniendo propiedad 'sistra.url'",ex);
				urlSistra = null;				
			}		
		}
		return urlSistra;
	}	
	
}
