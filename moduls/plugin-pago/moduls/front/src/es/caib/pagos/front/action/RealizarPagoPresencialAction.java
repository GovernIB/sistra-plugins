package es.caib.pagos.front.action;

import java.io.IOException;

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
 *  path="/realizarPagoPresencial"        
 *  scope="session"
 *  validate="true"
 *  input=".datosPago"
 *  
 * @struts.action-forward
 *  name="fail" path=".error"
 *
 *  
 */
public class RealizarPagoPresencialAction extends BaseAction
{

	Log logger = LogFactory.getLog( RealizarPagoPresencialAction.class );
	
	public ActionForward executeTask(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
		
		try{
			SesionPagoDelegate dlg = getSesionPago(request);
			byte[] datos = dlg.realizarPagoPresencial();
			String nombreFichero = "cartapago.pdf";

			response.reset();
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition","attachment; filename="+ nombreFichero + ";");
			response.getOutputStream().write(datos);
			
		} catch (DelegateException de){
			request.setAttribute(Constants.MESSAGE_KEY, de.getMessage());
			return mapping.findForward("fail");
		} catch (IOException ioe) {
			logger.info("Client aborted");
		} catch (Exception exc) {
			logger.error(exc);
		} finally {
			try{
				if ( !response.isCommitted() ) { 
					response.flushBuffer();
				}
			} catch(Exception ex) {
				logger.error(ex);
			}
		}
		return null;
    }
	
}
