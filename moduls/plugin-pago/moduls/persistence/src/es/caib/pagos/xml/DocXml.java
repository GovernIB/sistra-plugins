package es.caib.pagos.xml;

// JAXP packages


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Blob;
import java.sql.Clob;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import es.caib.pagos.client.JustificantePago;



public class DocXml
{	
	
	private static Log log = LogFactory.getLog(DocXml.class);


	private  Document xml = null;
	
	public DocXml(){

	}

	/** Método para parsear un InputStream en un Arbol DOM 
	 * @param is InputStream a parsear
	 * @return int 0 --> sin errores, otro caso  **/
	public int parse(InputStream is)
	{
		try
		{
	        // Configurar el arbol DOM a crear
	        DocumentBuilderFactory dbf =
	            DocumentBuilderFactory.newInstance();
	
	        dbf.setNamespaceAware(true);
	
	        // Configurar varias opciones de configuración
	        dbf.setIgnoringComments(false);
	        dbf.setIgnoringElementContentWhitespace(true);
	        dbf.setCoalescing(true);
	        dbf.setExpandEntityReferences(false);
	
	        // Crear un DocumentBuilder que satisfaga la condiciones de
	        // el DocumentBuilderFactory
	        DocumentBuilder db = null;
	        db = dbf.newDocumentBuilder();
	
	         // Paso 3: Parsear el fichero de entrada
	         xml = db.parse(is);
	         return 0;
        } 
        catch (Throwable ex) 
        {
			log.error(" Error al intentar convertir a documento XML una entrada de tipo InputStream. ", ex);	
            return -1;
        }

	}


	
	

  /** Método para parsear un InputStream en un Arbol DOM 
   * @param is InputStream a parsearf
   * @return int 0 --> sin errores, otro caso  **/
  public int parse(File a_archivo)
  {
	  	try
	  	{
	        // Configurar el arbol DOM a crear
	        DocumentBuilderFactory dbf =
	            DocumentBuilderFactory.newInstance();
	
	        dbf.setNamespaceAware(true);
	
	        // Configurar varias opciones de configuración
	        dbf.setIgnoringComments(true);
	        dbf.setIgnoringElementContentWhitespace(true);
	        dbf.setCoalescing(true);
	        dbf.setExpandEntityReferences(false);
	
	        // Crear un DocumentBuilder que satisfaga la condiciones de
	        // el DocumentBuilderFactory
	        DocumentBuilder db = null;
	        db = dbf.newDocumentBuilder();
	
	        // Paso 3: Parsear el fichero de entrada
         	xml = db.parse(a_archivo);
			return 0;
        } 
        catch (Throwable ex) 
        {
			log.error(" Error al intentar convertir a documento XML una entrada de tipo Archivo. ", ex);	
            return -1;
        }

  }	
	



	/**
	 * Returns the xml.
	 * @return Document
	 */
	public Document getXml() {
		return xml;
	}

	/**
	 * Sets the xml.
	 * @param xml The xml to set
	 */
	public int setXml(Document xml) {
		this.xml = xml;
		return 0;
	}
}