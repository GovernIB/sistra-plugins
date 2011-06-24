package es.caib.pagos.front.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.pagos.front.Constants;
import es.caib.pagos.persistence.delegate.SesionPagoDelegate;

/**
 * @struts.action
 *  path="/cancelarPagoTelematico"
 *  scope="request"
 *  validate="false"
 *  
 * @struts.action-forward
 *  name="fail" path=".error"
 *  
 * @struts.action-forward
 *  name="success" path=".pago"
 */

public class CancelarPagoTelematicoAction extends BaseAction
{

	Log logger = LogFactory.getLog( CancelarPagoTelematicoAction.class );
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception 
    {
		
		// Realizamos la confirmacion del pago
		try{
			SesionPagoDelegate dlg = getSesionPago(request);
			//return 1() / -1 () / -2 (Error de conexión con Plataforma de Pagos) / -3 ()
			int resultado = dlg.cancelarPagoTelematico();
			
			switch (resultado){
				case 1: // Pago cancelado
					return mapping.findForward("success");
				case -1: // Pago ha sido pagado
					request.setAttribute(Constants.MESSAGE_KEY,"sesionPagos.errorCancelarPago.Pagado");
					return mapping.findForward("fail");
				case -2: //El pago es de tipo presencial
					request.setAttribute(Constants.MESSAGE_KEY,"sesionPagos.errorCancelarPago.Presencial");
					return mapping.findForward("fail");
				default: // ERROR CONEXION (no existe localizador, error conexion, ...)
					request.setAttribute(Constants.MESSAGE_KEY,"sesionPagos.errorComprobarPago");
					return mapping.findForward("fail");			
			}		
		}catch(Exception e){
//			ERROR con el pago no debido a la pasarela de pagos.
			request.setAttribute(Constants.MESSAGE_KEY,"sesionPagos.errorGenericoComprobarPago");
			return mapping.findForward("fail");
		}	
    }
		
}
