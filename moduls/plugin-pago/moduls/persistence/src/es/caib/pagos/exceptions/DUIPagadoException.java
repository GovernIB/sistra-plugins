package es.caib.pagos.exceptions;

import es.caib.pagos.client.ClientePagos;

/**
 * Excepci�n lanzada cuando el DUI que se pasa ya est� pagado.
 * 
 * @author jcsoler
 *
 */

public class DUIPagadoException extends ClienteException {

	protected void init() {
		this.code = ClientePagos.ERROR_DUI_PAGADO;
	}


}
