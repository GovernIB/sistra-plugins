package es.caib.pagos.front.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.pagos.persistence.delegate.DelegateUtil;
import es.caib.pagos.persistence.delegate.SesionPagoDelegate;
import es.caib.sistra.plugins.pagos.DatosPago;
import es.caib.util.StringUtil;

/**
 * @struts.action
 *  path="/mostrarFormularioIncidencias"
 *  scope="request"
 *  validate="false"
 */
public class MostrarFormularioIncidenciasAction extends BaseAction
{
	 public ActionForward executeTask(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
		
		final SesionPagoDelegate dlg = getSesionPago(request);
		DatosPago datosPago = dlg.obtenerDatosPago();
		
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
		 url += "&nif=" + encodeParameter(nif);
		 url += "&nombre=" + encodeParameter(nombre);
		 url += "&fechaCreacion=" + encodeParameter(fechaCreacion);
		 
		 response.sendRedirect(url);		 
		 return null;
     }

	private String encodeParameter(String value)
			throws UnsupportedEncodingException {
		return URLEncoder.encode(value, "ISO-8859-1");
	}
}
