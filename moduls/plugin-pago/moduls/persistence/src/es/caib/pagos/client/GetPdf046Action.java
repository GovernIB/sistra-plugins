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

	
	public Hashtable execute(ClientePagos cliente, Hashtable data)  throws Exception{
		
		Hashtable resultado = new Hashtable();
		byte[] ls_resultado = null;
		GetPdf046Service service = new GetPdf046Service(cliente.getUrl());
		try {
			UsuariosWebServices usuario = UtilWs.getUsuario();
			ls_resultado = service.execute((String)data.get(Constants.KEY_LOCALIZADOR), (String)data.get(Constants.KEY_IMPORTE_INGRESAR), 
				(String)data.get(Constants.KEY_NIF_SP), (String)data.get(Constants.KEY_FECHA_CREACION), usuario);
		}catch (DelegateException de) {
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_PROPERTIES, "Error obteniendo los valores de usuario web service"));
			return resultado;
		} catch (ServiceException e) { 
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_COMUNICACION, "Error en la URL del servicio GetPdf046"));
			return resultado;
		} catch (RemoteException e) { 
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_COMUNICACION, "Error en la comunicacón con el servicio GetPdf046"));
			return resultado;
		}
		
		if (ls_resultado == null) {
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_RESPUESTA_NULA, "No se ha obtenido respuesta del servicio GetPdf046."));
		} else {
			resultado.put(Constants.KEY_RESULTADO, ls_resultado);
		}
		
		return resultado;
	}
	
	
}
