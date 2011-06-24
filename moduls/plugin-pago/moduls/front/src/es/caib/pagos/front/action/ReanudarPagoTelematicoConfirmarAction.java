package es.caib.pagos.front.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @struts.action
 *  path="/ReanudarPagoTelematico"
 *  scope="request"
 *  validate="false"
 *  
 * @struts.action-forward
 *  name="fail" path=".error"
 *  
 * @struts.action-forward
 *  name="success" path=".pagoTelematico"
 */
public class ReanudarPagoTelematicoConfirmarAction extends BaseAction
{

	Log logger = LogFactory.getLog( ReanudarPagoTelematicoConfirmarAction.class );
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception 
    {
		return mapping.findForward("success");		
    }
		
}
