package es.caib.pagos.front.form;

import org.apache.struts.validator.ValidatorForm;

public class PagoBancaForm extends ValidatorForm
{
	
	/**
	 * Modo pago (T/P/B)
	 */	
	private String modoPago;
	
	/**
	 * Banco para realizar pago banca online
	 */
	private String banco;
	
	public String getModoPago() {
		return modoPago;
	}
	public void setModoPago(String modoPago) {
		this.modoPago = modoPago;
	}
	
	public String getBanco() {
		return banco;
	}
	public void setBanco(String banco) {
		this.banco = banco;
	}
	
	
}
