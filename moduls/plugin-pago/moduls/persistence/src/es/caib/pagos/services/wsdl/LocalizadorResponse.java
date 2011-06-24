/**
 * LocalizadorResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.pagos.services.wsdl;

public class LocalizadorResponse  implements java.io.Serializable {
    private java.lang.String localizadorResult;

    public LocalizadorResponse() {
    }

    public LocalizadorResponse(
           java.lang.String localizadorResult) {
           this.localizadorResult = localizadorResult;
    }


    /**
     * Gets the localizadorResult value for this LocalizadorResponse.
     * 
     * @return localizadorResult
     */
    public java.lang.String getLocalizadorResult() {
        return localizadorResult;
    }


    /**
     * Sets the localizadorResult value for this LocalizadorResponse.
     * 
     * @param localizadorResult
     */
    public void setLocalizadorResult(java.lang.String localizadorResult) {
        this.localizadorResult = localizadorResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof LocalizadorResponse)) return false;
        LocalizadorResponse other = (LocalizadorResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.localizadorResult==null && other.getLocalizadorResult()==null) || 
             (this.localizadorResult!=null &&
              this.localizadorResult.equals(other.getLocalizadorResult())));
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
        if (getLocalizadorResult() != null) {
            _hashCode += getLocalizadorResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(LocalizadorResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.atib.es/services/", ">LocalizadorResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("localizadorResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.atib.es/services/", "LocalizadorResult"));
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
