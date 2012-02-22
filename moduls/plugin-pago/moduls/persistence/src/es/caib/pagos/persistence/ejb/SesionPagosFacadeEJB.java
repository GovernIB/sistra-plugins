package es.caib.pagos.persistence.ejb;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

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
import es.caib.pagos.client.WebServiceError;
import es.caib.pagos.exceptions.ClienteException;
import es.caib.pagos.exceptions.ComprobarPagoException;
import es.caib.pagos.exceptions.GetPdf046Exception;
import es.caib.pagos.exceptions.GetUrlPagoException;
import es.caib.pagos.exceptions.InicioPagoException;
import es.caib.pagos.exceptions.PagarConTarjetaException;
import es.caib.pagos.model.ModeloPagos;
import es.caib.pagos.model.SesionPagoCAIB;
import es.caib.pagos.model.TokenAccesoCAIB;
import es.caib.pagos.persistence.util.Configuracion;
import es.caib.pagos.persistence.util.PasarelaPagos;
import es.caib.pagos.util.Constants;
import es.caib.pagos.util.UtilWs;
import es.caib.sistra.plugins.login.ConstantesLogin;
import es.caib.sistra.plugins.pagos.ConstantesPago;
import es.caib.sistra.plugins.pagos.DatosPago;
import es.caib.sistra.plugins.pagos.EstadoSesionPago;

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
	 * Comprueba contra la pasarela de pagos si el pago ha sido realizado
	 * 
     * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
     *
	 * @return -1 (Estado diferente a pendiente de confirmacion) / 0 (Pago no ha sido pagado) / 1 (Pago confirmado) 
	 * 
	 */
	public int confirmarPago() {
		
		boolean confirmado = false;
		String fechaPago = null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try{
			
			log.debug("Confirmar pago");
		
			// Comprobamos si esta en estado pendiente de confirmacion
			if (sesionPago.getEstadoPago().getEstado() != ConstantesPago.SESIONPAGO_PAGO_PENDIENTE_CONFIRMAR){
				log.error("El pago no está pendiente de confirmar");
				return -1;			   
			}
		   
			// Si es telematico comprobamos contra la pasarela de pagos si esta pagado  
			if (sesionPago.getEstadoPago().getTipo() == ConstantesPago.TIPOPAGO_TELEMATICO){
				 
				boolean simularPago = Boolean.parseBoolean(Configuracion.getInstance().getProperty("pago.simular"));
				
				if(simularPago){ 
					Date date = new Date();
					fechaPago = formatter.format(date);
					Hashtable datos = new Hashtable();
					datos.put(Constants.KEY_LOCALIZADOR, sesionPago.getEstadoPago().getIdentificadorPago());
					datos.put(Constants.KEY_FECHA_PAGO, fechaPago);
					String cadena = UtilWs.getCadenaDatos(datos);
					String justificante = UtilWs.getJustificante(cadena, Constants.FIRMA_SIMULADA);
					
					sesionPago.getEstadoPago().setFechaPago(date);
					sesionPago.getEstadoPago().setReciboB64PagoTelematico(justificante);
					confirmado = true;
				} else {
					// Comprobamos contra pasarela de pagos
					
					Hashtable resultado = PasarelaPagos.comprobarPago(sesionPago.getEstadoPago().getIdentificadorPago());
					
					if (resultado.isEmpty()) {
						confirmado = false;
					} else {
						confirmado = true;
						Date fecha = formatter.parse((String)resultado.get(Constants.KEY_FECHA_PAGO));
						sesionPago.getEstadoPago().setFechaPago(fecha);
						sesionPago.getEstadoPago().setIdentificadorPago((String)resultado.get(Constants.KEY_LOCALIZADOR));
						sesionPago.getEstadoPago().setReciboB64PagoTelematico((String)resultado.get(Constants.KEY_JUSTIFICANTE));
					}
					
				}
	    	
	    	} else if (sesionPago.getEstadoPago().getTipo() == ConstantesPago.TIPOPAGO_PRESENCIAL){
	    		// Actualizamos estado pago
	    		// -- El identificador de pago (localizador) ya se ha establecido al iniciar el pago
	    		// -- La fecha del pago la tomamos como la actual
	    		sesionPago.getEstadoPago().setFechaPago(new Date());
	    		confirmado = true;
	    	} else{ 
	    		throw new EJBException("Tipo de pago no soportado");
	    	}
		   
		   // Si es presencial o es telematico y ha sido confirmado actualizamos sesion pago
			if (confirmado) {
				sesionPago.getEstadoPago().setEstado(ConstantesPago.SESIONPAGO_PAGO_CONFIRMADO);
		    	actualizaModeloPagos();
		        return 1;
			}
	        return 0;
		   
		}catch(ComprobarPagoException e){
			//log.error("Exception confirmando pago",e);
			throw new EJBException("Error confirmando pago", e);
			 
		} catch (ParseException pe){
			throw new EJBException("Error al convertir la fecha: " + fechaPago, pe);
		}
		finally{
			// Auditamos pago telematico confirmado
			if (confirmado && sesionPago.getEstadoPago().getTipo() == ConstantesPago.TIPOPAGO_TELEMATICO){
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
		boolean pagoRealizado = false;
		try{
			
			log.debug("Cancelar pago telematico");
			
			// Comprobamos si esta en estado pendiente de confirmacion
			if (sesionPago.getEstadoPago().getEstado() != ConstantesPago.SESIONPAGO_PAGO_PENDIENTE_CONFIRMAR){
				throw new EJBException("El pago no tiene estado pendiente de confirmar");
			}
		   
			// Si es telematico comprobamos contra la pasarela de pagos si esta pagado  
		   	if (sesionPago.getEstadoPago().getTipo() == ConstantesPago.TIPOPAGO_TELEMATICO){
			   
		   		localizadorPagoTelematico = sesionPago.getEstadoPago().getIdentificadorPago();
			   
		   		// Comprobamos contra pasarela de pagos
				Hashtable resultado = PasarelaPagos.comprobarPago(sesionPago.getEstadoPago().getIdentificadorPago());
				
				if (resultado.isEmpty()) {
					sesionPago.getEstadoPago().setIdentificadorPago("");
		    		sesionPago.getEstadoPago().setEstado(ConstantesPago.SESIONPAGO_EN_CURSO);
		    		actualizaModeloPagos();
			        cancelado = true;
			        return 1;
				} else {
					return -1;
				}
	    		 
	    	} else { //TODO se puede dar este caso?
	    		return -2; // El pago es presencial no se puede pagar desde aquí.
	    	}   	
		}catch(ComprobarPagoException e){
			//log.error("Exception cancelar pago telematico",e);
			throw new EJBException("Exception cancelar pago telemático", e);
		}finally{
			// Auditamos cancelacion pago telematico
			if (cancelado){
				logAuditoria(ConstantesAuditoria.EVENTO_PAGO_TELEMATICO_ANULADO,"S","",localizadorPagoTelematico);
			}
		}
	}

	/**
	 * Actualiza la BBDD con los datos de la sesion del pago
	 */
	private void actualizaModeloPagos() {
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
	}

	/**
	 * Cancela el pago presencial
	 * 
     * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
     * 
	 * 
	 */
	public void cancelarPagoPresencial() {
		//TODO eliminar este metodo ?
		try{
			
			log.debug("Cancelar pago presencial");
			
			// Comprobamos si esta en estado pendiente de confirmacion
			if (sesionPago.getEstadoPago().getEstado() != ConstantesPago.SESIONPAGO_PAGO_PENDIENTE_CONFIRMAR){
				throw new EJBException("El pago no tiene estado pendiente de confirmar");
			}
		   
			// Si es telematico comprobamos contra la pasarela de pagos si esta pagado
			if (sesionPago.getEstadoPago().getTipo() != ConstantesPago.TIPOPAGO_PRESENCIAL){
				throw new EJBException("El pago no es presencial");
			}
			   
			// Inicializamos la sesion de pago
     	   	sesionPago.getEstadoPago().setIdentificadorPago("");
     	   	sesionPago.getEstadoPago().setEstado(ConstantesPago.SESIONPAGO_EN_CURSO);
		    		
     	   	actualizaModeloPagos();			        
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

	
	/**
	 * Inicia el pago contra la pasarela y devuelve el justificante para el pago presencial
	 * 
     * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
     */
	public byte[] realizarPagoPresencial() {
		
		log.debug("Realizar pago presencial");
		
		byte[] datosFichero = null;
		
		try{
			// Iniciar sesion de pago con la pasarela
			ResultadoInicioPago resPagos = PasarelaPagos.iniciarSesionPagos(sesionPago.getDatosPago(), 
					String.valueOf(ConstantesPago.TIPOPAGO_PRESENCIAL));

		    sesionPago.getEstadoPago().setIdentificadorPago(resPagos.getLocalizador());
		    sesionPago.getEstadoPago().setTipo(ConstantesPago.TIPOPAGO_PRESENCIAL);
		    // Obtenemos el fichero 
		    datosFichero = PasarelaPagos.getPdf046(resPagos.getLocalizador(), sesionPago.getDatosPago().getImporte(), 
		    		sesionPago.getDatosPago().getNifDeclarante(), new Date());
		} catch (InicioPagoException ex) {
			//log.error("Error al iniciar la sesion de pagos.");
			throw new EJBException("sesionPagos.errorComprobarPago");
		} catch (GetPdf046Exception ce) {
			//log.error("Error al obtener la carta de pago.");
			throw new EJBException("sesionPagos.errorGenericoComprobarPago");
		}
		sesionPago.getEstadoPago().setEstado(ConstantesPago.SESIONPAGO_PAGO_PENDIENTE_CONFIRMAR);
		actualizaModeloPagos();
    
        return datosFichero;
		
	}


	/**
	 * Inicia el pago contra la pasarela y realiza el pago con tarjeta bancaria
	 * @return -1 Error de comunicación / 0 No pagado / 1 pagado
     * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
     */
	public int realizarPagoTarjeta(String numeroTarjeta, String caducidadTarjeta, String titularTarjeta, String cvvTarjeta) {
		
		log.debug("Realizar pago con tarjeta");

		boolean resultado;
		String resultadoAudita = "N";
		String descripcionAudita =  "";
		int ret;
		try{
			// Iniciar sesion de pago con la pasarela
			ResultadoInicioPago resPagos = PasarelaPagos.iniciarSesionPagos(sesionPago.getDatosPago(), String.valueOf(ConstantesPago.TIPOPAGO_TELEMATICO));
		    String tokenAcceso = resPagos.getToken();
		    sesionPago.getEstadoPago().setIdentificadorPago(resPagos.getLocalizador());
			sesionPago.getEstadoPago().setTipo(ConstantesPago.TIPOPAGO_TELEMATICO);
			
			boolean simularPago = Boolean.parseBoolean(Configuracion.getInstance().getProperty("pago.simular"));
			if (simularPago) {
				resultado = true;
			} else {
				String[] refsModelos = new String[]{tokenAcceso};
				resultado = PasarelaPagos.pagarConTarjeta(refsModelos, numeroTarjeta, caducidadTarjeta, titularTarjeta, cvvTarjeta);
			}
			
			if (resultado) {
				sesionPago.getEstadoPago().setEstado(ConstantesPago.SESIONPAGO_PAGO_PENDIENTE_CONFIRMAR);
				//llamamos a confirmarPago para poder obtener el justificante de pago y guardarlo en BBDD
				ret = confirmarPago();
				resultadoAudita = "S";
				descripcionAudita = "Pago realizado correctamente";
			}
			else {
				ret = 0; // por seguridad
				descripcionAudita = "Pago no realizado";
			}
						
		}catch (InicioPagoException ex) {
			//log.error("Error al iniciar la sesion de pagos.");
			descripcionAudita = "Error iniciando la sesion de pagos";
			throw new EJBException("sesionPagos.errorComprobarPago");
		}catch (PagarConTarjetaException pe) {
			//log.error("Error en el pago con tarjeta.");
			//en caso que el error sea de los controlados lanzamos excepción
			if (pe.getCause() instanceof ClienteException) {
				ClienteException ce = (ClienteException)pe.getCause();
				if (WebServiceError.ERROR_TARJETA == ce.getCode()) {
					descripcionAudita = ce.getMessage();
					throw new EJBException("sesionPagos.errorGenericoComprobarPago");
				}
			} 
			//si no es un error de comunicación
			sesionPago.getEstadoPago().setEstado(ConstantesPago.SESIONPAGO_PAGO_PENDIENTE_CONFIRMAR);
			ret = -1;
			
		}finally {
			if (this.sesionPago.getEstadoPago().getTipo() == ConstantesPago.TIPOPAGO_TELEMATICO){
				logAuditoria(ConstantesAuditoria.EVENTO_PAGO_TELEMATICO_TARJETA, resultadoAudita, descripcionAudita,
						this.sesionPago.getEstadoPago().getIdentificadorPago());
			}
		}

		return ret;
		
	}
	
	/**
	 * Inicia el pago contra la pasarela y devuelve la url para poder realizar el pago mediante banca online
	 * @param codigoEntidad
	 * @return
	 * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
	 */
	public String realizarPagoBanca(String codigoEntidad) {
		log.debug("Realizar pago banca ");
		String resultado;
		boolean pagoIniciado = false;
		try{
			// Iniciar sesion de pago con la pasarela
			ResultadoInicioPago resPagos = PasarelaPagos.iniciarSesionPagos(sesionPago.getDatosPago(), String.valueOf(ConstantesPago.TIPOPAGO_TELEMATICO));
		    String tokenAcceso = resPagos.getToken();
		    sesionPago.getEstadoPago().setIdentificadorPago(resPagos.getLocalizador());
			sesionPago.getEstadoPago().setTipo(ConstantesPago.TIPOPAGO_TELEMATICO);
		
			String[] refsModelos = new String[]{tokenAcceso};
			resultado = PasarelaPagos.getUrlPago(refsModelos, codigoEntidad);
			
			pagoIniciado = true;
			
			sesionPago.getEstadoPago().setEstado(ConstantesPago.SESIONPAGO_PAGO_PENDIENTE_CONFIRMAR);
			actualizaModeloPagos();
		} catch (InicioPagoException ex) {
			//log.error("Error al iniciar la sesion de pagos.");
			throw new EJBException("sesionPagos.errorComprobarPago");
		} catch (GetUrlPagoException gupe) {
			throw new EJBException("sesionPagos.errorGenericoComprobarPago");
		}finally {
			if (pagoIniciado && this.sesionPago.getEstadoPago().getTipo() == ConstantesPago.TIPOPAGO_TELEMATICO){
				logAuditoria(ConstantesAuditoria.EVENTO_PAGO_TELEMATICO_INICIADO,"S","",this.sesionPago.getEstadoPago().getIdentificadorPago());
			}
		}
        
		return resultado;
	}
	
	/**
	 * Obtiene el justificante de un pago ya realizado
	 * 
     * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
     */
	public byte[] obtenerJustificantePago() {
		
		log.debug("Obtener justificante de pago");
		
		byte[] datosFichero = null;
		
		try{
			datosFichero = PasarelaPagos.getPdf046(sesionPago.getEstadoPago().getIdentificadorPago(), sesionPago.getDatosPago().getImporte(), 
		    		sesionPago.getDatosPago().getNifDeclarante(), sesionPago.getDatosPago().getFechaDevengo());
		} catch (GetPdf046Exception ce) {
			//log.error("Error al obtener el justificante de pago.");
			throw new EJBException("sesionPagos.errorGenericoComprobarPago");
		}
    
        return datosFichero;
		
	}



}
