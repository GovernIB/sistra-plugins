/**
 * DatosTasa046.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.pagos.services.wsdl;

public class DatosTasa046  implements java.io.Serializable {
    private java.lang.Integer idConcepto;

    private java.lang.String codigo;

    private java.lang.String identificador;

    private java.lang.String descripcion;

    private java.lang.String importe;

    private java.lang.Integer codError;

    private java.lang.String textError;

    public DatosTasa046() {
    }

    public DatosTasa046(
           java.lang.Integer idConcepto,
           java.lang.String codigo,
           java.lang.String identificador,
           java.lang.String descripcion,
           java.lang.String importe,
           java.lang.Integer codError,
           java.lang.String textError) {
           this.idConcepto = idConcepto;
           this.codigo = codigo;
           this.identificador = identificador;
           this.descripcion = descripcion;
           this.importe = importe;
           this.codError = codError;
           this.textError = textError;
    }


    /**
     * Gets the idConcepto value for this DatosTasa046.
     * 
     * @return idConcepto
     */
    public java.lang.Integer getIdConcepto() {
        return idConcepto;
    }


    /**
     * Sets the idConcepto value for this DatosTasa046.
     * 
     * @param idConcepto
     */
    public void setIdConcepto(java.lang.Integer idConcepto) {
        this.idConcepto = idConcepto;
    }


    /**
     * Gets the codigo value for this DatosTasa046.
     * 
     * @return codigo
     */
    public java.lang.String getCodigo() {
        return codigo;
    }


    /**
     * Sets the codigo value for this DatosTasa046.
     * 
     * @param codigo
     */
    public void setCodigo(java.lang.String codigo) {
        this.codigo = codigo;
    }


    /**
     * Gets the identificador value for this DatosTasa046.
     * 
     * @return identificador
     */
    public java.lang.String getIdentificador() {
        return identificador;
    }


    /**
     * Sets the identificador value for this DatosTasa046.
     * 
     * @param identificador
     */
    public void setIdentificador(java.lang.String identificador) {
        this.identificador = identificador;
    }


    /**
     * Gets the descripcion value for this DatosTasa046.
     * 
     * @return descripcion
     */
    public java.lang.String getDescripcion() {
        return descripcion;
    }


    /**
     * Sets the descripcion value for this DatosTasa046.
     * 
     * @param descripcion
     */
    public void setDescripcion(java.lang.String descripcion) {
        this.descripcion = descripcion;
    }


    /**
     * Gets the importe value for this DatosTasa046.
     * 
     * @return importe
     */
    public java.lang.String getImporte() {
        return importe;
    }


    /**
     * Sets the importe value for this DatosTasa046.
     * 
     * @param importe
     */
    public void setImporte(java.lang.String importe) {
        this.importe = importe;
    }


    /**
     * Gets the codError value for this DatosTasa046.
     * 
     * @return codError
     */
    public java.lang.Integer getCodError() {
        return codError;
    }


    /**
     * Sets the codError value for this DatosTasa046.
     * 
     * @param codError
     */
    public void setCodError(java.lang.Integer codError) {
        this.codError = codError;
    }


    /**
     * Gets the textError value for this DatosTasa046.
     * 
     * @return textError
     */
    public java.lang.String getTextError() {
        return textError;
    }


    /**
     * Sets the textError value for this DatosTasa046.
     * 
     * @param textError
     */
    public void setTextError(java.lang.String textError) {
        this.textError = textError;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosTasa046)) return false;
        DatosTasa046 other = (DatosTasa046) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idConcepto==null && other.getIdConcepto()==null) || 
             (this.idConcepto!=null &&
              this.idConcepto.equals(other.getIdConcepto()))) &&
            ((this.codigo==null && other.getCodigo()==null) || 
             (this.codigo!=null &&
              this.codigo.equals(other.getCodigo()))) &&
            ((this.identificador==null && other.getIdentificador()==null) || 
             (this.identificador!=null &&
              this.identificador.equals(other.getIdentificador()))) &&
            ((this.descripcion==null && other.getDescripcion()==null) || 
             (this.descripcion!=null &&
              this.descripcion.equals(other.getDescripcion()))) &&
            ((this.importe==null && other.getImporte()==null) || 
             (this.importe!=null &&
              this.importe.equals(other.getImporte()))) &&
            ((this.codError==null && other.getCodError()==null) || 
             (this.codError!=null &&
              this.codError.equals(other.getCodError()))) &&
            ((this.textError==null && other.getTextError()==null) || 
             (this.textError!=null &&
              this.textError.equals(other.getTextError())));
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
        if (getIdConcepto() != null) {
            _hashCode += getIdConcepto().hashCode();
        }
        if (getCodigo() != null) {
            _hashCode += getCodigo().hashCode();
        }
        if (getIdentificador() != null) {
            _hashCode += getIdentificador().hashCode();
        }
        if (getDescripcion() != null) {
            _hashCode += getDescripcion().hashCode();
        }
        if (getImporte() != null) {
            _hashCode += getImporte().hashCode();
        }
        if (getCodError() != null) {
            _hashCode += getCodError().hashCode();
        }
        if (getTextError() != null) {
            _hashCode += getTextError().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosTasa046.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://atib.es/", "DatosTasa046"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idConcepto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atib.es/", "IdConcepto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atib.es/", "Codigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identificador");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atib.es/", "Identificador"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atib.es/", "Descripcion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importe");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atib.es/", "Importe"));
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
