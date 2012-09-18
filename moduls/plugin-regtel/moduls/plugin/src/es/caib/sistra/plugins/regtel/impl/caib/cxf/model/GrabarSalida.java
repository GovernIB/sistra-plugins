
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
 *         &lt;element name="parametrosSalida" type="{urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS}ParametrosRegistroSalidaWS"/>
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
    "parametrosSalida"
})
@XmlRootElement(name = "grabarSalida")
public class GrabarSalida {

    @XmlElement(namespace = "urn:es:caib:regweb:ws:v1:model:RegwebFacade", required = true)
    protected ParametrosRegistroSalidaWS parametrosSalida;

    /**
     * Gets the value of the parametrosSalida property.
     * 
     * @return
     *     possible object is
     *     {@link ParametrosRegistroSalidaWS }
     *     
     */
    public ParametrosRegistroSalidaWS getParametrosSalida() {
        return parametrosSalida;
    }

    /**
     * Sets the value of the parametrosSalida property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParametrosRegistroSalidaWS }
     *     
     */
    public void setParametrosSalida(ParametrosRegistroSalidaWS value) {
        this.parametrosSalida = value;
    }

}
