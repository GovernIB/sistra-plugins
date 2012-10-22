
package es.caib.regtel.plugincaib.ws.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParametrosRegistroPublicadoEntradaWS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ParametrosRegistroPublicadoEntradaWS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="anoEntrada" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="numero" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="oficina" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="numeroBOCAIB" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="fecha" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="pagina" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="lineas" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="contenido" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="observaciones" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="leido" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="errorfecha" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParametrosRegistroPublicadoEntradaWS", namespace = "urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", propOrder = {
    "anoEntrada",
    "numero",
    "oficina",
    "numeroBOCAIB",
    "fecha",
    "pagina",
    "lineas",
    "contenido",
    "observaciones",
    "leido",
    "errorfecha"
})
public class ParametrosRegistroPublicadoEntradaWS {

    protected int anoEntrada;
    protected int numero;
    protected int oficina;
    protected int numeroBOCAIB;
    protected int fecha;
    protected int pagina;
    protected int lineas;
    @XmlElement(required = true)
    protected String contenido;
    @XmlElement(required = true)
    protected String observaciones;
    protected boolean leido;
    protected boolean errorfecha;

    /**
     * Gets the value of the anoEntrada property.
     * 
     */
    public int getAnoEntrada() {
        return anoEntrada;
    }

    /**
     * Sets the value of the anoEntrada property.
     * 
     */
    public void setAnoEntrada(int value) {
        this.anoEntrada = value;
    }

    /**
     * Gets the value of the numero property.
     * 
     */
    public int getNumero() {
        return numero;
    }

    /**
     * Sets the value of the numero property.
     * 
     */
    public void setNumero(int value) {
        this.numero = value;
    }

    /**
     * Gets the value of the oficina property.
     * 
     */
    public int getOficina() {
        return oficina;
    }

    /**
     * Sets the value of the oficina property.
     * 
     */
    public void setOficina(int value) {
        this.oficina = value;
    }

    /**
     * Gets the value of the numeroBOCAIB property.
     * 
     */
    public int getNumeroBOCAIB() {
        return numeroBOCAIB;
    }

    /**
     * Sets the value of the numeroBOCAIB property.
     * 
     */
    public void setNumeroBOCAIB(int value) {
        this.numeroBOCAIB = value;
    }

    /**
     * Gets the value of the fecha property.
     * 
     */
    public int getFecha() {
        return fecha;
    }

    /**
     * Sets the value of the fecha property.
     * 
     */
    public void setFecha(int value) {
        this.fecha = value;
    }

    /**
     * Gets the value of the pagina property.
     * 
     */
    public int getPagina() {
        return pagina;
    }

    /**
     * Sets the value of the pagina property.
     * 
     */
    public void setPagina(int value) {
        this.pagina = value;
    }

    /**
     * Gets the value of the lineas property.
     * 
     */
    public int getLineas() {
        return lineas;
    }

    /**
     * Sets the value of the lineas property.
     * 
     */
    public void setLineas(int value) {
        this.lineas = value;
    }

    /**
     * Gets the value of the contenido property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContenido() {
        return contenido;
    }

    /**
     * Sets the value of the contenido property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContenido(String value) {
        this.contenido = value;
    }

    /**
     * Gets the value of the observaciones property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * Sets the value of the observaciones property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObservaciones(String value) {
        this.observaciones = value;
    }

    /**
     * Gets the value of the leido property.
     * 
     */
    public boolean isLeido() {
        return leido;
    }

    /**
     * Sets the value of the leido property.
     * 
     */
    public void setLeido(boolean value) {
        this.leido = value;
    }

    /**
     * Gets the value of the errorfecha property.
     * 
     */
    public boolean isErrorfecha() {
        return errorfecha;
    }

    /**
     * Sets the value of the errorfecha property.
     * 
     */
    public void setErrorfecha(boolean value) {
        this.errorfecha = value;
    }

}
