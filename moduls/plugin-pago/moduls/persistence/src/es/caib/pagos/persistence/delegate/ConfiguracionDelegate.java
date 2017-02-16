package es.caib.pagos.persistence.delegate;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import es.caib.pagos.persistence.intf.ConfiguracionFacadeLocal;
import es.caib.pagos.persistence.util.ConfiguracionFacadeUtil;
import es.caib.pagos.model.OrganismoInfo;


public class ConfiguracionDelegate implements StatelessDelegate {

    /* ========================================================= */
    /* ======================== MÉTODOS DE NEGOCIO ============= */
    /* ========================================================= */
	public String obtenerPropiedad(String propiedad) throws DelegateException
	{
		try
		{			
			return getFacade().obtenerPropiedad(propiedad);				
		} catch (Exception e) {
	        throw new DelegateException(e);
	    }	 	 
	 }
	
	public OrganismoInfo obtenerOrganismoInfo() throws DelegateException
	{
		try
		{			
			return getFacade().obtenerOrganismoInfo();				
		} catch (Exception e) {
	        throw new DelegateException(e);
	    }	 	 
	 }
	
    /* ========================================================= */
    /* ======================== REFERENCIA AL FACADE  ========== */
    /* ========================================================= */
    private ConfiguracionFacadeLocal getFacade() throws NamingException,RemoteException,CreateException {      	    	
    	return ConfiguracionFacadeUtil.getLocalHome().create();
    }

    protected ConfiguracionDelegate() throws DelegateException {        
    }                  
}

