package es.caib.pagos.front.action;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @struts.action 
 * 	name="pagoTarjetaForm"
 *  path="/iniciarPagoTarjeta"        
 *  scope="session"
 *  input=".pagoTarjeta"
 *  validate="false"
 *  
 *
 * @struts.action-forward
 * 	name="success" path=".pagoTarjeta"
 *  
 */
public class IniciarPagoTarjetaAction extends BaseAction
{

	Log logger = LogFactory.getLog( IniciarPagoTarjetaAction.class );
	
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception 
    {
//		request.setAttribute("meses", getMeses());
//		request.setAttribute("anyos", getAnyos());
		return mapping.findForward("success");

    }
	
//	public List getMeses() {
//		String[] meses = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"}; 
//		return Arrays.asList(meses);
//	}
//	
//	public List getAnyos() {
//		Calendar cal = Calendar.getInstance();
//		int anyoActual = cal.get(Calendar.YEAR);
//		Integer[] anyos = {anyoActual, anyoActual + 1, anyoActual + 2, anyoActual + 3, anyoActual + 4, anyoActual + 5, anyoActual + 6, anyoActual + 7, anyoActual + 8, anyoActual + 9, anyoActual + 10};
//		return Arrays.asList(anyos);
//	}
		
}
