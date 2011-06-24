package es.caib.pagos.exceptions;

import es.caib.pagos.client.ClientePagos;

/**
 * Excepción lanzada cuando el DUI que se pasa ya está pagado.
 * 
 * @author jcsoler
 *
 */

public class DUIPagadoException extends ClienteException {

	protected void init() {
		this.code = ClientePagos.ERROR_DUI_PAGADO;
	}


}
