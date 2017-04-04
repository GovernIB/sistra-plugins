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
import java.awt.Color;
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
    	int TamFuente = 10;
    	int TamFuente2 = 7;
    	int TamFuente3 = 8;
    	
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
		byte[] administracion = null;
		int numIS = 0;
		//ByteArrayInputStream [] iss = new ByteArrayInputStream[3 * numCopias];
		ByteArrayInputStream [] iss = new ByteArrayInputStream[numCopias];
		
		
		while(numeros.hasMoreElements() && referencias.hasMoreElements() && identificadores.hasMoreElements()){
			
			// Para cada copia generamos 3 copias: administracion, empresa e interesado y
			// stampamos el num secuencial
			ObjectStamp textos [] = new ObjectStamp[3];
			textos[0] = new TextoStamp();
			textos[0].setPage(0);
			textos[0].setRotation(0);		
			textos[1] = new TextoStamp();
			textos[1].setPage(1);
			textos[2] = new TextoStamp();
			textos[2].setPage(1);
			/*textos[3] = new TextoStamp();
			textos[3].setPage(1);
			textos[4] = new TextoStamp();
			textos[4].setPage(1);
			textos[4].setRotation(0);
			textos[5] = new TextoStamp();
			textos[5].setPage(1);
			textos[5].setRotation(0);
			textos[6] = new TextoStamp();
			textos[6].setPage(1);
			textos[6].setRotation(0);*/
			
			
			if ("OP0004REDI".equals(idtramite))
			{
				/*textos[0].setX(13);
				textos[0].setY(530);
				textos[4].setX(17);
				textos[4].setY(520);
				textos[5].setX(13);
				textos[5].setY(510);
				textos[6].setX(13);
				textos[6].setY(500);*/
				textos[0].setX(410);
				textos[0].setY(763);
				textos[1].setX(225);
				textos[1].setY(25);
				textos[2].setX(330);
				textos[2].setY(25);
				
			}
			else
			{
				/*textos[0].setX(13);
				textos[0].setY(545);
				textos[4].setX(17);
				textos[4].setY(535);
				textos[5].setX(13);
				textos[5].setY(525);
				textos[6].setX(13);
				textos[6].setY(515);*/
				textos[0].setX(470);
				textos[0].setY(745);
				textos[1].setX(225);
				textos[1].setY(27);
				textos[2].setX(330);
				textos[2].setY(27);
			}
			
			textos[0].setRotation(0);
			textos[1].setRotation(0);
			textos[2].setRotation(0);
			((TextoStamp) textos[0]).setTexto((String) numeros.nextElement());
			((TextoStamp) textos[1]).setTexto((String) referencias.nextElement());
			((TextoStamp) textos[2]).setTexto((String) identificadores.nextElement());
			
			// Ejemplar administracion
			/*((TextoStamp) textos[0]).setFontColor(Color.RED);
			((TextoStamp) textos[0]).setFontSize(TamFuente);
			((TextoStamp) textos[0]).setTexto("Exemplar per a l'IBAVI");
			((TextoStamp) textos[4]).setFontColor(Color.RED);
			((TextoStamp) textos[4]).setFontSize(TamFuente);
			((TextoStamp) textos[4]).setFontName("Helvetica-Oblique");
			((TextoStamp) textos[4]).setTexto("Ejemplar para IBAVI");*/
			bis = new ByteArrayInputStream(documentoF.getDatosFichero());
			bos = new ByteArrayOutputStream(sizeBuffer);
			UtilPDF.stamp(bos,bis,textos);
			administracion = bos.toByteArray();
			
			// Ejemplar empresa
			/*textos[4].setX(13);
			((TextoStamp) textos[0]).setFontSize(TamFuente2);
			((TextoStamp) textos[0]).setTexto("Exemplar per a l'ARRENDADOR");
			((TextoStamp) textos[4]).setFontSize(TamFuente2);
			((TextoStamp) textos[4]).setTexto("Ejemplar para el ARRENDADOR");
			bis = new ByteArrayInputStream(documentoF.getDatosFichero());
			bos = new ByteArrayOutputStream(sizeBuffer);
			UtilPDF.stamp(bos,bis,textos);
			empresa = bos.toByteArray();*/
			
			// Ejemplar interesado
			/*((TextoStamp) textos[0]).setFontSize(TamFuente3);
			((TextoStamp) textos[0]).setTexto("Exemplar per a l'ENTITAT");
			((TextoStamp) textos[4]).setFontName("Helvetica");
			((TextoStamp) textos[4]).setFontSize(TamFuente3);
			((TextoStamp) textos[4]).setTexto("COL·LABORADORA");
			((TextoStamp) textos[5]).setFontColor(Color.RED);
			((TextoStamp) textos[5]).setFontName("Helvetica-Oblique");
			((TextoStamp) textos[5]).setFontSize(TamFuente3);
			((TextoStamp) textos[5]).setTexto("Ejemplar para la ENTIDAD");
			((TextoStamp) textos[6]).setFontName("Helvetica-Oblique");
			((TextoStamp) textos[6]).setFontColor(Color.RED);
			((TextoStamp) textos[6]).setFontSize(TamFuente3);
			((TextoStamp) textos[6]).setTexto("COLABORADORA");
			bis = new ByteArrayInputStream(documentoF.getDatosFichero());
			bos = new ByteArrayOutputStream(sizeBuffer);
			UtilPDF.stamp(bos,bis,textos);
			interesado = bos.toByteArray();*/
			
			// Creamos input streams
			iss[numIS] = new ByteArrayInputStream(administracion);
			numIS++;
			/*iss[numIS] = new ByteArrayInputStream(empresa);
			numIS++;
			iss[numIS] = new ByteArrayInputStream(interesado);
			numIS++;*/
			
		}
		
		// Concatenamos todos los pdfs en uno
		//ByteArrayOutputStream pdfFinal = new ByteArrayOutputStream( 8192 + (plantilla.getArchivo().getDatos().length * numCopias * 3));
		if (numCopias > 1)
		{
			ByteArrayOutputStream pdfFinal = new ByteArrayOutputStream( 8192 + (plantilla.getArchivo().getDatos().length * numCopias));
			UtilPDF.concatenarPdf(pdfFinal,iss);
			// Devolvemos documento
			documentoF.setDatosFichero(pdfFinal.toByteArray());
		}
		else{
			documentoF.setDatosFichero(administracion);
		}
		
		return documentoF;
	}

		
}
