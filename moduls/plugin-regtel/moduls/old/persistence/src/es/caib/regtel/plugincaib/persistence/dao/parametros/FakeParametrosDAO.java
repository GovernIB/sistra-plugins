package es.caib.regtel.plugincaib.persistence.dao.parametros;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.regtel.plugincaib.persistence.util.ToolsBD;
import es.caib.sistra.plugins.regtel.OficinaRegistro;
import es.caib.sistra.plugins.regtel.ServicioDestinatario;
import es.caib.sistra.plugins.regtel.TipoAsunto;

public class FakeParametrosDAO extends ParametrosDAO
{
	
	private static String DATASOURCE_NAME = "java:/es.caib.sistra.db";
	private static Log log = LogFactory.getLog(FakeParametrosDAO.class);
	
	public List obtenerServiciosDestino(){
		log.debug( "obteniendo oficinas");
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Vector servicios=new Vector();
        ServicioDestinatario sd = null;
        try 
        {
            conn=es.caib.regtel.plugincaib.persistence.util.ToolsBD.getConn( DATASOURCE_NAME );
            String sentenciaSql="SELECT ROR_CODI CODIGO, ROR_CODI || ' - ' || ROR_DESC DESCRIPCION FROM STR_DREGOR ORDER BY DESCRIPCION";
            ps =conn.prepareStatement( sentenciaSql );
            rs=ps.executeQuery();
            
            while (rs.next()) 
            {
            	sd = new ServicioDestinatario();
            	sd.setCodigo(String.valueOf(rs.getString("CODIGO")));
            	sd.setDescripcion(rs.getString("DESCRIPCION"));
            	servicios.addElement( sd );
            }
            
            if (servicios.size()==0) 
            {
            	sd = new ServicioDestinatario();
            	sd.setCodigo("");
            	sd.setDescripcion("No hi ha serveis");
                servicios.addElement( sd );
            }
            
        } 
        catch (Exception e) 
        {
        	sd = new ServicioDestinatario();
        	sd.setCodigo("");
        	sd.setDescripcion("BuscarServicios Error en la SELECT");
            servicios.addElement( sd );
            log.error( "Obteniendo servicios "  );
            log.error( e );
        } 
        finally 
        {
        	ToolsBD.closeConn(conn, ps, rs);
        }
        log.debug( "Servicios " + servicios );
        return servicios;
		
	}
	public List obtenerOficinas() {
		log.debug( "obteniendo oficinas");
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Vector oficinas=new Vector();
        OficinaRegistro or = null;
        try 
        {
            conn=es.caib.regtel.plugincaib.persistence.util.ToolsBD.getConn( DATASOURCE_NAME );
            String sentenciaSql="SELECT OFI_CODI CODIGO, OFI_DESC DESCRIPCION FROM STR_DREGOF ORDER BY DESCRIPCION";
            ps =conn.prepareStatement( sentenciaSql );
            rs=ps.executeQuery();
            
            while (rs.next()) 
            {
            	or = new OficinaRegistro();
            	or.setCodigo(String.valueOf(rs.getString("CODIGO")));
            	or.setDescripcion(rs.getString("DESCRIPCION"));
            	oficinas.addElement( or );
            }
            
            if (oficinas.size()==0) 
            {
            	or = new OficinaRegistro();
            	or.setCodigo("");
            	or.setDescripcion("No hi ha oficines");
                oficinas.addElement( or );
            }
            
        } 
        catch (Exception e) 
        {
        	or = new OficinaRegistro();
        	or.setCodigo("");
        	or.setDescripcion("BuscarOficinas Error en la SELECT");
            oficinas.addElement( or );
            log.error( "Obteniendo oficinas "  );
            log.error( e );
        } 
        finally 
        {
        	ToolsBD.closeConn(conn, ps, rs);
        }
        log.debug( "Oficinas " + oficinas );
        return oficinas;
	}

	
	public List obtenerOficinas(String usuario, String autorizacion)
	{
		log.debug( "obteniendo oficinas para " + usuario );
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        usuario=usuario.toUpperCase();
        Vector oficinas=new Vector();
        OficinaRegistro or = null;
        
        try 
        {
            conn=es.caib.regtel.plugincaib.persistence.util.ToolsBD.getConn( DATASOURCE_NAME );
            String sentenciaSql="SELECT OFI_CODI CODIGO, OFI_DESC DESCRIPCION FROM STR_DREGOF ORDER BY DESCRIPCION";
            ps =conn.prepareStatement( sentenciaSql );
            rs=ps.executeQuery();
            
            while (rs.next()) 
            {
            	or = new OficinaRegistro();
            	or.setCodigo(String.valueOf(rs.getString("CODIGO")));
            	or.setDescripcion(rs.getString("DESCRIPCION"));
            	oficinas.addElement( or );
            }
            
            if (oficinas.size()==0) 
            {
            	or = new OficinaRegistro();
            	or.setCodigo("");
            	or.setDescripcion("No hi ha oficines per a l'usuari: "+usuario);
                oficinas.addElement( or );
            }
            
        } 
        catch (Exception e) 
        {
        	or = new OficinaRegistro();
        	or.setCodigo("");
        	or.setDescripcion("BuscarOficinas Error en la SELECT");
            oficinas.addElement( or );
            log.error( "Obteniendo oficinas para el usuario " + usuario );
            log.error( e );
        } 
        finally 
        {
        	ToolsBD.closeConn(conn, ps, rs);
        }
        log.debug( "Oficinas " + oficinas );
        return oficinas;
	}

