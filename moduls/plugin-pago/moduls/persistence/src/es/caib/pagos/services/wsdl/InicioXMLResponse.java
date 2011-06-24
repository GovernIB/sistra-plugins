/**
 * InicioXMLResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.pagos.services.wsdl;

public class InicioXMLResponse  implements java.io.Serializable {
    private java.lang.String inicioXMLResult;

    public InicioXMLResponse() {
    }

    public InicioXMLResponse(
           java.lang.String inicioXMLResult) {
           this.inicioXMLResult = inicioXMLResult;
    }


    /**
     * Gets the inicioXMLResult value for this InicioXMLResponse.
     * 
     * @return inicioXMLResult
     */
    public java.lang.String getInicioXMLResult() {
        return inicioXMLResult;
    }


    /**
     * Sets the inicioXMLResult value for this InicioXMLResponse.
     * 
     * @param inicioXMLResult
     */
    public void setInicioXMLResult(java.lang.String inicioXMLResult) {
        this.inicioXMLResult = inicioXMLResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InicioXMLResponse)) return false;
        InicioXMLResponse other = (InicioXMLResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.inicioXMLResult==null && other.getInicioXMLResult()==null) || 
             (this.inicioXMLResult!=null &&
              this.inicioXMLResult.equals(other.getInicioXMLResult())));
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
        if (getInicioXMLResult() != null) {
            _hashCode += getInicioXMLResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(InicioXMLResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.atib.es/services/", ">inicioXMLResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inicioXMLResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.atib.es/services/", "inicioXMLResult"));
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
