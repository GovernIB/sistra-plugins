package es.caib.pagos.client;

import java.util.Hashtable;

public interface WebServiceAction {
	
	public Hashtable execute(ClientePagos cliente, Hashtable data);

}
