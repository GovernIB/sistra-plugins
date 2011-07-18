package es.caib.redose.persistence.formateadores;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import es.caib.redose.model.PlantillaIdioma;
import es.caib.redose.modelInterfaz.DocumentoRDS;
import es.caib.xml.ConstantesXML;
import es.caib.xml.analiza.Analizador;
import es.caib.xml.analiza.Nodo;
import es.caib.xml.util.HashMapIterable;
import es.indra.util.pdf.NumeroPaginaStamp;
import es.indra.util.pdf.ObjectStamp;
import es.indra.util.pdf.UtilPDF;

/**
 * Generador de PDFs para XMLs de Formularios 
 *
 */
public class FormateadorPdfInterinos extends FormateadorPdfFormularios{
	
	protected static final String CODIGO_LISTAS = "[CODIGO]";
	protected final static String INDICE_LISTAS = "indice";
	
	protected final static String separador = "\n";
	protected final static int numeroCaracteresLinea=80;
	protected final static int numeroLineasPagina=44;
	protected final static int numeroLineasUltimaPagina=34;
	
	protected final static String XPATH_DOCUMENTACION = "/FORMULARIO/DATOS_PERSONALES/DOCUMENTACION";
	
	/**
	 * Genera PDF a partir Xpath
	 */
	public DocumentoRDS formatearDocumento(DocumentoRDS documento, PlantillaIdioma plantilla,List usos) throws Exception {
		
		// - Extraemos datos
		Analizador analizador = new Analizador ();				
		ByteArrayInputStream bis = new ByteArrayInputStream(documento.getDatosFichero());
    	HashMapIterable datosFormulario = analizador.analizar(bis,ConstantesXML.ENCODING);
    	bis.close();
    	Nodo nodo = (Nodo) datosFormulario.get(XPATH_DOCUMENTACION);
    	String texto = nodo.getValor();
    	
    	// - Obtenemos limite de paginas
    	bis = new ByteArrayInputStream(plantilla.getArchivo().getDatos());
    	int numPagesPDF = UtilPDF.getNumberOfPages(bis);
    	bis.close();
    	int limitePaginas = numPagesPDF - 1;
    	
    	// - Dividimos en bloques    	
    	String asig = asignarPaginas(texto,separador,numeroCaracteresLinea,numeroLineasPagina,numeroLineasUltimaPagina,limitePaginas);
		int numPags =  obtenerNumeroPaginas(asig,separador);
		int pages[]=new int[numPags + 1]; // paginas utilizadas: 1 + n intermedias + ultima 
		pages[0] = 1;
		String xpathDesglose = this.referenciaCampo(XPATH_DOCUMENTACION);
		for (int i=1;i<=numPags;i++){			
			if (i<numPags){			
				pages[i] = i+1;
				this.addDato(xpathDesglose+i,getTextoPagina(texto,separador,asig,i));
			}else{
				pages[i] = numPagesPDF;
				this.addDato(xpathDesglose+limitePaginas,getTextoPagina(texto,separador,asig,i));
			}				
		}
				
		// - Realizamos formateo
		DocumentoRDS documentoF =  super.formatearDocumento( documento,plantilla,usos);
		
		// - Eliminamos paginas sobrantes
		ByteArrayOutputStream bos = new ByteArrayOutputStream(plantilla.getArchivo().getDatos().length + texto.length());
		bis = new ByteArrayInputStream(documentoF.getDatosFichero());
		UtilPDF.extractPages(bos,bis,pages);
		documentoF.setDatosFichero(bos.toByteArray());
		bis.close();
		bos.close();		
		
		// Stampamos numeros paginas
		ObjectStamp textos [] = new ObjectStamp[1];
		textos[0] = new NumeroPaginaStamp();
		textos[0].setPage(0);
		textos[0].setX(500);
		textos[0].setY(20);		
		textos[0].setRotation(0);
		bos = new ByteArrayOutputStream(documentoF.getDatosFichero().length);
		bis = new ByteArrayInputStream(documentoF.getDatosFichero()); 
		UtilPDF.stamp(bos,bis,textos);
		documentoF.setDatosFichero(bos.toByteArray());
		bis.close();
		bos.close();		
		
		// Devolvemos documento	
		return documentoF;
	}

	
	 /**
	  * Asigna a cada item la pagina a la que va (devuelve String con las lineas asociadas a cada item  separadas por el separador)
	  * @param texto
	  * @param numeroCaracteresLinea
	  * @param numeroLineasPagina
	  * @param numeroLineasUltimaPagina
	  * @return
	 * @throws Exception 
	  */
	public String asignarPaginas(String texto, String separador,int numeroCaracteresLinea, int numeroLineasPagina, int numeroLineasUltimaPagina, int ultimaPagina) throws Exception{
		 
		 if (texto == null || texto.length() == 0) return "";
		 
		 String[] cadenas = texto.split(separador);
		 int asignacionPag [] = new int[cadenas.length]; // pagina asociada a cada item
		 int numLineasPag []  = new int[cadenas.length + 1]; // numero de lineas asociadas a cada pagina 
		   
		 int countLineas = 0;
		 int numPag = 1;
		 int numLineasCadena;		 
		 
		 for (int i=0;i<cadenas.length;i++){
			  // Control cambio de pagina
			  numLineasCadena = calcularNumLineas(cadenas[i],numeroCaracteresLinea);
			  
			  // Control de que una cadena no sobrepase el maximo de lineas de una pagina
			  if (numLineasCadena > numeroLineasPagina) throw new Exception("Existe una cadena que no cabe en una pagina");			  
			  
			  if ( countLineas + numLineasCadena >  numeroLineasPagina ){
				  	// cambio de pagina
				    numLineasPag[numPag] = countLineas;
					numPag ++;
					countLineas = numLineasCadena;									  
			  }else{
				  countLineas += numLineasCadena;
			  }
					  			  			 
			  // Asignamos num pagina al item
			  asignacionPag[i] = numPag;			  
		  }
		 numLineasPag[numPag] = countLineas;
		 
		 
		  // Control ultima pagina si no hay limite de paginas
		 if (ultimaPagina ==0) {
			 if (numLineasPag[numPag] > numeroLineasUltimaPagina){		 
				 // Si la última pagina sobrepasa el tamaño maximo de la ultima pagina
				 // creamos una pagina nueva con otro elemento
				 asignacionPag[asignacionPag.length - 1] = asignacionPag[asignacionPag.length - 1] + 1;			 
			 }
		 }else{
			 // Si hay limite de paginas comprobamos si se ha llegado a la pagina limite			 
			 if (numPag >= ultimaPagina){
				 
				 // Eliminamos elementos que pasen de la pagina limite
				 for (int i=0;i<cadenas.length;i++) {
					 if (asignacionPag[i] > ultimaPagina) asignacionPag[i] = -1;
					 if (i>ultimaPagina) numLineasPag[i] = 0;
				 }
				 
				// Si la ultima pagina ha sobrepasado el tamaño vamos eliminando items
				if (numLineasPag[ultimaPagina] > numeroLineasUltimaPagina){
					 // Vamos eliminando items hasta que quepa
					 for (int i=cadenas.length - 1;i>0;i--){
						 if (asignacionPag[i]==ultimaPagina){
							 asignacionPag[i] = -1;
							 numLineasCadena = calcularNumLineas(cadenas[i],numeroCaracteresLinea);
							 numLineasPag[ultimaPagina] = numLineasPag[ultimaPagina] - numLineasCadena;
							 if (numLineasPag[ultimaPagina] <= numeroLineasUltimaPagina) break;							 
						 }
					 }
				 }				 
			 
			 }else{
				  
				 // Si la ultima pagina ha sobrepasado el tamaño creamos una nueva pagina con el 
				 // ultimo item
				 if (numLineasPag[numPag] > numeroLineasUltimaPagina){
					 asignacionPag[asignacionPag.length - 1] = asignacionPag[asignacionPag.length - 1] + 1;	
					 numLineasCadena = calcularNumLineas(cadenas[asignacionPag.length - 1],numeroCaracteresLinea);
					 numLineasPag[numPag] = numLineasPag[numPag] - numLineasCadena;
					 numLineasPag[numPag+1] = numLineasCadena;
				 }
				 
			 }
			
			 
		 }
		 
		 // Devolvemos asignacion
		 return asignacionToString(asignacionPag,separador);
		 
	 }
	 
