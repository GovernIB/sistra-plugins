package es.caib.pagos.front.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.pagos.front.Constants;
import es.caib.pagos.front.form.PagoBancaForm;
import es.caib.pagos.model.ResultadoIniciarPago;
import es.caib.pagos.persistence.delegate.DelegateException;
import es.caib.pagos.persistence.delegate.DelegateUtil;
import es.caib.pagos.persistence.delegate.SesionPagoDelegate;


/**
 * @struts.action 
 * 	name="pagoBancaForm"
 *  path="/realizarPagoBanca"        
 *  scope="session"
 *  validate="true"
 *  input=".pagoBanca"
 *  
 * @struts.action-forward
 * 	name="fail" path=".error"
 * 
 * @struts.action-forward
 * 	name="pagoTiempoExcedido" path=".pagoTiempoExcedido"
 *  
 * 
 *  
 */
public class RealizarPagoBancaAction extends BaseAction
{
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception 
    {
		final PagoBancaForm pagoBancaForm = (PagoBancaForm) form;
		
		try{
			final SesionPagoDelegate dlg = getSesionPago(request);
			final String urlSistra = DelegateUtil.getConfiguracionDelegate().obtenerPropiedad("sistra.url"); //TODO constante
			final String contextPath = request.getContextPath();
			final String servlet = "/confirmarPago.do";//TODO constante
			
			final StringBuilder urlVuelta = new StringBuilder(urlSistra.length() + contextPath.length() + servlet.length());
			urlVuelta.append(urlSistra);
			urlVuelta.append(contextPath);
			urlVuelta.append(servlet);
			
			
			ResultadoIniciarPago res = dlg.realizarPagoBanca(pagoBancaForm.getBanco(), urlVuelta.toString());
			
			if (!res.isTiempoExcedido()) { 
				final String urlPago = res.getResultado();
				response.sendRedirect(urlPago);
				return null;
			} else {
				request.setAttribute(Constants.MESSAGE_KEY, res.getMensajeTiempoExcedido());
				return mapping.findForward("pagoTiempoExcedido");
			}
			
		}catch (DelegateException de){
			request.setAttribute(Constants.MESSAGE_KEY, de.getMessage());
			return mapping.findForward("fail");
		} 
    }
		
}
