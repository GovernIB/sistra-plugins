
package es.caib.sistra.plugins.regtel.impl.caib.cxf.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for listaErroresEntrada complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listaErroresEntrada">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="errores" type="{urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS}errorEntrada" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listaErroresEntrada", namespace = "urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", propOrder = {
    "errores"
})
public class ListaErroresEntrada {

    protected List<ErrorEntrada> errores;

    /**
     * Gets the value of the errores property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the errores property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getErrores().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ErrorEntrada }
     * 
     * 
     */
    public List<ErrorEntrada> getErrores() {
        if (errores == null) {
            errores = new ArrayList<ErrorEntrada>();
        }
        return this.errores;
    }

}
