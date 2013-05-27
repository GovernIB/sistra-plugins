package es.caib.pagosTPV.model;

import java.io.Serializable;

import es.caib.sistra.plugins.pagos.DatosPago;
import es.caib.sistra.plugins.pagos.EstadoSesionPago;
import es.caib.sistra.plugins.pagos.SesionSistra;

/**
 * Simula datos sesion de pago
 */
public class SesionPagoCAIB implements Serializable{
	private String localizador;
	private String historicoPedidosKO;
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
	public String getHistoricoPedidosKO() {
		return historicoPedidosKO;
	}
	public void setHistoricoPedidosKO(String historicoPedidosKO) {
		this.historicoPedidosKO = historicoPedidosKO;
	}

}
