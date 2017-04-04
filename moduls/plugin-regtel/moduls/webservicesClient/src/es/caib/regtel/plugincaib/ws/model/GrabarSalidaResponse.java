
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
 *         &lt;element name="grabarSalidaReturn" type="{urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS}ParametrosRegistroSalidaWS"/>
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
    "grabarSalidaReturn"
})
@XmlRootElement(name = "grabarSalidaResponse")
public class GrabarSalidaResponse {

    @XmlElement(namespace = "urn:es:caib:regweb:ws:v1:model:RegwebFacade", required = true)
    protected ParametrosRegistroSalidaWS grabarSalidaReturn;

    /**
     * Gets the value of the grabarSalidaReturn property.
     * 
     * @return
     *     possible object is
     *     {@link ParametrosRegistroSalidaWS }
     *     
     */
    public ParametrosRegistroSalidaWS getGrabarSalidaReturn() {
        return grabarSalidaReturn;
    }

    /**
     * Sets the value of the grabarSalidaReturn property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParametrosRegistroSalidaWS }
     *     
     */
    public void setGrabarSalidaReturn(ParametrosRegistroSalidaWS value) {
        this.grabarSalidaReturn = value;
    }

}
