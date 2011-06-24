package es.caib.regtel.plugincaib.persistence.dao.registro.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.caib.regtel.plugincaib.model.DatosRegistroSalida;
import es.caib.regtel.plugincaib.persistence.util.Configuracion;
import es.caib.regtel.plugincaib.persistence.util.ToolsBD;


public class RegistroSalidaHelper
{
	private static Log log = LogFactory.getLog( RegistroSalidaHelper.class );
	
    /**
     * 
     * @param conn
     * @param salida
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws Exception
     */
	public static DatosRegistroSalida grabar( Connection conn, DatosRegistroSalida salida, boolean validarOficina ) throws SQLException, ClassNotFoundException, Exception 
    {
		PreparedStatement ms = null;
		
		try{
	    	if ( log.isDebugEnabled() )
	    		log.debug( "grabar. entrada: " + salida );
	        
	        if (!salida.isValidado()) {
	            validar( conn, salida, validarOficina );
	        }
	        if (!salida.isValidado()) 
	        {
	        	String errores = "Errores validación:";
	      	   String key;
	      	   for (Iterator it = salida.getErrores().keySet().iterator();it.hasNext();){
	      		   key = (String) it.next(); 
	      		   errores = "\n" + key + " : " + (String) salida.getErrores().get(key); 
	      	   }
	            throw new Exception("No s'ha realitzat la validació de les dades del registre: " + errores );
	        }
	        salida.setRegistroSalidaGrabado( false );
	            
	        /* Descomponemos el año de la data de entrada, FZAANOEN y preparamos campo
	         FZSFENT en formato aaaammdd */
	        int fzsanoe;
	        String campo;
	        
	        Date fechaTest = ComunHelper.parseData( salida.getDataSalida() );
	        Calendar cal=Calendar.getInstance();
	        cal.setTime(fechaTest);
	        DateFormat date1=new SimpleDateFormat("yyyyMMdd");
	        
	        fzsanoe=cal.get(Calendar.YEAR);
	        salida.setAnoSalida( String.valueOf(fzsanoe) );
	        
	        int fzsfent=Integer.parseInt(date1.format(fechaTest));
	        
	        /* Recuperamos numero de salida */
	        //conn.setAutoCommit(false);
	        
	        int fzsnume=ToolsBD.RecogerNumeroSalida(conn, fzsanoe, salida.getOficina(), salida.getErrores() );
	        salida.setNumeroSalida(Integer.toString(fzsnume));
	        
	        if ( log.isDebugEnabled() ) log.debug( "Recuperando numero de salida " + salida.getNumeroSalida()); 
	        
	        /* Oficina, fzscagc */
	        int fzscagc=Integer.parseInt( salida.getOficina() );
	        
	        /* Fecha documento en un campo en formato aaaammdd, fzsfdoc */
	        fechaTest = ComunHelper.parseData( salida.getData() );
	        cal.setTime(fechaTest);
	        int fzsfdoc=Integer.parseInt(date1.format(fechaTest));
	        
	        /* Si el idioma del extracte es 1=castellano entonces el extracte lo guardamos
	           en el campo FZACONE, si es 2=catalan lo guardamos en FZACONE2 */
	        String fzscone, fzscone2;
	        if (salida.getIdioex().equals("1")) 
	        {
	            fzscone=salida.getComentario();
	            fzscone2="";
	        } else {
	            fzscone="";
	            fzscone2=salida.getComentario();
	        }
	        /* Preparamos los campos de Procedencia geografica, tipo de agrupacion geografica
	           y codigo de agrupacion geografica */
	        String fzsproce;
	        int fzsctagg, fzscagge;
	        if (salida.getFora().equals("")) {
	            fzsctagg=90;
	            fzscagge=Integer.parseInt( salida.getBalears() );
	            fzsproce="";
	        } else {
	            fzsproce=salida.getFora();
	            fzsctagg=0;
	            fzscagge=0;
	        }
	        /* Fecha de actualizacion a ceros */
	        int ceros=0;
	        
	        /* Codigo de Organismo */
	        int fzscorg=Integer.parseInt( salida.getRemitent() );
	        
	        /* Numero de entidad, fzsnent */
	        int fzsnent;
	        String fzscent;
	        if (salida.getAltres().equals("")) 
	        {
	            fzsnent=Integer.parseInt(salida.getEntidad2());
	            fzscent= salida.getEntidadCastellano();
	        } 
	        else 
	        {
	            fzsnent=0;
	            fzscent="";
	        }
	        
	        /* Idioma del extracto, fzscidi */
	        int fzscidi=Integer.parseInt(salida.getIdioex() );
	        
	        /* Hora del documento, fzshora mmss */
	        Date horaTest=ComunHelper.parseHour( salida.getHora() );
	        cal.setTime(horaTest);
	        DateFormat hhmm=new SimpleDateFormat("HHmm");
	        int fzshora=Integer.parseInt(hhmm.format(horaTest));
	        
	        /* Numero localizador y año localizador, fzsnloc y fzsaloc */
	        if (salida.getEntrada1().equals("")) {salida.setEntrada1 ( "0" ); }
	        if (salida.getEntrada2().equals("")) {salida.setEntrada2 ( "0" );}
	        int fzsnloc=Integer.parseInt(salida.getEntrada1() );
	        int fzsaloc=Integer.parseInt(salida.getEntrada2() );
	        
	        /* Numero de disquette, fzsndis */
	        if (salida.getDisquet().equals("")) {salida.setDisquet( "0" );}
	        int fzsndis=Integer.parseInt(salida.getDisquet());
	        /* Actualizamos el numero de disquete */
	        if (fzsndis>0){ToolsBD.actualizaDisqueteSalida(conn, fzsndis, salida.getOficina(), salida.getAnoSalida(), salida.getErrores());}
	        
	        /* Recuperamos la fecha y la hora del sistema, fzsfsis(aaaammdd) y
	           fzshsis (hhMMssmm) */
	        Date fechaSystem=new Date();
	        DateFormat aaaammdd=new SimpleDateFormat("yyyyMMdd");
	        int fzsfsis=Integer.parseInt(aaaammdd.format(fechaSystem));
	        
	        DateFormat hhmmss=new SimpleDateFormat("HHmmss");
	        DateFormat sss=new SimpleDateFormat("S");
	        String ss=sss.format(fechaSystem);
	        if (ss.length()>2) {
	            ss=ss.substring(0,2);
	        }
	        int fzshsis=Integer.parseInt(hhmmss.format(fechaSystem)+ss);
	        
	        /* Grabamos numero de correo si tuviera */
	        if (salida.getCorreo()!=null && !salida.getCorreo().equals("")) {
	            String insertBZNCORR="insert into bzncorr (fzpcensa, fzpcagco, fzpanoen, fzpnumen, fzpncorr)" +
	                    "values (?,?,?,?,?)";
	            ms=conn.prepareStatement(insertBZNCORR);
	            ms.setString(1, "S");
	            ms.setInt(2,fzscagc);
	            ms.setInt(3,fzsanoe);
	            ms.setInt(4,fzsnume);
	            ms.setString(5, salida.getCorreo());
	            ms.execute();
	            ms.close();
	            ms=null;
	        }
	        
            String insertOfifis="insert into bzsaloff (fosanoen, fosnumen, foscagco, ofs_codi)" +
            "values (?,?,?,?)";
            ms=conn.prepareStatement(insertOfifis);
            ms.setInt(1,fzsanoe);
            ms.setInt(2,fzsnume);
            ms.setInt(3,fzscagc);
    		//por defecto es el 0
            ms.setInt(4,0);
    		ms.execute();
    		ms.close();
    		ms = null;
	        
	        /* Ejecutamos sentencias SQL */
	        ms=conn.prepareStatement(SENTENCIA);
	        
	        ms.setInt(1,fzsanoe);
	        ms.setInt(2,fzsnume);
	        ms.setInt(3,fzscagc);
	        ms.setInt(4,fzsfdoc);
	        ms.setString(5,(salida.getAltres().length()>30) ? salida.getAltres().substring(0,30) : salida.getAltres());
	        ms.setString(6,(fzscone.length()>160) ? fzscone.substring(0,160) : fzscone);
	        ms.setString(7,salida.getTipo());
	        ms.setString(8,"N");
	        ms.setString(9,"");
	        ms.setString(10,(fzsproce.length()>25) ? fzsproce.substring(0,25) : fzsproce);
	        ms.setInt(11,fzsfent);
	        ms.setInt(12,fzsctagg);
	        ms.setInt(13,fzscagge);
	        ms.setInt(14,fzscorg);
	        ms.setInt(15,ceros);
	        ms.setString(16,(fzscent.length()>7) ? fzscent.substring(0,7) : fzscent);
	        ms.setInt(17,fzsnent);
	        ms.setInt(18,fzshora);
	        ms.setInt(19,fzscidi);
	        ms.setString(20,(fzscone2.length()>160) ? fzscone2.substring(0,160) : fzscone2); // 160 pos.
	        ms.setInt(21,fzsnloc);
	        ms.setInt(22,fzsaloc);
	        ms.setInt(23,fzsndis);
	        ms.setInt(24,fzsfsis);
	        ms.setInt(25,fzshsis);
	        ms.setString(26,(salida.getUsuario().toUpperCase().length()>10) ? salida.getUsuario().toUpperCase().substring(0,10) : salida.getUsuario().toUpperCase());
	        ms.setString(27,salida.getIdioma());
	        
	        salida.setRegistroSalidaGrabado( ms.execute() );
	        
	        if (fzscagc==33) 
	        {  // Si la oficina es la 33 llamamos a Expedientes VPO
	            if("PRODUCCION".equals(Configuracion.getProperty("entorno"))){
		        	String remitenteVPO="";
		            if (!salida.getAltres().trim().equals("")) 
		            {
		                remitenteVPO=salida.getAltres();
		            } 
		            else 
		            {
		                javax.naming.InitialContext contexto = new javax.naming.InitialContext();
		                remitenteVPO=ComunHelper.recuperaRemitenteCastellano(conn, fzscent, fzsnent+"");
		            }
		            ToolsBD.ejecutarVPO("S", "A", fzsanoe, fzsnume, fzscagc, fzsfdoc, remitenteVPO, salida.getComentario(), salida.getTipo(), fzsfent, fzscagge, salida.getUsuario(), salida.getPassword());
	            }else{
	            	log.debug( "Simula llamada a VPO.");
	            }
	        } 
//	        else if (fzscagc==13) 
//	        { // Si la oficina es la 13 llamamos a Expedientes de Cultura
//	            String remitenteCSS="";
//	            if (!salida.getAltres().trim().equals("")) 
//	            {
//	                remitenteCSS=salida.getAltres();
//	            } 
//	            else 
//	            {
//	                javax.naming.InitialContext contexto = new javax.naming.InitialContext();
//	                remitenteCSS=ComunHelper.recuperaRemitenteCastellano(conn, fzscent, fzsnent+"");
//	            }
//	            ToolsBD.ejecutarCSS("S", "A", fzscagc, fzsanoe, fzsnume, fzsfent, fzsfdoc, remitenteCSS, fzscagge, fzsproce, fzscorg, salida.getTipo(), salida.getIdioma(), salida.getComentario(), salida.getUsuario(), salida.getPassword() );
//	        }
	        ms.close();
	        ms=null;
	        
	        String usuario = salida.getUsuario();
			logLopdBZSALIDA(conn, "INSERT", (usuario.toUpperCase().length()>10) ? usuario.toUpperCase().substring(0,10) : usuario.toUpperCase()
					, fzsfsis, ComunHelper.getHoraAccessNumeric( fechaSystem ), fzsnume, fzsanoe, fzscagc);
			
	        if ( log.isDebugEnabled() )
	        	log.debug( "grabar. Salida [" + salida + "]" );
	        return salida;
		}finally{
			ToolsBD.closePs(ms);
		}
    }
    
