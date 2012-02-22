package es.caib.pagos.client;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.xml.rpc.ServiceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.pagos.persistence.delegate.DelegateException;
import es.caib.pagos.services.ComprobarPagoService;
import es.caib.pagos.services.wsdl.DatosRespuesta046;
import es.caib.pagos.services.wsdl.UsuariosWebServices;
import es.caib.pagos.util.Constants;
import es.caib.pagos.util.UtilWs;
import gva.ideas.excepciones.ExcepcionMensaje;

public class ComprobarPagoAction implements WebServiceAction {


	private static Log log = LogFactory.getLog(ComprobarPagoAction.class);

	public Hashtable execute(ClientePagos cliente, Hashtable data)  throws Exception{
		Hashtable resultado = new Hashtable();
		String localizador = (String)data.get(Constants.KEY_LOCALIZADOR);
		
        ComprobarPagoService comprobante = new ComprobarPagoService(cliente.getUrl());
		DatosRespuesta046 ls_resultado = null;
		try {
			UsuariosWebServices usuario = UtilWs.getUsuario();
			ls_resultado = comprobante.execute(localizador, usuario);
		} catch(DelegateException de) {
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_PROPERTIES, "Error obteniendo los valores de usuario web service"));
			log.error("Error obteniendo los valores de usuario web service");
			return resultado;
		} catch(ServiceException se){
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_COMUNICACION, "Error en la URL sel servicio ComprobarPago"));
			log.error("Error en la URL sel servicio ComprobarPago");
			return resultado;
		} catch(RemoteException re) {
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_COMUNICACION, "Error en la comunicacón con el servicio comprobarPago"));
			log.error("Error en la comunicacón con el servicio comprobarPago");
			return resultado;
		}
	
		if (ls_resultado == null) {
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_RESPUESTA_NULA, "No se ha obtenido respuesta del servicio ComprobarPago."));
			log.error("No se ha obtenido respuesta del servicio ComprobarPago.");
		} else {
			
			if (Constants.ESTADO_PAGADO.equals(ls_resultado.getEstadoPago())) {
				try {
					Hashtable datos = new Hashtable();
					datos.put(Constants.KEY_LOCALIZADOR, ls_resultado.getLocalizador());
					datos.put(Constants.KEY_FECHA_PAGO, ls_resultado.getFechaPago());
					String cadenaDatos = UtilWs.getCadenaDatos(datos);
					boolean mensajeOk = UtilWs.validarRespuesta(cadenaDatos, ls_resultado.getFirma());
					if (mensajeOk) {
						resultado.put(Constants.KEY_FECHA_PAGO, ls_resultado.getFechaPago());
						resultado.put(Constants.KEY_LOCALIZADOR, ls_resultado.getLocalizador());
						resultado.put(Constants.KEY_FIRMA, ls_resultado.getFirma());
						resultado.put(Constants.KEY_JUSTIFICANTE, UtilWs.getJustificante(cadenaDatos, ls_resultado.getFirma()));
					} else {
						resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_FIRMA, "La respuesta no se reconoce como autentica. Error al comprobar la firma"));
						log.error("La respuesta no se reconoce como autentica. Error al comprobar la firma");
					}
				} catch (ExcepcionMensaje em) {
					resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_FIRMA, "Error comprobando la firma. Mensaje incorrecto"));
					log.error("Error comprobando la firma. Mensaje incorrecto");
					return resultado;
				} catch (UnsupportedEncodingException uee) {
					resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_FIRMA, "Error al pasar a String con el CHARSET " + Constants.CHARSET));
					log.error("Error al pasar a String con el CHARSET " + Constants.CHARSET);
					return resultado;
				}
			}
			
			if (ls_resultado.getCodError() != null) {
				resultado.put(Constants.KEY_ERROR, new WebServiceError(ls_resultado.getCodError(), ls_resultado.getTextError()));
			}
		}

		return resultado;
	}
	
}
