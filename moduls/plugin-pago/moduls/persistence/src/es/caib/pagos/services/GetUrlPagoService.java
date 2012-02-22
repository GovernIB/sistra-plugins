package es.caib.pagos.services;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import es.caib.pagos.services.wsdl.DatosRespuestaGetUrlPago;
import es.caib.pagos.services.wsdl.Service_TasaLocator;
import es.caib.pagos.services.wsdl.Service_TasaSoap;
import es.caib.pagos.services.wsdl.Service_TasaSoapStub;
import es.caib.pagos.services.wsdl.UsuariosWebServices;
import es.caib.pagos.util.Constants;


public class GetUrlPagoService { 
	
	private String url;
	
	public GetUrlPagoService(String url) {
		this.url = url;
	}

	public DatosRespuestaGetUrlPago execute(String[] refsModelos, String codigoEntidad, UsuariosWebServices usuario) throws ServiceException, RemoteException{
		Service_TasaLocator sl = new Service_TasaLocator();
		sl.setEndpointAddress(Constants.SERVICE_SOAP, url);
		Service_TasaSoap port = sl.getService_TasaSoap();
		((Service_TasaSoapStub)port).setHeader(Constants.NAMESPACE_ATIB, Constants.PARTNAME_USUARIOS_WEBSERVICES, usuario);
		DatosRespuestaGetUrlPago res = port.getUrlPago(refsModelos, codigoEntidad);
		return res;
	}
	
}
