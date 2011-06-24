package es.caib.pagos.exceptions;

import es.caib.pagos.client.ClientePagos;

/**
 * Excepción lanzada cuando hay un error en la firma electrónica.
 * 
 * @author jcsoler
 *
 */

public class FirmaException extends ClienteException {

	protected void init() {
		this.code = ClientePagos.ERROR_FIRMA;
	}


}
