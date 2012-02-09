package es.caib.pagos.client;

import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.xml.rpc.ServiceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.pagos.persistence.delegate.DelegateException;
import es.caib.pagos.services.ImporteTasaService;
import es.caib.pagos.services.wsdl.DatosTasa046;
import es.caib.pagos.services.wsdl.UsuariosWebServices;
import es.caib.pagos.util.Constants;
import es.caib.pagos.util.UtilWs;

public class ImporteTasaAction implements WebServiceAction {

	private static Log log = LogFactory.getLog(ImporteTasaAction.class);
	

	public Hashtable execute(ClientePagos cliente, Hashtable data) throws Exception{
		Hashtable resultado = new Hashtable();
		String idTasa = (String)data.get(Constants.KEY_ID_TASA);

		ImporteTasaService importe = new ImporteTasaService(cliente.getUrl());
		DatosTasa046 ls_resultado = null;
		try {
			UsuariosWebServices usuario = UtilWs.getUsuario();
			ls_resultado = importe.execute(idTasa, usuario);
		} catch (DelegateException de) {
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_PROPERTIES, "Error obteniendo los valores de usuario web service " + de.getMessage()));
			return resultado;
		} catch (ServiceException e) { 
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_COMUNICACION, "Error en la URL sel servicio ImporteTasa " + e.getMessage()));
			return resultado;
		} catch (RemoteException e) { 
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_COMUNICACION, "Error en la comunicac�n con el servicio ImporteTasa " + e.getMessage()));
			return resultado;
		}
		if (ls_resultado == null) {
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_RESPUESTA_NULA, "No se ha obtenido resultado"));
		} else {
			if (ls_resultado.getCodError() == null) {
				resultado.put(Constants.KEY_IMPORTE, ls_resultado.getImporte());
			} else {
				resultado.put(Constants.KEY_ERROR, new WebServiceError(ls_resultado.getCodError(), ls_resultado.getTextError()));
			}
		}
		
		return resultado;
	}

}
