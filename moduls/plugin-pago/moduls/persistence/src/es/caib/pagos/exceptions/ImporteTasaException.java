package es.caib.pagos.exceptions;


/**
 * Clase para las excepciones del servicio que obtiene el importe de una tasa
 * 
 * @author ihdelpino
 *
 */

public class ImporteTasaException extends Exception {

	
	public ImporteTasaException() {
		super();
	
	}
	
	public ImporteTasaException(String message) {
		super(message);
	}
	
	public ImporteTasaException(String message, Throwable cause) {
		super(message, cause);
	
	}

	public ImporteTasaException(Throwable cause) {
		super(cause);
	
	}


}
