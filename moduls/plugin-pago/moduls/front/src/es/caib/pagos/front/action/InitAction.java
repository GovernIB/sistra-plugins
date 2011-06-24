package es.caib.pagos.front.action;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.pagos.front.Constants;
import es.caib.pagos.front.form.InitForm;
import es.caib.pagos.persistence.delegate.DelegateUtil;
import es.caib.pagos.persistence.delegate.SesionPagoDelegate;
import es.caib.sistra.plugins.pagos.ConstantesPago;
import es.caib.sistra.plugins.pagos.DatosPago;
import es.caib.sistra.plugins.pagos.EstadoSesionPago;

/**
 * @struts.action
 *  name="initForm"
 *  path="/init"
 *  scope="request"
 *  validate="false"
 *  
 * @struts.action-forward
 *  name="fail" path=".error"
 */
public class InitAction extends BaseAction
{

	Log logger = LogFactory.getLog( InitAction.class );
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception 
    {
		// Forzamos a reiniciar sesion
		Locale local = (java.util.Locale) request.getSession().getAttribute(org.apache.struts.Globals.LOCALE_KEY);
    	request.getSession().invalidate();
    	request.getSession();
		
		// Obtenemos token de acceso
		InitForm initForm = ( InitForm ) form;
			
		// Creamos sesion de pago
		SesionPagoDelegate dlg = DelegateUtil.getSesionPagoDelegate();
		try{
			dlg.create(initForm.getToken());
		}catch(Exception ex){
			request.setAttribute(Constants.MESSAGE_KEY,"sesionPagos.errorIniciar");
			setLocale(request,local);
			request.getSession().setAttribute(Constants.DATOS_SESION_IDIOMA_KEY,local.getLanguage());
			request.getSession().setAttribute(Constants.DATOS_SESION_NOMBRE_TRAMITE_KEY,"");
			setUrlRetornoSistra(request,"");
			setUrlMantenimientoSesionSistra(request,"");
			return mapping.findForward("fail");
		}
		
		// Almacenamos sesion de pago y la url de retorno a sistra
		setSesionPago(request,dlg);
		setUrlRetornoSistra(request,dlg.obtenerUrlRetornoSistra());
		setUrlMantenimientoSesionSistra(request,dlg.obtenerUrlMantenimientoSistra());
		
		// Obtenemos datos del pago para establecer locale
		DatosPago dp = dlg.obtenerDatosPago();
		if(dp != null){
			setLocale(request, new Locale( dp.getIdioma() ) );
			request.getSession().setAttribute(Constants.DATOS_SESION_IDIOMA_KEY,dp.getIdioma());
			request.getSession().setAttribute(Constants.DATOS_SESION_NOMBRE_TRAMITE_KEY,dp.getNombreTramite());
		}
		// Redirigimos a asistente pago segun estado y tipo del pago
		EstadoSesionPago ep = dlg.obtenerEstadoSesionPago();
		switch (ep.getEstado()){
			// Sesion en curso
			case ConstantesPago.SESIONPAGO_EN_CURSO:
				response.sendRedirect("datosPago.do");
				return null;
			case ConstantesPago.SESIONPAGO_PAGO_PENDIENTE_CONFIRMAR:
				if (ep.getTipo() == ConstantesPago.TIPOPAGO_TELEMATICO){
					response.sendRedirect("ReanudarPagoTelematico.do");					
				}else{
					response.sendRedirect("ReanudarPagoPresencial.do");			
				}
				return null;
			case ConstantesPago.SESIONPAGO_PAGO_CONFIRMADO:
				request.setAttribute(Constants.MESSAGE_KEY,"sesionPagos.errorPagoYaConfirmado");
				return mapping.findForward("fail");
			default:
				request.setAttribute(Constants.MESSAGE_KEY,"sesionPagos.errorEstadoNoValido");
				return mapping.findForward("fail");
		}		 
		
    }
		
}
