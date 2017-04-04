
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
 *         &lt;element name="anularSalidaReturn" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    "anularSalidaReturn"
})
@XmlRootElement(name = "anularSalidaResponse")
public class AnularSalidaResponse {

    @XmlElement(namespace = "urn:es:caib:regweb:ws:v1:model:RegwebFacade")
    protected boolean anularSalidaReturn;

    /**
     * Gets the value of the anularSalidaReturn property.
     * 
     */
    public boolean isAnularSalidaReturn() {
        return anularSalidaReturn;
    }

    /**
     * Sets the value of the anularSalidaReturn property.
     * 
     */
    public void setAnularSalidaReturn(boolean value) {
        this.anularSalidaReturn = value;
    }

}
