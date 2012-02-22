/**
 * DatosRespuesta046.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.pagos.services.wsdl;

public class DatosRespuesta046  implements java.io.Serializable {
    private java.lang.String token;

    private java.lang.String localizador;

    private java.lang.Integer codError;

    private java.lang.String textError;

    private java.lang.String estadoPago;

    private java.lang.String fechaPago;

    private java.lang.String firma;

    public DatosRespuesta046() {
    }

    public DatosRespuesta046(
           java.lang.String token,
           java.lang.String localizador,
           java.lang.Integer codError,
           java.lang.String textError,
           java.lang.String estadoPago,
           java.lang.String fechaPago,
           java.lang.String firma) {
           this.token = token;
           this.localizador = localizador;
           this.codError = codError;
           this.textError = textError;
           this.estadoPago = estadoPago;
           this.fechaPago = fechaPago;
           this.firma = firma;
    }


    /**
     * Gets the token value for this DatosRespuesta046.
     * 
     * @return token
     */
    public java.lang.String getToken() {
        return token;
    }


    /**
     * Sets the token value for this DatosRespuesta046.
     * 
     * @param token
     */
    public void setToken(java.lang.String token) {
        this.token = token;
    }


    /**
     * Gets the localizador value for this DatosRespuesta046.
     * 
     * @return localizador
     */
    public java.lang.String getLocalizador() {
        return localizador;
    }


    /**
     * Sets the localizador value for this DatosRespuesta046.
     * 
     * @param localizador
     */
    public void setLocalizador(java.lang.String localizador) {
        this.localizador = localizador;
    }


    /**
     * Gets the codError value for this DatosRespuesta046.
     * 
     * @return codError
     */
    public java.lang.Integer getCodError() {
        return codError;
    }


    /**
     * Sets the codError value for this DatosRespuesta046.
     * 
     * @param codError
     */
    public void setCodError(java.lang.Integer codError) {
        this.codError = codError;
    }


    /**
     * Gets the textError value for this DatosRespuesta046.
     * 
     * @return textError
     */
    public java.lang.String getTextError() {
        return textError;
    }


    /**
     * Sets the textError value for this DatosRespuesta046.
     * 
     * @param textError
     */
    public void setTextError(java.lang.String textError) {
        this.textError = textError;
    }


    /**
     * Gets the estadoPago value for this DatosRespuesta046.
     * 
     * @return estadoPago
     */
    public java.lang.String getEstadoPago() {
        return estadoPago;
    }


    /**
     * Sets the estadoPago value for this DatosRespuesta046.
     * 
     * @param estadoPago
     */
    public void setEstadoPago(java.lang.String estadoPago) {
        this.estadoPago = estadoPago;
    }


    /**
     * Gets the fechaPago value for this DatosRespuesta046.
     * 
     * @return fechaPago
     */
    public java.lang.String getFechaPago() {
        return fechaPago;
    }


    /**
     * Sets the fechaPago value for this DatosRespuesta046.
     * 
     * @param fechaPago
     */
    public void setFechaPago(java.lang.String fechaPago) {
        this.fechaPago = fechaPago;
    }


    /**
     * Gets the firma value for this DatosRespuesta046.
     * 
     * @return firma
     */
    public java.lang.String getFirma() {
        return firma;
    }


    /**
     * Sets the firma value for this DatosRespuesta046.
     * 
     * @param firma
     */
    public void setFirma(java.lang.String firma) {
        this.firma = firma;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosRespuesta046)) return false;
        DatosRespuesta046 other = (DatosRespuesta046) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.token==null && other.getToken()==null) || 
             (this.token!=null &&
              this.token.equals(other.getToken()))) &&
            ((this.localizador==null && other.getLocalizador()==null) || 
             (this.localizador!=null &&
              this.localizador.equals(other.getLocalizador()))) &&
            ((this.codError==null && other.getCodError()==null) || 
             (this.codError!=null &&
              this.codError.equals(other.getCodError()))) &&
            ((this.textError==null && other.getTextError()==null) || 
             (this.textError!=null &&
              this.textError.equals(other.getTextError()))) &&
            ((this.estadoPago==null && other.getEstadoPago()==null) || 
             (this.estadoPago!=null &&
              this.estadoPago.equals(other.getEstadoPago()))) &&
            ((this.fechaPago==null && other.getFechaPago()==null) || 
             (this.fechaPago!=null &&
              this.fechaPago.equals(other.getFechaPago()))) &&
            ((this.firma==null && other.getFirma()==null) || 
             (this.firma!=null &&
              this.firma.equals(other.getFirma())));
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
        if (getToken() != null) {
            _hashCode += getToken().hashCode();
        }
        if (getLocalizador() != null) {
            _hashCode += getLocalizador().hashCode();
        }
        if (getCodError() != null) {
            _hashCode += getCodError().hashCode();
        }
        if (getTextError() != null) {
            _hashCode += getTextError().hashCode();
        }
        if (getEstadoPago() != null) {
            _hashCode += getEstadoPago().hashCode();
        }
        if (getFechaPago() != null) {
            _hashCode += getFechaPago().hashCode();
        }
        if (getFirma() != null) {
            _hashCode += getFirma().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosRespuesta046.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://atib.es/", "DatosRespuesta046"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("token");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atib.es/", "Token"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("localizador");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atib.es/", "Localizador"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codError");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atib.es/", "CodError"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("textError");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atib.es/", "TextError"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estadoPago");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atib.es/", "EstadoPago"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaPago");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atib.es/", "FechaPago"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("firma");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atib.es/", "Firma"));
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
