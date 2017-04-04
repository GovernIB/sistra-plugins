package es.caib.pagosTPV.persistence.delegate;

/**
 * Define métodos estáticos para obtener delegates locales
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
    
    public static NotificacionPagosTPVDelegate getNotificacionPagosTPVDelegateDelegate() {
        return (NotificacionPagosTPVDelegate) DelegateFactory.getDelegate(NotificacionPagosTPVDelegate.class);
    }
    
    public static BackPagosTPVDelegate getBackPagosTPVDelegate() {
        return (BackPagosTPVDelegate) DelegateFactory.getDelegate(BackPagosTPVDelegate.class);
    }
    
 }
