package es.caib.sistra.checkEmail;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.sistra.plugins.email.ConstantesEmail;

/**
 * @web.servlet name="check"
 * @web.servlet-mapping url-pattern="/check"
 */
public class ChekEmailServlet extends HttpServlet 
{
	private Log log = LogFactory.getLog( ChekEmailServlet.class);
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String id = request.getParameter("idaviso");
    	String estado = request.getParameter("estado");
    	String mensaje = request.getParameter("mensaje");        	
    	
    	if (id == null)  {
    		response.getWriter().write("KO - No se ha indicado parametro idaviso");
    		return;
    	}
    	
    	String estadoEnvio;
    	if ("OK".equals(estado)) {
    		estadoEnvio = ConstantesEmail.ESTADO_ENVIADO;    		
    	} else if ("KO".equals(estado)) {
    		estadoEnvio = ConstantesEmail.ESTADO_NO_ENVIADO;
    	} else {
    		response.getWriter().write("KO - El parametro estado debe ser OK o KO");
    		return;
    	}
    
    	
    	if (id != null) {
    		String res = checkEmail(id,estadoEnvio,mensaje);
    		response.getWriter().write(res);
    	}    	
    }

    
	private String checkEmail(String id, String estado, String mensaje) throws ServletException {

		// Insertar en tabla check envios
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			
			log.debug("Insertamos en tabla MOB_CAIBVE id = " + id);
			
			String sql = "INSERT INTO MOB_CAIBVE (MCE_IDENV, MCE_ESTADO, MCE_DESCERR) VALUES (?,?,?)"; 
			
			InitialContext ic = new InitialContext();
			DataSource ds = (DataSource) ic.lookup("java:/es.caib.mobtratel.db");
			con = ds.getConnection();			
			stmt = con.prepareStatement(sql);
			stmt.setString(1, id);
			stmt.setString(2, estado);
			stmt.setString(3, mensaje);
			stmt.executeUpdate();				
			
			if (!con.getAutoCommit()) {
				con.commit();
			}
			
			log.debug("Registro insertado en tabla MOB_CAIBVE id = " + id);
			
			return "OK";
			
		} catch (Exception ex) {
			log.error("Excepcion al insertar en MOB_CAIBVE: " + ex.getMessage(), ex);
			return "KO - Excepcion al insertar en MOB_CAIBVE: " + ex.getMessage(); 			
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
