package es.caib.sistra.plugins.regtel.impl.caib;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.redose.modelInterfaz.ReferenciaRDS;
import es.caib.regtel.plugincaib.persistence.delegate.DelegateException;
import es.caib.regtel.plugincaib.persistence.delegate.DelegateRegistroWebUtil;
import es.caib.sistra.plugins.regtel.ConstantesPluginRegistro;
import es.caib.sistra.plugins.regtel.PluginRegistroIntf;
import es.caib.sistra.plugins.regtel.ResultadoRegistro;
import es.caib.xml.registro.factoria.impl.AsientoRegistral;
import es.caib.xml.registro.factoria.impl.Justificante;

/**
 * Implementacio del plugin de registre que empra la interfi­cie
 * d'EJBs logic del registre de la CAIB.
 *
 */
public class PluginRegtelCAIB implements PluginRegistroIntf {

	private static final Log logger = LogFactory.getLog(PluginRegtelCAIB.class);


	public ResultadoRegistro registroEntrada(
			AsientoRegistral asiento,
			ReferenciaRDS refAsiento,
			Map refAnexos) throws Exception {
		return DelegateRegistroWebUtil.getRegistroWebDelegate().registroEntrada(asiento, refAsiento, refAnexos);
	}

	public ResultadoRegistro registroSalida(
			AsientoRegistral asiento,
			ReferenciaRDS refAsiento,
			Map refAnexos) throws Exception {
		return DelegateRegistroWebUtil.getRegistroWebDelegate().registroSalida(asiento, refAsiento, refAnexos);
	}

	public ResultadoRegistro confirmarPreregistro(
			String usuario,
			String entidad,
			String oficina,
			String codigoProvincia,
			String codigoMunicipio,
			String descripcionMunicipio,
			Justificante justificantePreregistro,
			ReferenciaRDS refJustificante,
			ReferenciaRDS refAsiento,
			Map refAnexos) throws Exception {
		return DelegateRegistroWebUtil.getRegistroWebDelegate().confirmarPreregistro(usuario, oficina, codigoProvincia, codigoMunicipio, descripcionMunicipio, justificantePreregistro, refJustificante, refAsiento, refAnexos);
	}

	public List obtenerOficinasRegistro(String entidad, char tipoRegistro) {
		try {
			return DelegateRegistroWebUtil.getRegistroWebDelegate().obtenerOficinasRegistro(tipoRegistro);
		} catch (DelegateException e) {
			logger.error("Error obtenerOficinasRegistro: " + e.getMessage(), e);
			return new ArrayList();
		}
	}

	public List obtenerOficinasRegistroUsuario(String entidad, char tipoRegistro, String usuario) {
		try {
			return DelegateRegistroWebUtil.getRegistroWebDelegate().obtenerOficinasRegistroUsuario(tipoRegistro, usuario);
		} catch (DelegateException e) {
			logger.error("Error obtenerOficinasRegistroUsuario: " + e.getMessage(), e);
			return new ArrayList();
		}
	}

	public List obtenerTiposAsunto(String entidad) {
		try {
			return DelegateRegistroWebUtil.getRegistroWebDelegate().obtenerTiposAsunto();
		} catch (DelegateException e) {
			logger.error("Error obtenerTiposAsunto: " + e.getMessage(), e);
			return new ArrayList();
		}
	}

	public List obtenerServiciosDestino(String entidad) {
		try {
			return DelegateRegistroWebUtil.getRegistroWebDelegate().obtenerServiciosDestino();
		} catch (DelegateException e) {
			logger.error("Error obtenerServiciosDestino: " + e.getMessage(), e);
			return new ArrayList();
		}
	}

    public void anularRegistroEntrada(String entidad, String numeroRegistro, Date fechaRegistro) throws Exception {
    	DelegateRegistroWebUtil.getRegistroWebDelegate().anularRegistroEntrada(numeroRegistro, fechaRegistro);
	}

	public void anularRegistroSalida(String entidad, String numeroRegistro, Date fechaRegistro) throws Exception {
		DelegateRegistroWebUtil.getRegistroWebDelegate().anularRegistroSalida(numeroRegistro, fechaRegistro);
	}

	public String obtenerDescripcionSelloOficina(char tipoRegistro, String entidad, String codigoOficina) {
		try {
			return DelegateRegistroWebUtil.getRegistroWebDelegate().obtenerDescripcionSelloOficina(tipoRegistro, codigoOficina);
		} catch (DelegateException e) {
			logger.error("Error obtenerDescripcionSelloOficina: " + e.getMessage(), e);
			return null;
		}
	}


	public byte[] obtenerJustificanteRegistroEntrada(String entidad, String numeroRegistro,
			Date fechaRegistro) throws Exception {
		return null;
	}

	public byte[] obtenerJustificanteRegistroSalida(String entidad, String numeroRegistro,
			Date fechaRegistro) throws Exception {
		return null;
	}

	public String obtenerDescServiciosDestino(String servicioDestino) {
		return null;
	}

	public String obtenerReferenciaJustificanteRegistroEntrada(String arg0,
			String arg1, Date arg2) throws Exception {
		return null;
	}

	public char obtenerTipoJustificanteRegistroEntrada() {
		return ConstantesPluginRegistro.JUSTIFICANTE_DESCARGA;
	}

}
