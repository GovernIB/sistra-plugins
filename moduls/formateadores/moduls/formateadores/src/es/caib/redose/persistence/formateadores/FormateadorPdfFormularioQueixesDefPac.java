package es.caib.redose.persistence.formateadores;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
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

public class FormateadorPdfFormularioQueixesDefPac implements FormateadorDocumento{
	
	private Log _log = LogFactory.getLog( FormateadorPdfFormularioQueixesDefPac.class );
	
	private static String T_PAR = "PAR";

	private static String T_NODO = "NODO";
	
	private static String CAMPO_DP_EXPEDIENTE_SI_NO = "/FORMULARIO/DATOS_IDENTIFICACION/EXPEDIENTE";

	
	/* Circuito con Expediente */
	
	private static String CAMPO_DP_EXPEDIENTE = "/FORMULARIO/DATOS_IDENTIFICACION/NUMEROEXPEDIENTE";
	
	private static String CAMPO_IB_ESCRITOS = "/FORMULARIO/DATOS_ESCRITO/ESCRITO";

	private static String CAMPO_DP_TIPO_ESCRITO = "/FORMULARIO/DATOS_SOLICITANTE/TIPOESCRITO";

	private static String CAMPO_NUMERO = "/FORMULARIO/DATOS_SOLICITANTE/NUMERO";
	
	private static String CAMPO_DP_NUMERO_MOD = "/FORMULARIO/DATOS_SOLICITANTE/NUMERO_MOD";
	
	private static String CAMPO_NOMBRE = "/FORMULARIO/DATOS_SOLICITANTE/NOMBRE";

	private static String CAMPO_DP_NOMBRE_MOD = "/FORMULARIO/DATOS_SOLICITANTE/NOMBRE_MOD";

	private static String CAMPO_APELLIDO1 = "/FORMULARIO/DATOS_SOLICITANTE/APELLIDO1";

	private static String CAMPO_DP_APELLIDO1_MOD = "/FORMULARIO/DATOS_SOLICITANTE/APELLIDO1_MOD";

	private static String CAMPO_APELLIDO2 = "/FORMULARIO/DATOS_SOLICITANTE/APELLIDO2";

	private static String CAMPO_DP_APELLIDO2_MOD = "/FORMULARIO/DATOS_SOLICITANTE/APELLIDO2_MOD";

	private static String CAMPO_DP_TELEFONO1 = "/FORMULARIO/DATOS_SOLICITANTE/TELEFONO";

	private static String CAMPO_DP_TELEFONO2 = "/FORMULARIO/DATOS_SOLICITANTE/TELEFONO2";

	private static String CAMPO_DP_TELEFONO1_MOD = "/FORMULARIO/DATOS_SOLICITANTE/TELEFONO_MOD";

	private static String CAMPO_DP_TELEFONO2_MOD = "/FORMULARIO/DATOS_SOLICITANTE/TELEFONO2_MOD";

	private static String CAMPO_IDIOMA = "/FORMULARIO/RESPUESTA_ADMINISTRATIVA/IDIOMA";
	
	private static String CAMPO_MEDIO = "/FORMULARIO/RESPUESTA_ADMINISTRATIVA/MEDIO";

	private static String CAMPO_MAIL = "/FORMULARIO/RESPUESTA_ADMINISTRATIVA/MAIL";

	private static String CAMPO_DIRECCION = "/FORMULARIO/RESPUESTA_ADMINISTRATIVA/DIRECCION";

	private static String CAMPO_DIRECCION_NUMERO = "/FORMULARIO/RESPUESTA_ADMINISTRATIVA/DNUMERO";

	private static String CAMPO_DIRECCION_PISO = "/FORMULARIO/RESPUESTA_ADMINISTRATIVA/PISO";

	private static String CAMPO_MUNICIPIO = "/FORMULARIO/RESPUESTA_ADMINISTRATIVA/MUNICIPIO";

	private static String CAMPO_CP = "/FORMULARIO/RESPUESTA_ADMINISTRATIVA/CP";

	private static String CAMPO_DATOS_PACIENTE_NOMBRE = "/FORMULARIO/DATOS_PACIENTE/NOMBRE";

	private static String CAMPO_DATOS_PACIENTE_APELLIDO1 = "/FORMULARIO/DATOS_PACIENTE/APELLIDO1";

