package es.caib.sistra.plugins.regtel.impl.caib;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.redose.modelInterfaz.DocumentoRDS;
import es.caib.redose.modelInterfaz.ReferenciaRDS;
import es.caib.redose.persistence.delegate.DelegateRDSUtil;
import es.caib.redose.persistence.delegate.RdsDelegate;
import es.caib.regweb3.ws.api.v3.AnexoWs;
import es.caib.regweb3.ws.api.v3.DatosInteresadoWs;
import es.caib.regweb3.ws.api.v3.IdentificadorWs;
import es.caib.regweb3.ws.api.v3.InteresadoWs;
import es.caib.regweb3.ws.api.v3.LibroOficinaWs;
import es.caib.regweb3.ws.api.v3.RegWebInfoWs;
import es.caib.regweb3.ws.api.v3.RegWebRegistroEntradaWs;
import es.caib.regweb3.ws.api.v3.RegWebRegistroSalidaWs;
import es.caib.regweb3.ws.api.v3.RegistroEntradaWs;
import es.caib.regweb3.ws.api.v3.RegistroSalidaWs;
import es.caib.regweb3.ws.api.v3.RegistroWs;
import es.caib.regweb3.ws.api.v3.TipoAsuntoWs;
import es.caib.sistra.plugins.firma.FirmaIntf;
import es.caib.sistra.plugins.regtel.ConstantesPluginRegistro;
import es.caib.sistra.plugins.regtel.OficinaRegistro;
import es.caib.sistra.plugins.regtel.PluginRegistroIntf;
import es.caib.sistra.plugins.regtel.ResultadoRegistro;
import es.caib.sistra.plugins.regtel.ServicioDestinatario;
import es.caib.sistra.plugins.regtel.TipoAsunto;
import es.caib.xml.registro.factoria.ConstantesAsientoXML;
import es.caib.xml.registro.factoria.impl.AsientoRegistral;
import es.caib.xml.registro.factoria.impl.DatosAnexoDocumentacion;
import es.caib.xml.registro.factoria.impl.DatosInteresado;
import es.caib.xml.registro.factoria.impl.Justificante;

/**
 * Implementacio del plugin de registre que empra la interfi­cie
 * d'EJBs logic del registre de la CAIB.
 * 
 */
public class PluginRegweb3 implements PluginRegistroIntf {

	private static final Log logger = LogFactory.getLog(PluginRegweb3.class);
	
	/** {@inheritDoc} */   	
	public ResultadoRegistro registroEntrada(
			AsientoRegistral asiento,
			ReferenciaRDS refAsiento,
			Map refAnexos) throws Exception {
		
		// Mapea parametros ws
		RegistroEntradaWs paramEntrada = (RegistroEntradaWs) mapearParametrosRegistro(asiento, refAsiento, refAnexos);
		
		// Invoca a Regweb3
		RegWebRegistroEntradaWs service = UtilsRegweb3.getRegistroEntradaService();
		IdentificadorWs result = service.altaRegistroEntrada(paramEntrada);
		
		// Devuelve resultado registro
		ResultadoRegistro resReg = new ResultadoRegistro();
		resReg.setFechaRegistro(result.getFecha());
		resReg.setNumeroRegistro(result.getNumeroRegistroFormateado());
		return resReg;			
	}
	
	/** {@inheritDoc} */   
	public ResultadoRegistro registroSalida(
			AsientoRegistral asiento,
			ReferenciaRDS refAsiento,
			Map refAnexos) throws Exception {
		
		// Mapea parametros ws		
		RegistroSalidaWs paramEntrada = (RegistroSalidaWs) mapearParametrosRegistro(asiento, refAsiento, refAnexos);
		
		// Invoca a Regweb3
		RegWebRegistroSalidaWs service = UtilsRegweb3.getRegistroSalidaService();
		IdentificadorWs result = service.altaRegistroSalida(paramEntrada);
		
		// Devuelve resultado registro
		ResultadoRegistro resReg = new ResultadoRegistro();
		resReg.setFechaRegistro(result.getFecha());
		resReg.setNumeroRegistro(result.getNumeroRegistroFormateado());
		return resReg;		
	}

