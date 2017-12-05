package es.caib.pagosTPV.front.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.pagosTPV.persistence.delegate.DelegateUtil;
import es.caib.pagosTPV.persistence.delegate.SesionPagoDelegate;
import es.caib.sistra.plugins.pagos.DatosPago;
import es.caib.sistra.plugins.PluginFactory;
import es.caib.sistra.plugins.login.PluginLoginIntf;
import es.caib.util.StringUtil;

/**
 * @struts.action
 *  path="/mostrarFormularioIncidencias"
 *  scope="request"
 *  validate="false"
 */
public class MostrarFormularioIncidenciasAction extends BaseAction
{
	 public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
             HttpServletResponse response) throws Exception 
     {
		 
		// Intentamos recuperar info del tramite		 
		String tramiteDesc = "";
		String tramiteId = "";
		String procedimientoId = "";
		String nif = "";
		String nombre = "";
		String idPersistencia = "";
		String fechaCreacion = "";
		String lang = "es";
		String nivelAutenticacion = "";
		
		final SesionPagoDelegate dlg = getSesionPago(request);
		DatosPago datosPago = dlg.obtenerDatosPago();
		
		Principal p = request.getUserPrincipal();
		
		PluginLoginIntf plgLogin = PluginFactory.getInstance().getPluginLogin();
		
		char metodoAuth = plgLogin.getMetodoAutenticacion(p);
		String nivelAuth = Character.toString(metodoAuth);
		
		lang = datosPago.getIdioma();
		idPersistencia = datosPago.getIdentificadorTramite();
		tramiteDesc = datosPago.getNombreTramite();		
		tramiteId = datosPago.getModeloTramite();
		procedimientoId = datosPago.getIdProcedimiento();
		nif = datosPago.getNifDeclarante();
		nombre = datosPago.getNombreDeclarante();
		fechaCreacion = StringUtil.fechaACadena(datosPago.getFechaInicioTramite(), StringUtil.FORMATO_TIMESTAMP);
		
		
		 String contextoRaiz = DelegateUtil.getConfiguracionDelegate().obtenerPropiedad("sistra.contextoRaiz.front");
		 String url = contextoRaiz + "/incidencias/formulario?";
		 url += "lang=" + lang;
		 url += "&tramiteDesc=" + encodeParameter(tramiteDesc);
		 url += "&tramiteId=" + encodeParameter(tramiteId);
		 url += "&procedimientoId=" + encodeParameter(procedimientoId);
		 url += "&idPersistencia=" + encodeParameter(idPersistencia);
		 if((nif != null) && (!nif.equals(""))){
			 url += "&nif=" + encodeParameter(nif);
		 }
		 if((nombre != null) && (!nombre.equals(""))){
			 url += "&nombre=" + encodeParameter(nombre);
		 }
		 url += "&fechaCreacion=" + encodeParameter(fechaCreacion);
		 url += "&nivelAutenticacion=" + encodeParameter(nivelAuth);
		 
		 response.sendRedirect(url);		 
		 return null;
     }

	private String encodeParameter(String value)
			throws UnsupportedEncodingException {
		return URLEncoder.encode(value, "ISO-8859-1");
	}
}
