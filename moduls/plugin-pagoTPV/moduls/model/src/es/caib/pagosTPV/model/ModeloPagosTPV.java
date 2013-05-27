package es.caib.pagosTPV.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import es.caib.sistra.plugins.pagos.DatosPago;
import es.caib.sistra.plugins.pagos.EstadoSesionPago;
import es.caib.sistra.plugins.pagos.SesionSistra;

public class ModeloPagosTPV implements Serializable
{
	private String localizador;
	private char tipoPago;
	private String identificadorTramite;
	private String modeloTramite;
	private int versionTramite;	
	private String nombreTramite;
	private String nombreUsuario;	
	private String idioma;
	private String modelo; 
	private String idTasa;
	private String concepto;
	private Date fechaDevengo;
	private String importe;
	private String nifDeclarante;
	private String nombreDeclarante;
	private String telefonoDeclarante;
	private int estado;
	private String tipoPagoSeleccionado;
	private String identificadorPago;
	private Date fechaPago;
	private String reciboB64PagoTelematico;
	private String urlRetornoSistra;
	private String urlMantenimientoSesionSistra;
	private String token;
	private Date tiempoLimite;
	private char pagoFinalizado;
	private String identificadorOrganismo;
	private String historicoPedidosKO;
	
	
	public ModeloPagosTPV() {}
	

	public ModeloPagosTPV(SesionPagoCAIB sesionCAIB) {
		sesionCaibToModeloPagosTPV(sesionCAIB);
	}

	public ModeloPagosTPV(SesionPagoCAIB sesionCAIB, TokenAccesoCAIB tokenCAIB) {
		sesionCaibToModeloPagosTPV(sesionCAIB);
		
		this.token = tokenCAIB.getToken();
		this.tiempoLimite = tokenCAIB.getTiempoLimite();
		
	}
	
	private void sesionCaibToModeloPagosTPV(SesionPagoCAIB sesionCAIB) {
		this.localizador = sesionCAIB.getLocalizador();
		this.tipoPago = sesionCAIB.getDatosPago().getTipoPago();
		this.identificadorTramite = sesionCAIB.getDatosPago().getIdentificadorTramite();
		this.modeloTramite = sesionCAIB.getDatosPago().getModeloTramite();
		this.versionTramite = sesionCAIB.getDatosPago().getVersionTramite();
		this.nombreTramite = sesionCAIB.getDatosPago().getNombreTramite();
		this.identificadorOrganismo = sesionCAIB.getDatosPago().getIdentificadorOrganismo();
		this.modelo = sesionCAIB.getDatosPago().getModelo();
		this.idTasa = sesionCAIB.getDatosPago().getIdTasa();
		this.concepto = sesionCAIB.getDatosPago().getConcepto();
		this.nombreUsuario = sesionCAIB.getDatosPago().getNombreUsuario();
		this.idioma = sesionCAIB.getDatosPago().getIdioma();
		this.fechaDevengo = sesionCAIB.getDatosPago().getFechaDevengo();
		this.importe = sesionCAIB.getDatosPago().getImporte();
		this.nifDeclarante = sesionCAIB.getDatosPago().getNifDeclarante();
		this.nombreDeclarante = sesionCAIB.getDatosPago().getNombreDeclarante();
		this.telefonoDeclarante = sesionCAIB.getDatosPago().getTelefonoDeclarante();
		this.estado = sesionCAIB.getEstadoPago().getEstado();		
		this.tipoPagoSeleccionado = Character.toString(sesionCAIB.getEstadoPago().getTipo());
		this.identificadorPago = sesionCAIB.getEstadoPago().getIdentificadorPago();
		this.fechaPago = sesionCAIB.getEstadoPago().getFechaPago();
		this.reciboB64PagoTelematico = sesionCAIB.getEstadoPago().getReciboB64PagoTelematico();
		this.urlRetornoSistra = sesionCAIB.getSesionSistra().getUrlRetornoSistra();
		this.urlMantenimientoSesionSistra = sesionCAIB.getSesionSistra().getUrlMantenimientoSesionSistra();
		this.historicoPedidosKO = sesionCAIB.getHistoricoPedidosKO();
	}
	
