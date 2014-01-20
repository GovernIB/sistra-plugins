package es.caib.pagos.model;

import java.io.Serializable;
import java.util.Date;

import es.caib.sistra.plugins.pagos.DatosPago;
import es.caib.sistra.plugins.pagos.EstadoSesionPago;
import es.caib.sistra.plugins.pagos.SesionSistra;

public class ModeloPagos implements Serializable
{
	private String localizador;
	private String identificadorTramite;
	private String modeloTramite;
	private int versionTramite;	
	private String nombreTramite;
	private String nombreUsuario;	
	private char tipoPago;
	private String idioma;
	private String modelo; 
	private String idTasa;
	private String concepto;
	private Date fechaDevengo;
	private String importe;
	private String nifDeclarante;
	private String nombreDeclarante;
	private int estado;
	private char tipo;	
	private String identificadorPago;
	private Date fechaPago;
	private String reciboB64PagoTelematico;
	private String urlRetornoSistra;
	private String urlMantenimientoSesionSistra;
	private String token;
	private Date tiempoLimiteToken;
	private char pagoFinalizado;
	
	private Date fechaInicioSesion;
	private Date fechaMaximaPago;  
	private String mensajeTiempoMaximoPago;
	
	public ModeloPagos() {}
	
	/*
	public ModeloPagos(String localizador, String nombreTramite, String nombreUsuario, char tipoPago, String idioma, String modelo, String idTasa, String concepto, Date fechaDevengo, String importe, String nifDeclarante, String nombreDeclarante, int estado, char tipo, String identificadorPago, Date fechaPago, String reciboB64PagoTelematico, String urlRetornoSistra, String urlMantenimientoSesionSistra, String token, Date tiempoLimite, char pagoFinalizado, Date fechaInicioSesion, int tiempoMaximo) {
		this.localizador = localizador;
		this.nombreTramite = nombreTramite;
		this.nombreUsuario = nombreUsuario;
		this.tipoPago = tipoPago;
		this.idioma = idioma;
		this.modelo = modelo;
		this.idTasa = idTasa;
		this.concepto = concepto;
		this.fechaDevengo = fechaDevengo;
		this.importe = importe;
		this.nifDeclarante = nifDeclarante;
		this.nombreDeclarante = nombreDeclarante;
		this.estado = estado;
		this.tipo = tipo;
		this.identificadorPago = identificadorPago;
		this.fechaPago = fechaPago;
		this.reciboB64PagoTelematico = reciboB64PagoTelematico;
		this.urlRetornoSistra = urlRetornoSistra;
		this.urlMantenimientoSesionSistra = urlMantenimientoSesionSistra;
		this.token = token;
		this.tiempoLimiteToken = tiempoLimite;
		this.pagoFinalizado = pagoFinalizado;
		this.fechaInicioSesion = fechaInicioSesion;
		this.tiempoMaximoPago = tiempoMaximo;
	}
	*/

	public ModeloPagos(SesionPagoCAIB sesionCAIB) {
		this.localizador = sesionCAIB.getLocalizador();
		this.identificadorTramite = sesionCAIB.getDatosPago().getIdentificadorTramite();
		this.modeloTramite = sesionCAIB.getDatosPago().getModeloTramite();
		this.versionTramite = sesionCAIB.getDatosPago().getVersionTramite();
		this.nombreTramite = sesionCAIB.getDatosPago().getNombreTramite();
		this.modelo = sesionCAIB.getDatosPago().getModelo();
		this.idTasa = sesionCAIB.getDatosPago().getIdTasa();
		this.concepto = sesionCAIB.getDatosPago().getConcepto();
		this.nombreUsuario = sesionCAIB.getDatosPago().getNombreUsuario();
		this.tipoPago = sesionCAIB.getDatosPago().getTipoPago();
		this.idioma = sesionCAIB.getDatosPago().getIdioma();
		this.fechaDevengo = sesionCAIB.getDatosPago().getFechaDevengo();
		this.importe = sesionCAIB.getDatosPago().getImporte();
		this.nifDeclarante = sesionCAIB.getDatosPago().getNifDeclarante();
		this.nombreDeclarante = sesionCAIB.getDatosPago().getNombreDeclarante();
		this.estado = sesionCAIB.getEstadoPago().getEstado();
		this.tipo = sesionCAIB.getEstadoPago().getTipo();
		this.identificadorPago = sesionCAIB.getEstadoPago().getIdentificadorPago();
		this.fechaPago = sesionCAIB.getEstadoPago().getFechaPago();
		this.reciboB64PagoTelematico = sesionCAIB.getEstadoPago().getReciboB64PagoTelematico();
		this.urlRetornoSistra = sesionCAIB.getSesionSistra().getUrlRetornoSistra();
		this.urlMantenimientoSesionSistra = sesionCAIB.getSesionSistra().getUrlMantenimientoSesionSistra();
		this.fechaInicioSesion = sesionCAIB.getFechaInicioSesion();
		this.fechaMaximaPago = sesionCAIB.getDatosPago().getFechaMaximaPago();
		this.mensajeTiempoMaximoPago = sesionCAIB.getDatosPago().getMensajeTiempoMaximoPago();
	}

