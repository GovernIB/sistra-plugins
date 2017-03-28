package es.caib.redose.persistence.formateadores;


import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

import es.caib.redose.model.PlantillaIdioma;
import es.caib.redose.modelInterfaz.DocumentoRDS;
import es.caib.redose.persistence.util.UtilRDS;
import es.caib.util.StringUtil;
import es.caib.xml.pago.XmlDatosPago;
import es.indra.util.pdf.ObjectStamp;
import es.indra.util.pdf.PDFDocument;
import es.indra.util.pdf.Parrafo;
import es.indra.util.pdf.Propiedad;
import es.indra.util.pdf.Seccion;
import es.indra.util.pdf.Tabla;
import es.indra.util.pdf.TextoStamp;
import es.indra.util.pdf.UtilPDF;


/**
 * Generador de PDFs para Pagos (multientidad). 
 *
 */
public class FormateadorPdfDocumentoPagoPresencialTPVMultientidad implements FormateadorDocumento{
	
	/**
	 * Genera PDF a partir Xpath
	 */
	public DocumentoRDS formatearDocumento(DocumentoRDS documento, PlantillaIdioma plantilla,List usos) throws Exception {
				
		byte[] datosPlantilla = plantilla.getArchivo().getDatos();
		byte[] datosFichero = documento.getDatosFichero();
		
		Properties props = new Properties();		
		props.load(new ByteArrayInputStream(datosPlantilla));
		
		byte[] pdfContent = generarPDF(props, datosFichero);
		
		// 	Generamos 3 copias: banco, administracion e interesado		
		byte[] pdfBanco = generarPdfDestinarario(pdfContent, plantilla.getIdioma(), props.getProperty("pie.banco"));
		byte[] pdfAdministracion = generarPdfDestinarario(pdfContent, plantilla.getIdioma(), props.getProperty("pie.administracion"));
		byte[] pdfInteresado = generarPdfDestinarario(pdfContent, plantilla.getIdioma(), props.getProperty("pie.interesado"));
		
		// Concatenamos las 3 copias
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		InputStream pdfs[] = {new ByteArrayInputStream(pdfBanco),new ByteArrayInputStream(pdfAdministracion),new ByteArrayInputStream(pdfInteresado)};
		UtilPDF.concatenarPdf(bos,pdfs);
		pdfContent = bos.toByteArray();
		bos.close();
		
		// Devolvemos pdf generado
    	DocumentoRDS documentoF = UtilRDS.cloneDocumentoRDS(documento);
    	documentoF.setDatosFichero(pdfContent);		
    	documentoF.setNombreFichero(StringUtil.normalizarNombreFichero(documento.getTitulo()) + ".pdf");	
		
		return documentoF;
	}

