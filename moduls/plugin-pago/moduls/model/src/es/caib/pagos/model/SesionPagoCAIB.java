package es.caib.pagos.model;

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
	private DatosPago datosPago;
	private EstadoSesionPago estadoPago;
	private SesionSistra sesionSistra;
	
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
	public Date getFechaInicioSesion() {
		return fechaInicioSesion;
	}
	public void setFechaInicioSesion(Date fechaInicioSesion) {
		this.fechaInicioSesion = fechaInicioSesion;
	}

}
