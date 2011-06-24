package es.caib.pagos.persistence.ejb;

import java.io.ByteArrayInputStream;
import java.rmi.RemoteException;
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
import es.caib.loginModule.client.SeyconPrincipal;
import es.caib.pagos.client.ResultadoInicioPago;
import es.caib.pagos.model.ModeloPagos;
import es.caib.pagos.model.SesionPagoCAIB;
import es.caib.pagos.model.TokenAccesoCAIB;
import es.caib.pagos.persistence.util.Configuracion;
import es.caib.pagos.persistence.util.PasarelaPagos;
import es.caib.sistra.plugins.login.ConstantesLogin;
import es.caib.sistra.plugins.pagos.ConstantesPago;
import es.caib.sistra.plugins.pagos.DatosPago;
import es.caib.sistra.plugins.pagos.EstadoSesionPago;
import es.caib.xml.ConstantesXML;
import es.caib.xml.justificantepago.factoria.FactoriaObjetosXMLJustificantePago;
import es.caib.xml.justificantepago.factoria.ServicioJustificantePagoXML;
import es.caib.xml.justificantepago.factoria.impl.FactoriaObjetosXMLJustificantePagoImpl;
import es.caib.xml.justificantepago.factoria.impl.JustificantePago;

/**
 * SessionBean que implementa la logica de una sesion de pagos
 *
 * @ejb.bean
 *  name="pagos/persistence/SesionPagosFacade"
 *  jndi-name="es.caib.pagos.persistence.PagosFacade"
 *  type="Stateful"
 *  view-type="local" 
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 *
 */
public class SesionPagosFacadeEJB extends HibernateEJB {

	private static Log log = LogFactory.getLog(PagosFacadeEJB.class);
	
	private SesionPagoCAIB sesionPago;
	
	
	
	public void ejbActivate() throws EJBException, RemoteException {					
	}

	public void ejbPassivate() throws EJBException, RemoteException {	
	}
	
	/**
	 * Crea instancia de sesion de pagos
	 * 
     * @ejb.create-method
     * @ejb.permission role-name = "${role.todos}"
     */
	public void ejbCreate(String token) throws CreateException {
		
		super.ejbCreate();
		
		Session session = getSession();
		try{
			// Buscamos sesion asociada al token 	
			Query query = session.createQuery("FROM ModeloPagos AS mp WHERE mp.token = '"+token+"'");
    		if (query.list().isEmpty()){
        		throw new CreateException("No existe token de acceso");
            }
    		ModeloPagos mp = (ModeloPagos) query.uniqueResult();
	        if (mp == null) throw new CreateException("No existe token de acceso");
        	
			// Comprobamos tiempo limite de acceso
	        TokenAccesoCAIB tokenAcceso = mp.getTokenAccesoCAIB();
			if (tokenAcceso == null) throw new CreateException("No existe token de acceso");
			if (tokenAcceso.getTiempoLimite().before(new Date())) {			
				throw new CreateException("Se ha sobrepasado el tiempo limite para el token de acceso");
			}
			
			// Obtenemos sesion de pago para cachearla en la instancia
			sesionPago = mp.getSessionPagoCAIB();
			if (sesionPago == null){
				throw new CreateException("No se encuentra la sesion de pago asociada al token de acceso");
			}
			
		}catch(Exception ex){
			throw new EJBException("Excepcion creando instancia sesion pagos",ex);
		}finally{
			close(session);
	    }		
	}
	
