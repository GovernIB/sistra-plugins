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
     * de pago o justificante generado, en PDF.
     */
    public byte[] getPdf046(java.lang.String localizador, java.lang.String importeaingresar, java.lang.String nifsujetopasivo, java.lang.String fechacreacion) throws java.rmi.RemoteException;

    /**
     * Inicio del trámite con un String del XML en base64. Devuelve
     * un registro de tipo DATOSRESPUESTA046, con el que se puede realizar
     * el pago de la tasa descrita en el parámetro XML
     */
    public es.caib.pagos.services.wsdl.DatosRespuesta046 inserta046(java.lang.String sB64) throws java.rmi.RemoteException;

    /**
     * Comprobación del estado del servicio.
     */
    public es.caib.pagos.services.wsdl.DatosRespuesta046 ping046(java.lang.String incoming) throws java.rmi.RemoteException;

    /**
     * Devuelve los datos de una tasa dado su identificador.
     */
    public es.caib.pagos.services.wsdl.DatosTasa046 consultaDatosTasa046(java.lang.String identificador) throws java.rmi.RemoteException;

    /**
     * Analiza el estado del pago de un localizador
     */
    public es.caib.pagos.services.wsdl.DatosRespuesta046 estado046(java.lang.String localizador) throws java.rmi.RemoteException;

    /**
     * Dado un guid de pago, devuelve una lista del tipo de datos
     * DATOSDEMODELO.
     */
    public es.caib.pagos.services.wsdl.DatosDeModeloList getModelos_OBJ_Token(java.lang.String tokenPago) throws java.rmi.RemoteException;

    /**
     * Dado un rango de fechas, devuelve una lista del tipo de datos
     * DATOSDEMODELO.
     */
    public es.caib.pagos.services.wsdl.DatosDeModeloList getModelos_OBJ(java.lang.String fechaInicio, java.lang.String fechaFin, java.lang.String identificador) throws java.rmi.RemoteException;

    /**
     * Solicita una url para realizar el pago de una lista de modelos,
     * en la entidad bancaria seleccionada.
     */
    public es.caib.pagos.services.wsdl.DatosRespuestaGetUrlPago getUrlPago(java.lang.String[] refsModelos, java.lang.String codigoEntidad, java.lang.String urlDeVuelta) throws java.rmi.RemoteException;

    /**
     * Realiza un intento de pago con tarjeta de n localizadores.
     * Devuelve los datos del pago en un registro del tipo DATOSRESPUESTAPAGO.
     */
    public es.caib.pagos.services.wsdl.DatosRespuestaPago pagarConTarjeta(java.lang.String[] refsModelos, java.lang.String datosTarjeta) throws java.rmi.RemoteException;
}
