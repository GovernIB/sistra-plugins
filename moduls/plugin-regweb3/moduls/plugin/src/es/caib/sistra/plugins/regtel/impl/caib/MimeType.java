package es.caib.sistra.plugins.regtel.impl.caib;

import java.util.HashMap;
import java.util.Map;

/**
 * Mimetypes usados en tramitacion. 
 * Si no se devuelve generico:  application/octet-stream
 *
 */

public final class MimeType  {
 
 /**
  * Default Mime type
  */
 private static String s_strDefaultMimeType = "application/octet-stream";

 /**
  * Hash map that will be stored types in
  */
 private static Map s_mapMimeTypes = null;
 
 // Constructors /////////////////////////////////////////////////////////////
 
 /**
  * Static initializer;
  */
 static 
 {
    s_mapMimeTypes = new HashMap();
    
    // MsOffice
    s_mapMimeTypes.put("doc", "application/msword");
    s_mapMimeTypes.put("xls", "application/vnd.ms-excel");
    s_mapMimeTypes.put("ppt", "application/vnd.ms-powerpoint");
    s_mapMimeTypes.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    s_mapMimeTypes.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    s_mapMimeTypes.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");  
    
    // OpenOffice
    s_mapMimeTypes.put("odt", "application/vnd.oasis.opendocument.text");
    s_mapMimeTypes.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
    s_mapMimeTypes.put("odp", "application/vnd.oasis.opendocument.presentation");
    
    // Texto plano
    s_mapMimeTypes.put("txt", "text/plain");
    
    // PDF
    s_mapMimeTypes.put("pdf", "application/pdf");
    
    // XML
    s_mapMimeTypes.put("xml", "application/xml");
    
    // ZIP
    s_mapMimeTypes.put("zip", "application/zip");
    
    // Imagenes
    s_mapMimeTypes.put("jpeg", "image/jpeg");    
    s_mapMimeTypes.put("jpg", "image/jpeg");
    s_mapMimeTypes.put("gif", "image/gif");
    s_mapMimeTypes.put("png", "image/png");
    s_mapMimeTypes.put("tiff", "image/tiff");    
          
 }
 
 // Constructors /////////////////////////////////////////////////////////////
 
 /** 
  * Private constructor since this class cannot be instantiated
  */
 private MimeType()
 {
    // Do nothing
 }
 
 // Public methods ///////////////////////////////////////////////////////////
 
 /**
  * Method getting particular Mime type for the extension (key)
  * 
  * @param strKey - key value for returning Mime type
  * @return String
  */
 public static String getMimeTypeForExtension(
    String strKey
 )
 {
    String strMimeType = null;

    // get value for particular key
    strMimeType = (String) s_mapMimeTypes.get(strKey);
    if ((strMimeType == null) || (strMimeType.trim().length() == 0))
    {
       strMimeType = s_strDefaultMimeType;
    }
    
    return strMimeType;
 }
}