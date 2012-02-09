package es.caib.pagos.exceptions;


/**
 * Clase para las excepciones del servicio que obtiene la url para el pago por banca online
 * 
 * @author ihdelpino
 *
 */

public class GetUrlPagoException extends Exception {

	
	
	public GetUrlPagoException() {
		super();
	
	}
	
	public GetUrlPagoException(String message) {
		super(message);
	}
	
	public GetUrlPagoException(String message, Throwable cause) {
		super(message, cause);
	
	}

	public GetUrlPagoException(ClienteException cause) {
		super(cause);
	
	}


}
