package es.caib.pagos.exceptions;

import es.caib.pagos.client.ClientePagos;

/**
 * Excepción lanzada cuando el DUI no está pagado
 * 
 * @author jcsoler
 *
 */

public class DUINoPagadoException extends ClienteException {

	protected void init() {
		this.code = ClientePagos.ERROR_DUI_NO_PAGADO;
	}


}
