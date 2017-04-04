package es.caib.pagos.util;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.*;

import org.apache.commons.codec.binary.Base64;



/**
 * Clase de utilidades para el manejo de cadenas
 */
public class FuncionesCadena {

	/**
	 * Comprueba si la cadena esta vacia (nula ó ningún carácter excepto blancos)
	 * @param as_valor
	 * @return boolean
	 */
	public static boolean esCadenaVacia(String as_valor)
	{
		if (as_valor == null || as_valor.trim().length() == 0)	return true;
		return false;
	}
	
	/**
	 * Quita los acentos del texto
	 * @param String as_texto Cadena de texto a normalizar
	 * @return String Cadena de texto normalizada
	 **/
	public static String quitaAcentos(String as_texto)
	throws Throwable
	{

		String ls_textoNormalizado = as_texto;
		
		// Mayúsculas con acento grave
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "À","A");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "È","E");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "Ì","I");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "Ò","O");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "Ù","U");


		// Mayúsculas con acento agudo
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "Á","A");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "É","E");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "Í","I");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "Ó","O");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "Ú","U");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "Ý","Y");

		// Minúsculas con acento grave
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "à","a");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "è","e");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "ì","i");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "ò","o");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "ù","u");

		
		// Minúsculas con acento agudo
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "á","a");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "é","e");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "í","i");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "ó","o");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "ú","u");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "ý","y");

		// Minúsculas con diéresis
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "ä","a");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "ë","e");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "ï","i");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "ö","o");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "ü","u");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "ÿ","y");

		// Mayúsculas con diéresis
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "Ä","A");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "Ë","E");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "Ï","I");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "Ö","O");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "Ü","U");
		
		
		// Mayúsculas con acento ^
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "Â","A");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "Ê","E");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "Î","I");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "Ô","O");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "Û","U");
		
		// Minúsculas con acento ^
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "â","a");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "ê","e");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "î","i");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "ô","o");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "û","u");
		
		// Mayusculas con tilde
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "Ã","A");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "Õ","O");
				
		// Minusculas con tilde
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "ã","a");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "õ","o");
		
		// Otros
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "Å","A");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "å","a");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "ð","o");
		ls_textoNormalizado = FuncionesCadena.replace(ls_textoNormalizado, "õ","o");
		
		return ls_textoNormalizado;	

	}  		
	
	/**
	   * Método usada para reemplazar todas las ocurrencias de determinada cadena de texto
	   * por otra cadena de texto
	   * @param String Texto origen
	   * @param String Fragmento de texto a reemplazar
	   * @param String  Fragmento de texto con el que se reemplaza
	   * @return String Cadena de texto con el reemplazo de cadenas completado
	   **/
	  public static String replace(String s, String one, String another)
	  throws Throwable 
	  {
	  // En el String 's' sustituye 'one' por 'another'
	    if (s == null) 
	    {
	    	if (one == null && another != null)
	    	{
	    		return another; 
	    	}
	    	return null;
	    }
	     
	     
	   	
		if (s.length() == 0) 
		{
			if (one != null && one.length() == 0)
			{
				return another; 
			}
			return "";
		} 
		
		if (one == null || one.length()==0)
		{
			return s;
		}

		
		String res = "";
	    int i = s.indexOf(one,0);
	    int lastpos = 0;
	    while (i != -1) {
	      res += s.substring(lastpos,i) + another;
	      lastpos = i + one.length();
	      i = s.indexOf(one,lastpos);
	    }
	    res += s.substring(lastpos);  // the rest
	    return res;
	  }

	  /**
	   * Método usada para reemplazar la última ocurrencia de determinada cadena de texto
	   * por otra cadena de texto
	   * @param String Texto origen
	   * @param String Fragmento de texto a reemplazar
	   * @param String Fragmento de texto con el que se reemplaza
	   * @return String Cadena de texto con el reemplazo de cadenas completado
	   **/
	  public static String replaceLast(String s, String one, String another) {
	  // En el String 's' sustituye 'one' por 'another'
	    if (s.equals("")) return "";
	    String res = "";
	    int i = s.lastIndexOf(one,0);
	    int lastpos = 0;
	    if (i != -1) {
	      res += s.substring(lastpos,i) + another;
	      lastpos = i + one.length();
	      i = s.indexOf(one,lastpos);
	    }
	    res += s.substring(lastpos);  // the rest
	    return res;
	  }
	  
	  /** 
	   * Normaliza texto para introducirlo en javascript.
	   * Se supondrá que el texto irá entrecomillado por comillas simples cuando
	   * se inserte en el javascript
	   * @param as_texto Texto a introducir
	   * @return String normalizado para javascript
	   */
	  public static String normalizaJavascript(String as_texto) throws Throwable{	  	   
	  	   String ls_valorCampo = as_texto;
	  	   
	  	   // Normalizar \
		   ls_valorCampo = FuncionesCadena.replace(ls_valorCampo, "\\","\\\\");
	  	   // Normalizar comillas simples
	  	   ls_valorCampo = FuncionesCadena.replace(ls_valorCampo,"'","\\'");	  	   
		   // Normalizar \n	
		   ls_valorCampo = FuncionesCadena.replace(ls_valorCampo, "\r", "");
		   // Normalizar \n
		   ls_valorCampo = FuncionesCadena.replace(ls_valorCampo, "\n", "\\n");
		   
		   return ls_valorCampo;
	  	
	  }
	  
	  
	  
	  
	  /**
	   * Realiza split de una cadena por un separador
	   * @param as_cadena Cadena
	   * @param as_separador Separador
	   * @return Array de Strings
	   * @throws Throwable
	   */
	  public static String[] split(String as_cadena, String as_separador)
		throws Throwable
		{
			if (as_cadena == null) return null;
			Vector lv_splitted = new Vector();
			int li_beginIndex = 0;
			int li_endIndex = 0;
			int li_auxIndex = 0; 
			String ls_aux = null;
			boolean lb_inicial = true;
			while (li_beginIndex > -1)
			{
				if (!lb_inicial)
				{
					li_auxIndex = li_beginIndex + as_separador.length();
				}
				lb_inicial = false;
				if (li_auxIndex > as_cadena.length() - 1)
				{
					break;
				}
				li_endIndex = as_cadena.indexOf(as_separador, li_auxIndex);
				if (li_endIndex == -1)
				{
					ls_aux = as_cadena.substring(li_auxIndex);
					lv_splitted.addElement(ls_aux);
					break;
				} 
				ls_aux = as_cadena.substring(li_auxIndex, li_endIndex);
				lv_splitted.addElement(ls_aux);
				li_beginIndex = li_endIndex;
				
			}

			if (lv_splitted.size() > 0)
			{
				String[] ls_splitted = new String[lv_splitted.size()];
				for (int i=0; i<lv_splitted.size();i++)
				{
					ls_splitted[i] = (String) lv_splitted.elementAt(i);
				}
				return ls_splitted;
			}
			 
			return null;
		}
	  
	  /**
	   * Obtiene el Charset por defecto
	   * @return
	   */
	  public static String getCharset(){
	  	return "iso-8859-1";
	  }	  	
	  
	  /**
	   * Convierte en un vector de valores una cadena de parametros codificada como:
	   * 	
	   * 
	   * 	{{A1#,#A2}{B1#,#C1[-]C2} ... }   
	   *  
	   * @param as_cadena Cadena a descomponer
	   * @return Vector Vector compuesto por vectores de valores
	   */
	  public static Vector descomponerCadenaParametros(String as_cadena) {
		
		  try{
			  Vector l_parametros = new Vector();
				
			  // Obtener vector de campos
				  // Eliminamos espacios		
			  String ls_cadena = as_cadena.trim().substring(1,as_cadena.length()-1);
				  // Normalizar para que solo halla un elemento por el que trocear la cadena
			  ls_cadena = FuncionesCadena.replace(ls_cadena,"},","");	
			  ls_cadena = FuncionesCadena.replace(ls_cadena,"}","");
	  
				  // Partir la cadena de validaciones
			  String[] l_cadenaParam = FuncionesCadena.split(ls_cadena,"{");				  		 		    		  
			  int li_numeroElementos = l_cadenaParam.length;		 
			  		  				  
			  for (int li_indexElement=1;li_indexElement<li_numeroElementos;li_indexElement++){
				  Vector l_valores = new Vector();			  			  
				  String ls_nextElement = l_cadenaParam[li_indexElement];
				  if ( (ls_nextElement != null) && (ls_nextElement.trim().length() > 0) ){			  			  		
					  String[] l_campos = FuncionesCadena.split(ls_nextElement,"#,#");
					  for (int i=0;i < l_campos.length;i++){
					  	if (l_campos[i].indexOf("[-]") > 0) {				  	
					  	  String[] l_campos2 = FuncionesCadena.split(l_campos[i],"[-]");				  	
					  	  Vector lv_campos2 = new Vector();
					  	  for (int j=0;j < l_campos2.length;j++){				  	  		
					  	  	lv_campos2.add(l_campos2[j]);
					  	  }
					  	  l_valores.add(lv_campos2);
					  	}else{				  	
					  	  	l_valores.add(l_campos[i]);
					  	}
					  	
					  	  
					  }
				  }
				  l_parametros.add(l_valores);				 			  
			  }
			
			  return l_parametros;
		  } catch(Throwable t){
		  	t.printStackTrace();
			  return null;
		  }																
	  }	
	  
	  /**
		 * Convierte una cadena de texto plano a una cadena en formato BASE64
		 * @param String arg0 Cadena de texto plano
		 * @return String Cadena en formato BASE64
		 **/		
		public static String StringtoBase64 (String arg0)
		throws Throwable
		{
			return StringtoBase64(arg0, FuncionesCadena.getCharset());
		}
		
		/**
		 * Convierte una cadena de texto plano a una cadena en formato BASE64
		 * @param String arg0 Cadena de texto plano
		 * @return String Cadena en formato BASE64
		 **/		
		public static String StringtoBase64 (String arg0, String as_charset)
		throws Throwable
		{
			if (arg0 == null) return null;
			byte aux[] = arg0.getBytes(as_charset);
			return BytestoBase64(aux);
		
		}	
		/**
		 * Convierte un array de bytes a una cadena en formato BASE64
		 * @param byte[] arg0 Array de bytes a codificar
		 * @return String Cadena en formato BASE64
		 **/		
		public static String BytestoBase64 (byte[] arg0)
		throws Throwable
		{
			/*
			BASE64Encoder enco = new BASE64Encoder();
			String ls_res = enco.encode(arg0);
			*/
			String ls_res = new String(Base64.encodeBase64(arg0));
			return ls_res;
		}
		
		/**
		 * Convierte una cadena en formato BASE64 a una cadena de texto sin codificar
		 * @param String arg0 Cadena en formato BASE64
		 * @return String Cadena decodificada
		 **/	
		public static String Base64toString (String arg0)
		throws Throwable
		{
			return Base64toString(arg0, FuncionesCadena.getCharset());
		}
		
		/**
		 * Convierte una cadena en formato BASE64 a una cadena de texto sin codificar
		 * @param String arg0 Cadena en formato BASE64
		 * @return String Cadena decodificada
		 **/	
		public static String Base64toString (String arg0, String as_charset)
		throws Throwable	
		{
				if (arg0 == null) return null;
				
				/*
				BASE64Decoder l_dec = new BASE64Decoder();
				byte[] lb_dec = l_dec.decodeBuffer(arg0);
				*/
				byte[] lb_dec = Base64.decodeBase64(arg0.getBytes());
				
				return BytestoString(lb_dec, as_charset);
		}
		
		/**
		 * Convierte un array de bytes a cadena
		 * @param byte[] arg0 Array de bytes
		 * @return String Cadena equivalente al array de bytes pasado como parámetro
		 **/	
		public static String BytestoString (byte[] arg0, String as_charset)
		throws Throwable
		{

				return new String(arg0, as_charset);
		}
		
		 public static String lpad(String as_texto, int ai_longMinima, char ac_relleno)
		  {
		  	if (as_texto == null) return as_texto;
		  	if (as_texto.length() < ai_longMinima);
		  	int numRellenos = ai_longMinima - as_texto.length();
		  	for (int i=0; i< numRellenos;i++)
		  	{
		  		as_texto = ac_relleno +as_texto;
		  	}
		  	return as_texto;
		  }
		
		public static String getFecha(Timestamp a_datetime, String as_formato)
		throws Throwable
		{
			 	if (a_datetime == null) return "";
				Calendar l_fecha = Calendar.getInstance(new Locale("es","ES"));
				l_fecha.setTime(a_datetime);										
				String ls_dia = FuncionesCadena.lpad(Integer.toString(l_fecha.get(Calendar.DAY_OF_MONTH)), 2,'0');
				String ls_mes = FuncionesCadena.lpad(Integer.toString(1 + l_fecha.get(Calendar.MONTH)), 2,'0');
				String ls_anyo = FuncionesCadena.lpad(Integer.toString(l_fecha.get(Calendar.YEAR)), 4,'0');
	            String ls_anyo2 = ls_anyo.substring(2, 4);     					
				String ls_hora24 = FuncionesCadena.lpad(Integer.toString(l_fecha.get(Calendar.HOUR_OF_DAY)), 2,'0');
				String ls_minuto = FuncionesCadena.lpad(Integer.toString(l_fecha.get(Calendar.MINUTE)), 2,'0');
				String ls_segundo = FuncionesCadena.lpad(Integer.toString(l_fecha.get(Calendar.SECOND)), 2,'0');			
				String ls_hora = FuncionesCadena.lpad(Integer.toString(l_fecha.get(Calendar.HOUR)), 2,'0');
				
				//String ls_posMeridian = Integer.toString(l_fecha.get(Calendar.AM_PM));
				
				String ls_res = as_formato.toUpperCase(); 
				ls_res = replace(ls_res, "YYYY", ls_anyo);
	            ls_res = replace(ls_res, "YY", ls_anyo2);
				ls_res = replace(ls_res, "MM", ls_mes);
				ls_res = replace(ls_res, "DD", ls_dia);
				ls_res = replace(ls_res, "HH24", ls_hora24);
				ls_res = replace(ls_res, "HH", ls_hora);
				ls_res = replace(ls_res, "MI", ls_minuto);
				ls_res = replace(ls_res, "SS", ls_segundo);
				return ls_res;
		}
		
		/** 
		 * Recoge la cadena as_fecha con el formato de entrada as_formatoIN y la convierte en un 
		 * Timestamp
		 * @param String as_fecha Fecha de entrada
		 * @param String as_formatoIN Formato en el que viene la fecha		 
		 * @return String Cadena que contiene la fecha contenida en as_fecha con el formato as_formatoOUT
		 **/
		public static Timestamp getTimestamp(String as_fecha, String as_formatoIN)
		throws Exception
		{				
				if (as_fecha == null) return null;
				Calendar l_fecha = Calendar.getInstance(new Locale("es","ES"));
		
				if (as_formatoIN.equalsIgnoreCase("YYYYMMDDHH24MI"))
				{
					l_fecha.set(Calendar.DAY_OF_MONTH, Integer.parseInt(as_fecha.substring(6,8)));
					l_fecha.set(Calendar.MONTH, Integer.parseInt(as_fecha.substring(4,6)) - 1);
					l_fecha.set(Calendar.YEAR, Integer.parseInt(as_fecha.substring(0,4)));
					l_fecha.set(Calendar.HOUR_OF_DAY, Integer.parseInt(as_fecha.substring(8,10)));
					l_fecha.set(Calendar.MINUTE, Integer.parseInt(as_fecha.substring(10,12)));
					l_fecha.set(Calendar.SECOND,0);												
					l_fecha.set(Calendar.MILLISECOND,0);
				}else if (as_formatoIN.equalsIgnoreCase("YYYYMMDDHH24MISS"))
				{
					l_fecha.set(Calendar.DAY_OF_MONTH, Integer.parseInt(as_fecha.substring(6,8)));
					l_fecha.set(Calendar.MONTH, Integer.parseInt(as_fecha.substring(4,6)) - 1);
					l_fecha.set(Calendar.YEAR, Integer.parseInt(as_fecha.substring(0,4)));
					l_fecha.set(Calendar.HOUR_OF_DAY, Integer.parseInt(as_fecha.substring(8,10)));
					l_fecha.set(Calendar.MINUTE, Integer.parseInt(as_fecha.substring(10,12)));												
					l_fecha.set(Calendar.SECOND, Integer.parseInt(as_fecha.substring(12,14)));																
					l_fecha.set(Calendar.MILLISECOND,0);
				}else if (as_formatoIN.equalsIgnoreCase("DD/MM/YYYY"))
				{
					l_fecha.set(Calendar.DAY_OF_MONTH, Integer.parseInt(as_fecha.substring(0,2)));
					l_fecha.set(Calendar.MONTH, Integer.parseInt(as_fecha.substring(3,5)) - 1);
					l_fecha.set(Calendar.YEAR, Integer.parseInt(as_fecha.substring(6,10)));															
					l_fecha.set(Calendar.HOUR_OF_DAY, 0);
					l_fecha.set(Calendar.MINUTE, 0);												
					l_fecha.set(Calendar.SECOND, 0);																
					l_fecha.set(Calendar.MILLISECOND,0);
				}else {
					// Formato no soportado
					return null;		
				}
											
				Timestamp l_fec = new Timestamp(l_fecha.getTimeInMillis());
				return l_fec;				
		}
		
		public static String getFechaActual(String as_formato)
		throws Throwable
		{
			  return FuncionesCadena.getFecha(new Timestamp(System.currentTimeMillis()), as_formato) +" ";
		}
		
		public static String getDescripcionMes(String as_mes){
			  try{	
			  	int li_mes;
			  	if (as_mes.startsWith("0")){
			  		li_mes = Integer.parseInt(as_mes.substring(1));
			  	}else{
			  		li_mes = Integer.parseInt(as_mes);
			  	}
				return getDescripcionMes(li_mes);
			  }catch(Throwable e){			 
				  return null;
			  }						
		 }
		
		public static String getDescripcionMes(int ai_mes){
			  try{			
				switch (ai_mes){
					case 1: return "Enero";					
					case 2: return "Febrero";
					case 3: return "Marzo";
					case 4: return "Abril";
					case 5: return "Mayo";
					case 6: return "Junio";
					case 7: return "Julio";
					case 8: return "Agosto";
					case 9: return "Septiembre";
					case 10: return "Octubre";
					case 11: return "Noviembre";
					case 12: return "Diciembre";
					default: return null;					
				}
			  }catch(Throwable e){			 
				  return null;
			  }						
		 }
		
		public static String formateaNumero(String as_num){
			if (as_num == null || as_num.equals("")) return "";
			return formateaNumero(Integer.parseInt(as_num));
		}
		
		
		public static String formateaNumero(int valor)
		{						
			return formateaNumero(new Double(valor));
		}
		
		public static String formateaNumero(Double valor)
		{						
			NumberFormat nf = NumberFormat.getInstance();
			return nf.format(valor);		 
		}
}
