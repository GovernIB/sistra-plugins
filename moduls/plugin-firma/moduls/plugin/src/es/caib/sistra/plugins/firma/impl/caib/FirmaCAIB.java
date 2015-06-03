package es.caib.sistra.plugins.firma.impl.caib;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.signatura.api.Signature;
import es.caib.sistra.plugins.firma.FirmaIntf;
import es.caib.util.NifCif;

public class FirmaCAIB implements FirmaIntf{

	private static Log log = LogFactory.getLog(FirmaCAIB.class);
	
	/**
	 * Objeto signature del api de la CAIB
	 */
	private Signature signature;
	
	/**
	 * Obtiene el nif del certificado
	 */
	public String getNif() {
		try {
			String nif = UtilFirmaCAIB.getDNI(signature);
			if (nif != null) {
				nif = NifCif.normalizarDocumento(nif);
			}			
			return nif;
		} catch (Exception e) {
			log.error("Error al obtener nif de la firma",e);
			return null;
		}
	}

	/**
	 * Obtiene nombre y apellidos del certificado
	 */
	public String getNombreApellidos() {		
		try {
			return UtilFirmaCAIB.getNombreApellidos(signature);
		} catch (Exception e) {
			log.error("Error al obtener nombre y apellidos de la firma",e);
			return null;
		} 
	}

	/**
	 * Formato firma: no utilizado en CAIB
	 */
	public String getFormatoFirma() {
		return null;
	}

	/**
	 * Obtiene firma caib
	 * @return
	 */
	public Signature getSignature() {
		return signature;
	}

	/**
	 * Establece firma caib
	 * @param signature
	 */
	public void setSignature(Signature signature) {
		this.signature = signature;
	}
	
	/**
	 * Obtiene certificado en B64.
	 */
	public String getCertificadoB64() {	
		try {
			char[] chars = Base64Coder.encode(this.signature.getCert().getEncoded());
	    	String b64 = new String(chars);		
			return b64;
		} catch (Exception ex) {
			log.error("Error al obtener certificado en B64",ex);
			return null;
		}
	}

	/**
	 * Obtiene contenido firma.
	 */
	public byte[] getContenidoFirma() {		
		try {
			return UtilFirmaCAIB.serializaFirmaToBytes(signature);
		} catch (Exception e) {
			log.error("Error al obtener contenido firma",e);
			return null;
		}
	}

}
