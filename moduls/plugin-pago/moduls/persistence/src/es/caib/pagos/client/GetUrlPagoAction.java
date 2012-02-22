package es.caib.pagos.client;

import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.xml.rpc.ServiceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.pagos.persistence.delegate.DelegateException;
import es.caib.pagos.services.GetUrlPagoService;
import es.caib.pagos.services.wsdl.DatosRespuestaGetUrlPago;
import es.caib.pagos.services.wsdl.UsuariosWebServices;
import es.caib.pagos.util.Constants;
import es.caib.pagos.util.UtilWs;

public class GetUrlPagoAction implements WebServiceAction {

	private static Log log = LogFactory.getLog(GetUrlPagoAction.class);

	
	public Hashtable execute(ClientePagos cliente, Hashtable data)  throws Exception{
		
		Hashtable resultado = new Hashtable();
		DatosRespuestaGetUrlPago ls_resultado = null;
		GetUrlPagoService service = new GetUrlPagoService(cliente.getUrl());
		try {
			UsuariosWebServices usuario = UtilWs.getUsuario();
			ls_resultado = service.execute((String[])data.get(Constants.KEY_REFS_MODELOS), (String)data.get(Constants.KEY_CODIGO_ENTIDAD), usuario);
		}catch (DelegateException de) {
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_PROPERTIES, "Error obteniendo los valores de usuario web service"));
			return resultado;
		} catch (ServiceException e) { 
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_COMUNICACION, "Error en la URL del servicio GetUrlPago"));
			return resultado;
		} catch (RemoteException e) { 
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_COMUNICACION, "Error en la comunicacón con el servicio GetUrlPago"));
			return resultado;
		}
		
		if (ls_resultado == null) {
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_RESPUESTA_NULA, "No se ha obtenido respuesta del servicio GetUrlPago."));
		} else {
			resultado.put(Constants.KEY_URL, ls_resultado.getUrl());
			resultado.put(Constants.KEY_REF_PAGO, ls_resultado.getRefPago());
		}
		
		return resultado;
	}
	
	
}
