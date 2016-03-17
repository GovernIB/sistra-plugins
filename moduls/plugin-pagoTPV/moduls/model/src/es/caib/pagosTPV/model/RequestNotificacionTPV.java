package es.caib.pagosTPV.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * Peticion de notificacion de pago por parte del TPV.
 *
 */
public class RequestNotificacionTPV {
	/**
	 * Version firma.
	 */
	private String signatureVersion;
	/**
	 * Parametros firmados.
	 */
	private String merchantParameters;
	/**
	 * Firma.
	 */
	private String signature;
	
	public String print() {
	  return	ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	public String getSignatureVersion() {
		return signatureVersion;
	}
	public void setSignatureVersion(String signatureVersion) {
		this.signatureVersion = signatureVersion;
	}
	public String getMerchantParameters() {
		return merchantParameters;
	}
	public void setMerchantParameters(String merchantParameters) {
		this.merchantParameters = merchantParameters;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
}
