package es.caib.pagosTPV.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * Peticion de notificacion de pago por parte del TPV.
 *
 */
public class RequestNotificacionTPVDecoded {
	
	/**
	 * Fecha: dd/mm/yyyy.
	 */
	private String date;
	/**
	 * Hora: HH:mm.
	 */
	private String hour;
	/**
	 * Importe.
	 */
	private String amount;
	/**
	 * Moneda.
	 */
	private String currency;
	/**
	 * Orden.
	 */
	private String order;
	/**
	 * Codigo comercio.
	 */
	private String merchantCode;
	/**
	 * Terminal.
	 */
	private String terminal;	
	/**
	 * Codigo respuesta.
	 */
	private String response;
	/**
	 * Datos propios comercio: localizador sesion pago.
	 */
	private String merchantData;
	/**
	 * Si es pago seguro (0:no / 1: si).
	 */
	private String securePayment;
	/**
	 * Tipo transaccion.
	 */
	private String transactionType;
	/**
	 * Pais emision tarjeta.
	 */
	private String cardCountry;
	/**
	 * Codigo autorizacion.
	 */
	private String authorisationCode;
	/**
	 * Idioma.
	 */
	private String consumerLanguage;
	/**
	 * Tipo tarjeta:C – Crédito / D - Débito
	 */
	private String cardType;	
	/**
	 * Firma.
	 */
	private String signature;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getMerchantCode() {
		return merchantCode;
	}
	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getMerchantData() {
		return merchantData;
	}
	public void setMerchantData(String merchantData) {
		this.merchantData = merchantData;
	}
	public String getSecurePayment() {
		return securePayment;
	}
	public void setSecurePayment(String securePayment) {
		this.securePayment = securePayment;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public String getCardCountry() {
		return cardCountry;
	}
	public void setCardCountry(String cardCountry) {
		this.cardCountry = cardCountry;
	}
	public String getAuthorisationCode() {
		return authorisationCode;
	}
	public void setAuthorisationCode(String authorisationCode) {
		this.authorisationCode = authorisationCode;
	}
	public String getConsumerLanguage() {
		return consumerLanguage;
	}
	public void setConsumerLanguage(String consumerLanguage) {
		this.consumerLanguage = consumerLanguage;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String print() {
	  return	ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}	
}
