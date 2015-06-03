package es.caib.sistra.plugins.regtel.impl.caib;

/**
 * Constantes Regweb3.
 * @author Indra
 *
 */
public class ConstantesRegweb3 {
	
	/** Constante para indicar registro entrada. */
	public static final Long REGISTRO_ENTRADA = 1L;	
	/** Constante para indicar registro salida. */
	public static final Long REGISTRO_SALIDA = 2L;
	
	/** Tipo interesado Persona física. */
	public final static String TIPO_INTERESADO_PERSONA_FISICA = "01"; 
	/** Tipo interesado Persona juridica. */
	public final static String TIPO_INTERESADO_PERSONA_JURIDICA = "02";
	/** Tipo interesado Administración. */
	public final static String TIPO_INTERESADO_PERSONA_ADMINISTRACION = "03";
	
	/** Tipo documento identificacion: NIF. */
	public final static String TIPO_DOCID_NIF = "N";
	/** Tipo documento identificacion: NIF. */
	public final static String TIPO_DOCID_CIF = "C";
	
	/** Documetacion fisica: Acompaña documentación física (u otros soportes) requerida.*/
	public final static String DOC_FISICA_REQUERIDA = "01"; 		
	

	/** Tipo documento: formulario. */
	public final static String TIPO_DOCUMENTO_FORMULARIO = "01";
	/** Tipo documento: anexo. */
	public final static String TIPO_DOCUMENTO_ANEXO = "02";
	/** Tipo documento: fichero tecnico intermedio. */
	public final static String TIPO_DOCUMENTO_FICHERO_TECNICO = "03";
	
	
	/** Tipo documental: Comunicación. */
	public final static String TIPO_DOCUMENTAL_COMUNICACION = "TD06";
	/** Tipo documental: Notificación. */
	public final static String TIPO_DOCUMENTAL_NOTIFICACION = "TD07"; 
	/** Tipo documental: Acuse de recibo. */
	public final static String TIPO_DOCUMENTAL_ACUSE_RECIBO = "TD09";
	/** Tipo documental: Informe. */
	public final static String TIPO_DOCUMENTAL_INFORME = "TD13";
	/** Tipo documental: Solicitud. */
	public final static String TIPO_DOCUMENTAL_SOLICITUD = "TD14";
	/** Tipo documental: Otros. */
	public final static String TIPO_DOCUMENTAL_OTROS = "TD99";
	
	
	
	/** Origen documento: ciudadano. */
	public final static Integer ORIGEN_DOCUMENTO_CIUDADANO = new Integer(0);
	/** Origen documento: administracion. * */
	public final static Integer ORIGEN_DOCUMENTO_ADMINISTRACION = new Integer(1);
	
	/** Modo firma: sin firma. */
	public final static Integer MODO_FIRMA_SIN_FIRMA = new Integer(0);
	/** Modo firma: attached. */
	public final static Integer MODO_FIRMA_ATTACHED = new Integer(1);
	/** Modo firma: detached. */
	public final static Integer MODO_FIRMA_DETACHED = new Integer(2);
	
}
