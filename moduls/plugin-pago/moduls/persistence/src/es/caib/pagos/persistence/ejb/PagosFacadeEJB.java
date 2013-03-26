package es.caib.pagos.persistence.ejb;

import java.rmi.RemoteException;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.pagos.model.ModeloPagos;
import es.caib.pagos.model.SesionPagoCAIB;
import es.caib.pagos.model.TokenAccesoCAIB;
import es.caib.pagos.persistence.util.GeneradorId;
import es.caib.pagos.persistence.util.PasarelaPagos;
import es.caib.sistra.plugins.pagos.ConstantesPago;
import es.caib.sistra.plugins.pagos.DatosPago;
import es.caib.sistra.plugins.pagos.EstadoSesionPago;
import es.caib.sistra.plugins.pagos.SesionPago;
import es.caib.sistra.plugins.pagos.SesionSistra;
import es.caib.xml.ConstantesXML;

/**
 * SessionBean que implementa la interfaz del asistente
 * de pagos
 *
 * @ejb.bean
 *  name="pagos/persistence/PagosFacade"
 *  jndi-name="es.caib.pagos.persistence.PagosFacade"
 *  type="Stateless"
 *  view-type="remote" 
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 * 
 * 
 *
 */
public class PagosFacadeEJB extends HibernateEJB  {
 
	private static Log log = LogFactory.getLog(PagosFacadeEJB.class);
	
	/**
     * @ejb.create-method
     * @ejb.permission role-name = "${role.todos}"
     */
	public void ejbCreate() throws CreateException {	
		super.ejbCreate();
	}
	
	/**
	 * Inicia sesion de pago
	 * 
     * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
     */
	public SesionPago iniciarSesionPago(DatosPago datosPago, SesionSistra sesionSistra) {
		
		Session session = getSession();
		
		try{
			log.debug("Iniciar sesion pago");
			
			// Genera localizador
			String loca = GeneradorId.generarId();
					
			// Guarda datos
			SesionPagoCAIB sesionCAIB = new SesionPagoCAIB();
			sesionCAIB.setLocalizador(loca);
			sesionCAIB.setDatosPago(datosPago);
			sesionCAIB.setSesionSistra(sesionSistra);		
			EstadoSesionPago estado = new EstadoSesionPago();
			estado.setEstado(ConstantesPago.SESIONPAGO_EN_CURSO);
			sesionCAIB.setEstadoPago(estado);	
			
			// Generamos token para redireccion al asistente de pagos
			TokenAccesoCAIB tokenCAIB = new TokenAccesoCAIB();
			tokenCAIB.setLocalizador(loca);
			tokenCAIB.setTiempoLimite(new Date(System.currentTimeMillis() + 60000L) ); // Valido durante 60 segs
			String token = "TKNAC-" +  GeneradorId.generarId();
			tokenCAIB.setToken(token);
			
			// Almacenamos sesion
			ModeloPagos mp = new ModeloPagos(sesionCAIB,tokenCAIB);
			mp.setPagoFinalizado('N');
	        session.save(mp);
				        
			// Devolvemos sesion de pago creada
			SesionPago sesionPago = new SesionPago();
			sesionPago.setLocalizador(loca);
			// TODO Parametrizar context path
			sesionPago.setUrlSesionPago("/pagosCAIBFront/init.do?token="+token);
			log.debug("Iniciada sesion pago: localizador " + loca + " / token acceso: " + token);
			return sesionPago;
		}catch (Exception ex){
			log.error("Exception iniciando pago",ex);
			throw new EJBException("Exception iniciando pago",ex);
		}finally{
            close(session);
        }
	}

	/**
	 * Reanuda sesion de pago existente
	 * 
	 * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
	 */
	public SesionPago reanudarSesionPago(String localizador, SesionSistra sesionSistra) {
		Session session = getSession();
		try{
			log.debug("Reanudar sesion pago: localizador " + localizador);
			
			// Obtenemos sesion de pago
			Query query = session.createQuery("FROM ModeloPagos AS mp WHERE mp.localizador = '"+localizador+"'");
        	if (query.list().isEmpty()){
        		return null;        		
	        }
        	ModeloPagos mp = (ModeloPagos) query.uniqueResult();
        	
        	// Comprobamos que no haya sido confirmado
        	if (mp.getEstado() == ConstantesPago.SESIONPAGO_PAGO_CONFIRMADO){
				throw new Exception("La sesion de pago ya ha sido confirmada"); 
			}else if (mp.getEstado() == ConstantesPago.SESIONPAGO_NO_EXISTE_SESION){
				throw new Exception("La sesion no ha sido creada anteriormente");
			}else if("S".equals(mp.getPagoFinalizado()+"")){
				throw new Exception("La sesion de pago ya ha finalizado");
			}
						
			// Generamos token para redireccion al asistente de pagos
			TokenAccesoCAIB tokenCAIB = new TokenAccesoCAIB();
			tokenCAIB.setLocalizador(localizador);
			tokenCAIB.setTiempoLimite(new Date(System.currentTimeMillis() + 60000L) ); // Valido durante 60 segs
			String token = "TKNAC-" +  GeneradorId.generarId();
			tokenCAIB.setToken(token);
			
			// Actualizamos sesion de pago
			mp.setUrlRetornoSistra(sesionSistra.getUrlRetornoSistra());
			mp.setUrlMantenimientoSesionSistra(sesionSistra.getUrlMantenimientoSesionSistra());
			mp.setToken(tokenCAIB.getToken());
			mp.setTiempoLimite(tokenCAIB.getTiempoLimite());
			session.update(mp);
			
	        // Devolvemos sesion de pago creada
			SesionPago sesionPago = new SesionPago();
			sesionPago.setLocalizador(localizador);
			sesionPago.setLocalizador(token);
			
			// Devolvemos url reanudacion de pago
			sesionPago.setUrlSesionPago("/pagosCAIBFront/init.do?token="+token);
			log.debug("Reanudada sesion pago: localizador " + localizador + " / token acceso: " + token);
			return sesionPago;
			
		}catch (Exception ex){
			log.error("Exception reanudando pago",ex);
			throw new EJBException("Exception reanudando pago",ex);
		}finally{
            close(session);
        }
	}

