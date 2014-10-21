
package es.caib.regtel.plugincaib.ws.services;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.2.7
 * Mon Oct 20 18:14:43 CEST 2014
 * Generated source version: 2.2.7
 * 
 */

@WebFault(name = "fault", targetNamespace = "urn:es:caib:regweb:ws:v1:model:RegwebFacade")
public class RegwebFacadeException extends Exception {
    public static final long serialVersionUID = 20141020181443L;
    
    private es.caib.regtel.plugincaib.ws.model.RegwebFacadeException fault;

    public RegwebFacadeException() {
        super();
    }
    
    public RegwebFacadeException(String message) {
        super(message);
    }
    
    public RegwebFacadeException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegwebFacadeException(String message, es.caib.regtel.plugincaib.ws.model.RegwebFacadeException fault) {
        super(message);
        this.fault = fault;
    }

    public RegwebFacadeException(String message, es.caib.regtel.plugincaib.ws.model.RegwebFacadeException fault, Throwable cause) {
        super(message, cause);
        this.fault = fault;
    }

    public es.caib.regtel.plugincaib.ws.model.RegwebFacadeException getFaultInfo() {
        return this.fault;
    }
}
