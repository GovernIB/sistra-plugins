package es.caib.pagos.util;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

import es.caib.pagos.client.WebServiceError;
import es.caib.pagos.exceptions.ClienteException;
import es.caib.pagos.persistence.delegate.DelegateException;
import es.caib.pagos.persistence.delegate.DelegateUtil;
import es.caib.pagos.services.wsdl.UsuariosWebServices;
import gva.ideas.MensajeFirmado;
import gva.ideas.excepciones.ExcepcionMensaje;

/**
 * Clase de utilidades para las WebServiceAction
 * @author ihdelpino
 *
 */
public class UtilWs {
	
	
	
	/**
	 * Crea un objeto con los datos de usuario password para la
	 * cabecera de la petición SOAP
	 * @return
	 */
	public static UsuariosWebServices getUsuario() throws DelegateException{
				
		String nombreUsuario = DelegateUtil.getConfiguracionDelegate().obtenerPropiedad("usuarioWs");
		String password = DelegateUtil.getConfiguracionDelegate().obtenerPropiedad("passwordWs");
		UsuariosWebServices usuario = new UsuariosWebServices();
		usuario.setIdentificador(nombreUsuario); 
		usuario.setPassword(password);
		return usuario;
		
	}
	
	/**
	 * Valida el resultado de la llamada al WS
	 * @param results Resultado de la llamada al WS
	 * @throws ClienteException en caso de haberse producido algun error
	 * 
	 */
	public static void validateResults(Hashtable results) throws ClienteException
	{
		
		if(results.containsKey(Constants.KEY_ERROR))
		{
			WebServiceError error = (WebServiceError) results.get(Constants.KEY_ERROR);
			throw new ClienteException(error.getCodigo(), error.getError());
		}
	}
	
	/**
	 * Comprueba que la firma sea correcta
	 * @param firma
	 * @return
	 * @throws ExcepcionMensaje 
	 * @throws UnsupportedEncodingException 
	 */
	public static boolean comprobarFirma(String firma) throws ExcepcionMensaje, UnsupportedEncodingException
	{
		if (firma == null) {
			return true;
		}
		else {
//			byte [] handleContent = Base64.decode(firma);
//			String ls_pkcs7 = new String(handleContent, "ISO-8859-1");
			/*int li_ini = ls_firma.indexOf("<DATOS_PAGO>");
			int li_fin = ls_firma.indexOf("</DATOS_PAGO>");
			String ls_datos = ls_firma.substring(li_ini,li_fin + 13);
			li_ini = ls_firma.indexOf("<FIRMA>");
			li_fin = ls_firma.indexOf("</FIRMA>");
			String ls_pkcs7 = firma.substring(li_ini + 7,li_fin);*/
	
			MensajeFirmado l_mf = new MensajeFirmado();
			l_mf.cargarDeString(firma);	
			//l_mf.setDatos(ls_datos.getBytes(FuncionesCadena.getCharset()));
			return l_mf.comprobarIntegridadFirma();
		}

	}

	
}
