package es.caib.regtel.plugincaib.persistence.ejb;

import java.io.FileInputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.redose.modelInterfaz.ReferenciaRDS;
import es.caib.regtel.plugincaib.model.LogUsuariosRegistro;
import es.caib.regtel.plugincaib.model.LogUsuariosRegistroId;
import es.caib.regtel.plugincaib.persistence.delegate.DelegateException;
import es.caib.regtel.plugincaib.persistence.delegate.DelegateRegistroWebUtil;
import es.caib.sistra.plugins.regtel.ResultadoRegistro;
import es.caib.xml.registro.factoria.impl.AsientoRegistral;
import es.caib.xml.registro.factoria.impl.Justificante;


/**
 * SessionBean de registro telemático 
 * 
 * Las conexiones a registro se haran con el usuario configurado en el properties, se denomina "usuario de conexion".
 * Se establecera el usuario que realiza el registro. El "usuario de registro" coincidira con el "usuario de conexion" 
 * excepto a la hora de confirmar registro y a la  hora de anular registro, que se hara con el usuario que realizó el registro.
 * Para controlar en la anulación que "usuario de registro" usar se realizará un log de los usuarios de registro.
 *
 * @ejb.bean
 *  name="regtel/plugincaib/persistence/RegistroWebEJB"
 *  jndi-name="es.caib.regtel.plugincaib.persistence.RegistroWeb"
 *  type="Stateless"
 *  view-type="remote"
 *  
 */
public abstract class RegistroWebEJB implements SessionBean
{
	private static final Log logger = LogFactory.getLog(RegistroWebEJB.class);
	
	private Properties config = null;

	private SessionContext context;
	
	public void setSessionContext(SessionContext ctx) {
        this.context = ctx;
    }
	
	/**
	 * Obtiene implementacion regweb.
	 * @return
	 * @throws Exception
	 */
	private RegistroWebImplInt getImplementacionRegweb() {
		try {
			RegistroWebImplInt regweb;
			if ("WS".equals(getConfig().getProperty("plugin.regweb.modo"))) {
				regweb = new RegistroWebImplWs();			
			} else {
				regweb = new RegistroWebImplEjb();
			}			
			regweb.setPrincipal(getPrincipal());
			return regweb;
		} catch (Exception ex) {
			logger.error("Error al obtener implementacion regweb", ex);
			throw new EJBException("Error al obtener implementacion regweb", ex);
		}
	}
	
    /**
     * @ejb.create-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     * 
     */
	public void ejbCreate() throws CreateException {
		try
		{
				
		}
		catch( Exception exc )
		{
			//exc.printStackTrace();
			logger.error( exc );
			throw new CreateException( exc.getLocalizedMessage() );
		}
	}	
	
	
	/**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     */
	public ResultadoRegistro registroEntrada(
			AsientoRegistral asiento,
			ReferenciaRDS refAsiento,
			Map refAnexos) throws Exception {
		RegistroWebImplInt regweb = getImplementacionRegweb();
		ResultadoRegistro res = regweb.registroEntrada(asiento, refAsiento, refAnexos);
		logUsuarioRegistro("E",res,getUsuarioConexionRegistro());
		return res;
	}
	

	/**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     */
	public ResultadoRegistro registroSalida(
			AsientoRegistral asiento,
			ReferenciaRDS refAsiento,
			Map refAnexos) throws Exception {		
		RegistroWebImplInt regweb = getImplementacionRegweb();
		ResultadoRegistro res = regweb.registroSalida(asiento, refAsiento, refAnexos);		
		logUsuarioRegistro("S",res,getUsuarioConexionRegistro());
		return res;
	}

	
	/**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     */
	public ResultadoRegistro confirmarPreregistro(
			String oficina,
			String codigoProvincia,
			String codigoMunicipio,
			String descripcionMunicipio,
			Justificante justificantePreregistro,
			ReferenciaRDS refJustificante,
			ReferenciaRDS refAsiento,
			Map refAnexos) throws Exception {
		
		RegistroWebImplInt regweb = getImplementacionRegweb();
		ResultadoRegistro res = regweb.confirmarPreregistro(oficina, codigoProvincia,
				codigoMunicipio, descripcionMunicipio, justificantePreregistro,
				refJustificante, refAsiento, refAnexos);	
		logUsuarioRegistro("E",res,getPrincipal());
		return res;
				
	}

