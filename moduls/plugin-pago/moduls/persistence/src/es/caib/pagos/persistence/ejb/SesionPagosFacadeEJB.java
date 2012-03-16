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
					
		log.debug("Confirmar pago");
	
		// Comprobamos si esta en estado pendiente de confirmacion
		if (sesionPago.getEstadoPago().getEstado() != ConstantesPago.SESIONPAGO_PAGO_PENDIENTE_CONFIRMAR){
			log.error("El pago no está pendiente de confirmar");
			return -1;			   
		}
	   
		// Si es telematico comprobamos contra la pasarela de pagos si esta pagado  
		if (sesionPago.getEstadoPago().getTipo() == ConstantesPago.TIPOPAGO_TELEMATICO){
			 
			boolean simularPago = Boolean.parseBoolean(Configuracion.getInstance().getProperty("pago.simular"));
			String justificantePagoXML =  "";
			
			if(simularPago){ 
				justificantePagoXML = generarJustificanteSimulado();
			} else {
				// Comprobamos contra pasarela de pagos
				try {
					Hashtable resultado = PasarelaPagos.comprobarPago(sesionPago.getEstadoPago().getIdentificadorPago());
					if (((String)resultado.get(Constants.KEY_FIRMA)).length() > 0) {
						justificantePagoXML = generarJustificante((String)resultado.get(Constants.KEY_FIRMA));
					}
				} catch(ComprobarPagoException e){
					log.error("Exception confirmando pago",e);
					throw new EJBException("Error confirmando pago", e);	 
				} 

			}
			if (justificantePagoXML.length() > 0) {	
				es.caib.pagos.client.JustificantePago justificantePago = new es.caib.pagos.client.JustificantePago(justificantePagoXML); 
				try {
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					sesionPago.getEstadoPago().setFechaPago(formatter.parse(justificantePago.getFechaPago()));
				} catch (ParseException e) {
					throw new EJBException("Error convirtiendo la fecha de pago para el justificante", e);
				}
				sesionPago.getEstadoPago().setIdentificadorPago(justificantePago.getDui());
				sesionPago.getEstadoPago().setReciboB64PagoTelematico(justificantePagoXML);
				sesionPago.getEstadoPago().setEstado(ConstantesPago.SESIONPAGO_PAGO_CONFIRMADO);
				actualizaModeloPagos();
				logAuditoria(ConstantesAuditoria.EVENTO_PAGO_TELEMATICO_CONFIRMADO,"S","",this.sesionPago.getEstadoPago().getIdentificadorPago());
				return 1;
			}
    	
    	} else if (sesionPago.getEstadoPago().getTipo() == ConstantesPago.TIPOPAGO_PRESENCIAL){
       		sesionPago.getEstadoPago().setFechaPago(new Date());
       		sesionPago.getEstadoPago().setEstado(ConstantesPago.SESIONPAGO_PAGO_CONFIRMADO);
	    	actualizaModeloPagos();
    		return 1;
    	} else{ 
    		throw new EJBException("Tipo de pago no soportado");
    	}

        return 0;

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

		try{
			
			log.debug("Cancelar pago telematico");
			
			// Comprobamos si esta en estado pendiente de confirmacion
			if (sesionPago.getEstadoPago().getEstado() != ConstantesPago.SESIONPAGO_PAGO_PENDIENTE_CONFIRMAR){
				throw new EJBException("El pago no tiene estado pendiente de confirmar");
			}
		   
			// Si es telematico comprobamos contra la pasarela de pagos si esta pagado  
		   	if (sesionPago.getEstadoPago().getTipo() == ConstantesPago.TIPOPAGO_TELEMATICO){
			   
		   		// Comprobamos contra pasarela de pagos
				Hashtable resultado = PasarelaPagos.comprobarPago(sesionPago.getEstadoPago().getIdentificadorPago());
				
				if (resultado == null || resultado.containsKey(Constants.KEY_FIRMA)) {
					return -1;
				} else {
					sesionPago.getEstadoPago().setIdentificadorPago("");
		    		sesionPago.getEstadoPago().setEstado(ConstantesPago.SESIONPAGO_EN_CURSO);
		    		actualizaModeloPagos();
		    		// Auditamos cancelacion pago telematico
		    		logAuditoria(ConstantesAuditoria.EVENTO_PAGO_TELEMATICO_ANULADO,"S","",sesionPago.getEstadoPago().getIdentificadorPago());
			        return 1;
				}
	    		 
	    	} else { //TODO se puede dar este caso?
	    		return -2; // El pago es presencial no se puede pagar desde aquí.
	    	}   	
		}catch(ComprobarPagoException e){
			//log.error("Exception cancelar pago telematico",e);
			throw new EJBException("Exception cancelar pago telemático", e);
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
		
		try{
			// Iniciar sesion de pago con la pasarela
			ResultadoInicioPago resPagos = PasarelaPagos.iniciarSesionPagos(sesionPago.getDatosPago(), 
					String.valueOf(ConstantesPago.TIPOPAGO_PRESENCIAL));

		    sesionPago.getEstadoPago().setIdentificadorPago(resPagos.getLocalizador());
		    sesionPago.getEstadoPago().setTipo(ConstantesPago.TIPOPAGO_PRESENCIAL);
		    // Obtenemos el fichero 
		    byte[] datosFichero = PasarelaPagos.getPdf046(resPagos.getLocalizador(), sesionPago.getDatosPago().getImporte(), 
		    		sesionPago.getDatosPago().getNifDeclarante(), new Date());
		    sesionPago.getEstadoPago().setEstado(ConstantesPago.SESIONPAGO_PAGO_PENDIENTE_CONFIRMAR);
			actualizaModeloPagos();
	    
	        return datosFichero;
		} catch (InicioPagoException ex) {
			//log.error("Error al iniciar la sesion de pagos.");
			throw new EJBException("sesionPagos.errorComprobarPago", ex);
		} catch (GetPdf046Exception ce) {
			//log.error("Error al obtener la carta de pago.");
			throw new EJBException("sesionPagos.errorGenericoComprobarPago", ce);
		}
		
		
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

		    sesionPago.getEstadoPago().setIdentificadorPago(resPagos.getLocalizador());
			sesionPago.getEstadoPago().setTipo(ConstantesPago.TIPOPAGO_TELEMATICO);
			
			boolean simularPago = Boolean.parseBoolean(Configuracion.getInstance().getProperty("pago.simular"));
			if (simularPago) {
				resultado = true;
			} else {
				String[] refsModelos = new String[]{resPagos.getToken()};
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
			throw new EJBException("sesionPagos.errorComprobarPago", ex);
		}catch (PagarConTarjetaException pe) {
			//log.error("Error en el pago con tarjeta.");
			//en caso que el error sea de los controlados lanzamos excepción
			if (pe.getCause() instanceof ClienteException) {
				ClienteException ce = (ClienteException)pe.getCause();
				if (WebServiceError.ERROR_TARJETA == ce.getCode()) {
					descripcionAudita = ce.getMessage();
					throw new EJBException("sesionPagos.errorGenericoComprobarPago", pe);
				}
			} 
			//si no es un error de comunicación
			sesionPago.getEstadoPago().setEstado(ConstantesPago.SESIONPAGO_PAGO_PENDIENTE_CONFIRMAR);
			ret = -1;
			
		}finally {
			logAuditoria(ConstantesAuditoria.EVENTO_PAGO_TELEMATICO_TARJETA, resultadoAudita, descripcionAudita,
						this.sesionPago.getEstadoPago().getIdentificadorPago());
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
	public String realizarPagoBanca(String codigoEntidad, String urlVuelta) {
		log.debug("Realizar pago banca ");

		try{
			// Iniciar sesion de pago con la pasarela
			ResultadoInicioPago resPagos = PasarelaPagos.iniciarSesionPagos(sesionPago.getDatosPago(), String.valueOf(ConstantesPago.TIPOPAGO_TELEMATICO));
		    String tokenAcceso = resPagos.getToken();
		    sesionPago.getEstadoPago().setIdentificadorPago(resPagos.getLocalizador());
			sesionPago.getEstadoPago().setTipo(ConstantesPago.TIPOPAGO_TELEMATICO);
		
			String[] refsModelos = new String[]{tokenAcceso};
			String resultado = PasarelaPagos.getUrlPago(refsModelos, codigoEntidad, urlVuelta);

			sesionPago.getEstadoPago().setEstado(ConstantesPago.SESIONPAGO_PAGO_PENDIENTE_CONFIRMAR);
			actualizaModeloPagos();
			logAuditoria(ConstantesAuditoria.EVENTO_PAGO_TELEMATICO_INICIADO,"S","",this.sesionPago.getEstadoPago().getIdentificadorPago());
			return resultado;
			
		} catch (InicioPagoException ex) {
			//log.error("Error al iniciar la sesion de pagos.");
			throw new EJBException("sesionPagos.errorComprobarPago", ex);
		} catch (GetUrlPagoException gupe) {
			throw new EJBException("sesionPagos.errorGenericoComprobarPago", gupe);
		}

	}
	
	/**
	 * Obtiene el justificante de un pago ya realizado
	 * 
     * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
     */
	public byte[] obtenerJustificantePago() {
		
		log.debug("Obtener justificante de pago");

		try{
			return PasarelaPagos.getPdf046(sesionPago.getEstadoPago().getIdentificadorPago(), sesionPago.getDatosPago().getImporte(), 
		    		sesionPago.getDatosPago().getNifDeclarante(), sesionPago.getDatosPago().getFechaDevengo());
		} catch (GetPdf046Exception ce) {
			log.error("Error al obtener el justificante de pago.");
			throw new EJBException("sesionPagos.errorGenericoComprobarPago", ce);
		}
    
		
	}

	/**
	 * Genera justificante en formato XML
	 * @return
	 * @throws InicializacionFactoriaException 
	 * @throws UnsupportedEncodingException 
	 * @throws CargaObjetoXMLException 
	 * @throws EstablecerPropiedadException 
	 */
//	private JustificantePago generarJustificante(String justificante) 
//		throws InicializacionFactoriaException, CargaObjetoXMLException, UnsupportedEncodingException  {
//		
//		FactoriaObjetosXMLJustificantePago factoriaPago = new FactoriaObjetosXMLJustificantePagoImpl();
//		factoriaPago.setEncoding(ConstantesXML.ENCODING);
//		
//		JustificantePago justificanteXML = factoriaPago.crearJustificantePago(new ByteArrayInputStream(justificante.getBytes(ConstantesXML.ENCODING)));
//		return justificanteXML;
//	}
	
	/**
	 * Genera justificante de pago telematico simulado 
	 * @return
	 */
	private String generarJustificanteSimulado( ) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		StringBuilder str = new StringBuilder();
		str.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
		str.append("<JUSTIFICANTE_PAGO>");
		str.append("<DATOS_PAGO><LOCALIZADOR>");
		str.append(sesionPago.getEstadoPago().getIdentificadorPago());
		str.append("</LOCALIZADOR><DUI>");
		str.append(sesionPago.getEstadoPago().getIdentificadorPago());
		str.append("</DUI><FECHA_PAGO>");
		str.append(formatter.format(new Date()));
		str.append("</FECHA_PAGO></DATOS_PAGO>");
		str.append("<FIRMA>");
		str.append(Constants.FIRMA_SIMULADA);
		str.append("</FIRMA>");
		str.append("</JUSTIFICANTE_PAGO>");
		return str.toString();
	}
	
	/**
	 * Genera justificante de pago telemático a partir de los datos obtenidos en la respuesta del WS
	 * @param respuesta
	 * @return
	 */
	private String generarJustificante(String respuesta) {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
		sb.append("<JUSTIFICANTE_PAGO>");
		sb.append(respuesta);
		sb.append("</JUSTIFICANTE_PAGO>");
		return sb.toString();
	}

}
