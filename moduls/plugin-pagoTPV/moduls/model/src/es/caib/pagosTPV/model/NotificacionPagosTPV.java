package es.caib.pagosTPV.model;

import java.io.Serializable;

/**
 * Notificacion enviada por TPV.
 *
 */
public class NotificacionPagosTPV implements Serializable
{
	/**
	 * Numero de orden.
	 */
	private String orden;
	/**
	 * Localizador sesion pago.
	 */
	private String localizador;
	/**
	 * Codigo resultado.
	 */
	private String resultado;
	/**
	 * Datos firmados.
	 */
	private String datosFirmados;	
	/**
	 * Firma notificacion.
	 */
	private String firma;
	/**
	 * Autorizacion.
	 */
	private String autorizacion;
	/**
	 * Fecha.
	 */
	private String fecha;
	/**
	 * Hora.
	 */
	private String hora;
	
	
	public String getOrden() {
		return orden;
	}
	public void setOrden(String orden) {
		this.orden = orden;
	}
	public String getResultado() {
		return resultado;
	}
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	public String getFirma() {
		return firma;
	}
	public void setFirma(String firma) {
		this.firma = firma;
	}
	public String getAutorizacion() {
		return autorizacion;
	}
	public void setAutorizacion(String autorizacion) {
		this.autorizacion = autorizacion;
	}
	public String getLocalizador() {
		return localizador;
	}
	public void setLocalizador(String localizador) {
		this.localizador = localizador;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	public String getDatosFirmados() {
		return datosFirmados;
	}
	public void setDatosFirmados(String datosFirmados) {
		this.datosFirmados = datosFirmados;
	}
	
	public boolean isPagada() {
		return getResultado() != null && getResultado().startsWith("00");
	}
	
}
