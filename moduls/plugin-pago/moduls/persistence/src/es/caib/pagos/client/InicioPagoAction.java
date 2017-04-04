package es.caib.pagos.client;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.xml.rpc.ServiceException;

import org.apache.axis.encoding.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.pagos.exceptions.ParametroTasaException;
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

	public Hashtable execute(final ClientePagos cliente, final Hashtable data) {

		final Hashtable resultado = new Hashtable();
		try {
			final Tasa tasa = (Tasa)data.get(Constants.KEY_TASA);
			final String ls_handleB64 = getParametroTasa(tasa);
						
			final InicioPagoService service = new InicioPagoService(cliente.getUrl());
			 
			final UsuariosWebServices usuario = UtilWs.getUsuario();
			final DatosRespuesta046 ls_resultado = service.execute(ls_handleB64, usuario);
			log.debug("Localizador: " + ls_resultado.getLocalizador());
	
			if (ls_resultado == null) {
				resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_RESPUESTA_NULA, "No se ha obtenido respuesta del servicio InicioPago."));
				log.error("No se ha obtenido respuesta del servicio InicioPago.");
			} else {
				if (ls_resultado.getCodError() == null) {
						resultado.put(Constants.KEY_LOCALIZADOR, ls_resultado.getLocalizador());
						resultado.put(Constants.KEY_TOKEN, ls_resultado.getToken());
				} else {
					resultado.put(Constants.KEY_ERROR, new WebServiceError(ls_resultado.getCodError(), ls_resultado.getTextError()));
					log.error(ls_resultado.getTextError());
				}
			}
			
		} catch (ParametroTasaException pte) {
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_MSG_XML, pte.getMessage()));
			log.error(pte.getMessage());
		} catch (DelegateException de) {
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_PROPERTIES, "Error obteniendo los valores de usuario web service. " + de.getMessage()));
			log.error("Error obteniendo los valores de usuario web service. " + de.getMessage());
		} catch (ServiceException e) {
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_COMUNICACION, "Error de comunicación con el servicio InicioPago. " + e.getMessage()));
			log.error("Error de comunicación con el servicio InicioPago. " + e.getMessage());
		} catch (RemoteException e) { 
			resultado.put(Constants.KEY_ERROR, new WebServiceError(WebServiceError.ERROR_COMUNICACION, "Error de comunicación con el servicio InicioPago. " + e.getMessage()));
			log.error("Error de comunicación con el servicio InicioPago. " + e.getMessage());
		}

		return resultado;
	}
	
	/**
	 * Construimos el parametro de la tasa para enviar en la petición al WS
	 * @param tasa 
	 * @return
	 * @throws InicializacionFactoriaException 
	 * @throws EstablecerPropiedadException 
	 * @throws GuardaObjetoXMLException 
	 * @throws UnsupportedEncodingException 
	 * @throws Exception
	 */
	private String getParametroTasa(final Tasa tasa) throws ParametroTasaException {
		 
		FactoriaObjetosXMLTaxa factoria;
		try {
			factoria = ServicioTaxaXML.crearFactoriaObjetosXML();
			final Taxa tx = factoria.crearTaxa();
			copyTasa2Taxa(tasa,tx);
			final String tasaXML = factoria.guardarTaxa(tx);
			
			final byte[] handleContent = tasaXML.getBytes(Constants.CHARSET);
			return new String(Base64.encode(handleContent));
			
		} catch (InicializacionFactoriaException ife) {
			throw new ParametroTasaException("Error en la Inicializacion de la Factoria de Objetos XML: " + ife.getMessage(), ife);
		} catch (EstablecerPropiedadException epe) {
			throw new ParametroTasaException("Error en la construccion del XML: " + epe.getMessage(), epe);
		} catch (GuardaObjetoXMLException goxe) {
			throw new ParametroTasaException("Error al pasar el objeto Tasa a XML: " + goxe.getMessage(), goxe);
		} catch (UnsupportedEncodingException uee) {
			throw new ParametroTasaException("Error al convertir a Base64: " + uee.getMessage(), uee);
		}
		
		
	}
	
	/**
	 * Copia un objeto Tasa a Taxa(objeto XML)
	 * @param ts objeto Tasa origen
	 * @param tx objeto Taxa destino
	 * @throws EstablecerPropiedadException 
	 * @throws InicializacionFactoriaException 
	 * @throws Exception
	 */
	private void copyTasa2Taxa(final Tasa ts, final Taxa tx) throws EstablecerPropiedadException, InicializacionFactoriaException 
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
		
		final Declarant decl = getDeclarant(ts);
		
		tx.setDeclarant(decl);
		
		
	}
	
	/**
	 * Crea un objeto Declarant a partir de los datos de un objeto Tasa
	 * @param ts
	 * @return
	 * @throws EstablecerPropiedadException
	 * @throws InicializacionFactoriaException
	 */
	private Declarant getDeclarant(final Tasa ts) throws EstablecerPropiedadException, InicializacionFactoriaException
	{
		//TODO -- > refactorizar y eliminar los if (...!= null)
		final FactoriaObjetosXMLTaxa factoria = ServicioTaxaXML.crearFactoriaObjetosXML();
		final Declarant decl = factoria.crearDeclarant();
		
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
			final String nif = StringUtils.leftPad(ts.getNif(), 9, "0");
			decl.setNIF(nif);
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
		
		final Domicili domicili = getDomicili(ts, factoria);
		
		decl.setDomicili(domicili);

		return decl;
		
	}

	private Domicili getDomicili(final Tasa ts,	final FactoriaObjetosXMLTaxa factoria) throws EstablecerPropiedadException {
		
		final Domicili domicili = factoria.crearDomicili();
		
		if(ts.getEscala() != null)
		{
			domicili.setCodiEscala(ts.getCodigoEscala());
			domicili.setEscala(ts.getEscala());
		}
		if(ts.getLetra() != null)
		{
			domicili.setCodiLletra(ts.getCodigoLetra());
			domicili.setLletra(ts.getLetra());
		}
		if(ts.getNombreVia() != null)
		{
			domicili.setCodiNomVia(ts.getCodigoNombreVia());
			domicili.setNomVia(ts.getNombreVia());
		}
		if(ts.getNumero() != null)
		{
			domicili.setCodiNumero(ts.getCodigoNumero());
			domicili.setNumero(ts.getNumero());
		}
		if(ts.getPiso() != null)
		{
			domicili.setCodiPis(ts.getCodigoPiso());
			domicili.setPis(ts.getPiso());
		}
		if(ts.getPuerta() != null)
		{
			domicili.setCodiPorta(ts.getCodigoPuerta());
			domicili.setPorta(ts.getPuerta());
		}
		if(ts.getSiglas() != null)
		{
			domicili.setCodiSigles(ts.getCodigoSiglas());
			domicili.setSigles(ts.getSiglas());
		}
		
		return domicili;

	}

}
