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
public class PasarelaPagos {
	
	private static Log log = LogFactory.getLog(PasarelaPagos.class);
	
	/**
	 * Inicia sesión de pagos
	 * 
	 * @param datosPago
	 * @return Token de acceso y localizador
	 * @throws Exception 
	 * @throws ClienteException 
	 * @throws Exception
	 */
	public static ResultadoInicioPago iniciarSesionPagos(DatosPago datosPago, String tipo) throws Exception{ 	
		// Construimos tasa
		Tasa tasa = new Tasa();		
		tasa.setTipoPago(tipo);
		tasa.setModelo(datosPago.getModelo());
		tasa.setIdTasa(datosPago.getIdTasa());
		tasa.setImporte(datosPago.getImporte());        		
		tasa.setConcepto(datosPago.getConcepto());
		tasa.setNif(datosPago.getNifDeclarante()); 
		tasa.setNombre(datosPago.getNombreDeclarante());
		 
		// Iniciamos pago
		ResultadoInicioPago res = ClientePagos.getInstance().inicioPago(tasa);
		return res;	
	}
	
	/**
	 * Comprueba pago a través del localizador
	 *  
	 * @param datosPago
	 * @return Justificante de pago
	 * @throws Exception
	 */	
	public static String comprobarPago(String localizador) throws ClienteException{ 		
		//  Comprobamos pago
		String res;
		try{
			res = ClientePagos.getInstance().comprobarPago(localizador);			
		}catch(DUINoPagadoException cex){
			log.debug("Localizador no ha sido pagado");
			res = "-1";
		}catch(Exception ex){
			log.error("Error comprobando pago",ex);
			res = "-2";
		}
		
		// Devolvemos resultado
		log.debug("ComprobarPago para localizador = " + localizador + ". Resultado = " + res);
		return res;
	}
	
	/**
	 * 
	 * ESTA FUNCION YA NO SE UTILIZA
	 * 
	 * Obtiene token para acceder al justificante
	 *  
	 * @param localizador
	 * @return Token acceso. En caso de error: null
	 * 
	 
	public static String justificantePago(String localizador){ 		
		try{			
			String res = ClientePagos.getInstance().imprimirTasaPagada(localizador);				
			return res;
		}catch(Exception ex){
			return null;
		}
	}
	*/
	
	/**
	 * Devuelve url acceso inicio pago a la cual se debe redirigir el navegador tras
	 * iniciar sesión de pagos
	 * @param token
	 * @return Url
	 */
	public static String getUrlInicioPago(String idioma,String token) throws Exception{
		return ClientePagos.getInstance().getUrlInicioPago(idioma,token);
	}
	
	/**
	 * Devuelve url acceso justificante pago a la cual se debe redirigir el navegador tras
	 * pedir justificante pago
	 * @param token
	 * @return Url
	 */
	public static String getUrlJustificantePago(String idioma,String token) throws Exception{
		return ClientePagos.getInstance().getUrlJustificantePago(idioma,token);		
	}
	
	/**
	 * Función que permite calcular el importe de una tasa
	 * @param idTasa
	 * @return Importe tasa en cents
	 * @throws Exception 
	 * @throws ClienteException 
	 */
	public static long consultarImporteTasa(String idTasa) throws Exception{
		Long res = ClientePagos.getInstance().getImporte(idTasa);						
		return res.longValue();							
	}
}