	public SesionPagoCAIB getSessionPagoCAIB() {
		SesionPagoCAIB sesionCAIB = new SesionPagoCAIB();
		sesionCAIB.setLocalizador(localizador);
		sesionCAIB.setHistoricoPedidosKO(historicoPedidosKO);
		sesionCAIB.setDatosPago(new DatosPago());
		sesionCAIB.getDatosPago().setTipoPago(tipoPago);
		sesionCAIB.getDatosPago().setIdentificadorTramite(identificadorTramite);
		sesionCAIB.getDatosPago().setModeloTramite(modeloTramite);
		sesionCAIB.getDatosPago().setVersionTramite(versionTramite);
		sesionCAIB.getDatosPago().setNombreTramite(nombreTramite);
		sesionCAIB.getDatosPago().setIdentificadorOrganismo(identificadorOrganismo);
		sesionCAIB.getDatosPago().setModelo(modelo);
		sesionCAIB.getDatosPago().setIdTasa(idTasa);
		sesionCAIB.getDatosPago().setConcepto(concepto);
		sesionCAIB.getDatosPago().setFechaDevengo(fechaDevengo);
		sesionCAIB.getDatosPago().setImporte(importe);
		sesionCAIB.getDatosPago().setNifDeclarante(nifDeclarante);
		sesionCAIB.getDatosPago().setNombreDeclarante(nombreDeclarante);
		sesionCAIB.getDatosPago().setTelefonoDeclarante(telefonoDeclarante);
		sesionCAIB.getDatosPago().setNombreUsuario(nombreUsuario);
		sesionCAIB.getDatosPago().setIdioma(idioma);
		sesionCAIB.setEstadoPago(new EstadoSesionPago());
		if (StringUtils.isNotBlank(tipoPagoSeleccionado)) {
			sesionCAIB.getEstadoPago().setTipo(tipoPagoSeleccionado.charAt(0));
		}
		sesionCAIB.getEstadoPago().setEstado(estado);		
		sesionCAIB.getEstadoPago().setIdentificadorPago(identificadorPago);
		sesionCAIB.getEstadoPago().setFechaPago(fechaPago);
		sesionCAIB.getEstadoPago().setReciboB64PagoTelematico(reciboB64PagoTelematico);
		sesionCAIB.setSesionSistra(new SesionSistra());
		sesionCAIB.getSesionSistra().setUrlRetornoSistra(urlRetornoSistra);
		sesionCAIB.getSesionSistra().setUrlMantenimientoSesionSistra(urlMantenimientoSesionSistra);		
		return sesionCAIB;
	}
	
	
	
	public TokenAccesoCAIB getTokenAccesoCAIB(){
		TokenAccesoCAIB tokenCAIB = new TokenAccesoCAIB();
		tokenCAIB.setLocalizador(localizador);
		tokenCAIB.setTiempoLimite(tiempoLimite);
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
	
	public Date getTiempoLimite() {
		return tiempoLimite;
	}


	public void setTiempoLimite(Date tiempoLimite) {
		this.tiempoLimite = tiempoLimite;
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


	public char getTipoPago() {
		return tipoPago;
	}


	public void setTipoPago(char tipoPago) {
		this.tipoPago = tipoPago;
	}


	public String getIdentificadorOrganismo() {
		return identificadorOrganismo;
	}


	public void setIdentificadorOrganismo(String identificadorOrganismo) {
		this.identificadorOrganismo = identificadorOrganismo;
	}


	public String getHistoricoPedidosKO() {
		return historicoPedidosKO;
	}


	public void setHistoricoPedidosKO(String historicoPedidosKO) {
		this.historicoPedidosKO = historicoPedidosKO;
	}


	public String getTipoPagoSeleccionado() {
		return tipoPagoSeleccionado;
	}


	public void setTipoPagoSeleccionado(String tipoPagoSeleccionado) {
		this.tipoPagoSeleccionado = tipoPagoSeleccionado;
	}


	public String getTelefonoDeclarante() {
		return telefonoDeclarante;
	}


	public void setTelefonoDeclarante(String telefonoDeclarante) {
		this.telefonoDeclarante = telefonoDeclarante;
	}

}