	/**
	 * Inicia el pago contra la pasarela y devuelve la url de redireccion al portal de pagos
	 * 
     * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
     */
	public String realizarPago(String modoPago) {
		
		log.debug("Realizar pago");
		
		boolean pagoIniciado = false;
		
		if (modoPago == null) throw new EJBException("No se ha establecido el modo de pago");
		
		String urlAcceso;
		try{
			// Iniciar sesion de pago con la pasarela
			ResultadoInicioPago resPagos = PasarelaPagos.iniciarSesionPagos(sesionPago.getDatosPago(),modoPago);
		    String tokenAcceso = resPagos.getToken();
		    sesionPago.getEstadoPago().setIdentificadorPago(resPagos.getLocalizador());
		     
		    // Obtenemos url de redireccion al portal de pagos
			if (modoPago.equals(String.valueOf(ConstantesPago.TIPOPAGO_TELEMATICO))){
				sesionPago.getEstadoPago().setTipo(ConstantesPago.TIPOPAGO_TELEMATICO);
				urlAcceso = PasarelaPagos.getUrlInicioPago(this.sesionPago.getDatosPago().getIdioma(),tokenAcceso);
			}else{
				sesionPago.getEstadoPago().setTipo(ConstantesPago.TIPOPAGO_PRESENCIAL);
				urlAcceso =  PasarelaPagos.getUrlJustificantePago(this.sesionPago.getDatosPago().getIdioma(),tokenAcceso);
			}
		}catch(Exception e){
			log.error("Error iniciando la sesion de pagos contra el cliente de pagos.");
			throw new EJBException("Error iniciando la sesion de pagos contra el cliente de pagos.",e);
		}
		
		try{
			// Actualizamos estado sesion pago
			sesionPago.getEstadoPago().setEstado(ConstantesPago.SESIONPAGO_PAGO_PENDIENTE_CONFIRMAR);
			ModeloPagos mp = new ModeloPagos(sesionPago);
			mp.setPagoFinalizado('N');
			Session session = getSession();
			try{
				if(mp != null){
					session.saveOrUpdate(mp);
				}
			}catch (HibernateException he){
	        	throw new EJBException(he);
	        }finally{
	            close(session);
	        }
	        
	        // Devolvemos url de redireccion al portal de pagos
	        if (this.sesionPago.getEstadoPago().getTipo() == ConstantesPago.TIPOPAGO_TELEMATICO)
	        	pagoIniciado = true;
			return urlAcceso;
			
		}catch (Exception ex){
			log.error("Error realizando el pago",ex);
			throw new EJBException("Error realizando el pago",ex);
		}finally{
			// Auditamos pago telematico iniciado
			if (pagoIniciado && this.sesionPago.getEstadoPago().getTipo() == ConstantesPago.TIPOPAGO_TELEMATICO){
				logAuditoria(ConstantesAuditoria.EVENTO_PAGO_TELEMATICO_INICIADO,"S","",this.sesionPago.getEstadoPago().getIdentificadorPago());
			}
		}
	}

