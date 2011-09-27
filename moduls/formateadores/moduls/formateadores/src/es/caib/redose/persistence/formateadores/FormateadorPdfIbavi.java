package es.caib.redose.persistence.formateadores;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.redose.model.PlantillaIdioma;
import es.caib.redose.modelInterfaz.DocumentoRDS;
import es.caib.xml.ConstantesXML;
import es.caib.xml.analiza.Analizador;
import es.caib.xml.analiza.Nodo;
import es.caib.xml.util.HashMapIterable;
import es.indra.util.pdf.ObjectStamp;
import es.indra.util.pdf.TextoStamp;
import es.indra.util.pdf.UtilPDF;

/**
 * Generador de PDFs para XMLs de Reclamaciones
 *  
 */
public class FormateadorPdfIbavi extends FormateadorPdfFormularios{
	
	//protected final static String XPATH_ANYO = "/RECLAMACION/NUMERO_RECLAMACION/ANYO";
	//protected final static String XPATH_NUMCOPIAS = "/RECLAMACION/NUMERO_RECLAMACION/NUMERO_COPIAS";
	protected final static String XPATH_NUMSECUENCIAINICIO = "/RECLAMACION/NUMERO_RECLAMACION/INICIO_SECUENCIA";
	protected final static String XPATH_REFERENCIA = "/RECLAMACION/NUMERO_RECLAMACION/REFERENCIA";
	protected final static String XPATH_IDENTIFICADOR = "/RECLAMACION/NUMERO_RECLAMACION/IDENTIF";
	protected final static String XPATH_IDTRAMITE = "/RECLAMACION/TRAMITE/IDENTIFICADOR";
	protected final static String delimiter = ",";
	private static Log     log     = LogFactory.getLog( FormateadorPdfIbavi.class );
	
