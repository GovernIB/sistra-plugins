package es.caib.pagosTPV.persistence.ejb;

import java.rmi.RemoteException;
import java.security.Principal;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.audita.modelInterfaz.ConstantesAuditoria;
import es.caib.audita.modelInterfaz.Evento;
import es.caib.audita.persistence.delegate.DelegateAUDUtil;
import es.caib.pagosTPV.model.ModeloPagosTPV;
import es.caib.pagosTPV.model.NotificacionPagosTPV;
import es.caib.pagosTPV.model.RequestNotificacionTPV;
import es.caib.pagosTPV.model.RequestNotificacionTPVDecoded;
import es.caib.pagosTPV.persistence.util.Configuracion;
import es.caib.pagosTPV.persistence.util.PagoTPVUtil;
import es.caib.sistra.plugins.PluginFactory;
import es.caib.sistra.plugins.login.ConstantesLogin;
import es.caib.sistra.plugins.login.PluginLoginIntf;
import es.caib.sistra.plugins.pagos.ConstantesPago;

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
     * @ejb.permission role-name = "${role.auto}"
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
	public NotificacionPagosTPV realizarNotificacion(RequestNotificacionTPV notificacionPago) {
		// Inserta notificacion en tabla de notificaciones
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
			
			return notif;
			
		}catch (Exception ex){
			log.error("Exception guardando notificacion pago",ex);
			throw new EJBException("Exception guardando notificacion pago",ex);
		}finally{
            close(session);
        }				
	}

	
	/**
	 * Notificacion pago por parte del TPV.
	 * 
     * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
     * @ejb.permission role-name="${role.auto}"
     */
	public boolean confirmarSesionPago(String localizador, String identificadorPago) {
		
		// Comprobamos si existe notificacion pagada
		NotificacionPagosTPV notif = recuperarNotificacionPorLocalizadorIdPago(localizador, identificadorPago);
		if (notif == null) {
			return false;
		}
		
		
		Session session = getSession();
		try {
			
			// Verificamos si la notificacion es de pago realizado
			if (!notif.isPagada()) {
				return false;
			}
			
			// Obtenemos sesion de pago
			Query query = session
					.createQuery("FROM ModeloPagosTPV AS mp WHERE mp.localizador = '"
							+ localizador + "'");
			if (query.list().isEmpty()) {
				throw new Exception("No se encuentra sesion de pago con localizador " + localizador);
			}	
			ModeloPagosTPV mp = (ModeloPagosTPV) query.uniqueResult();
			
			// Verificamos firma
			String merchantPassword = Configuracion.getInstance().getMerchantPasswordTPV( mp.getIdentificadorOrganismo());
			String signature = PagoTPVUtil.generarFirmaNotificacionTPV(notif, merchantPassword);	
			if (!signature.equals(notif.getFirma())) {
	        	throw new Exception("No concuerda la firma de la notificacion");
	        }

			// Se ha pagado, generamos justificante
			String justificantePagoB64 = PagoTPVUtil
					.generarJustificanteTPVB64(notif);
			Date fechaPago = PagoTPVUtil.getFechaPago(notif);

			// Actualizamos BBDD dando pago como realizado			
			mp.setIdentificadorPago(notif.getOrden());
			mp.setFechaPago(fechaPago);
			mp.setReciboB64PagoTelematico(justificantePagoB64);
			mp.setEstado(ConstantesPago.SESIONPAGO_PAGO_CONFIRMADO);
			session.update(mp);

			// Realizamos log pago confirmado
			logPagoConfirmado(mp);
			
			return true;

		} catch (Exception ex) {
			throw new EJBException("Excepcion actualizando estado pago", ex);
		} finally {
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
	public  NotificacionPagosTPV recuperarNotificacion(String orden) {
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
	
	

	/**
	 * Recupera identificador pago a partir de la notificacion.
	 * @return 
	 * 
     * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
     */
	public  String getIdentificadorPago(String datosFirmados) {
		try{
			return PagoTPVUtil.getOrdenNotificacionTPV(datosFirmados);						
		}catch (Exception ex){
			log.error("Exception recuperando identificador pago a patir notificacion",ex);
			throw new EJBException("Exception recuperando identificador pago a patir notificacion",ex);
		}
	}
	
	
	
	private NotificacionPagosTPV recuperarNotificacionPorLocalizadorIdPago(String localizador, String identificadorPago) {
		Session session = getSession();
		try{
			Query query = session
				.createQuery("FROM NotificacionPagosTPV AS mp WHERE mp.localizador = '"
							+ localizador + "' and mp.orden = '" + identificadorPago + "'");
			if (query.list().isEmpty()) {
				return null;
			}
			NotificacionPagosTPV notif = (NotificacionPagosTPV) query.uniqueResult();
			return notif;			
		}catch (Exception ex){
			log.error("Exception recuperando notificacion pago",ex);
			throw new EJBException("Exception recuperando notificacion pago",ex);
		}finally{
            close(session);
        }
		
	}
	
	
	/**
	 * Genera un log pago confirmado	
	 */
	private void logPagoConfirmado(ModeloPagosTPV mp){
		try{
			// Creamos evento
			Evento evento = new Evento();
			evento.setTipo(ConstantesAuditoria.EVENTO_PAGO_TELEMATICO_CONFIRMADO);
			evento.setResultado("S");
			evento.setDescripcion("");
			evento.setModeloTramite(mp.getModeloTramite());
			evento.setVersionTramite(mp.getVersionTramite());
			evento.setIdioma(mp.getIdioma());
			evento.setClave(mp.getIdentificadorPago());
			evento.setIdPersistencia(mp.getIdentificadorTramite());
			evento.setNivelAutenticacion(mp.getNivelAutenticacion());
			if (!String.valueOf(ConstantesLogin.LOGIN_ANONIMO).equals(mp.getNivelAutenticacion())){
				evento.setUsuarioSeycon(mp.getUsuarioSeycon());
				evento.setNumeroDocumentoIdentificacion(mp.getNifUsuarioSeycon());
				evento.setNombre(mp.getNombreUsuarioSeycon());
			}						

			// Auditamos evento			
			DelegateAUDUtil.getAuditaDelegate().logEvento(evento, false);			
			
		}catch(Exception ex){
			log.error("Excepción registrando evento en auditoria: " + ex.getMessage(),ex);
		}
	}
	
}
