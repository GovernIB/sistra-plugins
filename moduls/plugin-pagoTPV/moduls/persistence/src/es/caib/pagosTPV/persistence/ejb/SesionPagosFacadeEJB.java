package es.caib.pagosTPV.persistence.ejb;

import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.impl.SessionFactoryImpl;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.audita.modelInterfaz.ConstantesAuditoria;
import es.caib.audita.modelInterfaz.Evento;
import es.caib.audita.persistence.delegate.DelegateAUDUtil;
import es.caib.loginModule.client.SeyconPrincipal;
import es.caib.pagosTPV.model.DocumentoPagoPresencial;
import es.caib.pagosTPV.model.ModeloPagosTPV;
import es.caib.pagosTPV.model.NotificacionPagosTPV;
import es.caib.pagosTPV.model.SesionPagoCAIB;
import es.caib.pagosTPV.model.TokenAccesoCAIB;
import es.caib.pagosTPV.model.UrlPagoTPV;
import es.caib.pagosTPV.persistence.delegate.DelegateUtil;
import es.caib.pagosTPV.persistence.util.Configuracion;
import es.caib.pagosTPV.persistence.util.PagoTPVUtil;
import es.caib.redose.modelInterfaz.DocumentoRDS;
import es.caib.redose.persistence.delegate.DelegateRDSUtil;
import es.caib.sistra.plugins.login.ConstantesLogin;
import es.caib.sistra.plugins.pagos.ConstantesPago;
import es.caib.sistra.plugins.pagos.DatosPago;
import es.caib.sistra.plugins.pagos.EstadoSesionPago;
import es.caib.util.StringUtil;
import es.caib.xml.ConstantesXML;
import es.caib.xml.pago.XmlDatosPago;

