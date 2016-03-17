package es.caib.pagosTPV.persistence.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import es.caib.pagosTPV.model.NotificacionPagosTPV;
import es.caib.pagosTPV.model.RequestNotificacionTPV;
import es.caib.pagosTPV.model.RequestNotificacionTPVDecoded;
import es.caib.pagosTPV.model.SesionPagoCAIB;
import es.caib.pagosTPV.model.UrlPagoTPV;

public class PagoTPVUtil {
    
	// 
	// FUNCIONES FIRMA
	//
	
	/**
	 * Genera peticion pedido.
	 * @param pagoTPV Datos pedido
	 * @return Genera peticion en formato JSON en B64
	 * @throws Exception
	 */
	public static String generarParametrosPedidoTPV(UrlPagoTPV pagoTPV) throws Exception {
	    try {
	    	ApiMacSha256 api = crearPeticionApi(pagoTPV);
	    	String merchantParameters = api.createMerchantParameters();	    		    	
	    	return merchantParameters;
	    } catch (Exception e) {
	        throw new Exception("No se ha podido generar parametros pedido", e);
	    }
	}
	
	/**
	 * Genera firma pedido.
	 * @param pagoTPV Datos pedido
	 * @param merchantPassword Password comercio
	 * @return Firma pedido
	 * @throws Exception
	 */
	public static String generarFirmaPedidoTPV(UrlPagoTPV pagoTPV, String merchantPassword) throws Exception {
	    try {
	    	ApiMacSha256 api = crearPeticionApi(pagoTPV);
	    	String firma = api.createMerchantSignature(merchantPassword);
	    	return firma;
	    } catch (Exception e) {
	        throw new Exception("No se ha podido generar firma pedido", e);
	    }
	}
	
	

	public static String generarFirmaNotificacionTPV( NotificacionPagosTPV notificacion, String merchantPassword ) throws Exception {
		ApiMacSha256 api = new ApiMacSha256();
		api.decodeMerchantParameters(notificacion.getDatosFirmados());
		String firma = api.createMerchantSignatureNotif(merchantPassword, notificacion.getDatosFirmados());
		return firma;
	}
	
	
	public static RequestNotificacionTPVDecoded decodeRequestNotificacionTPV(RequestNotificacionTPV requestNotif) throws Exception {
		ApiMacSha256 api = new ApiMacSha256();
		api.decodeMerchantParameters(requestNotif.getMerchantParameters());
		
		RequestNotificacionTPVDecoded requestNotifDecoded = new RequestNotificacionTPVDecoded();
		requestNotifDecoded.setDate              (api.getParameter("Ds_Date"));
        requestNotifDecoded.setHour              (api.getParameter("Ds_Hour"));
        requestNotifDecoded.setAmount            (api.getParameter("Ds_Amount"));
        requestNotifDecoded.setCurrency          (api.getParameter("Ds_Currency"));
        requestNotifDecoded.setOrder             (api.getParameter("Ds_Order"));
        requestNotifDecoded.setMerchantCode      (api.getParameter("Ds_MerchantCode"));
        requestNotifDecoded.setTerminal          (api.getParameter("Ds_Terminal"));        
        requestNotifDecoded.setResponse          (api.getParameter("Ds_Response"));
        requestNotifDecoded.setMerchantData      (api.getParameter("Ds_MerchantData"));
        requestNotifDecoded.setSecurePayment     (api.getParameter("Ds_SecurePayment"));
        requestNotifDecoded.setTransactionType   (api.getParameter("Ds_TransactionType"));
        requestNotifDecoded.setCardCountry       (api.getParameter("Ds_Card_Country"));
        requestNotifDecoded.setAuthorisationCode (api.getParameter("Ds_AuthorisationCode"));
        requestNotifDecoded.setConsumerLanguage  (api.getParameter("Ds_ConsumerLanguage"));
        requestNotifDecoded.setCardType          (api.getParameter("Ds_Card_Type"));
        
        requestNotifDecoded.setSignature         (requestNotif.getSignature());
        
		return requestNotifDecoded;		
	}
	
	
	public static RequestNotificacionTPV createRequestNotificacionTPV(RequestNotificacionTPVDecoded requestNotifDecoded, String merchantPassword) throws Exception {

		ApiMacSha256 api = new ApiMacSha256();
		
		api.setParameter("Ds_Date", requestNotifDecoded.getDate());
		api.setParameter("Ds_Hour", requestNotifDecoded.getHour());
		api.setParameter("Ds_Amount", requestNotifDecoded.getAmount());
		api.setParameter("Ds_Currency", requestNotifDecoded.getCurrency());
		api.setParameter("Ds_Order", requestNotifDecoded.getOrder());
		api.setParameter("Ds_MerchantCode", requestNotifDecoded.getMerchantCode());
		api.setParameter("Ds_Terminal", requestNotifDecoded.getTerminal());
		api.setParameter("Ds_Response", requestNotifDecoded.getResponse());
		api.setParameter("Ds_MerchantData", requestNotifDecoded.getMerchantData());
		api.setParameter("Ds_SecurePayment", requestNotifDecoded.getSecurePayment());
		api.setParameter("Ds_TransactionType", requestNotifDecoded.getTransactionType());
		api.setParameter("Ds_Card_Country", requestNotifDecoded.getCardCountry());
		api.setParameter("Ds_AuthorisationCode", requestNotifDecoded.getAuthorisationCode());
		api.setParameter("Ds_ConsumerLanguage", requestNotifDecoded.getConsumerLanguage());
		api.setParameter("Ds_Card_Type", requestNotifDecoded.getCardType());
		
		String merchantParameters = api.createMerchantParameters();
		String firma = api.createMerchantSignatureNotif(merchantPassword, merchantParameters);
		
		RequestNotificacionTPV requestNotif = new RequestNotificacionTPV();
		requestNotif.setSignatureVersion("HMAC_SHA256_V1");
		requestNotif.setMerchantParameters(merchantParameters);
		requestNotif.setSignature(firma);
		
		return requestNotif;		
	}
	
