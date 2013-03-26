package es.caib.redose.persistence.formateadores;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import es.caib.redose.model.PlantillaIdioma;
import es.caib.redose.modelInterfaz.DocumentoRDS;
import es.caib.util.StringUtil;
import es.caib.xml.pago.XmlDatosPago;
import es.indra.util.pdf.ObjectStamp;
import es.indra.util.pdf.TextoStamp;
import es.indra.util.pdf.UtilPDF;

/**
 * Generador de PDF para documento de pago presencial pago TPV.
 *
 */
public class FormateadorPdfDocumentoPagoPresencialTPV extends FormateadorPdfFormularios {
	
	private final static int PIE_BANCO = 0;
	private final static int PIE_ADMINISTRACION = 1;
	private final static int PIE_INTERESADO = 2;
	
	private final static String PIE_BANCO_ES = "Copia para la entidad bancaria";
	private final static String PIE_BANCO_CA = "Còpia per a l'entitat bancària ";
	private final static String PIE_BANCO_EN = "Copy for Bank";
	
	private final static String PIE_ADMINISTRACION_ES = "Copia para entregar la administración";
	private final static String PIE_ADMINISTRACION_CA = "Còpia per lliurar a l'administració";
	private final static String PIE_ADMINISTRACION_EN = "Copy for deliver to Administration";
	
	private final static String PIE_INTERESADO_ES = "Copia para el interesado";
	private final static String PIE_INTERESADO_CA = "Còpia per a l'interessat";
	private final static String PIE_INTERESADO_EN = "Copy for Interested";
	
	public FormateadorPdfDocumentoPagoPresencialTPV(){
		super();		
	}
	
	public DocumentoRDS formatearDocumento(DocumentoRDS documento, PlantillaIdioma plantilla,List usos) throws Exception {
		
		// Formateamos a PDF
		DocumentoRDS docPdf = super.formatearDocumento(documento, plantilla, usos);
		
		// Generamos 3 copias: banco, administracion e interesado		
		byte[] pdfBanco = generarPdf(docPdf, plantilla.getIdioma(), PIE_BANCO);
		byte[] pdfAdministracion = generarPdf(docPdf, plantilla.getIdioma(), PIE_ADMINISTRACION);
		byte[] pdfInteresado = generarPdf(docPdf, plantilla.getIdioma(), PIE_INTERESADO);

		// Concatenamos las 3 copias
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		InputStream pdfs[] = {new ByteArrayInputStream(pdfBanco),new ByteArrayInputStream(pdfAdministracion),new ByteArrayInputStream(pdfInteresado)};
		UtilPDF.concatenarPdf(bos,pdfs);
    	 
		// Retornamos pdf
		docPdf.setDatosFichero(bos.toByteArray());
		return docPdf;
	}
	
	/**
	 * Tratamos datos que se necesitan formatear
	 * @param datos Datos 
	 */
	protected void tratarDatos(HashMap datos) throws Exception{
		// Formateamos fecha devengo
		String fechaDevengoStr = (String) datos.get( referenciaCampo(XmlDatosPago.XML_FECHA_DEVENGO));
		SimpleDateFormat sdf = new SimpleDateFormat( XmlDatosPago.FORMATO_FECHAS );
		Date fechaDevengo = sdf.parse(fechaDevengoStr);
		datos.put(referenciaCampo(XmlDatosPago.XML_FECHA_DEVENGO), StringUtil.fechaACadena(fechaDevengo, StringUtil.FORMATO_FECHA));
		
		// Formateamos importe
		String importeStr = (String) datos.get( referenciaCampo(XmlDatosPago.XML_IMPORTE));
		importeStr = importeStr.substring(0, importeStr.length() - 2) + "." + importeStr.substring(importeStr.length() - 2);
		DecimalFormatSymbols df = new DecimalFormatSymbols(new Locale("es")); 
		NumberFormat formatter = new DecimalFormat("#,###.00", df);
		String num = formatter.format(Double.parseDouble(importeStr));
		datos.put(referenciaCampo(XmlDatosPago.XML_IMPORTE), num);
	}


	/**
	 * Genera PDF con el pie correspondiente
	 * @param docPdf
	 * @param idioma
	 * @param tipoPie
	 * @return
	 * @throws Exception
	 */
	private byte[] generarPdf(DocumentoRDS docPdf, String idioma, int tipoPie)
			throws Exception {
		ByteArrayInputStream bis = new ByteArrayInputStream(docPdf.getDatosFichero());
		ByteArrayOutputStream bos = new ByteArrayOutputStream(docPdf.getDatosFichero().length + 2048);
		ObjectStamp[] stamps = new ObjectStamp[1];
		TextoStamp textoStamp = new TextoStamp();
		String texto = "";
		switch (tipoPie) {
			case PIE_BANCO:
				if ("es".equals(idioma)) {
					texto = PIE_BANCO_ES;
				} else if ("en".equals(idioma)) {
					texto = PIE_BANCO_EN;
				} else {
					texto = PIE_BANCO_CA;
				}
				break;
			case PIE_ADMINISTRACION:
				if ("es".equals(idioma)) {
					texto = PIE_ADMINISTRACION_ES;
				} else if ("en".equals(idioma)) {
					texto = PIE_ADMINISTRACION_EN;
				} else {
					texto = PIE_ADMINISTRACION_CA;
				}
				break;
			case PIE_INTERESADO:
				if ("es".equals(idioma)) {
					texto = PIE_INTERESADO_ES;
				} else if ("en".equals(idioma)) {
					texto = PIE_INTERESADO_EN;
				} else {
					texto = PIE_INTERESADO_CA;
				}
				break;
		}
		textoStamp.setTexto(texto);
		textoStamp.setX(200);
		textoStamp.setY(50);			
		textoStamp.setFontColor(Color.GRAY);
		textoStamp.setFontSize(14);
		stamps[0] = textoStamp;
		UtilPDF.stamp(bos, bis, stamps );
		
		byte[] res = bos.toByteArray();
		return res;
	}
	
}
