package es.caib.pagos.persistence.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.pagos.client.ClientePagos;
import es.caib.pagos.client.ResultadoInicioPago;
import es.caib.pagos.client.Tasa;
import es.caib.pagos.exceptions.ClienteException;
import es.caib.pagos.exceptions.ComprobarPagoException;
import es.caib.pagos.exceptions.GetPdf046Exception;
import es.caib.pagos.exceptions.GetUrlPagoException;
import es.caib.pagos.exceptions.ImporteTasaException;
import es.caib.pagos.exceptions.InicioPagoException;
import es.caib.pagos.exceptions.PagarConTarjetaException;
import es.caib.pagos.persistence.delegate.DelegateException;
import es.caib.pagos.util.Constants;
import es.caib.sistra.plugins.pagos.DatosPago;

/**
 * 
 * Operaciones con la pasarela de pagos
 *
 */
public class PasarelaPagos {
	
	private static Log log = LogFactory.getLog(PasarelaPagos.class);
	
	private static String ERROR_CLIENTE = "Error en el cliente de pagos.";
	private static String ERROR_WS = "Error en el WebService.";
	private static String ERROR_GET_CLIENTE = "Error al obtener ClientePagos.";
	
	/**
	 * Inicia sesión de pagos
	 * 
	 * @param datosPago
	 * @return Token de acceso y localizador
	 * @throws InicioPagoException
	 */
	public static ResultadoInicioPago iniciarSesionPagos(DatosPago datosPago, String tipo) 
		throws  InicioPagoException{ 	
		// Construimos tasa
		Tasa tasa = new Tasa();		
		tasa.setTipoPago(tipo);
		tasa.setModelo(datosPago.getModelo());
		tasa.setIdTasa(datosPago.getIdTasa());
		tasa.setImporte(datosPago.getImporte());        		
		tasa.setConcepto(datosPago.getConcepto());
		tasa.setNif(datosPago.getNifDeclarante()); 
		tasa.setNombre(datosPago.getNombreDeclarante());
		 
		ResultadoInicioPago res = null;
		try {
			res = ClientePagos.getInstance().inicioPago(tasa);
		} catch (DelegateException de) { 
			log.error(de.getMessage());
			throw new InicioPagoException(de);
		} catch (ClienteException ce) {
			log.error(ce.getMessage());
			throw new InicioPagoException(ce);
		} 
		return res;	
	}
	
	/**
	 * Comprueba pago a través del localizador
	 *  
	 * @param datosPago
	 * @return fecha de realización del pago ( si se ha realizado) / null (en caso contrario)
	 * @throws ComprobarPagoException
	 */	
	public static Hashtable comprobarPago(String localizador) throws ComprobarPagoException{ 		
		
		Hashtable res = null;
		try {
			res = ClientePagos.getInstance().comprobarPago(localizador);			
		} catch (DelegateException de) { 
			log.error(ERROR_GET_CLIENTE + de.getMessage());
			throw new ComprobarPagoException(ERROR_GET_CLIENTE, de);
		} catch(ClienteException ce){
			log.error(ce.getMessage());
			throw new ComprobarPagoException(String.valueOf(ce.getCode()), ce);
		} 
		
		// Devolvemos resultado
		log.debug("ComprobarPago para localizador = " + localizador + ". Resultado = " + res);
		return res;
		
	}
	
	/**
	 * Función que permite obtener el importe de una tasa
	 * @param idTasa
	 * @return Importe tasa en cents
	 *  
	 * @throws ImporteTasaException 
	 * 
	 */
	public static long consultarImporteTasa(String idTasa) throws ImporteTasaException{
		
		try {
			Long res = ClientePagos.getInstance().getImporte(idTasa);
			return res.longValue();
		} catch (DelegateException d) {
			log.error(ERROR_GET_CLIENTE + d.getMessage());
			throw new ImporteTasaException(ERROR_GET_CLIENTE, d);
		} catch(ClienteException ce){
			log.error(ce.getMessage());
			throw new ImporteTasaException(String.valueOf(ce.getCode()), ce);
		} 
									
	}
	
	/**
	 * Función que permite obtener el PDF para entregar en el pago presencial
	 * @param localizador
	 * @param importe
	 * @param nif
	 * @param fecha
	 * @return Array de bytes con los datos del PDF
	 * @throws GetPdf046Exception
	 */
	public static byte[] getPdf046(String localizador, String importe, String nif, 
			Date fecha) throws GetPdf046Exception{
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String cadenaFecha = formato.format(fecha);
		// Importe (convertimos de cents)
		double impDec = Double.parseDouble(importe) / 100 ;	
		DecimalFormat f = (DecimalFormat) DecimalFormat.getInstance();
		f.setDecimalSeparatorAlwaysShown(true);
		f.setMaximumFractionDigits(2);
		f.setMinimumFractionDigits(2);
		f.setMinimumIntegerDigits(1);
		String importeDec = f.format(impDec);
		try {
			byte[] pdf = ClientePagos.getInstance().getPdf046(localizador, importeDec, nif, cadenaFecha);
			return pdf;
		} catch(DelegateException d) {
			log.error(ERROR_GET_CLIENTE + d.getMessage());
			throw new GetPdf046Exception(ERROR_GET_CLIENTE, d);
		} catch(ClienteException ce) {
			log.error(ERROR_CLIENTE + ce.getMessage());
			throw new GetPdf046Exception(String.valueOf(ce.getCode()), ce);
		} 
	}
	
	/**
	 * Funcion que permite realizar el pago mediante tarjeta bancaria
	 * @param refsModelos
	 * @param numeroTarjeta
	 * @param caducidadTarjeta
	 * @param titularTarjeta
	 * @param cvvTarjeta
	 * @return
	 * @throws PagarConTarjetaException
	 */
	public static boolean pagarConTarjeta(String[] refsModelos, String numeroTarjeta, 
			String caducidadTarjeta, String titularTarjeta, String cvvTarjeta) throws PagarConTarjetaException {
		
		try {
			boolean resultado = ClientePagos.getInstance().pagarConTarjeta(refsModelos, numeroTarjeta, caducidadTarjeta, titularTarjeta, cvvTarjeta);
			return resultado;
		} catch(DelegateException d) {
			log.error(ERROR_GET_CLIENTE + d.getMessage());
			throw new PagarConTarjetaException(ERROR_GET_CLIENTE, d);
		} catch(ClienteException ce) {
			log.error(ce.getMessage());
			throw new PagarConTarjetaException(ce);
		} 
	}
	
	/**
	 * Función que permite obtener la url para realizar el pago mediante banca online
	 * @param refsModelos
	 * @param codigoEntidad
	 * @return
	 * @throws GetUrlPagoException
	 */
	public static String getUrlPago(String[] refsModelos, String codigoEntidad, String urlVuelta) throws GetUrlPagoException{
		try {
			Hashtable res = ClientePagos.getInstance().getUrlPago(refsModelos, codigoEntidad, urlVuelta);
			//TODO pemndiente ver que hacemos con el refpago que nos llega
			return (String)res.get(Constants.KEY_URL);
		} catch (DelegateException d) {
			log.error(ERROR_GET_CLIENTE + d.getMessage());
			throw new GetUrlPagoException(ERROR_GET_CLIENTE, d);
		} catch(ClienteException ce){
			log.error(ce.getMessage());
			throw new GetUrlPagoException(String.valueOf(ce.getCode()), ce);
		} 
	}
	
}