    private static DatosRegistroSalida validar( Connection conn, DatosRegistroSalida salida, boolean validarOficina ) 
    {
        if ( log.isDebugEnabled( ) )
        	log.debug( "validar. Entrada " + salida );
    	ResultSet rs = null;
        PreparedStatement ps = null;
        boolean validado = false;
        boolean error = false;
        String entidadCastellano = null;
        Hashtable errores = new Hashtable();
        errores.clear();
        try 
        {
            /* Validamos la fecha de entrada */
        	error = !ComunHelper.validarFecha( salida.getDataSalida() );
            if (error) 
            {
                errores.put("datasalida","Data de sortida no és lògica");
            }
            /* La fecha de salida sera <= que la fecha del dia */
            Date fechaHoy=new Date();
            Date fechaTest = ComunHelper.parseData( salida.getDataSalida() );
            if (fechaTest.after(fechaHoy)) {
                errores.put("datasalida","Data de sortida posterior a la del dia");
            }
            
            /* Validamos Hora */
            if (salida.getHora() == null || "".equals( salida.getHora() ) ) {
                errores.put("hora","Hora de sortida no és lògica");
            } 
            else 
            {
                try 
                {
                    Date horaTest = ComunHelper.parseHour( salida.getHora() ) ;
                } catch (ParseException ex) {
                    errores.put("hora","Hora de sortida no és lògica");
                }
            }
            
            /* Validamos la oficina */
            if (!salida.getOficina().equals("")) 
            {
                try 
                {
                	
                	// TODO : Ojo, validación de usuario que está comentada y que habrá que activar !!!!!!!
                	log.debug( "Validar oficina [" + validarOficina + "]" );
                	if ( validarOficina )
                	{
	                    String sentenciaSql="select * from bzautor where fzhcusu=? and fzhcaut=? and fzhcagco in " +
	                            "(select faacagco from bagecom where faafbaja=0 and faacagco=?)";
	                    ps=conn.prepareStatement(sentenciaSql);
	                    ps.setString(1,salida.getUsuario());
	                    ps.setString(2,"AS");
	                    ps.setInt(3,Integer.parseInt(salida.getOficina()));
	                    rs=ps.executeQuery();
	                    
	                    if (rs.next()) {
	                    } else {
	                        errores.put("oficina","Oficina: "+salida.getOficina()+" no vàlida per a l'usuari: "+ salida.getUsuario() );
	                    }
                	}
                } 
                catch (Exception e) 
                {
                	log.error( salida.getUsuario() +": Error en validar l'oficina "+e.getMessage(),e );                    
                    errores.put("oficina","Error en validar l'oficina: "+salida.getOficina()+" de l'usuari: "+salida.getUsuario()+": "+e.getClass()+"->"+e.getMessage());
                } finally {
                	ToolsBD.closeRs(rs);
                	ToolsBD.closePs(ps);                   
                }
            } else {
                errores.put("oficina","Oficina: "+salida.getOficina()+" no vàlida per a l'usuari: "+salida.getUsuario());
            }
            /* Validamos Fecha del documento */
            if (salida.getData() == null || "".equals( salida.getData() ) ) 
            {
            	salida.setData( salida.getDataSalida() );
            }
            error = !ComunHelper.validarFecha( salida.getData() );
            if (error) {
                errores.put("data","Data document, no es lògica");
            }
            
            /* Validamos Tipo de documento */
            try 
            {
                String sentenciaSql="select * from bztdocu where fzictipe=? and fzifbaja=0";
                ps=conn.prepareStatement(sentenciaSql);
                ps.setString(1, salida.getTipo() );
                rs=ps.executeQuery();
                
                if (rs.next()) {
                } else {
                    errores.put("tipo","Tipus de document : "+salida.getTipo()+" no vàlid");
                }
            } catch (Exception e) {
            	log.error(salida.getUsuario()+": Error en validar el tipus de document"+e.getMessage(),e );                
                errores.put("tipo","Error en validar el tipus de document : "+salida.getTipo()+": "+e.getClass()+"->"+e.getMessage());
            } finally {
               ToolsBD.closeRs(rs);
               ToolsBD.closePs(ps);
            }
            
            /* Validamos el idioma del documento */
            try {
                String sentenciaSql="select * from bzidiom where fzmcidi=?";
                ps=conn.prepareStatement(sentenciaSql);
                ps.setString(1,salida.getIdioma());
                rs=ps.executeQuery();
                
                if (rs.next()) {
                } else {
                    errores.put("idioma","Idioma del document : "+salida.getIdioma()+" no vàlid");
                }
            } catch (Exception e) {
            	log.error(salida.getUsuario()+": Error en validar l'idioma del document"+e.getMessage(),e );
            	errores.put("idioma","Error en validar l'idioma del document: "+salida.getIdioma()+": "+e.getClass()+"->"+e.getMessage());
            } finally {
            	ToolsBD.closeRs(rs);
                ToolsBD.closePs(ps);
            }
            
            /* Validamos destinatario */
            if (salida.getEntidad1().equals("") && salida.getAltres().equals("")) 
            {
                errores.put("entidad1","Obligatori introduir destinatari");
            } 
            else if(!salida.getEntidad1().equals("") && !salida.getAltres().equals("")) 
            {
                errores.put("entidad1","Heu d'introduir: Entitat o Altres");
            } 
            else if (!salida.getEntidad1().equals("")) 
            {
                if (salida.getEntidad2().equals("")) {salida.setEntidad2 ( "0" );}
                try {
                    String sentenciaSql="select * from bzentid where fzgcent2=? and fzgnenti=? and fzgfbaja=0";
                    ps=conn.prepareStatement(sentenciaSql);
                    ps.setString(1,salida.getEntidad1());
                    ps.setInt(2,Integer.parseInt(salida.getEntidad2() ));
                    rs=ps.executeQuery();
                    
                    if (rs.next()) {
                        entidadCastellano=rs.getString("fzgcenti");
                    } else {
                        errores.put("entidad1","Entitat Destinatària : "+salida.getEntidad1()+"-"+salida.getEntidad2()+" no vàlida");
                    }
                } catch (Exception e) {
                	log.error(salida.getUsuario()+": Error en validar l'entitat destinatària "+e.getClass()+"->"+e.getMessage(),e );
                    errores.put("entidad1","Error en validar l'entitat destinatària: "+salida.getEntidad1()+"-"+salida.getEntidad2()+": "+e.getClass()+"->"+e.getMessage());
                } finally {
                	ToolsBD.closeRs(rs);
                    ToolsBD.closePs(ps);
                }
            }
            /** Validamos la longitud del destinatario */
            if ( !salida.getAltres().equals("") && salida.getAltres().length() > 30 )
            {
            	// opcion 1 : error
            	//errores.put( "altres", "Error al validar la longitud del destinatari, no pot ser superior a 30 caràcters"  );
            	// opcion 2 : truncar
            	salida.setAltres( salida.getAltres().substring( 0, 30 ) );
            }
            /* Validamos la procedencia geografica */
            if (salida.getBalears().equals("") && salida.getFora().equals("")) 
            {
                errores.put("balears","Obligatori introduir destí Geogràfic");
            } 
            else if (!salida.getBalears().equals("") && !salida.getFora().equals("")) 
            {
                errores.put("balears","Heu d'introduir Balears o Fora de Balears");
            } 
            else if (!salida.getBalears().equals("")) 
            {
                try 
                {
                    String sentenciaSql="select * from bagruge where fabctagg=90 and fabcagge=? and fabfbaja=0";
                    ps=conn.prepareStatement(sentenciaSql);
                    ps.setInt(1,Integer.parseInt(salida.getBalears()));
                    rs=ps.executeQuery();
                    
                    if (rs.next()) {
                    } else {
                        errores.put("balears","Desti geogràfic de Balears : "+salida.getBalears()+" no vàlid");
                    }
                } catch (Exception e) {
                	log.error(salida.getUsuario()+": Error en validar En comprovar el destí geogràfic "+e.getMessage(),e );
                	errores.put("balears","Error en validar En comprovar el destí geogràfic: "+salida.getBalears()+": "+e.getClass()+"->"+e.getMessage());
                } finally {
                	ToolsBD.closeRs(rs);
                    ToolsBD.closePs(ps);
                }
            }
            
            /* No hay validacion del numero de entrada del documento */
            
            if (salida.getEntrada1().equals("") && salida.getEntrada2().equals("")) {
            } else {
                int chk1=0;
                int chk2=0;
                try {
                    chk1=Integer.parseInt(salida.getEntrada1());
                    chk2=Integer.parseInt(salida.getEntrada2());
                } catch (Exception e) {
                    errores.put("entrada1","Ambdós camps de numero d'entrada han de ser numèrics");
                }
                if (chk2<1990 || chk2>2050) {
                    errores.put("entrada1","Any d'entrada, incorrecte");
                }
           }
            
            /* Validamos el Organismo emisor */
            try {
                String sentenciaSql="select * from bzofior where fzfcagco=? and fzfcorga=?";
                ps=conn.prepareStatement(sentenciaSql);
                ps.setInt(1,Integer.parseInt(salida.getOficina()));
                ps.setInt(2,Integer.parseInt(salida.getRemitent()));
                rs=ps.executeQuery();
                
                if (rs.next()) {
                } else {
                    errores.put("remitent","Organisme emisor: "+salida.getRemitent()+" no vàlid");
                }
            } catch (NumberFormatException e1) {
            	errores.put("remitent","Organisme emisor: "+salida.getRemitent()+" no vàlid");
            } catch (Exception e) {
            	log.error(salida.getUsuario()+": Error en validar l'organisme emisor "+e.getMessage(),e );
                errores.put("remitent","Error en validar l'organisme emisor: "+salida.getRemitent()+": "+e.getClass()+"->"+e.getMessage());
            } finally {
            	ToolsBD.closeRs(rs);
                ToolsBD.closePs(ps);
            }
            
            /* Validamos el idioma del extracto */
            
            if (!salida.getIdioex().equals("1") && !salida.getIdioex().equals("2")) {
                errores.put("idioex","L'idioma ha de ser 1 ò 2, idioma="+salida.getIdioex());
            }
            
            
            /* Comprobamos que la ultima fecha introducida en el fichero sea inferior o igual
             * que la fecha de entrada del registro */
            if (!salida.getOficina().equals("")  && !salida.isActualizacion()) {
                try {
                    String sentenciaSql="select max(fzsfentr) from bzsalida where fzsanoen=? and fzscagco=?";
                    fechaTest = ComunHelper.parseData( salida.getDataSalida() );
                    Calendar cal=Calendar.getInstance();
                    cal.setTime(fechaTest);
                    DateFormat date1=new SimpleDateFormat("yyyyMMdd");
                    
                    ps=conn.prepareStatement(sentenciaSql);
                    ps.setInt(1,cal.get(Calendar.YEAR));
                    ps.setInt(2,Integer.parseInt(salida.getOficina()));
                    rs=ps.executeQuery();
                    int ultimaFecha=0;
                    
                    if (rs.next()) {ultimaFecha=rs.getInt(1);}
                    if (ultimaFecha>Integer.parseInt(date1.format(fechaTest))) {
                        errores.put("datasalida","Data inferior a la darrera sortida");
                    }
                } catch (Exception e) {
                	log.error(salida.getUsuario()+": Error inesperat en la data de sortida "+e.getMessage(),e );
                    errores.put("datasalida","Error inesperat en la data de sortida: "+salida.getRemitent()+": "+e.getClass()+"->"+e.getMessage());
                } 
                finally 
                {
                	ToolsBD.closeRs(rs);
                	ToolsBD.closePs(ps);
                }
            }
            
            /* Solamente se podra introducir numero de correo para la oficina 32 
            if (!oficina.equals("32") && !correo.trim().equals("")) {
                errores.put("correo","El valor número de correu només introduïble per Oficina 32 (BOIB)");
            }
*/
            /* Validamos el numero de disquete */
            try {
                if (!salida.getDisquet().equals("")) 
                {
                    int chk1=Integer.parseInt(salida.getDisquet());
                }
            } catch (Exception e) {
                errores.put("disquet","Numero de disquet no vàlid");
            }
            
            /* Validamos el extracto del documento */
            if (salida.getComentario().equals("")) {
                errores.put("comentario","Heu d'introduir un extracte del document ");
            }
            
            /* Validaciones solamente validas para el proceso de actualizacion con modificacion de
             * campos criticos */
            if (salida.isActualizacion() && !salida.getMotivo().trim().equals("")) {
                if (salida.getComentarioNuevo().equals("")) {
                    errores.put("comentario","Heu d'introduir un extracte del document ");
                }
                /* Validamos remitente */
                if (salida.getEntidad1Nuevo().equals("") && salida.getAltresNuevo().equals("")) {
                    errores.put("entidad1","Obligatori introduir remitent");
                } else if(!salida.getEntidad1Nuevo().equals("") && !salida.getAltresNuevo().equals("")) {
                    errores.put("entidad1","Heu d'introduir: Entitat o Altres");
                } else if (!salida.getEntidad1Nuevo().equals("")) {
                    if (salida.getEntidad2Nuevo().equals("")) {salida.setEntidad2Nuevo ( "0" );}
                    try {
                        String sentenciaSql="select * from bzentid where fzgcent2=? and fzgnenti=? and fzgfbaja=0";
                        ps=conn.prepareStatement(sentenciaSql);
                        ps.setString(1,salida.getEntidad1Nuevo());
                        ps.setInt(2,Integer.parseInt( salida.getEntidad2Nuevo() ));
                        rs=ps.executeQuery();
                        
                        if (rs.next()) {
                        } else {
                            errores.put("entidad1","Entitat Destinatària : "+salida.getEntidad1()+"-"+salida.getEntidad2()+" no vàlid");
                        }
                    } catch (Exception e) {
                    	log.error(salida.getUsuario()+": Error inesperat en l'entitat destinatària "+e.getMessage() ,e);
                        errores.put("entidad1","Error inesperat en l'entitat destinatària : "+salida.getEntidad1()+"-"+salida.getEntidad2()+": "+e.getClass()+"->"+e.getMessage());
                    } finally {
                    	ToolsBD.closeRs(rs);
                        ToolsBD.closePs(ps);
                    }
                }
            }
            
            /* Fin de validaciones de campos */
        } catch (Exception e) {
            e.printStackTrace();
            validado=false;
        } 
        if (errores.size()==0) {
            validado=true;
        } else {
            validado=false;
        }
        
        salida.setValidado( validado );
        salida.setError( !validado );
        salida.setEntidadCastellano( entidadCastellano );
        salida.setErrores( errores );
        if( log.isDebugEnabled() ) log.debug( "validar. Salida " + salida );
        
        return salida;
    }
    
