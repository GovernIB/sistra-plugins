
package es.caib.regtel.plugincaib.ws.model;

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
 *         &lt;element name="actualizarSalidaReturn" type="{urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS}ParametrosRegistroSalidaWS"/>
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
    "actualizarSalidaReturn"
})
@XmlRootElement(name = "actualizarSalidaResponse")
public class ActualizarSalidaResponse {

    @XmlElement(namespace = "urn:es:caib:regweb:ws:v1:model:RegwebFacade", required = true)
    protected ParametrosRegistroSalidaWS actualizarSalidaReturn;

    /**
     * Gets the value of the actualizarSalidaReturn property.
     * 
     * @return
     *     possible object is
     *     {@link ParametrosRegistroSalidaWS }
     *     
     */
    public ParametrosRegistroSalidaWS getActualizarSalidaReturn() {
        return actualizarSalidaReturn;
    }

    /**
     * Sets the value of the actualizarSalidaReturn property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParametrosRegistroSalidaWS }
     *     
     */
    public void setActualizarSalidaReturn(ParametrosRegistroSalidaWS value) {
        this.actualizarSalidaReturn = value;
    }

}
