package es.caib.pagos.persistence.delegate;

/**
 * Excepci�n producida en la capa delegate.
 */
public class DelegateException extends Exception {

    public DelegateException(Throwable cause) {
        super(cause.getMessage(),cause);
    }
}
