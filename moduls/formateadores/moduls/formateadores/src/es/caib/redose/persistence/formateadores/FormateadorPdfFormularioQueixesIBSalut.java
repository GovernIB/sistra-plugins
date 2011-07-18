package es.caib.redose.persistence.formateadores;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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
import es.indra.util.pdf.ImageStamp;
import es.indra.util.pdf.ObjectStamp;
import es.indra.util.pdf.PDFDocument;
import es.indra.util.pdf.Propiedad;
import es.indra.util.pdf.Seccion;
import es.indra.util.pdf.UtilPDF;


/**
 * 
 * Formateador específico para el formulario de Queixes
 * de IB Salut
 *
 */

public class FormateadorPdfFormularioQueixesIBSalut implements FormateadorDocumento{
	
	private Log _log = LogFactory.getLog( FormateadorPdfFormularioQueixesIBSalut.class );
	
	
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

	// Campos Especificos del IB Salut

	private static String CAMPO_IB_ESCRITOS = "/FORMULARIO/TIPOS_ESCRITO_IB/ESCRITOS";

	private static String CAMPO_DATOS_PACIENTE_NOMBRE = "/FORMULARIO/DATOS_PACIENTE/NOMBRE";

	private static String CAMPO_DATOS_PACIENTE_APELLIDO1 = "/FORMULARIO/DATOS_PACIENTE/APELLIDO1";

	private static String CAMPO_DATOS_PACIENTE_APELLIDO2 = "/FORMULARIO/DATOS_PACIENTE/APELLIDO2";

	private static String CAMPO_DATOS_PACIENTE_FECHA = "/FORMULARIO/DATOS_PACIENTE/FECHANACIMIENTO";

	private static String CAMPO_DATOS_PACIENTE_NSS = "/FORMULARIO/DATOS_PACIENTE/NSS";

	private static String CAMPO_DATOS_PACIENTE_NIF = "/FORMULARIO/DATOS_PACIENTE/NIF";

	private static String CAMPO_IB_TITULO_QUEJA = "/FORMULARIO/DESCRIPCION_QUEJA_IB/TITULO";

	private static String CAMPO_IB_CENTRO = "/FORMULARIO/DESCRIPCION_QUEJA_IB/CENTRO";

	private static String CAMPO_IB_DETALLES = "/FORMULARIO/DESCRIPCION_QUEJA_IB/DETALLES";

	private static String CODIGO_CENTRO = "/FORMULARIO/DESCRIPCION_QUEJA_IB/CENTRO";
	
	private static int POSX_LOGO_IB = 40; 

	private static int POSY_LOGOS = 730; 

	private static float X_SCALE_LOGO = 150;
	
	private static float Y_SCALE_LOGO = 55;
	
	private static String ID_LOGO_IBS = "IBS"; 
	
	
	public DocumentoRDS formatearDocumento(DocumentoRDS documento, PlantillaIdioma plantilla, List usos) throws Exception {
		PDFDocument docPDF;
		String cabecera;
		String letras[] = {"A","B","C","D","E"};
		int numSecciones = 0;
				
		// Cargamos fichero de properties con los textos a utilizar
		Properties props = new Properties();
		props.load(new ByteArrayInputStream(plantilla.getArchivo().getDatos()));
		
		// Parseamos xml
		Analizador analizador = new Analizador ();					
		HashMapIterable datosFormulario = analizador.analizar(new ByteArrayInputStream(documento.getDatosFichero()),ConstantesXML.ENCODING);	

		cabecera = props.getProperty("cabecera.titulo");
		
		    	
    	// CABECERA: TITULO
    	// No ponemos logo
		String imagen=null;

		docPDF = new PDFDocument(imagen,cabecera);		
		
		float[] widths = new float[2];
		widths[0] = 20f;
		widths[1] = 80f;

    	    	
		// SECCION TIPO DE ESCRITO
    	Seccion seccion = new Seccion(letras[numSecciones],props.getProperty("datosSolicitud.titulo" + letras[numSecciones]));
    	numSecciones ++;
    	
    	Propiedad propiedad;
    	
		Nodo nodo = (Nodo) datosFormulario.get(CAMPO_IB_ESCRITOS);
		String valor = "";
		Par par = null;
		if(nodo != null)
		{
			par = (Par)nodo.getAtributos().get(0);
			if(par != null)
			{
				valor = par.getValor();
				propiedad = new Propiedad(props.getProperty("datosSolicitud.tipo"),props.getProperty("datosSolicitud.tipo" + valor),widths);
				seccion.addCampo(propiedad);

		    	docPDF.addSeccion(seccion);
				
			}
		}


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

    	// SECCION DATOS DEL PACIENTE
		
    	seccion = new Seccion(letras[numSecciones],props.getProperty("datosSolicitud.titulo" + letras[numSecciones]));
		numSecciones++;

		// Nombre Paciente
		valor = "";
		nodo = (Nodo) datosFormulario.get(CAMPO_DATOS_PACIENTE_NOMBRE);
		if(nodo != null)
		{
			valor = nodo.getValor();
			propiedad = new Propiedad(props.getProperty("datosSolicitud.paciente.nombre"),valor,widths);
			seccion.addCampo(propiedad);
		}

		
		// Apellido1 Paciente
		valor = "";
		nodo = (Nodo) datosFormulario.get(CAMPO_DATOS_PACIENTE_APELLIDO1);
		if(nodo != null)
		{
			valor = nodo.getValor();
			propiedad = new Propiedad(props.getProperty("datosSolicitud.paciente.ape1"),valor,widths);
			seccion.addCampo(propiedad);
		}

		// Apellido2 Paciente
		valor = "";
		nodo = (Nodo) datosFormulario.get(CAMPO_DATOS_PACIENTE_APELLIDO2);
		if(nodo != null)
		{
			valor = nodo.getValor();
			propiedad = new Propiedad(props.getProperty("datosSolicitud.paciente.ape2"),valor,widths);
			seccion.addCampo(propiedad);
		}

		
		// Fecha de Nacimiento del Paciente
		valor = "";
		nodo = (Nodo) datosFormulario.get(CAMPO_DATOS_PACIENTE_FECHA);
		if(nodo != null)
		{
			valor = nodo.getValor();
			propiedad = new Propiedad(props.getProperty("datosSolicitud.paciente.fechanac"),valor,widths);
			seccion.addCampo(propiedad);
		}

		// DNI
		valor = "";
		nodo = (Nodo) datosFormulario.get(CAMPO_DATOS_PACIENTE_NIF);
		if(nodo != null)
		{
			valor = nodo.getValor();
			propiedad = new Propiedad(props.getProperty("datosSolicitud.paciente.nif"),valor,widths);
			seccion.addCampo(propiedad);
		}
		// Numero de Tarjeta Sanitaria
		valor = "";
		nodo = (Nodo) datosFormulario.get(CAMPO_DATOS_PACIENTE_NSS);
		if(nodo != null)
		{
			valor = nodo.getValor();
			propiedad = new Propiedad(props.getProperty("datosSolicitud.paciente.tarjeta"),valor,widths);
			seccion.addCampo(propiedad);
		}

		
    	docPDF.addSeccion(seccion);



    	// SECCION DATOS DEL ESCRITO
		
    	seccion = new Seccion(letras[numSecciones],props.getProperty("datosSolicitud.titulo" + letras[numSecciones]));
		numSecciones++;

		// Para que no se parta el campo
		seccion.setSplitLate(false);


		// Titulo
		valor = "";
		nodo = (Nodo) datosFormulario.get(CAMPO_IB_TITULO_QUEJA);
		if(nodo != null)
		{
			valor = nodo.getValor();
			propiedad = new Propiedad(props.getProperty("datosSolicitud.titulo"),valor,widths);
			seccion.addCampo(propiedad);
		}

		// Centro
		valor = "";
		nodo = (Nodo) datosFormulario.get(CAMPO_IB_CENTRO);
		if(nodo != null)
		{
			valor = nodo.getValor();
			propiedad = new Propiedad(props.getProperty("datosSolicitud.centro"),valor,widths);
			seccion.addCampo(propiedad);
		}

		// Descripcion
		valor = "";
		nodo = (Nodo) datosFormulario.get(CAMPO_IB_DETALLES);
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
		
		ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());
		