	public byte[] generarPDF(Properties props, byte[] datosFichero)
			throws IOException, Exception {
		PDFDocument docPDF;
		String cabecera;
		String letras[] = {"A","B","C","D","E","F","G"};
		int numSecciones = 0;
		Seccion seccion;
						
		XmlDatosPago datosFormulario = new XmlDatosPago();		
		datosFormulario.setBytes(datosFichero);
		
    	// Codigo entidad
    	String codigoEntidad = datosFormulario.getCodigoEntidad();
    	
    	// Tipo pago (P/T)
    	String tipoPago = datosFormulario.getTipoPago() + "";
    	
		// CABECERA: TITULO Y LOGO
		cabecera = props.getProperty("cabecera.titulo");
		// Verificamos si tiene logo personalizado por entidad
		String urlLogo = props.getProperty("urlLogo");
		if (StringUtils.isNotBlank(codigoEntidad)) {
			String urlLogoEntidad = props.getProperty("urlLogo." + codigoEntidad);
			if (StringUtils.isNotBlank(urlLogoEntidad)) {
				urlLogo = urlLogoEntidad;
			}
		}
		
    	// Creamos documento con la imagen y el título
		docPDF = new PDFDocument(urlLogo,cabecera);		
		    	
		// SECCION DECLARANTE
		seccion = new Seccion(letras[numSecciones], props.getProperty("declarante.titulo"));
    	numSecciones ++;    		
		seccion.addCampo(new Propiedad(props.getProperty("declarante.nif"), datosFormulario.getNif()));
		seccion.addCampo(new Propiedad(props.getProperty("declarante.nombre"), datosFormulario.getNombre()));
		seccion.addCampo(new Propiedad(props.getProperty("declarante.telefono"), datosFormulario.getTelefono()));
		docPDF.addSeccion(seccion);
		
		// SECCION TASA		
		seccion = new Seccion(letras[numSecciones], props.getProperty("tasa.titulo"));
    	numSecciones ++;    		
    	seccion.addCampo(new Propiedad(props.getProperty("tasa.identificador"), datosFormulario.getLocalizador()));		
		seccion.addCampo(new Propiedad(props.getProperty("tasa.concepto"), datosFormulario.getConcepto()));		
		String importe = datosFormulario.getImporte();
		if ( !StringUtils.isEmpty(importe )) {
			double dImporte = new Double( importe ).doubleValue();
			dImporte = dImporte / 100;
			String importeFormat = "" + dImporte;
			importeFormat = importeFormat.replaceAll( "\\.", "," );
			seccion.addCampo(new Propiedad(props.getProperty("tasa.importe"), importeFormat));
		}		
		Date fechaDevengo = datosFormulario.getFechaDevengo();
    	if (fechaDevengo != null )
    	{
    		String fechaDevengoFormat = StringUtil.fechaACadena(fechaDevengo, StringUtil.FORMATO_FECHA); 
    		seccion.addCampo(new Propiedad(props.getProperty("tasa.fecha"), fechaDevengoFormat));
    	}		
		docPDF.addSeccion(seccion);
		
		// SECCION ENTIDADES Y SELLO ENTIDAD 
		if (StringUtils.isNotBlank(datosFormulario.getInstruccionesPresencialEntidad1Nombre())) {
			
			seccion = new Seccion(letras[numSecciones], props.getProperty("entidadesColaboradoras.titulo"));
			numSecciones ++;   
			
			Vector columnas = new Vector();
    		columnas.add(props.getProperty("entidadesColaboradoras.entidad"));
    		columnas.add(props.getProperty("entidadesColaboradoras.numeroCuenta"));
    		
    		Vector campos = new Vector();
    		Vector cp;  
    		
    		for (int i=1; i<=10;i=i+1) {
    			
    			if (StringUtils.isBlank(getEntidadColaboradoraNombre(datosFormulario, i))) {
    				break;
    			}
    			
				cp = new Vector();
				cp.add(getEntidadColaboradoraNombre(datosFormulario, i));
				cp.add(getEntidadColaboradoraCuenta(datosFormulario, i));				
				campos.add(cp);			
			}
			
			Tabla tabla =  new Tabla(columnas,campos);
			tabla.setMostrarCabeceras(true);
			seccion.addCampo(tabla);
			
			docPDF.addSeccion(seccion);			
					
		}
		
		// SECCION SELLO ENTIDAD
		seccion = new Seccion(letras[numSecciones], props.getProperty("selloEntidadBancaria.titulo"));
		numSecciones ++;   
		seccion.addCampo(new Parrafo("\n\n\n\n\n\n\n\n"));
		docPDF.addSeccion(seccion);	
		
		// SECCION INSTRUCCIONES PRESENCIAL
		if ( StringUtils.isNotBlank(datosFormulario.getInstruccionesPresencialTexto())) {
			seccion = new Seccion(letras[numSecciones], props.getProperty("instrucciones.titulo"));
			numSecciones ++;   
			seccion.addCampo(new Parrafo(datosFormulario.getInstruccionesPresencialTexto()));
			docPDF.addSeccion(seccion);	
		}
		
    	// Generamos pdf
    	ByteArrayOutputStream bos;
    	bos = new ByteArrayOutputStream();    		
    	docPDF.generate(bos);    	
    	byte[] pdfContent = bos.toByteArray();
    	bos.close();
		return pdfContent;
	}

	private Object getEntidadColaboradoraCuenta(XmlDatosPago datosFormulario,
			int i) throws Exception{
		Method m = datosFormulario.getClass().getDeclaredMethod("getInstruccionesPresencialEntidad" + i + "Cuenta");
		String nombre = (String) m.invoke(datosFormulario);
		if (nombre == null) {
			nombre = "";
		}
		return nombre;
	}

	private String getEntidadColaboradoraNombre(XmlDatosPago datosFormulario,
			int i) throws Exception{
		Method m = datosFormulario.getClass().getDeclaredMethod("getInstruccionesPresencialEntidad" + i + "Nombre");
		String nombre = (String) m.invoke(datosFormulario);
		if (nombre == null) {
			nombre = "";
		}
		return nombre;
	}	
	
	/**
	 * Genera PDF con el pie correspondiente
	 * @param docPdf
	 * @param idioma
	 * @param tipoPie
	 * @return
	 * @throws Exception
	 */
	private byte[] generarPdfDestinarario(byte[] docPdf, String idioma, String texto)
			throws Exception {
		ByteArrayInputStream bis = new ByteArrayInputStream(docPdf);
		ByteArrayOutputStream bos = new ByteArrayOutputStream(docPdf.length + 2048);
		ObjectStamp[] stamps = new ObjectStamp[1];
		TextoStamp textoStamp = new TextoStamp();
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