	/**
	 * Genera PDF a partir Xpath
	 */
	public DocumentoRDS formatearDocumento(DocumentoRDS documento, PlantillaIdioma plantilla,List usos) throws Exception {
		
		// - Extraemos datos
		Analizador analizador = new Analizador ();				
		ByteArrayInputStream bis = new ByteArrayInputStream(documento.getDatosFichero());
    	HashMapIterable datosFormulario = analizador.analizar(bis,ConstantesXML.ENCODING);
    	bis.close();
    	
    	// - Obtenemos año, numero de copias y num secuencia inicial
    	//Nodo nodo = (Nodo) datosFormulario.get(XPATH_NUMCOPIAS);
    	//int numCopias = Integer.parseInt(nodo.getValor());
    	Nodo nodo = (Nodo) datosFormulario.get(XPATH_NUMSECUENCIAINICIO);
    	String numSecuenciaInicio = nodo.getValor();
    	nodo = (Nodo) datosFormulario.get(XPATH_REFERENCIA);
    	String referencia = nodo.getValor();
    	nodo = (Nodo) datosFormulario.get(XPATH_IDENTIFICADOR);
    	String identificador = nodo.getValor();
    	//nodo = (Nodo) datosFormulario.get(XPATH_ANYO);
    	//String anyo = nodo.getValor();
    	nodo = (Nodo) datosFormulario.get(XPATH_IDTRAMITE);
    	String idtramite = nodo.getValor();
    	
    	// - Creamos objeto StringTokenizer para extraer los diferentes números a imprimir en los pdfs.
		StringTokenizer numeros = new StringTokenizer(numSecuenciaInicio,delimiter);
		StringTokenizer referencias = new StringTokenizer(referencia,delimiter);
		StringTokenizer identificadores = new StringTokenizer(identificador,delimiter);
		int numCopias = numeros.countTokens();
		log.debug( ".DocumentosRDS() Tokens = "
                + numCopias + " \n\n" );
		log.debug( ".DocumentosRDS() numSecuenciaInicio = "
                + numSecuenciaInicio + " \n\n" );
		log.debug( ".DocumentosRDS() referencia = "
                + referencia + " \n\n" );
		log.debug( ".DocumentosRDS() identificador = "
                + identificador + " \n\n" );
		
		// Eliminamos la conversión del documento en solo lectura heredada de la clase FormateadorPdfFormularios
		this.setSoloLectura(false);
		
    	// - Realizamos formateo a partir de la plantilla
		DocumentoRDS documentoF =  super.formatearDocumento( documento,plantilla,usos);
		
		// - Generamos n pdfs (cada copia -> 3 pdfs)
		int sizeBuffer = plantilla.getArchivo().getDatos().length + 1024;
		ByteArrayOutputStream bos;
		byte[] administracion,interesado,empresa;
		int numIS = 0;
		ByteArrayInputStream [] iss = new ByteArrayInputStream[3 * numCopias];
		
		
		while(numeros.hasMoreElements() && referencias.hasMoreElements() && identificadores.hasMoreElements()){
			
			// Para cada copia generamos 3 copias: administracion, empresa e interesado y
			// stampamos el num secuencial
			ObjectStamp textos [] = new ObjectStamp[4];
			textos[0] = new TextoStamp();
			textos[0].setPage(0);
			textos[0].setRotation(0);		
			textos[1] = new TextoStamp();
			textos[1].setPage(1);
			textos[2] = new TextoStamp();
			textos[2].setPage(1);
			textos[3] = new TextoStamp();
			textos[3].setPage(1);
			
			
			if ("OP0004REDI".equals(idtramite))
			{
				textos[0].setX(130);
				textos[0].setY(5);		
				textos[1].setX(410);
				textos[1].setY(763);
				textos[2].setX(225);
				textos[2].setY(25);
				textos[3].setX(330);
				textos[3].setY(25);
				
			}
			else
			{
				textos[0].setX(130);
				textos[0].setY(5);		
				textos[1].setX(470);
				textos[1].setY(745);
				textos[2].setX(225);
				textos[2].setY(27);
				textos[3].setX(330);
				textos[3].setY(27);
			}
			
			textos[1].setRotation(0);
			textos[2].setRotation(0);
			textos[3].setRotation(0);
			((TextoStamp) textos[1]).setTexto((String) numeros.nextElement());
			((TextoStamp) textos[2]).setTexto((String) referencias.nextElement());
			((TextoStamp) textos[3]).setTexto((String) identificadores.nextElement());
			
			// Ejemplar administracion
			((TextoStamp) textos[0]).setTexto("Ejemplar para IBAVI / Exemplar per a l'IBAVI");
			bis = new ByteArrayInputStream(documentoF.getDatosFichero());
			bos = new ByteArrayOutputStream(sizeBuffer);
			UtilPDF.stamp(bos,bis,textos);
			administracion = bos.toByteArray();
			
			// Ejemplar empresa
			((TextoStamp) textos[0]).setTexto("Ejemplar para el ARRENDADOR / Exemplar per a l'ARRENDADOR");
			bis = new ByteArrayInputStream(documentoF.getDatosFichero());
			bos = new ByteArrayOutputStream(sizeBuffer);
			UtilPDF.stamp(bos,bis,textos);
			empresa = bos.toByteArray();
			
			// Ejemplar interesado
			((TextoStamp) textos[0]).setTexto("Ejemplar para la ENTIDAD COLABORADORA / Exemplar per a l'ENTITAT COL·LABORADORA");
			bis = new ByteArrayInputStream(documentoF.getDatosFichero());
			bos = new ByteArrayOutputStream(sizeBuffer);
			UtilPDF.stamp(bos,bis,textos);
			interesado = bos.toByteArray();
			
			// Creamos input streams
			iss[numIS] = new ByteArrayInputStream(administracion);
			numIS++;
			iss[numIS] = new ByteArrayInputStream(empresa);
			numIS++;
			iss[numIS] = new ByteArrayInputStream(interesado);
			numIS++;
					
		}
		
		// Concatenamos todos los pdfs en uno
		ByteArrayOutputStream pdfFinal = new ByteArrayOutputStream( 8192 + (plantilla.getArchivo().getDatos().length * numCopias * 3));
		UtilPDF.concatenarPdf(pdfFinal,iss);
		
		// Devolvemos documento
		documentoF.setDatosFichero(pdfFinal.toByteArray());
		return documentoF;
	}

		
}