	/** {@inheritDoc} */   
	public ResultadoRegistro confirmarPreregistro(
			String usuario,
			String oficina,
			String codigoProvincia,
			String codigoMunicipio,
			String descripcionMunicipio,
			Justificante justificantePreregistro,
			ReferenciaRDS refJustificante,
			ReferenciaRDS refAsiento,
			Map refAnexos) throws Exception {
		
		// Obtenemos asiento
		AsientoRegistral asientoRegistral = justificantePreregistro.getAsientoRegistral();

		// Establecemos oficina origen
		asientoRegistral.getDatosOrigen().setCodigoEntidadRegistralOrigen(oficina);
		
		// Mapea parametros ws
		RegistroEntradaWs paramEntrada = (RegistroEntradaWs) mapearParametrosRegistro(asientoRegistral, refAsiento, refAnexos);
		
		// Establecemos como usuario que realiza el registro al usuario conectado
		paramEntrada.setCodigoUsuario(usuario);
		
		// Invoca a Regweb3
		RegWebRegistroEntradaWs service = UtilsRegweb3.getRegistroEntradaService();
		IdentificadorWs result = service.altaRegistroEntrada(paramEntrada);
		
		// Devuelve resultado registro
		ResultadoRegistro resReg = new ResultadoRegistro();
		resReg.setFechaRegistro(result.getFecha());
		resReg.setNumeroRegistro(result.getNumeroRegistroFormateado());
		return resReg;		
		
		
	}

	/** {@inheritDoc} */   
	public List obtenerOficinasRegistro(char tipoRegistro) {
		return obtenerOficinasRegistroUsuario(tipoRegistro, null);	
	}

	/** {@inheritDoc} */   
	public List obtenerOficinasRegistroUsuario(char tipoRegistro, String usuario) {
		List resultado = new ArrayList();
		try {
			RegWebInfoWs service = UtilsRegweb3.getRegistroInfoService();
			
			Long regType = null;
			if (tipoRegistro == ConstantesPluginRegistro.REGISTRO_ENTRADA) {
				regType = ConstantesRegweb3.REGISTRO_ENTRADA;
			} else if (tipoRegistro == ConstantesPluginRegistro.REGISTRO_SALIDA) {
				regType = ConstantesRegweb3.REGISTRO_SALIDA;
			} else {
				throw new Exception("Tipo registro no soportado: " + tipoRegistro);
			}
			
			
			List<LibroOficinaWs> resWs = null;
			
			if (usuario != null) {
				resWs = service.obtenerLibrosOficinaUsuario(UtilsRegweb3.getCodigoEntidad(), usuario, regType);
			} else {
				resWs = service.obtenerLibrosOficina(UtilsRegweb3.getCodigoEntidad(), regType);
			}
			
			for (LibroOficinaWs lo : resWs) {
				String codigoLibro = lo.getLibroWs().getCodigoLibro();
				String descLibro = lo.getLibroWs().getNombreCorto();
				String codigoOficina = lo.getOficinaWs().getCodigo();
				String descOficina = lo.getOficinaWs().getNombre();
				
				OficinaRegistro of = new OficinaRegistro();
				of.setCodigo(UtilsRegweb3.getOficinaAsiento(codigoLibro, codigoOficina));
				of.setDescripcion(descLibro + " - " + descOficina);
				resultado.add(of);
			}
						
		} catch (Exception ex) {
			logger.error("Error consultando oficinas registro: " + ex.getMessage(), ex);
			resultado = new ArrayList();
		}
		
		return resultado;	
	}