	private static String CAMPO_DATOS_PACIENTE_APELLIDO2 = "/FORMULARIO/DATOS_PACIENTE/APELLIDO2";

	private static String CAMPO_DATOS_PACIENTE_FECHA = "/FORMULARIO/DATOS_PACIENTE/FECHANACIMIENTO";

	private static String CAMPO_DATOS_PACIENTE_NSS = "/FORMULARIO/DATOS_PACIENTE/NSS";

	private static String CAMPO_DATOS_PACIENTE_NIF = "/FORMULARIO/DATOS_PACIENTE/NIF";


	private static String CAMPO_DP_CENTRO_IBS = "/FORMULARIO/DATOS_ESCRITO/CENTRO";

	private static String CAMPO_DP_TITULO = "/FORMULARIO/ESCRITO_DEFENSOR/TITULODEFENSOR";

	private static String CAMPO_DP_DETALLES = "/FORMULARIO/ESCRITO_DEFENSOR/TEXTAREA";


	private static String CAMPO_DP_TITULO_IBS = "/FORMULARIO/DATOS_ESCRITO/TITULOIB";

	private static String CAMPO_DP_FECHA_ENTRADA_IBS = "/FORMULARIO/DATOS_ESCRITO/FECHAENTRADAOCULTO";

	private static String CAMPO_DP_FECHA_FINALIZACION_IBS = "/FORMULARIO/DATOS_ESCRITO/FECHAFINALIZACIONOCULTO";

	private static String CAMPO_DP_ESTADO_IBS = "/FORMULARIO/DATOS_ESCRITO/ESTADOIBSOCULTO";

	private static String CAMPO_DP_RESPUESTA_IBS = "/FORMULARIO/DATOS_ESCRITO/RESPUESTAOCULTO";


	//private static String CAMPO_DP_EXPEDIENTE = "/FORMULARIO/DATOS_IDENTIFICACION/NUMEROEXPEDIENTE";

	private static String CAMPO_DP_DETALLES_IBS = "/FORMULARIO/DATOS_ESCRITO/TEXTAREA";

	/* Circuito sin Expediente */

	private static String CAMPO_DP_NUMERO = "/FORMULARIO/DATOS_GENERALES/NUMERO_SEXP";

	private static String CAMPO_DP_NOMBRE = "/FORMULARIO/DATOS_GENERALES/NOMBRE_SEXP";

	private static String CAMPO_DP_APELLIDO1 = "/FORMULARIO/DATOS_GENERALES/APELLIDO1_SEXP";

	private static String CAMPO_DP_APELLIDO2 = "/FORMULARIO/DATOS_GENERALES/APELLIDO2_SEXP";

	private static String CAMPO_DP_TELEFONO1_SEXP = "/FORMULARIO/DATOS_GENERALES/TELEFONO_SEXP";

	private static String CAMPO_DP_TELEFONO2_SEXP = "/FORMULARIO/DATOS_GENERALES/TELEFONO2_SEXP";
	
	private static String CAMPO_DP_PACIENTE_NOMBRE = "/FORMULARIO/DATOS_GENERALES/NOMBRE_SEXP2";

	private static String CAMPO_DP_PACIENTE_APELLIDO1 = "/FORMULARIO/DATOS_GENERALES/APELLIDO1_SEXP2";

	private static String CAMPO_DP_PACIENTE_APELLIDO2 = "/FORMULARIO/DATOS_GENERALES/APELLIDO2_SEXP2";

	private static String CAMPO_DP_PACIENTE_FECHA = "/FORMULARIO/DATOS_GENERALES/FECHANACIMIENTO_SEXP2";

	private static String CAMPO_DP_PACIENTE_NSS = "/FORMULARIO/DATOS_GENERALES/NSS_SEXP";

	private static String CAMPO_DP_PACIENTE_NIF = "/FORMULARIO/DATOS_GENERALES/NIF_SEXP";
	
	private static String CAMPO_DP_IDIOMA = "/FORMULARIO/DATOS_GENERALES/IDIOMA_SEXP";

	private static String CAMPO_DP_MEDIO = "/FORMULARIO/DATOS_GENERALES/MEDIO_SEXP";

