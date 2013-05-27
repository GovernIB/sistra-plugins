package es.caib.pagosTPV.front.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @struts.action
 *  path="/retornoInicioPago"
 *  scope="request"
 *  
 * @struts.action-forward
 *  name="success" path=".pago"
 */

public class RetornoInicioPagoAction extends BaseAction
{

	Log logger = LogFactory.getLog( RetornoInicioPagoAction.class );
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception 
    {		
		return mapping.findForward("success");		
    }
		
}