	/** {@inheritDoc} */   
	public List obtenerTiposAsunto() {
		List resultado = new ArrayList();
		try {
			RegWebInfoWs service = UtilsRegweb3.getRegistroInfoService();
			List<TipoAsuntoWs> tiposAsunto = service.listarTipoAsunto(UtilsRegweb3.getCodigoEntidad());
			for (TipoAsuntoWs asuntoR : tiposAsunto) {
				TipoAsunto asunto = new TipoAsunto();
				asunto.setCodigo(asuntoR.getCodigo());
				asunto.setDescripcion(asuntoR.getNombre());
				resultado.add(asunto);
			}			
		} catch (Exception ex) {
			logger.error("Error consultando tipos asunto: " + ex.getMessage(), ex);
			resultado = new ArrayList();
		}		
		return resultado;			
	}

	/** {@inheritDoc} */   
	public List obtenerServiciosDestino() {
		List resultado = null;
		try {
			List<UnidadTF> res = UtilsRegweb3.getDir3UnidadesService().obtenerArbolUnidadesDestinatarias( UtilsRegweb3.getCodigoEntidad());
			resultado = new ArrayList();
			for (UnidadTF u : res) {
				ServicioDestinatario sd = new ServicioDestinatario();
				sd.setCodigo(u.getCodigo());
				sd.setDescripcion(u.getDenominacion());
				if (StringUtils.isNotBlank(u.getCodUnidadSuperior())) {
					sd.setCodigoPadre(u.getCodUnidadSuperior());
				}
				resultado.add(sd);
			}			
		} catch (Exception ex) {
			logger.error("Error consultando servicios destino: " + ex.getMessage(), ex);
			resultado = new ArrayList();
		}		
		return resultado;						
	}
	
	/** {@inheritDoc} */   
    public void anularRegistroEntrada(String numeroRegistro, Date fechaRegistro) throws Exception {
    	String user = ConfiguracionRegweb3.getInstance().getProperty("regweb3.usuario");
    	UtilsRegweb3.getRegistroEntradaService().anularRegistroEntrada(numeroRegistro, user, UtilsRegweb3.getCodigoEntidad(), true);    	
	}

    /** {@inheritDoc} */   
	public void anularRegistroSalida(String numeroRegistro, Date fechaRegistro) throws Exception {
		String user = ConfiguracionRegweb3.getInstance().getProperty("regweb3.usuario");
    	UtilsRegweb3.getRegistroSalidaService().anularRegistroSalida(numeroRegistro, user, UtilsRegweb3.getCodigoEntidad(), true);
	}

	/** {@inheritDoc} */   
	public String obtenerDescripcionSelloOficina(char tipoRegistro, String codigoOficinaAsiento) {
		String resultado = "";
		try {
			RegWebInfoWs service = UtilsRegweb3.getRegistroInfoService();
			
			Long regType = null;
			if (tipoRegistro == ConstantesPluginRegistro.REGISTRO_ENTRADA) {
				regType = ConstantesRegweb3.REGISTRO_ENTRADA;
			} else if (tipoRegistro == ConstantesPluginRegistro.REGISTRO_SALIDA) {
				regType = ConstantesRegweb3.REGISTRO_SALIDA;
			} else {
				throw new Exception("Tipo registro no soportado: " + tipoRegistro);
			}
			
			
			List<LibroOficinaWs> resWs = service.obtenerLibrosOficina(UtilsRegweb3.getCodigoEntidad(), regType);
			
			boolean enc = false;
			
			for (LibroOficinaWs lo : resWs) {
				String codigoLibro = lo.getLibroWs().getCodigoLibro();				
				String codigoOficina = lo.getOficinaWs().getCodigo();
				
				if (codigoOficinaAsiento.equals(UtilsRegweb3.getOficinaAsiento(codigoLibro,codigoOficina))) {
					enc = true;
					resultado = lo.getLibroWs().getNombreCorto();
					break;
				}				
			}	
			
			if (!enc) {
				logger.error("Error consultando oficina registro: no se encuentra oficina " + codigoOficinaAsiento);
			}
						
		} catch (Exception ex) {
			logger.error("Error consultando oficinas registro: " + ex.getMessage(), ex);
			resultado = "";
		}
		
		return resultado;
	}
	
	// ----------- Funciones auxiliares
	
