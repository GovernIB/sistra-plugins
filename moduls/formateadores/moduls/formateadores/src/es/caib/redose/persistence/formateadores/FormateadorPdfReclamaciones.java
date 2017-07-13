package es.caib.redose.persistence.formateadores;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

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
public class FormateadorPdfReclamaciones extends FormateadorPdfFormularios{
	
	protected final static String XPATH_ANYO = "/RECLAMACION/NUMERO_RECLAMACION/ANYO";
	protected final static String XPATH_NUMCOPIAS = "/RECLAMACION/NUMERO_RECLAMACION/NUMERO_COPIAS";
	protected final static String XPATH_NUMSECUENCIAINICIO = "/RECLAMACION/NUMERO_RECLAMACION/INICIO_SECUENCIA";
	
	private final static String PIE_EMPRESA = "Exemplar per a l'empresa reclamada o denunciada / Ejemplar para la empresa reclamada o denunciada";
	
	private final static String PIE_ADMINISTRACION = "Exemplar per a l'administraci\u00F3 / Ejemplar para la administraci\u00F3n";
	
	private final static String PIE_INTERESADO = "Exemplar per a la persona reclamant o denunciant / Ejemplar para la persona reclamante o denunciante";
	
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
    	Nodo nodo = (Nodo) datosFormulario.get(XPATH_NUMCOPIAS);
    	int numCopias = Integer.parseInt(nodo.getValor());
    	nodo = (Nodo) datosFormulario.get(XPATH_NUMSECUENCIAINICIO);
    	long numSecuenciaInicio = Long.parseLong(nodo.getValor());
    	nodo = (Nodo) datosFormulario.get(XPATH_ANYO);
    	String anyo = nodo.getValor();
    	
    	// - Realizamos formateo a partir de la plantilla
		DocumentoRDS documentoF =  super.formatearDocumento( documento,plantilla,usos);
		
		// - Generamos n pdfs (cada copia -> 3 pdfs)
		int sizeBuffer = plantilla.getArchivo().getDatos().length + 1024;
		ByteArrayOutputStream bos;
		byte[] administracion,interesado,empresa;
		int numIS = 0;
		InputStream [] iss = new ByteArrayInputStream[3 * numCopias];
		
		for (int i=0;i<numCopias;i++){
			
			// Para cada copia generamos 3 copias: administracion, empresa e interesado y
			// stampamos el num secuencial
			String secuencia = anyo + "/" + Long.toString(numSecuenciaInicio + i);
			
			administracion = generarPdfDestinarario(documentoF.getDatosFichero(), plantilla.getIdioma(), PIE_ADMINISTRACION, secuencia);
			empresa = generarPdfDestinarario(documentoF.getDatosFichero(), plantilla.getIdioma(), PIE_EMPRESA, secuencia);
			interesado = generarPdfDestinarario(documentoF.getDatosFichero(), plantilla.getIdioma(), PIE_INTERESADO, secuencia);
			
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
	
	private byte[] generarPdfDestinarario(byte[] docPdf, String idioma, String texto, String secuencia)
			throws Exception {
		ByteArrayInputStream bis = new ByteArrayInputStream(docPdf);
		ByteArrayOutputStream bos = new ByteArrayOutputStream(docPdf.length + 2048);
		ObjectStamp[] stamps = new ObjectStamp[2];
		TextoStamp textoStamp = new TextoStamp();
		textoStamp.setTexto(texto);
		textoStamp.setX(40);
		textoStamp.setY(20);
		textoStamp.setRotation(0);
		stamps[0] = textoStamp;
		
		TextoStamp textoStamp2 = new TextoStamp();
		textoStamp2.setPage(1);
		textoStamp2.setX(460);
		textoStamp2.setY(756);
		textoStamp2.setRotation(0);
		textoStamp2.setTexto(secuencia);
		stamps[1] = textoStamp2;
		
		UtilPDF.stamp(bos, bis, stamps );
		
		byte[] res = bos.toByteArray();
		return res;
	}

		
}