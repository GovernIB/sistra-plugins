package es.caib.sistra.plugins.sms.impl.bit;



import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.caib.sistra.plugins.sms.ConstantesSMS;
import es.caib.sistra.plugins.sms.EstadoEnvio;
import es.caib.sistra.plugins.sms.PluginSmsIntf;
import es.caib.sistra.plugins.sms.impl.bit.Configuracion;

/**
 * 	
 * 	Plugin para envio de sms a traves de provato
 *
 */
public class PluginSmsBIT implements PluginSmsIntf{
	
	private Log log = LogFactory.getLog( PluginSmsBIT.class);

	public void enviarSMS(String idEnvio, String cuentaSMS,String telefono,String mensaje,boolean inmediato) throws Exception{
		
        String user = Configuracion.getInstance().getProperty("sms.user");
        String password = Configuracion.getInstance().getProperty("sms.password");        
        
        int posicion = cuentaSMS.indexOf(":");
        
        String sender = "";
        String campaign = "";
        
        if (posicion != -1) {
        	 sender   = cuentaSMS.substring(0,cuentaSMS.indexOf(":"));
        	 campaign = cuentaSMS.substring(cuentaSMS.indexOf(":") + 1,cuentaSMS.length());
        }
                        
        String url = Configuracion.getInstance().getProperty("sms.url");
        String servlet = Configuracion.getInstance().getProperty("sms.servlet");
		
	    try {
	          // Preparamos la llamada
	          PostMethod filePost = new PostMethod(url + servlet);
	          String header = "Basic " + new String(Base64.encodeBase64((user + ":" + password).getBytes("UTF-8")), "UTF-8");
	          filePost.setRequestHeader("Authorization", header);
	          
	          // Establecemos los parámetros
	          filePost.addParameter("idCampanya", campaign);
	          filePost.addParameter("remitente", sender);

	          // Añadimos 34 por delante del teléfono porque SMSBIT lo requiere
	          filePost.addParameter("numero", "34"+telefono);    
	          filePost.addParameter("texto", mensaje);
	 
	   
	          // Ejecutamos la llamada
	          HttpClient client = new HttpClient();
	          int status = client.executeMethod(filePost);
	          String respuesta = filePost.getResponseBodyAsString();
	          if (status == 500) { // Si es un error controlado lo obtenemos
	              throw new Exception(filePost.getResponseBodyAsString());
	          } else if (status != 200) { // En caso contrario activamos una excepción
	              throw new Exception("Error enviando sms, el servidor responde " + status);
	          }
	      
	      } catch (Exception e) {
	    	  throw new Exception("Error enviando sms, el servidor responde " + e.getMessage());
	      }
	     
	   }
		
		

	
	public EstadoEnvio consultarEstadoEnvio(String idEnvio) throws Exception{
		EstadoEnvio e = new EstadoEnvio();
		e.setEstado(ConstantesSMS.ESTADO_DESCONOCIDO);
		e.setDescripcionEstado("Operacion no soportada");
		return e;
	}

}
