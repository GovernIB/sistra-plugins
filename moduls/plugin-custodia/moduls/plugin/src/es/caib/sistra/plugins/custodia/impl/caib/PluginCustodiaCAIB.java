package es.caib.sistra.plugins.custodia.impl.caib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.redose.modelInterfaz.DocumentoRDS;
import es.caib.signatura.api.Signature;
import es.caib.signatura.cliente.ClienteCustodia;
import es.caib.sistra.plugins.custodia.PluginCustodiaIntf;
import es.caib.sistra.plugins.custodia.util.Configuracion;
import es.caib.sistra.plugins.firma.FirmaIntf;
import es.caib.sistra.plugins.firma.impl.caib.FirmaCAIB;
import es.caib.sistra.plugins.firma.impl.caib.UtilFirmaCAIB;
import es.caib.xml.registro.factoria.ConstantesAsientoXML;
import es.caib.xml.registro.factoria.FactoriaObjetosXMLRegistro;
import es.caib.xml.registro.factoria.ServicioRegistroXML;
import es.caib.xml.registro.factoria.impl.AsientoRegistral;
import es.caib.xml.registro.factoria.impl.Justificante;

public class PluginCustodiaCAIB implements PluginCustodiaIntf{
	
	private ClienteCustodia clienteCustodia;
	private static Log log = LogFactory.getLog(PluginCustodiaCAIB.class);
	
	public String custodiarDocumento(DocumentoRDS documento) throws Exception {
		try {
			log.debug("Entramos en custodiarDocumento");
			String codigo = "";
			String tipoDocumento = buscarTipoDocumento(documento);
			Configuracion conf = Configuracion.getInstance();
			clienteCustodia = new ClienteCustodia(conf.getProperty("ClienteCustodia"));
			byte[] xmlResponse = null;
			//obtenemos las firmas del documentos
			FirmaIntf[] firmas = documento.getFirmas();
			if(firmas != null){
				log.debug("Hay "+firmas.length+" firmas para este documento.");
				
				// Empaquetamos documento y firmas en SMIME
				UtilFirmaCAIB firmaCaib = new UtilFirmaCAIB();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				Signature[] signs = new Signature[firmas.length];
				for(int i=0;i<firmas.length;i++){
					signs[i] = ((FirmaCAIB)firmas[i]).getSignature();
				}
				firmaCaib.iniciarDispositivo();				
				firmaCaib.generarSMIME(new ByteArrayInputStream(documento.getDatosFichero()),signs,baos);
				
				//Generamos idreserva para custodiar y reservamos documento en custodia
				String idReserva = generarCodigoCustodia(documento.getReferenciaRDS().getCodigo());
				log.debug("Reservamos documento con idReserva: " + idReserva);
				xmlResponse = clienteCustodia.reservarDocumento_v2(idReserva);
				
				// Interpretamos respuesta reservar documento
				ReservarDocumentoResponseParser parserRDR = new ReservarDocumentoResponseParser(xmlResponse);
				if (!ReservarDocumentoResponseParser.SUCCESS.equals(parserRDR.getResultado())){
					log.debug("Respuesta custodia para reservar documento no es SUCCES: " + generaMensajeError(parserRDR.getResultado(),parserRDR.getException(),parserRDR.getStackTrace()));							
					throw new Exception("Respuesta custodia para reservar documento no es SUCCESS: " + generaMensajeError(parserRDR.getResultado(),parserRDR.getException(),parserRDR.getStackTrace()));							
				}
				log.debug("Reservado documento. Codigo externo: " + parserRDR.getCodigoExterno());
				
				// El codigo de acceso al documento sera el hash
				codigo = parserRDR.getHash();	
				
				// Invocamos a custodia para custodiar el SMIME
				log.debug("Custodiamos documento");
				xmlResponse = clienteCustodia.custodiarSMIME(new ByteArrayInputStream(baos.toByteArray()),
							documento.getNombreFichero(), idReserva, tipoDocumento);
				
				//  Interpretamos respuesta reservar documento
				CustodiarDocumentoResponseParser parserCDR = new CustodiarDocumentoResponseParser(xmlResponse);
				if (!CustodiarDocumentoResponseParser.SUCCESS.equals(parserCDR.getResultado())){
					log.debug("Respuesta custodia para custodiar documento no es SUCCES: " + generaMensajeError(parserCDR.getResultado(),parserCDR.getException(),parserCDR.getStackTrace()));
					throw new Exception("Respuesta custodia para custodiar documento no es SUCCESS: " + generaMensajeError(parserCDR.getResultado(),parserCDR.getException(),parserCDR.getStackTrace()));
				}
				log.debug("El documento se ha custodiado correctamente");
				
			}else{
				log.debug("No hay firmas para este documento.");
				throw new Exception("No hay firmas para este documento.");
			}
			log.debug("Salimos de custodiarDocumento");
			return codigo;	
		} catch (Exception e) {
			log.error("Error al custodiar el documento.", e);
			throw e;
		}
	}

