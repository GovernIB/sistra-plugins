package es.caib.pagos.exceptions;


/**
 * Clase para las excepciones en el cifrado descifrado de datos
 * 
 * @author ihdelpino
 *
 */

public class CifradoException extends Exception {
	
	public CifradoException() {
		super();
	
	}
	
	public CifradoException(final String message) {
		super(message);
	}
	
	public CifradoException(final String message, final Throwable cause) {
		super(message, cause);
	
	}

}