	private static ApiMacSha256 crearPeticionApi(UrlPagoTPV pagoTPV) {
		
		ApiMacSha256 api = new ApiMacSha256();
		
		api.setParameter("Ds_Merchant_MerchantName", pagoTPV.getMerchantName());
		api.setParameter("Ds_Merchant_MerchantCode", pagoTPV.getMerchantCode());
		api.setParameter("Ds_Merchant_MerchantData", pagoTPV.getMerchantData());
		api.setParameter("Ds_Merchant_Terminal", pagoTPV.getMerchantTerminal());
		api.setParameter("Ds_Merchant_Order", pagoTPV.getMerchantOrder());
		api.setParameter("Ds_Merchant_Amount", pagoTPV.getMerchantAmount());
		api.setParameter("Ds_Merchant_ProductDescription", pagoTPV.getMerchantProductDescription());
		api.setParameter("Ds_Merchant_Titular", pagoTPV.getMerchantTitular());
		api.setParameter("Ds_Merchant_Currency", pagoTPV.getMerchantCurrency());
		api.setParameter("Ds_Merchant_TransactionType", pagoTPV.getMerchantTransactionTypeAut());
		api.setParameter("Ds_Merchant_MerchantURL", pagoTPV.getMerchantMerchantURL());
		api.setParameter("Ds_Merchant_UrlOK", pagoTPV.getMerchantUrlOK());
		api.setParameter("Ds_Merchant_UrlKO", pagoTPV.getMerchantUrlKO());
		api.setParameter("Ds_Merchant_ConsumerLanguage", pagoTPV.getMerchantConsumerLanguage());
		
		
		/*
		api.setParameter("DS_MERCHANT_AMOUNT", pagoTPV.getMerchantAmount());
		api.setParameter("DS_MERCHANT_ORDER", pagoTPV.getMerchantOrder());
		api.setParameter("DS_MERCHANT_MERCHANTCODE", pagoTPV.getMerchantCode());
		api.setParameter("DS_MERCHANT_CURRENCY", pagoTPV.getMerchantCurrency());
		api.setParameter("DS_MERCHANT_TRANSACTIONTYPE", pagoTPV.getMerchantTransactionTypeAut());
		api.setParameter("DS_MERCHANT_TERMINAL", pagoTPV.getMerchantTerminal());
		api.setParameter("DS_MERCHANT_MERCHANTURL", pagoTPV.getMerchantMerchantURL());
		api.setParameter("DS_MERCHANT_URLOK", pagoTPV.getMerchantUrlOK());
		api.setParameter("DS_MERCHANT_URLKO", pagoTPV.getMerchantUrlKO());
		
		
		// Estas no aparecen en la documentacion
		
		api.setParameter("DS_MERCHANT_PRODUCTDESCRIPTION", pagoTPV.getMerchantProductDescription());
		api.setParameter("DS_MERCHANT_TITULAR", pagoTPV.getMerchantTitular());
		api.setParameter("DS_MERCHANT_CONSUMERLANGUAGE", pagoTPV.getMerchantConsumerLanguage());
		*/
		
		
		return api;
	}
	
	
	public static String generarFirmaNotificacionTPV_SHA1(SesionPagoCAIB sesionPago,
			String resultado) throws Exception {
		//Digest=SHA-1(Ds_Amount + Ds_Order + Ds_MerchantCode + Ds_Currency + Ds_Response + CLAVE SECRETA)
		String signature = new String();
		try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            sha.update(sesionPago.getDatosPago().getImporte().getBytes());
            sha.update(sesionPago.getEstadoPago().getIdentificadorPago().getBytes());
            sha.update(Configuracion.getInstance().getMerchantCodeTPV(sesionPago.getDatosPago().getIdentificadorOrganismo()).getBytes());
            sha.update(Configuracion.getInstance().getMerchantCurrencyTPV().getBytes());
            sha.update(resultado.getBytes());
            String pwd = Configuracion.getInstance().getMerchantPasswordTPV(sesionPago.getDatosPago().getIdentificadorOrganismo());
            byte[] hash = sha.digest(pwd.getBytes());

            int h = 0;
            String s = new String();
            int SHA1_DIGEST_LENGTH = 20;
            for (int i = 0; i < SHA1_DIGEST_LENGTH; i++) {
                h = (int) hash[i];          // Convertir de byte a int
                if (h < 0) {
                    h += 256;  // Si son valores negativos, pueden haber problemas de conversi¢n.
                }
                s = Integer.toHexString(h); // Devuelve el valor hexadecimal como un String
                if (s.length() < 2) {
                	signature = signature.concat("0"); // A?ade un 0 si es necesario
                }
                signature = signature.concat(s); // A?ade la conversi¢n a la cadena ya existente
            }

            signature = signature.toUpperCase(); // Convierte la cadena generada a Mayusculas.
            
        } catch (NoSuchAlgorithmException nsae) {
            throw new Exception("No se ha podido validar firma notificacion", nsae);
        }
		return signature;
	}	
	

}