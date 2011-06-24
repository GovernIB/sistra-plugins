package es.caib.regtel.plugincaib.persistence.dao.registro.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ComunHelper
{
	private static Log log = LogFactory.getLog( ComunHelper.class );
    /**
     * La responsabilidad de cerrar la conexion queda en manos de la aplicación llamante
     * @param conn
     * @param entidad1
     * @param entidad2
     * @return
     */
    public static String recuperaRemitenteCastellano(Connection conn, String entidad1, String entidad2) {
    	log.debug( "Recuperando remitente castellano para entidad1 [" + entidad1 + "] y entidad2 [" + entidad2 + "]");
        //Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String descripcionRemitente=null;
        
        if (entidad2.equals("")) {
            entidad2="0";
        }
        try {
            //conn=ToolsBD.getConn( this.getJNDIDatasource() );
            String sentenciaSql="select * from bzentid where fzgcenti=? and fzgnenti=? and fzgfbaja=0";
            ps=conn.prepareStatement(sentenciaSql);
            ps.setString(1,entidad1.toUpperCase());
            ps.setInt(2,Integer.parseInt(entidad2));
            rs=ps.executeQuery();
            if (rs.next()) {
                descripcionRemitente=rs.getString("fzgdenti");
            } else {
                descripcionRemitente="Remitent inexistent";
            }
        } catch (Exception e) {
            descripcionRemitente="Remitent inexistent";
            if (entidad1!=null && entidad2!=null)
            	log.error("ERROR: En recuperar el remitent de l'entitat "+entidad1+"-"+entidad2+".");
            else
            	log.error("ERROR: En recuperar el remitent de l'entitat NULL-NULL inexistent.");
    		log.error( e );
        } finally {
        	//ToolsBD.closeConn(conn, ps, rs);
        }
        log.debug( "Descripcion remitente [" + descripcionRemitente + "]");
        return descripcionRemitente;
    }
    
    public static Date parseData ( String data ) throws ParseException
    {
    	DateFormat dateF= new SimpleDateFormat(DATE_FORMAT );
        dateF.setLenient(false);
    	return dateF.parse( data );
    }
    
    public static Date parseHour ( String hour ) throws ParseException
    {
    	DateFormat horaF=new SimpleDateFormat(HOUR_FORMAT);
        horaF.setLenient(false);
    	return horaF.parse( hour ); 
    }
    
    
    /**
     * @param fecha
     */
    public static boolean validarFecha(String fecha) 
    {
    	DateFormat dateF= new SimpleDateFormat(DATE_FORMAT );
        try 
        {
            dateF.setLenient(false);
            dateF.parse(fecha);
            return true;
        } 
        catch (Exception ex) 
        {
        	log.error("Error validant la data:"+ex.getMessage());
        	ex.printStackTrace();
            return false;
        }
    }
    
    /**
     * 
     * @param date
     * @return data d'accés en format numèric (ddmmyyyy)
     */
    public static int getDataAccessNumeric( Date fechaSystem )
    {
    	
        DateFormat aaaammdd=new SimpleDateFormat("yyyyMMdd");
        int fzafsis=Integer.parseInt(aaaammdd.format(fechaSystem));
       
        return fzafsis; 
        
    }
    
    /**
     * 
     * @param fechaSystem
     * @return hora d'accés en format numèric (hhmissmis, hora (2 posicions), minut (2 posicions), segons (2 posicions), milisegons (3 posicions)
     */
    public static int getHoraAccessNumeric( Date fechaSystem )
    {
        DateFormat sss=new SimpleDateFormat("S");
        String ss=sss.format(fechaSystem);

    	DateFormat hhmmss=new SimpleDateFormat("HHmmss");
        switch (ss.length()) {
    		//Hem d'emplenar amb 0s.
        	case (1):
        		ss="00" + ss;
        		break;
        	case (2):
        		ss="0" + ss;
        		break;
       	}
        int horamili=Integer.parseInt(hhmmss.format(fechaSystem)+ss);
        
    	return horamili;
    }
    
    public static String DATE_FORMAT = "dd/MM/yyyy";
    public static String HOUR_FORMAT = "HH:mm";
    
    
    public static void main ( String args[] )
    {
    	getHoraAccessNumeric( new Date() );
    }
}