    /**
	 * Emplena la taula de control d'accés complint la llei LOPD per la taula BZSALIDA
	 * @param conn conexión
	 * @param tipusAcces <code>String</code> tipus d'accés a la taula
	 * @param usuari <code>String</code> codi de l'usuari que fa l'acció.
	 * @param data <code>Intr</code> data d'accés en format numèric (ddmmyyyy)
	 * @param hora <code>Int</code> hora d'accés en format numèric (hhmissmis, hora (2 posicions), minut (2 posicions), segons (2 posicions), milisegons (3 posicions)
	 * @param nombreRegistre <code>Int</code> nombre de registre
	 * @param any <code>Int</code> any del registre
	 * @param oficina <code>Int</code> oficina on s'ha registrat
	 * @author Sebastià Matas Riera (bitel)
	 */
	
    private static void logLopdBZSALIDA(Connection conn, String tipusAcces, String usuari, int data, int hora, int nombreRegistre, int any, int oficina ) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			String sentenciaSql="insert into BZSALPD (FZUTIPAC, FZUCUSU, FZUDATAC, FZUHORAC, FZUNUMEN, FZUANOEN," +
				" FZUCAGCO) values (?,?,?,?,?,?,?)";
				ps=conn.prepareStatement(sentenciaSql);
				ps.setString(1,tipusAcces);
				ps.setString(2,usuari);
				ps.setInt(3,data);
				ps.setInt(4,hora);
				ps.setInt(5, nombreRegistre);
				ps.setInt(6, any);
				ps.setInt(7,oficina);
				ps.execute();
				ps.close();
				ps=null;
		} catch (Exception e) {			
			log.error("ERROR: S'ha produ\357t un error a logLopdBZSALID",e);
		} finally {
			ToolsBD.closeRs(rs);
            ToolsBD.closePs(ps);
		}
		//System.out.println("RegistroSalidaBean: Desada informació dins BZSALPD: "+tipusAcces+" "+usuari+" "+data+" "+hora+" "+nombreRegistre+" "+any+" "+oficina);
     }
	
	private static String SENTENCIA="insert into bzsalida (" +
    "fzsanoen, fzsnumen, fzscagco, fzsfdocu, fzsremit, fzsconen, fzsctipe, fzscedie, fzsenula,"+
    "fzsproce, fzsfentr, fzsctagg, fzscagge, fzscorga, fzsfactu, fzscenti, fzsnenti, fzshora,"+
    "fzscidio, fzscone2, fzsnloc, fzsaloc, fzsndis, fzsfsis, fzshsis, fzscusu, fzscidi"+
    ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
}
