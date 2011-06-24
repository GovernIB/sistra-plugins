package es.caib.pagos.client;

import java.net.MalformedURLException;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.pagos.services.ImporteTasaService;

public class ImporteTasaAction implements WebServiceAction {

	private static Log log = LogFactory.getLog(ImporteTasaAction.class);
	


	public Hashtable execute(ClientePagos cliente, Hashtable data) throws Exception {
		Hashtable resultado = new Hashtable();
		String idTasa = (String)data.get("idtasa");

		ImporteTasaService importe = new ImporteTasaService(cliente.getUrl());
		String ls_resultado = null;
		try {
			ls_resultado = importe.execute(idTasa);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			resultado.put("error", new WebServiceError("Error al generar la URL del servicio ImporteTasa"));
		}

		log.debug("resultado importeTasa: " + ls_resultado);

		StringTokenizer st = new StringTokenizer(ls_resultado,"#");
		if(st.countTokens() == 2)
		{
			String value = (String) st.nextElement();
			resultado.put("estado",value);
			value = (String) st.nextElement();
			resultado.put("importe",value);
		}
		else
		{
			resultado.put("error", new WebServiceError(ls_resultado));
		}
		return resultado;
	}

}
