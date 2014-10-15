package es.caib.regtel.plugincaib.persistence.delegate;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;

import es.caib.redose.modelInterfaz.ReferenciaRDS;
import es.caib.regtel.plugincaib.model.LogUsuariosRegistro;
import es.caib.regtel.plugincaib.model.LogUsuariosRegistroId;
import es.caib.regtel.plugincaib.persistence.intf.LogUsuariosRegistroEJB;
import es.caib.regtel.plugincaib.persistence.util.LogUsuariosRegistroEJBUtil;

/**
 * Business delegate para operar con LogUsuariosRegistroDelegate
 */
public class LogUsuariosRegistroDelegate implements StatelessDelegate {

    /* ========================================================= */
    /* ======================== MÉTODOS DE NEGOCIO ============= */
    /* ========================================================= */
	
	public void realizarLogUsuarioRegistro(LogUsuariosRegistro logUsu) throws DelegateException
	{	
		try {
        	getFacade().realizarLogUsuarioRegistro(logUsu);
		} catch (Exception e) {
        	throw new DelegateException(e);
    	}
	}
	
	public LogUsuariosRegistro obtenerLogUsuarioRegistro(LogUsuariosRegistroId id) throws DelegateException
	{
		try {
        	return getFacade().obtenerLogUsuarioRegistro(id);
		} catch (Exception e) {
        	throw new DelegateException(e);
    	}
	}

	
    /* ========================================================= */
    /* ======================== REFERENCIA AL FACADE  ========== */
    /* ========================================================= */
    private LogUsuariosRegistroEJB getFacade() throws NamingException,CreateException,RemoteException {      	    	
    	return LogUsuariosRegistroEJBUtil.getHome().create();
    }
	               
}