/**
 * SessionBean que implementa la logica de una sesion de pagos
 *
 * @ejb.bean
 *  name="pagos/persistence/SesionPagosTPVFacade"
 *  jndi-name="es.caib.pagosTPV.persistence.PagosFacade"
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
			Query query = session.createQuery("FROM ModeloPagosTPV AS mp WHERE mp.token = '"+token+"'");
    		if (query.list().isEmpty()){
        		throw new CreateException("No existe token de acceso");
            }
    		ModeloPagosTPV mp = (ModeloPagosTPV) query.uniqueResult();
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
	 * @return -1 (No se ha recibido notificacion de pago) / 0 (Notificacion de pago no confirmado) / 1 (Notificacion de pago confirmado)
	 * 
	 */
	public int confirmarPagoBanca() {
					
		log.debug("Confirmar pago banca");
	
		// Comprobamos si se ha marcado como pagado 
		try {
			// Solo se puede cancelar desde aqui un pago telematico  
		   	if (sesionPago.getEstadoPago().getTipo() != ConstantesPago.TIPOPAGO_TELEMATICO){
		   		throw new EJBException("El pago no es telematico");
		   	}	
		   	
			// Comprobamos si esta en estado pendiente de confirmacion
			// en ese caso no es necesario hacer la comprobación 
			if (sesionPago.getEstadoPago().getEstado() != ConstantesPago.SESIONPAGO_PAGO_PENDIENTE_CONFIRMAR){
				log.info("El pago no está pendiente de confirmar");
				throw new Exception("El pago no está pendiente de confirmar");		
			}
			
			
			NotificacionPagosTPV notif = recibidaNotificacionPago(sesionPago);
			if (notif == null) {
				// No se ha recibido notificacion
				return -1;	
			}
			// Verificamos si se ha pagado
			if (!notif.getResultado().startsWith("00")) {
				// No se ha pagado
				return 0;
			}	
			// Se ha pagado, generamos justificante
			String justificantePagoXML = generarJustificanteTPV(notif);
			String justificantePagoB64 = new String(Base64.encodeBase64(justificantePagoXML.getBytes(ConstantesXML.ENCODING)),ConstantesXML.ENCODING);
			// Actualizamos BBDD dando pago como realizado
			sesionPago.getEstadoPago().setIdentificadorPago(notif.getOrden());
			sesionPago.getEstadoPago().setFechaPago(StringUtil.cadenaAFecha(notif.getFecha() + " " + notif.getHora() + ":00", StringUtil.FORMATO_TIMESTAMP));
			sesionPago.getEstadoPago().setReciboB64PagoTelematico(justificantePagoB64);
			sesionPago.getEstadoPago().setEstado(ConstantesPago.SESIONPAGO_PAGO_CONFIRMADO);
			actualizaModeloPagos();
			
			logAuditoria(ConstantesAuditoria.EVENTO_PAGO_TELEMATICO_CONFIRMADO,"S","",this.sesionPago.getEstadoPago().getIdentificadorPago());
			return 1;
				
		} catch(Exception e){
			log.error("Exception confirmando pago",e);
			throw new EJBException(e);	 
		} 

	}

	/**
	 * Comprueba contra la pasarela de pagos si el pago ha sido realizado, si nos devuelve que el pago no ha sido realizado se cancela.
	 * 
     * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
     *
	 * @return 1(Pago cancelado) / -1 (Pago ha sido pagado)
	 * @throws Exception
	 */
	public int cancelarPagoBanca() {

		log.debug("Cancelar pago banca");
		
		// Comprobamos si esta en estado pendiente de confirmacion
		if (sesionPago.getEstadoPago().getEstado() != ConstantesPago.SESIONPAGO_PAGO_PENDIENTE_CONFIRMAR){
			throw new EJBException("El pago no tiene estado pendiente de confirmar");
		}
		
		// Solo se puede cancelar desde aqui un pago telematico  
	   	if (sesionPago.getEstadoPago().getTipo() != ConstantesPago.TIPOPAGO_TELEMATICO){
	   		throw new EJBException("El pago no es telematico");
	   	}	
		   
	   	// Intentamos confirmar para ver si esta pagado
	   	int conf = confirmarPagoBanca();
	   	if (conf == 1) {
	   		// Pago realizado, no se puede cancelar
	   		log.debug("Pago realizado, no se puede cancelar");
	   		return -1;	   		
	   	} else {
	   		// Pago erroneo o sin notificacino de pago, cancelamos pago
	   		log.debug("Pago no ha sido realizado, cancelamos pago");
	   		String listaPedidosKO = StringUtils.defaultString(sesionPago.getHistoricoPedidosKO());
    		if (listaPedidosKO.length() > 0) {
    			listaPedidosKO = listaPedidosKO + ",";
    		}
    		sesionPago.setHistoricoPedidosKO(listaPedidosKO + sesionPago.getEstadoPago().getIdentificadorPago());
	   		sesionPago.getEstadoPago().setIdentificadorPago("");
    		sesionPago.getEstadoPago().setEstado(ConstantesPago.SESIONPAGO_EN_CURSO);
    		actualizaModeloPagos();
    		// Auditamos cancelacion pago telematico
    		logAuditoria(ConstantesAuditoria.EVENTO_PAGO_TELEMATICO_ANULADO,"S","",sesionPago.getEstadoPago().getIdentificadorPago());
	        return 1;
	   	}		 
	}

	/**
	 * Actualiza la BBDD con los datos de la sesion del pago
	 */
	private void actualizaModeloPagos() {
		ModeloPagosTPV mp = new ModeloPagosTPV(sesionPago);
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
	 * Inicia el pago contra la pasarela y devuelve la url para poder realizar el pago mediante banca online
	 * @param codigoEntidad
	 * @return
	 * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
	 */
	public UrlPagoTPV realizarPagoBanca() {
		log.debug("Realizar pago banca ");
		try{
			// Verificamos que el pago no este iniciado
			if (sesionPago.getEstadoPago().getEstado() != ConstantesPago.SESIONPAGO_EN_CURSO) {
				throw new Exception("El pago no esta en estado para iniciar");
			}
			
			// Iniciar sesion de pago con la pasarela
			UrlPagoTPV pagoTPV = generarUrlPagoTPV();
			
			// Actualiza datos sesion de pago
			sesionPago.getEstadoPago().setIdentificadorPago(pagoTPV.getMerchantOrder());
			sesionPago.getEstadoPago().setTipo(ConstantesPago.TIPOPAGO_TELEMATICO);
			sesionPago.getEstadoPago().setEstado(ConstantesPago.SESIONPAGO_PAGO_PENDIENTE_CONFIRMAR);
			actualizaModeloPagos();			

			// Devolvemos URL de pago
			logAuditoria(ConstantesAuditoria.EVENTO_PAGO_TELEMATICO_INICIADO,"S","",this.sesionPago.getEstadoPago().getIdentificadorPago());
			return pagoTPV;			
		} catch (Exception ex) {
			log.error("Error al realizar pago banca: " + ex.getMessage(), ex);
			throw new EJBException(ex);
		}
	}

	/**
	 * Marca pago presencial como confirmado.
	 * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
	 */
	public void realizarPagoPresencial() {
		log.debug("Realizar pago presencial ");
		try{
			// Verificamos que el pago no este iniciado
			if (sesionPago.getEstadoPago().getEstado() != ConstantesPago.SESIONPAGO_EN_CURSO) {
				throw new Exception("El pago no esta en estado para iniciar");
			}
			
			// Actualiza datos sesion de pago
			sesionPago.getEstadoPago().setIdentificadorPago(generarNumeroPedido());
			sesionPago.getEstadoPago().setTipo(ConstantesPago.TIPOPAGO_PRESENCIAL);
			sesionPago.getEstadoPago().setEstado(ConstantesPago.SESIONPAGO_PAGO_CONFIRMADO);
			actualizaModeloPagos();			

		} catch (Exception ex) {
			log.error("Error al realizar pago presencial: " + ex.getMessage(), ex);
			throw new EJBException(ex);
		}
	}
	
	/**
	 * Descarga documento de pago presencial
	 * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
	 */
	public DocumentoPagoPresencial descargarDocumentoPagoPresencial() {
		log.debug("Descarga documento pago presencial ");
		try{
			// Verificamos que el pago sea presencial y este confirmado
			if (sesionPago.getEstadoPago().getEstado() != ConstantesPago.SESIONPAGO_PAGO_CONFIRMADO) {
				throw new Exception("El pago no esta en estado para iniciar");
			}
			if (sesionPago.getEstadoPago().getTipo() != ConstantesPago.TIPOPAGO_PRESENCIAL) {
				throw new Exception("El pago no es presencial");
			}
			
			// Devuelve documento de pago
			DocumentoPagoPresencial docPago = new DocumentoPagoPresencial();
			docPago.setNombre(sesionPago.getEstadoPago().getIdentificadorPago() + ".pdf");
			docPago.setContenido(generarDocumentoPagoPresencial());
			return docPago;

		} catch (Exception ex) {
			log.error("Error al descargar documento pago presencial: " + ex.getMessage(), ex);
			throw new EJBException(ex);
		}
	}
	
	/**
	 * Cancela pago presencial.
	 * 
     * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
     *
	 * @throws Exception
	 */
	public void cancelarPagoPresencial() {

		log.debug("Cancelar pago banca");
		
		// Comprobamos si esta en estado pendiente de confirmacion
		if (sesionPago.getEstadoPago().getEstado() != ConstantesPago.SESIONPAGO_PAGO_CONFIRMADO){
			throw new EJBException("El pago no tiene estado confirmado");
		}
		
		// Solo se puede cancelar desde aqui un pago telematico  
	   	if (sesionPago.getEstadoPago().getTipo() != ConstantesPago.TIPOPAGO_PRESENCIAL){
	   		throw new EJBException("El pago no es telematico");
	   	}	
		   
	   	sesionPago.getEstadoPago().setIdentificadorPago("");
    	sesionPago.getEstadoPago().setEstado(ConstantesPago.SESIONPAGO_EN_CURSO);
    	actualizaModeloPagos();
    			 
	}
	
	// -------------------------------------------------------------------------------
	// FUNCIONES AUXILIARES
	// -------------------------------------------------------------------------------
	
	/**
	 * Genera documento de pago presencial
	 * @param sesionPago2
	 * @return PDF documento de pago
	 * @throws Exception 
	 */
	private byte[] generarDocumentoPagoPresencial() throws Exception {
		
		String modelo = Configuracion.getInstance().getModeloRDSDocumentoPagoPresencial();
		int version = Configuracion.getInstance().getVersionRDSDocumentoPagoPresencial();
		String plantilla = Configuracion.getInstance().getPlantillaRDSDocumentoPagoPresencial();
		
		// Generamos xml de pago
		XmlDatosPago xmlPago = new XmlDatosPago();
		xmlPago.setModeloRDSPago(modelo);
		xmlPago.setVersionRDSPago(version);
		xmlPago.setNif(this.sesionPago.getDatosPago().getNifDeclarante());
		xmlPago.setNombre(this.sesionPago.getDatosPago().getNombreDeclarante());
		xmlPago.setTelefono(this.sesionPago.getDatosPago().getTelefonoDeclarante());
		xmlPago.setOrganoEmisor(this.sesionPago.getDatosPago().getIdentificadorOrganismo());
		xmlPago.setModelo(this.sesionPago.getDatosPago().getModelo());
		xmlPago.setConcepto(this.sesionPago.getDatosPago().getConcepto());
		xmlPago.setIdTasa(this.sesionPago.getDatosPago().getIdTasa());
		xmlPago.setFechaDevengo(this.sesionPago.getDatosPago().getFechaDevengo());
		xmlPago.setTipoPago(this.sesionPago.getEstadoPago().getTipo());
		xmlPago.setLocalizador(this.sesionPago.getLocalizador());
		xmlPago.setEstado('S');
		xmlPago.setNumeroDUI(this.sesionPago.getEstadoPago().getIdentificadorPago());
		xmlPago.setFechaPago(this.sesionPago.getEstadoPago().getFechaPago());
		xmlPago.setImporte(this.sesionPago.getDatosPago().getImporte());
		xmlPago.setInstruccionesPresencialTexto(Configuracion.getInstance().getDocumentoPagoPresencialInstrucciones(this.sesionPago.getDatosPago().getIdentificadorOrganismo(), this.sesionPago.getDatosPago().getIdioma()));
		xmlPago.setInstruccionesPresencialEntidad1Nombre(Configuracion.getInstance().getDocumentoPagoPresencialEntidad1Nombre(this.sesionPago.getDatosPago().getIdentificadorOrganismo()));
		xmlPago.setInstruccionesPresencialEntidad1Cuenta(Configuracion.getInstance().getDocumentoPagoPresencialEntidad1Cuenta(this.sesionPago.getDatosPago().getIdentificadorOrganismo()));
		xmlPago.setInstruccionesPresencialEntidad2Nombre(Configuracion.getInstance().getDocumentoPagoPresencialEntidad2Nombre(this.sesionPago.getDatosPago().getIdentificadorOrganismo()));
		xmlPago.setInstruccionesPresencialEntidad2Cuenta(Configuracion.getInstance().getDocumentoPagoPresencialEntidad2Cuenta(this.sesionPago.getDatosPago().getIdentificadorOrganismo()));
		xmlPago.setInstruccionesPresencialEntidad3Nombre(Configuracion.getInstance().getDocumentoPagoPresencialEntidad3Nombre(this.sesionPago.getDatosPago().getIdentificadorOrganismo()));
		xmlPago.setInstruccionesPresencialEntidad3Cuenta(Configuracion.getInstance().getDocumentoPagoPresencialEntidad3Cuenta(this.sesionPago.getDatosPago().getIdentificadorOrganismo()));
		byte[] xmlContent = xmlPago.getBytes();
				
		//String strXml = xmlPago.getString();
		
		// Formateamos a PDF con REDOSE
		DocumentoRDS documentoRDS = new DocumentoRDS();
		documentoRDS.setModelo(modelo);
		documentoRDS.setVersion(version);
		documentoRDS.setTitulo("Pago presencial " + this.sesionPago.getEstadoPago().getIdentificadorPago());
		documentoRDS.setNombreFichero(this.sesionPago.getEstadoPago().getIdentificadorPago() + ".xml");
		documentoRDS.setDatosFichero(xmlContent);
		DocumentoRDS docFormateado = DelegateRDSUtil.getRdsDelegate().formatearDocumento(documentoRDS, modelo, version, plantilla, this.sesionPago.getDatosPago().getIdioma());
		
		return docFormateado.getDatosFichero();
	}


	
	/**
	 * Genera justificante de pago TPV 
	 * @return
	 */
	private String generarJustificanteTPV(NotificacionPagosTPV notificacionPago) {
		StringBuilder str = new StringBuilder();
		str.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
		str.append("<JUSTIFICANTE_PAGO>");
		str.append("<DATOS_PAGO><LOCALIZADOR>");
		str.append(notificacionPago.getLocalizador());
		str.append("</LOCALIZADOR><DUI>");
		str.append(notificacionPago.getOrden());
		str.append("</DUI><FECHA_PAGO>");
		String strDate = notificacionPago.getFecha() + " " + notificacionPago.getHora() + ":00";
		Date fcDate = StringUtil.cadenaAFecha(strDate, StringUtil.FORMATO_TIMESTAMP);
		str.append(StringUtil.fechaACadena(fcDate, StringUtil.FORMATO_REGISTRO));
		str.append("</FECHA_PAGO></DATOS_PAGO>");
		str.append("<FIRMA>");
		str.append(notificacionPago.getFirma());
		str.append("</FIRMA>");
		str.append("</JUSTIFICANTE_PAGO>");
		return str.toString();
	}
	
	
	/**
	 * Genera URL para iniciar pago.
	 * @return URL para iniciar pago.
	 * @throws Exception
	 */
	private UrlPagoTPV generarUrlPagoTPV() throws Exception {
		UrlPagoTPV pagoTPV = new UrlPagoTPV();
		
		pagoTPV.setMerchantAmount(sesionPago.getDatosPago().getImporte());
		pagoTPV.setMerchantCode(Configuracion.getInstance().getMerchantCodeTPV(sesionPago.getDatosPago().getIdentificadorOrganismo()));
		pagoTPV.setMerchantConsumerLanguage(Configuracion.getInstance().getIdiomaTPV(sesionPago.getDatosPago().getIdioma()));
		pagoTPV.setMerchantCurrency(Configuracion.getInstance().getMerchantCurrencyTPV());
		pagoTPV.setMerchantData(sesionPago.getLocalizador());
		pagoTPV.setMerchantMerchantURL(Configuracion.getInstance().getUrlTPVNotificacion());
		pagoTPV.setMerchantName(Configuracion.getInstance().getMerchantNameTPV(sesionPago.getDatosPago().getIdentificadorOrganismo()));
		pagoTPV.setMerchantOrder(generarNumeroPedido());
		pagoTPV.setMerchantProductDescription(StringUtils.substring(sesionPago.getDatosPago().getConcepto(),0,100));
		pagoTPV.setMerchantTerminal(Configuracion.getInstance().getMerchantTerminalTPV(sesionPago.getDatosPago().getIdentificadorOrganismo()));
		pagoTPV.setMerchantTitular(StringUtils.substring(sesionPago.getDatosPago().getNombreDeclarante(), 0, 60));
		pagoTPV.setMerchantTransactionTypeAut(Configuracion.getInstance().getMerchantTransactionTypeAut());
		pagoTPV.setMerchantUrlKO(Configuracion.getInstance().getUrlTPVRetornoKO());
		pagoTPV.setMerchantUrlOK(Configuracion.getInstance().getUrlTPVRetornoOK());
		pagoTPV.setUrlPagoTPV(Configuracion.getInstance().getUrlTPV());		
		
		pagoTPV.setMerchantSignature(generarMerchantSignature(pagoTPV, Configuracion.getInstance().getMerchantPasswordTPV(sesionPago.getDatosPago().getIdentificadorOrganismo())));
		
		if (StringUtils.isEmpty(pagoTPV.getMerchantAmount()) || 
				StringUtils.isEmpty(pagoTPV.getMerchantCode())||
				StringUtils.isEmpty(pagoTPV.getMerchantData())|| 
				StringUtils.isEmpty(pagoTPV.getMerchantConsumerLanguage())|| 
				StringUtils.isEmpty(pagoTPV.getMerchantCurrency())|| 
				StringUtils.isEmpty(pagoTPV.getMerchantMerchantURL())|| 
				StringUtils.isEmpty(pagoTPV.getMerchantName())|| 
				StringUtils.isEmpty(pagoTPV.getMerchantOrder())|| 
				StringUtils.isEmpty(pagoTPV.getMerchantProductDescription())|| 
				StringUtils.isEmpty(pagoTPV.getMerchantSignature())|| 
				StringUtils.isEmpty(pagoTPV.getMerchantTerminal())|| 
				StringUtils.isEmpty(pagoTPV.getMerchantTitular()) ||
				StringUtils.isEmpty(pagoTPV.getMerchantTransactionTypeAut()) ||
				StringUtils.isEmpty(pagoTPV.getMerchantUrlKO()) ||
				StringUtils.isEmpty(pagoTPV.getMerchantUrlOK()) ||
				StringUtils.isEmpty(pagoTPV.getUrlPagoTPV())) {
			
			
			throw new Exception("Alguno de los datos de inicio de pago esta vacio: \n" + pagoTPV.print());
			
		}
		
		return pagoTPV;
	}
	
	
	/**
	 * Genera firma pedido.
	 * @param pagoTPV Datos pedido
	 * @param merchantPassword Password comercio
	 * @return Firma pedido
	 * @throws Exception
	 */
	private String generarMerchantSignature(UrlPagoTPV pagoTPV, String merchantPassword) throws Exception {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            sha.update(pagoTPV.getMerchantAmount().getBytes());
            sha.update(pagoTPV.getMerchantOrder().getBytes());
            sha.update(pagoTPV.getMerchantCode().getBytes());
            sha.update(pagoTPV.getMerchantCurrency().getBytes());
            sha.update(pagoTPV.getMerchantTransactionTypeAut().getBytes());
            sha.update(pagoTPV.getMerchantMerchantURL().getBytes());
            byte[] hash = sha.digest(merchantPassword.getBytes());

            String Merchant_Signature = new String();

            int h = 0;
            String s = new String();
            int SHA1_DIGEST_LENGTH = 20;
            for (int i = 0; i < SHA1_DIGEST_LENGTH; i++) {
                h = (int) hash[i];          // Convertir de byte a int
                if (h < 0) {
                    h += 256;  // Si son valores negativos, pueden haber problemas de conversi¢n.
                }
                s = Integer.toHexString(h); // Devuelve el valor hexadecimal como un String
                if (s.length() < 2) {
                    Merchant_Signature = Merchant_Signature.concat("0"); // A?ade un 0 si es necesario
                }
                Merchant_Signature = Merchant_Signature.concat(s); // A?ade la conversi¢n a la cadena ya existente
            }

            Merchant_Signature = Merchant_Signature.toUpperCase(); // Convierte la cadena generada a Mayusculas.

            return Merchant_Signature;
        } catch (NoSuchAlgorithmException nsae) {
            throw new Exception("No se ha podido generar firma pedido", nsae);
        }
    }


	/**
	 * Genera numero de pedido.
	 * @return Numero de pedido
	 * @throws Exception 
	 */
	private String generarNumeroPedido() throws Exception {
		Session session = getSession();
		PreparedStatement pst=null;
		
		String prefijoOrden = Configuracion.getInstance().getPrefijoOrden(); 
		
		try{
			String sql = ((SessionFactoryImpl)session.getSessionFactory()).getDialect().getSequenceNextValString("ZPE_SEQTPV");
	        pst = session.connection().prepareStatement(sql);
	    	pst.execute(); 
	    	ResultSet rs = pst.getResultSet();
	    	rs.next();
	    	int num = rs.getInt(1);	
	    	return prefijoOrden + StringUtils.leftPad("" + num, (12 - prefijoOrden.length()), '0');
		}catch(Exception ex){
			throw new Exception("Excepcion generando numero de pedido",ex);
		}finally{
			try{if(pst != null) pst.close();}catch(Exception ex){}
			close(session);
	    }				
	}
	
	/**
	 * Verifica si se ha recibido la notificacion de pago.
	 * @return NotificacionPagosTPV (nulo si no se ha notificado)
	 * @throws Exception 
	 */
	private NotificacionPagosTPV recibidaNotificacionPago(SesionPagoCAIB sesionPago) throws Exception {
		// Recuperamos notificacion
		NotificacionPagosTPV notificacion = DelegateUtil.getNotificacionPagosTPVDelegateDelegate().recuperarNotificacion(sesionPago.getEstadoPago().getIdentificadorPago());
		if (notificacion == null){
			return null;
		}
		
		// Verificamos que la notificacion pertenezca a la misma sesion de pago
		if (!notificacion.getLocalizador().equals(sesionPago.getLocalizador())) {
			throw new Exception("No concuerda localizador de la sesion de pagos de la notificacion");
		}
		
		// Verificamos firma
		String signature = PagoTPVUtil.generarFirmaNotificacionTPV(sesionPago, notificacion.getResultado());
        if (!signature.equals(notificacion.getFirma())) {
        	throw new Exception("No concuerda la firma de la notificacion");
        }
			
        return notificacion;
		
	}

	

}
