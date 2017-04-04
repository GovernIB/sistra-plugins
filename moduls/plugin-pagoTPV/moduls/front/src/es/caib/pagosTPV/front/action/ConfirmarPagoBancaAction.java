package es.caib.pagosTPV.front.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.pagosTPV.front.Constants;
import es.caib.pagosTPV.persistence.delegate.SesionPagoDelegate;
import es.caib.sistra.plugins.pagos.ConstantesPago;
import es.caib.sistra.plugins.pagos.EstadoSesionPago;

/**
 * @struts.action 
 *  path="/confirmarPagoBanca"        
 *  scope="session"
 *  validate="true"
 *  input=".datosPago"
 *  
 * @struts.action-forward
 *  name="fail" path=".error"
 *  
 * @struts.action-forward
 * 	name="pagoOK" path=".pagoBancaCorrecto"
 * 
 * @struts.action-forward
 * 	name="pagoKO" path=".pagoBancaErroneo"
 * 
 * @struts.action-forward
 * 	name="pagoSinNotificacion" path=".pagoBancaSinNotificacion"
 *  
 */
public class ConfirmarPagoBancaAction extends BaseAction
{

	Log logger = LogFactory.getLog( ConfirmarPagoBancaAction.class );
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception 
    {
		// Realizamos la confirmacion del pago
		try{
			SesionPagoDelegate dlg = getSesionPago(request);
			EstadoSesionPago estado = dlg.obtenerEstadoSesionPago();
			
			// Si ya esta pagado, lo damos como pagado
			if (ConstantesPago.SESIONPAGO_PAGO_CONFIRMADO == estado.getEstado()) {
				return mapping.findForward("pagoFinalizado");
			}
			
			// Si no esta pagado, intentamos confirmarlo
			int resultado = dlg.confirmarPagoBanca();
			switch (resultado){
				case 1: // PAGADO (NOTIFICACION OK)
					return mapping.findForward("pagoOK");
				case 0: // NO PAGADO (NOTIFICACION KO)
					// Cancelamos pago KO
					dlg.cancelarPagoBanca();
					// Mostramos mensaje para reintentar
					return mapping.findForward("pagoKO");
				case -1: // NO SE HA RECIBIDO NOTIFICACION
					return mapping.findForward("pagoSinNotificacion");																
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
