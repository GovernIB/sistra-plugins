package es.caib.pagosTPV.persistence.ejb;


import javax.ejb.CreateException;
import javax.ejb.EJBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.pagosTPV.model.OrganismoInfo;
import es.caib.pagosTPV.persistence.util.Configuracion;

/**
 * EJB que sirve para obtener la configuracion del modulo
 *
 * @ejb.bean
 *  name="pagos/persistence/ConfiguracionTPVFacade"
 *  local-jndi-name = "es.caib.pagosTPV.persistence.ConfiguracionFacade"
 *  type="Stateless"
 *  view-type="local"
 */
public abstract class ConfiguracionFacadeEJB extends HibernateEJB  {
	
	protected static Log log = LogFactory.getLog(ConfiguracionFacadeEJB.class);
	
	/**
     * @ejb.create-method
     * @ejb.permission unchecked = "true"
     */
	public void ejbCreate() throws CreateException {
		super.ejbCreate();
	}
		
	/**
	 * 
	 * Obtiene propiedades de configuracion
	 * 
     * @ejb.interface-method
     * @ejb.permission unchecked = "true"
     */
    public String obtenerPropiedad(String property) {
    	try{
    		return Configuracion.getInstance().getProperty(property);
    	}catch(Exception ex){
    		throw new EJBException(ex);
    	}         
    }
 	
    /**
	 * 
	 * Obtiene las propiedades de configuracion referentes al organismo y las empaqueta en 
	 * una clase de modelo
	 * 
     * @ejb.interface-method
     * @ejb.permission unchecked = "true"
     */
    public OrganismoInfo obtenerOrganismoInfo() {
    	try{
    		OrganismoInfo oi = Configuracion.getInstance().obtenerOrganismoInfo();
    		return oi;
    	}catch(Exception ex){
    		throw new EJBException(ex);
    	}         
    }
    
    /**
   	 * 
   	 * Obtiene las propiedades de configuracion referentes al organismo y las empaqueta en 
   	 * una clase de modelo
   	 * 
        * @ejb.interface-method
        * @ejb.permission unchecked = "true"
        */
       public OrganismoInfo obtenerOrganismoInfo(String entidad) {
       	try{
       		OrganismoInfo oi = Configuracion.getInstance().obtenerOrganismoInfo(entidad);
       		return oi;
       	}catch(Exception ex){
       		throw new EJBException(ex);
       	}         
       }

}
