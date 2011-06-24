package es.caib.sistra.plugins.custodia.impl.caib;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;


/**
 * Parser para interpretar la operacion de ReservarDocumento y ConsultarReservaDocumento
 * @author rsanz
 *
 */
public class ReservarDocumentoResponseParser {
	/**
	 * Parser del resultat de cust�dia.
	 * 
	 * public Logger log = Logger
	 * .getLogger(ReservaDocumentoResponseParser.class);
	 */

	private byte xml[] = null;

	private String resultado = null;

	private String exception = null;

	private String stackTrace = null;

	private String codigoExterno = null;
	
	private String hash = null;

	public final static String SUCCESS = "Success";

	public ReservarDocumentoResponseParser(byte[] xml) throws Exception {
		this.xml = xml;
		parse(xml);
	}

	private void parse(byte[] xml2) throws Exception {
		
		/*
				 <con:ReservaResponse xmlns:con="http://www.caib.es.signatura.custodia">
					<dss:Result xmlns:dss="urn:oasis:names:tc:dss:1.0:core:schema">
						<dss:ResultMajor>Succes</dss:ResultMajor>
						<dss:ResultMinor/>
						<dss:ResultMessage xml:lang="es"/>
					</dss:Result>
					<con:Hash>12585514719411026650-uploadDocument7152882148318620708</con:Hash>
					<con:Codigo>1026650-uploadDocument</con:Codigo>
				</con:ReservaResponse>
		 */

		Document xml = DocumentHelper.parseText(new String(xml2,"UTF-8"));
		Element docXML = xml.getRootElement();
		docXML.add(new Namespace("con", "http://www.caib.es.signatura.custodia")); 
		docXML.add(new Namespace("dss", "urn:oasis:names:tc:dss:1.0:core:schema"));
		Node value;
		
		// Resultado
		value = docXML.selectSingleNode("/con:ReservaResponse/dss:Result/dss:ResultMajor");	
		if (value == null || value.getText() == null) {
			throw new Exception("Error en la respuesta de custodia, no se indica el resultado de la operacion:" + xml);
		}
		setResultado(value.getText());
		
		// Excepcion
		value = docXML.selectSingleNode("/con:ReservaResponse/dss:Result/dss:ResultMinor");
		if (value == null || value.getText() == null) {
			setException(null);
		}else{
			setException(value.getText());
		}
		
		// StackTrace
		value = docXML.selectSingleNode("/con:ReservaResponse/dss:Result/dss:ResultMessage");
		if (value == null || value.getText() == null) {
			setStackTrace(null);
		}else{
			setStackTrace(value.getText());
		}	
		
		//	Codigo externo
		value = docXML.selectSingleNode("/con:ReservaResponse/con:Codigo");
		if (value == null || value.getText() == null) {
			setCodigoExterno(null);
		}else{
			setCodigoExterno(value.getText());
		}
		
		// Hash
		value = docXML.selectSingleNode("/con:ReservaResponse/con:Hash");
		if (value == null || value.getText() == null) {
			setHash(null);
		}else{
			setHash(value.getText());
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

	/**
	 * @return the codigoExterno
	 */
	public  String getCodigoExterno() {
		return codigoExterno;
	}

	/**
	 * @param codigoExterno
	 *            the codigoExterno to set
	 */
	private  void setCodigoExterno(String codigoExterno) {
		this.codigoExterno = codigoExterno;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

}
