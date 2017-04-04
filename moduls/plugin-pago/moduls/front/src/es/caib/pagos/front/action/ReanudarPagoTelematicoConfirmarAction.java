package es.caib.pagos.front.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.pagos.persistence.delegate.SesionPagoDelegate;
import es.caib.sistra.plugins.pagos.DatosPago;
import es.caib.sistra.plugins.pagos.EstadoSesionPago;

/**
 * @struts.action
 *  path="/ReanudarPagoTelematico"
 *  scope="request"
 *  validate="false"
 *  
 * @struts.action-forward
 *  name="fail" path=".error"
 *  
 * @struts.action-forward
 *  name="success" path=".pagoTelematico"
 */
public class ReanudarPagoTelematicoConfirmarAction extends BaseAction
{

	Log logger = LogFactory.getLog( ReanudarPagoTelematicoConfirmarAction.class );
	
	public ActionForward executeTask(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception 
    {
		//TODO --> aquí llamar a la comprobación del pago y redireccionar a la pantalla que toque pagoFinalizado o error
		SesionPagoDelegate dlg = getSesionPago(request);
		DatosPago dp = dlg.obtenerDatosPago();
		EstadoSesionPago es = dlg.obtenerEstadoSesionPago();
		return mapping.findForward("success");		
    }
		
}
