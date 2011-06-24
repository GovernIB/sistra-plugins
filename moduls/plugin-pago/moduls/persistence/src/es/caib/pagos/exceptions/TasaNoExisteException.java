package es.caib.pagos.exceptions;

import es.caib.pagos.client.ClientePagos;

/**
 * Excepción lanzada cuando la Tasa que se pasa no existe
 * 
 * @author jcsoler
 *
 */

public class TasaNoExisteException extends ClienteException {

	protected void init() {
		this.code = ClientePagos.ERROR_TASA_NO_EXISTE;
	}


}
