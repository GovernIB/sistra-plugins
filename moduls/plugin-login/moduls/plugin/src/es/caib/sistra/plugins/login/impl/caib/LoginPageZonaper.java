package es.caib.sistra.plugins.login.impl.caib;

import java.util.Locale;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import es.caib.loginModule.util.LoginPage;


public class LoginPageZonaper implements LoginPage{
	
	private static final String KEY_LANGUAGE_LOGIN ="es.caib.zonaper.front.languageLoginPage";
	
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
		 		 	
		 if (!"es".equals(lang) && !"ca".equals(lang) && !"en".equals(lang)  ) {
			 lang = "es";
		 }
		 
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
			String lang = getLanguage().getLanguage();
			
			// Establecemos cabecera
			String header = getTexto("parrafo1",lang) + "</p>" +  			
							"<p>" + getTexto("parrafo2",lang) + "</p><p>";
			
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
    	 return "/zonaperfront/estilos/loginCAIB.css";    	    	     	     	     	
     }
          
	 // Determinar qué tipo de acceso se requiere para la url invocada. Debe ser uno de los siguientes valores: 
     // NONE -> Acceso no permitido 
     // ANONYMOUS -> Identificado automáticamente de forma anónima 
     // FORM -> De cualquier forma 
     // BASIC -> Password o certificado 
     // CLIENT-CERT -> Certificado
     public int getLoginMethod() {
 		 try{	    	 
	    	 
	    	 // Comprobamos que sea un punto de entrada válido: debe ser init.do
	    	 String url = request.getRequestURL().toString();	    	
	    	 if (!url.contains(request.getContextPath() + "/protected/init.do")){
	    		 // Si ya esta autenticado damos por buena la autenticacion anterior, devolvemos FORM
	    		 // Si no esta autenticado no dejamos pasar
	    		 if (request.getUserPrincipal() == null)
	    				return LoginPage.NONE;
	    			else
	    				return LoginPage.FORM;
	    	 }	    	 
	    	 
	    	 // Controlamos si filtramos niveles
	    	 String niveles = request.getParameter("autenticacion");
	    	 if (niveles != null){
	    		 
	    		 // Si sólo tiene acceso anónimo realizamos autenticación automática
	 	 		if ( "A".equals( niveles ) ){	 			
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
	    	 }else{	    	 
	    		 // Permitimos todos los niveles 
	    		 return LoginPage.FORM;
	    	 }
	 		
    	 }catch(Exception ex){
    		 return LoginPage.NONE;
    	 }
 		
     }
     

     private String getTexto(String key,String lang){
    	 // Obtenemos properties con los textos    	 
    	 if (textos == null){    		 
    		 try{
	    		 Properties props = new Properties();    		 
	    		 props.load(this.getClass().getResourceAsStream("LoginPageZonaper.properties"));
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
}