	/**
	 *  Mapea datos asiento a parametro ws.
	 * @param asiento asiento
	 * @param refAsiento referencia asiento
	 * @param refAnexos referencia anexos
	 * @return parametro ws
	 * @throws Exception
	 */
	private RegistroWs mapearParametrosRegistro(
			AsientoRegistral asiento, ReferenciaRDS refAsiento, Map refAnexos) throws Exception {
		
		// Crea parametros segun sea registro entrada o salida
		boolean esRegistroSalida = (asiento.getDatosOrigen().getTipoRegistro() == ConstantesAsientoXML.TIPO_REGISTRO_SALIDA);
		
		RegistroWs registroWs = null;
		if (esRegistroSalida) {
			registroWs = new RegistroSalidaWs();			
		} else {
			registroWs = new RegistroEntradaWs();
		}
		
		
		// Datos aplicacion
		registroWs.setAplicacion(UtilsRegweb3.getCodigoAplicacion());
        registroWs.setVersion(UtilsRegweb3.getVersionAplicacion());
        
        // Usuario que registra (por defecto SISTRA, excepto para confirmacion preregistro)
        registroWs.setCodigoUsuario(UtilsRegweb3.getUsuarioRegistroSistra());
		
		// Datos oficina registro
        String oficinaAsientoRegistral = asiento.getDatosOrigen().getCodigoEntidadRegistralOrigen();
		registroWs.setOficina(UtilsRegweb3.getOficina(oficinaAsientoRegistral));
		registroWs.setLibro(UtilsRegweb3.getLibro(oficinaAsientoRegistral));
		if (esRegistroSalida) {
			((RegistroSalidaWs) registroWs).setOrigen(asiento.getDatosAsunto().getCodigoOrganoDestino());
		} else {
			((RegistroEntradaWs) registroWs).setDestino(asiento.getDatosAsunto().getCodigoOrganoDestino());
		}

		// Datos asunto
        registroWs.setExtracto(asiento.getDatosAsunto().getExtractoAsunto());
        registroWs.setDocFisica(new Long(ConstantesRegweb3.DOC_FISICA_REQUERIDA));
        registroWs.setIdioma(asiento.getDatosAsunto().getIdiomaAsunto());
        registroWs.setTipoAsunto(asiento.getDatosAsunto().getTipoAsunto());        
        // TODO ¿CODIGO ASUNTO?
        //registroEntradaWs.setCodigoAsunto(null);

        // Datos interesado: representante / representado               
        DatosInteresado representanteAsiento = UtilsRegweb3.obtenerDatosInteresadoAsiento(asiento, "RPT");
        DatosInteresado representadoAsiento = UtilsRegweb3.obtenerDatosInteresadoAsiento(asiento, "RPD");
        
        DatosInteresadoWs interesado = null;
        DatosInteresadoWs representante = null;
        
        if (representadoAsiento != null) {
        	interesado =  UtilsRegweb3.crearInteresado(representadoAsiento);
        	representante = UtilsRegweb3.crearInteresado(representanteAsiento);
        } else {
        	interesado =  UtilsRegweb3.crearInteresado(representanteAsiento);
        }
               
        InteresadoWs interesadoWs = new InteresadoWs();
        interesadoWs.setInteresado(interesado); 
        interesadoWs.setRepresentante(representante);
        registroWs.getInteresados().add(interesadoWs);

        
        // Anexos
        if ("true".equals(ConfiguracionRegweb3.getInstance().getProperty("regweb3.insertarDocs"))) {
	        
        	boolean anexarInternos = "true".equals(ConfiguracionRegweb3.getInstance().getProperty("regweb3.insertarDocs.internos"));
        	boolean anexarFormateados = "true".equals(ConfiguracionRegweb3.getInstance().getProperty("regweb3.insertarDocs.formateados"));
        	
        	Integer origenDocumento;
	        String tipoDocumental;
	        if (esRegistroSalida) {
				origenDocumento  = ConstantesRegweb3.ORIGEN_DOCUMENTO_ADMINISTRACION;
				tipoDocumental = ConstantesRegweb3.TIPO_DOCUMENTAL_NOTIFICACION;			
			} else {
				origenDocumento  = ConstantesRegweb3.ORIGEN_DOCUMENTO_CIUDADANO;
				tipoDocumental = ConstantesRegweb3.TIPO_DOCUMENTAL_SOLICITUD;
			}
			
	        // - Asiento registral
	        // 		- Xml de asiento
			if (anexarInternos) {				
				AnexoWs anexoAsientoWs = generarAnexoWs(refAsiento, ConstantesRegweb3.TIPO_DOCUMENTO_FICHERO_TECNICO,
						tipoDocumental, origenDocumento, ConstantesRegweb3.VALIDEZ_DOCUMENTO_ORIGINAL);
		        registroWs.getAnexos().add(anexoAsientoWs);
			}
			if (anexarFormateados) {
				AnexoWs anexoAsientoFWs = generarAnexoWs(refAsiento, true, ConstantesRegweb3.TIPO_DOCUMENTO_FICHERO_TECNICO,
						tipoDocumental, origenDocumento, ConstantesRegweb3.VALIDEZ_DOCUMENTO_ORIGINAL);
		        registroWs.getAnexos().add(anexoAsientoFWs);
			}
	        
	        // - Ficheros asiento
	        for (Iterator it = asiento.getDatosAnexoDocumentacion().iterator();it.hasNext();) {
	        	AnexoWs anexoWs = null;
	        	String tipoDocumento = ConstantesRegweb3.TIPO_DOCUMENTO_ANEXO;
	        	String validezDocumento = ConstantesRegweb3.VALIDEZ_DOCUMENTO_COPIA;
	        	
	        	DatosAnexoDocumentacion da = (DatosAnexoDocumentacion) it.next();
	        	ReferenciaRDS refRDS = (ReferenciaRDS) refAnexos.get(da.getIdentificadorDocumento());
	        	
	        	// Fichero tecnico: datos propios, aviso notificacion, formularios, pagos
	        	if (da.getTipoDocumento().equals(ConstantesAsientoXML.DATOSANEXO_DATOS_PROPIOS) ||
	        		da.getTipoDocumento().equals(ConstantesAsientoXML.DATOSANEXO_AVISO_NOTIFICACION) || 
	        		da.getTipoDocumento().equals(ConstantesAsientoXML.DATOSANEXO_OFICIO_REMISION) || 
	        		da.getTipoDocumento().equals(ConstantesAsientoXML.DATOSANEXO_FORMULARIO) || 
	        		da.getTipoDocumento().equals(ConstantesAsientoXML.DATOSANEXO_PAGO)) {
	        			tipoDocumento = ConstantesRegweb3.TIPO_DOCUMENTO_FICHERO_TECNICO;
	        			validezDocumento = ConstantesRegweb3.VALIDEZ_DOCUMENTO_ORIGINAL;
	        	} 
	        	
	        	// Generamos anexo ws y añadimos a lista
	        	if (anexarInternos || tipoDocumento != ConstantesRegweb3.TIPO_DOCUMENTO_FICHERO_TECNICO) {
	        		anexoWs = generarAnexoWs(refRDS, tipoDocumento, tipoDocumental, origenDocumento, validezDocumento);
	        		registroWs.getAnexos().add(anexoWs);
	        	}
	        	
	        	// Para formularios y pagos vemos si se adjunta como formateados
	        	if (anexarFormateados && 
	        			(da.getTipoDocumento().equals(ConstantesAsientoXML.DATOSANEXO_FORMULARIO) || da.getTipoDocumento().equals(ConstantesAsientoXML.DATOSANEXO_PAGO) ) ) {	        	
	        		tipoDocumento = ConstantesRegweb3.TIPO_DOCUMENTO_FORMULARIO;
	        		validezDocumento = ConstantesRegweb3.VALIDEZ_DOCUMENTO_ORIGINAL;
	        		anexoWs = generarAnexoWs(refRDS, true, tipoDocumento, tipoDocumental, origenDocumento, validezDocumento);
	        		registroWs.getAnexos().add(anexoWs);
	        	}
	        	
	        }
        }
        
        /* NO USADOS        
        registroEntradaWs.setContactoUsuario("earrivi@gmail.com");
        registroEntradaWs.setNumExpediente("");
        registroEntradaWs.setNumTransporte("");
        registroEntradaWs.setObservaciones("");
        registroEntradaWs.setRefExterna("");
        registroEntradaWs.setTipoTransporte("");
        registroEntradaWs.setExpone("");
        registroEntradaWs.setSolicita("");
        */

        return registroWs;
        
	}

	
	
