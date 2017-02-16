package es.caib.pagosTPV.front.controller;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.tiles.ComponentContext;
import org.apache.struts.tiles.Controller;
import org.apache.struts.util.MessageResources;

import es.caib.pagosTPV.front.Constants;
import es.caib.pagosTPV.front.util.LangUtil;
import es.caib.pagosTPV.front.util.PagosFrontRequestHelper;
import es.caib.pagosTPV.model.OrganismoInfo;
import es.caib.pagosTPV.persistence.delegate.SesionPagoDelegate;
import es.caib.util.ContactoUtil;

/**
 * Controller con métodos de utilidad.
 */
public abstract class BaseController implements Controller {

    public final void perform(ComponentContext tileContext,
                              HttpServletRequest request, HttpServletResponse response,
                              ServletContext servletContext)
            throws ServletException, IOException {
    	
    	try {
            execute(tileContext, request, response, servletContext);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    	
    	
    	// Obtenemos info organismo
    	OrganismoInfo info = (OrganismoInfo) request.getSession().getServletContext().getAttribute(Constants.ORGANISMO_INFO_KEY);
    	
    	// Generamos literal de contacto
		OrganismoInfo oi = (OrganismoInfo) servletContext.getAttribute(Constants.ORGANISMO_INFO_KEY);
		
		String lang = LangUtil.getLang(request);
		String telefono = oi.getTelefonoIncidencias();
		String email = oi.getEmailSoporteIncidencias();
		String url = oi.getUrlSoporteIncidencias();
		String asunto = (java.lang.String) request.getSession().getAttribute(es.caib.pagosTPV.front.Constants.DATOS_SESION_NOMBRE_TRAMITE_KEY);
		
		String literalContacto;
		try {
			literalContacto = ContactoUtil.generarLiteralContacto(telefono, email, url,
				asunto, lang);
		} catch (Exception e) {
		       throw new ServletException(e);	    
		}
		
		request.setAttribute("literalContacto", literalContacto);
    	
    }

    abstract public void execute(ComponentContext tileContext,
                                 HttpServletRequest request, HttpServletResponse response,
                                 ServletContext servletContext) throws Exception;

    protected MessageResources getResources(HttpServletRequest request) {
        return ((MessageResources) request.getAttribute(Globals.MESSAGES_KEY));

    }
        
	public  Locale getLocale(HttpServletRequest request) 
    {
        return PagosFrontRequestHelper.getLocale( request );
    }
    
    public String getLang(HttpServletRequest request) {
        return  PagosFrontRequestHelper.getLang( request );
    }
    
    public SesionPagoDelegate getSesionPago(HttpServletRequest request){
    	return PagosFrontRequestHelper.getSesionPago(request);
    }
    
    public String getUrlRetornoSistra( HttpServletRequest request )
    {
    	return  PagosFrontRequestHelper.getUrlRetornoSistra(request);
    }
       
}
