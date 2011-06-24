package es.caib.pagos.exceptions;

import es.caib.pagos.client.ClientePagos;

/**
 * Excepci�n lanzada cuando el DUI no est� pagado
 * 
 * @author jcsoler
 *
 */

public class DUINoPagadoException extends ClienteException {

	protected void init() {
		this.code = ClientePagos.ERROR_DUI_NO_PAGADO;
	}


}
