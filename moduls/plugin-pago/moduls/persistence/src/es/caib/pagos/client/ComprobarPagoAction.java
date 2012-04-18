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
import es.caib.pagos.util.FuncionesCadena;
import es.caib.pagos.util.UtilWs;
import gva.ideas.MensajeFirmado;
import gva.ideas.excepciones.ExcepcionMensaje;

public class ComprobarPagoAction implements WebServiceAction {


	private static Log log = LogFactory.getLog(ComprobarPagoAction.class);

	public Hashtable execute(final ClientePagos cliente, final Hashtable data){

		Hashtable resultado = new Hashtable();
		final String localizador = (String)data.get(Constants.KEY_LOCALIZADOR);
        final ComprobarPagoService comprobante = new ComprobarPagoService(cliente.getUrl());

		try {
			final UsuariosWebServices usuario = UtilWs.getUsuario();
			//localizador = "0462812795471";//TODO hardcode para pruebas
			final DatosRespuesta046 ls_resultado = comprobante.execute(localizador, usuario);
			resultado = tratarResultado(ls_resultado);
		} catch(DelegateException de) {
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_PROPERTIES, "Error obteniendo los valores de usuario web service"));
			log.error("Error obteniendo los valores de usuario web service");
		} catch(ServiceException se){
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_COMUNICACION, "Error en la URL sel servicio ComprobarPago"));
			log.error("Error en la URL sel servicio ComprobarPago");
		} catch(RemoteException re) {
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_COMUNICACION, "Error en la comunicacón con el servicio comprobarPago"));
			log.error("Error en la comunicacón con el servicio comprobarPago");
		}
	
		return resultado;
	}

	/**
	 * Trata el resultado obtenido de la llamada al WS
	 * @param ls_resultado resultado
	 * @return
	 */
	private Hashtable tratarResultado(final DatosRespuesta046 ls_resultado) {
		
		final Hashtable resultado = new Hashtable();
		
		if (ls_resultado == null) {
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_RESPUESTA_NULA, "No se ha obtenido respuesta del servicio ComprobarPago."));
			log.error("No se ha obtenido respuesta del servicio ComprobarPago.");
		} else {
			
			if (Constants.ESTADO_PAGADO.equals(ls_resultado.getEstadoPago())) {
				try {
					final boolean mensajeOk = comprobarFirma(ls_resultado.getFirma());
					
					if (mensajeOk) {
						resultado.put(Constants.KEY_FECHA_PAGO, ls_resultado.getFechaPago());
						resultado.put(Constants.KEY_LOCALIZADOR, ls_resultado.getLocalizador());
						resultado.put(Constants.KEY_FIRMA, ls_resultado.getFirma());
					} else {
						resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_FIRMA, "La respuesta no se reconoce como autentica. Error al comprobar la firma"));
						log.error("La respuesta no se reconoce como autentica. Error al comprobar la firma");
					}
				} catch (ExcepcionMensaje em) {
					log.error("Error comprobando la firma: ", em);
					//final String error = em.getCodigoError()+"\n"+em.getDescripcionError()+"\n"+em.getDescripcionErrorNativo();
					resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_FIRMA, "Error comprobando la firma. Mensaje incorrecto"));
				} catch (UnsupportedEncodingException uee) {
					resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_FIRMA, "Error al pasar a String con el CHARSET " + Constants.CHARSET));
					log.error("Error al pasar a String con el CHARSET " + Constants.CHARSET);
				}
			} else {
				if (ls_resultado.getCodError() != null) {
					resultado.put(Constants.KEY_ERROR, new WebServiceError(ls_resultado.getCodError(), ls_resultado.getTextError()));
				}
			}
		}

		return resultado;
	}
	
	/**
	 * Valida que la respuesta proviene de la ATIB
	 * @param datos
	 * @param firma
	 * @return
	 * @throws ExcepcionMensaje
	 * @throws UnsupportedEncodingException
	 */
	private boolean comprobarFirma(final String justificante) throws ExcepcionMensaje, UnsupportedEncodingException
	{
		
		int li_ini = justificante.indexOf("<DATOS_PAGO>");
		int li_fin = justificante.indexOf("</DATOS_PAGO>");				
		final String ls_datos = justificante.substring(li_ini,li_fin + 13);
		li_ini = justificante.indexOf("<FIRMA>");
		li_fin = justificante.indexOf("</FIRMA>");
		final String ls_pkcs7 = justificante.substring(li_ini + 7,li_fin);

		final MensajeFirmado l_mf = new MensajeFirmado();
		l_mf.cargarDeString(ls_pkcs7);	
		l_mf.setDatos(ls_datos.getBytes(FuncionesCadena.getCharset()));
		return l_mf.comprobarIntegridadFirma();
		
	}
	
	
	
}
