package es.caib.pagos.client;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.xml.rpc.ServiceException;

import org.apache.axis.encoding.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.pagos.persistence.delegate.DelegateException;
import es.caib.pagos.services.InicioPagoService;
import es.caib.pagos.services.wsdl.DatosRespuesta046;
import es.caib.pagos.services.wsdl.UsuariosWebServices;
import es.caib.pagos.util.Constants;
import es.caib.pagos.util.UtilWs;
import es.caib.xml.EstablecerPropiedadException;
import es.caib.xml.GuardaObjetoXMLException;
import es.caib.xml.InicializacionFactoriaException;
import es.caib.xml.taxa.factoria.FactoriaObjetosXMLTaxa;
import es.caib.xml.taxa.factoria.ServicioTaxaXML;
import es.caib.xml.taxa.factoria.impl.Declarant;
import es.caib.xml.taxa.factoria.impl.Domicili;
import es.caib.xml.taxa.factoria.impl.Taxa;

public class InicioPagoAction implements WebServiceAction {

	private static Log log = LogFactory.getLog(InicioPagoAction.class);

	public Hashtable execute(ClientePagos cliente, Hashtable data) throws Exception{
		Hashtable resultado = new Hashtable();
		Tasa tasa = (Tasa)data.get(Constants.KEY_TASA);
		String ls_handleB64;
				
		try {
			ls_handleB64 = getParametroPeticion(tasa);
		} catch (InicializacionFactoriaException e) {
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_MSG_XML, "Error en la Inicializacion de la Factoria de Objetos XML"));
			//log.error("Error en la Inicializacion de la Factoria de Objetos XML. Excepción: " + e.getMessage());
			return resultado;
		} catch (EstablecerPropiedadException e) {
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_MSG_XML, "Error en la construccion del XML:" + e.toString()));
			//log.error("Error en la construccion del XML:" + e.toString() + " Excepción: " + e.getMessage());
			return resultado;
		} catch (GuardaObjetoXMLException e) {
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_MSG_XML, "Error al pasar el objeto Tasa a XML: " + e.toString()));
			//log.error("Error al pasar el objeto Tasa a XML: " + e.toString() + " Excepción: " + e.getMessage());
			return resultado;
		} catch (UnsupportedEncodingException e) {
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_MSG_XML, "Error al convertir a Base64"));
			//log.error("Error al convertir a Base64. Excepción: " + e.getMessage());
			return resultado;
		}
						
		//llamamos al servicio
		
		InicioPagoService service = new InicioPagoService(cliente.getUrl());
		
		DatosRespuesta046 ls_resultado = null;
		try {
			UsuariosWebServices usuario = UtilWs.getUsuario();
			ls_resultado = service.execute(ls_handleB64, usuario);
			log.debug("Localizador: " + ls_resultado.getLocalizador());
		} catch (DelegateException de) {
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_PROPERTIES, "Error obteniendo los valores de usuario web service. " + de.getMessage()));
			return resultado;
		} catch (ServiceException e) {
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_COMUNICACION, "Error de comunicación con el servicio InicioPago. " + e.getMessage()));
			return resultado;
		} catch (RemoteException e) { 
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_COMUNICACION, "Error de comunicación con el servicio InicioPago. " + e.getMessage()));
			return resultado;
		}
		
		if (ls_resultado == null) {
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_RESPUESTA_NULA, "No se ha obtenido respuesta del servicio InicioPago."));
		} else {
			if (ls_resultado.getCodError() == null) {
					resultado.put(Constants.KEY_LOCALIZADOR, ls_resultado.getLocalizador());
					resultado.put(Constants.KEY_TOKEN, ls_resultado.getToken());
			} else {
				resultado.put(Constants.KEY_ERROR, new WebServiceError(ls_resultado.getCodError(), ls_resultado.getTextError()));
			}
		}

		return resultado;
	}
	
	/**
	 * Construimos el parametro para enviar en la petición al WS
	 * @param tasa 
	 * @return
	 * @throws InicializacionFactoriaException 
	 * @throws EstablecerPropiedadException 
	 * @throws GuardaObjetoXMLException 
	 * @throws UnsupportedEncodingException 
	 * @throws Exception
	 */
	private String getParametroPeticion(Tasa tasa) throws EstablecerPropiedadException, InicializacionFactoriaException ,
		GuardaObjetoXMLException, UnsupportedEncodingException {
		 //TODO Mejorar este método
		FactoriaObjetosXMLTaxa factoria = ServicioTaxaXML.crearFactoriaObjetosXML();
		Taxa tx = factoria.crearTaxa();
		copyTasa2Taxa(tasa,tx);
		String tasaXML = factoria.guardarTaxa(tx);
		
		byte[] handleContent = tasaXML.getBytes(Constants.CHARSET);
		
		return new String(Base64.encode(handleContent));
	}
	
	/**
	 * Copia un objeto Tasa a Taxa(objeto XML)
	 * @param ts objeto Tasa origen
	 * @param tx objeto Taxa destino
	 * @throws EstablecerPropiedadException 
	 * @throws InicializacionFactoriaException 
	 * @throws Exception
	 */
	private void copyTasa2Taxa(Tasa ts, Taxa tx) throws EstablecerPropiedadException, InicializacionFactoriaException 
	{
		// copiamos cada uno de los atributos
		
		tx.setModelo(ts.getModelo());
		tx.setVersio("1.0");
		//concepto
		if(ts.getConcepto() != null)
		{
			tx.setCodiConcepte(ts.getCodigoConcepto());
			tx.setConcepte(ts.getConcepto());
		}
		//idtasa
		if(ts.getIdTasa() != null )
		{
			tx.setCodiIdtaxa(ts.getCodigoTasa());
			tx.setIdtaxa(ts.getIdTasa());
		}
		//importr
		if(ts.getImporte() != null)
		{
		    tx.setCodiImporte(ts.getCodigoImporte());
			tx.setImporte(ts.getImporte());
		}
		//localizador
		if(ts.getLocalizador() != null)
		{
			tx.setLocalizador(ts.getLocalizador());
		}
		//accion
		if(Tasa.PRESENCIAL.equals(ts.getTipoPago()))
		{
			tx.setAccio("imprimir");
		}
		else
		{
			tx.setAccio("pagar");
		}
		//subconcepto
		if(ts.getSubConcepto() != null)
		{
			tx.setCodiSubconcepte(ts.getCodigoSubConcepto());
			tx.setSubconcepte(ts.getSubConcepto());
		}
		
		Declarant decl = getDeclarant(ts);
		
		tx.setDeclarant(decl);
		
		
	}
	
	/**
	 * Crea un objeto Declarant a partir de los datos de un objeto Tasa
	 * @param ts
	 * @return
	 * @throws EstablecerPropiedadException
	 * @throws InicializacionFactoriaException
	 */
	private Declarant getDeclarant(Tasa ts) throws EstablecerPropiedadException, InicializacionFactoriaException
	{
		FactoriaObjetosXMLTaxa factoria = ServicioTaxaXML.crearFactoriaObjetosXML();
		Declarant decl = factoria.crearDeclarant();
		decl.setCodiCodiPostal(ts.getCodigoCodigoPostal());
		if(ts.getCodigoPostal() != null)
		{
			decl.setCodiPostal(ts.getCodigoPostal());
		}
		decl.setCodiFAX(ts.getCodigoFax());
		if(ts.getFax() != null)
		{
			decl.setFAX(ts.getFax());
		}
		decl.setCodiLocalitat(ts.getCodigoLocalidad());
		if(ts.getLocalidad() != null)
		{
			decl.setLocalitat(ts.getLocalidad());
		}
		if(ts.getNif() != null)
		{
			decl.setCodiNIF(ts.getCodigoNif());
			if (ts.getNif().length() < 9){
				String relleno = "";
				for (int i=ts.getNif().length();i<9;i++) relleno += "0";
				decl.setNIF(relleno + ts.getNif());  
			}else{				
				decl.setNIF(ts.getNif());
			}
		}
		if(ts.getNombre() != null)
		{
			decl.setCodiNom(ts.getCodigoNombre());
			decl.setNom(ts.getNombre());
		}
		decl.setCodiProvincia(ts.getCodigoProvincia());
		if(ts.getProvincia() != null)
		{
			decl.setProvincia(ts.getProvincia());
		}
		decl.setCodiTelefon(ts.getCodigoTelefono());
		if(ts.getTelefono() != null)
		{
			decl.setTelefon(ts.getTelefono());
		}
		int filled = 0;
		Domicili domicili = factoria.crearDomicili();
		{
			if(ts.getEscala() != null)
			{
				domicili.setCodiEscala(ts.getCodigoEscala());
				domicili.setEscala(ts.getEscala());
				filled++;
			}
			if(ts.getLetra() != null)
			{
				domicili.setCodiLletra(ts.getCodigoLetra());
				domicili.setLletra(ts.getLetra());
				filled++;
			}
			if(ts.getNombreVia() != null)
			{
				domicili.setCodiNomVia(ts.getCodigoNombreVia());
				domicili.setNomVia(ts.getNombreVia());
				filled++;
			}
			if(ts.getNumero() != null)
			{
				domicili.setCodiNumero(ts.getCodigoNumero());
				domicili.setNumero(ts.getNumero());
				filled++;
			}
			if(ts.getPiso() != null)
			{
				domicili.setCodiPis(ts.getCodigoPiso());
				domicili.setPis(ts.getPiso());
				filled++;
			}
			if(ts.getPuerta() != null)
			{
				domicili.setCodiPorta(ts.getCodigoPuerta());
				domicili.setPorta(ts.getPuerta());
				filled++;
			}
			if(ts.getSiglas() != null)
			{
				domicili.setCodiSigles(ts.getCodigoSiglas());
				domicili.setSigles(ts.getSiglas());
				filled++;
			}
		}
		if(filled > 0)
		{
			decl.setDomicili(domicili);
		}
		
		return decl;
		
	}

}
