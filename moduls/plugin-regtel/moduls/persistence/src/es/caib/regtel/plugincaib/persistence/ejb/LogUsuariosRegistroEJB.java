package es.caib.regtel.plugincaib.persistence.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import net.sf.hibernate.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.regtel.plugincaib.model.LogUsuariosRegistro;
import es.caib.regtel.plugincaib.model.LogUsuariosRegistroId;

/**
 * SessionBean que implementa la interfaz de LogUsuariosRegistroEJB.
 *
 * @ejb.bean
 *  name="regtel/plugincaib/persistence/LogUsuariosRegistroEJB"
 *  jndi-name="es.caib.regtel.plugincaib.persistence.LogUsuariosRegistroEJB"
 *  type="Stateless"
 *  view-type="remote" 
 *  transaction-type="Container"
 *
 * @ejb.transaction type="Required"
 * 
 */
public class LogUsuariosRegistroEJB extends HibernateEJB  {
 
	private static Log log = LogFactory.getLog(LogUsuariosRegistroEJB.class);
	
	/**
     * @ejb.create-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     */
	public void ejbCreate() throws CreateException {	
		super.ejbCreate();
	}
	
	public void ejbActivate() throws EJBException, RemoteException {
	}

	public void ejbPassivate() throws EJBException, RemoteException {
	}
	
	/**
	 * Realiza log de usuario registro.
	 * 
     * @ejb.interface-method
     * @ejb.permission role-name="${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     */
	public void realizarLogUsuarioRegistro(LogUsuariosRegistro logUsu) {
		
		Session session = getSession();
		
		try{
			session.save(logUsu);			
		}catch (Exception ex){
			log.error("Exception guardando log usuario registro",ex);
			throw new EJBException("Exception guardando log usuario registro",ex);
		}finally{
            close(session);
        }
	}
	
	
	/**
	 * Recupera log de usuario registro.
	 * @return 
	 * 
     * @ejb.interface-method
     * @ejb.permission role-name="${role.auto}"
     */
	public LogUsuariosRegistro obtenerLogUsuarioRegistro(LogUsuariosRegistroId id) {
		Session session = getSession();
		try{
			LogUsuariosRegistro logusu = (LogUsuariosRegistro) session.get(LogUsuariosRegistro.class, id);
			return logusu;			
		}catch (Exception ex){
			log.error("Exception recuperando log usuario registro",ex);
			throw new EJBException("Exception recuperando log usuario registro",ex);
		}finally{
            close(session);
        }
	}
	

}
