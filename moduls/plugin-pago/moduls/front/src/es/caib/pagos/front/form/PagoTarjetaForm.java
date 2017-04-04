package es.caib.pagos.front.form;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.struts.validator.ValidatorForm;

public class PagoTarjetaForm extends ValidatorForm
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
	
	private List meses;
	
	private List anyos;
	
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
	
	
	public List getMeses() {
		String[] meses = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"}; 
		return Arrays.asList(meses);
	}
	
	public List getAnyos() {
		Calendar cal = Calendar.getInstance();
		int anyoActual = cal.get(Calendar.YEAR);
		Integer[] anyos = {anyoActual, anyoActual + 1, anyoActual + 2, anyoActual + 3, anyoActual + 4, anyoActual + 5, anyoActual + 6, anyoActual + 7, anyoActual + 8, anyoActual + 9, anyoActual + 10};
		return Arrays.asList(anyos);
	}
	
	
}