	public ModeloPagos(SesionPagoCAIB sesionCAIB, TokenAccesoCAIB tokenCAIB) {
		this.localizador = sesionCAIB.getLocalizador();
		this.identificadorTramite = sesionCAIB.getDatosPago().getIdentificadorTramite();
		this.modeloTramite = sesionCAIB.getDatosPago().getModeloTramite();
		this.versionTramite = sesionCAIB.getDatosPago().getVersionTramite();
		this.nombreTramite = sesionCAIB.getDatosPago().getNombreTramite();
		this.modelo = sesionCAIB.getDatosPago().getModelo();
		this.idTasa = sesionCAIB.getDatosPago().getIdTasa();
		this.concepto = sesionCAIB.getDatosPago().getConcepto();
		this.nombreUsuario = sesionCAIB.getDatosPago().getNombreUsuario();
		this.tipoPago = sesionCAIB.getDatosPago().getTipoPago();
		this.idioma = sesionCAIB.getDatosPago().getIdioma();
		this.fechaDevengo = sesionCAIB.getDatosPago().getFechaDevengo();
		this.importe = sesionCAIB.getDatosPago().getImporte();
		this.nifDeclarante = sesionCAIB.getDatosPago().getNifDeclarante();
		this.nombreDeclarante = sesionCAIB.getDatosPago().getNombreDeclarante();
		this.estado = sesionCAIB.getEstadoPago().getEstado();
		this.tipo = sesionCAIB.getEstadoPago().getTipo();
		this.identificadorPago = sesionCAIB.getEstadoPago().getIdentificadorPago();
		this.fechaPago = sesionCAIB.getEstadoPago().getFechaPago();
		this.reciboB64PagoTelematico = sesionCAIB.getEstadoPago().getReciboB64PagoTelematico();
		this.urlRetornoSistra = sesionCAIB.getSesionSistra().getUrlRetornoSistra();
		this.urlMantenimientoSesionSistra = sesionCAIB.getSesionSistra().getUrlMantenimientoSesionSistra();
		this.token = tokenCAIB.getToken();
		this.tiempoLimiteToken = tokenCAIB.getTiempoLimite();
		this.fechaInicioSesion = sesionCAIB.getFechaInicioSesion();
		this.fechaMaximaPago = sesionCAIB.getDatosPago().getFechaMaximaPago();
		this.mensajeTiempoMaximoPago = sesionCAIB.getDatosPago().getMensajeTiempoMaximoPago();
		
	}
	
	public SesionPagoCAIB getSessionPagoCAIB() {
		SesionPagoCAIB sesionCAIB = new SesionPagoCAIB();
		sesionCAIB.setLocalizador(localizador);
		sesionCAIB.setDatosPago(new DatosPago());
		sesionCAIB.getDatosPago().setIdentificadorTramite(identificadorTramite);
		sesionCAIB.getDatosPago().setModeloTramite(modeloTramite);
		sesionCAIB.getDatosPago().setVersionTramite(versionTramite);
		sesionCAIB.getDatosPago().setNombreTramite(nombreTramite);
		sesionCAIB.getDatosPago().setModelo(modelo);
		sesionCAIB.getDatosPago().setIdTasa(idTasa);
		sesionCAIB.getDatosPago().setConcepto(concepto);
		sesionCAIB.getDatosPago().setFechaDevengo(fechaDevengo);
		sesionCAIB.getDatosPago().setImporte(importe);
		sesionCAIB.getDatosPago().setNifDeclarante(nifDeclarante);
		sesionCAIB.getDatosPago().setNombreDeclarante(nombreDeclarante);
		sesionCAIB.getDatosPago().setNombreUsuario(nombreUsuario);
		sesionCAIB.getDatosPago().setTipoPago(tipoPago);
		sesionCAIB.getDatosPago().setIdioma(idioma);
		sesionCAIB.setEstadoPago(new EstadoSesionPago());
		sesionCAIB.getEstadoPago().setEstado(estado);
		sesionCAIB.getEstadoPago().setTipo(tipo);
		sesionCAIB.getEstadoPago().setIdentificadorPago(identificadorPago);
		sesionCAIB.getEstadoPago().setFechaPago(fechaPago);
		sesionCAIB.getEstadoPago().setReciboB64PagoTelematico(reciboB64PagoTelematico);
		sesionCAIB.setSesionSistra(new SesionSistra());
		sesionCAIB.getSesionSistra().setUrlRetornoSistra(urlRetornoSistra);
		sesionCAIB.getSesionSistra().setUrlMantenimientoSesionSistra(urlMantenimientoSesionSistra);
		sesionCAIB.setFechaInicioSesion(fechaInicioSesion);
		sesionCAIB.getDatosPago().setFechaMaximaPago(fechaMaximaPago);
		sesionCAIB.getDatosPago().setMensajeTiempoMaximoPago(mensajeTiempoMaximoPago);
		return sesionCAIB;
	}
	
