package es.caib.pagos.front.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.pagos.front.Constants;
import es.caib.pagos.front.form.PagoBancaForm;
import es.caib.pagos.persistence.delegate.DelegateException;
import es.caib.pagos.persistence.delegate.SesionPagoDelegate;

/**
 * @struts.action 
 * 	name="pagoBancaForm"
 *  path="/realizarPagoBanca"        
 *  scope="session"
 *  validate="true"
 *  input=".pagoBanca"
 *  
 * @struts.action-forward
 * 	name="fail" path=".error"
 *  
 * 
 * @struts.action-forward
 * 	name="success" path=".pagoTelematico"
 *  
 */
public class RealizarPagoBancaAction extends BaseAction
{

	Log logger = LogFactory.getLog( RealizarPagoBancaAction.class );
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception 
    {
		PagoBancaForm pagoBancaForm = (PagoBancaForm) form;
		
		try{
			SesionPagoDelegate dlg = getSesionPago(request);
			String urlPago = dlg.realizarPagoBanca(pagoBancaForm.getBanco());
			request.setAttribute( Constants.PAYMENT_URL, urlPago );

			return mapping.findForward("success");
		}catch (DelegateException de){
			request.setAttribute(Constants.MESSAGE_KEY, de.getMessage());
			return mapping.findForward("fail");
		} 
    }
		
}
