/**
 * DatosRespuestaPago.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.pagos.services.wsdl;

public class DatosRespuestaPago  implements java.io.Serializable {
    private java.lang.String refPago;

    private boolean pagado;

    private java.lang.String error;

    public DatosRespuestaPago() {
    }

    public DatosRespuestaPago(
           java.lang.String refPago,
           boolean pagado,
           java.lang.String error) {
           this.refPago = refPago;
           this.pagado = pagado;
           this.error = error;
    }


    /**
     * Gets the refPago value for this DatosRespuestaPago.
     * 
     * @return refPago
     */
    public java.lang.String getRefPago() {
        return refPago;
    }


    /**
     * Sets the refPago value for this DatosRespuestaPago.
     * 
     * @param refPago
     */
    public void setRefPago(java.lang.String refPago) {
        this.refPago = refPago;
    }


    /**
     * Gets the pagado value for this DatosRespuestaPago.
     * 
     * @return pagado
     */
    public boolean isPagado() {
        return pagado;
    }


    /**
     * Sets the pagado value for this DatosRespuestaPago.
     * 
     * @param pagado
     */
    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }


    /**
     * Gets the error value for this DatosRespuestaPago.
     * 
     * @return error
     */
    public java.lang.String getError() {
        return error;
    }


    /**
     * Sets the error value for this DatosRespuestaPago.
     * 
     * @param error
     */
    public void setError(java.lang.String error) {
        this.error = error;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosRespuestaPago)) return false;
        DatosRespuestaPago other = (DatosRespuestaPago) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.refPago==null && other.getRefPago()==null) || 
             (this.refPago!=null &&
              this.refPago.equals(other.getRefPago()))) &&
            this.pagado == other.isPagado() &&
            ((this.error==null && other.getError()==null) || 
             (this.error!=null &&
              this.error.equals(other.getError())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getRefPago() != null) {
            _hashCode += getRefPago().hashCode();
        }
        _hashCode += (isPagado() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getError() != null) {
            _hashCode += getError().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosRespuestaPago.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://atib.es/", "DatosRespuestaPago"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("refPago");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atib.es/", "RefPago"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pagado");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atib.es/", "Pagado"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("error");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atib.es/", "Error"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
