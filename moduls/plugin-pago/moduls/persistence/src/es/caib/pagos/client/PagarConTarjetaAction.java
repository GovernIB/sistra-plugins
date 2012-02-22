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

	private static Log log = LogFactory.getLog(InicioPagoAction.class);

	 
	public Hashtable execute(ClientePagos cliente, Hashtable data) throws Exception{

		PagarConTarjetaService service = new PagarConTarjetaService(cliente.getUrl());
		DatosRespuestaPago ls_resultado = null;
		Hashtable resultado = new Hashtable();
		try {
			//parametros
			String[] refsModelos = (String[])data.get(Constants.KEY_REFS_MODELOS);
			String numeroTarjeta = (String)data.get(Constants.KEY_NUM_TARJETA);
			String caducidadTarjeta = (String)data.get(Constants.KEY_CAD_TARJETA);
			String titularTarjeta = (String)data.get(Constants.KEY_TIT_TARJETA);
			String cvvTarjeta = (String)data.get(Constants.KEY_CVV_TARJETA);
			
			ls_resultado = service.execute(refsModelos, numeroTarjeta, caducidadTarjeta, titularTarjeta, cvvTarjeta, UtilWs.getUsuario());
		} catch (DelegateException de) {
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_PROPERTIES, "Error obteniendo los valores de usuario web service"));
			return resultado;
		} catch (ServiceException e) { 
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_COMUNICACION, "Error en la URL sel servicio PagarConTarjeta"));
			return resultado;
		} catch (RemoteException e) { 
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_COMUNICACION, "Error en la comunicacón con el servicio PagarConTarjeta"));
			return resultado;
		}
		
		if (ls_resultado == null) {
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_RESPUESTA_NULA, "No se ha obtenido respuesta del servicio PagarConTarjeta."));
		} else {
			if (ls_resultado.isPagado()) {
				resultado.put(Constants.KEY_REF_PAGO, ls_resultado.getRefPago());
				resultado.put(Constants.KEY_RESULTADO, Constants.ESTADO_PAGADO);
			} else {
				resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_TARJETA, ls_resultado.getError()));
				resultado.put(Constants.KEY_RESULTADO, Constants.ESTADO_NO_PAGADO);
			}

		}

		return resultado;
	}
}
	
	