	public TokenAccesoCAIB getTokenAccesoCAIB(){
		TokenAccesoCAIB tokenCAIB = new TokenAccesoCAIB();
		tokenCAIB.setLocalizador(localizador);
		tokenCAIB.setTiempoLimite(tiempoLimiteToken);
		tokenCAIB.setToken(token);
		return tokenCAIB;
	}
	
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public Date getFechaDevengo() {
		return fechaDevengo;
	}
	public void setFechaDevengo(Date fechaDevengo) {
		this.fechaDevengo = fechaDevengo;
	}
	public Date getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}
	public String getIdentificadorPago() {
		return identificadorPago;
	}
	public void setIdentificadorPago(String identificadorPago) {
		this.identificadorPago = identificadorPago;
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
	public String getLocalizador() {
		return localizador;
	}
	public void setLocalizador(String localizador) {
		this.localizador = localizador;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public String getNifDeclarante() {
		return nifDeclarante;
	}
	public void setNifDeclarante(String nifDeclarante) {
		this.nifDeclarante = nifDeclarante;
	}
	public String getNombreDeclarante() {
		return nombreDeclarante;
	}
	public void setNombreDeclarante(String nombreDeclarante) {
		this.nombreDeclarante = nombreDeclarante;
	}
	public String getNombreTramite() {
		return nombreTramite;
	}
	public void setNombreTramite(String nombreTramite) {
		this.nombreTramite = nombreTramite;
	}
	public String getReciboB64PagoTelematico() {
		return reciboB64PagoTelematico;
	}
	public void setReciboB64PagoTelematico(String reciboB64PagoTelematico) {
		this.reciboB64PagoTelematico = reciboB64PagoTelematico;
	}
	public char getTipo() {
		return tipo;
	}
	public void setTipo(char tipo) {
		this.tipo = tipo;
	}
	public String getUrlMantenimientoSesionSistra() {
		return urlMantenimientoSesionSistra;
	}
	public void setUrlMantenimientoSesionSistra(String urlMantenimientoSesionSistra) {
		this.urlMantenimientoSesionSistra = urlMantenimientoSesionSistra;
	}
	public String getUrlRetornoSistra() {
		return urlRetornoSistra;
	}
	public void setUrlRetornoSistra(String urlRetornoSistra) {
		this.urlRetornoSistra = urlRetornoSistra;
	}
	
	public Date getTiempoLimiteToken() {
		return tiempoLimiteToken;
	}


	public void setTiempoLimiteToken(Date tiempoLimite) {
		this.tiempoLimiteToken = tiempoLimite;
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public String getIdioma() {
		return idioma;
	}


	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}


	public String getNombreUsuario() {
		return nombreUsuario;
	}


	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}


	public char getTipoPago() {
		return tipoPago;
	}


	public void setTipoPago(char tipoPago) {
		this.tipoPago = tipoPago;
	}


	public char getPagoFinalizado() {
		return pagoFinalizado;
	}


	public void setPagoFinalizado(char pagoFinalizado) {
		this.pagoFinalizado = pagoFinalizado;
	}


	public String getIdentificadorTramite() {
		return identificadorTramite;
	}


	public void setIdentificadorTramite(String identificadorTramite) {
		this.identificadorTramite = identificadorTramite;
	}


	public String getModeloTramite() {
		return modeloTramite;
	}


	public void setModeloTramite(String modeloTramite) {
		this.modeloTramite = modeloTramite;
	}


	public int getVersionTramite() {
		return versionTramite;
	}


	public void setVersionTramite(int versionTramite) {
		this.versionTramite = versionTramite;
	}


	public Date getFechaInicioSesion() {
		return fechaInicioSesion;
	}


	public void setFechaInicioSesion(Date fechaInicioSesion) {
		this.fechaInicioSesion = fechaInicioSesion;
	}


	public Date getFechaMaximaPago() {
		return fechaMaximaPago;
	}


	public void setFechaMaximaPago(Date tiempoMaximo) {
		this.fechaMaximaPago = tiempoMaximo;
	}

	public String getMensajeTiempoMaximoPago() {
		return mensajeTiempoMaximoPago;
	}

	public void setMensajeTiempoMaximoPago(String mensajeTiempoMaximoPago) {
		this.mensajeTiempoMaximoPago = mensajeTiempoMaximoPago;
	}
	
	
}
