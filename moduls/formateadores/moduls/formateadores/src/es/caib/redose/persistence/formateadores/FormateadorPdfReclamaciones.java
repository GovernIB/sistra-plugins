package es.caib.redose.persistence.formateadores;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
		ByteArrayInputStream [] iss = new ByteArrayInputStream[3 * numCopias];
		
		for (int i=0;i<numCopias;i++){
			
			// Para cada copia generamos 3 copias: administracion, empresa e interesado y
			// stampamos el num secuencial
			ObjectStamp textos [] = new ObjectStamp[2];
			textos[0] = new TextoStamp();
			textos[0].setPage(0);
			textos[0].setX(40);
			textos[0].setY(20);		
			textos[0].setRotation(0);		
			textos[1] = new TextoStamp();
			textos[1].setPage(1);
			textos[1].setX(460);
			textos[1].setY(756);		
			textos[1].setRotation(0);
			((TextoStamp) textos[1]).setTexto(anyo + "/" + Long.toString(numSecuenciaInicio + i));
			
			
			// Ejemplar administracion
			((TextoStamp) textos[0]).setTexto("Exemplar per a l'administració / Ejemplar para la administración");
			bis = new ByteArrayInputStream(documentoF.getDatosFichero());
			bos = new ByteArrayOutputStream(sizeBuffer);
			UtilPDF.stamp(bos,bis,textos);
			administracion = bos.toByteArray();
			
			// Ejemplar empresa
			((TextoStamp) textos[0]).setTexto("Exemplar per a l'empresa reclamada o denunciada / Ejemplar para la empresa reclamada o denunciada");
			bis = new ByteArrayInputStream(documentoF.getDatosFichero());
			bos = new ByteArrayOutputStream(sizeBuffer);
			UtilPDF.stamp(bos,bis,textos);
			empresa = bos.toByteArray();
			
			// Ejemplar interesado
			((TextoStamp) textos[0]).setTexto("Exemplar per a la persona reclamant o denunciant / Ejemplar para la persona reclamante o denunciante");
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