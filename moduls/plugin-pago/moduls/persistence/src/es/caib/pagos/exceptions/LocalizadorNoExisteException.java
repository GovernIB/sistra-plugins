package es.caib.pagos.exceptions;

import es.caib.pagos.client.ClientePagos;

/**
 * Excepción lanzada cuando el Localizador que se pasa no existe
 * 
 * @author jcsoler
 *
 */

public class LocalizadorNoExisteException extends ClienteException {

	protected void init() {
		this.code = ClientePagos.ERROR_LOCALIZADOR_NO_EXISTE;
	}


}
