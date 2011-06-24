package es.caib.sistra.plugins.firma.impl.caib.applet;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.*;


/**
 * Clase para la gestión de mensajes
 */
public class SignaturaClientProperties 
{

private static final String CONFIGURATION_FILE = "signatura";
  private PropertyResourceBundle properties;

  public SignaturaClientProperties(String idioma) throws Exception
  {
	  try {
		  properties =  (PropertyResourceBundle) ResourceBundle.getBundle(CONFIGURATION_FILE,  new Locale(idioma));
	  } catch (MissingResourceException e) {
		  properties =  (PropertyResourceBundle) ResourceBundle.getBundle(CONFIGURATION_FILE,  new Locale("ca"));
	  }
  }



  public String getProperty(String name) 
  {
    String value;
    try{
      value = properties.getString(name);
    }
    catch (MissingResourceException me)
    {
      value = name;
    }
    return value;
  }
}