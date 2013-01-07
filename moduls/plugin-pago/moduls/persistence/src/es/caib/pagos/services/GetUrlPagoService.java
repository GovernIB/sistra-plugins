package es.caib.pagos.services;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import es.caib.pagos.services.wsdl.DatosRespuestaGetUrlPago;
import es.caib.pagos.services.wsdl.Service_TasaLocator;
import es.caib.pagos.services.wsdl.Service_TasaSoap;
import es.caib.pagos.services.wsdl.Service_TasaSoapStub;
import es.caib.pagos.services.wsdl.UsuariosWebServices;
import es.caib.pagos.util.Constants;
import es.caib.pagos.util.UtilWs;


public class GetUrlPagoService { 
	
	private final String url;
	
	public GetUrlPagoService(final String url) {
		this.url = url;
	}

	public DatosRespuestaGetUrlPago execute(final String[] refsModelos, final String codigoEntidad, final String urlVuelta, final UsuariosWebServices usuario) throws ServiceException, RemoteException{
		final Service_TasaLocator sl = new Service_TasaLocator();
		sl.setEndpointAddress(Constants.SERVICE_SOAP, url);
		final Service_TasaSoap port = sl.getService_TasaSoap();
		((Service_TasaSoapStub)port).setHeader(Constants.NAMESPACE_ATIB, Constants.PARTNAME_USUARIOS_WEBSERVICES, usuario);
		
		UtilWs.habilitarLog(sl);
		
		return port.getUrlPago(refsModelos, codigoEntidad, urlVuelta);
	}
	
}
