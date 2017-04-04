package es.caib.pagos.exceptions;


/**
 *  Clase para las excepciones producidas al crear el parametro de la tasa
 *  
 * 
 * @author ihdelpino
 *
 */

public class ParametroTasaException extends Exception {
	

	public ParametroTasaException() {
		super();
	
	}

	public ParametroTasaException(final String message) {
		super(message);
	
	}

	public ParametroTasaException(final String message, final Throwable cause) {
		super(message, cause);
	
	}

	public ParametroTasaException(final Throwable cause) {
		super(cause);
	
	}

}
