package es.caib.pagosTPV.persistence.ejb;

import java.rmi.RemoteException;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import es.caib.pagosTPV.model.ModeloPagosTPV;
import es.caib.pagosTPV.model.NotificacionPagosTPV;
import es.caib.pagosTPV.model.Page;
import es.caib.pagosTPV.model.RequestNotificacionTPV;
import es.caib.pagosTPV.model.RequestNotificacionTPVDecoded;
import es.caib.pagosTPV.model.SesionPagoCAIB;
import es.caib.pagosTPV.persistence.delegate.DelegateException;
import es.caib.pagosTPV.persistence.delegate.DelegateUtil;
import es.caib.pagosTPV.persistence.util.Configuracion;
import es.caib.pagosTPV.persistence.util.PagoTPVUtil;
import es.caib.sistra.plugins.pagos.ConstantesPago;
import es.caib.util.StringUtil;

/**
 * SessionBean que implementa acciones de back de pagos TPV
 *
 * @ejb.bean
 *  name="pagos/persistence/BackPagosTPVFacade"
 *  jndi-name="es.caib.pagosTPV.persistence.BackPagosTPVFacade"
 *  type="Stateless"
 *  view-type="remote" 
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 * 
 */
public class BackPagosTPVFacadeEJB extends HibernateEJB  {
 
	/**
     * @ejb.create-method
     * @ejb.permission role-name = "${role.admin}"
     */
	public void ejbCreate() throws CreateException {	
		super.ejbCreate();
	}
	
	public void ejbActivate() throws EJBException, RemoteException {
	}

	public void ejbPassivate() throws EJBException, RemoteException {
	}
	 
	
	/**
	 * Realiza búsqueda paginada.
	 * 
     * @ejb.interface-method
     * @ejb.permission role-name="${role.admin}" 
     */
	public Page busquedaPaginadaSesionesTPV(int pagina, int longitudPagina, String nif )
	{
		Session session = getSession();
        try 
        {
        	String where = " m.tipoPagoSeleccionado = 'T' AND " + 
			        				" ( m.estado = '" + ConstantesPago.SESIONPAGO_PAGO_CONFIRMADO + "' OR  m.estado = '" + ConstantesPago.SESIONPAGO_PAGO_PENDIENTE_CONFIRMAR + "' ) ";
        	if (nif != null) {
        		where += " AND m.nifDeclarante = :nif";
        	}
        	
			Query query = 
        		session.createQuery( "FROM ModeloPagosTPV m WHERE " + where + " ORDER BY m.fechaDevengo DESC" );
        	Query queryCount = 
        		session.createQuery( "SELECT COUNT(*) FROM ModeloPagosTPV m WHERE " + where );
        	
        	if (nif != null) {
        		query.setString("nif", nif);
        		queryCount.setString("nif", nif);
        	}
        	
            Page page = new Page( query, pagina, longitudPagina, queryCount );
            return page;
        }
        catch( HibernateException he )
        {
        	throw new EJBException( he );
        }
        catch( Exception exc )
        {
        	throw new EJBException( exc );
        }
        finally
        {
        	close( session );
        }
	}
	
	/**
	 * Confirma manualmente una sesion de TPV.
	 * 
     * @ejb.interface-method
     * @ejb.permission role-name="${role.admin}"
     * 
     * @return -1 Ya existe notificacion KO / 0 Ya existe notificacion OK / 1 Se ha creado notificacion OK
     */
	public int confirmarSesionTPV(String localizador) {
		
		// Recuperamos sesion pago 	
		SesionPagoCAIB sesionPago = null;
		Session session = getSession();
        try 
        {
			Query query = session.createQuery("FROM ModeloPagosTPV AS mp WHERE mp.localizador = '"+localizador+"'");
    		if (query.list().isEmpty()){
        		throw new Exception("No existe token de acceso");
            }
    		ModeloPagosTPV mp = (ModeloPagosTPV) query.uniqueResult();
	        if (mp == null) throw new Exception("No existe sesion pago");
	     
			sesionPago = mp.getSessionPagoCAIB();
			if (sesionPago == null){
				throw new Exception("No se encuentra la sesion de pago asociada al token de acceso");
			}
			
			if (sesionPago.getEstadoPago().getEstado() != ConstantesPago.SESIONPAGO_PAGO_PENDIENTE_CONFIRMAR){
				throw new Exception("El pago no está pendiente de confirmar");			  
			}
        } catch( Exception exc )
        {
        	throw new EJBException( exc );
        }
        finally
        {
        	close( session );
        }
		
		 // Verificamos si ya existe notificacion
		 try {
			NotificacionPagosTPV notif = DelegateUtil.getNotificacionPagosTPVDelegateDelegate().recuperarNotificacion(sesionPago.getEstadoPago().getIdentificadorPago());
			if (notif != null) {
				if (notif.getResultado().startsWith("00")) {
					return 0;
				} else {
					return -1;
				}
			}
		} catch (DelegateException e) {
			throw new EJBException( e );
		}
		
		
		// Generamos notificacion manualmente
        try {
        	RequestNotificacionTPVDecoded r = new RequestNotificacionTPVDecoded();
    		Date date = new Date();
    		r.setAmount(sesionPago.getDatosPago().getImporte());
    		r.setCardCountry("es");
    		r.setCardType("C");
    		r.setConsumerLanguage(Configuracion.getInstance().getIdiomaTPV(sesionPago.getDatosPago().getIdioma()));
    		r.setCurrency(Configuracion.getInstance().getMerchantCurrencyTPV());
    		r.setDate(StringUtil.fechaACadena(date,"dd/MM/yyyy"));
    		r.setHour(StringUtil.fechaACadena(date,"HH:mm"));
    		r.setMerchantCode(Configuracion.getInstance().getMerchantCodeTPV(sesionPago.getDatosPago().getIdentificadorOrganismo()));
    		r.setMerchantData(sesionPago.getLocalizador());
    		r.setOrder(sesionPago.getEstadoPago().getIdentificadorPago());
    		r.setResponse("0000");
    		r.setAuthorisationCode("MANUAL");
    		r.setSecurePayment("1");
    		r.setTerminal(Configuracion.getInstance().getMerchantTerminalTPV(sesionPago.getDatosPago().getIdentificadorOrganismo()));
    		r.setTransactionType(Configuracion.getInstance().getMerchantTransactionTypeAut());
    		
    		String merchantPassword = Configuracion.getInstance().getMerchantPasswordTPV(sesionPago.getDatosPago().getIdentificadorOrganismo());
    		RequestNotificacionTPV requestNotificacion = PagoTPVUtil.createRequestNotificacionTPV(r, merchantPassword);
    		
			DelegateUtil.getNotificacionPagosTPVDelegateDelegate().realizarNotificacion(requestNotificacion);
		} catch (Exception e) {
			throw new EJBException( e );
		}
		
		return 1;
        
	}

}
