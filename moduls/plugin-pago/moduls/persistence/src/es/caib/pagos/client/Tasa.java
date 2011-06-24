package es.caib.pagos.client;

public class Tasa {
	/**
	 * Tipo de pago: T (Telemático por Banca Electrónica) / P (Presencial mediante documento de pago impreso)
	 */
	public static final String TELEMATICO = "T";
	public static final String PRESENCIAL = "P";
	private String tipoPago = null;
	private String version = null;
	private String modelo = null;
	private String idTasa = null;
	private String codigoTasa = "c04";
	private String subConcepto = null;
	private String codigoSubConcepto = "c24";
	private String importe = null;
	private String codigoImporte = "c75";
	private String concepto = null;
	private String codigoConcepto = "c22";
	private String nif = null;
	private String codigoNif = "c05";
	private String nombre = null;
	private String codigoNombre = "c06";
	private String siglas = null;
	private String codigoSiglas = "c07";
	private String nombreVia = null;
	private String codigoNombreVia = "c08";
	private String numero = null;
	private String codigoNumero = "c09";
	private String letra = null;
	private String codigoLetra = "c10";
	private String escala = null;
	private String codigoEscala = "c11";
	private String piso = null;
	private String codigoPiso = "c12";
	private String puerta = null;
	private String codigoPuerta = "c13";
	private String telefono = null;
	private String codigoTelefono = "c14";
	private String fax = null;
	private String codigoFax = "c15";
	private String localidad = null;
	private String codigoLocalidad = "c16";
	private String provincia = null;
	private String codigoProvincia = "c17";
	private String codigoPostal = null;
	private String codigoCodigoPostal = "c18";
	
	private String localizador = null;
	
	public String getCodigoPostal() {
		return codigoPostal;
	}
	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	public String getEscala() {
		return escala;
	}
	public void setEscala(String escala) {
		this.escala = escala;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getIdTasa() {
		return idTasa;
	}
	public void setIdTasa(String idTasa) {
		this.idTasa = idTasa;
	}
	public String getImporte() {
		return importe;
	}
	public void setImporte(String importe) {
		this.importe = importe;
	}
	public String getLetra() {
		return letra;
	}
	public void setLetra(String letra) {
		this.letra = letra;
	}
	public String getLocalidad() {
		return localidad;
	}
	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public String getNif() {
		return nif;
	}
	public void setNif(String nif) {
		this.nif = nif;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPiso() {
		return piso;
	}
	public void setPiso(String piso) {
		this.piso = piso;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getPuerta() {
		return puerta;
	}
	public void setPuerta(String puerta) {
		this.puerta = puerta;
	}
	public String getSiglas() {
		return siglas;
	}
	public void setSiglas(String siglas) {
		this.siglas = siglas;
	}
	public String getSubConcepto() {
		return subConcepto;
	}
	public void setSubConcepto(String subConcepto) {
		this.subConcepto = subConcepto;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getCodigoCodigoPostal() {
		return codigoCodigoPostal;
	}
	public String getCodigoConcepto() {
		return codigoConcepto;
	}
	public String getCodigoEscala() {
		return codigoEscala;
	}
	public String getCodigoFax() {
		return codigoFax;
	}
	public String getCodigoImporte() {
		return codigoImporte;
	}
	public String getCodigoLetra() {
		return codigoLetra;
	}
	public String getCodigoLocalidad() {
		return codigoLocalidad;
	}
	public String getCodigoNif() {
		return codigoNif;
	}
	public String getCodigoNombre() {
		return codigoNombre;
	}
	public String getCodigoPiso() {
		return codigoPiso;
	}
	public String getCodigoProvincia() {
		return codigoProvincia;
	}
	public String getCodigoPuerta() {
		return codigoPuerta;
	}
	public String getCodigoSiglas() {
		return codigoSiglas;
	}
	public String getCodigoSubConcepto() {
		return codigoSubConcepto;
	}
	public String getCodigoTasa() {
		return codigoTasa;
	}
	public String getCodigoTelefono() {
		return codigoTelefono;
	}
	public String getNombreVia() {
		return nombreVia;
	}
	public void setNombreVia(String nombreVia) {
		this.nombreVia = nombreVia;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getCodigoNombreVia() {
		return codigoNombreVia;
	}
	public String getCodigoNumero() {
		return codigoNumero;
	}
	public String getTipoPago() {
		return tipoPago;
	}
	public void setTipoPago(String tipoPago) {
		this.tipoPago = tipoPago;
	}
	public String getLocalizador() {
		return localizador;
	}
	public void setLocalizador(String localizador) {
		this.localizador = localizador;
	}
	



}
