package es.caib.regtel.plugincaib.ws;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.util.ws.client.WsClientSistraUtil;

/**
 * Cliente de WS para interfaz version 1 de backoffices para Sistra
 * 
 */
public class ClienteWS {
	
	private static Log log = LogFactory.getLog(ClienteWS.class);
	private static final QName SERVICE_NAME = new QName("urn:es:caib:regweb:ws:v1:services", "RegwebFacadeService");
	private static final QName PORT_NAME = new QName("urn:es:caib:regweb:ws:v1:services", "RegwebFacade");
	
	
	public static es.caib.regtel.plugincaib.ws.services.RegwebFacade generarPort(String url,String user,String pass) throws Exception{
		javax.xml.ws.Service service =javax.xml.ws.Service.create(SERVICE_NAME); 
		service.addPort(PORT_NAME,javax.xml.ws.soap.SOAPBinding.SOAP11HTTP_BINDING, url);
		es.caib.regtel.plugincaib.ws.services.RegwebFacade port = service.getPort(PORT_NAME,es.caib.regtel.plugincaib.ws.services.RegwebFacade.class);
          
		// Configura puerto para log
		WsClientSistraUtil.configurePort((BindingProvider)port,null,url,user,pass);
		
        return port;
	}	
	
}
