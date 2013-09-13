package es.caib.sistra.plugins.firma.impl.caib.applet;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Panel;
//import java.security.AccessController;
//import java.security.PrivilegedAction;

import javax.swing.JComboBox;

import es.caib.signatura.api.UpgradeNeededException;
import es.caib.sistra.plugins.firma.impl.caib.UtilFirmaCAIB;

public class AppletFirma  extends Applet {		
		
	protected UtilFirmaCAIB firma = null;	
	private String password = null;
	
	private Panel panel = null;
	private JComboBox certificadosList = new JComboBox();
	
    protected String lastError = null; 
    protected Exception exceptionError=null;
    private SignaturaClientProperties signaturaProperties;
  
    private StringBuffer ficheroB64Split;
    
    public AppletFirma()
    {
    	super();    
	}
    
    /**
     * Inicializa applet
     */
    public void init()
    {    	
    	// Creamos layout
    	if (panel == null) {
			panel = new Panel();
			panel.setLayout(null);			
			panel.setBackground(Color.GRAY);
			
			
			panel.setLocation(2, 1);
			panel.setSize(223, 29);
			this.setSize(225, 32);
			
			this.setLayout(null);
			this.add(panel);
			this.setBackground(Color.white);

			certificadosList.setBounds(3, 5, 217, 21);
			panel.add(certificadosList);			
		}		
    	
    	// Accedemos a fichero de mensajes
		try
		{
			String idioma = this.getParameter("idioma");
			if (idioma == null) idioma = "es";		
			signaturaProperties = new SignaturaClientProperties(idioma);			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}    	
    }
       
    
    /**
     * Inicializa dispositivo de firma
     * @return
     */
    public boolean inicializarDispositivo( final String contentType ){
    	boolean res = inicializarDispositivoImpl( contentType );
		return res;
    	/*
    	return ((Boolean)  AccessController.doPrivileged(
				 new PrivilegedAction() {
				   public Object run() {
					   boolean res = inicializarDispositivoImpl( contentType );
					   return new Boolean(res);
				   }
				 }
			   )).booleanValue();
    	 */			  
    }
    
    
    /**
     * Inicializa dispositivo de firma
     * @return
     */
    private boolean inicializarDispositivoImpl( String contentType ){
    	// Creamos objeto de firma y lo inicializamos
    	try{
    		boolean useAlwaysDefaultContentType = "true".equals( this.getParameter( "useAlwaysDefaultContentType" ) );
			firma = new UtilFirmaCAIB( useAlwaysDefaultContentType );	
			firma.iniciarDispositivo();
			String list[] = firma.getCertList( contentType );
			if (list.length <= 0) {
				setLastError("certListNotFound");
				return false;
			}
			
			certificadosList.removeAllItems();
			
			for (int i=0;i<list.length;i++){
				certificadosList.addItem(list[i]);
			}
			return true;
    	} catch (UpgradeNeededException e) {
    		setLastError("upgradeneeded");
			e.printStackTrace();
			return false;
		}catch (Exception ex){    		
			setLastError("initProviderError");
			ex.printStackTrace();
			return false;
    	}
    }
    
    
    /**
     * Establece password a utilizar
     * @param pass
     */
	public void setPassword(String pass)
	{
		password = pass;		
	}
    
	 /**
     * Realiza firma de cadena
     * @param cadena
     * @return
     */	
	 public String firmarCadena(final String cadena, final String contentType ){
		 /*
		  String firma = (String)  AccessController.doPrivileged(
					 new PrivilegedAction() {
					   public Object run() {
						   return firmarCadenaImpl(cadena, contentType );
					   }
					 }
				   );		  
		  		  
		  return firma;
		  */
		 return firmarCadenaImpl(cadena, contentType );
	    }
    /**
     * Realiza firma de cadena
     * @param cadena
     * @return
     */	
	 private String firmarCadenaImpl(String cadena, String contentType )
	{
		try
		{				
			// Obtenemos certificado con el que firmar
			Object objCertificado = certificadosList.getSelectedItem();
			if (objCertificado == null){
				throw new Exception ("No se encuentra ningún certificado disponible");
			}
			
			return firma.firmarCadena(cadena, objCertificado.toString(), password, contentType);
			//return URIComponentEncodedFirma;
		}
		catch (Exception ex)
		{
			setLastError("signatureError",ex);	
			ex.printStackTrace();
			return null; 
		}
	}		
    
    /**
     * Realiza firma de fichero
     * @param cadena
     * @return
     */	
	 public String firmarFichero(final String cadena, final String contentType ){	   
		/*
		    return (String)  AccessController.doPrivileged(
					 new PrivilegedAction() {
					   public Object run() {
						   return firmarFicheroImpl(cadena, contentType );
					   }
					 }
				   );
	  */
		 return firmarFicheroImpl(cadena, contentType );
	    }
	 
