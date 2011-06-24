package es.caib.pagos.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Properties;

import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

public class ClienteService {

	protected String propertiesFile = "";
	
	protected String url = "";

	public ClienteService(String url) {
		super();
		this.propertiesFile = this.getClass().getName() + ".properties";
		this.url = url;
	}

	protected String getPropertiesFile() {
		return propertiesFile;
	}

	protected void setPropertiesFile(String propertiesFile) {
		this.propertiesFile = propertiesFile;
	}
	
	public String getParameter(String parameter)
	{
		Properties props = new Properties();
		try {
			props.load(this.getClass().getResourceAsStream(getPropertiesFile()));
			return props.getProperty(parameter);
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public String execute(String value) throws ServiceException, MalformedURLException, RemoteException
	{

		Service service = new Service();
		
		Call call = (Call) service.createCall();
		
		call.setTargetEndpointAddress(new URL(this.url + "/" + getParameter("urlEndPoint")));
        
		/*
		oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ws1");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://tempuri.org/", "idTasa"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://tempuri.org/", "ws1Result"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;
	    */
		
		org.apache.axis.description.OperationDesc oper = new org.apache.axis.description.OperationDesc();
        org.apache.axis.description.ParameterDesc param;
        
        oper.setName(getParameter("operacion.nombre"));
        //String qname = this.url;
        String qname = "http://tempuri.org/";
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName(qname, getParameter("parametro.nombre")), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setOmittable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName(qname, getParameter("resultado.nombre")));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);        
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        
        /*
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("http://tempuri.org/inicio");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://tempuri.org/", "inicio"));
         */
        call.setOperation(oper);
        call.setUseSOAPAction(true);
        call.setSOAPActionURI("http://tempuri.org/" + getParameter("soapActionURI"));
        call.setEncodingStyle(null);
        call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        call.setOperationName(new javax.xml.namespace.QName(qname, getParameter("operacion.nombre")));       

        String response = (String) call.invoke(new java.lang.Object[] {value});
		return response;

	}


}
