
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
 *         &lt;element name="buscarDocumentosReturn" type="{urn:es:caib:regweb:ws:v1:model:RegwebFacade}listaResultados"/>
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
    "buscarDocumentosReturn"
})
@XmlRootElement(name = "buscarDocumentosResponse")
public class BuscarDocumentosResponse {

    @XmlElement(namespace = "urn:es:caib:regweb:ws:v1:model:RegwebFacade", required = true)
    protected ListaResultados buscarDocumentosReturn;

    /**
     * Gets the value of the buscarDocumentosReturn property.
     * 
     * @return
     *     possible object is
     *     {@link ListaResultados }
     *     
     */
    public ListaResultados getBuscarDocumentosReturn() {
        return buscarDocumentosReturn;
    }

    /**
     * Sets the value of the buscarDocumentosReturn property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListaResultados }
     *     
     */
    public void setBuscarDocumentosReturn(ListaResultados value) {
        this.buscarDocumentosReturn = value;
    }

}
