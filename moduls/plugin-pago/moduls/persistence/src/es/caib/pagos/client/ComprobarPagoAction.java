package es.caib.pagos.client;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
			if (ls_resultado.getCodError() == null) {
				//TODO comprobacion de firma
				//antes de hacer nada comprobamos la firma
//				boolean firmaOk = false;
//				try {
//					firmaOk = UtilWs.comprobarFirma(ls_resultado.getFirma());
//				} catch (ExcepcionMensaje em) {
//					resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_GENERAL, "Error comprobando la firma. Mensaje incorrecto"));
//					log.error("Error comprobando la firma. Mensaje incorrecto");
//					return resultado;
//				} catch (UnsupportedEncodingException uee) {
//					resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_GENERAL, "Error al pasar a String con el CHARSET " + Constants.CHARSET));
//					log.error("Error al pasar a String con el CHARSET " + Constants.CHARSET);
//					return resultado;
//				}
//				if (firmaOk) {
//					log.debug("Se ha comprobado correctamente la firma: " + ls_resultado.getFirma());
					if (Constants.ESTADO_PAGADO.equals(ls_resultado.getEstadoPago())) {
						try {
							SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
							Date fechaPago = sdf.parse(ls_resultado.getFechaPago());
							resultado.put(Constants.KEY_FECHA_PAGO, ls_resultado.getFechaPago());
						} catch (ParseException pe) {
							resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_FECHA_PAGO, "Error creando la fecha de pago"));
							log.error("Error creando la fecha de pago.");
						}
					} else {
						resultado.put(Constants.KEY_FECHA_PAGO, "");
					}
					//resultado.put(Constants.KEY_ESTADO, ls_resultado.getEstadoPago());
				
//				} else {
//					resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_WS_FIRMA, "La respuesta no se reconoce como autentica. Error al comprobar la firma"));
//					log.error("La respuesta no se reconoce como autentica. Error al comprobar la firma");
//				}
			} else {
				resultado.put(Constants.KEY_ERROR, new WebServiceError(ls_resultado.getCodError(), ls_resultado.getTextError()));
			}
		}

		return resultado;
	}
	
	
