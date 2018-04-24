package es.caib.sistra.plugins.firma.impl.caib;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.signatura.api.Signature;
import es.caib.sistra.plugins.firma.FirmaIntf;
import es.caib.util.NifCif;

public class FirmaCAIB implements FirmaIntf{

	private static Log log = LogFactory.getLog(FirmaCAIB.class);
	
	public static final String FORMATO_FIRMA_SIGNATURE = "SIGNATURE";
	public static final String FORMATO_FIRMA_SMIME = "SMIME";
	
	/**
	 * Objeto signature del api de la CAIB
	 */
	private Signature signature;
	
	/**
	 * SMIME cuando proviene firma de SMIME.
	 */
	private String smime;
	
	/**
	 * Formato firma: SMIME o SIGNATURE.
	 */
	private String formatoFirma;
	
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
	 * Formato firma: SMIME o SIGNATURE.
	 */
	public String getFormatoFirma() {
		return formatoFirma;
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
	 * Obtiene contenido firma.
	 */
	public byte[] getContenidoFirma() {		
		try {
			return UtilFirmaCAIB.parseFirmaToBytes(this);
		} catch (Exception e) {
			log.error("Error al obtener contenido firma",e);
			return null;
		}
	}

	public String getSmime() {
		return smime;
	}

	public void setSmime(String smime) {
		this.smime = smime;
	}

	public void setFormatoFirma(String formatoFirma) {
		this.formatoFirma = formatoFirma;
	}

	public String getNifRepresentante() {
		try {
			String nifRep = UtilFirmaCAIB.getNifRepresentante(signature);
			if (nifRep != null) {
				nifRep = NifCif.normalizarDocumento(nifRep);
			}			
			return nifRep;
		} catch (Exception e) {
			log.error("Error al obtener nif del representante de la firma",e);
			return null;
		}
	}

	public String getNombreApellidosRepresentante() {
		try {
			return UtilFirmaCAIB.getNombreApellidosRepresentante(signature);
		} catch (Exception e) {
			log.error("Error al obtener nombre y apellidos del representante de la firma",e);
			return null;
		}
	}

}
