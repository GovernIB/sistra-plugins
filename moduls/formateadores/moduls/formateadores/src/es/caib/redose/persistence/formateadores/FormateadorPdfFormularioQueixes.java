package es.caib.redose.persistence.formateadores;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.redose.model.PlantillaIdioma;
import es.caib.redose.modelInterfaz.DocumentoRDS;
import es.caib.redose.persistence.util.UtilRDS;
import es.caib.util.StringUtil;
import es.caib.xml.ConstantesXML;
import es.caib.xml.analiza.Analizador;
import es.caib.xml.analiza.Nodo;
import es.caib.xml.analiza.Par;
import es.caib.xml.util.HashMapIterable;
import es.indra.util.pdf.PDFDocument;
import es.indra.util.pdf.Propiedad;
import es.indra.util.pdf.Seccion;

/**
 * 
 * Formateador específico para el formulario de Queixes
 * de IB Salut
 *
 */

public class FormateadorPdfFormularioQueixes implements FormateadorDocumento{
	
	private Log _log = LogFactory.getLog( FormateadorPdfFormularioQueixes.class );
	
	private static String CAMPO_NUMERO = "/FORMULARIO/DATOS_SOLICITANTE/NUMERO";
	
	private static String CAMPO_NOMBRE = "/FORMULARIO/DATOS_SOLICITANTE/NOMBRE";

	private static String CAMPO_APELLIDO1 = "/FORMULARIO/DATOS_SOLICITANTE/APELLIDO1";

	private static String CAMPO_APELLIDO2 = "/FORMULARIO/DATOS_SOLICITANTE/APELLIDO2";

	private static String CAMPO_IDIOMA = "/FORMULARIO/RESPUESTA_ADMINISTRATIVA/IDIOMA";

	private static String CAMPO_TELEFONO1 = "/FORMULARIO/DATOS_SOLICITANTE/TELEFONO";

	private static String CAMPO_TELEFONO2 = "/FORMULARIO/DATOS_SOLICITANTE/TELEFONO2";

	private static String CAMPO_MEDIO = "/FORMULARIO/RESPUESTA_ADMINISTRATIVA/MEDIO";

	private static String CAMPO_MAIL = "/FORMULARIO/RESPUESTA_ADMINISTRATIVA/MAIL";

	private static String CAMPO_DIRECCION = "/FORMULARIO/RESPUESTA_ADMINISTRATIVA/DIRECCION";

	private static String CAMPO_DIRECCION_NUMERO = "/FORMULARIO/RESPUESTA_ADMINISTRATIVA/DNUMERO";

	private static String CAMPO_DIRECCION_PISO = "/FORMULARIO/RESPUESTA_ADMINISTRATIVA/PISO";

	private static String CAMPO_CP = "/FORMULARIO/RESPUESTA_ADMINISTRATIVA/CP";

	private static String CAMPO_MUNICIPIO = "/FORMULARIO/RESPUESTA_ADMINISTRATIVA/MUNICIPIO";

	private static String CAMPO_TITULO_QUEJA = "/FORMULARIO/DESCRIPCION_QUEJA/TITULO";

	private static String CAMPO_TIPOCONSEJERIA = "/FORMULARIO/DESCRIPCION_QUEJA/TIPOCONSEJERIA";

	private static String CAMPO_CONSEJERIA = "/FORMULARIO/DESCRIPCION_QUEJA/CONSEJERIA";

	private static String CAMPO_TIPOESCRITO = "/FORMULARIO/DESCRIPCION_QUEJA/TIPOESCRITO";

	private static String CAMPO_DETALLES = "/FORMULARIO/DESCRIPCION_QUEJA/DETALLES";
	
