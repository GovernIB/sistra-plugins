package es.caib.sistra.plugins.regtel.impl.caib;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.redose.modelInterfaz.ReferenciaRDS;
import es.caib.regtel.plugincaib.model.DatosRegistro;
import es.caib.regtel.plugincaib.model.DatosRegistroEntrada;
import es.caib.regtel.plugincaib.model.DatosRegistroSalida;
import es.caib.regtel.plugincaib.persistence.delegate.DelegateRegistroWebUtil;
import es.caib.sistra.plugins.regtel.PluginRegistroIntf;
import es.caib.sistra.plugins.regtel.ResultadoRegistro;
import es.caib.util.StringUtil;
import es.caib.xml.registro.factoria.ConstantesAsientoXML;
import es.caib.xml.registro.factoria.impl.AsientoRegistral;
import es.caib.xml.registro.factoria.impl.DatosInteresado;
import es.caib.xml.registro.factoria.impl.DireccionCodificada;
import es.caib.xml.registro.factoria.impl.Justificante;

/**
 * 	
 * 	Objeto CAIB para simular registro organismo
 *
 */
public class PluginRegtelCAIB implements PluginRegistroIntf{
	private static Log log = LogFactory.getLog(PluginRegtelCAIB.class);
	
	private static String CODIGO_PROVINCIA_CAIB = "7";
	private static String PAIS_ESPANYA = "ESPAÑA";
	public final static String CODIGO_PAIS_ESPANYA = "ESP";
	public final static String CODIGO_PAIS_ESPANYA_REDUCIDO = "ES";
	public final static String AUTORIZACION = "AE";
	
	public ResultadoRegistro registroEntrada(AsientoRegistral arg0, ReferenciaRDS arg1, Map arg2) throws Exception {
		DatosRegistroEntrada datos = new DatosRegistroEntrada();
		mapeaAsientoRegistralADatosRegistro( datos, arg0);
		return DelegateRegistroWebUtil.getRegistroWebDelegate().registroEntrada(datos);
	}

	public ResultadoRegistro registroSalida(AsientoRegistral arg0, ReferenciaRDS arg1, Map arg2) throws Exception {
		DatosRegistroSalida datos = new DatosRegistroSalida();
		mapeaAsientoRegistralADatosRegistro( datos, arg0);
		return DelegateRegistroWebUtil.getRegistroWebDelegate().registroSalida(datos);
	}

	public ResultadoRegistro confirmarPreregistro(String arg0, String arg1, String arg2, String arg3, Justificante arg4, ReferenciaRDS arg5, ReferenciaRDS arg6, Map arg7) throws Exception {
		DatosRegistroEntrada datos = new DatosRegistroEntrada();
		mapeaAsientoRegistralADatosRegistro( datos, arg4.getAsientoRegistral());
		datos.setOficina(arg0);
		if(arg1 != null && !"7".equals(arg1)){
			datos.setBalears(arg2);
			datos.setFora("");
		}else{
			datos.setBalears("");
			datos.setFora(arg3);
		}
		datos.setComentario("Confirmació preregistre "+arg4.getNumeroRegistro()+" - "+datos.getComentario());
		return DelegateRegistroWebUtil.getRegistroWebDelegate().registroEntrada(datos);
	}
	
