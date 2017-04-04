package es.caib.pagosTPV.back.form;

import org.apache.struts.validator.ValidatorForm;

public class SesionTPVForm extends ValidatorForm
{
	private String localizador;
	
	public String getLocalizador() {
		return localizador;
	}

	public void setLocalizador(String localizador) {
		this.localizador = localizador;
	}

}
