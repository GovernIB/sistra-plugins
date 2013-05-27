package es.caib.pagosTPV.front.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.pagosTPV.front.Constants;
import es.caib.pagosTPV.persistence.delegate.SesionPagoDelegate;

/**
 * @struts.action
 *  path="/cancelarPagoPresencial"
 *  scope="request"
 *  validate="false"
 *  
 * @struts.action-forward
 *  name="fail" path=".error"
 *  
 * @struts.action-forward
 *  name="success" path=".pago"
 */

public class CancelarPagoPresencialAction extends BaseAction
{

	Log logger = LogFactory.getLog( CancelarPagoPresencialAction.class );
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception 
    {
		
		// Realizamos la confirmacion del pago
		try{
			SesionPagoDelegate dlg = getSesionPago(request);
			dlg.cancelarPagoPresencial();	
			return mapping.findForward("success");
		}catch(Exception e){
			//	ERROR con el pago no debido a la pasarela de pagos.
			request.setAttribute(Constants.MESSAGE_KEY,"sesionPagos.errorGenericoComprobarPago");
			return mapping.findForward("fail");
		}	
    }
		
}
