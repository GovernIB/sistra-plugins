/**
 * Ws4Response.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.pagos.services.wsdl;

public class Ws4Response  implements java.io.Serializable {
    private java.lang.String ws4Result;

    public Ws4Response() {
    }

    public Ws4Response(
           java.lang.String ws4Result) {
           this.ws4Result = ws4Result;
    }


    /**
     * Gets the ws4Result value for this Ws4Response.
     * 
     * @return ws4Result
     */
    public java.lang.String getWs4Result() {
        return ws4Result;
    }


    /**
     * Sets the ws4Result value for this Ws4Response.
     * 
     * @param ws4Result
     */
    public void setWs4Result(java.lang.String ws4Result) {
        this.ws4Result = ws4Result;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Ws4Response)) return false;
        Ws4Response other = (Ws4Response) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ws4Result==null && other.getWs4Result()==null) || 
             (this.ws4Result!=null &&
              this.ws4Result.equals(other.getWs4Result())));
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
        if (getWs4Result() != null) {
            _hashCode += getWs4Result().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Ws4Response.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.atib.es/services/", ">ws4Response"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ws4Result");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.atib.es/services/", "ws4Result"));
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