	/**
	 * Comprueba contra la pasarela de pagos si el pago ha sido realizado
	 * 
     * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
     *
	 * @return 1(Pago confirmado) / -1 (Pago no ha sido pagado) / -2 (Error de conexión con Plataforma de Pagos) 
	 * @throws Exception
	 */
	public int confirmarPago() {
		
		boolean pagoConfirmado = false;
		try{
			
		   log.debug("Confirmar pago");
			
		   // Comprobamos si esta en estado pendiente de confirmacion
		   if (sesionPago.getEstadoPago().getEstado() != ConstantesPago.SESIONPAGO_PAGO_PENDIENTE_CONFIRMAR){
			   	throw new Exception("El pago no tiene estado pendiente de confirmar");			   
		   }
		   
		   // Si es telematico comprobamos contra la pasarela de pagos si esta pagado  
		   if (sesionPago.getEstadoPago().getTipo() == ConstantesPago.TIPOPAGO_TELEMATICO){
			 
			   boolean simularPago = Boolean.parseBoolean(Configuracion.getInstance().getProperty("pago.simular"));
			   String justificantePago;
			   if(simularPago){
				   justificantePago = generarJustificanteSimulado();
			   }else{
		    		// Comprobamos contra pasarela de pagos
				    justificantePago = PasarelaPagos.comprobarPago(sesionPago.getEstadoPago().getIdentificadorPago());
 			    }
	    		if (justificantePago.equals("-1")) return -1; // Pago no efectuado
	    		if (justificantePago.equals("-2")) return -2; // Error conexión plataforma pagos
 
	    		// Parseamos justificante devuelto por la pasarela
	    		FactoriaObjetosXMLJustificantePago factoriaPago = new FactoriaObjetosXMLJustificantePagoImpl();
	    		factoriaPago.setEncoding(ConstantesXML.ENCODING);
	    		JustificantePago justificantePagoXML = factoriaPago.crearJustificantePago(new ByteArrayInputStream(justificantePago.getBytes(ConstantesXML.ENCODING)));
	    		
	    		// Actualizamos estado pago
	    		// ------------ En principio debe ser el mismo que da al iniciar la sesion pero por si acaso lo refrescamos
	    		sesionPago.getEstadoPago().setIdentificadorPago(justificantePagoXML.getDatosPago().getDui()); 
			    sesionPago.getEstadoPago().setFechaPago(justificantePagoXML.getDatosPago().getFechaPago());
	    		sesionPago.getEstadoPago().setReciboB64PagoTelematico(justificantePago);
	    	}else if (sesionPago.getEstadoPago().getTipo() == ConstantesPago.TIPOPAGO_PRESENCIAL){
	    		// Actualizamos estado pago
	    		// -- El identificador de pago (localizador) ya se ha establecido al iniciar el pago
	    		// -- La fecha del pago la tomamos como la actual
	    		sesionPago.getEstadoPago().setFechaPago(new Date());	    		
	    	}else{
	    		throw new Exception("Tipo de pago no soportado");
	    	}
		   
		   // Si es presencial o es telematico y ha sido confirmado actualizamos sesion pago		   
		 	sesionPago.getEstadoPago().setEstado(ConstantesPago.SESIONPAGO_PAGO_CONFIRMADO);
	    	ModeloPagos mp = new ModeloPagos(sesionPago);
	    	mp.setPagoFinalizado('N');
			Session session = getSession();
			try{
				if(mp != null){
					session.saveOrUpdate(mp);
				}
			}catch (HibernateException he){
	        	throw new EJBException(he);
	        }finally{
	            close(session);
	        }
	        
	        // Damos el pago por confirmado
	        pagoConfirmado = true;
	    	return 1;
		   
		}catch(Exception e){
			log.error("Exception confirmando pago",e);
			throw new EJBException(e);
		}finally{
			// Auditamos pago telematico confirmado
			if (pagoConfirmado && sesionPago.getEstadoPago().getTipo() == ConstantesPago.TIPOPAGO_TELEMATICO){
				logAuditoria(ConstantesAuditoria.EVENTO_PAGO_TELEMATICO_CONFIRMADO,"S","",this.sesionPago.getEstadoPago().getIdentificadorPago());
			}
		}
	}

