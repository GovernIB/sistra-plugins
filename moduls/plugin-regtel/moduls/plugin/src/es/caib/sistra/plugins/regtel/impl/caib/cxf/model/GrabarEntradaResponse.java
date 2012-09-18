
package es.caib.sistra.plugins.regtel.impl.caib.cxf.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="grabarEntradaReturn" type="{urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS}ParametrosRegistroEntradaWS"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "grabarEntradaReturn"
})
@XmlRootElement(name = "grabarEntradaResponse")
public class GrabarEntradaResponse {

    @XmlElement(namespace = "urn:es:caib:regweb:ws:v1:model:RegwebFacade", required = true)
    protected ParametrosRegistroEntradaWS grabarEntradaReturn;

    /**
     * Gets the value of the grabarEntradaReturn property.
     * 
     * @return
     *     possible object is
     *     {@link ParametrosRegistroEntradaWS }
     *     
     */
    public ParametrosRegistroEntradaWS getGrabarEntradaReturn() {
        return grabarEntradaReturn;
    }

    /**
     * Sets the value of the grabarEntradaReturn property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParametrosRegistroEntradaWS }
     *     
     */
    public void setGrabarEntradaReturn(ParametrosRegistroEntradaWS value) {
        this.grabarEntradaReturn = value;
    }

}
