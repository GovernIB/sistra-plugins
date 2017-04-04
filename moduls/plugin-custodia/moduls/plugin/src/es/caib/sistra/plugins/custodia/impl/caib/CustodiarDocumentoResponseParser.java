package es.caib.sistra.plugins.custodia.impl.caib;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;


/**
 * Parser para resultado custodiar documento
 * @author rsanz
 *
 */
public class CustodiarDocumentoResponseParser {
	/**
	 * Parser del resultat de custòdia.
	 * 
	 * public Logger log = Logger
	 * .getLogger(CustodiarDocumentoResponseParser.class);
	 */

	private byte xml[] = null;

	private String resultado = null;

	private String exception = null;

	private String stackTrace = null;

	public final static String SUCCESS = "Success";

	public CustodiarDocumentoResponseParser(byte[] xml) throws Exception {
		this.xml = xml;
		parse(xml);
	}

	private void parse(byte[] xml2) throws Exception {
		
		/*
				 <cus:CustodiaResponse xmlns:cus="http://www.caib.es.signatura.custodia">
					<dss:VerifyResponse xmlns:dss="urn:oasis:names:tc:dss:1.0:core:schema" Profile="urn:oasis:names:tc:dss:1.0:profiles:CAdES">
						<dss:Result xmlns:dss="urn:oasis:names:tc:dss:1.0:core:schema">
							<dss:ResultMajor>Success</dss:ResultMajor>
							<dss:ResultMinor>Archive</dss:ResultMinor>
							<dss:ResultMessage xml:lang="es"/>
						</dss:Result>
						<dss:OptionalOutputs>
							...
						</dss:OptionalOutputs>
					</dss:VerifyResponse>
				</cus:CustodiaResponse>
		 */
		
		Document xml = DocumentHelper.parseText(new String(xml2,"UTF-8"));
		Element docXML = xml.getRootElement();
		docXML.add(new Namespace("cus", "http://www.caib.es.signatura.custodia")); 
		docXML.add(new Namespace("dss", "urn:oasis:names:tc:dss:1.0:core:schema"));
		Node value;
		
		// Resultado
		value = docXML.selectSingleNode("/cus:CustodiaResponse/dss:VerifyResponse/dss:Result/dss:ResultMajor");
		if (value == null || value.getText() == null) {
			throw new Exception("Error en la respuesta de custodia, no se indica el resultado de la operacion:" + xml);
		}
		setResultado(value.getText());
		
		// Excepcion
		value = docXML.selectSingleNode("/cus:CustodiaResponse/dss:VerifyResponse/dss:Result/dss:ResultMinor");
		if (value == null || value.getText() == null) {
			setException(null);
		}else{
			setException(value.getText());
		}
		
		// StackTrace
		value = docXML.selectSingleNode("/cus:CustodiaResponse/dss:VerifyResponse/dss:Result/dss:ResultMessage");
		if (value == null || value.getText() == null) {
			setStackTrace(null);
		}else{
			setStackTrace(value.getText());
		}		
		
	}

	/**
	 * @return the xml
	 */
	public  byte[] getXml() {
		return xml;
	}

	/**
	 * @param xml
	 *            the xml to set
	 */
	public  void setXml(byte[] xml) {
		this.xml = xml;
	}

	/**
	 * @return the resultado
	 */
	public  String getResultado() {
		return resultado;
	}

	/**
	 * @param resultado
	 *            the resultado to set
	 */
	public  void setResultado(String resultado) {
		this.resultado = resultado;
	}

	/**
	 * @return the exception
	 */
	public  String getException() {
		return exception;
	}

	/**
	 * @param exception
	 *            the exception to set
	 */
	public  void setException(String exception) {
		this.exception = exception;
	}

	/**
	 * @return the stackTrace
	 */
	public  String getStackTrace() {
		return stackTrace;
	}

	/**
	 * @param stackTrace
	 *            the stackTrace to set
	 */
	public  void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}		

}
