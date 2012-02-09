package es.caib.pagos.front.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.pagos.front.Constants;
import es.caib.pagos.front.form.PagoTarjetaForm;
import es.caib.pagos.persistence.delegate.DelegateException;
import es.caib.pagos.persistence.delegate.SesionPagoDelegate;

/**
 * @struts.action 
 * 	name="pagoTarjetaForm"
 *  path="/realizarPagoTarjeta"        
 *  scope="session"
 *  validate="true"
 *  input=".pagoTarjeta"
 *  
 * @struts.action-forward
 * 	name="fail" path=".error"
 *  
 * @struts.action-forward
 * 	name="success" path=".pagoFinalizado"
 *  
 */
public class RealizarPagoTarjetaAction extends BaseAction
{

	Log logger = LogFactory.getLog( RealizarPagoTarjetaAction.class );
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception 
    {
		
		PagoTarjetaForm pagoTarjetaForm = (PagoTarjetaForm) form;
		
		try{
			SesionPagoDelegate dlg = getSesionPago(request);
			String mesCaducidad = pagoTarjetaForm.getMesCaducidadTarjeta();
			String anyoCaducidad = pagoTarjetaForm.getAnyoCaducidadTarjeta().substring(2);
			String caducidadTarjeta = mesCaducidad + anyoCaducidad; 
			int resultado = dlg.realizarPagoTarjeta(pagoTarjetaForm.getNumeroTarjeta(), caducidadTarjeta,
					pagoTarjetaForm.getTitularTarjeta(), pagoTarjetaForm.getCodigoVerificacionTarjeta());
			request.setAttribute("resultadoPago", resultado);
			return mapping.findForward("success");
		} catch (DelegateException de){
				request.setAttribute(Constants.MESSAGE_KEY, de.getMessage());
				return mapping.findForward("fail");
		}
    }
		
}
