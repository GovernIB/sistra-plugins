package es.caib.pagosTPV.persistence.delegate;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import es.caib.pagosTPV.model.OrganismoInfo;
import es.caib.pagosTPV.persistence.intf.ConfiguracionTPVFacadeLocal;
import es.caib.pagosTPV.persistence.util.ConfiguracionTPVFacadeUtil;

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
	
	public OrganismoInfo obtenerOrganismoInfo(String entidad) throws DelegateException
	{
		try
		{			
			return getFacade().obtenerOrganismoInfo(entidad);				
		} catch (Exception e) {
	        throw new DelegateException(e);
	    }	 	 
	 }
	
    /* ========================================================= */
    /* ======================== REFERENCIA AL FACADE  ========== */
    /* ========================================================= */
    private ConfiguracionTPVFacadeLocal getFacade() throws NamingException,RemoteException,CreateException {      	    	
    	return ConfiguracionTPVFacadeUtil.getLocalHome().create();
    }

    protected ConfiguracionDelegate() throws DelegateException {        
    }

	                
}

