package es.caib.pagos.services;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import es.caib.pagos.services.wsdl.Service_TasaLocator;
import es.caib.pagos.services.wsdl.Service_TasaSoap;
import es.caib.pagos.services.wsdl.Service_TasaSoapStub;
import es.caib.pagos.services.wsdl.UsuariosWebServices;
import es.caib.pagos.util.Constants;


public class GetPdf046Service { 
	
	private String url;
	
	public GetPdf046Service(String url) {
		this.url = url;
	}

	public byte[] execute(String localizador, String importeaingresar,
			String nifsujetopasivo, String fechacreacion, UsuariosWebServices usuario) throws ServiceException, RemoteException{
		Service_TasaLocator sl = new Service_TasaLocator();
		sl.setEndpointAddress(Constants.SERVICE_SOAP, url);
		Service_TasaSoap port = sl.getService_TasaSoap();
		((Service_TasaSoapStub)port).setHeader(Constants.NAMESPACE_ATIB, Constants.PARTNAME_USUARIOS_WEBSERVICES, usuario);
		byte[] res = port.getPdf046(localizador, importeaingresar, nifsujetopasivo, fechacreacion);
		return res;
	}


	
}
