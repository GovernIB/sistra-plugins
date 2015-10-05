package es.caib.pagosTPV.persistence.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import net.sf.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.pagosTPV.model.NotificacionPagosTPV;
import es.caib.pagosTPV.model.RequestNotificacionTPV;

/**
 * SessionBean que implementa la interfaz de NotificacionPagosTPV
 *
 * @ejb.bean
 *  name="pagos/persistence/NotificacionPagosTPVFacade"
 *  jndi-name="es.caib.pagosTPV.persistence.NotificacionPagosTPVFacade"
 *  type="Stateless"
 *  view-type="remote" 
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 * 
 */
public class NotificacionPagosTPVFacadeEJB extends HibernateEJB  {
 
	private static Log log = LogFactory.getLog(NotificacionPagosTPVFacadeEJB.class);
	
	/**
     * @ejb.create-method
     * @ejb.permission role-name = "${role.todos}"
     */
	public void ejbCreate() throws CreateException {	
		super.ejbCreate();
	}
	
	public void ejbActivate() throws EJBException, RemoteException {
	}

	public void ejbPassivate() throws EJBException, RemoteException {
	}
	
	/**
	 * Notificacion pago por parte del TPV.
	 * 
     * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
     */
	public void realizarNotificacion(RequestNotificacionTPV notificacionPago) {
		
		log.debug("Realizar notificacion pago: " + notificacionPago.getOrder());
		
		if (notificacionPago.getOrder() == null) {
			log.error("No se ha recibido parametro orden. Contenido orden: \n" + notificacionPago.print());
			throw new EJBException("No se ha recibido parametro orden");
		}
		
		if (recuperarNotificacion(notificacionPago.getOrder()) != null) {
			log.error("Ya existe una notificacion referente a la orden: " + notificacionPago.getOrder());
			throw new EJBException("Ya existe una notificacion referente a la orden: " + notificacionPago.getOrder());
		}
		
		Session session = getSession();
		
		try{
			log.debug("Guardar notificacion pago: " + notificacionPago.getOrder());
			
			NotificacionPagosTPV notif = new NotificacionPagosTPV();
			notif.setOrden(notificacionPago.getOrder());
			notif.setLocalizador(notificacionPago.getMerchantData());
			notif.setResultado(notificacionPago.getResponse());
			notif.setAutorizacion(notificacionPago.getAuthorisationCode());
			notif.setFirma(notificacionPago.getSignature());
			notif.setFecha(notificacionPago.getDate());
			notif.setHora(notificacionPago.getHour());
			
			session.save(notif);
			
			log.debug("Guardada notificacion pago: " + notificacionPago.getOrder());
			
		}catch (Exception ex){
			log.error("Exception guardando notificacion pago",ex);
			throw new EJBException("Exception guardando notificacion pago",ex);
		}finally{
            close(session);
        }
	}
	
	
	/**
	 * Recupera notificacion pago por parte del TPV.
	 * @return 
	 * 
     * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
     */
	public NotificacionPagosTPV recuperarNotificacion(String orden) {
		Session session = getSession();
		try{
			NotificacionPagosTPV notif = (NotificacionPagosTPV) session.get(NotificacionPagosTPV.class, orden);
			return notif;			
		}catch (Exception ex){
			log.error("Exception recuperando notificacion pago",ex);
			throw new EJBException("Exception recuperando notificacion pago",ex);
		}finally{
            close(session);
        }
	}
	

}
