package es.caib.pagos.client;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.pagos.exceptions.CifradoException;
import es.caib.pagos.exceptions.ClienteException;
import es.caib.pagos.persistence.delegate.DelegateException;
import es.caib.pagos.persistence.delegate.DelegateUtil;
import es.caib.pagos.util.Cifrar;
import es.caib.pagos.util.Constants;
import es.caib.signatura.utils.Base64;


/**
 * Clase interfaz para realizar todo el protocolo para Pagos Telematicos
 * 
 * @author jcsoler
 *
 */

public final class ClientePagos {

	private static Log log = LogFactory.getLog(ClientePagos.class);
	
	private static ClientePagos clientePagos = null;

	//WSActions
	private static final String IMPORTE_TASA = "importeTasa";
	private static final String INICIO_PAGO = "inicioPago";
	private static final String COMPROBAR_PAGO = "comprobarPago";
	private static final String GET_PDF_046 = "getPdf046";
	private static final String GET_URL_PAGO = "getUrlPago";
	private static final String PAGAR_CON_TARJETA = "pagarConTarjeta";
	
	//clave cifrar datos tarjeta

	private static final String CLAVE = "8B4tuXwn";
	private static final String INIT_VECTOR = "8B4tuXwn";
	
	private Hashtable actions = null;
	private String url = "";

	/** 
	 * Constructor
	 * 
	 */ 
	private ClientePagos(){		
	}
	
	/**
	 * Singleton del cliente de pagos
	 * @throws DelegateException 
	 *
	 */
	public static ClientePagos getInstance() throws DelegateException {
		if (clientePagos == null){
			clientePagos = new ClientePagos();
			clientePagos.init();						
		}
		return clientePagos;		
	}
	
	/**
	 * Metodo que inicializa las acciones que se pueden hacer con el Cliente de Pagos.
	 * 
	 * Inicializa la url a la que se enviarán las peticiones del WebService. Dicho valor se coge
	 * del fichero ClientePagos.properties
	 * @throws DelegateException 
	 *
	 */
	protected void init() throws DelegateException 
	{ 
		final String fase = DelegateUtil.getConfiguracionDelegate().obtenerPropiedad("fase");
		this.url = DelegateUtil.getConfiguracionDelegate().obtenerPropiedad("url." + fase);
				
		this.actions = new Hashtable();
		this.actions.put(IMPORTE_TASA,new ImporteTasaAction()); //ConsultaDatosTasa046
		this.actions.put(INICIO_PAGO,new InicioPagoAction()); //Inserta046
		this.actions.put(COMPROBAR_PAGO,new ComprobarPagoAction()); //Estado046
		this.actions.put(GET_PDF_046,new GetPdf046Action());
		this.actions.put(GET_URL_PAGO,new GetUrlPagoAction());
		this.actions.put(PAGAR_CON_TARJETA,new PagarConTarjetaAction());
	
	}
	
	/**
	 * Llama a la operación ConsultaDatosTasa046
	 * Servicio para Saber el Importe de una Tasa
	 * @param tasa
	 * @return Devuelve un Long con el valor de la Tasa. 
	 * @throws ClienteException
	 */
	public Long getImporte(final String tasa) throws ClienteException
	{		
	
		final Hashtable data = new Hashtable();
		data.put(Constants.KEY_ID_TASA, tasa);
		log.debug("importeTasa (" + tasa + ")");
		final Hashtable results = ejecutarAccion(data, IMPORTE_TASA);

		String ls_importe = (String) results.get(Constants.KEY_IMPORTE);
		final int idx = ls_importe.indexOf(',');
		if(idx != -1) // Proteccion
		{
			ls_importe = ls_importe.substring(0,idx);
		}
		return Long.valueOf(ls_importe);
		
	} 
	
	/**
	 * Servicio que Inicia el Pago Telematico
	 * @param tasa
	 * @return Devuelve una instancia de la clase ResultadoInicioPago 
	 * que haya cualquier error
	 * @throws ClienteException, WebServiceException
	 * 
	 */
	public ResultadoInicioPago inicioPago(final Tasa tasa) throws ClienteException
	{
		
		final Hashtable data = new Hashtable();
		data.put(Constants.KEY_TASA, tasa);
		log.debug("inicioPago (" + tasa.getIdTasa() + ")");
		
		final Hashtable results = ejecutarAccion(data, INICIO_PAGO);
		
		final String ls_localizador = (String) results.get(Constants.KEY_LOCALIZADOR);
		final String ls_token = (String) results.get(Constants.KEY_TOKEN);
		final ResultadoInicioPago resultado = new ResultadoInicioPago(ls_token,ls_localizador);

		return resultado;
		
	}
	
	/**
	 * Servicio para comprobar un Pago a través del localizador
	 * @param localizador
	 * @return Devuelve un hashtable con la fecha de pago, el localizador y la firma
	 * @throws ClienteException
	 */
	public Hashtable comprobarPago(final String localizador) throws ClienteException
	{
		final Hashtable data = new Hashtable();
		data.put(Constants.KEY_LOCALIZADOR, localizador);
		log.debug("comprobarPago (" + localizador + ")");
		return ejecutarAccion(data, COMPROBAR_PAGO);
	}
	