	public void eliminarDocumento(String codigo) throws Exception {
		try {
			log.debug("Entramos en eliminarDocumento");
			Configuracion conf = Configuracion.getInstance();
			clienteCustodia = new ClienteCustodia(conf.getProperty("ClienteCustodia"));
			
			// Primero hay que recuperar el id externo a partir del hash
			byte[] xmlResponse = clienteCustodia.consultarReservaDocumento(codigo);
			
			// Interpretamos respuesta reservar documento
			ReservarDocumentoResponseParser parserRDR = new ReservarDocumentoResponseParser(xmlResponse);
			if (!ReservarDocumentoResponseParser.SUCCESS.equals(parserRDR.getResultado())){
				log.debug("Respuesta custodia para reservar documento no es SUCCES: " + generaMensajeError(parserRDR.getResultado(),parserRDR.getException(),parserRDR.getStackTrace()));
				throw new Exception("Respuesta custodia para reservar documento no es SUCCESS: " + generaMensajeError(parserRDR.getResultado(),parserRDR.getException(),parserRDR.getStackTrace()));
			}
			log.debug("Reservado documento. Codigo externo: " + parserRDR.getCodigoExterno());
			String idExterno = parserRDR.getCodigoExterno();
			
			// Despues hay que borrar a partir del id externo
			log.debug("Llamamos a eliminar de custodia");
			xmlResponse = clienteCustodia.eliminar(idExterno);

			// Interpretamos respuesta eliminar documento
			EliminarDocumentoResponseParser parserEDR = new EliminarDocumentoResponseParser(xmlResponse);
			if (!EliminarDocumentoResponseParser.SUCCESS.equals(parserEDR.getResultado())){
				log.debug("Respuesta custodia para eliminar documento no es SUCCES: " + generaMensajeError(parserEDR.getResultado(),parserEDR.getException(),parserEDR.getStackTrace()));
				// Si la excepcion es por que el documento no se encuentra pq ya ha sido borrado lo damos por bueno
				if (EliminarDocumentoResponseParser.EXCEPCION_DOCUMENTO_NO_ENCONTRADO.equals(parserEDR.getException())){
					log.debug("Documento ya ha sido borrado con anterioridad, lo damos por borrado");
				}else{
					throw new Exception("Respuesta custodia para eliminar documento no es SUCCESS: " + generaMensajeError(parserEDR.getResultado(),parserEDR.getException(),parserEDR.getStackTrace()));
				}
			}
			
			log.debug("El documento se ha eliminado de custodia correctamente.");
			
		} catch (Exception e) {
			log.error("Error al custodiar el documento.", e);
			
			// En caso de que sea error debido a que el doc no existe no debe generar error 
			if(!e.getMessage().toUpperCase().contains("DOCUMENTO_NO_ENCONTRADO")){
				throw e;
			}
		}
	}
	
	private static String generarCodigoCustodia(long codigo) throws Exception{
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmssS");
			String versio = sdf.format(new Date());
			String codigoCustodia = codigo+versio;			
			return codigoCustodia;
		}catch(Exception e){
			log.error("Error en generarCodigoCustodia", e);
			throw e;
		}
	}

	private String buscarTipoDocumento(DocumentoRDS documento) throws Exception{
		try{
			Configuracion conf = Configuracion.getInstance();
			String tipoAsiento = "";
			log.debug("entrando a buscarTipoDocumento");
			if("GE0002ASIENTO".equals(documento.getModelo())){
				//si es un asiento parseamos el documento y vamos a buscar el tipo
				FactoriaObjetosXMLRegistro factoria = ServicioRegistroXML.crearFactoriaObjetosXML();
				AsientoRegistral asiento = factoria.crearAsientoRegistral(new ByteArrayInputStream(documento.getDatosFichero()));
				tipoAsiento = asiento.getDatosOrigen().getTipoRegistro()+"";
				if(tipoAsiento.equals(ConstantesAsientoXML.TIPO_REGISTRO_ENTRADA+"") || tipoAsiento.equals(ConstantesAsientoXML.TIPO_ENVIO+"") || tipoAsiento.equals(ConstantesAsientoXML.TIPO_PREREGISTRO+"") || tipoAsiento.equals(ConstantesAsientoXML.TIPO_PREENVIO+"")){
					return conf.getProperty("GE0002ASIENTO_ENTRADA");
				}else if(tipoAsiento.equals(ConstantesAsientoXML.TIPO_ACUSE_RECIBO+"")){
					return conf.getProperty("GE0002ASIENTO_ACUSE");
				}else{
					throw new Exception("Este tipo de documento "+documento.getModelo()+" no se puede custodiar.");
				}
			}else if("GE0001JUSTIF".equals(documento.getModelo())){
				//si es un justificante parseamos el documento y vamos a buscar el tipo
				FactoriaObjetosXMLRegistro factoria = ServicioRegistroXML.crearFactoriaObjetosXML();
				Justificante  justificanteXML = factoria.crearJustificanteRegistro( new ByteArrayInputStream( documento.getDatosFichero() ) );
				tipoAsiento = justificanteXML.getAsientoRegistral().getDatosOrigen().getTipoRegistro()+"";
				if(tipoAsiento.equals(ConstantesAsientoXML.TIPO_REGISTRO_ENTRADA+"") || tipoAsiento.equals(ConstantesAsientoXML.TIPO_ENVIO+"") || tipoAsiento.equals(ConstantesAsientoXML.TIPO_PREREGISTRO+"") || tipoAsiento.equals(ConstantesAsientoXML.TIPO_PREENVIO+"")){
					return conf.getProperty("GE0001JUSTIF_ENTRADA");
				}else{
					throw new Exception("Este tipo de documento "+documento.getModelo()+" no se puede custodiar.");
				}
			}else {
				//si es un documento añadido desde el modulo de gestion de expedientes de bantel
				String tipo = conf.getProperty(documento.getModelo()); 
				if(tipo != null){
					return tipo;
				}else{
					throw new Exception("Error el tipo del documento "+ documento.getModelo() +" no ha sido encontrado.");
				}
			}
		}catch(Exception e){
			log.error("Error en buscarTipoDocumento", e);
			throw e;
		}
	}
	
	private String generaMensajeError(String resultado, String excepcion, String stackTrace){
		String mens = 
			"\nResultado: " + resultado + 
			(excepcion!=null?"\nExcepcion: " + excepcion:"")  +
			(stackTrace!=null?"\nStackTrace: " + stackTrace:"") ;
		return mens;
	}
	
}