	/**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     */
	public List obtenerOficinasRegistro() {
		RegistroWebImplInt regweb = getImplementacionRegweb();
		return regweb.obtenerOficinasRegistro();		
	}

	/**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     */
	public List obtenerOficinasRegistroUsuario(String usuario) {
		RegistroWebImplInt regweb = getImplementacionRegweb();
		return regweb.obtenerOficinasRegistroUsuario(usuario);
	}

	/**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     */
	public List obtenerTiposAsunto() {
		RegistroWebImplInt regweb = getImplementacionRegweb();
		return regweb.obtenerTiposAsunto();
	}

	/**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     */
	public List obtenerServiciosDestino() {
		RegistroWebImplInt regweb = getImplementacionRegweb();
		return regweb.obtenerServiciosDestino();				
	}
	
	/**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     */
    public void anularRegistroEntrada(String numeroRegistro, Date fechaRegistro) throws Exception {
    	RegistroWebImplInt regweb = getImplementacionRegweb();
		regweb.anularRegistroEntrada(numeroRegistro, fechaRegistro);
	}

    /**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     */
	public void anularRegistroSalida(String numeroRegistro, Date fechaRegistro) throws Exception {
		RegistroWebImplInt regweb = getImplementacionRegweb();
		regweb.anularRegistroSalida(numeroRegistro, fechaRegistro);
	}
	

    /**
     * @ejb.interface-method
     * @ejb.permission role-name = "${role.todos}"
     * @ejb.permission role-name = "${role.auto}"
     */
	public String obtenerDescripcionSelloOficina(String codigoOficina) throws Exception {
		RegistroWebImplInt regweb = getImplementacionRegweb();
		return regweb.obtenerDescripcionSelloOficina(codigoOficina);
	}
	
	// -------------------------------------------------------------------------------------------------------------------------------
	//		FUNCIONES UTILIDAD
	// -------------------------------------------------------------------------------------------------------------------------------
	private Properties getConfig() throws Exception {
		if (config == null) {
			config = new Properties();
			
			// Path directorio de configuracion
       	 	String pathConf = System.getProperty("ad.path.properties");
       	 	
			// Propiedades globales
       	 	config.load(new FileInputStream(pathConf + "sistra/global.properties"));
			
       	 	// Propiedades plugin
			config.load(new FileInputStream(pathConf + "sistra/plugins/plugin-regtel.properties"));
		}
		return config; 
	}	
	
	private String getUsuarioConexionRegistro() throws Exception {
		String auto = getConfig().getProperty("plugin.regweb.auth.auto");
		String userName = null;
		if ("true".equals(auto)) {
			userName = getConfig().getProperty("auto.user");			
		} else {
			userName = getConfig().getProperty("plugin.regweb.auth.username");
		}
		return userName;
	}		
	
	
	private String getPrincipal() throws Exception {
		return context.getCallerPrincipal().getName();
	}		
	

	private void logUsuarioRegistro(String tipoRegistro, ResultadoRegistro res,
			String usuarioRegistro) throws DelegateException {
		LogUsuariosRegistro lu = new LogUsuariosRegistro();
		lu.setId(new LogUsuariosRegistroId(tipoRegistro, res.getNumeroRegistro()));
		lu.setFechaRegistro(res.getFechaRegistro());
		lu.setUsuarioRegistro(usuarioRegistro);
		DelegateRegistroWebUtil.getLogUsuariosRegistroDelegate().realizarLogUsuarioRegistro(lu);
	}

}
