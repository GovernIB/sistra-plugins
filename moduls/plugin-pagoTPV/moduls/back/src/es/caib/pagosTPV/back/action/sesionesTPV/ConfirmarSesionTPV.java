package es.caib.pagosTPV.back.action.sesionesTPV;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.pagosTPV.back.action.BaseAction;
import es.caib.pagosTPV.persistence.delegate.DelegateUtil;

/**
 * @struts.action
 *  path="/back/sesionesTPV/confirmarSesionTPV"
 *  
 *  @struts.action-forward
 *  	name="mensaje" path=".mensaje"
 */
public class ConfirmarSesionTPV extends BaseAction
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception 
    {
		String localizador = request.getParameter("localizador");
		int res = DelegateUtil.getBackPagosTPVDelegate().confirmarSesionTPV(localizador);
		
		String mensaje = "";
		switch (res) {
			case 1: mensaje = "mensaje.notificacionManualRealizada";
				break;
			case 0: mensaje = "mensaje.notificacionManualExistenteOK";
				break;
			case -1: mensaje = "mensaje.notificacionManualExistenteKO";
				break;
		}
		
		
		request.setAttribute("mensaje", mensaje);
		return mapping.findForward( "mensaje" );
		
	}
	
}
