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
import es.caib.sistra.plugins.pagos.ConstantesPago;

/**
 * @struts.action 
 * 	name="pagoForm"
 *  path="/confirmarPago"        
 *  scope="session"
 *  validate="true"
 *  input=".datosPago"
 *  
 * @struts.action-forward
 *  name="fail" path=".error"
 *  
 * @struts.action-forward
 *  name="presencial" path=".pagoPresencial"
 *  
 */
public class ConfirmarPagoAction extends BaseAction
{

	Log logger = LogFactory.getLog( ConfirmarPagoAction.class );
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception 
    {
		// Realizamos la confirmacion del pago
		try{
			PagoForm pagoForm = (PagoForm) form;
			SesionPagoDelegate dlg = getSesionPago(request);
			
			int resultado = dlg.confirmarPago();
			
			switch (resultado){
				case 1: // PAGADO
					String urlRetornoSistra = dlg.obtenerUrlRetornoSistra();
		 			response.sendRedirect(urlRetornoSistra);
					return null;
				case 0: // NO PAGADO
					request.setAttribute(Constants.MESSAGE_KEY,"sesionPagos.errorConfirmarPago.noPagado");
					return mapping.findForward("fail");
				case -1: // ERROR ESTADO DIFERENTE PENDIENTE DE PAGO 
					if (String.valueOf(ConstantesPago.TIPOPAGO_PRESENCIAL).equals(pagoForm.getModoPago())) {
						request.setAttribute("mostrarAlerta","sesionPagos.errorComprobarPago");
						return mapping.findForward("presencial");
					} else {
						request.setAttribute(Constants.MESSAGE_KEY,"sesionPagos.errorComprobarPago");
						return mapping.findForward("fail");
					}
				default: 
					request.setAttribute(Constants.MESSAGE_KEY,"sesionPagos.errorComprobarPago");
					return mapping.findForward("fail");	
			}
		}catch(Exception e){
			request.setAttribute(Constants.MESSAGE_KEY,"sesionPagos.errorGenericoComprobarPago");
			return mapping.findForward("fail");
		}
				
    }
		
}
