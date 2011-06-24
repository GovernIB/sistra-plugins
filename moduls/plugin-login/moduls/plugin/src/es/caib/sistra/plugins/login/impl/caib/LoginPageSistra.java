package es.caib.sistra.plugins.login.impl.caib;

import java.util.Locale;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import es.caib.loginModule.util.LoginPage;
import es.caib.sistra.modelInterfaz.InformacionLoginTramite;
import es.caib.sistra.persistence.delegate.DelegateSISTRAUtil;


public class LoginPageSistra implements es.caib.loginModule.util.LoginPage{
	
	private static final String KEY_LANGUAGE_LOGIN ="es.caib.sistra.front.languageLoginPage";
	
	private static Properties textos = null;
	private HttpServletRequest request = null;	
	
	public void setRequest (HttpServletRequest request){
		this.request = request;		
	}
	
	
	/**
	 * Obtiene lenguaje de la request para mostrar la página de login en el lenguaje correcto
	 * @param request
	 * @return Lenguaje
	 */
	public Locale getLanguage() {
		 Locale locale = (Locale) request.getSession().getAttribute(KEY_LANGUAGE_LOGIN);
		 if (locale != null) return locale;
		 
		 String lang="es";		 
		 if (request.getParameter("lang") != null) lang = request.getParameter("lang");
		 if (request.getParameter("language") != null ) lang = request.getParameter("language");
		 		 		 		 		
		 return new Locale(lang);		 
	 }
     
	 /**
	 * Ante un cambio de idioma en la página de login almacenamos cambio de idioma en la request.
	 * Este dato será accedido por el sistema para realizar el cambio de lenguaje dandole preferencia
	 * al indicado en la request
	 * @param request
	 * 
	 */
	public void setLanguage(Locale locale) {
		request.getSession().setAttribute(KEY_LANGUAGE_LOGIN,locale);
	 }
	 
	 /**
	  * Obtiene texto de presentacion
	  * @param request
	  * @return Texto presentacion
	  */	 
	public String getHeader() {
    	try{
    		
    		// Caso especial autenticacion portal
    		if (request.getParameter("autenticaPortal")!= null) return "";
    		
	    	String lang = getLanguage().getLanguage();
	    	String modelo = request.getParameter("modelo");
	    	int version = Integer.parseInt(request.getParameter("version"));
	    	 
	    	 // Obtenemos descripcion trámite
	    	InformacionLoginTramite infoTramite = DelegateSISTRAUtil.getSistraDelegate().obtenerInfoLoginTramite(modelo,version,lang);
	    	String desc = infoTramite.getDescripcionTramite();
			
			// Establecemos cabecera
			String header = getTexto("parrafo1",lang) + ":</p>" +   
							"<p id=\"nomTramit\">" + desc + "</p>" + 
							"<p>" + getTexto("parrafo2",lang) +"</p><p>";
			
			return header;			
    	}catch(Exception ex){
    		return "Se ha producido un error inesperado. Si el error persiste contacte con el administrador";
    	}
		
     }
     
     /**
      * Devuelve hoja de estilos
      * @param request
      * @return
      */
	public String getStyleSheet() { 		
		/*
    	 String urlx = request.getRequestURL().toString();
    	 urlx = urlx.substring(0,urlx.indexOf(request.getContextPath())) +  
    		 			request.getContextPath() + "/estilos/login.css";
    	 return urlx;
    	 */
		return "/sistrafront/estilos/loginCAIB.css";
    
    	     	     	     	 
     }
          
