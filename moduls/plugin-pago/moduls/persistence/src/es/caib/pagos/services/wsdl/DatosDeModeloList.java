/**
 * DatosDeModeloList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.pagos.services.wsdl;

public class DatosDeModeloList  implements java.io.Serializable {
    private es.caib.pagos.services.wsdl.DatosDeModelo[] modelos;

    private java.lang.String error;

    public DatosDeModeloList() {
    }

    public DatosDeModeloList(
           es.caib.pagos.services.wsdl.DatosDeModelo[] modelos,
           java.lang.String error) {
           this.modelos = modelos;
           this.error = error;
    }


    /**
     * Gets the modelos value for this DatosDeModeloList.
     * 
     * @return modelos
     */
    public es.caib.pagos.services.wsdl.DatosDeModelo[] getModelos() {
        return modelos;
    }


    /**
     * Sets the modelos value for this DatosDeModeloList.
     * 
     * @param modelos
     */
    public void setModelos(es.caib.pagos.services.wsdl.DatosDeModelo[] modelos) {
        this.modelos = modelos;
    }


    /**
     * Gets the error value for this DatosDeModeloList.
     * 
     * @return error
     */
    public java.lang.String getError() {
        return error;
    }


    /**
     * Sets the error value for this DatosDeModeloList.
     * 
     * @param error
     */
    public void setError(java.lang.String error) {
        this.error = error;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosDeModeloList)) return false;
        DatosDeModeloList other = (DatosDeModeloList) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.modelos==null && other.getModelos()==null) || 
             (this.modelos!=null &&
              java.util.Arrays.equals(this.modelos, other.getModelos()))) &&
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
        if (getModelos() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getModelos());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getModelos(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getError() != null) {
            _hashCode += getError().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosDeModeloList.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://atib.es/", "DatosDeModeloList"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("modelos");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atib.es/", "modelos"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://atib.es/", "DatosDeModelo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://atib.es/", "DatosDeModelo"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("error");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atib.es/", "error"));
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
