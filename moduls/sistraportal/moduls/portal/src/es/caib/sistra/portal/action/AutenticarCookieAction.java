package es.caib.sistra.portal.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.sistra.persistence.delegate.DelegateUtil;

/**
 * @struts.action
 *  path="/protected/autenticarCookie"
 *  scope="request"
 *  validate="false"
 *  
 *  
 * 
 */
public class AutenticarCookieAction extends Action{
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception 
    {
		 response.sendRedirect(request.getContextPath() + "/enlacePortal.do?"+request.getQueryString());
         return null;                                           			
    }
 }
