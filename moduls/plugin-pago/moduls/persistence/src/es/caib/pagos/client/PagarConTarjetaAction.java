package es.caib.pagos.client;

import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.xml.rpc.ServiceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.pagos.persistence.delegate.DelegateException;
import es.caib.pagos.services.PagarConTarjetaService;
import es.caib.pagos.services.wsdl.DatosRespuestaPago;
import es.caib.pagos.util.Constants;
import es.caib.pagos.util.UtilWs;

public class PagarConTarjetaAction implements WebServiceAction {
	 
	private static Log log = LogFactory.getLog(PagarConTarjetaAction.class);
	
	public Hashtable execute(final ClientePagos cliente, final Hashtable data) {

		final PagarConTarjetaService service = new PagarConTarjetaService(cliente.getUrl());
		
		final Hashtable resultado = new Hashtable();
		try {
			//parametros
			final String[] refsModelos = (String[])data.get(Constants.KEY_REFS_MODELOS);
			final String datosTarjeta = (String)data.get(Constants.KEY_DATOS_TARJETA);
			
			final DatosRespuestaPago ls_resultado = service.execute(refsModelos, datosTarjeta, UtilWs.getUsuario());
			
			if (ls_resultado == null) {
				resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_RESPUESTA_NULA, "No se ha obtenido respuesta del servicio PagarConTarjeta."));
				log.error("No se ha obtenido respuesta del servicio PagarConTarjeta.");
			} else {
				if (ls_resultado.isPagado()) {
					resultado.put(Constants.KEY_REF_PAGO, ls_resultado.getRefPago());
					resultado.put(Constants.KEY_RESULTADO, Constants.ESTADO_PAGADO);
				} else {
					resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_TARJETA, ls_resultado.getError()));
					log.error("Pago no realizado. Respuesta --> " + ls_resultado.getError());
					resultado.put(Constants.KEY_RESULTADO, Constants.ESTADO_NO_PAGADO);
				}

			}
			
		} catch (DelegateException de) {
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_PROPERTIES, "Error obteniendo los valores de usuario web service"));
			log.error("Error obteniendo los valores de usuario web service");
		} catch (ServiceException e) { 
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_COMUNICACION, "Error en la URL sel servicio PagarConTarjeta"));
			log.error("Error en la URL sel servicio PagarConTarjeta");
		} catch (RemoteException e) { 
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_COMUNICACION, "Error en la comunicacón con el servicio PagarConTarjeta"));
			log.error("Error en la comunicacón con el servicio PagarConTarjeta");
		}

		return resultado;
	}
}
	
	