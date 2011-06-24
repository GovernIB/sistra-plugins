/**
 * ServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.pagos.services.wsdl;

public interface ServiceSoap extends java.rmi.Remote {

    /**
     * Inicio de Servico con un String del XML en base64: Devuelve
     * un token valido para 20 minutos para efectuar el Pago
     */
    public java.lang.String inicio(java.lang.String sB64) throws java.rmi.RemoteException;

    /**
     * Inicio de Servico con XMLDocument: Devuelve untoken valido
     * para 20 minutos para efectuar el Pago
     */
    public java.lang.String inicioXML(es.caib.pagos.services.wsdl.InicioXMLXIn xIn) throws java.rmi.RemoteException;

    /**
     * Devuelve el texto enviado
     */
    public java.lang.String ping(java.lang.String incoming) throws java.rmi.RemoteException;

    /**
     * Devuelve el precio de una tasa en centimos.
     */
    public java.lang.String ws1(java.lang.String idTasa) throws java.rmi.RemoteException;

    /**
     * Analiza el estado del Localizador, Devuelve un XML firmado
     */
    public java.lang.String ws4(java.lang.String localizador) throws java.rmi.RemoteException;

    /**
     * Obtener un Token para el acceso del usuario al Portal Del Contribuyente
     * para comprobar el pago. Enta con el localizador
     */
    public java.lang.String ws5(java.lang.String localizador) throws java.rmi.RemoteException;

    /**
     * Devuelve Localizador.
     */
    public java.lang.String localizador(java.lang.String modelo) throws java.rmi.RemoteException;

    /**
     * Devuelve la versión de la aplicación.
     */
    public java.lang.String getVersion(int IDApp) throws java.rmi.RemoteException;
}
