package es.caib.sistra.plugins.login.impl.caib;

import java.security.Principal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.loginModule.client.SeyconPrincipal;
import es.caib.sistra.plugins.login.ConstantesLogin;
import es.caib.sistra.plugins.login.PluginLoginIntf;
import es.caib.util.NifCif;
import es.caib.util.StringUtil;

public class PluginLoginCAIB implements PluginLoginIntf {

	private static Log log = LogFactory.getLog(PluginLoginCAIB.class);
	
	/**
	 * Obtiene metodo de autenticacion
	 */
	public char getMetodoAutenticacion(Principal principal) {
		SeyconPrincipal sp = (SeyconPrincipal) principal;
		int credentialType = sp.getCredentialType();
		if (credentialType == SeyconPrincipal.ANONYMOUS_CREDENTIAL)
			return ConstantesLogin.LOGIN_ANONIMO;
		else if (credentialType == SeyconPrincipal.PASSWORD_CREDENTIAL)
			return ConstantesLogin.LOGIN_USUARIO;
		else if (credentialType == SeyconPrincipal.SIGNATURE_CREDENTIAL)
			return ConstantesLogin.LOGIN_CERTIFICADO;
		else {			 
				log.error("Atención: nivel de autenticacion no reconocido: '" +sp.getCredentialType() + "'. Autenticamos como anonimo." );
				return ConstantesLogin.LOGIN_ANONIMO;			
		}
	}
	
	/**
	 * Obtiene nif
	 */
	public String getNif(Principal principal) {
		SeyconPrincipal sp = (SeyconPrincipal) principal;		
		return (sp.getNif()!=null?NifCif.normalizarDocumento(sp.getNif()):null);		
	}

	/**
	 * Obtiene nombre y apellidos
	 */
	public String getNombreCompleto(Principal principal) {
		SeyconPrincipal sp = (SeyconPrincipal) principal;		
		String nombre = sp.getFullName();
		
		// Quitamos palabra (AUTENTICACION), parche para DNIe
		if (nombre != null) {
			try  {
				nombre = StringUtil.replace(nombre, "(AUTENTICACION)", "");
				nombre = nombre.trim();
			} catch (Exception ex) {
				log.error("Error al intentar reemplazar literal (AUTENTICACION) en nombre del certificado");
			}
		}
		
		return nombre;
		
	}
	
}
