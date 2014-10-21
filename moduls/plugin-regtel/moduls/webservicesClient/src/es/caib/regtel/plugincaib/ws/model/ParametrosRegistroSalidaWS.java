
package es.caib.regtel.plugincaib.ws.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParametrosRegistroSalidaWS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ParametrosRegistroSalidaWS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="usuarioRegistro" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="usuarioConexion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="anoSalida" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="actualizacion" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="registroSalidaGrabado" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="entidadCastellano" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="registroAnulado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroSalida" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcionOficina" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcionOficinaFisica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcionDestinatario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="correo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcionOrganismoRemitente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcionDocumento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcionIdiomaDocumento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="destinoGeografico" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idiomaExtracto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="datasalida" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="hora" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="oficina" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="oficinafisica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="data" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idioma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entidad1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entidad2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="altres" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="balears" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fora" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entrada1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entrada2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="remitent" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idioex" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="disquet" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="registroActualizado" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="comentario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="motivo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entidad1Nuevo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entidad2Nuevo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="altresNuevo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="comentarioNuevo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataVisado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="validado" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="leido" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="entidad1Grabada" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="emailRemitent" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="localitzadorsDocs" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errores" type="{urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS}listaErroresSalida" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParametrosRegistroSalidaWS", namespace = "urn:es:caib:regweb:ws:v1:model:ParametrosRegistroSalidaWS", propOrder = {
    "usuarioRegistro",
    "usuarioConexion",
    "password",
    "anoSalida",
    "actualizacion",
    "registroSalidaGrabado",
    "entidadCastellano",
    "registroAnulado",
    "numeroSalida",
    "descripcionOficina",
    "descripcionOficinaFisica",
    "descripcionDestinatario",
    "correo",
    "descripcionOrganismoRemitente",
    "descripcionDocumento",
    "descripcionIdiomaDocumento",
    "destinoGeografico",
    "idiomaExtracto",
    "datasalida",
    "hora",
    "oficina",
    "oficinafisica",
    "data",
    "tipo",
    "idioma",
    "entidad1",
    "entidad2",
    "altres",
    "balears",
    "fora",
    "entrada1",
    "entrada2",
    "remitent",
    "idioex",
    "disquet",
    "registroActualizado",
    "comentario",
    "motivo",
    "entidad1Nuevo",
    "entidad2Nuevo",
    "altresNuevo",
    "comentarioNuevo",
    "dataVisado",
    "validado",
    "leido",
    "entidad1Grabada",
    "emailRemitent",
    "localitzadorsDocs",
    "errores"
})
public class ParametrosRegistroSalidaWS {

    @XmlElement(required = true)
    protected String usuarioRegistro;
    @XmlElement(required = true)
    protected String usuarioConexion;
    @XmlElement(required = true)
    protected String password;
    protected String anoSalida;
    protected Boolean actualizacion;
    protected Boolean registroSalidaGrabado;
    protected String entidadCastellano;
    protected String registroAnulado;
    protected String numeroSalida;
    protected String descripcionOficina;
    protected String descripcionOficinaFisica;
    protected String descripcionDestinatario;
    protected String correo;
    protected String descripcionOrganismoRemitente;
    protected String descripcionDocumento;
    protected String descripcionIdiomaDocumento;
    protected String destinoGeografico;
    protected String idiomaExtracto;
    protected String datasalida;
    protected String hora;
    protected String oficina;
    protected String oficinafisica;
    protected String data;
    protected String tipo;
    protected String idioma;
    protected String entidad1;
    protected String entidad2;
    protected String altres;
    protected String balears;
    protected String fora;
    protected String entrada1;
    protected String entrada2;
    protected String remitent;
    protected String idioex;
    protected String disquet;
    protected Boolean registroActualizado;
    protected String comentario;
    protected String motivo;
    protected String entidad1Nuevo;
    protected String entidad2Nuevo;
    protected String altresNuevo;
    protected String comentarioNuevo;
    protected String dataVisado;
    protected Boolean validado;
    protected Boolean leido;
    protected String entidad1Grabada;
    protected String emailRemitent;
    protected String localitzadorsDocs;
    protected ListaErroresSalida errores;

