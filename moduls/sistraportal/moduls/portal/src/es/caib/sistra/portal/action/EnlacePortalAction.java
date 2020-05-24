package es.caib.sistra.portal.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.caib.loginModule.client.SeyconPrincipal;
import es.caib.sistra.model.TramiteInacabado;
import es.caib.sistra.model.TramiteNivel;
import es.caib.sistra.model.TramiteVersion;
import es.caib.sistra.persistence.delegate.ConsultaPADDelegate;
import es.caib.sistra.persistence.delegate.DelegateUtil;
import es.caib.sistra.persistence.delegate.TramiteVersionDelegate;
import es.caib.sistra.portal.Constants;
import es.caib.util.DataUtil;
import es.caib.util.StringUtil;
import es.caib.zonaper.modelInterfaz.PersonaPAD;
import es.caib.zonaper.modelInterfaz.TramitePersistentePAD;
import es.caib.zonaper.persistence.delegate.DelegatePADUtil;
import es.caib.zonaper.persistence.delegate.PadDelegate;

/**
 * @struts.action
 *  path="/enlacePortal"
 *  scope="request"
 *  validate="false"
 *
 * @struts.action-forward
 *  name="success" path="/enlacePortal.jsp"
 *
 *  @struts.action-forward
 *  name="autenticar" path="/protected/autenticar.do"
 *
 */
public class EnlacePortalAction extends Action{

	private static Log log = LogFactory.getLog( EnlacePortalAction.class );

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
    	boolean autenticado = false,conAutenticacion=false,anonimo=false,error=false,continuarAnonimo=false;
    	String modelo="",language="";
		int version=0;
    	List tramitesInacabados = new ArrayList();

    	try{
    		// Recogemos parametros: modelo / version / idioma
    		modelo = request.getParameter("modelo");
    		version = Integer.parseInt(request.getParameter("version"));
    		language = request.getParameter("language");

    		// Cambiamos idioma si es necesario
    		if (language == null) language = Constants.DEFAULT_LANG_KEY;
            request.getSession(true).setAttribute(Globals.LOCALE_KEY, new Locale(language));

    		// Obtenemos niveles de autenticacion soportados
    		TramiteVersionDelegate tvd = DelegateUtil.getTramiteVersionDelegate();
    		TramiteVersion tv = tvd.obtenerTramiteVersionCompleto(modelo,version);
    		String niveles="";
    		for (Iterator it = tv.getNiveles().iterator();it.hasNext();){
    			niveles += ((TramiteNivel) it.next()).getNivelAutenticacion();
    		}
    		conAutenticacion = niveles.indexOf('C') >= 0 || niveles.indexOf('U') >= 0;
    		anonimo = niveles.indexOf('A') >= 0;
    		continuarAnonimo=(tv.getReducido() != 'S');

    		// En caso de estar autenticado obtenemos tramites pendientes
    		if (request.getUserPrincipal() != null){
    			SeyconPrincipal user = (SeyconPrincipal) request.getUserPrincipal();

    			 log.debug("EnlacePortal - Existe usuario autenticado: " + user.getName());

    			if (user.getCredentialType() !=  SeyconPrincipal.ANONYMOUS_CREDENTIAL){
    				// Indicamos que esta autenticado
    				autenticado = true;
    				// Obtenemos los tramites en persistencia para el usuario (tenemos en cuenta flujo)
    	    		PadDelegate pad = DelegatePADUtil.getPadDelegate();
    	    		List tramitesPersistentes = pad.obtenerTramitesPersistentesUsuario(modelo,version);
    	    		for (Iterator it=tramitesPersistentes.iterator();it.hasNext();){
    	    			TramitePersistentePAD t = (TramitePersistentePAD) it.next();

    	    			TramiteInacabado ti = new TramiteInacabado();
    	    			ti.setUltimaModificacion(t.getFechaModificacion());
    	    			ti.setDiasPersistencia(DataUtil.distancia(
    	    								StringUtil.fechaACadena(t.getFechaModificacion(),"dd/MM/yyyy"),
    	    								StringUtil.fechaACadena(t.getFechaCaducidad(),"dd/MM/yyyy"))
    	    								);
    	    			ti.setIdPersistencia(t.getIdPersistencia());

    	    			// Controlamos flujo
    	    			//  - Se ha remitido el tramite a otra persona
    	    			ConsultaPADDelegate consultaPAD = DelegateUtil.getConsultaPADDelegate();
    	    			if (!t.getUsuarioFlujoTramitacion().equals(user.getName())){
     	    				PersonaPAD userPAD = consultaPAD.obtenerDatosPADporUsuarioSeycon(t.getUsuarioFlujoTramitacion());
     	    				if (userPAD != null) ti.setRemitidoA(userPAD.getNombreCompleto());
    	    					else ti.setRemitidoA("Usuario no registrado");
    	    			}
    	    			//  - Otra persona le ha remitido el tramite
    	    			if (t.getUsuarioFlujoTramitacion().equals(user.getName()) && !t.getUsuario().equals(user.getName())){
     	    				PersonaPAD userPAD = consultaPAD.obtenerDatosPADporUsuarioSeycon(t.getUsuario());
     	    				if (userPAD != null) ti.setRemitidoPor(userPAD.getNombreCompleto());
    	    					else ti.setRemitidoPor("Usuario no registrado");
    	    			}

    	    			tramitesInacabados.add(ti);
    	    		}
    			}
    		}

    	}catch(Exception ex){
    		ex.printStackTrace();
    		// Controlar error
    		error = true;
    	}

    	request.setAttribute("error",Boolean.toString(error));
    	request.setAttribute("modelo",modelo);
    	request.setAttribute("version",Integer.toString(version));
    	request.setAttribute("idioma",language);
    	request.setAttribute("autenticado",Boolean.toString(autenticado));
    	request.setAttribute("conAutenticacion",Boolean.toString(conAutenticacion));
    	request.setAttribute("anonimo",Boolean.toString(anonimo));
    	request.setAttribute("continuarAnonimo",Boolean.toString(continuarAnonimo));
    	request.setAttribute("tramitesInacabados",tramitesInacabados);
    	return mapping.findForward("success");
    }
}
