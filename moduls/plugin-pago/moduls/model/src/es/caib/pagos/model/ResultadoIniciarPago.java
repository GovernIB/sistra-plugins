package es.caib.pagos.model;

/**
 * Resultado iniciar pago.
 * @author rsanz
 *
 */
public class ResultadoIniciarPago {

	/**
	 * Resultado (url para pago por banca y token para pago por tajeta).
	 */
	private String resultado;

	/**
	 * Indica si se ha excedido el tiempo maximo de pago.
	 */
	private boolean tiempoExcedido;
	
	/**
	 * En caso de exceder el tiempo de pago, indica el mensaje a mostrar.
	 */
	private String mensajeTiempoExcedido;

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	public boolean isTiempoExcedido() {
		return tiempoExcedido;
	}

	public void setTiempoExcedido(boolean tiempoExcedido) {
		this.tiempoExcedido = tiempoExcedido;
	}

	public String getMensajeTiempoExcedido() {
		return mensajeTiempoExcedido;
	}

	public void setMensajeTiempoExcedido(String mensajeTiempoExcedido) {
		this.mensajeTiempoExcedido = mensajeTiempoExcedido;
	}

		
}
