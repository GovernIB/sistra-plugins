package es.caib.sistra.plugins.email.impl.caib;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.sistra.plugins.email.ConstantesEmail;
import es.caib.sistra.plugins.email.EstadoEnvio;
import es.caib.sistra.plugins.email.PluginEmailIntf;

/**
 * 	
 * 	Plugin para comprobacion envio de email
 *
 */
public class PluginEmailCAIB implements PluginEmailIntf{
	
	private Log log = LogFactory.getLog( PluginEmailCAIB.class);

	public EstadoEnvio consultarEstadoEnvio(String idEnvio) throws Exception {

		// Accedemos a tabla de check envios
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			
			log.debug("Verificando envio en tabla MOB_CAIBVE");
			
			InitialContext ic = new InitialContext();
			DataSource ds = (DataSource) ic.lookup("java:/es.caib.mobtratel.db");
			
			con = ds.getConnection();
			
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT MCE_ESTADO, MCE_DESCERR FROM MOB_CAIBVE WHERE MCE_IDENV = " + idEnvio );
			
			EstadoEnvio ee = new EstadoEnvio();
			if (rs.next()) {
				ee.setEstado(rs.getString("MCE_ESTADO").charAt(0));
				ee.setDescripcionEstado(rs.getString("MCE_DESCERR"));
				log.debug("Existe en la tabla. Estado: " + ee.getEstado());
			} else {
				ee.setEstado(ConstantesEmail.ESTADO_DESCONOCIDO);
				log.debug("No existe en la tabla");
			}
			 
			return ee;
		
		} catch (Exception ex) {
			log.debug("Excepcion al comprobar si existe en la tabla: " + ex.getMessage(), ex);
			EstadoEnvio ee = new EstadoEnvio();
			ee.setEstado(ConstantesEmail.ESTADO_DESCONOCIDO);
			return ee;
		} finally {
			if (rs != null) { 
				try { rs.close();} catch (Exception ex) {}
			}
			if (stmt != null) { 
				try { stmt.close();} catch (Exception ex) {}
			}
			if (con != null) { 
				try { con.close();} catch (Exception ex) {}
			}
		}
		
	}

	
}