//	public static void main(String[] arg){
//		/*	
//		 	Resultado ComprobarPago: PA#PEpVU1RJRklDQU5URV9QQUdPPjxEQVRPU19QQUdPPjxMT0NBTElaQURPUj4wNDYyODExNTM3MDQ1PC9MT0NBTElaQURPUj48RFVJPjA0NjI4MTE1MzcwNDU8L0RVST48RkVDSEFfUEFHTz4yMDEwMDExODE2MDUxNDwvRkVDSEFfUEFHTz48L0RBVE9TX1BBR08+PEZJUk1BPkFBPT08L0ZJUk1BPjwvSlVTVElGSUNBTlRFX1BBR08+
//	 		Resultado ComprobarPago Justificante: <JUSTIFICANTE_PAGO><DATOS_PAGO><LOCALIZADOR>0462811537045</LOCALIZADOR><DUI>0462811537045</DUI><FECHA_PAGO>20100118160514</FECHA_PAGO></DATOS_PAGO><FIRMA>AA==</FIRMA></JUSTIFICANTE_PAGO>
//	 	*/
//		// RWwgY29uanVudG8gZGUgY2xhdmVzIG5vIGV4aXN0ZQ0K
//		try{
//			
//			
//			MensajeFirmado l_mf = new MensajeFirmado();
//			byte [] handleContent = Base64.decode("PA#PEpVU1RJRklDQU5URV9QQUdPPjxEQVRPU19QQUdPPjxMT0NBTElaQURPUj4wNDYyODExNTM3MDQ1PC9MT0NBTElaQURPUj48RFVJPjA0NjI4MTE1MzcwNDU8L0RVST48RkVDSEFfUEFHTz4yMDEwMDExODE2MDUxNDwvRkVDSEFfUEFHTz48L0RBVE9TX1BBR08+PEZJUk1BPkFBPT08L0ZJUk1BPjwvSlVTVElGSUNBTlRFX1BBR08+");
//			String firma = new String(handleContent, Constants.CHARSET);
//			l_mf.cargarDeString(firma);
//			String ls_datos = 
//			l_mf.setDatos(ls_datos.getBytes(FuncionesCadena.getCharset()));
//			return l_mf.comprobarIntegridadFirma();
//			
////			ComprobarPagoAction a = new ComprobarPagoAction();
////			byte [] handleContent = Base64.decode("PA#PEpVU1RJRklDQU5URV9QQUdPPjxEQVRPU19QQUdPPjxMT0NBTElaQURPUj4wNDYyODExNTM3MDQ1PC9MT0NBTElaQURPUj48RFVJPjA0NjI4MTE1MzcwNDU8L0RVST48RkVDSEFfUEFHTz4yMDEwMDExODE2MDUxNDwvRkVDSEFfUEFHTz48L0RBVE9TX1BBR08+PEZJUk1BPkFBPT08L0ZJUk1BPjwvSlVTVElGSUNBTlRFX1BBR08+");
////			String ls_justificante = new String(handleContent, Constants.CHARSET);
////		
////			System.out.println(ls_justificante);
//			
//			
////			handleContent = Base64.decode("AA==");
////			ls_justificante = new String(handleContent,Constants.CHARSET);
////			System.out.println(ls_justificante);
//			
//			//System.out.println(a.comprobarFirma("<JUSTIFICANTE_PAGO><DATOS_PAGO><LOCALIZADOR>0462811537422</LOCALIZADOR><DUI>0462811537422</DUI><FECHA_PAGO>20100119120418</FECHA_PAGO></DATOS_PAGO><FIRMA>RWwgY29uanVudG8gZGUgY2xhdmVzIG5vIGV4aXN0ZQ0K</FIRMA></JUSTIFICANTE_PAGO>"));
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		
		/*
		 2010-01-19 12:06:54,056 DEBUG [es.caib.pagos.client.ClientePagos] comprobarPago (0462811537422)
		 2010-01-19 12:07:08,061 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago: PA#PEpVU1RJRklDQU5URV9QQUdPPjxEQVRPU19QQUdPPjxMT0NBTElaQURPUj4wNDYyODExNTM3NDIyPC9MT0NBTElaQURPUj48RFVJPjA0NjI4MTE1Mzc0MjI8L0RVST48RkVDSEFfUEFHTz4yMDEwMDExOTEyMDQxODwvRkVDSEFfUEFHTz48L0RBVE9TX1BBR08+PEZJUk1BPlJXd2dZMjl1YW5WdWRHOGdaR1VnWTJ4aGRtVnpJRzV2SUdWNGFYTjBaUTBLPC9GSVJNQT48L0pVU1RJRklDQU5URV9QQUdPPg==
		 2010-01-19 12:07:08,061 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago Justificante: <JUSTIFICANTE_PAGO><DATOS_PAGO><LOCALIZADOR>0462811537422</LOCALIZADOR><DUI>0462811537422</DUI><FECHA_PAGO>20100119120418</FECHA_PAGO></DATOS_PAGO><FIRMA>RWwgY29uanVudG8gZGUgY2xhdmVzIG5vIGV4aXN0ZQ0K</FIRMA></JUSTIFICANTE_PAGO>
		 2010-01-19 12:07:08,062 ERROR [es.caib.pagos.client.ComprobarPagoAction] comprobarFirma: 

		 
		 2010-01-19 12:07:08,090 DEBUG [es.caib.pagos.client.ClientePagos] comprobarPago (0462811537422)
		 2010-01-19 12:07:22,086 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago: PA#PEpVU1RJRklDQU5URV9QQUdPPjxEQVRPU19QQUdPPjxMT0NBTElaQURPUj4wNDYyODExNTM3NDIyPC9MT0NBTElaQURPUj48RFVJPjA0NjI4MTE1Mzc0MjI8L0RVST48RkVDSEFfUEFHTz4yMDEwMDExOTEyMDQxODwvRkVDSEFfUEFHTz48L0RBVE9TX1BBR08+PEZJUk1BPlJXd2dZMjl1YW5WdWRHOGdaR1VnWTJ4aGRtVnpJRzV2SUdWNGFYTjBaUTBLPC9GSVJNQT48L0pVU1RJRklDQU5URV9QQUdPPg==
		 2010-01-19 12:07:22,086 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago Justificante: <JUSTIFICANTE_PAGO><DATOS_PAGO><LOCALIZADOR>0462811537422</LOCALIZADOR><DUI>0462811537422</DUI><FECHA_PAGO>20100119120418</FECHA_PAGO></DATOS_PAGO><FIRMA>RWwgY29uanVudG8gZGUgY2xhdmVzIG5vIGV4aXN0ZQ0K</FIRMA></JUSTIFICANTE_PAGO>
		 2010-01-19 12:07:22,088 ERROR [es.caib.pagos.client.ComprobarPagoAction] comprobarFirma: 

		 
		 2010-01-19 12:07:22,111 DEBUG [es.caib.pagos.client.ClientePagos] comprobarPago (0462811537422)
		 2010-01-19 12:07:36,556 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago: PA#PEpVU1RJRklDQU5URV9QQUdPPjxEQVRPU19QQUdPPjxMT0NBTElaQURPUj4wNDYyODExNTM3NDIyPC9MT0NBTElaQURPUj48RFVJPjA0NjI4MTE1Mzc0MjI8L0RVST48RkVDSEFfUEFHTz4yMDEwMDExOTEyMDQxODwvRkVDSEFfUEFHTz48L0RBVE9TX1BBR08+PEZJUk1BPlJXd2dZMjl1YW5WdWRHOGdaR1VnWTJ4aGRtVnpJRzV2SUdWNGFYTjBaUTBLPC9GSVJNQT48L0pVU1RJRklDQU5URV9QQUdPPg==
		 2010-01-19 12:07:36,556 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago Justificante: <JUSTIFICANTE_PAGO><DATOS_PAGO><LOCALIZADOR>0462811537422</LOCALIZADOR><DUI>0462811537422</DUI><FECHA_PAGO>20100119120418</FECHA_PAGO></DATOS_PAGO><FIRMA>RWwgY29uanVudG8gZGUgY2xhdmVzIG5vIGV4aXN0ZQ0K</FIRMA></JUSTIFICANTE_PAGO>
		 2010-01-19 12:07:36,557 ERROR [es.caib.pagos.client.ComprobarPagoAction] comprobarFirma: 

		 
		 2010-01-19 12:09:18,938 DEBUG [es.caib.pagos.client.ClientePagos] comprobarPago (0462811537422)
		 2010-01-19 12:09:32,812 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago: PA#PEpVU1RJRklDQU5URV9QQUdPPjxEQVRPU19QQUdPPjxMT0NBTElaQURPUj4wNDYyODExNTM3NDIyPC9MT0NBTElaQURPUj48RFVJPjA0NjI4MTE1Mzc0MjI8L0RVST48RkVDSEFfUEFHTz4yMDEwMDExOTEyMDQxODwvRkVDSEFfUEFHTz48L0RBVE9TX1BBR08+PEZJUk1BPlJXd2dZMjl1YW5WdWRHOGdaR1VnWTJ4aGRtVnpJRzV2SUdWNGFYTjBaUTBLPC9GSVJNQT48L0pVU1RJRklDQU5URV9QQUdPPg==
		 2010-01-19 12:09:32,812 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago Justificante: <JUSTIFICANTE_PAGO><DATOS_PAGO><LOCALIZADOR>0462811537422</LOCALIZADOR><DUI>0462811537422</DUI><FECHA_PAGO>20100119120418</FECHA_PAGO></DATOS_PAGO><FIRMA>RWwgY29uanVudG8gZGUgY2xhdmVzIG5vIGV4aXN0ZQ0K</FIRMA></JUSTIFICANTE_PAGO>
		 2010-01-19 12:09:32,814 ERROR [es.caib.pagos.client.ComprobarPagoAction] comprobarFirma: 

		 
		 2010-01-19 12:10:01,964 DEBUG [es.caib.pagos.client.ClientePagos] comprobarPago (0462811537422)
		 2010-01-19 12:10:15,846 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago: PA#PEpVU1RJRklDQU5URV9QQUdPPjxEQVRPU19QQUdPPjxMT0NBTElaQURPUj4wNDYyODExNTM3NDIyPC9MT0NBTElaQURPUj48RFVJPjA0NjI4MTE1Mzc0MjI8L0RVST48RkVDSEFfUEFHTz4yMDEwMDExOTEyMDQxODwvRkVDSEFfUEFHTz48L0RBVE9TX1BBR08+PEZJUk1BPlJXd2dZMjl1YW5WdWRHOGdaR1VnWTJ4aGRtVnpJRzV2SUdWNGFYTjBaUTBLPC9GSVJNQT48L0pVU1RJRklDQU5URV9QQUdPPg==
		 2010-01-19 12:10:15,846 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago Justificante: <JUSTIFICANTE_PAGO><DATOS_PAGO><LOCALIZADOR>0462811537422</LOCALIZADOR><DUI>0462811537422</DUI><FECHA_PAGO>20100119120418</FECHA_PAGO></DATOS_PAGO><FIRMA>RWwgY29uanVudG8gZGUgY2xhdmVzIG5vIGV4aXN0ZQ0K</FIRMA></JUSTIFICANTE_PAGO>
		 2010-01-19 12:10:15,848 ERROR [es.caib.pagos.client.ComprobarPagoAction] comprobarFirma: 

		 
		 2010-01-19 12:11:08,318 DEBUG [es.caib.pagos.client.ClientePagos] comprobarPago (0462811537422)
		 2010-01-19 12:11:22,239 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago: PA#PEpVU1RJRklDQU5URV9QQUdPPjxEQVRPU19QQUdPPjxMT0NBTElaQURPUj4wNDYyODExNTM3NDIyPC9MT0NBTElaQURPUj48RFVJPjA0NjI4MTE1Mzc0MjI8L0RVST48RkVDSEFfUEFHTz4yMDEwMDExOTEyMDQxODwvRkVDSEFfUEFHTz48L0RBVE9TX1BBR08+PEZJUk1BPlJXd2dZMjl1YW5WdWRHOGdaR1VnWTJ4aGRtVnpJRzV2SUdWNGFYTjBaUTBLPC9GSVJNQT48L0pVU1RJRklDQU5URV9QQUdPPg==
		 2010-01-19 12:11:22,239 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago Justificante: <JUSTIFICANTE_PAGO><DATOS_PAGO><LOCALIZADOR>0462811537422</LOCALIZADOR><DUI>0462811537422</DUI><FECHA_PAGO>20100119120418</FECHA_PAGO></DATOS_PAGO><FIRMA>RWwgY29uanVudG8gZGUgY2xhdmVzIG5vIGV4aXN0ZQ0K</FIRMA></JUSTIFICANTE_PAGO>
		 2010-01-19 12:11:22,240 ERROR [es.caib.pagos.client.ComprobarPagoAction] comprobarFirma: 

		 
		 2010-01-19 12:12:49,758 DEBUG [es.caib.pagos.client.ClientePagos] comprobarPago (0462811537422)
		 2010-01-19 12:13:03,813 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago: PA#PEpVU1RJRklDQU5URV9QQUdPPjxEQVRPU19QQUdPPjxMT0NBTElaQURPUj4wNDYyODExNTM3NDIyPC9MT0NBTElaQURPUj48RFVJPjA0NjI4MTE1Mzc0MjI8L0RVST48RkVDSEFfUEFHTz4yMDEwMDExOTEyMDQxODwvRkVDSEFfUEFHTz48L0RBVE9TX1BBR08+PEZJUk1BPlJXd2dZMjl1YW5WdWRHOGdaR1VnWTJ4aGRtVnpJRzV2SUdWNGFYTjBaUTBLPC9GSVJNQT48L0pVU1RJRklDQU5URV9QQUdPPg==
		 2010-01-19 12:13:03,813 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago Justificante: <JUSTIFICANTE_PAGO><DATOS_PAGO><LOCALIZADOR>0462811537422</LOCALIZADOR><DUI>0462811537422</DUI><FECHA_PAGO>20100119120418</FECHA_PAGO></DATOS_PAGO><FIRMA>RWwgY29uanVudG8gZGUgY2xhdmVzIG5vIGV4aXN0ZQ0K</FIRMA></JUSTIFICANTE_PAGO>
		 2010-01-19 12:13:03,814 ERROR [es.caib.pagos.client.ComprobarPagoAction] comprobarFirma: 


		 2010-01-19 12:28:56,163 DEBUG [es.caib.pagos.client.ClientePagos] comprobarPago (0462811534202)
		 2010-01-19 12:29:10,415 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago: PA#PEpVU1RJRklDQU5URV9QQUdPPjxEQVRPU19QQUdPPjxMT0NBTElaQURPUj4wNDYyODExNTM0MjAyPC9MT0NBTElaQURPUj48RFVJPjA0NjI4MTE1MzQyMDI8L0RVST48RkVDSEFfUEFHTz4yMDA5MTIzMTE4MjU1NTwvRkVDSEFfUEFHTz48L0RBVE9TX1BBR08+PEZJUk1BPlJXd2dZMjl1YW5WdWRHOGdaR1VnWTJ4aGRtVnpJRzV2SUdWNGFYTjBaUTBLPC9GSVJNQT48L0pVU1RJRklDQU5URV9QQUdPPg==
		 2010-01-19 12:29:10,415 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago Justificante: <JUSTIFICANTE_PAGO><DATOS_PAGO><LOCALIZADOR>0462811534202</LOCALIZADOR><DUI>0462811534202</DUI><FECHA_PAGO>20091231182555</FECHA_PAGO></DATOS_PAGO><FIRMA>RWwgY29uanVudG8gZGUgY2xhdmVzIG5vIGV4aXN0ZQ0K</FIRMA></JUSTIFICANTE_PAGO>
		 2010-01-19 12:29:10,416 ERROR [es.caib.pagos.client.ComprobarPagoAction] comprobarFirma: 

		 
		 2010-01-19 12:32:00,701 DEBUG [es.caib.pagos.client.ClientePagos] comprobarPago (0462811534220)
		 2010-01-19 12:32:14,921 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago: PA#PEpVU1RJRklDQU5URV9QQUdPPjxEQVRPU19QQUdPPjxMT0NBTElaQURPUj4wNDYyODExNTM0MjIwPC9MT0NBTElaQURPUj48RFVJPjA0NjI4MTE1MzQyMjA8L0RVST48RkVDSEFfUEFHTz4yMDEwMDEwMTEzNDAyNzwvRkVDSEFfUEFHTz48L0RBVE9TX1BBR08+PEZJUk1BPlJXd2dZMjl1YW5WdWRHOGdaR1VnWTJ4aGRtVnpJRzV2SUdWNGFYTjBaUTBLPC9GSVJNQT48L0pVU1RJRklDQU5URV9QQUdPPg==
		 2010-01-19 12:32:14,921 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago Justificante: <JUSTIFICANTE_PAGO><DATOS_PAGO><LOCALIZADOR>0462811534220</LOCALIZADOR><DUI>0462811534220</DUI><FECHA_PAGO>20100101134027</FECHA_PAGO></DATOS_PAGO><FIRMA>RWwgY29uanVudG8gZGUgY2xhdmVzIG5vIGV4aXN0ZQ0K</FIRMA></JUSTIFICANTE_PAGO>
		 2010-01-19 12:32:14,922 ERROR [es.caib.pagos.client.ComprobarPagoAction] comprobarFirma: 


		 2010-01-19 12:34:29,543 DEBUG [es.caib.pagos.client.ClientePagos] comprobarPago (0462811534211)
		 2010-01-19 12:34:43,588 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago: PA#PEpVU1RJRklDQU5URV9QQUdPPjxEQVRPU19QQUdPPjxMT0NBTElaQURPUj4wNDYyODExNTM0MjExPC9MT0NBTElaQURPUj48RFVJPjA0NjI4MTE1MzQyMTE8L0RVST48RkVDSEFfUEFHTz4yMDEwMDEwMTEyMzAzNTwvRkVDSEFfUEFHTz48L0RBVE9TX1BBR08+PEZJUk1BPlJXd2dZMjl1YW5WdWRHOGdaR1VnWTJ4aGRtVnpJRzV2SUdWNGFYTjBaUTBLPC9GSVJNQT48L0pVU1RJRklDQU5URV9QQUdPPg==
		 2010-01-19 12:34:43,588 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago Justificante: <JUSTIFICANTE_PAGO><DATOS_PAGO><LOCALIZADOR>0462811534211</LOCALIZADOR><DUI>0462811534211</DUI><FECHA_PAGO>20100101123035</FECHA_PAGO></DATOS_PAGO><FIRMA>RWwgY29uanVudG8gZGUgY2xhdmVzIG5vIGV4aXN0ZQ0K</FIRMA></JUSTIFICANTE_PAGO>
		 2010-01-19 12:34:43,590 ERROR [es.caib.pagos.client.ComprobarPagoAction] comprobarFirma: 

		 2010-01-19 13:57:48,591 DEBUG [es.caib.pagos.client.ClientePagos] comprobarPago (0462811537054)
		 2010-01-19 13:58:03,125 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago: PA#PEpVU1RJRklDQU5URV9QQUdPPjxEQVRPU19QQUdPPjxMT0NBTElaQURPUj4wNDYyODExNTM3MDU0PC9MT0NBTElaQURPUj48RFVJPjA0NjI4MTE1MzcwNTQ8L0RVST48RkVDSEFfUEFHTz4yMDEwMDExODE2MzM0ODwvRkVDSEFfUEFHTz48L0RBVE9TX1BBR08+PEZJUk1BPlJXd2dZMjl1YW5WdWRHOGdaR1VnWTJ4aGRtVnpJRzV2SUdWNGFYTjBaUTBLPC9GSVJNQT48L0pVU1RJRklDQU5URV9QQUdPPg==
		 2010-01-19 13:58:03,126 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago Justificante: <JUSTIFICANTE_PAGO><DATOS_PAGO><LOCALIZADOR>0462811537054</LOCALIZADOR><DUI>0462811537054</DUI><FECHA_PAGO>20100118163348</FECHA_PAGO></DATOS_PAGO><FIRMA>RWwgY29uanVudG8gZGUgY2xhdmVzIG5vIGV4aXN0ZQ0K</FIRMA></JUSTIFICANTE_PAGO>
		 2010-01-19 13:58:03,127 ERROR [es.caib.pagos.client.ComprobarPagoAction] comprobarFirma: 

		 
		 2010-01-19 13:58:17,336 DEBUG [es.caib.pagos.client.ClientePagos] comprobarPago (0462811537054)
		 2010-01-19 13:58:31,507 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago: PA#PEpVU1RJRklDQU5URV9QQUdPPjxEQVRPU19QQUdPPjxMT0NBTElaQURPUj4wNDYyODExNTM3MDU0PC9MT0NBTElaQURPUj48RFVJPjA0NjI4MTE1MzcwNTQ8L0RVST48RkVDSEFfUEFHTz4yMDEwMDExODE2MzM0ODwvRkVDSEFfUEFHTz48L0RBVE9TX1BBR08+PEZJUk1BPlJXd2dZMjl1YW5WdWRHOGdaR1VnWTJ4aGRtVnpJRzV2SUdWNGFYTjBaUTBLPC9GSVJNQT48L0pVU1RJRklDQU5URV9QQUdPPg==
		 2010-01-19 13:58:31,507 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago Justificante: <JUSTIFICANTE_PAGO><DATOS_PAGO><LOCALIZADOR>0462811537054</LOCALIZADOR><DUI>0462811537054</DUI><FECHA_PAGO>20100118163348</FECHA_PAGO></DATOS_PAGO><FIRMA>RWwgY29uanVudG8gZGUgY2xhdmVzIG5vIGV4aXN0ZQ0K</FIRMA></JUSTIFICANTE_PAGO>
		 2010-01-19 13:58:31,509 ERROR [es.caib.pagos.client.ComprobarPagoAction] comprobarFirma: 
 
		  
		 2010-01-19 14:30:01,032 DEBUG [es.caib.pagos.client.ClientePagos] comprobarPago (0462811537045)
		 2010-01-19 14:30:19,160 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago: PA#PEpVU1RJRklDQU5URV9QQUdPPjxEQVRPU19QQUdPPjxMT0NBTElaQURPUj4wNDYyODExNTM3MDQ1PC9MT0NBTElaQURPUj48RFVJPjA0NjI4MTE1MzcwNDU8L0RVST48RkVDSEFfUEFHTz4yMDEwMDExODE2MDUxNDwvRkVDSEFfUEFHTz48L0RBVE9TX1BBR08+PEZJUk1BPlJXd2dZMjl1YW5WdWRHOGdaR1VnWTJ4aGRtVnpJRzV2SUdWNGFYTjBaUTBLPC9GSVJNQT48L0pVU1RJRklDQU5URV9QQUdPPg==
		 2010-01-19 14:30:19,160 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago Justificante: <JUSTIFICANTE_PAGO><DATOS_PAGO><LOCALIZADOR>0462811537045</LOCALIZADOR><DUI>0462811537045</DUI><FECHA_PAGO>20100118160514</FECHA_PAGO></DATOS_PAGO><FIRMA>RWwgY29uanVudG8gZGUgY2xhdmVzIG5vIGV4aXN0ZQ0K</FIRMA></JUSTIFICANTE_PAGO>
 		 2010-01-19 14:30:19,162 ERROR [es.caib.pagos.client.ComprobarPagoAction] comprobarFirma: 

		 
		 2010-01-19 14:47:28,044 DEBUG [es.caib.pagos.client.ClientePagos] comprobarPago (0462811537045)
		 2010-01-19 14:47:30,007 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago: PA#PEpVU1RJRklDQU5URV9QQUdPPjxEQVRPU19QQUdPPjxMT0NBTElaQURPUj4wNDYyODExNTM3MDQ1PC9MT0NBTElaQURPUj48RFVJPjA0NjI4MTE1MzcwNDU8L0RVST48RkVDSEFfUEFHTz4yMDEwMDExODE2MDUxNDwvRkVDSEFfUEFHTz48L0RBVE9TX1BBR08+PEZJUk1BPkFBPT08L0ZJUk1BPjwvSlVTVElGSUNBTlRFX1BBR08+
		 2010-01-19 14:47:30,007 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago Justificante: <JUSTIFICANTE_PAGO><DATOS_PAGO><LOCALIZADOR>0462811537045</LOCALIZADOR><DUI>0462811537045</DUI><FECHA_PAGO>20100118160514</FECHA_PAGO></DATOS_PAGO><FIRMA>AA==</FIRMA></JUSTIFICANTE_PAGO>
		 2010-01-19 14:47:30,009 ERROR [es.caib.pagos.client.ComprobarPagoAction] comprobarFirma: 
 
 
 		 2010-01-19 14:48:05,021 DEBUG [es.caib.pagos.client.ClientePagos] comprobarPago (0462811537045)
		 2010-01-19 14:48:05,798 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago: PA#PEpVU1RJRklDQU5URV9QQUdPPjxEQVRPU19QQUdPPjxMT0NBTElaQURPUj4wNDYyODExNTM3MDQ1PC9MT0NBTElaQURPUj48RFVJPjA0NjI4MTE1MzcwNDU8L0RVST48RkVDSEFfUEFHTz4yMDEwMDExODE2MDUxNDwvRkVDSEFfUEFHTz48L0RBVE9TX1BBR08+PEZJUk1BPkFBPT08L0ZJUk1BPjwvSlVTVElGSUNBTlRFX1BBR08+
		 2010-01-19 14:48:05,798 DEBUG [es.caib.pagos.client.ComprobarPagoAction] Resultado ComprobarPago Justificante: <JUSTIFICANTE_PAGO><DATOS_PAGO><LOCALIZADOR>0462811537045</LOCALIZADOR><DUI>0462811537045</DUI><FECHA_PAGO>20100118160514</FECHA_PAGO></DATOS_PAGO><FIRMA>AA==</FIRMA></JUSTIFICANTE_PAGO>
		 2010-01-19 14:48:05,800 ERROR [es.caib.pagos.client.ComprobarPagoAction] comprobarFirma: 

		 */
//	}
}