	private static String CAMPO_DP_MAIL = "/FORMULARIO/DATOS_GENERALES/MAIL_SEXP";

	private static String CAMPO_DP_DIRECCION = "/FORMULARIO/DATOS_GENERALES/DIRECCION_SEXP";

	private static String CAMPO_DP_DIRECCION_NUMERO = "/FORMULARIO/DATOS_GENERALES/DNUMERO_SEXP";

	private static String CAMPO_DP_DIRECCION_PISO = "/FORMULARIO/DATOS_GENERALES/PISO_SEXP";

	private static String CAMPO_DP_CP = "/FORMULARIO/DATOS_GENERALES/CP_SEXP";

	private static String CAMPO_DP_MUNICIPIO = "/FORMULARIO/DATOS_GENERALES/MUNICIPIO_SEXP";

	private static String CAMPO_DP_CENTRO2_IBS = "/FORMULARIO/DATOS_GENERALES/CENTRO_SEXP";

	private static String CAMPO_DP_DETALLES2_IBS = "/FORMULARIO/DATOS_GENERALES/TEXTAREA2_SEXP";
	
	
	//Defensor

	private static String CAMPO_DP_TITULO_QUEJAS = "/FORMULARIO/DATOS_GENERALES/TITULO2_SEXP";

	private static String CAMPO_DP_DETALLES2 = "/FORMULARIO/DATOS_GENERALES/TEXTAREA_SEXP";

	private static String CAMPO_DP_MOD_DATOS_SOLICITANTE = "/FORMULARIO/DATOS_SOLICITANTE/MODIFICAR";

	// TODO Pep, revisar
	private static String CAMPO_DP_MOD_RESP_ADMINISTRATIVA = "/FORMULARIO/RESPUESTA_ADMINISTRATIVA/MODIFICAR";
	// TODO Pep, revisar
	private static String CAMPO_IDIOMA_MOD = "/FORMULARIO/RESPUESTA_ADMINISTRATIVA/IDIOMA_MOD";
	
	private static String CAMPO_MEDIO_MOD = "/FORMULARIO/RESPUESTA_ADMINISTRATIVA/MEDIO_MOD";

	private static String CAMPO_MAIL_MOD = "/FORMULARIO/RESPUESTA_ADMINISTRATIVA/MAIL_MOD";

	private static String CAMPO_DIRECCION_MOD = "/FORMULARIO/RESPUESTA_ADMINISTRATIVA/DIRECCION_MOD";

	private static String CAMPO_DIRECCION_NUMERO_MOD = "/FORMULARIO/RESPUESTA_ADMINISTRATIVA/DNUMERO_MOD";

	private static String CAMPO_DIRECCION_PISO_MOD = "/FORMULARIO/RESPUESTA_ADMINISTRATIVA/PISO_MOD";

	private static String CAMPO_MUNICIPIO_MOD = "/FORMULARIO/RESPUESTA_ADMINISTRATIVA/MUNICIPIO_MOD";

	private static String CAMPO_CP_MOD = "/FORMULARIO/RESPUESTA_ADMINISTRATIVA/CP_MOD";


	
	private static int POSX_LOGO_IB = 40; 

	private static int POSX_LOGO_DP = 410; 

	private static int POSY_LOGOS = 725; 

	private static float X_SCALE_LOGO = 150;
	
	private static float Y_SCALE_LOGO = 55;
	
	private static String ID_LOGO_IBS = "IBS"; 
	
	private static String ID_LOGO_DP = "DUS"; 
	
	private Properties props = new Properties();
	
