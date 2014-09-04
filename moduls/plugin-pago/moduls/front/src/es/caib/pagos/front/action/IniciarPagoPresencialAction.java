package es.caib.pagos.front.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.pagos.persistence.delegate.SesionPagoDelegate;
import es.caib.sistra.plugins.pagos.ConstantesPago;
import es.caib.sistra.plugins.pagos.EstadoSesionPago;

/**
 * @struts.action 
 * 	name="pagoForm"
 *  path="/iniciarPagoPresencial"        
 *  scope="session"
 *  validate="false"
 *  input=".pago"
 *  
 *
 * @struts.action-forward
 * 	name="success" path=".pagoPresencial"
 *  
 */
public class IniciarPagoPresencialAction extends BaseAction
{
	
	public ActionForward executeTask(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception 
    {
		SesionPagoDelegate dlg = getSesionPago(request);
		EstadoSesionPago estado = dlg.obtenerEstadoSesionPago();
		estado.setTipo(ConstantesPago.TIPOPAGO_PRESENCIAL);
		
		return mapping.findForward("success");

    }
		
}
