package es.caib.pagosTPV.front.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        throw new ServletException("GET method not supported.");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	RequestNotificacionTPV requestNotif = new RequestNotificacionTPV();
    	requestNotif.setDate              (request.getParameter("Ds_Date"));
        requestNotif.setHour              (request.getParameter("Ds_Hour"));
        requestNotif.setAmount            (request.getParameter("Ds_Amount"));
        requestNotif.setCurrency          (request.getParameter("Ds_Currency"));
        requestNotif.setOrder             (request.getParameter("Ds_Order"));
        requestNotif.setMerchantCode      (request.getParameter("Ds_MerchantCode"));
        requestNotif.setTerminal          (request.getParameter("Ds_Terminal"));
        requestNotif.setSignature         (request.getParameter("Ds_Signature"));
        requestNotif.setResponse          (request.getParameter("Ds_Response"));
        requestNotif.setMerchantData      (request.getParameter("Ds_MerchantData"));
        requestNotif.setSecurePayment     (request.getParameter("Ds_SecurePayment"));
        requestNotif.setTransactionType   (request.getParameter("Ds_TransactionType"));
        requestNotif.setCardCountry       (request.getParameter("Ds_Card_Country"));
        requestNotif.setAuthorisationCode (request.getParameter("Ds_AuthorisationCode"));
        requestNotif.setConsumerLanguage  (request.getParameter("Ds_ConsumerLanguage"));
        requestNotif.setCardType          (request.getParameter("Ds_Card_Type"));

        try {
        	DelegateUtil.getNotificacionPagosTPVDelegateDelegate().realizarNotificacion(requestNotif);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch(Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new ServletException(e);
        }
    }

}
