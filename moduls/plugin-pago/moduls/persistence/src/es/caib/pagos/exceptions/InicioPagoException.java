package es.caib.pagos.exceptions;


/**
 *  Clase para los errores del servicio inicio pago
 * 
 * @author ihdelpino
 *
 */

public class InicioPagoException extends Exception {
	

	public InicioPagoException() {
		super();
	
	}

	public InicioPagoException(String message) {
		super(message);
	
	}

	public InicioPagoException(String message, Throwable cause) {
		super(message, cause);
	
	}

	public InicioPagoException(Throwable cause) {
		super(cause);
	
	}

}
