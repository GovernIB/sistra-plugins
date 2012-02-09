/**
 * Service_TasaLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.pagos.services.wsdl;

public class Service_TasaLocator extends org.apache.axis.client.Service implements es.caib.pagos.services.wsdl.Service_Tasa {

    public Service_TasaLocator() {
    }


    public Service_TasaLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public Service_TasaLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for Service_TasaSoap
    private java.lang.String Service_TasaSoap_address = "http://www.atib.es/servicios/service_tasa.asmx";

    public java.lang.String getService_TasaSoapAddress() {
        return Service_TasaSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String Service_TasaSoapWSDDServiceName = "Service_TasaSoap";

    public java.lang.String getService_TasaSoapWSDDServiceName() {
        return Service_TasaSoapWSDDServiceName;
    }

    public void setService_TasaSoapWSDDServiceName(java.lang.String name) {
        Service_TasaSoapWSDDServiceName = name;
    }

    public es.caib.pagos.services.wsdl.Service_TasaSoap getService_TasaSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Service_TasaSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getService_TasaSoap(endpoint);
    }

    public es.caib.pagos.services.wsdl.Service_TasaSoap getService_TasaSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.caib.pagos.services.wsdl.Service_TasaSoapStub _stub = new es.caib.pagos.services.wsdl.Service_TasaSoapStub(portAddress, this);
            _stub.setPortName(getService_TasaSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setService_TasaSoapEndpointAddress(java.lang.String address) {
        Service_TasaSoap_address = address;
    }


    // Use to get a proxy class for Service_TasaSoap12
    private java.lang.String Service_TasaSoap12_address = "http://www.atib.es/servicios/service_tasa.asmx";

    public java.lang.String getService_TasaSoap12Address() {
        return Service_TasaSoap12_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String Service_TasaSoap12WSDDServiceName = "Service_TasaSoap12";

    public java.lang.String getService_TasaSoap12WSDDServiceName() {
        return Service_TasaSoap12WSDDServiceName;
    }

    public void setService_TasaSoap12WSDDServiceName(java.lang.String name) {
        Service_TasaSoap12WSDDServiceName = name;
    }

    public es.caib.pagos.services.wsdl.Service_TasaSoap getService_TasaSoap12() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Service_TasaSoap12_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getService_TasaSoap12(endpoint);
    }

    public es.caib.pagos.services.wsdl.Service_TasaSoap getService_TasaSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            es.caib.pagos.services.wsdl.Service_TasaSoap12Stub _stub = new es.caib.pagos.services.wsdl.Service_TasaSoap12Stub(portAddress, this);
            _stub.setPortName(getService_TasaSoap12WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setService_TasaSoap12EndpointAddress(java.lang.String address) {
        Service_TasaSoap12_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     * This service has multiple ports for a given interface;
     * the proxy implementation returned may be indeterminate.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (es.caib.pagos.services.wsdl.Service_TasaSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                es.caib.pagos.services.wsdl.Service_TasaSoapStub _stub = new es.caib.pagos.services.wsdl.Service_TasaSoapStub(new java.net.URL(Service_TasaSoap_address), this);
                _stub.setPortName(getService_TasaSoapWSDDServiceName());
                return _stub;
            }
            if (es.caib.pagos.services.wsdl.Service_TasaSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                es.caib.pagos.services.wsdl.Service_TasaSoap12Stub _stub = new es.caib.pagos.services.wsdl.Service_TasaSoap12Stub(new java.net.URL(Service_TasaSoap12_address), this);
                _stub.setPortName(getService_TasaSoap12WSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("Service_TasaSoap".equals(inputPortName)) {
            return getService_TasaSoap();
        }
        else if ("Service_TasaSoap12".equals(inputPortName)) {
            return getService_TasaSoap12();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://atib.es/", "Service_Tasa");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://atib.es/", "Service_TasaSoap"));
            ports.add(new javax.xml.namespace.QName("http://atib.es/", "Service_TasaSoap12"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("Service_TasaSoap".equals(portName)) {
            setService_TasaSoapEndpointAddress(address);
        }
        else 
if ("Service_TasaSoap12".equals(portName)) {
            setService_TasaSoap12EndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
