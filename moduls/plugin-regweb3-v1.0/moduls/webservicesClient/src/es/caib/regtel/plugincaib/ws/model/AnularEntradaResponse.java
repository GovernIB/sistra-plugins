
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
 *         &lt;element name="anularEntradaReturn" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    "anularEntradaReturn"
})
@XmlRootElement(name = "anularEntradaResponse")
public class AnularEntradaResponse {

    @XmlElement(namespace = "urn:es:caib:regweb:ws:v1:model:RegwebFacade")
    protected boolean anularEntradaReturn;

    /**
     * Gets the value of the anularEntradaReturn property.
     * 
     */
    public boolean isAnularEntradaReturn() {
        return anularEntradaReturn;
    }

    /**
     * Sets the value of the anularEntradaReturn property.
     * 
     */
    public void setAnularEntradaReturn(boolean value) {
        this.anularEntradaReturn = value;
    }

}
