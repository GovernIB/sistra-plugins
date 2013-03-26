package es.caib.pagosTPV.front.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.pagosTPV.front.Constants;
import es.caib.pagosTPV.persistence.delegate.DelegateException;
import es.caib.pagosTPV.persistence.delegate.SesionPagoDelegate;
import es.caib.sistra.plugins.pagos.ConstantesPago;
import es.caib.sistra.plugins.pagos.EstadoSesionPago;

/**
 * @struts.action 
 *  path="/iniciarPagoPresencial"        
 *  scope="session"
 *  validate="false"
 *  input=".pago"
 *  
 *
 * @struts.action-forward
 * 	name="success" path=".pagoPresencial"
 * 
 * @struts.action-forward
 *  name="fail" path=".error"
 * 
 */
public class IniciarPagoPresencialAction extends BaseAction
{
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception 
    {
		try{
			final SesionPagoDelegate dlg = getSesionPago(request);
			
			// Verificamos antes estado del pago, si ya esta confirmado y es presencial mostramos directamente pantalla (por si  F5)
			EstadoSesionPago estadoSesion = dlg.obtenerEstadoSesionPago();
			if (estadoSesion.getEstado() != ConstantesPago.SESIONPAGO_PAGO_CONFIRMADO) {
				// Realizamos pago presencial
				dlg.realizarPagoPresencial();
			}
			
			return mapping.findForward("success");			
		}catch (DelegateException de){
			request.setAttribute(Constants.MESSAGE_KEY, "sesionPagos.errorPagar");
			return mapping.findForward("fail");
		} 		
    }
		
}
