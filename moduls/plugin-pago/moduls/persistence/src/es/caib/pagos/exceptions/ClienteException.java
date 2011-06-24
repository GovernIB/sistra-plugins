package es.caib.pagos.exceptions;

import es.caib.pagos.client.ClientePagos;

/**
 *  Clase de la que heredar�n todas las posibles excepciones del Cliente de Pagos
 * 
 * @author jcsoler
 *
 */

public class ClienteException extends Exception {
	
	protected int code;

	public ClienteException() {
		super();
		init();
	}

	public ClienteException(String message) {
		super(message);
		init();
	}

	public ClienteException(String message, Throwable cause) {
		super(message, cause);
		init();
	}

	public ClienteException(Throwable cause) {
		super(cause);
		init();
	}
	
	protected void init()
	{
		this.code = ClientePagos.ERROR_GENERAL;
	}

	/**
	 * 
	 * Devuelve el C�digo del Error. Los c�digos est�n definidos en ClientePagos
	 * 
	 * @return
	 */
	
	public int getCode() {
		return code;
	}

	protected void setCode(int code) {
		this.code = code;
	}

}
