package es.caib.redose.persistence.formateadores;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Properties;

import es.caib.redose.model.PlantillaIdioma;
import es.caib.redose.modelInterfaz.DocumentoRDS;
import es.caib.redose.persistence.util.UtilRDS;
import es.caib.util.StringUtil;
import es.caib.xml.ConstantesXML;
import es.caib.xml.analiza.Analizador;
import es.caib.xml.analiza.Nodo;
import es.caib.xml.util.HashMapIterable;
import es.indra.util.pdf.PDFDocument;
import es.indra.util.pdf.Propiedad;
import es.indra.util.pdf.Seccion;


/**
 * Generador de PDFs para XMLs de Formularios 
 *
 */
public class FormateadorPdfConsultaQuejas implements FormateadorDocumento{
	
	private static String TIPO = "/FORMULARIO/ESTADO_QUEJA/TIPO";

	private static String CODIGO = "/FORMULARIO/ESTADO_QUEJA/CODIGO";

	private static String NOMBRE_SOLICITANTE = "/FORMULARIO/ESTADO_QUEJA/NOMBRE_SOLICITANTE";

	private static String APELLIDO1_SOLICITANTE = "/FORMULARIO/ESTADO_QUEJA/APELLIDO1_SOLICITANTE";

	private static String APELLIDO2_SOLICITANTE = "/FORMULARIO/ESTADO_QUEJA/APELLIDO2_SOLICITANTE";

	private static String DNI_SOLICITANTE = "/FORMULARIO/ESTADO_QUEJA/NUM_DOCUMENTO_SOLICITANTE";

	private static String TIPO_DOCUMENTO = "/FORMULARIO/ESTADO_QUEJA/TIPO_DOCUMENTO";

	private static String EMAIL = "/FORMULARIO/ESTADO_QUEJA/EMAIL";

	private static String DIRECCION = "/FORMULARIO/ESTADO_QUEJA/DIRECCION";

	private static String NUMERO = "/FORMULARIO/ESTADO_QUEJA/NUMERO";

	private static String PISO = "/FORMULARIO/ESTADO_QUEJA/PISO";

	private static String CP = "/FORMULARIO/ESTADO_QUEJA/CP";

	private static String MUNICIPIO = "/FORMULARIO/ESTADO_QUEJA/MUNICIPIO";

	private static String FECHA_ENTRADA = "/FORMULARIO/ESTADO_QUEJA/FECHA";

	private static String IDIOMA = "/FORMULARIO/ESTADO_QUEJA/IDIOMA";

	private static String FORMA_CONTACTO = "/FORMULARIO/ESTADO_QUEJA/FORMA_CONTACTO";

	private static String ASUNTO = "/FORMULARIO/ESTADO_QUEJA/ASUNTO";

	private static String CONSEJERIA = "/FORMULARIO/ESTADO_QUEJA/CONSEJERIA";

	private static String DESCRIPCION = "/FORMULARIO/ESTADO_QUEJA/DESCRIPCION";

	private static String ESTADO = "/FORMULARIO/ESTADO_QUEJA/ESTADO";

	private static String FECHA_FINALIZACION = "/FORMULARIO/ESTADO_QUEJA/FECHA_FIN";

	private static String RESPUESTA = "/FORMULARIO/ESTADO_QUEJA/RESPUESTA";
	

