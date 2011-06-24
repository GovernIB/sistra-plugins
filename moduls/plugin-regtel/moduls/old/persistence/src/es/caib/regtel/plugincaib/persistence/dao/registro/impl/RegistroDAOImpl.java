package es.caib.regtel.plugincaib.persistence.dao.registro.impl;

import java.sql.Connection;
import java.sql.DataTruncation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.regtel.plugincaib.model.DatosRegistroEntrada;
import es.caib.regtel.plugincaib.model.DatosRegistroSalida;
import es.caib.regtel.plugincaib.persistence.dao.registro.RegistroDAO;
import es.caib.regtel.plugincaib.persistence.util.ToolsBD;

public class RegistroDAOImpl extends RegistroDAO
{
	private static Log log = LogFactory.getLog( RegistroDAOImpl.class );
	
	public DatosRegistroEntrada grabar( DatosRegistroEntrada entrada, boolean validarOficina ) throws Exception 
	{
    	log.debug( "grabar. Entrada: [" + entrada + "]" );
        Connection conn = null;
        try 
        {
            conn=ToolsBD.getConn( getJNDIDatasource() );
            
            /* RAFA: NO SE PUEDE ESTABLECER AUTOCOMMIT EN UNA MANAGED TX
            conn.setAutoCommit(false);
            */
            
            // Ejecuta algoritmo de registro
            RegistroEntradaHelper.grabar (conn, entrada, validarOficina );
            log.debug( "grabar. Salida: [" + entrada + "]" );
            
            /*  RAFA: NO SE PUEDE HACER COMMIT / ROLLBACK EN UNA MANAGED TX
            conn.commit();
            */
            
            return entrada;
        }
        catch( DataTruncation dte )
		{
			log.error( "In field index" + dte.getIndex() +  " ];Expected data size [" + dte.getDataSize() + "]" );
			log.error( "Problema con la longitud de uno del campo [" + dte.getIndex() + "]", dte );
			entrada.setRegistroGrabado ( false );
            entrada.getErrores().put("","Error inesperat, no s'ha desat el registre"+": "+dte.getClass()+"->"+dte.getMessage());
			throw dte;
		}
        catch (Exception ex) 
        {
        	log.error(entrada.getUsuario()+": Excepció: "+ex.getMessage());
            ex.printStackTrace();
            entrada.setRegistroGrabado ( false );
            entrada.getErrores().put("","Error inesperat, no s'ha desat el registre"+": "+ex.getClass()+"->"+ex.getMessage());
            
            /*  RAFA: NO SE PUEDE HACER COMMIT / ROLLBACK EN UNA MANAGED TX
            try 
            {
            	
            	if (conn != null)
            		conn.rollback();            	
            } 
            catch (SQLException sqle) 
            {
                throw new Exception(entrada.getUsuario()+": S'ha produït un error i no s'han pogut tornar enrere els canvis efectuats", sqle);
            }
            */
            throw new Exception("Error inesperat: No s'ha desat el registre", ex);
        } 
        finally 
        {
            try 
            {
                if (conn != null && !conn.isClosed()) conn.close();
            } 
            catch (Exception e)
            {
                log.error(entrada.getUsuario()+": Excepció en tancar la connexió: "+e.getMessage());
                e.printStackTrace();
            }
        }
    }
	
	public DatosRegistroSalida grabar(DatosRegistroSalida salida, boolean validarOficina ) throws Exception
	{
		if ( log.isDebugEnabled() )
		{
			log.debug( "grabar. entrada .[" + salida + "]");
		}
		Connection conn = null;
		try
		{
			conn = ToolsBD.getConn( this.getJNDIDatasource() );
			RegistroSalidaHelper.grabar( conn, salida, validarOficina );
			log.debug( "grabar. Salida: [" + salida + "]" );
			return salida;
		}
		catch( DataTruncation dte )
		{
			log.error( "In field index" + dte.getIndex() +  " ];Expected data size [" + dte.getDataSize() + "]" );
			log.error( "Problema con la longitud de uno del campo [" + dte.getIndex() + "]", dte );
			throw dte;
		}
		finally 
        {
            try 
            {
                if (conn != null && !conn.isClosed()) conn.close();
            } 
            catch (Exception e)
            {
                log.error(salida.getUsuario()+": Excepció en tancar la connexió: "+e.getMessage());
                e.printStackTrace();
            }
        }
	}

	public void anularRegistroEntrada(String usuario, String oficina, String numeroEntrada, String anyoEntrada, boolean validarOficina) throws Exception {
		if ( log.isDebugEnabled() )
		{
			log.debug( "Anular registro entrada. Entrada .[" + usuario + " - " + oficina + " - " + numeroEntrada + " - " +  anyoEntrada + "]");
		}
		Connection conn = null;
		try
		{
			conn = ToolsBD.getConn( this.getJNDIDatasource() );
			RegistroEntradaHelper.anularRegistroEntrada( conn,usuario,oficina,numeroEntrada,anyoEntrada,validarOficina);
			log.debug( "Anular registro entrada. Salida: registro anulado");
		}
		catch( DataTruncation dte )
		{
			log.error( "In field index" + dte.getIndex() +  " ];Expected data size [" + dte.getDataSize() + "]" );
			log.error( "Problema con la longitud de uno del campo [" + dte.getIndex() + "]", dte );
			throw dte;
		}
		finally 
        {
            try 
            {
                if (conn != null && !conn.isClosed()) conn.close();
            } 
            catch (Exception e)
            {
                log.error(usuario+": Excepció en tancar la connexió: "+e.getMessage());
                e.printStackTrace();
            }
        }		
	}
}
