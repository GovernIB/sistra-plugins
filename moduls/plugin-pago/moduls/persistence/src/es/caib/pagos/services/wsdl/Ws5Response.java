/**
 * Ws5Response.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.pagos.services.wsdl;

public class Ws5Response  implements java.io.Serializable {
    private java.lang.String ws5Result;

    public Ws5Response() {
    }

    public Ws5Response(
           java.lang.String ws5Result) {
           this.ws5Result = ws5Result;
    }


    /**
     * Gets the ws5Result value for this Ws5Response.
     * 
     * @return ws5Result
     */
    public java.lang.String getWs5Result() {
        return ws5Result;
    }


    /**
     * Sets the ws5Result value for this Ws5Response.
     * 
     * @param ws5Result
     */
    public void setWs5Result(java.lang.String ws5Result) {
        this.ws5Result = ws5Result;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Ws5Response)) return false;
        Ws5Response other = (Ws5Response) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ws5Result==null && other.getWs5Result()==null) || 
             (this.ws5Result!=null &&
              this.ws5Result.equals(other.getWs5Result())));
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
        if (getWs5Result() != null) {
            _hashCode += getWs5Result().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Ws5Response.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.atib.es/services/", ">ws5Response"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ws5Result");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.atib.es/services/", "ws5Result"));
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