	/**
	 * Comprueba estado sesion de pago
	 * 
	 * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
	 */
	public EstadoSesionPago comprobarEstadoSesionPago(String localizador) {
		Session session = getSession();
		try{
			log.debug("Comprobar estado sesion pago: localizador " + localizador);
			EstadoSesionPago estado = new EstadoSesionPago();
			
			// Obtenemos sesion de pago			
			Query query = session.createQuery("FROM ModeloPagos AS mp WHERE mp.localizador = '"+localizador+"'");
			
			// No existe sesion
        	if (query.list().isEmpty()){	        	
        		estado.setEstado(ConstantesPago.SESIONPAGO_NO_EXISTE_SESION);
				return estado;
	        }	        

        	// Devolvemos estado sesion
        	ModeloPagos mp = (ModeloPagos) query.uniqueResult();
			SesionPagoCAIB sesionCAIB = mp.getSessionPagoCAIB();
			if (sesionCAIB == null) {
				estado.setEstado(ConstantesPago.SESIONPAGO_NO_EXISTE_SESION);
				return estado;
			}
			log.debug("Estado=" + sesionCAIB.getEstadoPago().getEstado() );

			estado.setEstado(sesionCAIB.getEstadoPago().getEstado());
			
			if (sesionCAIB.getEstadoPago().getEstado() == ConstantesPago.SESIONPAGO_PAGO_PENDIENTE_CONFIRMAR) {
//				SI EL ESTADO ES "PENDIENTE DE CONFIRMAR" SE ESTABLECE MENSAJE DEBUG				
				estado.setTipo(sesionCAIB.getEstadoPago().getTipo());
				if(sesionCAIB.getEstadoPago().getIdentificadorPago() != null){
					estado.setDescripcionEstado("Pago iniciado en la pasarela de pago con localizador "+sesionCAIB.getEstadoPago().getIdentificadorPago());					  	
				}
			} else if (sesionCAIB.getEstadoPago().getEstado() == ConstantesPago.SESIONPAGO_PAGO_CONFIRMADO) {
				// SI EL ESTADO ES "CONFIRMADO" SE ESTABLECEN DATOS PAGO EFECTUADO
				estado.setTipo(sesionCAIB.getEstadoPago().getTipo());
				estado.setIdentificadorPago(sesionCAIB.getEstadoPago().getIdentificadorPago());
				estado.setFechaPago(sesionCAIB.getEstadoPago().getFechaPago());
				
				// PATCH: NO SE ESTABA CONVIRTIENDO A B64
				//estado.setReciboB64PagoTelematico(sesionCAIB.getEstadoPago().getReciboB64PagoTelematico());
				String reciboB64 = null;
				if (sesionCAIB.getEstadoPago().getReciboB64PagoTelematico() != null) {
					reciboB64 = new String(Base64.encodeBase64(sesionCAIB.getEstadoPago().getReciboB64PagoTelematico().getBytes(ConstantesXML.ENCODING)),ConstantesXML.ENCODING);
				}
				estado.setReciboB64PagoTelematico(reciboB64);
			}
			
			return estado;
			
		}catch (Exception ex){
			log.error("Exception comprobando estado pago",ex);
			throw new EJBException("Exception comprobando estado pago",ex);
		}finally{
            close(session);
        }
	}

	/**
	 * Finaliza sesion de pago
	 * 
	 * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
	 */
	public void finalizarSesionPago(String localizador){
		Session session = getSession();
		try{
			log.debug("Finalizar sesion pago");			
			Query query = session.createQuery("FROM ModeloPagos AS mp WHERE mp.localizador = '"+localizador+"'");
			
			if (query.list().isEmpty()){
				log.debug("No existe sesion");
				return;
			}
			        	
    		ModeloPagos mp = (ModeloPagos) query.uniqueResult();       
    		
    		// No se permite finalizar una sesion de pago pendiente de confirmacion
    		if (mp.getEstado() == ConstantesPago.SESIONPAGO_PAGO_PENDIENTE_CONFIRMAR){
    			throw new Exception("No se permite finalizar una sesion de pago pendiente de confirmacion");
    		}
    		// Borramos sesion de pago
    		mp.setPagoFinalizado('S');
    		session.saveOrUpdate(mp);
    		
	        log.debug("Eliminada datos sesion pago");
		}catch (Exception ex){
			log.error("Exception finalizando sesion pago",ex);
			throw new EJBException("Exception comprobando estado pago",ex);
		}finally{
            close(session);
        }
	}
	
	 /**
	 * Consulta el importe de una tasa
	 * 
	 * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
	 
	 * @param idTasa
	 * @return Importe en cents de la tasa. Devuelve -1 si error
	 */
	public long consultarImporteTasa(String idTasa) {
		try{
			log.debug("consultarImporteTasa");
			long imp = PasarelaPagos.consultarImporteTasa(idTasa);
			return imp;			
        }catch (Exception ex){
        	log.error("Error consultando importe tasa para idTasa "  + idTasa);
			throw new EJBException("Error consultando importe tasa para idTasa "  + idTasa, ex);
        }
	}


	public void ejbActivate() throws EJBException, RemoteException {
	}

	public void ejbPassivate() throws EJBException, RemoteException {
	}
}
