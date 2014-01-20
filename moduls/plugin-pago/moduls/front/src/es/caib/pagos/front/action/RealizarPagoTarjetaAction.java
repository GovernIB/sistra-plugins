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
import es.caib.pagos.model.ResultadoIniciarPago;
import es.caib.pagos.persistence.delegate.DelegateException;
import es.caib.pagos.persistence.delegate.SesionPagoDelegate;

/**
 * @struts.action 
 * 	name="pagoTarjetaForm"
 *  path="/realizarPagoTarjeta"        
 *  scope="request"
 *  validate="true"
 *  input=".pagoTarjeta"
 *  
 * @struts.action-forward
 * 	name="fail" path=".error"
 *  
 * @struts.action-forward
 * 	name="success" path=".pagoFinalizado"
 * 
 * @struts.action-forward
 * 	name="pagoTiempoExcedido" path=".pagoTiempoExcedido"
 *  
 * 
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
			
			// Iniciamos sesion de pago dejando el pago como pendiente confirmar
			ResultadoIniciarPago res = dlg.realizarPagoTarjetaIniciarSesion();
			
			if (!res.isTiempoExcedido()) { 
				// Realizamos proceso de pago con tarjeta
				String token = res.getResultado();
				int resultado = dlg.realizarPagoTarjetaPagar(token, pagoTarjetaForm.getNumeroTarjeta(), caducidadTarjeta,
						pagoTarjetaForm.getTitularTarjeta(), pagoTarjetaForm.getCodigoVerificacionTarjeta());
				
				request.setAttribute("resultadoPago", resultado);
				return mapping.findForward("success");
			} else {
				request.setAttribute(Constants.MESSAGE_KEY, res.getMensajeTiempoExcedido());
				return mapping.findForward("pagoTiempoExcedido");
			}
			
			
			
		} catch (DelegateException de){ 
			String msg = de.getMessage();
			int idx = msg.indexOf("ClienteException:");
			if (idx == -1) {
				
				// RAFA: FIJAMOS ERROR			
				// request.setAttribute(Constants.MESSAGE_KEY, de.getMessage());
				request.setAttribute(Constants.MESSAGE_KEY, "sesionPagos.errorGenericoComprobarPago");
				
				request.setAttribute("respuesta", "");
			} else {
				int length = "ClienteException:".length();
				request.setAttribute("respuesta", msg.substring(idx + length));
			}
			
			return mapping.findForward("fail");
		}
    }
		
}
