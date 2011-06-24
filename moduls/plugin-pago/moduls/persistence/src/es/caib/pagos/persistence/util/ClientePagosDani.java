package es.caib.pagos.persistence.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.pagos.client.ClientePagos;
import es.caib.pagos.client.ResultadoInicioPago;
import es.caib.pagos.client.Tasa;
import es.caib.pagos.exceptions.ClienteException;
import es.caib.pagos.exceptions.DUINoPagadoException;
import es.caib.sistra.plugins.pagos.DatosPago;

/**
 * 
 * Operaciones con la pasarela de pagos
 *
 */
public class ClientePagosDani {

	private ClientePagosDani() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static ClientePagosDani getInstance(){
		return new ClientePagosDani();
	}
	
	public static ResultadoInicioPago inicioPago(Tasa tasa){
		ResultadoInicioPago rip = new ResultadoInicioPago();
		rip.setLocalizador("111111");
		rip.setToken("333-333-333");
		return rip;
	}
	
	public String comprobarPago(String localizador) throws DUINoPagadoException{
		return "-1";//"22222222222222";
	}
	
	public String imprimirTasaPagada(String localizador){
		return "";
	}
	
	public String getUrlInicioPago(String idioma, String token){
		return "http://www.google.es";
	}
	
	public String getUrlJustificantePago(String idioma, String token){
		return "http://indraweb.indra.es";
	}
	
	public Long getImporte(String tasa){
		return new Long(1000);
	}
//	ResultadoInicioPago res = ClientePagos.getInstance().inicioPago(tasa);	
//	String res = ClientePagos.getInstance().comprobarPago(localizador);	
//	String res ClientePagos.getInstance().imprimirTasaPagada(localizador);	
//	 String ClientePagos.getInstance().getUrlInicioPago(idioma,token);
//	 String ClientePagos.getInstance().getUrlJustificantePago(idioma,token);
	 
}
