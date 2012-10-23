
package es.caib.regtel.plugincaib.ws.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParametrosRegistroEntradaWS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ParametrosRegistroEntradaWS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="usuario" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="usuarioRegistro" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="actualizacion" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="altres" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="altresNuevo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="anoEntrada" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="balears" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="comentario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="comentarioNuevo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="correo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="data" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataentrada" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataVisado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcionDocumento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcionIdiomaDocumento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcionMunicipi060" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcionOficina" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcionOficinaFisica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcionOrganismoDestinatario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcionRemitente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="destinatari" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="disquet" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="emailRemitent" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entidad1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entidad1Grabada" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entidad1Nuevo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entidad2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entidad2Nuevo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entidadCastellano" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fora" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="hora" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idioex" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idioma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idiomaExtracto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="leido" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="localitzadorsDocs" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="motivo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="municipi060" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroDocumentosRegistro060" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroEntrada" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="oficina" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="oficinafisica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="origenRegistro" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="paramRegPubEnt" type="{urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS}ParametrosRegistroPublicadoEntradaWS" minOccurs="0"/>
 *         &lt;element name="procedenciaGeografica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="registroActualizado" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="registroAnulado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="registroGrabado" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="salida1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="salida2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="validado" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="errores" type="{urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS}listaErroresEntrada" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParametrosRegistroEntradaWS", namespace = "urn:es:caib:regweb:ws:v1:model:ParametrosRegistroEntradaWS", propOrder = {
    "usuario",
    "password",
    "usuarioRegistro",
    "actualizacion",
    "altres",
    "altresNuevo",
    "anoEntrada",
    "balears",
    "comentario",
    "comentarioNuevo",
    "correo",
    "data",
    "dataentrada",
    "dataVisado",
    "descripcionDocumento",
    "descripcionIdiomaDocumento",
    "descripcionMunicipi060",
    "descripcionOficina",
    "descripcionOficinaFisica",
    "descripcionOrganismoDestinatario",
    "descripcionRemitente",
    "destinatari",
    "disquet",
    "emailRemitent",
    "entidad1",
    "entidad1Grabada",
    "entidad1Nuevo",
    "entidad2",
    "entidad2Nuevo",
    "entidadCastellano",
    "fora",
    "hora",
    "idioex",
    "idioma",
    "idiomaExtracto",
    "leido",
    "localitzadorsDocs",
    "motivo",
    "municipi060",
    "numeroDocumentosRegistro060",
    "numeroEntrada",
    "oficina",
    "oficinafisica",
    "origenRegistro",
    "paramRegPubEnt",
    "procedenciaGeografica",
    "registroActualizado",
    "registroAnulado",
    "registroGrabado",
    "salida1",
    "salida2",
    "tipo",
    "validado",
    "errores"
})
public class ParametrosRegistroEntradaWS {

    @XmlElement(required = true)
    protected String usuario;
    @XmlElement(required = true)
    protected String password;
    @XmlElement(required = true)
    protected String usuarioRegistro;
    protected Boolean actualizacion;
    protected String altres;
    protected String altresNuevo;
    protected String anoEntrada;
    protected String balears;
    protected String comentario;
    protected String comentarioNuevo;
    protected String correo;
    protected String data;
    protected String dataentrada;
    protected String dataVisado;
    protected String descripcionDocumento;
    protected String descripcionIdiomaDocumento;
    protected String descripcionMunicipi060;
    protected String descripcionOficina;
    protected String descripcionOficinaFisica;
    protected String descripcionOrganismoDestinatario;
    protected String descripcionRemitente;
    protected String destinatari;
    protected String disquet;
    protected String emailRemitent;
    protected String entidad1;
    protected String entidad1Grabada;
    protected String entidad1Nuevo;
    protected String entidad2;
    protected String entidad2Nuevo;
    protected String entidadCastellano;
    protected String fora;
    protected String hora;
    protected String idioex;
    protected String idioma;
    protected String idiomaExtracto;
    protected Boolean leido;
    protected String localitzadorsDocs;
    protected String motivo;
    protected String municipi060;
    protected String numeroDocumentosRegistro060;
    protected String numeroEntrada;
    protected String oficina;
    protected String oficinafisica;
    protected String origenRegistro;
    protected ParametrosRegistroPublicadoEntradaWS paramRegPubEnt;
    protected String procedenciaGeografica;
    protected Boolean registroActualizado;
    protected String registroAnulado;
    protected Boolean registroGrabado;
    protected String salida1;
    protected String salida2;
    protected String tipo;
    protected Boolean validado;
    protected ListaErroresEntrada errores;