    /**
     * Gets the value of the usuarioRegistro property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsuarioRegistro() {
        return usuarioRegistro;
    }

    /**
     * Sets the value of the usuarioRegistro property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsuarioRegistro(String value) {
        this.usuarioRegistro = value;
    }

    /**
     * Gets the value of the usuarioConexion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsuarioConexion() {
        return usuarioConexion;
    }

    /**
     * Sets the value of the usuarioConexion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsuarioConexion(String value) {
        this.usuarioConexion = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the anoSalida property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnoSalida() {
        return anoSalida;
    }

    /**
     * Sets the value of the anoSalida property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnoSalida(String value) {
        this.anoSalida = value;
    }

    /**
     * Gets the value of the actualizacion property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isActualizacion() {
        return actualizacion;
    }

    /**
     * Sets the value of the actualizacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setActualizacion(Boolean value) {
        this.actualizacion = value;
    }

    /**
     * Gets the value of the registroSalidaGrabado property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRegistroSalidaGrabado() {
        return registroSalidaGrabado;
    }

    /**
     * Sets the value of the registroSalidaGrabado property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRegistroSalidaGrabado(Boolean value) {
        this.registroSalidaGrabado = value;
    }

    /**
     * Gets the value of the entidadCastellano property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntidadCastellano() {
        return entidadCastellano;
    }

    /**
     * Sets the value of the entidadCastellano property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntidadCastellano(String value) {
        this.entidadCastellano = value;
    }

    /**
     * Gets the value of the registroAnulado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegistroAnulado() {
        return registroAnulado;
    }

    /**
     * Sets the value of the registroAnulado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegistroAnulado(String value) {
        this.registroAnulado = value;
    }

    /**
     * Gets the value of the numeroSalida property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroSalida() {
        return numeroSalida;
    }

    /**
     * Sets the value of the numeroSalida property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroSalida(String value) {
        this.numeroSalida = value;
    }

    /**
     * Gets the value of the descripcionOficina property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionOficina() {
        return descripcionOficina;
    }

    /**
     * Sets the value of the descripcionOficina property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionOficina(String value) {
        this.descripcionOficina = value;
    }

    /**
     * Gets the value of the descripcionOficinaFisica property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionOficinaFisica() {
        return descripcionOficinaFisica;
    }

    /**
     * Sets the value of the descripcionOficinaFisica property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionOficinaFisica(String value) {
        this.descripcionOficinaFisica = value;
    }

    /**
     * Gets the value of the descripcionDestinatario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionDestinatario() {
        return descripcionDestinatario;
    }

    /**
     * Sets the value of the descripcionDestinatario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionDestinatario(String value) {
        this.descripcionDestinatario = value;
    }

    /**
     * Gets the value of the correo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Sets the value of the correo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCorreo(String value) {
        this.correo = value;
    }

    /**
     * Gets the value of the descripcionOrganismoRemitente property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionOrganismoRemitente() {
        return descripcionOrganismoRemitente;
    }

    /**
     * Sets the value of the descripcionOrganismoRemitente property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionOrganismoRemitente(String value) {
        this.descripcionOrganismoRemitente = value;
    }

    /**
     * Gets the value of the descripcionDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionDocumento() {
        return descripcionDocumento;
    }

    /**
     * Sets the value of the descripcionDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionDocumento(String value) {
        this.descripcionDocumento = value;
    }

    /**
     * Gets the value of the descripcionIdiomaDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionIdiomaDocumento() {
        return descripcionIdiomaDocumento;
    }

    /**
     * Sets the value of the descripcionIdiomaDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionIdiomaDocumento(String value) {
        this.descripcionIdiomaDocumento = value;
    }

    /**
     * Gets the value of the destinoGeografico property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinoGeografico() {
        return destinoGeografico;
    }

    /**
     * Sets the value of the destinoGeografico property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinoGeografico(String value) {
        this.destinoGeografico = value;
    }

    /**
     * Gets the value of the idiomaExtracto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdiomaExtracto() {
        return idiomaExtracto;
    }

    /**
     * Sets the value of the idiomaExtracto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdiomaExtracto(String value) {
        this.idiomaExtracto = value;
    }

    /**
     * Gets the value of the datasalida property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatasalida() {
        return datasalida;
    }

    /**
     * Sets the value of the datasalida property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatasalida(String value) {
        this.datasalida = value;
    }

    /**
     * Gets the value of the hora property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHora() {
        return hora;
    }

    /**
     * Sets the value of the hora property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHora(String value) {
        this.hora = value;
    }

    /**
     * Gets the value of the oficina property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOficina() {
        return oficina;
    }

    /**
     * Sets the value of the oficina property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOficina(String value) {
        this.oficina = value;
    }

    /**
     * Gets the value of the oficinafisica property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOficinafisica() {
        return oficinafisica;
    }

    /**
     * Sets the value of the oficinafisica property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOficinafisica(String value) {
        this.oficinafisica = value;
    }

    /**
     * Gets the value of the data property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getData() {
        return data;
    }

    /**
     * Sets the value of the data property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setData(String value) {
        this.data = value;
    }

    /**
     * Gets the value of the tipo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Sets the value of the tipo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipo(String value) {
        this.tipo = value;
    }

    /**
     * Gets the value of the idioma property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdioma() {
        return idioma;
    }

    /**
     * Sets the value of the idioma property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdioma(String value) {
        this.idioma = value;
    }

    /**
     * Gets the value of the entidad1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntidad1() {
        return entidad1;
    }

    /**
     * Sets the value of the entidad1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntidad1(String value) {
        this.entidad1 = value;
    }

    /**
     * Gets the value of the entidad2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntidad2() {
        return entidad2;
    }

    /**
     * Sets the value of the entidad2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntidad2(String value) {
        this.entidad2 = value;
    }

    /**
     * Gets the value of the altres property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAltres() {
        return altres;
    }

    /**
     * Sets the value of the altres property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAltres(String value) {
        this.altres = value;
    }

    /**
     * Gets the value of the balears property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBalears() {
        return balears;
    }

    /**
     * Sets the value of the balears property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBalears(String value) {
        this.balears = value;
    }

    /**
     * Gets the value of the fora property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFora() {
        return fora;
    }

    /**
     * Sets the value of the fora property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFora(String value) {
        this.fora = value;
    }

    /**
     * Gets the value of the entrada1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntrada1() {
        return entrada1;
    }

    /**
     * Sets the value of the entrada1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntrada1(String value) {
        this.entrada1 = value;
    }

    /**
     * Gets the value of the entrada2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntrada2() {
        return entrada2;
    }

    /**
     * Sets the value of the entrada2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntrada2(String value) {
        this.entrada2 = value;
    }

    /**
     * Gets the value of the remitent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemitent() {
        return remitent;
    }

    /**
     * Sets the value of the remitent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemitent(String value) {
        this.remitent = value;
    }

    /**
     * Gets the value of the idioex property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdioex() {
        return idioex;
    }

    /**
     * Sets the value of the idioex property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdioex(String value) {
        this.idioex = value;
    }

    /**
     * Gets the value of the disquet property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisquet() {
        return disquet;
    }

    /**
     * Sets the value of the disquet property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisquet(String value) {
        this.disquet = value;
    }

    /**
     * Gets the value of the registroActualizado property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRegistroActualizado() {
        return registroActualizado;
    }

    /**
     * Sets the value of the registroActualizado property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRegistroActualizado(Boolean value) {
        this.registroActualizado = value;
    }

    /**
     * Gets the value of the comentario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComentario() {
        return comentario;
    }

    /**
     * Sets the value of the comentario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComentario(String value) {
        this.comentario = value;
    }

    /**
     * Gets the value of the motivo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotivo() {
        return motivo;
    }

    /**
     * Sets the value of the motivo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotivo(String value) {
        this.motivo = value;
    }

    /**
     * Gets the value of the entidad1Nuevo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntidad1Nuevo() {
        return entidad1Nuevo;
    }

    /**
     * Sets the value of the entidad1Nuevo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntidad1Nuevo(String value) {
        this.entidad1Nuevo = value;
    }

    /**
     * Gets the value of the entidad2Nuevo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntidad2Nuevo() {
        return entidad2Nuevo;
    }

    /**
     * Sets the value of the entidad2Nuevo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntidad2Nuevo(String value) {
        this.entidad2Nuevo = value;
    }

    /**
     * Gets the value of the altresNuevo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAltresNuevo() {
        return altresNuevo;
    }

    /**
     * Sets the value of the altresNuevo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAltresNuevo(String value) {
        this.altresNuevo = value;
    }

    /**
     * Gets the value of the comentarioNuevo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComentarioNuevo() {
        return comentarioNuevo;
    }

    /**
     * Sets the value of the comentarioNuevo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComentarioNuevo(String value) {
        this.comentarioNuevo = value;
    }

    /**
     * Gets the value of the dataVisado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataVisado() {
        return dataVisado;
    }

    /**
     * Sets the value of the dataVisado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataVisado(String value) {
        this.dataVisado = value;
    }

    /**
     * Gets the value of the validado property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isValidado() {
        return validado;
    }

    /**
     * Sets the value of the validado property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setValidado(Boolean value) {
        this.validado = value;
    }

    /**
     * Gets the value of the leido property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isLeido() {
        return leido;
    }

    /**
     * Sets the value of the leido property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLeido(Boolean value) {
        this.leido = value;
    }

    /**
     * Gets the value of the entidad1Grabada property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntidad1Grabada() {
        return entidad1Grabada;
    }

    /**
     * Sets the value of the entidad1Grabada property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntidad1Grabada(String value) {
        this.entidad1Grabada = value;
    }

    /**
     * Gets the value of the emailRemitent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailRemitent() {
        return emailRemitent;
    }

    /**
     * Sets the value of the emailRemitent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailRemitent(String value) {
        this.emailRemitent = value;
    }

    /**
     * Gets the value of the localitzadorsDocs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalitzadorsDocs() {
        return localitzadorsDocs;
    }

    /**
     * Sets the value of the localitzadorsDocs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalitzadorsDocs(String value) {
        this.localitzadorsDocs = value;
    }

    /**
     * Gets the value of the errores property.
     * 
     * @return
     *     possible object is
     *     {@link ListaErroresSalida }
     *     
     */
    public ListaErroresSalida getErrores() {
        return errores;
    }

    /**
     * Sets the value of the errores property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListaErroresSalida }
     *     
     */
    public void setErrores(ListaErroresSalida value) {
        this.errores = value;
    }

}
