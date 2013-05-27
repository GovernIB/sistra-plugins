package es.caib.pagosTPV.persistence.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import es.caib.pagosTPV.model.SesionPagoCAIB;

public class PagoTPVUtil {

	public static String generarFirmaNotificacionTPV(SesionPagoCAIB sesionPago,
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