    /**
     * Gets the value of the usuario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Sets the value of the usuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsuario(String value) {
        this.usuario = value;
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
     * Gets the value of the anoEntrada property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnoEntrada() {
        return anoEntrada;
    }

    /**
     * Sets the value of the anoEntrada property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnoEntrada(String value) {
        this.anoEntrada = value;
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
     * Gets the value of the dataentrada property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataentrada() {
        return dataentrada;
    }

    /**
     * Sets the value of the dataentrada property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataentrada(String value) {
        this.dataentrada = value;
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
     * Gets the value of the descripcionMunicipi060 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionMunicipi060() {
        return descripcionMunicipi060;
    }

    /**
     * Sets the value of the descripcionMunicipi060 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionMunicipi060(String value) {
        this.descripcionMunicipi060 = value;
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
     * Gets the value of the descripcionOrganismoDestinatario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionOrganismoDestinatario() {
        return descripcionOrganismoDestinatario;
    }

    /**
     * Sets the value of the descripcionOrganismoDestinatario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionOrganismoDestinatario(String value) {
        this.descripcionOrganismoDestinatario = value;
    }

    /**
     * Gets the value of the descripcionRemitente property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionRemitente() {
        return descripcionRemitente;
    }

    /**
     * Sets the value of the descripcionRemitente property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionRemitente(String value) {
        this.descripcionRemitente = value;
    }

    /**
     * Gets the value of the destinatari property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinatari() {
        return destinatari;
    }

    /**
     * Sets the value of the destinatari property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinatari(String value) {
        this.destinatari = value;
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
     * Gets the value of the municipi060 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMunicipi060() {
        return municipi060;
    }

    /**
     * Sets the value of the municipi060 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMunicipi060(String value) {
        this.municipi060 = value;
    }

    /**
     * Gets the value of the numeroDocumentosRegistro060 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroDocumentosRegistro060() {
        return numeroDocumentosRegistro060;
    }

    /**
     * Sets the value of the numeroDocumentosRegistro060 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroDocumentosRegistro060(String value) {
        this.numeroDocumentosRegistro060 = value;
    }

    /**
     * Gets the value of the numeroEntrada property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroEntrada() {
        return numeroEntrada;
    }

    /**
     * Sets the value of the numeroEntrada property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroEntrada(String value) {
        this.numeroEntrada = value;
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
     * Gets the value of the origenRegistro property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigenRegistro() {
        return origenRegistro;
    }

    /**
     * Sets the value of the origenRegistro property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigenRegistro(String value) {
        this.origenRegistro = value;
    }

    /**
     * Gets the value of the paramRegPubEnt property.
     * 
     * @return
     *     possible object is
     *     {@link ParametrosRegistroPublicadoEntradaWS }
     *     
     */
    public ParametrosRegistroPublicadoEntradaWS getParamRegPubEnt() {
        return paramRegPubEnt;
    }

    /**
     * Sets the value of the paramRegPubEnt property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParametrosRegistroPublicadoEntradaWS }
     *     
     */
    public void setParamRegPubEnt(ParametrosRegistroPublicadoEntradaWS value) {
        this.paramRegPubEnt = value;
    }

    /**
     * Gets the value of the procedenciaGeografica property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcedenciaGeografica() {
        return procedenciaGeografica;
    }

    /**
     * Sets the value of the procedenciaGeografica property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcedenciaGeografica(String value) {
        this.procedenciaGeografica = value;
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
     * Gets the value of the registroGrabado property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRegistroGrabado() {
        return registroGrabado;
    }

    /**
     * Sets the value of the registroGrabado property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRegistroGrabado(Boolean value) {
        this.registroGrabado = value;
    }

    /**
     * Gets the value of the salida1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSalida1() {
        return salida1;
    }

    /**
     * Sets the value of the salida1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSalida1(String value) {
        this.salida1 = value;
    }

    /**
     * Gets the value of the salida2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSalida2() {
        return salida2;
    }

    /**
     * Sets the value of the salida2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSalida2(String value) {
        this.salida2 = value;
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
     * Gets the value of the errores property.
     * 
     * @return
     *     possible object is
     *     {@link ListaErroresEntrada }
     *     
     */
    public ListaErroresEntrada getErrores() {
        return errores;
    }

    /**
     * Sets the value of the errores property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListaErroresEntrada }
     *     
     */
    public void setErrores(ListaErroresEntrada value) {
        this.errores = value;
    }

}
