package es.caib.sistra.plugins.firma.impl.caib;

/**
 * Datos certificado.
 * 
 * @author Indra
 */
public final class DatosCertificado {

    private String nif;

    private String nombreApellidos;

    private String nifRepresentante;

    private String nombreApellidosRepresentante;

    /**
     * @return the nif
     */
    public String getNif() {
        return nif;
    }

    /**
     * @param pNif
     *            the nif to set
     */
    public void setNif(final String pNif) {
        nif = pNif;
    }

    /**
     * @return the nombreApellidos
     */
    public String getNombreApellidos() {
        return nombreApellidos;
    }

    /**
     * @param pNombreApellidos
     *            the nombreApellidos to set
     */
    public void setNombreApellidos(final String pNombreApellidos) {
        nombreApellidos = pNombreApellidos;
    }

    /**
     * @return the nifRepresentante
     */
    public String getNifRepresentante() {
        return nifRepresentante;
    }

    /**
     * @param pNifRepresentante
     *            the nifRepresentante to set
     */
    public void setNifRepresentante(final String pNifRepresentante) {
        nifRepresentante = pNifRepresentante;
    }

    /**
     * @return the nombreApellidosRepresentante
     */
    public String getNombreApellidosRepresentante() {
        return nombreApellidosRepresentante;
    }

    /**
     * @param pNombreApellidosRepresentante
     *            the nombreApellidosRepresentante to set
     */
    public void setNombreApellidosRepresentante(
            final String pNombreApellidosRepresentante) {
        nombreApellidosRepresentante = pNombreApellidosRepresentante;
    }

}
