package es.caib.pagos.exceptions;


/**
 *  Clase para las excepciones del servicio inicio pago
 * 
 * @author ihdelpino
 *
 */

public class ComprobarPagoException extends Exception {
	

	public ComprobarPagoException() {
		super();
	
	}

	public ComprobarPagoException(String message) {
		super(message);
	
	}

	public ComprobarPagoException(String message, Throwable cause) {
		super(message, cause);
	
	}

	public ComprobarPagoException(Throwable cause) {
		super(cause);
	
	}

}