     // Determinar qué tipo de acceso se requiere para la url invocada. Debe ser uno de los siguientes valores: 
     // NONE -> Acceso no permitido 
     // ANONYMOUS -> Identificado automáticamente de forma anónima 
     // FORM -> De cualquier forma 
     // BASIC -> Password o certificado 
     // CLIENT-CERT -> Certificado
     public int getLoginMethod() {
 		 try{	  
 			 String lang = getLanguage().getLanguage(); 
 			 String modelo = request.getParameter("modelo");
	    	 String version = request.getParameter("version");
	    	 String modo = request.getParameter("autenticacion");

	    	 // Comprobamos que sea un punto de entrada válido: debe ser init.do y debe tener como parámetros modelo y versión
	    	 String url = request.getRequestURL().toString();	    	
	    	 if (!url.contains(request.getContextPath() + "/protected/init.do")){    		 
	    		 // Si ya esta autenticado damos por buena la autenticacion anterior, devolvemos FORM
	    		 // Si no esta autenticado no dejamos pasar
	    		 if (request.getUserPrincipal() == null)
	    				return LoginPage.NONE;
	    			else
	    				return LoginPage.FORM;
	    	 }
	    	 	    	 	    	 
	    	 if (modelo == null || version == null) {
	    		 // Nuevo punto de entrada para autenticación del enlace del portal
	    		 if (request.getParameter("autenticaPortal")!= null){
	    			return LoginPage.BASIC;
	    		 }else{
	    			 return LoginPage.NONE;
	    		 }
	    	 }
	    	 
	    	// Obtenemos niveles permitidos    	 
	    	InformacionLoginTramite infoTramite = DelegateSISTRAUtil.getSistraDelegate().obtenerInfoLoginTramite(modelo,Integer.parseInt(version),lang);
	    	String niveles=infoTramite.getNivelesAutenticacion();
	 		
	 		// Filtramos niveles permitidos si se indica modo
	 		if (modo != null){
		 		String nivelesModo = "";
		 		if (niveles.indexOf("C") >= 0 && modo.indexOf("C") >= 0 ) nivelesModo += "C";
		 		if (niveles.indexOf("U") >= 0 && modo.indexOf("U") >= 0 ) nivelesModo += "U";
		 		if (niveles.indexOf("A") >= 0 && modo.indexOf("A") >= 0 ) nivelesModo += "A";
		 		niveles = nivelesModo;
	 		}	 		
	 		
	 		// Si sólo tiene acceso anónimo realizamos autenticación automática
	 		if ( "A".equals( niveles ) ){	 			
	 			return LoginPage.ANONYMOUS;
			}
	 		
	 		// Si contiene acceso anonimo, no esta autenticado y tiene activado el acceso anonimo por defecto
	 		// realizamos autenticacion automatica
	 		if (niveles.indexOf("A") >= 0 && infoTramite.isInicioAnonimoDefecto() && !autenticado()){
	 			return LoginPage.ANONYMOUS;
	 		}
	 		
	 		// Comprobamos si sólo tiene acceso por certificado
	 		if ("C".equals(niveles)){	 			
	 			return LoginPage.CLIENT_CERT;
	 		}
	 		
	 		// Comprobamos si tiene los tres
	 		if (niveles.indexOf("C") >= 0 && niveles.indexOf("U") >= 0 && niveles.indexOf("A") >= 0){	 			
	 			return LoginPage.FORM;
	 		}
	 		
	 		// Comprobamos si debe estar autenticado
	 		// (aunque en la plataforma se permita indicar sólo usuario en la gestión de login no)
	 		if (niveles.indexOf("C") >= 0 || niveles.indexOf("U") >= 0){
	 			return LoginPage.BASIC;
	 		}
	 		
	 		// Debería cumplir una de las demás	 		
	 		return LoginPage.NONE;
	 		
    	 }catch(Exception ex){    		 
    		 return LoginPage.NONE;
    	 }
 		
     }
     

     private String getTexto(String key,String lang){
    	 // Obtenemos properties con los textos    	 
    	 if (textos == null){    		 
    		 try{
	    		 Properties props = new Properties();    		 
	    		 props.load(this.getClass().getResourceAsStream("LoginPageSistra.properties"));
	    		 textos=props;
    		 }catch(Exception ex){
    			 ex.printStackTrace();
    		 }    		     		 
    	 }
    	 
    	 // Devolvemos textos
    	 return (String) textos.get(key + "." + lang);
    	 
     }


	public String getCertMessage() {
		return null;
	}

	public String getUserPasswordMessage() {
		return null;
	}


	public String getAnonymousMessage() {
		 Locale locale = getLanguage();
    	 return getTexto("anonimo",locale.getLanguage());    	 
	}
     
	
	/**
	 * URL del nombre del servidor
	 * null se interpreta como url configurada en el jboss-service.xml
	 */	 	 
	public String getServerNameUrl (){
		return  LoginUtil.getUrlSistra();
	}

	
	/**
	 * Comprueba si hay sesion de autenticacion establecido
	 * 
	 * TODO De momento miramos la cookie, habrá que cambiarlo al método seycon correspondiente una vez este operativo 
	 * 
	 * @return true/false
	 */
	private boolean autenticado(){
		return  LoginUtil.existeCookieAutenticada(request);
	}
}
