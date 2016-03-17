package es.caib.pagosTPV.persistence.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import net.sf.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.pagosTPV.model.NotificacionPagosTPV;
import es.caib.pagosTPV.model.RequestNotificacionTPV;
import es.caib.pagosTPV.model.RequestNotificacionTPVDecoded;
import es.caib.pagosTPV.persistence.util.PagoTPVUtil;

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
		
		log.debug("Realizar notificacion pago: decodificamos datos");
		RequestNotificacionTPVDecoded notificacionPagoDecoded = null;
		try {
			notificacionPagoDecoded = PagoTPVUtil.decodeRequestNotificacionTPV(notificacionPago);
		} catch (Exception e) {
			throw new EJBException("No se ha podido decodificar los datos del pago");
		}
		
		log.debug("Realizar notificacion pago: " + notificacionPagoDecoded.getOrder());
		
		if (notificacionPagoDecoded.getOrder() == null) {
			log.error("No se ha recibido parametro orden. Contenido orden: \n" + notificacionPagoDecoded.print());
			throw new EJBException("No se ha recibido parametro orden");
		}
		
		if (recuperarNotificacion(notificacionPagoDecoded.getOrder()) != null) {
			log.error("Ya existe una notificacion referente a la orden: " + notificacionPagoDecoded.getOrder());
			throw new EJBException("Ya existe una notificacion referente a la orden: " + notificacionPagoDecoded.getOrder());
		}
		
		Session session = getSession();
		
		try{
			log.debug("Guardar notificacion pago: " + notificacionPagoDecoded.getOrder());
			
			NotificacionPagosTPV notif = new NotificacionPagosTPV();
			notif.setOrden(notificacionPagoDecoded.getOrder());
			notif.setLocalizador(notificacionPagoDecoded.getMerchantData());
			notif.setResultado(notificacionPagoDecoded.getResponse());
			notif.setAutorizacion(notificacionPagoDecoded.getAuthorisationCode());
			notif.setDatosFirmados(notificacionPago.getMerchantParameters());
			notif.setFirma(notificacionPagoDecoded.getSignature());
			notif.setFecha(notificacionPagoDecoded.getDate());
			notif.setHora(notificacionPagoDecoded.getHour());
			
			session.save(notif);
			
			log.debug("Guardada notificacion pago: " + notificacionPagoDecoded.getOrder());
			
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
