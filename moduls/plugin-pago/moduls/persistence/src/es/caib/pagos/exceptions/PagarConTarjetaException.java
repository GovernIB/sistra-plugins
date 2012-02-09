package es.caib.pagos.exceptions;


/**
 * Clase para las excepciones del servicio de pago con tarjeta
 * 
 * @author ihdelpino
 *
 */

public class PagarConTarjetaException extends Exception {

	
	
	public PagarConTarjetaException() {
		super();
	
	}
	
	public PagarConTarjetaException(String message) {
		super(message);
	}
	
	public PagarConTarjetaException(String message, Throwable cause) {
		super(message, cause);
	
	}

	public PagarConTarjetaException(ClienteException cause) {
		super(cause);
	
	}


}