	private HashMapIterable datosFormulario = null;
	
	
	public DocumentoRDS formatearDocumento(DocumentoRDS documento, PlantillaIdioma plantilla, List usos) throws Exception {
		PDFDocument docPDF;
		String cabecera;
		String letras[] = {"A","B","C","D","E"};
		int numSecciones = 0;
				
		// Cargamos fichero de properties con los textos a utilizar
		this.props.load(new ByteArrayInputStream(plantilla.getArchivo().getDatos()));
		
		// Parseamos xml
		Analizador analizador = new Analizador ();					
		this.datosFormulario = analizador.analizar(new ByteArrayInputStream(documento.getDatosFichero()),ConstantesXML.ENCODING);	

		cabecera = props.getProperty("cabecera.titulo");
		
		    	
    	// CABECERA: TITULO
    	// No ponemos logo
		String imagen=null;

		docPDF = new PDFDocument(imagen,cabecera);		
		
		float[] widths = new float[2];
		widths[0] = 20f;
		widths[1] = 80f;

    	Nodo nodo = (Nodo) datosFormulario.get(CAMPO_DP_EXPEDIENTE_SI_NO);
		String valor = "";
		Par par = null;
		boolean conExpediente = false;
		
		if(nodo != null)
		{
			par = (Par)nodo.getAtributos().get(0);
			if((par != null) && par.getValor().equals("S")) conExpediente = true;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
		Date now = new Date();


		valor = "";
		Seccion seccion = null;
		String nombre = "";
		boolean datosModificados = false;
		boolean respAdministrativaModificados = false;
		boolean mail = false;
		boolean sugerencia = false;
		if(conExpediente)
		{
			// SECCION IDENTIFICACION DEL EXPEDIENTE
	    	seccion = new Seccion(letras[numSecciones],props.getProperty("datosSolicitud.titulo" + letras[numSecciones]));
	    	numSecciones ++;
	    	
			
			/* Tipo de escrito */
			
			createAddPropiedad(CAMPO_DP_TIPO_ESCRITO, FormateadorPdfFormularioQueixesDefPac.T_PAR,seccion,
							   "datosSolicitud.tipo", widths );
			

			// Numero de Envio
			createAddPropiedad(CAMPO_DP_EXPEDIENTE, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
					   		   "datosSolicitud.envio", widths );

			docPDF.addSeccion(seccion);
		

			// SECCION DATOS DEL SOLICITANTE
			
	    	seccion = new Seccion(letras[numSecciones],props.getProperty("datosSolicitud.titulo" + letras[numSecciones]));
			numSecciones++;

			valor = getDatosCampo(CAMPO_DP_MOD_DATOS_SOLICITANTE,FormateadorPdfFormularioQueixesDefPac.T_NODO);
			if(valor.equals("true")) datosModificados =  true;

			if(datosModificados)
			{
				// NIF Solicitante
				createAddPropiedad(CAMPO_DP_NUMERO_MOD, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
		   		           "datosSolicitud.nif", widths );
				// Nombre Solicitante
				nodo = (Nodo) datosFormulario.get(CAMPO_DP_NOMBRE_MOD);
				if(nodo != null)	nombre = nodo.getValor() + " ";
				// Apellido1 Solicitante
				nodo = (Nodo) datosFormulario.get(CAMPO_DP_APELLIDO1_MOD);
				if(nodo != null)	nombre += nodo.getValor() + " ";
				// Apellido2 Solicitante
				nodo = (Nodo) datosFormulario.get(CAMPO_DP_APELLIDO2_MOD);
				if(nodo != null)	nombre += nodo.getValor();
				createAddPropiedad(seccion,"datosSolicitud.nombre",  nombre, widths);
				// Telefono1
				createAddPropiedad(CAMPO_DP_TELEFONO1_MOD, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
		   		           "datosSolicitud.telefono1", widths );
				// Telefono2
				createAddPropiedad(CAMPO_DP_TELEFONO2_MOD, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
		   		           "datosSolicitud.telefono2", widths );

			}
			else
			{
				// NIF Solicitante
				createAddPropiedad(CAMPO_NUMERO, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
		   		           "datosSolicitud.nif", widths );
				// Nombre Solicitante
				nodo = (Nodo) datosFormulario.get(CAMPO_NOMBRE);
				if(nodo != null)	nombre = nodo.getValor() + " ";
				// Apellido1 Solicitante
				nodo = (Nodo) datosFormulario.get(CAMPO_APELLIDO1);
				if(nodo != null)	nombre += nodo.getValor() + " ";
				// Apellido2 Solicitante
				nodo = (Nodo) datosFormulario.get(CAMPO_APELLIDO2);
				if(nodo != null)	nombre += nodo.getValor();
				createAddPropiedad(seccion,"datosSolicitud.nombre",  nombre, widths);
				// Telefono1
				createAddPropiedad(CAMPO_DP_TELEFONO1, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
		   		           "datosSolicitud.telefono1", widths );
				// Telefono2
				createAddPropiedad(CAMPO_DP_TELEFONO2, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
		   		           "datosSolicitud.telefono2", widths );
			}

			valor = getDatosCampo(CAMPO_DP_MOD_RESP_ADMINISTRATIVA,FormateadorPdfFormularioQueixesDefPac.T_NODO);
			if(valor.equals("true")) respAdministrativaModificados =  true;
			
			valor = getDatosCampo(CAMPO_MEDIO + ((respAdministrativaModificados) ? "_MOD" : ""), FormateadorPdfFormularioQueixesDefPac.T_PAR);
			if(valor.equals("email")) mail =  true;

			// email
			createAddPropiedad(CAMPO_MAIL + ((respAdministrativaModificados) ? "_MOD" : ""), FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
					"datosSolicitud.mail", widths );
			// direccion
			nodo = (Nodo) datosFormulario.get(CAMPO_DIRECCION  + ((respAdministrativaModificados) ? "_MOD" : ""));
			String direccion = "";
			if(nodo != null)	direccion = nodo.getValor();
			// numero
			valor = "";
			nodo = (Nodo) datosFormulario.get(CAMPO_DIRECCION_NUMERO + ((respAdministrativaModificados) ? "_MOD" : ""));
			if(nodo != null)	direccion += "," + nodo.getValor();
			// piso
			valor = "";
			nodo = (Nodo) datosFormulario.get(CAMPO_DIRECCION_PISO + ((respAdministrativaModificados) ? "_MOD" : ""));
			if(nodo != null)	direccion += "," + nodo.getValor();
			if(!direccion.equals(""))	createAddPropiedad(seccion,"datosSolicitud.direccion",  direccion, widths);
			
			// CP
			createAddPropiedad(CAMPO_CP + ((respAdministrativaModificados) ? "_MOD" : ""), FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
					"datosSolicitud.cp", widths );
			
			// Municipio
			createAddPropiedad(CAMPO_MUNICIPIO + ((respAdministrativaModificados) ? "_MOD" : ""), FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
					"datosSolicitud.municipio", widths );

			docPDF.addSeccion(seccion);

			
	    	// SECCION DATOS DEL PACIENTE
			
	    	seccion = new Seccion(letras[numSecciones],props.getProperty("datosSolicitud.titulo" + letras[numSecciones]));
			numSecciones++;

			// Nombre Paciente
			nodo = (Nodo) datosFormulario.get(CAMPO_DATOS_PACIENTE_NOMBRE);
			if(nodo != null)	nombre = nodo.getValor() + " ";
			// Apellido1 Paciente
			nodo = (Nodo) datosFormulario.get(CAMPO_DATOS_PACIENTE_APELLIDO1);
			if(nodo != null)	nombre += nodo.getValor() + " ";
			// Apellido2 Paciente
			nodo = (Nodo) datosFormulario.get(CAMPO_DATOS_PACIENTE_APELLIDO2);
			if(nodo != null)	nombre += nodo.getValor();
			createAddPropiedad(seccion,"datosSolicitud.paciente.nombre",  nombre, widths);

			
			// Fecha de Nacimiento del Paciente
			createAddPropiedad(CAMPO_DATOS_PACIENTE_FECHA, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
	   		   		   "datosSolicitud.paciente.fechanac", widths );

			// DNI
			createAddPropiedad(CAMPO_DATOS_PACIENTE_NIF, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
	   		   		   "datosSolicitud.paciente.nif", widths );
			
			// Numero de Tarjeta Sanitaria
			createAddPropiedad(CAMPO_DATOS_PACIENTE_NSS, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
	   		   		   "datosSolicitud.paciente.tarjeta", widths );

			
	    	docPDF.addSeccion(seccion);

	    	// DATOS DEL ESCRITO QUE PRESENTA AL DEFENSOR DEL PACIENTE
			
	    	seccion = new Seccion(letras[numSecciones],props.getProperty("datosSolicitud.titulo" + letras[numSecciones]));
			numSecciones++;

			// Para que no se parta el campo
			seccion.setSplitLate(false);

			// idioma
			
			valor = getDatosCampo(CAMPO_IDIOMA + ((respAdministrativaModificados) ? "_MOD" : ""), FormateadorPdfFormularioQueixesDefPac.T_PAR);
			if(valor != null)
			{
				createAddPropiedad(seccion,"datosSolicitud.idioma",  props.getProperty("datosSolicitud.idioma" + valor), widths);
			}

			// medio
			valor = getDatosCampo(CAMPO_MEDIO + ((respAdministrativaModificados) ? "_MOD" : ""), FormateadorPdfFormularioQueixesDefPac.T_PAR);
			if(valor != null)
			{
				createAddPropiedad(seccion,"datosSolicitud.medio",  props.getProperty("datosSolicitud.medio" + valor), widths);
			}

			// Fecha de Entrada
			createAddPropiedad(seccion,"datosSolicitud.fechaEntrada", sdf.format(now),widths );
			
			// Centro
			createAddPropiedad(CAMPO_DP_CENTRO_IBS, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
	   		   		   		   "datosSolicitud.centro", widths );
			// Título
			createAddPropiedad(CAMPO_DP_TITULO, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
	   		   		   		   "datosSolicitud.titulo", widths );
			// Descripcion
			createAddPropiedad(CAMPO_DP_DETALLES, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
	   		   		   		   "datosSolicitud.descripcion", widths );


	    	docPDF.addSeccion(seccion);


	    	// DATOS DEL ESCRITO PRESENTADO AL IB-SALUT
			
	    	seccion = new Seccion(letras[numSecciones],props.getProperty("datosSolicitud.titulo" + letras[numSecciones]));
			numSecciones++;
			
			// Para que no se parta el campo
			seccion.setSplitLate(false);


			// Numero de Expediente
			createAddPropiedad(CAMPO_DP_EXPEDIENTE, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
					   		   "datosSolicitud.numeroExpediente", widths );

			// Tipo de Expediente
			createAddPropiedad(CAMPO_IB_ESCRITOS, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
					   		   "datosSolicitud.tipoExpediente", widths );
			
			// Fecha de Entrada
			createAddPropiedad(CAMPO_DP_FECHA_ENTRADA_IBS, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
					   		   "datosSolicitud.fechaEntrada", widths );
			
			// Fecha de Finalizacion
			createAddPropiedad(CAMPO_DP_FECHA_FINALIZACION_IBS, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
					   		   "datosSolicitud.fechaFinalizacion", widths );


			// Centro
			createAddPropiedad(CAMPO_DP_CENTRO_IBS, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
	   		   		   		   "datosSolicitud.centro", widths );

			// Titulo
			createAddPropiedad(CAMPO_DP_TITULO_IBS, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
	   		   		   		   "datosSolicitud.titulo", widths );
			

			// Estado
			createAddPropiedad(CAMPO_DP_ESTADO_IBS, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
	   		   		   		   "datosSolicitud.estado", widths );
			
			// Descripcion IBS
			createAddPropiedad(CAMPO_DP_DETALLES_IBS, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
	   		   		   		   "datosSolicitud.descripcion", widths );

			// Respuesta IBS
			createAddPropiedad(CAMPO_DP_RESPUESTA_IBS, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
	   		   		   		   "datosSolicitud.respuesta", widths );


	    	docPDF.addSeccion(seccion);


		}
		else
		{
	    	// SECCION DATOS DEL SOLICITANTE
			
	    	seccion = new Seccion(letras[numSecciones++],props.getProperty("datosSolicitud.titulo" + letras[numSecciones]));
	    	
			createAddPropiedad(CAMPO_DP_TIPO_ESCRITO, FormateadorPdfFormularioQueixesDefPac.T_PAR,seccion,
					   "datosSolicitud.tipo", widths );
			
			 valor = getDatosCampo(CAMPO_DP_TIPO_ESCRITO,FormateadorPdfFormularioQueixesDefPac.T_PAR);
			 if(valor != null)
			 {
				 if(valor.equals("Suggeriment")) sugerencia = true;
			 }


			// NIF Solicitante
			createAddPropiedad(CAMPO_DP_NUMERO, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
        	   		           "datosSolicitud.nif", widths );
			
			// Nombre Solicitante
			nodo = (Nodo) datosFormulario.get(CAMPO_DP_NOMBRE);
			if(nodo != null)	nombre = nodo.getValor() + " ";
			// Apellido1 Solicitante
			nodo = (Nodo) datosFormulario.get(CAMPO_DP_APELLIDO1);
			if(nodo != null)	nombre += nodo.getValor() + " ";
			// Apellido2 Solicitante
			nodo = (Nodo) datosFormulario.get(CAMPO_DP_APELLIDO2);
			if(nodo != null)	nombre += nodo.getValor();
			createAddPropiedad(seccion,"datosSolicitud.nombre",  nombre, widths);
			// Telefono1
			createAddPropiedad(CAMPO_DP_TELEFONO1_SEXP, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
	   		           "datosSolicitud.telefono1", widths );
			// Telefono2
			createAddPropiedad(CAMPO_DP_TELEFONO2_SEXP, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
	   		           "datosSolicitud.telefono2", widths );

			valor = getDatosCampo(CAMPO_DP_MEDIO, FormateadorPdfFormularioQueixesDefPac.T_PAR);
			if(valor.equals("email")) mail =  true;

			// email
			createAddPropiedad(CAMPO_DP_MAIL, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
					"datosSolicitud.mail", widths );
			
			// direccion
			nodo = (Nodo) datosFormulario.get(CAMPO_DP_DIRECCION);
			String direccion = "";
			if(nodo != null)	direccion = nodo.getValor();
			// numero
			valor = "";
			nodo = (Nodo) datosFormulario.get(CAMPO_DP_DIRECCION_NUMERO);
			if(nodo != null)	direccion += "," + nodo.getValor();
			// piso
			valor = "";
			nodo = (Nodo) datosFormulario.get(CAMPO_DP_DIRECCION_PISO);
			if(nodo != null)	direccion += "," + nodo.getValor();
			if(!direccion.equals(""))	createAddPropiedad(seccion,"datosSolicitud.direccion",  direccion, widths);
			
			// CP
			createAddPropiedad(CAMPO_DP_CP, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
					"datosSolicitud.cp", widths );
			
			// Municipio
			createAddPropiedad(CAMPO_DP_MUNICIPIO, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
					"datosSolicitud.municipio", widths );
			
	    	docPDF.addSeccion(seccion);


	    	if(!sugerencia)
	    	{
	    		// SECCION DATOS DEL PACIENTE
	    		
	    		seccion = new Seccion(letras[numSecciones++],props.getProperty("datosSolicitud.titulo" + letras[numSecciones]));
	    		
	    		nombre = null;
	    		
	    		// Nombre Paciente
	    		nodo = (Nodo) datosFormulario.get(CAMPO_DP_PACIENTE_NOMBRE);
	    		if(nodo != null)	nombre = nodo.getValor() + " ";
	    		// Apellido1 Paciente
	    		nodo = (Nodo) datosFormulario.get(CAMPO_DP_PACIENTE_APELLIDO1);
	    		if(nodo != null)	nombre += nodo.getValor() + " ";
	    		// Apellido2 Paciente
	    		nodo = (Nodo) datosFormulario.get(CAMPO_DP_PACIENTE_APELLIDO2);
	    		if(nodo != null)	nombre += nodo.getValor();
	    		createAddPropiedad(seccion,"datosSolicitud.paciente.nombre",  nombre, widths);
	    		
	    		
	    		// Fecha de Nacimiento del Paciente
	    		createAddPropiedad(CAMPO_DP_PACIENTE_FECHA, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
	    				"datosSolicitud.paciente.fechanac", widths );
	    		
	    		// DNI
	    		createAddPropiedad(CAMPO_DP_PACIENTE_NIF, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
	    				"datosSolicitud.paciente.nif", widths );
	    		
	    		// Numero de Tarjeta Sanitaria
	    		createAddPropiedad(CAMPO_DP_PACIENTE_NSS, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
	    				"datosSolicitud.paciente.tarjeta", widths );
	    		
	    		
	    		docPDF.addSeccion(seccion);
	    	}

	    	// DATOS DEL ESCRITO QUE PRESENTA AL DEFENSOR DEL PACIENTE

	    	if(sugerencia)
	    	{
	    		// Ojo, chapuza, para que no salga la seccion Datos del paciente
	    		// Ponemos sección B , pero con la propiedad de la seccion C
	    		seccion = new Seccion("B",props.getProperty("datosSolicitud.titulo" + letras[3]));
	    	}
	    	else
	    	{
		    	seccion = new Seccion(letras[numSecciones++],props.getProperty("datosSolicitud.titulo" + letras[numSecciones]));
	    	}

			// Para que no se parta el campo
			seccion.setSplitLate(false);

			// Fecha de Entrada
			createAddPropiedad(seccion,"datosSolicitud.fechaEntrada", sdf.format(now),widths );

			// idioma
			
			valor = getDatosCampo(CAMPO_DP_IDIOMA, FormateadorPdfFormularioQueixesDefPac.T_PAR);
			if(valor != null)
			{
				createAddPropiedad(seccion,"datosSolicitud.idioma",  props.getProperty("datosSolicitud.idioma" + valor), widths);
			}

			// medio
			valor = getDatosCampo(CAMPO_DP_MEDIO, FormateadorPdfFormularioQueixesDefPac.T_PAR);
			if(valor != null)
			{
				createAddPropiedad(seccion,"datosSolicitud.medio",  props.getProperty("datosSolicitud.medio" + valor), widths);
			}

			
			// Centro
			createAddPropiedad(CAMPO_DP_CENTRO2_IBS, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
	   		   		   		   "datosSolicitud.centro", widths );
			
			
			createAddPropiedad(CAMPO_DP_TITULO_QUEJAS, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
	   		   		   "datosSolicitud.titulo", widths );

			// Descripcion
			createAddPropiedad(CAMPO_DP_DETALLES2, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
	   		   		   		   "datosSolicitud.descripcion", widths );


	    	docPDF.addSeccion(seccion);


	    	if(!sugerencia)
	    	{
	    		// DATOS DEL ESCRITO PRESENTADO AL IB-SALUT
	    		
	    		seccion = new Seccion(letras[numSecciones++],props.getProperty("datosSolicitud.titulo" + letras[numSecciones]));
	    		
	    		// Para que no se parta el campo
	    		seccion.setSplitLate(false);
	    		
	    		
	    		// Centro
	    		createAddPropiedad(CAMPO_DP_CENTRO2_IBS, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
	    				"datosSolicitud.centro", widths );
	    		
	    		
	    		// Descripcion IBS
	    		createAddPropiedad(CAMPO_DP_DETALLES2_IBS, FormateadorPdfFormularioQueixesDefPac.T_NODO,seccion,
	    				"datosSolicitud.descripcion", widths );
	    		
	    		
	    		docPDF.addSeccion(seccion);
	    	}
		}


    	// Generamos pdf
    	ByteArrayOutputStream bos;
    	

		bos = new ByteArrayOutputStream();    		
		docPDF.generate(bos);
		
		ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());
		
		bais = new ByteArrayInputStream(bos.toByteArray());
		stampLogo(bos,bais,this.ID_LOGO_IBS,POSX_LOGO_IB,POSY_LOGOS);
		
		bais = new ByteArrayInputStream(bos.toByteArray());
		stampLogo(bos,bais,this.ID_LOGO_DP, POSX_LOGO_DP, POSY_LOGOS);
		
		bais.close();
		bos.close();
		
		
    	
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
	

	 private void createAddPropiedad(Seccion seccion, String key, String texto,float[] widths )
	 {
		 if(texto == null) return;
		 Propiedad propiedad = new Propiedad(props.getProperty(key),texto,widths);
		 seccion.addCampo(propiedad);
	 }

	 private void createAddPropiedad(String campo, String type, Seccion seccion, String key,float[] widths )
	 {
		 String valor = getDatosCampo(campo,type);
		 if(valor != null) createAddPropiedad(seccion,key,valor,widths);
	 }
	 
	 private String getDatosCampo(String campo, String type)
	 {
		 Nodo nodo = (Nodo) datosFormulario.get(campo);
		 if(nodo == null) return null;
		 if(type.equals(FormateadorPdfFormularioQueixesDefPac.T_NODO))
		 {
			 return nodo.getValor();
		 }
		 else
		 {
			 Par par = (Par)nodo.getAtributos().get(0);
			 if(par != null)
			 {
				 return par.getValor();
			 }
		 }
		 return null;
	 }


}
