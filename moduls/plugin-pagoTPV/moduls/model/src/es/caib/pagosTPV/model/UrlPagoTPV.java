package es.caib.pagosTPV.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Datos para inciar pago contra TPV.
 * @author rsanz
 *
 */
public class UrlPagoTPV {
	
	/**
	 * Url incio pago TPV.
	 */
	private String urlPagoTPV;
	/**
	 * Nombre comercio.
	 */
	private String merchantName;
	/**
	 * Código comercio (FUC).
	 */
	private String merchantCode;
	/**
	 * Terminal comercio.
	 */
	private String merchantTerminal;
	/**
	 * Firma pedido.
	 */
	private String merchantSignature;
	/**
	 * Moneda.
	 */
	private String merchantCurrency;
	/**
	 * Tipo transaccion.
	 */
	private String merchantTransactionTypeAut;
	/**
	 * Titular.
	 */
	private String merchantTitular;
	/**
	 * Num pedido.
	 */
	private String merchantOrder;
	/**
	 * Importe (cents).
	 */
	private String merchantAmount;
	/**
	 * Descripcion producto.
	 */
	private String merchantProductDescription;
	/**
	 * Idioma.
	 */
	private String merchantConsumerLanguage;
	/**
	 * Url notificacion tras pago.
	 */
	private String merchantMerchantURL;
	/**
	 * Url redireccion tras pago OK.
	 */
	private String merchantUrlOK;
	/**
	 * Url redireccion tras pago KO.
	 */
	private String merchantUrlKO;
	/**
	 * Datos comercio: enviaremos localizador sesion pago.
	 */
	private String merchantData;
	
	public String getUrlPagoTPV() {
		return urlPagoTPV;
	}
	public void setUrlPagoTPV(String urlPagoTPV) {
		this.urlPagoTPV = urlPagoTPV;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getMerchantCode() {
		return merchantCode;
	}
	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}
	public String getMerchantTerminal() {
		return merchantTerminal;
	}
	public void setMerchantTerminal(String merchantTerminal) {
		this.merchantTerminal = merchantTerminal;
	}
	public String getMerchantSignature() {
		return merchantSignature;
	}
	public void setMerchantSignature(String merchantSignature) {
		this.merchantSignature = merchantSignature;
	}
	public String getMerchantCurrency() {
		return merchantCurrency;
	}
	public void setMerchantCurrency(String merchantCurrency) {
		this.merchantCurrency = merchantCurrency;
	}
	public String getMerchantTransactionTypeAut() {
		return merchantTransactionTypeAut;
	}
	public void setMerchantTransactionTypeAut(String merchantTransactionTypeAut) {
		this.merchantTransactionTypeAut = merchantTransactionTypeAut;
	}
	public String getMerchantTitular() {
		return merchantTitular;
	}
	public void setMerchantTitular(String merchantTitular) {
		this.merchantTitular = merchantTitular;
	}
	public String getMerchantOrder() {
		return merchantOrder;
	}
	public void setMerchantOrder(String merchantOrder) {
		this.merchantOrder = merchantOrder;
	}
	public String getMerchantAmount() {
		return merchantAmount;
	}
	public void setMerchantAmount(String merchantAmount) {
		this.merchantAmount = merchantAmount;
	}
	public String getMerchantProductDescription() {
		return merchantProductDescription;
	}
	public void setMerchantProductDescription(String merchantProductDescription) {
		this.merchantProductDescription = merchantProductDescription;
	}
	public String getMerchantConsumerLanguage() {
		return merchantConsumerLanguage;
	}
	public void setMerchantConsumerLanguage(String merchantConsumerLanguage) {
		this.merchantConsumerLanguage = merchantConsumerLanguage;
	}
	public String getMerchantMerchantURL() {
		return merchantMerchantURL;
	}
	public void setMerchantMerchantURL(String merchantMerchantURL) {
		this.merchantMerchantURL = merchantMerchantURL;
	}
	public String getMerchantUrlOK() {
		return merchantUrlOK;
	}
	public void setMerchantUrlOK(String merchantUrlOK) {
		this.merchantUrlOK = merchantUrlOK;
	}
	public String getMerchantUrlKO() {
		return merchantUrlKO;
	}
	public void setMerchantUrlKO(String merchantUrlKO) {
		this.merchantUrlKO = merchantUrlKO;
	}

	public String print() {
	 return	ToStringBuilder.reflectionToString(this,ToStringStyle.MULTI_LINE_STYLE);
	}
	public String getMerchantData() {
		return merchantData;
	}
	public void setMerchantData(String merchantData) {
		this.merchantData = merchantData;
	}
}
