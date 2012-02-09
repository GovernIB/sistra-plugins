/**
 * DatosDeModelo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package es.caib.pagos.services.wsdl;

public class DatosDeModelo  implements java.io.Serializable {
    private java.lang.String localizador;

    private java.lang.String nifsujetopasivo;

    private java.lang.String descripcion;

    private java.lang.String identificador;

    private java.math.BigDecimal importe;

    private java.util.Calendar fechaalta;

    private java.util.Calendar fechapago;

    public DatosDeModelo() {
    }

    public DatosDeModelo(
           java.lang.String localizador,
           java.lang.String nifsujetopasivo,
           java.lang.String descripcion,
           java.lang.String identificador,
           java.math.BigDecimal importe,
           java.util.Calendar fechaalta,
           java.util.Calendar fechapago) {
           this.localizador = localizador;
           this.nifsujetopasivo = nifsujetopasivo;
           this.descripcion = descripcion;
           this.identificador = identificador;
           this.importe = importe;
           this.fechaalta = fechaalta;
           this.fechapago = fechapago;
    }


    /**
     * Gets the localizador value for this DatosDeModelo.
     * 
     * @return localizador
     */
    public java.lang.String getLocalizador() {
        return localizador;
    }


    /**
     * Sets the localizador value for this DatosDeModelo.
     * 
     * @param localizador
     */
    public void setLocalizador(java.lang.String localizador) {
        this.localizador = localizador;
    }


    /**
     * Gets the nifsujetopasivo value for this DatosDeModelo.
     * 
     * @return nifsujetopasivo
     */
    public java.lang.String getNifsujetopasivo() {
        return nifsujetopasivo;
    }


    /**
     * Sets the nifsujetopasivo value for this DatosDeModelo.
     * 
     * @param nifsujetopasivo
     */
    public void setNifsujetopasivo(java.lang.String nifsujetopasivo) {
        this.nifsujetopasivo = nifsujetopasivo;
    }


    /**
     * Gets the descripcion value for this DatosDeModelo.
     * 
     * @return descripcion
     */
    public java.lang.String getDescripcion() {
        return descripcion;
    }


    /**
     * Sets the descripcion value for this DatosDeModelo.
     * 
     * @param descripcion
     */
    public void setDescripcion(java.lang.String descripcion) {
        this.descripcion = descripcion;
    }


    /**
     * Gets the identificador value for this DatosDeModelo.
     * 
     * @return identificador
     */
    public java.lang.String getIdentificador() {
        return identificador;
    }


    /**
     * Sets the identificador value for this DatosDeModelo.
     * 
     * @param identificador
     */
    public void setIdentificador(java.lang.String identificador) {
        this.identificador = identificador;
    }


    /**
     * Gets the importe value for this DatosDeModelo.
     * 
     * @return importe
     */
    public java.math.BigDecimal getImporte() {
        return importe;
    }


    /**
     * Sets the importe value for this DatosDeModelo.
     * 
     * @param importe
     */
    public void setImporte(java.math.BigDecimal importe) {
        this.importe = importe;
    }


    /**
     * Gets the fechaalta value for this DatosDeModelo.
     * 
     * @return fechaalta
     */
    public java.util.Calendar getFechaalta() {
        return fechaalta;
    }


    /**
     * Sets the fechaalta value for this DatosDeModelo.
     * 
     * @param fechaalta
     */
    public void setFechaalta(java.util.Calendar fechaalta) {
        this.fechaalta = fechaalta;
    }


    /**
     * Gets the fechapago value for this DatosDeModelo.
     * 
     * @return fechapago
     */
    public java.util.Calendar getFechapago() {
        return fechapago;
    }


    /**
     * Sets the fechapago value for this DatosDeModelo.
     * 
     * @param fechapago
     */
    public void setFechapago(java.util.Calendar fechapago) {
        this.fechapago = fechapago;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DatosDeModelo)) return false;
        DatosDeModelo other = (DatosDeModelo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.localizador==null && other.getLocalizador()==null) || 
             (this.localizador!=null &&
              this.localizador.equals(other.getLocalizador()))) &&
            ((this.nifsujetopasivo==null && other.getNifsujetopasivo()==null) || 
             (this.nifsujetopasivo!=null &&
              this.nifsujetopasivo.equals(other.getNifsujetopasivo()))) &&
            ((this.descripcion==null && other.getDescripcion()==null) || 
             (this.descripcion!=null &&
              this.descripcion.equals(other.getDescripcion()))) &&
            ((this.identificador==null && other.getIdentificador()==null) || 
             (this.identificador!=null &&
              this.identificador.equals(other.getIdentificador()))) &&
            ((this.importe==null && other.getImporte()==null) || 
             (this.importe!=null &&
              this.importe.equals(other.getImporte()))) &&
            ((this.fechaalta==null && other.getFechaalta()==null) || 
             (this.fechaalta!=null &&
              this.fechaalta.equals(other.getFechaalta()))) &&
            ((this.fechapago==null && other.getFechapago()==null) || 
             (this.fechapago!=null &&
              this.fechapago.equals(other.getFechapago())));
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
        if (getLocalizador() != null) {
            _hashCode += getLocalizador().hashCode();
        }
        if (getNifsujetopasivo() != null) {
            _hashCode += getNifsujetopasivo().hashCode();
        }
        if (getDescripcion() != null) {
            _hashCode += getDescripcion().hashCode();
        }
        if (getIdentificador() != null) {
            _hashCode += getIdentificador().hashCode();
        }
        if (getImporte() != null) {
            _hashCode += getImporte().hashCode();
        }
        if (getFechaalta() != null) {
            _hashCode += getFechaalta().hashCode();
        }
        if (getFechapago() != null) {
            _hashCode += getFechapago().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DatosDeModelo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://atib.es/", "DatosDeModelo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("localizador");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atib.es/", "localizador"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nifsujetopasivo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atib.es/", "nifsujetopasivo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atib.es/", "descripcion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identificador");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atib.es/", "identificador"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("importe");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atib.es/", "importe"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechaalta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atib.es/", "fechaalta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fechapago");
        elemField.setXmlName(new javax.xml.namespace.QName("http://atib.es/", "fechapago"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
