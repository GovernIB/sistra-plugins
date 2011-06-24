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


public class ParametrosDAOImpl extends ParametrosDAO
{
	Log log = LogFactory.getLog( ParametrosDAOImpl.class );
	
	public List obtenerServiciosDestino(){
		log.debug( "obteniendo servicios destino");
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Vector servicos=new Vector();
        ServicioDestinatario sd = null;
        
        try 
        {
            conn=es.caib.regtel.plugincaib.persistence.util.ToolsBD.getConn( getJNDIDatasource() );
            String sentenciaSql="select faxcorga CODIGO, CHAR(faxcorga) || '-' || faxdorgt DESCRIPCION  " +
            		"from borgani where faxfbaja=0 order by DESCRIPCION";
            ps=conn.prepareStatement(sentenciaSql);
            rs=ps.executeQuery();
            
            while (rs.next()) 
            {
            	sd = new ServicioDestinatario();
            	sd.setCodigo(String.valueOf(rs.getString("CODIGO")));
            	sd.setDescripcion(rs.getString("DESCRIPCION"));
            	servicos.addElement( sd );
            }
            
            if (servicos.size()==0) 
            {
            	sd = new ServicioDestinatario();
            	sd.setCodigo("");
            	sd.setDescripcion("No hi ha serveis");
                servicos.addElement( sd );
            }
            
        } 
        catch (Exception e) 
        {
        	sd = new ServicioDestinatario();
        	sd.setCodigo("");
        	sd.setDescripcion("BuscarServicios Error en la SELECT");
            servicos.addElement( sd );
            log.error( "Obteniendo servicios destino",e);            
        } 
        finally 
        {
        	ToolsBD.closeConn(conn, ps, rs);
        }
        log.debug( "Serveis " + servicos );
        return servicos;
	}
	
	public List obtenerOficinas(){
		log.debug( "obteniendo oficinas");
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Vector oficinas=new Vector();
        OficinaRegistro or = null;
         
        try 
        { 
        	conn=es.caib.regtel.plugincaib.persistence.util.ToolsBD.getConn( getJNDIDatasource() );
            String sentenciaSql="select faacagco CODIGO, CHAR(faacagco) || '-' || faadagco DESCRIPCION " +
            		"from bagecom where faafbaja=0 order by DESCRIPCION";
            ps=conn.prepareStatement(sentenciaSql);
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
            log.error( "Obteniendo oficinas",e);
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
            conn=es.caib.regtel.plugincaib.persistence.util.ToolsBD.getConn( getJNDIDatasource() );
            String sentenciaSql="select faacagco CODIGO, CHAR(faacagco) || '-' || faadagco DESCRIPCION from bagecom where faafbaja=0 and faacagco " +
                    "in (select fzhcagco from bzautor where fzhcusu=? and fzhcaut=?) order by DESCRIPCION";
            ps=conn.prepareStatement(sentenciaSql);
            ps.setString(1,usuario);
            ps.setString(2,autorizacion);
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
            log.error( "Obteniendo oficinas para el usuario " + usuario,e );            
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
        Vector documentos=new Vector();
        TipoAsunto ta = null;
        try 
        {
            conn=ToolsBD.getConn( getJNDIDatasource() );
            String sentenciaSql="select * from bztdocu where fzifbaja=0 and fzictipe<>'DU' order by fzictipe";
            ps=conn.prepareStatement(sentenciaSql);
            rs=ps.executeQuery();
            TipoAsunto initialElement = new TipoAsunto();
            initialElement.setCodigo( "" );
            initialElement.setDescripcion(" " );
            documentos.addElement( initialElement );
            while (rs.next()) {
            	ta = new TipoAsunto();
            	ta.setCodigo(rs.getString("fzictipe"));
            	ta.setDescripcion(rs.getString("fzictipe")+"-"+rs.getString("fzidtipe"));
            	documentos.addElement( ta );
            }
            if (documentos.size()==0) {
            	ta = new TipoAsunto();
            	ta.setCodigo("");
            	ta.setDescripcion("No hi ha tipus de documents" );
                documentos.addElement( ta );
            }
        } catch (Exception e) {
        	ta = new TipoAsunto();
         	ta.setCodigo("");
         	ta.setDescripcion("Error en la SELECT");
            documentos.addElement( ta );
    		log.error( "Error obteniendo documentos",e );
        } finally {
        	ToolsBD.closeConn(conn, ps, rs);
        }
        log.debug( "Tipos de documentos " + documentos );
        return documentos;
	}

	public List obtenerIdiomas()
	{
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Vector idiomas=new Vector();
        try 
        {
        	HashMap row = null;
            conn=ToolsBD.getConn( getJNDIDatasource() );
            String sentenciaSql="select * from bzidiom";
            ps=conn.prepareStatement(sentenciaSql);
            rs=ps.executeQuery();
            while (rs.next()) {
            	row = new HashMap();
            	// TODO : el idioma viene como código 1, 2
            	row.put( rs.getString("fzmcidi"), rs.getString("fzmdidi") );
                idiomas.addElement( row );
            }
            if (idiomas.size()==0) {
            	row.put( "", "No hi ha idiomes" );
            	idiomas.addElement( row );
            }
        } catch (Exception e) {
        	HashMap row = new HashMap();
        	row.put( "", "Error en la SELECT" );
        	idiomas.add( row );
        	log.error("Error obteniendo idiomas", e );
        } finally {
        	ToolsBD.closeConn(conn, ps, rs);
        }
        log.debug( "Idiomas " + idiomas );
        return idiomas;
	}

	public List obtenerMunicipiosBaleares()
	{
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        Vector baleares=new Vector();
        try 
        {
            conn=ToolsBD.getConn( getJNDIDatasource() );
            String sentenciaSql="select * from bagruge where fabctagg=90 and fabfbaja=0 order by fabdagge";
            ps=conn.prepareStatement(sentenciaSql);
            rs=ps.executeQuery();
            
            HashMap initialElement = new HashMap();
            initialElement.put( "", " " );
            baleares.add( initialElement );
            while (rs.next()) 
            {
            	HashMap row = new HashMap();
                row.put(rs.getString("fabcagge").toString(), rs.getString("fabdagge") );
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
            conn=ToolsBD.getConn( getJNDIDatasource() );
            String sentenciaSql="select * from bagecom where faacagco=? ";
            ps=conn.prepareStatement(sentenciaSql);
            ps.setInt(1,Integer.parseInt(codigoOficina));
            rs=ps.executeQuery();
            
            if (rs.next()) {
                descripcionOficina=rs.getString("faadagco");
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
