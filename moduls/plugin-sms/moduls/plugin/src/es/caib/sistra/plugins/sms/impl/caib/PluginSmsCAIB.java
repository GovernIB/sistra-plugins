package es.caib.sistra.plugins.sms.impl.caib;

import ie.ncl.vivato.client.soap.VivatoSoapCAIBClient;
import ie.ncl.vivato.message.VivatoMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.sistra.plugins.sms.ConstantesSMS;
import es.caib.sistra.plugins.sms.EstadoEnvio;
import es.caib.sistra.plugins.sms.PluginSmsIntf;

/**
 * 	
 * 	Plugin para envio de sms a traves de provato
 *
 */
public class PluginSmsCAIB implements PluginSmsIntf{
	
	private Log log = LogFactory.getLog( PluginSmsCAIB.class);

	public void enviarSMS(String idEnvio, String cuentaSMS,String telefono,String mensaje,boolean inmediato) throws Exception{
		VivatoSoapCAIBClient vc = new VivatoSoapCAIBClient();
		
		vc.setApplicationName(cuentaSMS);
		vc.setServerUrl(Configuracion.getInstance().getProperty("sms.url"));
		vc.setUsername(Configuracion.getInstance().getProperty("sms.username"));
		vc.setPassword(Configuracion.getInstance().getProperty("sms.password"));
		
		vc.connect();
		
		VivatoMessage msg = new VivatoMessage();
		
		// Si es inmediato aplicamos prioridad al mensaje
		if (inmediato){
			log.debug("SMS inmediato: prioridad 9");
			msg.setPriority(9);	
		}

		msg.setContentType(VivatoMessage.TEXT_MESSAGE_TYPE);
		msg.setDataCodingScheme(VivatoMessage.DCS_UNICODE);
		msg.setContent(mensaje != null ? mensaje : "");
		msg.setSource(Configuracion.getInstance().getProperty("sms.remitent"));
		log.debug("Enviamos SMS al telefono: " + telefono);
		msg.setDestination(telefono);
		
		String id = vc.sendMessage(msg);
		log.debug("Enviado SMS correctamente a provato: " + telefono);
		
		vc.close();
		
	}
	
	public EstadoEnvio consultarEstadoEnvio(String idEnvio) throws Exception{
		EstadoEnvio e = new EstadoEnvio();
		e.setEstado(ConstantesSMS.ESTADO_DESCONOCIDO);
		e.setDescripcionEstado("Operacion no soportada");
		return e;
	}

}
