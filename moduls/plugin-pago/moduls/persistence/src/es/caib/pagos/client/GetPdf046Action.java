package es.caib.pagos.client;

import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.xml.rpc.ServiceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.pagos.persistence.delegate.DelegateException;
import es.caib.pagos.services.GetPdf046Service;
import es.caib.pagos.services.wsdl.UsuariosWebServices;
import es.caib.pagos.util.Constants;
import es.caib.pagos.util.UtilWs;

public class GetPdf046Action implements WebServiceAction {

	private static Log log = LogFactory.getLog(GetPdf046Action.class);
	
	public Hashtable execute(final ClientePagos cliente, final Hashtable data){
		
		final Hashtable resultado = new Hashtable();
		final GetPdf046Service service = new GetPdf046Service(cliente.getUrl());
		try {
			final UsuariosWebServices usuario = UtilWs.getUsuario();
			final byte[] ls_resultado = service.execute((String)data.get(Constants.KEY_LOCALIZADOR), (String)data.get(Constants.KEY_IMPORTE_INGRESAR), 
				(String)data.get(Constants.KEY_NIF_SP), (String)data.get(Constants.KEY_FECHA_CREACION), usuario);
			if (ls_resultado == null) {
				resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_RESPUESTA_NULA, "No se ha obtenido respuesta del servicio GetPdf046."));
			} else {
				resultado.put(Constants.KEY_RESULTADO, ls_resultado);
			}
		}catch (DelegateException de) {
			log.error("Error obteniendo los valores de usuario web service");
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_PROPERTIES, "Error obteniendo los valores de usuario web service"));
		} catch (ServiceException e) { 
			log.error("Error en la URL del servicio GetPdf046");
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_COMUNICACION, "Error en la URL del servicio GetPdf046"));
		} catch (RemoteException e) { 
			log.error("Error en la comunicación con el servicio GetPdf046");
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_COMUNICACION, "Error en la comunicación con el servicio GetPdf046"));
		}
		
		return resultado;
	}
	
	
}
