package es.caib.pagos.front.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.pagos.front.Constants;
import es.caib.pagos.front.form.PagoForm;
import es.caib.pagos.persistence.delegate.SesionPagoDelegate;

/**
 * @struts.action 
 * 	name="pagoBancaForm"
 *  path="/iniciarPagoBanca"        
 *  scope="session"
 *  validate="false"
 *  input=".pago"
 *  
 *
 * @struts.action-forward
 * 	name="success" path=".pagoBanca"
 *  
 */
public class IniciarPagoBancaAction extends BaseAction
{

	Log logger = LogFactory.getLog( IniciarPagoBancaAction.class );
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception 
    {
		return mapping.findForward("success");

    }
		
}
