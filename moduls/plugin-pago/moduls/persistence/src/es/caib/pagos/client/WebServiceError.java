package es.caib.pagos.client;

public class WebServiceError {

	//C�digos de error en la respuesta del WS definidos por la ATIB
	
//	public static final int ERROR_WS_NE_LOCALIZADOR = 2;
//	public static final int ERROR_WS_LOCALIZADOR_PAGADO = 3;
//	public static final int ERROR_WS_CTO_TASA = 4;
//	public static final int ERROR_WS_USUARIO = 6;
//	public static final int ERROR_WS_NIF_OBLIGATORIO = 7;
//	public static final int ERROR_WS_NOMBRE_OBLIGATORIO = 8;
//	public static final int ERROR_WS_IMP_TASA = 9;
//	public static final int ERROR_WS_FORMATO_NIF = 10;
//	public static final int ERROR_WS_NE_TASA = 20;
//	public static final int ERROR_WS_ID_TASA = 21;
	
	//C�digo de error cuando se produce un error en el pago con tarjeta. La ATIB no lo codifica
	public static final int ERROR_TARJETA = 90;
	//C�digo de error cuando la firma del mensaje recibido no es correcta
	public static final int ERROR_FIRMA = 91;
	//C�digos de error
	public static final int ERROR_PROPERTIES = 92; //C�digo de error al acceder al fichero de properties
	public static final int ERROR_COMUNICACION = 93;
	public static final int ERROR_RESPUESTA_NULA = 94;
	public static final int ERROR_FECHA_PAGO = 95; //C�digo error al convertir fecha de pago
	public static final int ERROR_MSG_XML = 96; //C�digo error al crear mensaje XML
	public static final int ERROR_INDETERMINADO = 99; //C�digo error indeterminado
	public static final int ERROR_CIFRADO = 100; //C�digo error al cifrar
	
	private int codigo;
	private String error = null;
	
	public WebServiceError(final int codigo, final String error) {
		super();
		this.codigo = codigo;
		this.error = error;
	}
	
	public WebServiceError(final Integer codigo, final String error) {
		super();
		this.codigo = codigo.intValue();
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setError(final String error) {
		this.error = error;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(final int codigo) {
		this.codigo = codigo;
	}
	
	
	

}
