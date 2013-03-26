package es.caib.pagosTPV.persistence.delegate;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import es.caib.pagosTPV.model.NotificacionPagosTPV;
import es.caib.pagosTPV.model.RequestNotificacionTPV;
import es.caib.pagosTPV.persistence.intf.NotificacionPagosTPVFacade;
import es.caib.pagosTPV.persistence.util.NotificacionPagosTPVFacadeUtil;

/**
 * Business delegate para operar con asistente pagos
 */
public class NotificacionPagosTPVDelegate implements StatelessDelegate {

    /* ========================================================= */
    /* ======================== M�TODOS DE NEGOCIO ============= */
    /* ========================================================= */
	
	public void realizarNotificacion(RequestNotificacionTPV notificacionPago) throws DelegateException{
    	try {
            getFacade().realizarNotificacion(notificacionPago);
        } catch (Exception e) {
            throw new DelegateException(e);
        }
    }
	
	public NotificacionPagosTPV recuperarNotificacion(String orden) throws DelegateException{
    	try {
            return getFacade().recuperarNotificacion(orden);
        } catch (Exception e) {
            throw new DelegateException(e);
        }
    }

	
    /* ========================================================= */
    /* ======================== REFERENCIA AL FACADE  ========== */
    /* ========================================================= */
    private NotificacionPagosTPVFacade getFacade() throws NamingException,CreateException,RemoteException {      	    	
    	return NotificacionPagosTPVFacadeUtil.getHome().create();
    }

    protected NotificacionPagosTPVDelegate() throws DelegateException {       
    }

	               
}
