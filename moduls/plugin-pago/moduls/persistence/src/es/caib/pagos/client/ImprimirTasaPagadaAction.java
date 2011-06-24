package es.caib.pagos.client;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.xml.rpc.ServiceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.pagos.services.ImprimirTasaPagadaService;

public class ImprimirTasaPagadaAction implements WebServiceAction {

	private static Log log = LogFactory.getLog(ImprimirTasaPagadaAction.class);


	public Hashtable execute(ClientePagos cliente, Hashtable data) throws Exception {
		Hashtable resultado = new Hashtable();
		String localizador = (String)data.get("localizador");
		
		ImprimirTasaPagadaService imprimir = new ImprimirTasaPagadaService(cliente.getUrl());
		String ls_resultado = null;
		try {
			ls_resultado = imprimir.execute(localizador);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			resultado.put("error", new WebServiceError("Error al generar la URL del servicio ImprimirTasaPagada"));
		}

		log.debug("Resultado ImprimirTasaPagada: " + ls_resultado);

		StringTokenizer st = new StringTokenizer(ls_resultado,"#");
		if(st.countTokens() == 3)
		{
			String value = (String) st.nextElement();
			resultado.put("estado",value);
			value = (String) st.nextElement();
			resultado.put("localizador",value);
			value = (String) st.nextElement();
			resultado.put("token",value);
		}
		else
		{
			resultado.put("error", new WebServiceError(ls_resultado));
		}
		return resultado;
	}

}
