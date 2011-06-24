package es.caib.pagos.services;

import es.caib.pagos.services.wsdl.ServiceLocator;
import es.caib.pagos.services.wsdl.ServiceSoap;




public class InicioPagoService { //extends ClienteService{

	
	private String url;
	
	public InicioPagoService(String url) {
		this.url = url;
	}
	
	
	public String execute(String value) throws Exception{
		ServiceLocator sl = new ServiceLocator();
		sl.setEndpointAddress("ServiceSoap",url);
		ServiceSoap port = sl.getServiceSoap();
		String res = port.inicio(value);
		return res;
		
	}


	
}
