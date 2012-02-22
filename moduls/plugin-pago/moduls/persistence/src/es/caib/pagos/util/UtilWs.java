package es.caib.pagos.util;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

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
		//TODO --> modificar para que no dependa de los tipos de datos del WS ?
		String nombreUsuario = DelegateUtil.getConfiguracionDelegate().obtenerPropiedad("usuarioWs");
		String password = DelegateUtil.getConfiguracionDelegate().obtenerPropiedad("passwordWs");
		UsuariosWebServices usuario = new UsuariosWebServices();
		usuario.setIdentificador(nombreUsuario); 
		usuario.setPassword(password);
		return usuario;
		
	}
	
	/**
	 * Obtiene una cadena con el formato
	 * &lt;DATOS_PAGO>&lt;LOCALIZADOR>localizador&lt;/LOCALIZADOR>&lt;DUI>dui&lt;/DUI>&lt;FECHA_PAGO>fecha&lt;/FECHA_PAGO>&lt;/DATOS_PAGO>
	 * 		
	 * @param datos
	 * @return
	 */
	public static String getCadenaDatos(Hashtable datos) {
	
		StringBuffer str = new StringBuffer();
		str.append("<DATOS_PAGO><LOCALIZADOR>");
		str.append((String)datos.get(Constants.KEY_LOCALIZADOR));
		str.append("</LOCALIZADOR><DUI>");
		str.append((String)datos.get(Constants.KEY_LOCALIZADOR));
		str.append("</DUI><FECHA_PAGO>");
		str.append((String)datos.get(Constants.KEY_FECHA_PAGO));
		str.append("</FECHA_PAGO></DATOS_PAGO>");
		return str.toString();
	}

	/**
	 * Valida que la respuesta proviene de la ATIB
	 * @param datos
	 * @param firma
	 * @return
	 * @throws ExcepcionMensaje
	 * @throws UnsupportedEncodingException
	 */
	public static boolean validarRespuesta(String cadenaDatos, String firma) throws ExcepcionMensaje, UnsupportedEncodingException {
		
		MensajeFirmado mf = new MensajeFirmado();
		mf.cargarDeString(firma);
		mf.setDatos(cadenaDatos.getBytes(FuncionesCadena.getCharset()));
		return mf.comprobarIntegridadFirma();

	}
	
	/**
	 * Obtiene el justificante del pago 
	 * @param datos
	 * @param firma
	 * @return
	 */
	public static String getJustificante(String datos, String firma) {
		StringBuffer justificante = new StringBuffer();
		justificante.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
		justificante.append(datos);
		justificante.append("<FIRMA>");
		justificante.append(firma);
		justificante.append("</FIRMA>");
		return justificante.toString();
	}
	
	/**
	 * Para testear la firma
	 * @return
	 */
	public static boolean testFirma() {
		
		try{

			MensajeFirmado l_mf = new MensajeFirmado();
			String datos = "<DATOS_PAGO><LOCALIZADOR>0462812850464</LOCALIZADOR><DUI>0462812850464</DUI><FECHA_PAGO>21/02/2012 9:35:37</FECHA_PAGO></DATOS_PAGO>";
			l_mf.cargarDeString("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");
			l_mf.setDatos(datos.getBytes(FuncionesCadena.getCharset()));
			boolean a = l_mf.comprobarIntegridadFirma();
		
			return a;
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
}
