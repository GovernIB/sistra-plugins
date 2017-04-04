package es.caib.regtel.plugincaib.model;

import java.rmi.RemoteException;

/**
 * Excepción producida en el Registro : api web
 * 
 */
public class ExcepcionRegistroWeb extends RemoteException {
		
		public ExcepcionRegistroWeb() {
			super();
	    }

		public ExcepcionRegistroWeb(String message) {
			super(message);
	    }
		
	    public ExcepcionRegistroWeb(String message, Throwable cause) {
	        super(message, cause);
	    }
}