	private static String CODIGO_CENTRO = "/FORMULARIO/DESCRIPCION_QUEJA_IB/CENTRO";
	
	
	public DocumentoRDS formatearDocumento(DocumentoRDS documento, PlantillaIdioma plantilla, List usos) throws Exception {
		PDFDocument docPDF;
		String cabecera;
		String letras[] = {"A","B","C"};
		int numSecciones = 0;
				
		// Cargamos fichero de properties con los textos a utilizar
		Properties props = new Properties();
		props.load(new ByteArrayInputStream(plantilla.getArchivo().getDatos()));
		String urlLogo = props.getProperty("urlLogo");
		
		// Parseamos xml
		Analizador analizador = new Analizador ();					
		HashMapIterable datosFormulario = analizador.analizar(new ByteArrayInputStream(documento.getDatosFichero()),ConstantesXML.ENCODING);	

		cabecera = props.getProperty("cabecera.titulo");
		
		    	
    	// CABECERA: TITULO
    	// No ponemos logo

		docPDF = new PDFDocument(urlLogo,cabecera);		
		
		float[] widths = new float[2];
		widths[0] = 20f;
		widths[1] = 80f;

    	    	
		// SECCION TIPO DE ESCRITO
    	Seccion seccion;
    	Propiedad propiedad;
    	
		Nodo nodo;
		String valor = "";
		Par par = null;
		
		boolean anonima = true;


		nodo = (Nodo) datosFormulario.get(CAMPO_NUMERO);
		if(nodo != null)
		{
			valor = nodo.getValor();
			if((valor != null) && (!valor.equals("")))
			{
				anonima = false;
			}
		}

		if(anonima == false)
		{
			// SECCION DATOS DEL SOLICITANTE
			
			seccion = new Seccion(letras[numSecciones],props.getProperty("datosSolicitud.titulo" + letras[numSecciones]));
			numSecciones++;
			
			// Nombre Solicitante
			valor = "";
			nodo = (Nodo) datosFormulario.get(CAMPO_NOMBRE);
			if(nodo != null)
			{
				valor = nodo.getValor();
				propiedad = new Propiedad(props.getProperty("datosSolicitud.nombre"),valor,widths);
				seccion.addCampo(propiedad);
			}
			
			
			// Apellido1 Solicitante
			valor = "";
			nodo = (Nodo) datosFormulario.get(CAMPO_APELLIDO1);
			if(nodo != null)
			{
				valor = nodo.getValor();
				propiedad = new Propiedad(props.getProperty("datosSolicitud.ape1"),valor,widths);
				seccion.addCampo(propiedad);
			}
			
			// Apellido2 Solicitante
			valor = "";
			nodo = (Nodo) datosFormulario.get(CAMPO_APELLIDO2);
			if(nodo != null)
			{
				valor = nodo.getValor();
				propiedad = new Propiedad(props.getProperty("datosSolicitud.ape2"),valor,widths);
				seccion.addCampo(propiedad);
			}
			
			
			// DNI
			valor = "";
			nodo = (Nodo) datosFormulario.get(CAMPO_NUMERO);
			if(nodo != null)
			{
				valor = nodo.getValor();
				propiedad = new Propiedad(props.getProperty("datosSolicitud.nif"),valor,widths);
				seccion.addCampo(propiedad);
			}
			
			docPDF.addSeccion(seccion);

			
			// SECCION RESPUESTA ADMINISTRATIVA AL ESCRITO PRESENTADO
			
			seccion = new Seccion(letras[numSecciones],props.getProperty("datosSolicitud.titulo" + letras[numSecciones]));
			numSecciones++;
			
			// idioma
			valor = "";
			nodo = (Nodo) datosFormulario.get(CAMPO_IDIOMA);
			if(nodo != null)
			{
				par = (Par)nodo.getAtributos().get(0);
				if(par != null)
				{
					valor = par.getValor();
					propiedad = new Propiedad(props.getProperty("datosSolicitud.idioma"),props.getProperty("datosSolicitud.idioma" + valor),widths);
					seccion.addCampo(propiedad);
				}
			}
			
			// Telefono1
			valor = "";
			nodo = (Nodo) datosFormulario.get(CAMPO_TELEFONO1);
			if(nodo != null)
			{
				valor = nodo.getValor();
				propiedad = new Propiedad(props.getProperty("datosSolicitud.telefono1"),valor,widths);
				seccion.addCampo(propiedad);
			}

			// Telefono2
			valor = "";
			nodo = (Nodo) datosFormulario.get(CAMPO_TELEFONO2);
			if(nodo != null)
			{
				valor = nodo.getValor();
				propiedad = new Propiedad(props.getProperty("datosSolicitud.telefono2"),valor,widths);
				seccion.addCampo(propiedad);
			}

			
			// medio
			valor = "";
			nodo = (Nodo) datosFormulario.get(CAMPO_MEDIO);
			if(nodo != null)
			{
				par = (Par)nodo.getAtributos().get(0);
				if(par != null)
				{
					valor = par.getValor();
					propiedad = new Propiedad(props.getProperty("datosSolicitud.medio"),props.getProperty("datosSolicitud.medio" + valor),widths);
					seccion.addCampo(propiedad);
				}
			}
			
			// email
			valor = "";
			nodo = (Nodo) datosFormulario.get(CAMPO_MAIL);
			if(nodo != null)
			{
				valor = nodo.getValor();
				propiedad = new Propiedad(props.getProperty("datosSolicitud.mail"),valor,widths);
				seccion.addCampo(propiedad);
			}
			
			// direccion
			valor = "";
			nodo = (Nodo) datosFormulario.get(CAMPO_DIRECCION);
			if(nodo != null)
			{
				valor = nodo.getValor();
				propiedad = new Propiedad(props.getProperty("datosSolicitud.direccion"),valor,widths);
				seccion.addCampo(propiedad);
			}
			
			// numero
			valor = "";
			nodo = (Nodo) datosFormulario.get(CAMPO_DIRECCION_NUMERO);
			if(nodo != null)
			{
				valor = nodo.getValor();
				propiedad = new Propiedad(props.getProperty("datosSolicitud.numero"),valor,widths);
				seccion.addCampo(propiedad);
			}
			
			// piso
			valor = "";
			nodo = (Nodo) datosFormulario.get(CAMPO_DIRECCION_PISO);
			if(nodo != null)
			{
				valor = nodo.getValor();
				propiedad = new Propiedad(props.getProperty("datosSolicitud.piso"),valor,widths);
				seccion.addCampo(propiedad);
			}
			
			// CP
			valor = "";
			nodo = (Nodo) datosFormulario.get(CAMPO_CP);
			if(nodo != null)
			{
				valor = nodo.getValor();
				propiedad = new Propiedad(props.getProperty("datosSolicitud.cp"),valor,widths);
				seccion.addCampo(propiedad);
			}
			
			// Municipio
			valor = "";
			nodo = (Nodo) datosFormulario.get(CAMPO_MUNICIPIO);
			if(nodo != null)
			{
				valor = nodo.getValor();
				propiedad = new Propiedad(props.getProperty("datosSolicitud.municipio"),valor,widths);
				seccion.addCampo(propiedad);
			}
			
			docPDF.addSeccion(seccion);
		}


    	// SECCION DATOS DEL ESCRITO
		
		if(anonima)
		{
			String keyTituloAnonima = props.getProperty("datosSolicitud.tituloAnonima");
			if(keyTituloAnonima == null)
			{
		    	seccion = new Seccion(letras[numSecciones],props.getProperty("datosSolicitud.titulo" + letras[numSecciones]));
			}
			else
			{
				seccion = new Seccion(letras[0],props.getProperty("datosSolicitud.tituloAnonima"));
			}
		}
		else
		{
	    	seccion = new Seccion(letras[numSecciones],props.getProperty("datosSolicitud.titulo" + letras[numSecciones]));
		}
		numSecciones++;

		// Para que no se parta el campo
		seccion.setSplitLate(false);

		// Tipo de escrito
		valor = "";
		nodo = (Nodo) datosFormulario.get(CAMPO_TIPOESCRITO);
		if(nodo != null)
		{
			valor = nodo.getValor();
			propiedad = new Propiedad(props.getProperty("datosSolicitud.tipoEscrito"),valor,widths);
			seccion.addCampo(propiedad);
		}

		// Titulo
		valor = "";
		nodo = (Nodo) datosFormulario.get(CAMPO_TITULO_QUEJA);
		if(nodo != null)
		{
			valor = nodo.getValor();
			propiedad = new Propiedad(props.getProperty("datosSolicitud.titulo"),valor,widths);
			seccion.addCampo(propiedad);
		}

		// Centro
		valor = "";
		nodo = (Nodo) datosFormulario.get(CAMPO_TIPOCONSEJERIA);
		if(nodo != null)
		{
			valor = nodo.getValor();
			propiedad = new Propiedad(props.getProperty("datosSolicitud.centro"),valor,widths);
			seccion.addCampo(propiedad);
		}
		else  /* Por compatibilidad */
		{
			nodo = (Nodo) datosFormulario.get(CAMPO_CONSEJERIA);
			if(nodo != null)
			{
				valor = nodo.getValor();
				propiedad = new Propiedad(props.getProperty("datosSolicitud.consejeria"),valor,widths);
				seccion.addCampo(propiedad);
			}
		}

		// Descripcion
		valor = "";
		nodo = (Nodo) datosFormulario.get(CAMPO_DETALLES);
		if(nodo != null)
		{
			valor = nodo.getValor();
			propiedad = new Propiedad(props.getProperty("datosSolicitud.descripcion"),valor,widths);
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
	

	

}