	/**
	 * Comprueba contra la pasarela de pagos si el pago ha sido realizado, si nos devuelve que el pago no ha sido realizado se cancela.
	 * 
     * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
     *
	 * @return 1(Pago cancelado) / -1 (Pago ha sido pagado) / -2 (El pago es de tipo presencial) / -3 (Error de conexión con Plataforma de Pagos)
	 * @throws Exception
	 */
	public int cancelarPagoTelematico() {
		boolean cancelado = false;
		String localizadorPagoTelematico = null;
		
		try{
			
		   log.debug("Cancelar pago telematico");
			
		   // Comprobamos si esta en estado pendiente de confirmacion
		   if (sesionPago.getEstadoPago().getEstado() != ConstantesPago.SESIONPAGO_PAGO_PENDIENTE_CONFIRMAR){
			   	throw new Exception("El pago no tiene estado pendiente de confirmar");
		   }
		   
		   // Si es telematico comprobamos contra la pasarela de pagos si esta pagado  
		   if (sesionPago.getEstadoPago().getTipo() == ConstantesPago.TIPOPAGO_TELEMATICO){
			   
			   localizadorPagoTelematico = sesionPago.getEstadoPago().getIdentificadorPago();
			   
	    		// Comprobamos contra pasarela de pagos
				String justificantePago = PasarelaPagos.comprobarPago(sesionPago.getEstadoPago().getIdentificadorPago());
				
			    //Pago no efectuado
			    if (justificantePago.equals("-1")){  
			    	// Parseamos justificante devuelto por la pasarela			    	
		    		sesionPago.getEstadoPago().setIdentificadorPago("");
		    		sesionPago.getEstadoPago().setEstado(ConstantesPago.SESIONPAGO_EN_CURSO);
		    		
		    		ModeloPagos mp = new ModeloPagos(sesionPago);
			    	mp.setPagoFinalizado('N');
					Session session = getSession();
					try{
						if(mp != null){
							session.saveOrUpdate(mp);
						}
					}catch (HibernateException he){
			        	throw new EJBException(he);
			        }finally{
			            close(session);
			        }
			        
			        // Damos el pago por cancelado
			        cancelado = true;
			    	return 1;
			    }else if (justificantePago.equals("-2")){
			    	return -3; // Error conexión plataforma pagos
			    }else{
			    	return -1; // Ya se ha realizado el pago utilizando el Portal del Contriuent. No se puede Cancelar.
			    }
	    		 
	    	} else {
	    		return -2; // El pago es presencial no se puede pagar desde aquí.
	    	}   	
		}catch(Exception e){
			log.error("Exception cancelar pago telematico",e);
			throw new EJBException(e);
		}finally{
			// Auditamos cancelacion pago telematico
			if (cancelado){
				logAuditoria(ConstantesAuditoria.EVENTO_PAGO_TELEMATICO_ANULADO,"S","",localizadorPagoTelematico);
			}
		}
	}

	/**
	 * Cancela el pago presencial
	 * 
     * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
     * 
	 * @throws Exception
	 */
	public void cancelarPagoPresencial() {
		try{
			
		   log.debug("Cancelar pago presencial");
			
		   // Comprobamos si esta en estado pendiente de confirmacion
		   if (sesionPago.getEstadoPago().getEstado() != ConstantesPago.SESIONPAGO_PAGO_PENDIENTE_CONFIRMAR){
			   	throw new Exception("El pago no tiene estado pendiente de confirmar");
		   }
		   
		   // Si es telematico comprobamos contra la pasarela de pagos si esta pagado  
		   if (sesionPago.getEstadoPago().getTipo() != ConstantesPago.TIPOPAGO_PRESENCIAL){
				throw new Exception("El pago no es presencial");
		   }
			   
		   // Inicializamos la sesion de pago
     	   sesionPago.getEstadoPago().setIdentificadorPago("");
		   sesionPago.getEstadoPago().setEstado(ConstantesPago.SESIONPAGO_EN_CURSO);
		    		
		   ModeloPagos mp = new ModeloPagos(sesionPago);
		   mp.setPagoFinalizado('N');
		   Session session = getSession();
		   try{
				if(mp != null){
					session.saveOrUpdate(mp);
				}
			}catch (HibernateException he){
	        	throw new EJBException(he);
	        }finally{
	            close(session);
	        }			        
		}catch(Exception e){
			log.error("Exception cancelar pago presencial",e);
			throw new EJBException(e);
		}	       
	}

	
	
	/**
	 * Obtiene datos del pago
	 * 
     * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
     */
	public DatosPago obtenerDatosPago() {
		try{
			log.debug("Obtener datos pago");
			return sesionPago.getDatosPago();	
		}catch (Exception ex){
			log.error("Exception obteniendo datos pago",ex);
			throw new EJBException("Exception obteniendo datos pago",ex);
		}	
	}
	
	/**
	 * Obtiene estado actual de la sesion de pago
	 * 
     * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
     */
	public EstadoSesionPago obtenerEstadoSesionPago() {
		try{
			log.debug("Obtener estado pago");			
			return sesionPago.getEstadoPago();
		}catch (Exception ex){
			log.error("Exception obteniendo estado pago",ex);
			throw new EJBException("Exception obteniendo estado pago",ex);
		}	
	}

