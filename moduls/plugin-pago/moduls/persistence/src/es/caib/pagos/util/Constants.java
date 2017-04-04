package es.caib.pagos.util;

import java.io.Serializable;

/**
 * Constants globals.
 */
public class Constants implements Serializable {

    /**
     * Atributo de contexto donde se guarda el charset utilizado.
     */
    public static final String CHARSET = "UTF-8";
    
    /**
     * Clave para almacenar los valores de error
     */
    public static final String KEY_ERROR = "error";
    
    /**
     * Clave para almacenar los valores de token
     */
    public static final String KEY_TOKEN = "token";
    
    /**
     * Clave para almacenar los valores de localizador
     */
    public static final String KEY_LOCALIZADOR = "localizador";
    
    /**
     * Clave para almacenar los valores de importe
     */
    public static final String KEY_IMPORTE = "importe";
    
    /**
     * Clave para almacenar los valores de tasa
     */
    public static final String KEY_TASA = "tasa";
    
    /**
     * Clave para almacenar los valores de tasa
     */
    public static final String KEY_ID_TASA = "idtasa";
    
    /**
     * Clave para almacenar los valores de estado
     */
    public static final String KEY_ESTADO = "estado";
    
    /**
     * Clave para almacenar los valores de importe a ingresar
     */
    public static final String KEY_IMPORTE_INGRESAR = "importeaingresar";
    
    /**
     * Clave para almacenar los valores de nif del sujeto pasivo
     */
    public static final String KEY_NIF_SP = "nifsujetopasivo";
    
    /**
     * Clave para almacenar los valores de fecha de creación
     */
    public static final String KEY_FECHA_CREACION = "fechacreacion";
    
    /**
     * Clave para almacenar los valores de fecha de pago
     */
    public static final String KEY_FECHA_PAGO = "fechaPago";
    
    /**
     * Clave para almacenar los valores de resultado
     */
    public static final String KEY_RESULTADO = "resultado";
    
    /**
     * Clave para almacenar los valores de refsModelos
     */
    public static final String KEY_REFS_MODELOS = "refsModelos";
    
    /**
     * Clave para almacenar los valores de numero de tarjeta
     */
    public static final String KEY_NUM_TARJETA = "numeroTarjeta";
    
    /**
     * Clave para almacenar los valores de caducidad tarjeta
     */
    public static final String KEY_CAD_TARJETA = "caducidadTarjeta";
    
    /**
     * Clave para almacenar los valores de titular tarjeta
     */
    public static final String KEY_TIT_TARJETA = "titularTarjeta";
    
    /**
     * Clave para almacenar los valores de cvv tarjeta
     */
    public static final String KEY_CVV_TARJETA = "cvvTarjeta";
    
    /**
     * Clave para almacenar los valores de ref pago
     */
    public static final String KEY_REF_PAGO = "refPago";
    
    /**
     * Clave para almacenar los valores del código de la entidad bancaria
     */
    public static final String KEY_CODIGO_ENTIDAD = "codigoEntidad";
    
    
    /**
     * Clave para almacenar los valores de la url
     */
    public static final String KEY_URL = "url";
    
    /**
     * Clave para almacenar los valores de la firma
     */
    public static final String KEY_FIRMA = "firma";
    
    
    /**
     * Clave para almacenar los valores del justificante
     */
    public static final String KEY_JUSTIFICANTE = "justificante";
    
    /**
     * Clave para almacenar los valores del justificante
     */
    public static final String KEY_DATOS_TARJETA = "datosTarjeta";
    
    /**
     * Nombre del servicio soap
     */
	public static final String SERVICE_SOAP = "Service_TasaSoap";
	
	/**
	 * Namespace de los WS de la ATIB
	 */
	public static final String NAMESPACE_ATIB = "http://atib.es/";
	
	/**
	 * Part name del elemento UsuariosWebServices
	 */
	public static final String PARTNAME_USUARIOS_WEBSERVICES = "UsuariosWebServices";
	
	public static final String ESTADO_PAGADO = "OK";
	
	public static final String ESTADO_NO_PAGADO = "NK";
	
	public static final String FIRMA_SIMULADA = "FIRMA SIMULADA PARA PAGO SIMULADO";
	
}
