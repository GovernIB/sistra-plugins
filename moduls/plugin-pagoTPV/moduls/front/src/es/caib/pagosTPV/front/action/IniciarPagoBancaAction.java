package es.caib.pagosTPV.front.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.pagosTPV.front.Constants;
import es.caib.pagosTPV.model.UrlPagoTPV;
import es.caib.pagosTPV.persistence.delegate.DelegateException;
import es.caib.pagosTPV.persistence.delegate.SesionPagoDelegate;
import es.caib.sistra.plugins.pagos.ConstantesPago;
import es.caib.sistra.plugins.pagos.EstadoSesionPago;

/**
 * @struts.action 
 *  path="/iniciarPagoBanca"        
 *  scope="session"
 *  validate="false"
 *  input=".pago"
 *  
 *
 * @struts.action-forward
 * 	name="success" path=".pagoBanca"
 * 
 *@struts.action-forward
 *  name="fail" path=".error"
 *  
 * @struts.action-forward
 * 	name="pagoTiempoExcedido" path=".pagoTiempoExcedido"
 * 
 */
public class IniciarPagoBancaAction extends BaseAction
{
	
	protected static Log log = LogFactory.getLog(IniciarPagoBancaAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception 
    {
		try{
			final SesionPagoDelegate dlg = getSesionPago(request);
			
			// Verificamos antes estado del pago, si esta pendiente redirigimos a confirmar
			EstadoSesionPago estadoSesion = dlg.obtenerEstadoSesionPago();
			if (estadoSesion.getEstado() == ConstantesPago.SESIONPAGO_PAGO_PENDIENTE_CONFIRMAR) {
				response.sendRedirect("confirmarPagoBanca.do");					
				return null;
			}
			
			// Iniciamos sesion de pago
			UrlPagoTPV urlPago = dlg.realizarPagoBanca();
			
			if (!urlPago.isTiempoExcedido()) {
				log.debug("Inicio pago TPV : \n" + urlPago.print());
				request.setAttribute("urlPago", urlPago);	
				return mapping.findForward("success");
			} else {
				request.setAttribute(Constants.MESSAGE_KEY, urlPago.getMensajeTiempoExcedido());
				return mapping.findForward("pagoTiempoExcedido");
			}
						
		}catch (DelegateException de){
			request.setAttribute(Constants.MESSAGE_KEY, "sesionPagos.errorPagar");
			return mapping.findForward("fail");
		} 		
    }
		
}