	public List obtenerOficinasRegistro() {
		List lista = new ArrayList();
		try {
			lista = DelegateRegistroWebUtil.getRegistroWebDelegate().obtenerOficinas();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}
		
	public List obtenerOficinasRegistroUsuario(String arg0) {
		List lista = new ArrayList();
		try {
			lista = DelegateRegistroWebUtil.getRegistroWebDelegate().obtenerOficinas(arg0,AUTORIZACION);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}

	public List obtenerTiposAsunto() {
		List lista = new ArrayList();
		try {
			lista = DelegateRegistroWebUtil.getRegistroWebDelegate().obtenerDocumentos();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}

	public List obtenerServiciosDestino() {
		List lista = new ArrayList();
		try {
			lista = DelegateRegistroWebUtil.getRegistroWebDelegate().obtenerServiciosDestino();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}
	
	
	/**
	 * Esta funcion no es necesaria en la CAIB para asegurar la transaccionalidad, sino para 
	 * el uso por parte de envíos al BOIB para anular un registro telemático de entrada
	 */
	public void anularRegistroEntrada(String numeroRegistro, Date fechaRegistro) throws Exception {
		log.info("accediendo a anular registro entrada: " + numeroRegistro);
		String [] tokens = numeroRegistro.split("/");
		String usuari = null;
		String oficina = tokens[0];
		String numero = tokens[1];
		String anyo = tokens[2];
		DelegateRegistroWebUtil.getRegistroWebDelegate().anularRegistroEntrada(usuari,oficina,numero,anyo);
	}

	public void anularRegistroSalida(String numeroRegistro, Date fechaRegistro) throws Exception {
		// Esta funcion no es necesaria en la CAIB para asegurar la transaccionalidad
		log.info("accediendo a anular registro salida: " + numeroRegistro);
	}
	
	private void mapeaAsientoRegistralADatosRegistro( DatosRegistro datos, AsientoRegistral asientoRegistral ) throws Exception
	{
		
		try{
//			datos.setUsuario( AS400Usr );
	    	
			
			/*	 MODIFICACION 04/08: LA HORA NO LA DEBE PONER LA CAPA TELEMATICA
	    	// Se obtienen de la misma forma en que antes se hacían en ValoresBean
	    	String strFecha = getFecha();
	    	String strHora = getHorasMinutos();
	    	*/
			String strFecha = null;
			String strHora = null;
	    	
	    	DatosInteresado datosInteresado = this.obtenerRepresentate( asientoRegistral );
	    	// TODO : Comprobar de donde se obtiene esta contraseña
//	    	datos.setPassword( AS400Pwd );
	    	
	    	if ( datos instanceof DatosRegistroSalida )
	    	//if ( asientoRegistral.getDatosOrigen().getTipoRegistro().charValue() == ConstantesAsientoXML.TIPO_REGISTRO_SALIDA )
	    	{
	    		DatosRegistroSalida datosRegistroSalida = ( DatosRegistroSalida ) datos;
	    		datosRegistroSalida.setDataSalida( strFecha );
	    		datosRegistroSalida.setRemitent( asientoRegistral.getDatosAsunto().getCodigoOrganoDestino() );
	    	}
	    	else
	    	{
	    		DatosRegistroEntrada datosRegistroEntrada = ( DatosRegistroEntrada ) datos;
	    		datosRegistroEntrada.setDataentrada( strFecha );
	    		datosRegistroEntrada.setDestinatari( asientoRegistral.getDatosAsunto().getCodigoOrganoDestino() );
	    	}
	    	
	    	datos.setHora( strHora );
	    	datos.setData( strFecha );
	    	
	    	datos.setOficina( asientoRegistral.getDatosOrigen().getCodigoEntidadRegistralOrigen() );    	
	    	datos.setTipo ( asientoRegistral.getDatosAsunto().getTipoAsunto() );
	    	datos.setIdioma( StringUtil.getIdiomaRegistro( asientoRegistral.getDatosAsunto().getIdiomaAsunto() ) );
	    	if ( datosInteresado != null )
	    	{
	    		datos.setAltres( datosInteresado.getIdentificacionInteresado() );
	    		if(   datosInteresado.getDireccionCodificada() != null )
	    		{
	    			DireccionCodificada direccionCodificada = datosInteresado.getDireccionCodificada();
	    			String pais 		= (direccionCodificada.getPaisOrigen()!=null?direccionCodificada.getPaisOrigen():"");
	    			String provincia 	= direccionCodificada.getCodigoProvincia();
	    			String municipio 	= direccionCodificada.getCodigoMunicipio();
	    			
	    			String municipioBaleares = "";
	    			String municipioFuera = "";
	    			
	    			if ( CODIGO_PROVINCIA_CAIB.equals( provincia ) )
	    			{
	    				municipioBaleares = municipio;
	    			}
	    			else
	    			{
	    				// Si pertenece a España mostramos el municipio (provincia)    				
	    				if (	PAIS_ESPANYA.equals( pais.toUpperCase() ) 	|| 
	    						CODIGO_PAIS_ESPANYA.equals( pais.toUpperCase()) ||
	    						CODIGO_PAIS_ESPANYA_REDUCIDO.equals(pais.toUpperCase())
	    					)
	        	    			municipioFuera = direccionCodificada.getNombreMunicipio() + "(" + direccionCodificada.getNombreProvincia()+ ")"; 
	    				else
	    						municipioFuera = direccionCodificada.getPaisOrigen();     						
	    			}
	    			datos.setBalears( municipioBaleares );
	    			datos.setFora( municipioFuera );
	    		}
	    	}
	    	datos.setIdioex( datos.getIdioma() );
	    	datos.setComentario( asientoRegistral.getDatosAsunto().getExtractoAsunto() );	 
		}catch(Exception ex){
			throw new Exception("Excepcion mapeando datos de AsientoRegistral a DatosRegistro");
		}
		
	}
	 private DatosInteresado obtenerRepresentate( AsientoRegistral asiento )
	 {
		DatosInteresado datosInteresado = null;
		for ( Iterator it = asiento.getDatosInteresado().iterator(); it.hasNext(); )
		{
			datosInteresado = ( DatosInteresado ) it.next();
			if ( ConstantesAsientoXML.DATOSINTERESADO_TIPO_REPRESENTANTE.equals( datosInteresado.getTipoInteresado() ) )
			{
				return datosInteresado;
			}
		}
		return datosInteresado;
	 }

}
