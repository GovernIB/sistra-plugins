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
	
	public Hashtable execute(final ClientePagos cliente, final Hashtable data){
		
		final Hashtable resultado = new Hashtable();
		final GetUrlPagoService service = new GetUrlPagoService(cliente.getUrl());
		try {
			final UsuariosWebServices usuario = UtilWs.getUsuario();
			final DatosRespuestaGetUrlPago ls_resultado = service.execute((String[])data.get(Constants.KEY_REFS_MODELOS), (String)data.get(Constants.KEY_CODIGO_ENTIDAD), (String)data.get(Constants.KEY_URL), usuario);
			if (ls_resultado == null) {
				log.error("No se ha obtenido respuesta del servicio GetUrlPago.");
				resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_RESPUESTA_NULA, "No se ha obtenido respuesta del servicio GetUrlPago."));
			} else {
				resultado.put(Constants.KEY_URL, ls_resultado.getUrl());
				resultado.put(Constants.KEY_REF_PAGO, ls_resultado.getRefPago());
			}
		}catch (DelegateException de) {
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_PROPERTIES, "Error obteniendo los valores de usuario web service"));
			log.error("Error obteniendo los valores de usuario web service");
		} catch (ServiceException e) { 
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_COMUNICACION, "Error en la URL del servicio GetUrlPago"));
			log.error("Error en la URL del servicio GetUrlPago");
		} catch (RemoteException e) { 
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_COMUNICACION, "Error en la comunicacón con el servicio GetUrlPago"));
			log.error("Error en la comunicacón con el servicio GetUrlPago");
		}

		return resultado;
	}
	
	
}
