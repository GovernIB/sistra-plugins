package es.caib.pagos.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import es.caib.pagos.exceptions.CifradoException;
 

/**
 * Utilidades para cifrar.
 * 
 * @author Indra
 * 
 */
public final class Cifrar {
    
	private static final String CHARSET = "UTF-8";
	private static final String ALGORITMO_CLAVE = "DES";
	private static final String ALGORITMO_CIFRADO = "DES/CBC/PKCS5Padding";
	
	private Cifrar(){};
	
	/**
     * Realiza cifrado.
     * 
     * @param plainContent
     *            Contenido a cifrar
     * @param clave
     *            Clave cifrado (8 caracteres).
     *            Vector de inicialización (8 caracteres).
     * @return Contenido cifrado
     * @throws CifradoException 
     */
	public static byte[] cifrar(final byte[] plainContent, final String clave, final String initVector) throws CifradoException {
		return execute(plainContent, clave, initVector, Cipher.ENCRYPT_MODE);
	}
	
	/**
     * Realiza descifrado.
     * 
     * @param encodedContent
     *            Contenido cifrado
     * @param clave
     *            Clave cifrado (8 caracteres).
     *            Vector de inicialización (8 caracteres).
     * @return Contenido descifrado
     * @throws CifradoException 
     */
	public static byte[] descifrar(final byte[] encodedContent, final String clave, final String initVector) throws CifradoException {
		return execute(encodedContent, clave, initVector, Cipher.DECRYPT_MODE);
	}
	
    /**
     * Realiza el cifrado/descifrado según se especifique en los parámetros
     * 
     * @param content
     *            Contenido a cifrar
     * @param clave
     *            Clave cifrado (8 caracteres).
     * @param initVector
     * 			  Vector de inicialización (8 caracteres).
     * @param mode
     * 			  Modo para indicar si se cifra o se descifra
     * @return Contenido cifrado/descifrado
     * @throws CifradoException 
     */
    private static byte[] execute(final byte[] content, final String clave, final String initVector, final int mode) throws CifradoException {
        try {
            final SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITMO_CLAVE);
            final DESKeySpec kspec = new DESKeySpec(clave.getBytes(CHARSET));
            final SecretKey ks = skf.generateSecret(kspec);
            final IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(CHARSET));
            final Cipher cifrado = Cipher.getInstance(ALGORITMO_CIFRADO);
            cifrado.init(mode, ks, iv);
            final byte[] res = cifrado.doFinal(content);

            return res;
        } catch (final NoSuchAlgorithmException nsae) {
            throw new CifradoException("Algoritmo incorrecto --> " + ALGORITMO_CIFRADO + ": " + nsae.getMessage(),
                    nsae);
        } catch (InvalidKeyException ike) {
        	throw new CifradoException("Clave invalida --> " + clave + ": " + ike.getMessage(),
        			ike);
		} catch (UnsupportedEncodingException uee) {
			throw new CifradoException("Encoding no soportado --> " + CHARSET + ": " + uee.getMessage(),
					uee);
		} catch (InvalidKeySpecException ikse) {
			throw new CifradoException("Error en la especificacion de la clave --> " + clave + ": " + ikse.getMessage(),
					ikse);
		} catch (NoSuchPaddingException nspe) {
			throw new CifradoException("No existe el padding: " + nspe.getMessage(),
					nspe);
		} catch (IllegalBlockSizeException ibse) {
			throw new CifradoException("Tamaño de bloque incorrecto: " + ibse.getMessage(),
					ibse);
		} catch (BadPaddingException bpe) {
			throw new CifradoException("Tipo de padding incorrecto: " + bpe.getMessage(),
					bpe);
		} catch (InvalidAlgorithmParameterException iape) {
			throw new CifradoException("Algoritmo incorrecto --> " + ALGORITMO_CIFRADO + ": " + iape.getMessage(),
					iape);
		}

    }

}
