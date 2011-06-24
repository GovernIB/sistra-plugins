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
 * 	name="pagoForm"
 *  path="/realizarPagoTelematico"        
 *  scope="session"
 *  validate="true"
 *  input=".datosPago"
 *  
 * @struts.action-forward
 *  name="fail" path=".error"
 *  
 * @struts.action-forward
 * name="succes" path=".pagoTelematico"
 * 
 */
public class RealizarPagoTelematicoAction extends BaseAction
{

	Log logger = LogFactory.getLog( RealizarPagoTelematicoAction.class );
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception 
    {
		
		PagoForm pagoForm = (PagoForm) form;
				
		// Realizamos el pago
		String urlAcceso = null;
		try{
			SesionPagoDelegate dlg = getSesionPago(request);
			urlAcceso = dlg.realizarPago(pagoForm.getModoPago());
		}catch(Exception ex){
			if(ex.getMessage().contains("Error iniciando")){
				request.setAttribute(Constants.MESSAGE_KEY,"sesionPagos.errorComprobarPago");
			}else{
				request.setAttribute(Constants.MESSAGE_KEY,"sesionPagos.errorGenericoComprobarPago");
			}
			return mapping.findForward("fail");
		}
		
		if (urlAcceso == null){
			// TODO Tratamiento errores
			request.setAttribute(Constants.MESSAGE_KEY,"sesionPagos.errorGenericoComprobarPago");
			return mapping.findForward("fail");
 		}else{
 			request.setAttribute( Constants.PAYMENT_URL, urlAcceso );
			return mapping.findForward("succes");
 		}	
    }
		
}
