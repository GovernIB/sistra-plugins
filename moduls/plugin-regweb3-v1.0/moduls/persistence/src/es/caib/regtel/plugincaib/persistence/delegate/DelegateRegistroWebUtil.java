package es.caib.regtel.plugincaib.persistence.delegate;

/**
 * Define métodos estáticos para obtener delegates locales
 */
public final class DelegateRegistroWebUtil {

    private DelegateRegistroWebUtil() {

    }
  
    public static RegistroWebDelegate getRegistroWebDelegate() {
        return (RegistroWebDelegate) DelegateFactory.getDelegate(RegistroWebDelegate.class);
    }        
    
 }
