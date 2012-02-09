/**
 * Service_TasaSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.pagos.services.wsdl;

public interface Service_TasaSoap extends java.rmi.Remote {

    /**
     * Dado un localizador, devuelve un array de bytes de la carta
     * de pago o justificante, generada en PDF.
     */
    public byte[] getPdf046(java.lang.String localizador, java.lang.String importeaingresar, java.lang.String nifsujetopasivo, java.lang.String fechacreacion) throws java.rmi.RemoteException;

    /**
     * Inicio de Servico con un String del XML en base64: Devuelve
     * un token valido para 20 minutos para efectuar el Pago
     */
    public es.caib.pagos.services.wsdl.DatosRespuesta046 inserta046(java.lang.String sB64) throws java.rmi.RemoteException;

    /**
     * Devuelve un error si existe algún servicio con problemas
     */
    public es.caib.pagos.services.wsdl.DatosRespuesta046 ping046(java.lang.String incoming) throws java.rmi.RemoteException;

    /**
     * Devuelve los datos de una tasa dado su identificador.
     */
    public es.caib.pagos.services.wsdl.DatosTasa046 consultaDatosTasa046(java.lang.String identificador) throws java.rmi.RemoteException;

    /**
     * Analiza el estado del Localizador
     */
    public es.caib.pagos.services.wsdl.DatosRespuesta046 estado046(java.lang.String localizador) throws java.rmi.RemoteException;

    /**
     * Dado un rango de fechas, devuelve una lista de DATOSDEMODELO.
     */
    public es.caib.pagos.services.wsdl.DatosDeModeloList getModelos_OBJ(java.lang.String fechaInicio, java.lang.String fechaFin, java.lang.String identificador) throws java.rmi.RemoteException;

    /**
     * Soicita una url para realizar el pago B64
     */
    public java.lang.String getUrlPago(java.lang.String[] refsModelos, java.lang.String codigoEntidad) throws java.rmi.RemoteException;

    /**
     * Realiza un intento de pago con tarjeta de n modelos en el SGP.
     * Devuelve los datos del pago del SGP
     */
    public es.caib.pagos.services.wsdl.DatosRespuestaPago pagarConTarjeta(java.lang.String[] refsModelos, java.lang.String numeroTarjeta, java.lang.String caducidadTarjeta, java.lang.String titularTarjeta, java.lang.String cvvTarjeta) throws java.rmi.RemoteException;
}
