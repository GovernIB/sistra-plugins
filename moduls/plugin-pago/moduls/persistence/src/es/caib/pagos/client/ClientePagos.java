package es.caib.pagos.client;

import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.pagos.exceptions.ClienteException;
import es.caib.pagos.exceptions.DUINoPagadoException;
import es.caib.pagos.exceptions.DUIPagadoException;
import es.caib.pagos.exceptions.FirmaException;
import es.caib.pagos.exceptions.LocalizadorNoExisteException;
import es.caib.pagos.exceptions.TasaNoExisteException;
import es.caib.pagos.persistence.delegate.DelegateUtil;


/**
 * Clase interfaz para realizar todo el protocolo para Pagos Telematicos
 * 
 * @author jcsoler
 *
 */

public class ClientePagos {

	private static Log log = LogFactory.getLog(ClientePagos.class);
	
	private static ClientePagos clientePagos = null;

	public static final int ERROR_GENERAL = 0;
	public static final int ERROR_DUI_PAGADO = 1;
	public static final int ERROR_TASA_NO_EXISTE = 2;
	public static final int ERROR_LOCALIZADOR_NO_EXISTE = 3;
	public static final int ERROR_DUI_NO_PAGADO = 4;
	public static final int ERROR_FIRMA = 5;
	
	protected static final int PRESENCIAL = 0;
	protected static final int TELEMATICO = 0;	
	protected static final String ERROR_NE = "NE";
	protected static final String ERROR_ER = "ER";
	protected static final String ERROR_NP = "NP";
	protected static final String ERROR_PA = "PA";
	protected static final String ERROR_FR = "FR";		
	
	private Hashtable actions = null;
	private String url = "";
	private String url_inicio_pago_es;
	private String url_inicio_pago_ca;
	private String url_justificante_pago_es;
	private String url_justificante_pago_ca;
	private String fase = "pruebas";

	/** 
	 * Constructor
	 * 
	 */ 
	private ClientePagos(){		
	}
	
