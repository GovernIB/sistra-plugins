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
	
	public Hashtable execute(final ClientePagos cliente, final Hashtable data){

		final Hashtable resultado = new Hashtable();
		final ImporteTasaService importe = new ImporteTasaService(cliente.getUrl());
		
		try {
			final UsuariosWebServices usuario = UtilWs.getUsuario();
			final String idTasa = (String)data.get(Constants.KEY_ID_TASA);
			final DatosTasa046 ls_resultado = importe.execute(idTasa, usuario);
			if (ls_resultado == null) {
				resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_RESPUESTA_NULA, "No se ha obtenido resultado"));
			} else {
				if (ls_resultado.getCodError() == null) {
					resultado.put(Constants.KEY_IMPORTE, ls_resultado.getImporte());
				} else {
					resultado.put(Constants.KEY_ERROR, new WebServiceError(ls_resultado.getCodError(), ls_resultado.getTextError()));
				}
			}
		} catch (DelegateException de) {
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_PROPERTIES, "Error obteniendo los valores de usuario web service " + de.getMessage()));
			log.error("Error obteniendo los valores de usuario web service ");
		} catch (ServiceException e) { 
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_COMUNICACION, "Error en la URL sel servicio ImporteTasa " + e.getMessage()));
			log.error("Error en la URL sel servicio ImporteTasa ");
		} catch (RemoteException e) { 
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_COMUNICACION, "Error en la comunicacón con el servicio ImporteTasa " + e.getMessage()));
			log.error("Error en la comunicacón con el servicio ImporteTasa ");
		}
		
		return resultado;
	}

}