	/**
	 * Servicio para obtener el PDF de carta de pago del modelo 046
	 * @param localizador
	 * @param importe
	 * @param nif
	 * @param fecha
	 * @return
	 * @throws ClienteException
	 */
	public byte[] getPdf046(final String localizador, final String importe, final String nif, 
			final String fecha) throws ClienteException
	{
	
		final Hashtable data = new Hashtable();
		data.put(Constants.KEY_LOCALIZADOR, localizador);
		data.put(Constants.KEY_IMPORTE_INGRESAR, importe);
		data.put(Constants.KEY_NIF_SP, nif);
		data.put(Constants.KEY_FECHA_CREACION, fecha); 
		
		log.debug(GET_PDF_046);
		log.debug("Localizador: " + localizador);
		log.debug("importeaingresar: " + importe);
		log.debug("nifsujetopasivo: " + nif);
		log.debug("fechacreacion: " + fecha);
		
		final Hashtable results = ejecutarAccion(data, GET_PDF_046);
		
		return (byte[])results.get(Constants.KEY_RESULTADO);

	}
	
	/**
	 * Servicio para realizar el pago con tarjeta bancaria
	 * @param refsModelos
	 * @param numeroTarjeta
	 * @param caducidadTarjeta
	 * @param titularTarjeta
	 * @param cvvTarjeta
	 * @return
	 * @throws ClienteException
	 */
	public boolean pagarConTarjeta(final String[] refsModelos, final String numeroTarjeta, 
			final String caducidadTarjeta, final String titularTarjeta, final String cvvTarjeta) throws ClienteException
	{
		
		final Hashtable data = new Hashtable();
		data.put(Constants.KEY_REFS_MODELOS, refsModelos);
		
		//encriptamos los datos de la tarjeta
		//3 por los tres #
		final int longitud = 3 + numeroTarjeta.length() + caducidadTarjeta.length() + titularTarjeta.length() + cvvTarjeta.length();
		final StringBuilder sb = new StringBuilder(longitud);
		
		sb.append(numeroTarjeta);
		sb.append("#");
		sb.append(caducidadTarjeta);
		sb.append("#");
		sb.append(titularTarjeta);
		sb.append("#");
		sb.append(cvvTarjeta);

		final String datosTarjeta = sb.toString();
		
		try {
			final byte[] datosCifrados = Cifrar.cifrar(datosTarjeta.getBytes(Constants.CHARSET), CLAVE, INIT_VECTOR);
			data.put(Constants.KEY_DATOS_TARJETA, Base64.encodeBytes(datosCifrados));
		} catch (UnsupportedEncodingException ue) { 
			//TODO revisar mensaje
			throw new ClienteException(WebServiceError.ERROR_CIFRADO, "Error durante el cifrado de datos de la tarjeta", ue);
		} catch (CifradoException ce) {
			//TODO revisar mensaje
			throw new ClienteException(WebServiceError.ERROR_CIFRADO, "Error durante el cifrado de datos de la tarjeta", ce);
		}

		log.debug(PAGAR_CON_TARJETA);
		
		final Hashtable results = ejecutarAccion(data, PAGAR_CON_TARJETA);
	
		return Constants.ESTADO_PAGADO.equals((String)results.get(Constants.KEY_RESULTADO));
		
	}

	/**
	 * Servicio para obtener la url de la entidad bancaria para realizar el pago por banca online
	 * @param refsModelos
	 * @param codigoEntidad
	 * @return
	 * @throws ClienteException
	 */
	public Hashtable getUrlPago(final String[] refsModelos, final String codigoEntidad, final String urlVuelta) throws ClienteException
	{
		
		final Hashtable data = new Hashtable();
		data.put(Constants.KEY_REFS_MODELOS, refsModelos);
		data.put(Constants.KEY_CODIGO_ENTIDAD, codigoEntidad);
		data.put(Constants.KEY_URL, urlVuelta);

		log.debug(GET_URL_PAGO);
		log.debug("Modelo: " + refsModelos[0]);
		log.debug("Codigo entidad: " + codigoEntidad);
		log.debug("Codigo entidad: " + urlVuelta);

		final Hashtable results = ejecutarAccion(data, GET_URL_PAGO);
	
		return results;

	}
	
	/**
	 * Ejecuta una acción del WS
	 * @param data Parámetros de la acción
	 * @param accion Nombre de la acción a ejecutar
	 * @return Resultado de la ejecución
	 * @throws ClienteException 
	 */
	private Hashtable ejecutarAccion(final Hashtable data, final String accion) throws ClienteException {
		final WebServiceAction action = (WebServiceAction) actions.get(accion);
		final Hashtable results = action.execute(this,data);
	
		if(results != null && results.containsKey(Constants.KEY_ERROR))
		{
			final WebServiceError error = (WebServiceError) results.get(Constants.KEY_ERROR);
			throw new ClienteException(error.getCodigo(), error.getError());
		}
		return results;
	}
	
	
	/**
	 * Metodo que devuelve la URL del WebService
	 * @return
	 */

	public String getUrl() {
		return url;
	}

	/**
	 * Metodo para establecer la URL del WebService
	 * @param url
	 */
	public void setUrl(final String url) {
		this.url = url;
	}

	
	
}