		nodo = (Nodo) datosFormulario.get(CODIGO_CENTRO);
		String codigoCentro = null;
		boolean estampado = false;
		
		if(nodo != null)
		{
			par = (Par)nodo.getAtributos().get(0);
			if(par != null)
			{
				codigoCentro = par.getValor();
				bais = new ByteArrayInputStream(bos.toByteArray());
				stampLogo(bos,bais,codigoCentro,POSX_LOGO_IB,POSY_LOGOS);
				bais.close();
				estampado = true;
			}
		}
		
		if(!estampado)
		{
			stampLogo(bos,bais,"IBS",POSX_LOGO_IB,POSY_LOGOS);
		}
		
		
    	
		// Devolvemos pdf generado
		DocumentoRDS documentoF = UtilRDS.cloneDocumentoRDS(documento);
		documentoF.setDatosFichero(bos.toByteArray());
		documentoF.setNombreFichero(StringUtil.normalizarNombreFichero(documento.getTitulo()) + ".pdf");	
		
		bos.close();
		
		return documentoF;
	}
	
	private void stampLogo(OutputStream baos, InputStream bais, String codigoCentro, int x, int y) throws Exception
	{
		ObjectStamp logos [] = new ObjectStamp[1];
		
		_log.debug("Vamos a poner el logo del centro: " + codigoCentro);
		
		String recurso = "resources/IBSalut/" + codigoCentro + ".JPG";
		
		InputStream is = this.getClass().getResourceAsStream(recurso);
		
		if(is == null)
		{
			_log.debug("No existe el logo del centro: " + codigoCentro + " y vamos a estampar el del IBS");
			recurso = "resources/IBSalut/" + ID_LOGO_IBS + ".JPG";
			is = this.getClass().getResourceAsStream(recurso);
			if(is == null) return;
		}

		_log.debug("Si existe el logo del centro: " + codigoCentro + " y vamos a estamparlo");

		byte [] imagen =obtenerContenidoFichero(is);			
		logos[0] = new ImageStamp();
		((ImageStamp) logos[0]).setImagen(imagen);		
		
		logos[0].setPage(1);
		logos[0].setX(x);
		logos[0].setY(y);		
		logos[0].setOverContent(false);
		((ImageStamp) logos[0]).setXScale(new Float(X_SCALE_LOGO));
		((ImageStamp) logos[0]).setYScale(new Float(Y_SCALE_LOGO));
		
		//Reseteamos el buffer
		((ByteArrayOutputStream) baos).reset();
		
		UtilPDF.stamp(baos,bais,logos);
		
		is.close();

	}
	
	 private byte[] obtenerContenidoFichero( InputStream is ) throws Exception
	 {
	  byte[] byteToReturn = new byte[ is.available()];
	  is.read( byteToReturn, 0, byteToReturn.length );
	  return byteToReturn;
	 }
	

	

}
