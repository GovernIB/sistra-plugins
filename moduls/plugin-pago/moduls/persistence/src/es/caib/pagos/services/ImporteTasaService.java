package es.caib.pagos.services;

import es.caib.pagos.services.wsdl.ServiceLocator;
import es.caib.pagos.services.wsdl.ServiceSoap;




public class ImporteTasaService { //extends ClienteService{

	private String url;
	
	public ImporteTasaService(String url) {
		this.url = url;
		//super(url);
		//this.setPropertiesFile("ImporteTasaService.properties");
	}
	
	
	public String execute(String value) throws Exception{
		ServiceLocator sl = new ServiceLocator();
		sl.setEndpointAddress("ServiceSoap",url);
		ServiceSoap port = sl.getServiceSoap();
		String res = port.ws1(value);
		return res;
		
	}
	


}
