package es.caib.pagos.exceptions;

import es.caib.pagos.client.ClientePagos;

/**
 * Excepci�n lanzada cuando hay un error en la firma electr�nica.
 * 
 * @author jcsoler
 *
 */

public class FirmaException extends ClienteException {

	protected void init() {
		this.code = ClientePagos.ERROR_FIRMA;
	}


}
