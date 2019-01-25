package es.caib.sistra.plugins.firma.impl.caib;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.signatura.api.Signature;
import es.caib.sistra.plugins.firma.FirmaIntf;
import es.caib.util.NifCif;

public class FirmaExtendedCAIB implements FirmaIntf{

	private static Log log = LogFactory.getLog(FirmaExtendedCAIB.class);
	
	public static final String FORMATO_FIRMA_SIGNATURE = "SIGNATURE";
	public static final String FORMATO_FIRMA_SMIME = "SMIME";
	public static final String FORMATO_FIRMA_EXTENDED = "EXTENDED";
	
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
     * Nif.
     */
    private final String nif;

    /**
     * Nombre apellidos.
     */
    private final String nombreApellidos;

    /**
     * Nif representante.
     */
    private final String nifRepresentante;

    /**
     * Nombre apellidos representante.
     */
    private final String nombreApellidosRepresentante;
    
	
	/**
	 * Constructor.
	 * 
	 * @param signature
	 * 			objeto signature
	 * @param smime
	 * 			smime
	 * @param formatoFirma
	 * 			formato firma
	 * @param nif
	 * 			nif del firmante
	 * @param nombreApellidos
	 * 			nombre y apellidos del firmante
	 * @param nifRepresentante
	 * 			nif del representante
	 * @param nombreApellidosRepresentante
	 * 			nombre y apellidos, o razón social, del representante
	 */
	public FirmaExtendedCAIB(Signature signature, String smime,
			String formatoFirma, String nif, String nombreApellidos,
			String nifRepresentante, String nombreApellidosRepresentante) {
		super();
		this.signature = signature;
		this.smime = smime;
		this.formatoFirma = formatoFirma;
		this.nif = nif;
		this.nombreApellidos = nombreApellidos;
		this.nifRepresentante = nifRepresentante;
		this.nombreApellidosRepresentante = nombreApellidosRepresentante;
	}
	
	

	/**
	 * @return the nif
	 */
	public String getNif() {
		return nif;
	}



	/**
	 * @return the nombreApellidos
	 */
	public String getNombreApellidos() {
		return nombreApellidos;
	}



	/**
	 * @return the nifRepresentante
	 */
	public String getNifRepresentante() {
		return nifRepresentante;
	}



	/**
	 * @return the nombreApellidosRepresentante
	 */
	public String getNombreApellidosRepresentante() {
		return nombreApellidosRepresentante;
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

}