	public List obtenerDocumentos()
	{
		Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Vector baleares=new Vector();
        TipoAsunto ta = null;
        try 
        {
        	 conn=ToolsBD.getConn( DATASOURCE_NAME );
             String sentenciaSql="SELECT ASU_CODI CODIGO, ASU_DESC DESCRIPCION FROM STR_DREGAS ORDER BY DESCRIPCION";
             ps=conn.prepareStatement(sentenciaSql);
             rs=ps.executeQuery();
             
             TipoAsunto initialElement = new TipoAsunto();
             initialElement.setCodigo( "" );
             initialElement.setDescripcion(" " );
             baleares.add( initialElement );
             while (rs.next()) 
             {
            	 ta = new TipoAsunto();
             	 ta.setCodigo(rs.getString("CODIGO"));
             	 ta.setDescripcion(rs.getString("DESCRIPCION"));
                 baleares.addElement( ta );
             }
             if (baleares.size()==1) 
             {
            	 ta = new TipoAsunto();
             	 ta.setCodigo("");
             	 ta.setDescripcion("");
                 baleares.addElement( ta );
             }
        } 
        catch (Exception e) {
        	ta = new TipoAsunto();
         	ta.setCodigo("");
         	ta.setDescripcion("Error en la SELECT");
            baleares.addElement( ta );
    		log.error( e );
        } finally {
        	ToolsBD.closeConn(conn, ps, rs);
        }
        log.debug( "Asuntos " + baleares );
        return baleares;
	}

	public List obtenerIdiomas()
	{
		Vector result = new Vector();
		HashMap row = new HashMap();
		row.put( "2", "Català" );
		result.addElement( row );
		row = new HashMap();
		row.put( "1", "Castellà" );
		result.addElement( row );
		return result;
	}

	public List obtenerMunicipiosBaleares()
	{
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Vector baleares=new Vector();
        try 
        {
        	 conn=ToolsBD.getConn( DATASOURCE_NAME );
             String sentenciaSql="SELECT MUN_CODIGO CODIGO, MUN_DENOFI DESCRIPCION FROM STR_DMUNIC WHERE MUN_PROVIN = 7 ORDER BY DESCRIPCION";
             ps=conn.prepareStatement(sentenciaSql);
             rs=ps.executeQuery();
             
             HashMap initialElement = new HashMap();
             initialElement.put( "", " " );
             baleares.add( initialElement );
             while (rs.next()) 
             {
             	HashMap row = new HashMap();
                 row.put(rs.getString("CODIGO"), rs.getString("DESCRIPCION") );
                 baleares.addElement( row );
             }
             if (baleares.size()==1) 
             {
             	HashMap row = new HashMap();
             	row.put( "",  "" );
                 baleares.addElement( row );
             }
        } 
        catch (Exception e) {
        	HashMap row = new HashMap();
        	row.put( "",  "Error en la SELECT" );
            baleares.addElement( row );
    		log.error( e );
        } finally {
        	ToolsBD.closeConn(conn, ps, rs);
        }
        log.debug( "Municipios baleares " + baleares );
        return baleares;
	}
	
	
	public String obtenerDescripcionOficina(String codigoOficina)
	{
		Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String descripcionOficina=null;
        try {
            conn=ToolsBD.getConn( DATASOURCE_NAME );
            String sentenciaSql="select OFI_DESC DESCRIPCION FROM STR_DREGOF WHERE OFI_CODI = ? ";
            ps=conn.prepareStatement(sentenciaSql);
            ps.setString(1, codigoOficina );
            rs=ps.executeQuery();
            
            if (rs.next()) {
                descripcionOficina=rs.getString("DESCRIPCION");
            } else {
                descripcionOficina="Oficina inexistente";
            }
        } catch (Exception e) {
            descripcionOficina="Oficina inexistente";
            System.out.println("ERROR: ");
    		e.printStackTrace();
        } finally {
        	ToolsBD.closeConn(conn, ps, rs);
        }
        return descripcionOficina;
	}

	public String obtenerFecha()
	{
		DateFormat dateF=new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date fechaTest=new java.util.Date();
        return dateF.format(fechaTest);
	}

	public String obtenerHorasMinutos()
	{
        DateFormat dateF=new SimpleDateFormat("HH:mm");
        java.util.Date fechaTest=new java.util.Date();
        return dateF.format(fechaTest);
	}

}
