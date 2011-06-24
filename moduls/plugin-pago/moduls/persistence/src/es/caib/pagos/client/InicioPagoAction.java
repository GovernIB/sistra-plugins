package es.caib.pagos.client;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.xml.rpc.ServiceException;

import org.apache.axis.encoding.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.pagos.services.InicioPagoService;
import es.caib.pagos.util.Constants;
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

	
	public Hashtable execute(ClientePagos cliente, Hashtable data) throws Exception {
		Hashtable resultado = new Hashtable();
		Tasa tasa = (Tasa)data.get("tasa");
		Taxa tx = null;
		FactoriaObjetosXMLTaxa factoria = null;
		String tasaXML;
		try {
			factoria = ServicioTaxaXML.crearFactoriaObjetosXML();
			tx = factoria.crearTaxa();
			copyTasa2Taxa(tasa,tx);
			tasaXML = factoria.guardarTaxa(tx);
			log.debug("Enviamos: " + tasaXML);
		} catch (InicializacionFactoriaException e) {
			resultado.put("error", new WebServiceError("Error en la Inicializacion de la Factoria de Objetos XML"));
			e.printStackTrace();
			return resultado;
		} catch (EstablecerPropiedadException e) {
			resultado.put("error", new WebServiceError("Error en la construccion del XML:" + e.toString()));
			e.printStackTrace();
			return resultado;
		} catch (GuardaObjetoXMLException e) {
			resultado.put("error", new WebServiceError("Error al pasar el objeto Tasa a XML: " + e.toString()));
			e.printStackTrace();
			return resultado;
		}
		Base64 encoder = new Base64();
		byte[] handleContent = null;
		try {
			handleContent = tasaXML.getBytes(Constants.CHARSET);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			resultado.put("error", new WebServiceError("Error al convertir a Base64"));
			return resultado;
		}
		String ls_handleB64 = new String(encoder.encode(handleContent));
		
		InicioPagoService imprimir = new InicioPagoService(cliente.getUrl());
		String ls_resultado = null;
		try {
			ls_resultado = imprimir.execute(ls_handleB64);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			resultado.put("error", new WebServiceError("Error al generar la URL del servicio ImprimirTasaPagada"));
		}
		log.debug("Resultado InicioPago: " + ls_resultado);

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
	
	private void copyTasa2Taxa(Tasa ts, Taxa tx) throws EstablecerPropiedadException, InicializacionFactoriaException
	{
		// copiamos cada uno de los atributos
		
		tx.setModelo(ts.getModelo());
		tx.setVersio("1.0");
		if(ts.getConcepto() != null)
		{
			tx.setCodiConcepte(ts.getCodigoConcepto());
			tx.setConcepte(ts.getConcepto());
		}
		if(ts.getIdTasa() != null )
		{
			tx.setCodiIdtaxa(ts.getCodigoTasa());
			tx.setIdtaxa(ts.getIdTasa());
		}
		if(ts.getImporte() != null)
		{
		    tx.setCodiImporte(ts.getCodigoImporte());
			tx.setImporte(ts.getImporte());
		}

		if(ts.getLocalizador() != null)
		{
			tx.setLocalizador(ts.getLocalizador());
		}

		if(Tasa.PRESENCIAL.equals(ts.getTipoPago()))
		{
			tx.setAccio("imprimir");
		}
		else
		{
			tx.setAccio("pagar");
		}
		if(ts.getSubConcepto() != null)
		{
			tx.setCodiSubconcepte(ts.getCodigoSubConcepto());
			tx.setSubconcepte(ts.getSubConcepto());
		}
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
			tx.setDeclarant(decl);
		}
		
	}

}