	 /**
     * Realiza firma de fichero en b64
     * @param cadena
     * @return
     */	
	 public String firmarFicheroB64(final String b64, final String contentType ){	   
		/*
		    return (String)  AccessController.doPrivileged(
					 new PrivilegedAction() {
					   public Object run() {
						   return firmarFicheroB64Impl(b64, contentType );
					   }
					 }
				   );
         */
		 return firmarFicheroB64Impl(b64, contentType );
	    }
    
    /**
     * Realiza firma
     * @param cadena
     * @return
     */	
	 private String firmarFicheroImpl(String path, String contentType )
	{
		try
		{	
			// Obtenemos certificado
			Object objCertificado = certificadosList.getSelectedItem();
			if (objCertificado == null){
				throw new Exception ("No se encuentra ningún certificado disponible");
			}
									
			// Realizamos firma
			return firma.firmarFichero(path, objCertificado.toString(), password, contentType );
		}
		catch (Exception ex)
		{
			setLastError("signatureError",ex);	
			ex.printStackTrace();
			return null; 
		}
	}		
	 
	 
	    /**
	     * Realiza firma
	     * @param cadena
	     * @return
	     */	
		 private String firmarFicheroB64Impl(String b64, String contentType )
		{
			try
			{	
				// Obtenemos certificado
				Object objCertificado = certificadosList.getSelectedItem();
				if (objCertificado == null){
					throw new Exception ("No se encuentra ningún certificado disponible");
				}
				
				// Realizamos firma
				return firma.firmarFicheroB64(b64, objCertificado.toString(), password, contentType );
				
			}
			catch (Exception ex)
			{
				setLastError("signatureError",ex);	
				ex.printStackTrace();
				return null; 
			}
		}		

	/**
	 * Establece ultimo error generado
	 * @param codError The aError to set
	 */
	private void setLastError(String codError)
	{
		this.lastError = codError;
		this.exceptionError = null;
	}
	
	/**
	 * Establece ultimo error generado
	 * @param codError The aError to set
	 */
	private void setLastError(String codError,Exception exc)
	{
		this.lastError = codError;
		this.exceptionError = exc;
	}
	
	 /**
     * Obtiene error generado (traducido)
     * @return
     */
    public String getLastError()
    {
    	try{
    		String message = signaturaProperties.getProperty(lastError);
    		if (this.exceptionError != null) message = message + ". Error : " + this.exceptionError.toString();    		    		
    		return message;
    	}catch (Exception ex){
    		return lastError;
    	}
    }           
    
    
    /**
     * Pasa cadena a B64
     * @param cadena
     * @return
     */
    public String cadenaToBase64( final String cadena)
    {
    	/*
    	return (String)  AccessController.doPrivileged(
				 new PrivilegedAction() {
				   public Object run() {
					   return cadenaToBase64Impl( cadena );
				   }
				 }
			   );
		*/
    	 return cadenaToBase64Impl( cadena );
    }
    
    /**
     * 
     * @param cadena
     * @return
     */
    private String cadenaToBase64Impl( String cadena ){    	
    	try{
    		return UtilFirmaCAIB.cadenaToBase64UrlSafe(cadena);
    	}catch (Exception ex){    		
			setLastError("cadenaToBase64Error");
			ex.printStackTrace();
			return null;
    	}	              
    } 
    
    /**
     * 
     * @param cadenaB64
     * @return
     */
    public String base64ToCadena( final String cadenaB64)
    {
    	/*
    	return (String)  AccessController.doPrivileged(
				 new PrivilegedAction() {
				   public Object run() {
					   return base64ToCadenaImpl( cadenaB64 );
				   }
				 }
			   );
		*/
    	return base64ToCadenaImpl( cadenaB64 );
    }
    
    
    /**
     * Pasa a cadena una cadena en B64
     * @param cadena
     * @return
     */
    private String base64ToCadenaImpl(String cadenaB64){
    	try{
    		return UtilFirmaCAIB.base64UrlSafeToCadena(cadenaB64);
    	}catch(Exception ex){
    		setLastError("base64ToCadenaError");
    		ex.printStackTrace();
    		return null;
    	}
    }
    
    /**
     * Indica que se va a pasar un fichero en b64 troceado.
     * 
     */
    public void splitFicheroB64(int fileSize){
    	ficheroB64Split = new StringBuffer(fileSize);
    }
    
    /**
     * Añade split
     * @param split
     */
    public void addSplitFicheroB64(String split){
    	ficheroB64Split.append(split);
    }
    
   
    /**
     * Realiza firma de fichero en b64 troceado
     * @param cadena
     * @return
     */	
	 public String firmarFicheroB64Split(final String contentType ){	   
		 return firmarFicheroB64Impl(ficheroB64Split.toString(), contentType );
    }
    

}
