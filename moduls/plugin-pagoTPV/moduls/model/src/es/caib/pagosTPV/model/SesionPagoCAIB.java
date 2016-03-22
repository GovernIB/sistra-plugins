package es.caib.pagosTPV.model;

import java.io.Serializable;
import java.util.Date;

import es.caib.sistra.plugins.pagos.DatosPago;
import es.caib.sistra.plugins.pagos.EstadoSesionPago;
import es.caib.sistra.plugins.pagos.SesionSistra;

/**
 * Simula datos sesion de pago
 */
public class SesionPagoCAIB implements Serializable{
	private Date fechaInicioSesion;
	private String localizador;
	private String historicoPedidosKO;
	private DatosPago datosPago;
	private EstadoSesionPago estadoPago;
	private SesionSistra sesionSistra;
	private String nivelAutenticacion;
	private String usuarioSeycon;
	private String nifUsuarioSeycon;
	private String nombreUsuarioSeycon;
	
	public DatosPago getDatosPago() {
		return datosPago;
	}
	public void setDatosPago(DatosPago datosPago) {
		this.datosPago = datosPago;
	}
	public EstadoSesionPago getEstadoPago() {
		return estadoPago;
	}
	public void setEstadoPago(EstadoSesionPago estadoPago) {
		this.estadoPago = estadoPago;
	}
	public String getLocalizador() {
		return localizador;
	}
	public void setLocalizador(String localizador) {
		this.localizador = localizador;
	}
	public SesionSistra getSesionSistra() {
		return sesionSistra;
	}
	public void setSesionSistra(SesionSistra sesionSistra) {
		this.sesionSistra = sesionSistra;
	}
	public String getHistoricoPedidosKO() {
		return historicoPedidosKO;
	}
	public void setHistoricoPedidosKO(String historicoPedidosKO) {
		this.historicoPedidosKO = historicoPedidosKO;
	}
	public Date getFechaInicioSesion() {
		return fechaInicioSesion;
	}
	public void setFechaInicioSesion(Date fechaInicioSesion) {
		this.fechaInicioSesion = fechaInicioSesion;
	}
	public String getNivelAutenticacion() {
		return nivelAutenticacion;
	}
	public void setNivelAutenticacion(String nivelAutenticacion) {
		this.nivelAutenticacion = nivelAutenticacion;
	}
	public String getUsuarioSeycon() {
		return usuarioSeycon;
	}
	public void setUsuarioSeycon(String usuarioSeycon) {
		this.usuarioSeycon = usuarioSeycon;
	}
	public String getNifUsuarioSeycon() {
		return nifUsuarioSeycon;
	}
	public void setNifUsuarioSeycon(String nifUsuarioSeycon) {
		this.nifUsuarioSeycon = nifUsuarioSeycon;
	}
	public String getNombreUsuarioSeycon() {
		return nombreUsuarioSeycon;
	}
	public void setNombreUsuarioSeycon(String nombreUsuarioSeycon) {
		this.nombreUsuarioSeycon = nombreUsuarioSeycon;
	}

}