	/**
	 * Genera AnexoWS en funcion documento REDOSE
	 * @param refRDS
	 * @param tipoDocumento
	 * @param tipoDocumental
	 * @param origenDocumento
	 * @return
	 */
	private AnexoWs generarAnexoWs(ReferenciaRDS refRDS, boolean formatearDocumento, String tipoDocumento, String tipoDocumental,
			Integer origenDocumento, String validezDocumento) throws Exception {
		RdsDelegate rdsDelegate = DelegateRDSUtil.getRdsDelegate();
		DocumentoRDS docRDS = null;
		
		if (formatearDocumento) {
			docRDS = rdsDelegate.consultarDocumentoFormateadoRegistro(refRDS);
		} else {
			docRDS = rdsDelegate.consultarDocumento(refRDS);
		}
		
		AnexoWs anexoAsiento = new AnexoWs();
        anexoAsiento.setTitulo(docRDS.getTitulo());
        anexoAsiento.setNombreFicheroAnexado(docRDS.getNombreFichero());
        anexoAsiento.setFicheroAnexado(docRDS.getDatosFichero());        
        anexoAsiento.setTipoMIMEFicheroAnexado(MimeType.getMimeTypeForExtension(getExtension(docRDS.getNombreFichero())));
        anexoAsiento.setTipoDocumental(tipoDocumental);
        anexoAsiento.setTipoDocumento(tipoDocumento);     
        anexoAsiento.setOrigenCiudadanoAdmin(origenDocumento);
        anexoAsiento.setValidezDocumento(validezDocumento);
        
        // Solo se puede anexar 1 firma
        if (docRDS.getFirmas() != null && docRDS.getFirmas().length > 0) {
        	anexoAsiento.setModoFirma(ConstantesRegweb3.MODO_FIRMA_DETACHED);
        	FirmaIntf firma = docRDS.getFirmas()[0];
        	byte[] contentFirma = firma.getContenidoFirma();
        	anexoAsiento.setFirmaAnexada(contentFirma);        	
        	anexoAsiento.setNombreFirmaAnexada(UtilsRegweb3.obtenerNombreFirma(firma));
        	anexoAsiento.setTipoMIMEFirmaAnexada(MimeType.getMimeTypeForExtension(UtilsRegweb3.getExtension(anexoAsiento.getNombreFirmaAnexada())));  
        } else {
        	anexoAsiento.setModoFirma(ConstantesRegweb3.MODO_FIRMA_SIN_FIRMA);
        }
		return anexoAsiento;
	}

	/**
	 * Genera AnexoWS en funcion documento REDOSE
	 * @param refRDS
	 * @param tipoDocumento
	 * @param tipoDocumental
	 * @param origenDocumento
	 * @return
	 */
	private AnexoWs generarAnexoWs(ReferenciaRDS refRDS, String tipoDocumento, String tipoDocumental,
			Integer origenDocumento, String validezDocumento) throws Exception {
		return generarAnexoWs(refRDS, false, tipoDocumento, tipoDocumental, origenDocumento, validezDocumento);
	}
	
	/**
     * Obtiene extension fichero.
     */
	private String getExtension(String filename){
		if(filename.lastIndexOf(".") != -1){
			return filename.substring(filename.lastIndexOf(".") + 1);
		}else{
			return "";
		}
	}
	
	

}