	/**
	 * Singleton del cliente de pagos
	 *
	 */
	public static ClientePagos getInstance() throws Exception{
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
	 *
	 */
	protected void init() throws Exception
	{ 
		this.fase = DelegateUtil.getConfiguracionDelegate().obtenerPropiedad("fase");
		this.url = DelegateUtil.getConfiguracionDelegate().obtenerPropiedad("url." + this.fase);
		this.url_inicio_pago_es = DelegateUtil.getConfiguracionDelegate().obtenerPropiedad("url_inicio_pago_es." + this.fase);
		this.url_inicio_pago_ca = DelegateUtil.getConfiguracionDelegate().obtenerPropiedad("url_inicio_pago_ca." + this.fase);
		this.url_justificante_pago_es = DelegateUtil.getConfiguracionDelegate().obtenerPropiedad("url_justificante_pago_es." + this.fase);
		this.url_justificante_pago_ca = DelegateUtil.getConfiguracionDelegate().obtenerPropiedad("url_justificante_pago_ca." + this.fase);
		
		this.actions = new Hashtable();
		this.actions.put("importeTasa",new ImporteTasaAction());
		this.actions.put("inicioPago",new InicioPagoAction());
		this.actions.put("comprobarPago",new ComprobarPagoAction());
		this.actions.put("imprimirTasaPagada",new ImprimirTasaPagadaAction());
	}

	/**
	 * Obtiene url para iniciar pago una vez se obtiene el token de acceso
	 * @param idioma
	 * @return Url de acceso
	 */
	public String getUrlInicioPago(String idioma,String token){
		if (idioma.equals("ca")) return url_inicio_pago_ca + token;
		else return url_inicio_pago_es + token; 
	}
	
	
	/**
	 * Obtiene url para acceder al modelo de un pago ya realizado
	 * @param localizador
	 * @return
	 */
	public String getUrlModelo(String idioma,String localizador){
		String url = ""; 
		if (idioma.equals("ca"))
		{
			url = url_justificante_pago_ca;
		}
		else
		{
			url = url_justificante_pago_es;
		}
		int idx = url.indexOf("?");
		if(idx != -1)
		{
			url = url.substring(0,idx) + "?localizador=" + localizador;
		}
		return url;
	}
	
	/**
	 * Obtiene url para justificante pago una vez se obtiene el token de acceso
	 * @param idioma
	 * @return Url de acceso
	 */
	public String getUrlJustificantePago(String idioma,String token){
		if (idioma.equals("ca"))	return url_justificante_pago_ca + token;
		else return url_justificante_pago_es + token; 
	}	
	/**
	 * Servicio para Saber el Importe de una Tasa
	 * @param tasa
	 * @return Devuelve un Long con el valor de la Tasa. Si devuelve null significa
	 * que ha habido un error y se puede recoger con getLastError
	 * @throws ClienteException
	 */
	public Long getImporte(String tasa) throws ClienteException
	{		
		Long resultado = null;
		try{
			Hashtable data = new Hashtable();
			data.put("idtasa", tasa);
			log.debug("importeTasa (" + tasa + ")");
			WebServiceAction action = (WebServiceAction) actions.get("importeTasa");
			Hashtable results = action.execute(this,data);
			if(results.containsKey("error"))
			{
				WebServiceError error = (WebServiceError) results.get("error");
				if(error.getError().equals(ClientePagos.ERROR_NE))
				{
					throw new TasaNoExisteException();
				}
				else
				{
					throw new ClienteException();
				}
			}
			else
			{
				String ls_importe = (String) results.get("importe");
				int idx = ls_importe.indexOf(",");
				if(idx != -1) // Proteccion
				{
					ls_importe = ls_importe.substring(0,idx);
				}
				//ls_importe = ls_importe.replace(',' , '.');
				resultado = Long.valueOf(ls_importe);
			}
			return resultado;
		}catch(ClienteException ex){
			log.error("Error en el Acceso al Importe de la tasa " + tasa + "Excepcion: "+ ex.getLocalizedMessage());
			throw ex;
		}catch(Exception ex){
			log.error("Error en el Acceso al Importe de la tasa " + tasa + "Excepcion: "+ ex.getLocalizedMessage());
			throw new ClienteException("Error en el Acceso al Importe de la tasa " + 
					                   tasa + "Excepcion: "+ ex.getLocalizedMessage(),ex);
		}
	}
	
	/**
	 * Servicio que Inicia el Pago Telematico
	 * @param tasa
	 * @return Devuelve una instancia de la clase ResultadoInicioPago o null en el caso de 
	 * que haya cualquier error
	 * @throws ClienteException
	 */
	public ResultadoInicioPago inicioPago(Tasa tasa) throws ClienteException
	{
		ResultadoInicioPago resultado = null;
		try{
			Hashtable data = new Hashtable();
			data.put("tasa", tasa);
			log.debug("InicioPago:");
			WebServiceAction action = (WebServiceAction) actions.get("inicioPago");
			Hashtable results = action.execute(this,data);
			if(results.containsKey("error"))
			{
				WebServiceError error = (WebServiceError) results.get("error");
				if(error.getError().equals(ClientePagos.ERROR_NE))
				{
					throw new TasaNoExisteException();
				}
				else if(error.getError().equals(ClientePagos.ERROR_PA))
				{
					throw new DUIPagadoException();
				}
				else
				{
					throw new ClienteException();
				}

			}
			else
			{
				String ls_localizador = (String) results.get("localizador");
				String ls_token = (String) results.get("token");
				resultado = new ResultadoInicioPago(ls_token,ls_localizador);
			}

			return resultado;
		}catch(ClienteException ex){
			log.error("Error en el Inicio de Pago de la tasa " + tasa.getIdTasa() + "Excepcion: "+ ex.getLocalizedMessage());
			throw ex;
		}catch(Exception ex){
			log.error("Error en el Inicio de Pago de la tasa " + tasa.getIdTasa() + "Excepcion: "+ ex.getLocalizedMessage());
			throw new ClienteException("Error en el Inicio de Pago de la tasa " + 
					                   tasa.getIdTasa() + "Excepcion: "+ ex.getLocalizedMessage(),ex);
		}
	}
	
	/**
	 * Servicio para comprobar un Pago a través del localizador
	 * @param localizador
	 * @return Devuelve justificante de pago, o null en caso de que haya cualquier error
	 * @throws ClienteException
	 */
	public String comprobarPago(String localizador) throws ClienteException
	{
		String resultado = null;
		try{
			Hashtable data = new Hashtable();
			data.put("localizador", localizador);
			log.debug("comprobarPago (" + localizador + ")");
			WebServiceAction action = (WebServiceAction) actions.get("comprobarPago");
			Hashtable results = action.execute(this,data);
			if(results.containsKey("error"))
			{
				WebServiceError error = (WebServiceError) results.get("error");
				if(error.getError().equals(ClientePagos.ERROR_NE))
				{
					throw new LocalizadorNoExisteException();
				}
				else if(error.getError().equals(ClientePagos.ERROR_NP))
				{
					throw new DUINoPagadoException();
				}
				else if(error.getError().equals(ClientePagos.ERROR_FR))
				{
					throw new FirmaException();
				}
				else
				{
					throw new ClienteException();
				}
			}
			else
			{
				resultado = (String) results.get("justificante");
			}

			return resultado;
		}catch(ClienteException ex){
			log.error("Error al comprobar el Pago del localizador " + localizador + "Excepcion: "+ ex.getLocalizedMessage());
			throw ex;
		}catch(Exception ex){
			log.error("Error al comprobar el Pago del localizador " + localizador + "Excepcion: "+ ex.getLocalizedMessage());
			throw new ClienteException("Error al comprobar el Pago del localizador " + 
					localizador + "Excepcion: "+ ex.getLocalizedMessage(),ex);
		}
	}
	
	/**
	 * Servicio para imprimir una Tasa Pagada a partir del localizador
	 * @param localizador
	 * @return Devuelve el token mediante el cual se accedera a una URL de la Plataforma
	 * de Pagos, o null si hay cualquier problema
	 * @throws ClienteException
	 */

	public String imprimirTasaPagada(String localizador) throws ClienteException
	{
		String resultado = null;
		try{
			Hashtable data = new Hashtable();
			data.put("localizador", localizador);
			log.debug("imprimirTasaPagada (" + localizador + ")");
			WebServiceAction action = (WebServiceAction) actions.get("imprimirTasaPagada");
			Hashtable results = action.execute(this,data);
			if(results.containsKey("error"))
			{
				WebServiceError error = (WebServiceError) results.get("error");
				if(error.getError().equals(ClientePagos.ERROR_NE))
				{
					throw new LocalizadorNoExisteException();
				}
				else if(error.getError().equals(ClientePagos.ERROR_NP))
				{
					throw new DUINoPagadoException();
				}
				else
				{
					throw new ClienteException();
				}
			}
			else
			{
				resultado = (String) results.get("token");
			}
			
			return resultado;
			
		}catch(ClienteException ex){
			log.error("Error al imprimir la Tasa pagada del localizador " +	localizador + "Excepcion: "+ ex.getLocalizedMessage());
			throw ex;			
		}catch(Exception ex){
			log.error("Error al imprimir la Tasa pagada del localizador " +	localizador + "Excepcion: "+ ex.getLocalizedMessage());
			throw new ClienteException("Error al imprimir la Tasa pagada del localizador " + 
					localizador + "Excepcion: "+ ex.getLocalizedMessage(),ex);
		}
		
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
	public void setUrl(String url) {
		this.url = url;
	}

	
	
}
