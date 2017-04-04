package es.caib.pagosTPV.front.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.pagosTPV.persistence.delegate.SesionPagoDelegate;
import es.caib.sistra.plugins.pagos.ConstantesPago;
import es.caib.sistra.plugins.pagos.EstadoSesionPago;

/**
 * @struts.action
 *  path="/datosPago"
 *  scope="request"
 *  validate="false"
 *  
 * @struts.action-forward
 *  name="success" path=".pago"
 *  
 */
public class DatosPagoAction extends BaseAction
{

	Log logger = LogFactory.getLog( DatosPagoAction.class );
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception 
    {
		
		SesionPagoDelegate dlg = getSesionPago(request);
		EstadoSesionPago estado = dlg.obtenerEstadoSesionPago();
		
		// Si no esta en curso, es que ya se ha iniciado por lo que pasamos a confirmar su estado
		if (ConstantesPago.SESIONPAGO_EN_CURSO != estado.getEstado()) {
			response.sendRedirect("confirmarPagoBanca.do");
			return null;
		}
		
		return mapping.findForward("success");		
    }
		
}