	 /**
	  * Obtiene total paginas a partir de la asignacion de paginas
	  * @param texto
	  * @param numeroCaracteresLinea
	  * @param numeroLineasPagina
	  * @param numeroLineasUltimaPagina
	  * @return
	  */
	public  int obtenerNumeroPaginas(String asignacionPaginas,String separador){
		  String numPags[]=asignacionPaginas.split(separador);		  
		  if (numPags.length <= 0) return 0;
		  
		  for (int i=numPags.length - 1;i>=0;i--){
			  if (Integer.parseInt(numPags[i])==-1) continue;
			  return Integer.parseInt(numPags[i]);
		  }		
		  
		  return 0;
	 }
	 
    /**
	 * Devuelve el texto de la pagina pasada como ultimo parametro. Cada item (separado por los caracteres
	 * ## supondrá una linea nueva '- ' 
	 */
	public  String getTextoPagina(String texto,String separador,String asignacionPaginas,int numeroPagina)
	  {
		  String numPags[]=asignacionPaginas.split(separador);
		  String[] cadenas = texto.split(separador);
		  StringBuffer result = new StringBuffer(40 * 70);
		  result.append("");
		  
		  for (int i=0;i<cadenas.length;i++){
			  int pagAct = Integer.parseInt(numPags[i]);			  
			  if (numeroPagina == pagAct){
				  result.append(cadenas[i] + "\r\n");
			  }
			  if (pagAct > numeroPagina){
				  break; // siguiente pagina			  
			  }
			  if (pagAct == -1){
				  result.append("                                                 ... ... ... ... ...");
				  break; // se ha realizado truncamiento			  
			  }			  
		  }
		  		  
		  return result.toString();
	  }
		  
	  
	public  String asignacionToString(int asig[],String separador){
		  String res = "";
		  for (int i=0;i<asig.length;i++){
			  if (i>0) res+= separador;
			  res+=asig[i];
		  }
		  return res;
	  }
	  
	public  int calcularNumLineas(String texto,int numeroCaracteresLinea){
		  final int margenSeguridad=15;
		  int cociente = texto.length() / numeroCaracteresLinea;
		  int resto    = texto.length() % numeroCaracteresLinea;
		  int numLineas = cociente + (resto>0?1:0);
		  if (resto > numeroCaracteresLinea - margenSeguridad ) numLineas++;
		  return numLineas;	 			  
	  }
		  

		
}
