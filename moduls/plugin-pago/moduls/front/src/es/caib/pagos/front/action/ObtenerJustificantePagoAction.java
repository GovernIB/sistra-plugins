package es.caib.pagos.front.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.pagos.front.Constants;
import es.caib.pagos.persistence.delegate.DelegateException;
import es.caib.pagos.persistence.delegate.SesionPagoDelegate;

/**
 * @struts.action 
 * 	name="pagoForm"
 *  path="/obtenerJustificantePago"        
 *  scope="session"
 *  validate="false"
 *  input=".datosPago"
 *  
 * @struts.action-forward
 *  name="fail" path=".error"
 *
 *  
 */
public class ObtenerJustificantePagoAction extends BaseAction
{

	Log logger = LogFactory.getLog( ObtenerJustificantePagoAction.class );
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception 
    {
					
		byte[] datos = null;
		try{
			SesionPagoDelegate dlg = getSesionPago(request);
			datos = dlg.obtenerJustificantePago();
			
			String nombreFichero = "justificante.pdf";
			byte[] datosFichero = datos;

			response.reset();
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition","attachment; filename="+ nombreFichero + ";");
			response.getOutputStream().write(datosFichero);
			return null;
			
		} catch (DelegateException de){
			request.setAttribute(Constants.MESSAGE_KEY, de.getMessage());
			return mapping.findForward("fail");
		} 
    }
		
}
