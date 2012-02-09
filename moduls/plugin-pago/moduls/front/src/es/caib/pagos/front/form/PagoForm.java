package es.caib.pagos.front.form;

import org.apache.struts.validator.ValidatorForm;

public class PagoForm extends ValidatorForm
{
	
	/**
	 * Modo pago (T/P/B)
	 */	
	private String modoPago;
	/**
	 * Titular de la tarjeta
	 */
	private String titularTarjeta;
	/**
	 * Numero tarjeta
	 */	
	private String numeroTarjeta;
	/**
	 * Fecha caducidad tarjeta (MMAA)
	 */	
	private String fechaCaducidadTarjeta;
	/**
	 * Anyo caducidad tarjeta
	 */
	private String anyoCaducidadTarjeta;
	/**
	 * Mes caducidad tarjeta
	 */
	private String mesCaducidadTarjeta;
	/**
	 * Codigo verificacion tarjeta
	 */	
	private String codigoVerificacionTarjeta;
	/**
	 * Banco para realizar pago banca online
	 */
	private String banco;
	
	public String getCodigoVerificacionTarjeta() {
		return codigoVerificacionTarjeta;
	}
	public void setCodigoVerificacionTarjeta(String codigoVerificacionTarjeta) {
		this.codigoVerificacionTarjeta = codigoVerificacionTarjeta;
	}
	public String getNumeroTarjeta() {
		return numeroTarjeta;
	}
	public void setNumeroTarjeta(String numeroTarjeta) {
		this.numeroTarjeta = numeroTarjeta;
	}
	public String getFechaCaducidadTarjeta() {
		return fechaCaducidadTarjeta;
	}
	public void setFechaCaducidadTarjeta(String fechaCaducidadTarjeta) {
		this.fechaCaducidadTarjeta = fechaCaducidadTarjeta;
	}
	public String getModoPago() {
		return modoPago;
	}
	public void setModoPago(String modoPago) {
		this.modoPago = modoPago;
	}
	public String getTitularTarjeta() {
		return titularTarjeta;
	}
	public void setTitularTarjeta(String titularTarjeta) {
		this.titularTarjeta = titularTarjeta;
	}
	public String getAnyoCaducidadTarjeta() {
		return anyoCaducidadTarjeta;
	}
	public void setAnyoCaducidadTarjeta(String anyoCaducidadTarjeta) {
		this.anyoCaducidadTarjeta = anyoCaducidadTarjeta;
	}
	public String getMesCaducidadTarjeta() {
		return mesCaducidadTarjeta;
	}
	public void setMesCaducidadTarjeta(String mesCaducidadTarjeta) {
		this.mesCaducidadTarjeta = mesCaducidadTarjeta;
	}
	public String getBanco() {
		return banco;
	}
	public void setBanco(String banco) {
		this.banco = banco;
	}
	
	
}
