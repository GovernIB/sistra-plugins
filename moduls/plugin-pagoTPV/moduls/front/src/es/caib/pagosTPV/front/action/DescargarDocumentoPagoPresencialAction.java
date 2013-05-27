package es.caib.pagosTPV.front.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.pagosTPV.front.Constants;
import es.caib.pagosTPV.model.DocumentoPagoPresencial;
import es.caib.pagosTPV.persistence.delegate.DelegateException;
import es.caib.pagosTPV.persistence.delegate.SesionPagoDelegate;

/**
 * @struts.action 
 *  path="/descargarDocumentoPagoPresencial"        
 *  scope="session"
 *  validate="false"
 * 
 */
public class DescargarDocumentoPagoPresencialAction extends BaseAction
{
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception 
    {
		try{
			final SesionPagoDelegate dlg = getSesionPago(request);
			DocumentoPagoPresencial docPago = dlg.descargarDocumentoPagoPresencial();
			
			response.reset();
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition","attachment; filename="+ docPago.getNombre() + ";");
			response.getOutputStream().write(docPago.getContenido());
			return null;
			
		}catch (DelegateException de){
			request.setAttribute(Constants.MESSAGE_KEY, "sesionPagos.errorPagar");
			return mapping.findForward("fail");
		} 		
    }
		
}
