package es.caib.pagosTPV.persistence.delegate;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import es.caib.pagosTPV.model.Page;
import es.caib.pagosTPV.persistence.intf.BackPagosTPVFacade;
import es.caib.pagosTPV.persistence.util.BackPagosTPVFacadeUtil;

/**
 * Business delegate para operar con asistente pagos
 */
public class BackPagosTPVDelegate implements StatelessDelegate {

    /* ========================================================= */
    /* ======================== MÉTODOS DE NEGOCIO ============= */
    /* ========================================================= */
	
	public Page busquedaPaginadaSesionesTPV(int pagina, int longitudPagina, String nif ) throws DelegateException{
    	try {
            return getFacade().busquedaPaginadaSesionesTPV(pagina, longitudPagina, nif);
        } catch (Exception e) {
            throw new DelegateException(e);
        }
    }
	
	public int confirmarSesionTPV(String localizador) throws DelegateException{
    	try {
           return getFacade().confirmarSesionTPV(localizador);
        } catch (Exception e) {
            throw new DelegateException(e);
        }
    }

	
    /* ========================================================= */
    /* ======================== REFERENCIA AL FACADE  ========== */
    /* ========================================================= */
    private BackPagosTPVFacade getFacade() throws NamingException,CreateException,RemoteException {      	    	
    	return BackPagosTPVFacadeUtil.getHome().create();
    }

    protected BackPagosTPVDelegate() throws DelegateException {       
    }

	               
}
