package es.caib.pagos.front;

import java.io.Serializable;

/**
 * Constants globals.
 */
public class Constants implements Serializable 
{
	/**
     * Atributo de contexto donde se guarda el lenguaje por defecto.
     */
    public static final String DEFAULT_LANG_KEY 	= "es.caib.pagos.front.DEFAULT_LANG";
    public static final String DEFAULT_LANG 		= "ca";	
    
    /**
     * Atributo de sesion donde se guarda la sesion de pagos
     */
	public static final String DATOS_SESION_KEY					= "es.caib.pagos.front.DATOS_SESION";
	
	/**
     * Atributo de sesion donde se guarda el idioma de la sesion de pagos
     */
	public static final String DATOS_SESION_IDIOMA_KEY 			= "es.caib.pagos.front.DATOS_SESION_IDIOMA_KEY";
	
	/**
     * Atributo de sesion donde se guarda el nombre de tramite de la sesion de pagos
     */
	public static final String DATOS_SESION_NOMBRE_TRAMITE_KEY	= "es.caib.pagos.front.DATOS_SESION_NOMBRE_TRAMITE_KEY";
	
	/**
     * Atributo de sesion donde se guarda la url de retorno a SISTRA
     */
	public static final String URL_RETORNO_SISTRA_KEY		= "es.caib.pagos.front.URL_RETORNO_SISTRA";
	
	 /**
     * Atributo de sesion donde se guarda la url de mantenimiento de SISTRA
     */
	public static final String URL_MANTENIMIENTO_SISTRA_KEY		= "es.caib.pagos.front.URL_MANTENIMIENTO_SISTRA_KEY";
	
	/**
	 * Atributo de request donde se guarda mensaje para fail
	 */
	public static String MESSAGE_KEY = "mensaje";
	public static String PAYMENT_URL = "urlAcceso";
	
	/**
	 * Atributo de request donde se guarda la informacion del Organismo
	 */
	public static String ORGANISMO_INFO_KEY = "informacionOrganismo";
	
	
	
	public static String NOMBREFICHERO_KEY = "nombrefichero";
	public static String DATOSFICHERO_KEY = "datosfichero";
	
	
}
