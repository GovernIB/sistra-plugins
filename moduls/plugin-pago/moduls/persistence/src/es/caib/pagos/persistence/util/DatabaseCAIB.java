package es.caib.pagos.persistence.util;

import java.util.HashMap;
import java.util.Map;

import es.caib.pagos.model.SesionPagoCAIB;
import es.caib.pagos.model.TokenAccesoCAIB;

/**
 * Simula acceso a BBDD
 *
 */
public class DatabaseCAIB {

	private static Map sesionesPago = new HashMap();
	
	private static Map tokensAcceso = new HashMap();
	
	
	public static void guardarSesionPago(SesionPagoCAIB sesionPago){
		sesionesPago.put(sesionPago.getLocalizador(),sesionPago);
	}
	
	public static SesionPagoCAIB obtenerSesionPago(String localizador){
		return (SesionPagoCAIB) sesionesPago.get(localizador);
	}
	
	public static void borrarSesionPago(String localizador){
		if (sesionesPago.containsKey(localizador)){
			sesionesPago.remove(localizador);
		}
	}
	
	
	public static void guardarTokenAcceso(TokenAccesoCAIB token){
		tokensAcceso.put(token.getToken(),token);
	}
	
	public static TokenAccesoCAIB obtenerTokenAcceso(String token){
		return (TokenAccesoCAIB) tokensAcceso.get(token);
	}
	
	public static void borrarTokenAcceso(String token){
		if (tokensAcceso.containsKey(token)){
			tokensAcceso.remove(token);
		}
	}
	
	
}
