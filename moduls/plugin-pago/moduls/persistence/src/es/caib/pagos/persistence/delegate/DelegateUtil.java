package es.caib.pagos.persistence.delegate;

/**
 * Define m�todos est�ticos para obtener delegates locales
 */
public final class DelegateUtil {

    private DelegateUtil() {

    }
  
    public static SesionPagoDelegate getSesionPagoDelegate() {
        return (SesionPagoDelegate) DelegateFactory.getDelegate(SesionPagoDelegate.class);
    }
    
    public static ConfiguracionDelegate getConfiguracionDelegate() {
        return (ConfiguracionDelegate) DelegateFactory.getDelegate(ConfiguracionDelegate.class);
    }
    
 }