	/**
	 * Obtiene url de retorno a sistra
	 * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
	 */
	public String obtenerUrlRetornoSistra(){
		try{
			log.debug("Obtener url retorno sistra");
			return sesionPago.getSesionSistra().getUrlRetornoSistra();
		}catch (Exception ex){
			log.error("Exception obteniendo url retorno a sistra",ex);
			throw new EJBException("Exception obteniendo url retorno a sistra",ex);
		}
	}
	
	/**
	 * Obtiene url de mantenimiento de sistra
	 * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
	 */
	public String obtenerUrlMantenimientoSistra(){
		try{
			log.debug("Obtener url Mantenimiento sistra");
			return sesionPago.getSesionSistra().getUrlMantenimientoSesionSistra();
		}catch (Exception ex){
			log.error("Exception obteniendo url mantenimiento sistra",ex);
			throw new EJBException("Exception obteniendo url mantenimiento sistra",ex);
		}
	}
		
	
	/**
	 * Genera justificante de pago telematico simulado
	 * @param datosPago
	 * @return
	 */
	private String generarJustificanteSimulado() throws Exception {
		FactoriaObjetosXMLJustificantePago factoria = ServicioJustificantePagoXML.crearFactoriaObjetosXML();
		factoria.setIndentacion (true);
		JustificantePago just = factoria.crearJustificantePago ();
		
		es.caib.xml.justificantepago.factoria.impl.DatosPago datosPagoXML = factoria.crearDatosPago();
		datosPagoXML.setDui (sesionPago.getEstadoPago().getIdentificadorPago());
		datosPagoXML.setFechaPago (new Date());
		datosPagoXML.setEstado("PA");
		datosPagoXML.setLocalizador(sesionPago.getEstadoPago().getIdentificadorPago());
		just.setDatosPago (datosPagoXML);
		just.setFirma("FIRMA SIMULADA PARA PAGO SIMULADO");
		
		return factoria.guardarJustificantePago (just);
	}
	
	/**
	 * Genera un log en la auditoria
	 * @param tipoEvento
	 * @param resultado
	 * @param descripcion
	 * @param clave
	 */
	private void logAuditoria(String tipoEvento,String resultado,String descripcion,String clave){
		try{
			// Creamos evento
			Evento evento = new Evento();
			evento.setTipo(tipoEvento);
			evento.setResultado(resultado);
			evento.setDescripcion(descripcion);
			evento.setModeloTramite(this.sesionPago.getDatosPago().getModeloTramite());
			evento.setVersionTramite(this.sesionPago.getDatosPago().getVersionTramite());
			evento.setIdioma(this.sesionPago.getDatosPago().getIdioma());
			evento.setClave(clave);
			evento.setIdPersistencia(this.sesionPago.getDatosPago().getIdentificadorTramite());
			
			SeyconPrincipal p = (SeyconPrincipal) this.ctx.getCallerPrincipal();
			String nivelAuth=null;
			if (p.getCredentialType() == SeyconPrincipal.ANONYMOUS_CREDENTIAL){				
				nivelAuth= Character.toString(ConstantesLogin.LOGIN_ANONIMO);
			}else if (p.getCredentialType() == SeyconPrincipal.PASSWORD_CREDENTIAL){					
				nivelAuth= Character.toString(ConstantesLogin.LOGIN_USUARIO);
			}else if (p.getCredentialType() == SeyconPrincipal.SIGNATURE_CREDENTIAL){
				nivelAuth= Character.toString(ConstantesLogin.LOGIN_CERTIFICADO);								
			}
			evento.setNivelAutenticacion(nivelAuth);
			if (p.getCredentialType() != SeyconPrincipal.ANONYMOUS_CREDENTIAL){
				evento.setUsuarioSeycon(p.getName());
				evento.setNumeroDocumentoIdentificacion(p.getNif());
				evento.setNombre(p.getFullName());
			}						

			// Auditamos evento			
			DelegateAUDUtil.getAuditaDelegate().logEvento(evento);			
			
		}catch(Exception ex){
			log.error("Excepción registrando evento en auditoria: " + ex.getMessage(),ex);
		}
	}
}