	/**
	 * Genera PDF a partir Xpath
	 */
	public DocumentoRDS formatearDocumento(DocumentoRDS documento, PlantillaIdioma plantilla,List usos) throws Exception {
		
		PDFDocument docPDF;
		String cabecera;
		String letras[] = {"A","B","C","D"};
		int numSecciones = 0;
				
		// Cargamos fichero de properties con los textos a utilizar
		Properties props = new Properties();
		props.load(new ByteArrayInputStream(plantilla.getArchivo().getDatos()));
		String urlLogo = props.getProperty("urlLogo");
		
		// Parseamos xml
		Analizador analizador = new Analizador ();					
		HashMapIterable datosFormulario = analizador.analizar(new ByteArrayInputStream(documento.getDatosFichero()),ConstantesXML.ENCODING);	
				

		cabecera = props.getProperty("cabecera.titulo");
		
		float[] widths = new float[2];
		widths[0] = 20f;
		widths[1] = 80f;


		    	
    	// CABECERA: TITULO Y LOGO
    	// Creamos documento con la imagen y el título
		docPDF = new PDFDocument(urlLogo,cabecera);		
    	    	
		// SECCION TIPO DE ESCRITO
    	Seccion seccion = new Seccion(letras[numSecciones],props.getProperty("datosSolicitud.titulo" + letras[numSecciones]));
    	numSecciones ++;
    	
		Nodo nodo = (Nodo) datosFormulario.get(TIPO);
		String valor = "";
		if(nodo != null)
		{
			valor = nodo.getValor();
		}

		Propiedad propiedad = new Propiedad(props.getProperty("datosSolicitud.tipo"),valor,widths);
		seccion.addCampo(propiedad);

		// Numero de Expediente
		valor = "";
		nodo = (Nodo) datosFormulario.get(CODIGO);
		if(nodo != null)
		{
			valor = nodo.getValor();
			propiedad = new Propiedad(props.getProperty("datosSolicitud.numeroExpediente"),valor,widths);
			seccion.addCampo(propiedad);
		}

    	docPDF.addSeccion(seccion);

    	// SECCION DATOS DEL SOLICITANTE
		
    	seccion = new Seccion(letras[numSecciones],props.getProperty("datosSolicitud.titulo" + letras[numSecciones]));
		numSecciones++;

		// TIPO DOCUMENTO
		valor = "";
		nodo = (Nodo) datosFormulario.get(TIPO_DOCUMENTO);
		if(nodo != null)
		{
			valor = nodo.getValor();
			valor = convertirTipoDocumento(valor);
			propiedad = new Propiedad(props.getProperty("datosSolicitud.documentoAcreditativo"),valor,widths);
			seccion.addCampo(propiedad);
		}

		
		// DNI
		valor = "";
		nodo = (Nodo) datosFormulario.get(DNI_SOLICITANTE);
		if(nodo != null)
		{
			valor = nodo.getValor();
			propiedad = new Propiedad(props.getProperty("datosSolicitud.numeroDocumento"),valor,widths);
			seccion.addCampo(propiedad);
		}

		// Nombre Solicitante
		valor = "";
		String nombre = "";
		String apellido1 = "";
		String apellido2 = "";
		nodo = (Nodo) datosFormulario.get(NOMBRE_SOLICITANTE);
		if(nodo != null)	nombre = nodo.getValor();

		
		// Apellido1 Solicitante
		nodo = (Nodo) datosFormulario.get(APELLIDO1_SOLICITANTE);
		if(nodo != null)	apellido1 = nodo.getValor();

		// Apellido2 Solicitante
		nodo = (Nodo) datosFormulario.get(APELLIDO2_SOLICITANTE);
		if(nodo != null)	apellido2 = nodo.getValor();
		
		String nombreApellidos = nombre + " " + apellido1 + " " + apellido2;
		
		if(!nombreApellidos.trim().equals(""))
		{
			propiedad = new Propiedad(props.getProperty("datosSolicitud.nombre"),nombreApellidos,widths);
			seccion.addCampo(propiedad);
		}

		// email
		valor = "";
		nodo = (Nodo) datosFormulario.get(EMAIL);
		if(nodo != null)
		{
			valor = nodo.getValor();
			if(!valor.trim().equals(""))
			{
				propiedad = new Propiedad(props.getProperty("datosSolicitud.mail"),valor,widths);
				seccion.addCampo(propiedad);
			}
		}

		// direccion
		valor = "";
		nodo = (Nodo) datosFormulario.get(DIRECCION);
		if(nodo != null)
		{
			valor = nodo.getValor();
			if(!valor.trim().equals(""))
			{
				propiedad = new Propiedad(props.getProperty("datosSolicitud.direccion"),valor,widths);
				seccion.addCampo(propiedad);
			}
		}

		// numero
		valor = "";
		nodo = (Nodo) datosFormulario.get(NUMERO);
		if(nodo != null)
		{
			valor = nodo.getValor();
			if(!valor.trim().equals(""))
			{
				propiedad = new Propiedad(props.getProperty("datosSolicitud.numero"),valor,widths);
				seccion.addCampo(propiedad);
			}
		}
		
		// piso
		valor = "";
		nodo = (Nodo) datosFormulario.get(PISO);
		if(nodo != null)
		{
			valor = nodo.getValor();
			if(!valor.trim().equals(""))
			{
				propiedad = new Propiedad(props.getProperty("datosSolicitud.piso"),valor,widths);
				seccion.addCampo(propiedad);
			}
		}
		
		// CP
		valor = "";
		nodo = (Nodo) datosFormulario.get(CP);
		if(nodo != null)
		{
			valor = nodo.getValor();
			if(!valor.trim().equals(""))
			{
				propiedad = new Propiedad(props.getProperty("datosSolicitud.cp"),valor,widths);
				seccion.addCampo(propiedad);
			}
		}
		
		// Municipio
		valor = "";
		nodo = (Nodo) datosFormulario.get(MUNICIPIO);
		if(nodo != null)
		{
			valor = nodo.getValor();
			if(!valor.trim().equals(""))
			{
				propiedad = new Propiedad(props.getProperty("datosSolicitud.municipio"),valor,widths);
				seccion.addCampo(propiedad);
			}
		}
		
    	docPDF.addSeccion(seccion);


    	// SECCION DATOS DEL ESCRITO
		
    	seccion = new Seccion(letras[numSecciones],props.getProperty("datosSolicitud.titulo" + letras[numSecciones]));
		numSecciones++;
		
		// Para que no se parta el campo
		seccion.setSplitLate(false);


		// Fecha de Entrada
		valor = "";
		nodo = (Nodo) datosFormulario.get(FECHA_ENTRADA);
		if(nodo != null)
		{
			valor = nodo.getValor();
			if(!valor.trim().equals(""))
			propiedad = new Propiedad(props.getProperty("datosSolicitud.fechaent"),valor,widths);
			seccion.addCampo(propiedad);
		}

		// Idioma Respuesta
		valor = "";
		nodo = (Nodo) datosFormulario.get(IDIOMA);
		if(nodo != null)
		{
			valor = nodo.getValor();
			valor = props.getProperty(valor,"català");
			propiedad = new Propiedad(props.getProperty("datosSolicitud.idioma"),valor,widths);
			seccion.addCampo(propiedad);
		}

		// Forma de contacto
		valor = "";
		nodo = (Nodo) datosFormulario.get(FORMA_CONTACTO);
		if(nodo != null)
		{
			valor = nodo.getValor();
			propiedad = new Propiedad(props.getProperty("datosSolicitud.formaContacto"),valor,widths);
			seccion.addCampo(propiedad);
		}

		// Consejeria
		valor = "";
		nodo = (Nodo) datosFormulario.get(CONSEJERIA);
		if(nodo != null)
		{
			valor = nodo.getValor();
			propiedad = new Propiedad(props.getProperty("datosSolicitud.consejeria"),valor,widths);
			seccion.addCampo(propiedad);
		}


		// Titulo
		valor = "";
		nodo = (Nodo) datosFormulario.get(ASUNTO);
		if(nodo != null)
		{
			valor = nodo.getValor();
			propiedad = new Propiedad(props.getProperty("datosSolicitud.titulo"),valor,widths);
			seccion.addCampo(propiedad);
		}

		// Descripcion
		valor = "";
		nodo = (Nodo) datosFormulario.get(DESCRIPCION);
		if(nodo != null)
		{
			valor = nodo.getValor();
			propiedad = new Propiedad(props.getProperty("datosSolicitud.descripcion"),valor,widths);
			seccion.addCampo(propiedad);
		}


    	docPDF.addSeccion(seccion);

    	// SECCION RESPUESTA
		
    	seccion = new Seccion(letras[numSecciones],props.getProperty("datosSolicitud.titulo" + letras[numSecciones]));
		numSecciones++;
		
		seccion.setSplitLate(false);



		// Estado
		valor = "";
		nodo = (Nodo) datosFormulario.get(ESTADO);
		if(nodo != null)
		{
			valor = nodo.getValor();
			propiedad = new Propiedad(props.getProperty("datosSolicitud.estado"),valor,widths);
			seccion.addCampo(propiedad);
		}

		// Fecha Finalización
		valor = "";
		nodo = (Nodo) datosFormulario.get(FECHA_FINALIZACION);
		if(nodo != null)
		{
			valor = nodo.getValor();
			propiedad = new Propiedad(props.getProperty("datosSolicitud.fechafin"),valor,widths);
			seccion.addCampo(propiedad);
		}

		// Respuesta
		valor = "";
		nodo = (Nodo) datosFormulario.get(RESPUESTA);
		if(nodo != null)
		{
			valor = nodo.getValor();
			propiedad = new Propiedad(props.getProperty("datosSolicitud.respuesta"),valor,widths);
			seccion.addCampo(propiedad);
		}

    	docPDF.addSeccion(seccion);

    	// Generamos pdf
    	ByteArrayOutputStream bos;
    	

		bos = new ByteArrayOutputStream();    		
		docPDF.generate(bos);
    	
		// Devolvemos pdf generado		
		DocumentoRDS documentoF = UtilRDS.cloneDocumentoRDS(documento);
		documentoF.setDatosFichero(bos.toByteArray());
		documentoF.setNombreFichero(StringUtil.normalizarNombreFichero(documento.getTitulo()) + ".pdf");		
		
		bos.close();
		
		return documentoF;
	}
	
	
	private String convertirTipoDocumento(String tipo)
	{
		if(tipo.equals("1")) return "NIF";
		else if(tipo.equals("3")) return "NIE";
		else if(tipo.equals("4")) return "CIF";
		else return "NIF";
	}

		
}
