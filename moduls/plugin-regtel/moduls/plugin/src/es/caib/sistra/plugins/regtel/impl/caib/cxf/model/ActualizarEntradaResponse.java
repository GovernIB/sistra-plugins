
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
 *         &lt;element name="actualizarEntradaReturn" type="{urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS}ParametrosRegistroEntradaWS"/>
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
    "actualizarEntradaReturn"
})
@XmlRootElement(name = "actualizarEntradaResponse")
public class ActualizarEntradaResponse {

    @XmlElement(namespace = "urn:es:caib:regweb:ws:v1:model:RegwebFacade", required = true)
    protected ParametrosRegistroEntradaWS actualizarEntradaReturn;

    /**
     * Gets the value of the actualizarEntradaReturn property.
     * 
     * @return
     *     possible object is
     *     {@link ParametrosRegistroEntradaWS }
     *     
     */
    public ParametrosRegistroEntradaWS getActualizarEntradaReturn() {
        return actualizarEntradaReturn;
    }

    /**
     * Sets the value of the actualizarEntradaReturn property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParametrosRegistroEntradaWS }
     *     
     */
    public void setActualizarEntradaReturn(ParametrosRegistroEntradaWS value) {
        this.actualizarEntradaReturn = value;
    }

}
