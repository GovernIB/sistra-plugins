package es.caib.pagosTPV.front.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.pagosTPV.model.RequestNotificacionTPV;
import es.caib.pagosTPV.persistence.delegate.DelegateUtil;


/**
 * Servlet invocado por el TPV para enviar el resultado de un proceso de pago.
 * Almacena notificacion en tabla de notificaciones
 *
 * @web.servlet name="notificacionTPV"
 * @web.servlet-mapping url-pattern="/notificacionTPV"
 */
public class NotificacionTPVServlet extends HttpServlet {
	
	protected static Log log = LogFactory.getLog(NotificacionTPVServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	throw new ServletException("GET method not supported.");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	RequestNotificacionTPV requestNotif = new RequestNotificacionTPV();
    	requestNotif.setSignatureVersion(request.getParameter("Ds_SignatureVersion"));
        requestNotif.setMerchantParameters(request.getParameter("Ds_MerchantParameters"));
        requestNotif.setSignature(request.getParameter("Ds_Signature"));
        log.debug("Notificacion TPV recibida: \n" + requestNotif.print());
        try {
        	DelegateUtil.getNotificacionPagosTPVDelegateDelegate().realizarNotificacion(requestNotif);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch(Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new ServletException(e);
        }
    }

}
