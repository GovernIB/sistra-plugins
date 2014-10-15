
/*
 * 
 */

package es.caib.regtel.plugincaib.ws.services;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.2.7
 * Wed Sep 24 15:21:20 CEST 2014
 * Generated source version: 2.2.7
 * 
 */


@WebServiceClient(name = "RegwebFacadeService", 
                  wsdlLocation = "RegwebFacade.wsdl",
                  targetNamespace = "urn:es:caib:regweb:ws:v1:services") 
public class RegwebFacadeService extends Service {

    public final static URL WSDL_LOCATION;
    public final static QName SERVICE = new QName("urn:es:caib:regweb:ws:v1:services", "RegwebFacadeService");
    public final static QName RegwebFacade = new QName("urn:es:caib:regweb:ws:v1:services", "RegwebFacade");
    static {
        URL url = null;
        try {
            url = new URL("RegwebFacade.wsdl");
        } catch (MalformedURLException e) {
            System.err.println("Can not initialize the default wsdl from RegwebFacade.wsdl");
            // e.printStackTrace();
        }
        WSDL_LOCATION = url;
    }

    public RegwebFacadeService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public RegwebFacadeService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public RegwebFacadeService() {
        super(WSDL_LOCATION, SERVICE);
    }

    /**
     * 
     * @return
     *     returns RegwebFacade
     */
    @WebEndpoint(name = "RegwebFacade")
    public RegwebFacade getRegwebFacade() {
        return super.getPort(RegwebFacade, RegwebFacade.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns RegwebFacade
     */
    @WebEndpoint(name = "RegwebFacade")
    public RegwebFacade getRegwebFacade(WebServiceFeature... features) {
        return super.getPort(RegwebFacade, RegwebFacade.class, features);
    }

}
