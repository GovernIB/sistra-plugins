package es.caib.pagos.client;

public class ResultadoInicioPago {
	
	private String token;
	
	private String localizador;

	public ResultadoInicioPago() {
	}

	public ResultadoInicioPago(final String token, final String localizador) {
		super();
		this.token = token;
		this.localizador = localizador;
	}

	public String getLocalizador() {
		return localizador;
	}

	public void setLocalizador(final String localizador) {
		this.localizador = localizador;
	}

	public String getToken() {
		return token;
	}

	public void setToken(final String token) {
		this.token = token;
	}

}
