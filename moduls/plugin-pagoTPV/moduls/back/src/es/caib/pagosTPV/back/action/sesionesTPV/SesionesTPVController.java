package es.caib.pagosTPV.back.action.sesionesTPV;

import org.apache.struts.tiles.Controller;
import org.apache.struts.tiles.ComponentContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.pagosTPV.back.Constants;
import es.caib.pagosTPV.persistence.delegate.BackPagosTPVDelegate;
import es.caib.pagosTPV.persistence.delegate.DelegateException;
import es.caib.pagosTPV.persistence.delegate.DelegateUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Recupera sesiones TPV.
 */
public class SesionesTPVController implements Controller{
    protected static Log log = LogFactory.getLog(SesionesTPVController.class);

    private static final String PAGE_PARAM = "pagina";
    private static final int LONGITUD_PAGINA = 20;
    
    public void perform(ComponentContext tileContext,
                        HttpServletRequest request, HttpServletResponse response,
                        ServletContext servletContext)
            throws ServletException, IOException {

         try {
            log.info("Entramos en ListaLogsGestorDocumentalErroresController");

            String strPage = request.getParameter( PAGE_PARAM );
            strPage = StringUtils.isEmpty( strPage ) ? "0" : strPage;
        	int pagina = Integer.parseInt( strPage, 10 );
        	
        	String strNif = request.getParameter("nif");
        	if (strNif == null) {
        		 strNif = (String) request.getSession().getAttribute(Constants.NIF_KEY);
        	}
        	if (StringUtils.isEmpty(strNif)) {
        		strNif = null;
        	}
        	request.getSession().setAttribute(Constants.NIF_KEY, strNif);
        	
        	
        	BackPagosTPVDelegate delegate = DelegateUtil.getBackPagosTPVDelegate();
        	request.setAttribute( "page", delegate.busquedaPaginadaSesionesTPV(pagina, LONGITUD_PAGINA, strNif ) );

        } catch (DelegateException e) {
            throw new ServletException(e);
        }
    }
}

