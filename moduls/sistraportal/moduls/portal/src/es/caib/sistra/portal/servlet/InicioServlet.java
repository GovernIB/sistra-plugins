package es.caib.sistra.portal.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.caib.sistra.plugins.login.impl.caib.LoginUtil;

/**
 * @web.servlet name="inicio"
 * @web.servlet-mapping url-pattern="/inicio"
 */
public class InicioServlet extends HttpServlet 
{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	// Invalidamos session para forzar siempre la autenticacion desde la cookie
    	request.getSession().invalidate();
    	
    	// Si existe una cookie autenticada de seycon autenticamos este modulo
    	String urlFin;
    	if (LoginUtil.existeCookieAutenticada(request)){
    		urlFin = request.getContextPath() + "/protected/autenticarCookie.do?" + request.getQueryString();
    	}else{    	
    	// sino redirigimos a enlace portal
    		urlFin = request.getContextPath() + "/enlacePortal.do?" + request.getQueryString();
    	}
    	
    	// Redirigimos
     	response.sendRedirect(urlFin);	
	}
}
