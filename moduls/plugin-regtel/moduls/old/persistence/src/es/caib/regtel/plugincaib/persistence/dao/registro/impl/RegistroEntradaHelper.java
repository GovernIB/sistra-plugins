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

import es.caib.regtel.plugincaib.model.DatosRegistroEntrada;
import es.caib.regtel.plugincaib.persistence.util.Configuracion;
import es.caib.regtel.plugincaib.persistence.util.ToolsBD;

public class RegistroEntradaHelper
{
	private static Log log = LogFactory.getLog( RegistroEntradaHelper.class );
	
	/**
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws Exception
     */
   public static void grabar(Connection conn, DatosRegistroEntrada entrada, boolean validarOficina ) throws SQLException, ClassNotFoundException, Exception {
	   PreparedStatement ms = null;
	   try{
		   if ( log.isDebugEnabled() )
			   log.debug( "cargar. Entrada: [" + entrada + "]" );
	       /* Grabamos registro si las validaciones son correctas */
	      
	       if (!entrada.isValidado()) {
	           entrada = validar( conn, entrada, validarOficina );
	       }
	       if (!entrada.isValidado()) {
	    	   String errores = "Errores validación:";
	    	   String key;
	    	   for (Iterator it = entrada.getErrores().keySet().iterator();it.hasNext();){
	    		   key = (String) it.next(); 
	    		   errores = "\n" + key + " : " + (String) entrada.getErrores().get(key); 
	    	   }
	           throw new Exception("No s'ha realitzat la validació de les dades del registre.\n" + errores);
	       }
	       
	       entrada.setRegistroGrabado( false );
	       
	           /* Descomponemos el año de la data de entrada, FZAANOEN y preparamos campo
	            FZAFENT en formato aaaammdd */
	           int fzaanoe;
	           String campo;
	           
	           Date fechaTest = ComunHelper.parseData( entrada.getDataentrada() );
	           
	           Calendar cal=Calendar.getInstance();
	           cal.setTime(fechaTest);
	           DateFormat date1=new SimpleDateFormat("yyyyMMdd");
	           
	           fzaanoe=cal.get(Calendar.YEAR);
	           entrada.setAnoEntrada( String.valueOf(fzaanoe ) );
	           
	           int fzafent=Integer.parseInt(date1.format(fechaTest));
	           
	           /* Recuperamos numero de entrada */
	           log.debug( "Recuperando numero de entrada" );
	           int fzanume=ToolsBD.RecogerNumeroEntrada(conn, fzaanoe, entrada.getOficina(), entrada.getErrores());
	           if ( log.isDebugEnabled() ) log.debug( "Numero entrada [" + fzanume + "]");
	           entrada.setNumeroEntrada(Integer.toString(fzanume));
	        //   System.out.println("getNumero="+getNumero());
	         //  System.out.println("getNumeroEntrada="+getNumeroEntrada());
	           /* Oficina, fzacagc */
	           int fzacagc=Integer.parseInt( entrada.getOficina() );
	           
	           /* Fecha documento en un campo en formato aaaammdd, fzafdoc */
	           fechaTest = ComunHelper.parseData( entrada.getData() );
	           cal.setTime(fechaTest);
	           int fzafdoc=Integer.parseInt(date1.format(fechaTest));
	           
	           /* Si el idioma del extracte es 1=castellano entonces el extracte lo guardamos
	              en el campo FZACONE, si es 2=catalan lo guardamos en FZACONE2 */
	           String fzacone, fzacone2;
	           if (entrada.getIdioex().equals("1")) {
	               fzacone=entrada.getComentario();
	               fzacone2="";
	           } else {
	               fzacone="";
	               fzacone2=entrada.getComentario();
	           }
	           
	           /* Preparamos los campos de Procedencia geografica, tipo de agrupacion geografica
	              y codigo de agrupacion geografica */
	           String fzaproce;
	           int fzactagg, fzacagge;
	           if (entrada.getFora().equals("")) {
	               fzactagg=90;
	               fzacagge=Integer.parseInt(entrada.getBalears());
	               fzaproce="";
	           } else {
	               fzaproce=entrada.getFora();
	               fzactagg=0;
	               fzacagge=0;
	           }
	           /* Fecha de actualizacion a ceros */
	           int ceros=0;
	           
	           /* Codigo de Organismo */
	           int fzacorg=Integer.parseInt(entrada.getDestinatari());
	           
	           /* Numero de entidad, fzanent */
	           int fzanent;
	           String fzacent;
	           if (entrada.getAltres().equals("")) {
	               entrada.setAltres( "" );
	               fzanent=Integer.parseInt(entrada.getEntidad2());
	               fzacent=entrada.getEntidadCastellano();
	           } else {
	               fzanent=0;
	               fzacent="";
	           }
	           
	           /* Idioma del extracto, fzacidi */
	           int fzacidi=Integer.parseInt( entrada.getIdioex() );
	           
	           /* Hora del documento, fzahora mmss */
	           Date horaTest=ComunHelper.parseHour( entrada.getHora() );
	           cal.setTime(horaTest);
	           DateFormat hhmm=new SimpleDateFormat("HHmm");
	           int fzahora=Integer.parseInt(hhmm.format(horaTest));
	           
	           /* Numero localizador y año localizador, fzanloc y fzaaloc */
	           if (entrada.getSalida1().equals("")) {entrada.setSalida1 ( "0" );}
	           if (entrada.getSalida2().equals("")) {entrada.setSalida2 ( "0" ) ;}
	           int fzanloc=Integer.parseInt(entrada.getSalida1());
	           int fzaaloc=Integer.parseInt(entrada.getSalida2());
	           
	           /* Numero de disquette, fzandis */
	           if (entrada.getDisquet().equals("")) {entrada.setDisquet("0");}
	           int fzandis=Integer.parseInt(entrada.getDisquet());
	           /* Actualizamos el numero de disquete */
	           if (fzandis>0){ToolsBD.actualizaDisqueteEntrada(conn, fzandis, entrada.getOficina(), entrada.getAnoEntrada(), entrada.getErrores());}
	           
	           /* Recuperamos la fecha y la hora del sistema, fzafsis(aaaammdd) y fzahsis (hhMMssmm) */
	           Date fechaSystem=new Date();
	           DateFormat aaaammdd=new SimpleDateFormat("yyyyMMdd");
	           int fzafsis=Integer.parseInt(aaaammdd.format(fechaSystem));
	           
	           DateFormat hhmmss=new SimpleDateFormat("HHmmss");
	           DateFormat sss=new SimpleDateFormat("S");
	           String ss=sss.format(fechaSystem);
	           if (ss.length()>2) {
	               ss=ss.substring(0,2);
	           }
	           int fzahsis=Integer.parseInt(hhmmss.format(fechaSystem)+ss);
	           
	           /* Grabamos numero de correo si tuviera */
	           if (entrada.getCorreo()!=null && !entrada.getCorreo().equals("")) {
	               String insertBZNCORR="insert into bzncorr (fzpcensa, fzpcagco, fzpanoen, fzpnumen, fzpncorr)" +
	                       "values (?,?,?,?,?)";
	               ms=conn.prepareStatement(insertBZNCORR);
	               ms.setString(1, "E");
	               ms.setInt(2,fzacagc);
	               ms.setInt(3,fzaanoe);
	               ms.setInt(4,fzanume);
	               ms.setString(5, entrada.getCorreo());
	               ms.execute();
	               ms.close();
	               ms=null;
	           }
	           
	           String insertOfifis="insert into bzentoff (foeanoen, foenumen, foecagco, ofe_codi)" +
	            "values (?,?,?,?)";
	            ms=conn.prepareStatement(insertOfifis);
	            ms.setInt(1,fzaanoe);
	            ms.setInt(2,fzanume);
	            ms.setInt(3,fzacagc);
	    		//por defecto es el 0
	            ms.setInt(4,0);
	    		ms.execute();
	    		ms.close();
	    		ms=null;
	    		
	    		
	           /* Ejecutamos sentencias SQL */
	           ms=conn.prepareStatement(SENTENCIA);
	           
	           ms.setInt(1,fzaanoe);
	           ms.setInt(2,fzanume);
	           ms.setInt(3,fzacagc);
	           ms.setInt(4,fzafdoc);
	           ms.setString(5,(entrada.getAltres().length()>30) ? entrada.getAltres().substring(0,30) : entrada.getAltres()); // 30 pos.
	           ms.setString(6,(fzacone.length()>160) ? fzacone.substring(0,160) : fzacone); // 160 pos.
	           ms.setString(7,(entrada.getTipo().length()>2) ? entrada.getTipo().substring(0,2) : entrada.getTipo());  // 2 pos.
	           ms.setString(8,"N");
	           ms.setString(9,"");
	           ms.setString(10,(fzaproce.length()>25) ? fzaproce.substring(0,25) : fzaproce); // 25 pos.
	           ms.setInt(11,fzafent);
	           ms.setInt(12,fzactagg);
	           ms.setInt(13,fzacagge);
	           ms.setInt(14,fzacorg);
	           ms.setInt(15,ceros);
	           ms.setString(16,(fzacent.length()>7) ? fzacent.substring(0,7) : fzacent); // 7 pos.
	           ms.setInt(17,fzanent);
	           ms.setInt(18,fzahora);
	           ms.setInt(19,fzacidi);
	           ms.setString(20,(fzacone2.length()>160) ? fzacone2.substring(0,160) : fzacone2); // 160 pos.
	           ms.setInt(21,fzanloc);
	           ms.setInt(22,fzaaloc);
	           ms.setInt(23,fzandis);
	           ms.setInt(24,fzafsis);
	           ms.setInt(25,fzahsis);
	           ms.setString(26,(entrada.getUsuario().toUpperCase().length()>10) ? entrada.getUsuario().toUpperCase().substring(0,10) : entrada.getUsuario().toUpperCase()); // 10 pos.
	           ms.setString(27,entrada.getIdioma());
	           
	           entrada.setRegistroGrabado( ms.execute() );
	           //entrada.setRegistroGrabado( true );
	           
	           if (fzacagc==33) 
	           {  // Si la oficina es la 33 llamamos a Expedientes VPO
	        	   if("PRODUCCION".equals(Configuracion.getProperty("entorno"))){
		   		       String remitenteVPO="";
		               if (!entrada.getAltres().trim().equals("")) 
		               {
		                   remitenteVPO=entrada.getAltres();
		               } else 
		               {
		            	   remitenteVPO = ComunHelper.recuperaRemitenteCastellano(conn, fzacent, fzanent+"");
		               }
		               ToolsBD.ejecutarVPO("E", "A", fzaanoe, fzanume, fzacagc, fzafdoc, remitenteVPO, entrada.getComentario(), entrada.getTipo(), fzafent, fzacagge, entrada.getUsuario(), entrada.getPassword() );
	        	   }else{
		            	log.debug( "Simula llamada a VPO.");
		           }
	           } 
//	           else if (fzacagc==13) 
//	           { // Si la oficina es la 13 llamamos a Expedientes de Cultura
//	               String remitenteCSS="";
//	               if (!entrada.getAltres().trim().equals("")) 
//	               {
//	                   remitenteCSS=entrada.getAltres();
//	               }
//	               else 
//	               {
//	            	   remitenteCSS = ComunHelper.recuperaRemitenteCastellano(conn, fzacent, fzanent+"");
//	               }
//	               ToolsBD.ejecutarCSS("E", "A", fzacagc, fzaanoe, fzanume, fzafent, fzafdoc, remitenteCSS, fzacagge, fzaproce, fzacorg, entrada.getTipo(), entrada.getIdioma(), entrada.getComentario(), entrada.getUsuario(), entrada.getPassword());
//	           }
	           ms.close();
	           ms=null;
	           
	           String usuario = entrada.getUsuario();
	           // log de proteccion de datos
	           logLopdBZENTRA(conn, "INSERT", (usuario.length()>10) ? usuario.substring(0,10) : usuario
	   				, fzafsis, ComunHelper.getHoraAccessNumeric( fechaSystem ), fzanume, fzaanoe, fzacagc);
	           
	           if ( log.isDebugEnabled() ) log.debug( "cargar. Salida: [" + entrada + "]" );
	   }finally{
		   ToolsBD.closePs(ms);
	   }
   }
   
	/**
	 * Valida los datos de entrada. La responsabilidad de cerrar la conexión es de los métodos llamantes
	 * @param conn
	 * @param entrada
	 * @return
	 */
   	private static DatosRegistroEntrada validar( Connection conn, DatosRegistroEntrada entrada, boolean validarOficina ) 
    {
        ResultSet rs = null;
        PreparedStatement ps = null;
        boolean validado = false;
        boolean error 	 = false;
        boolean actualizacion = false;
        String entidadCastellano=null;
        Hashtable errores = new Hashtable();
        errores.clear();
        try 
        {
        	if ( log.isDebugEnabled() ) log.debug( "validar. Entrada: [" + entrada + "]" );
            //conn=es.caib.regtel.persistence.util.ToolsBD.getConn( getJNDIDatasource() );
            /* Validamos la fecha de entrada */
            error = !ComunHelper.validarFecha(entrada.getDataentrada());
            if (error) 
            {
                errores.put("dataentrada","Data d'entrada no es logica");
            }
            /* La fecha de entrada sera <= que la fecha del dia */
            Date fechaHoy=new Date();
            Date fechaTest = ComunHelper.parseData( entrada.getDataentrada() );
            if (fechaTest.after(fechaHoy)) 
            {
                errores.put("dataentrada","Data d'entrada posterior a la del dia");
            }
            
            /* Validamos Hora */
            if (entrada.getHora()==null) 
            {
                errores.put("hora","Hora d'entrada no es logica");
            }
            else 
            {
                try 
                {
                    Date horaTest=ComunHelper.parseHour(entrada.getHora());
                }
                catch (ParseException ex) 
                {
                    errores.put("hora","Hora d'entrada no es logica");
                    errores.put("hora",entrada.getHora());
                }
            }
            
            /* Validamos la oficina */
            if (!entrada.getOficina().equals("")) 
            {           
                try 
                {                	
                	log.debug( "Validar oficina [" + validarOficina + "]" );
                	if (validarOficina){                         		
                		if (!validarOficina(conn,entrada.getUsuario(),entrada.getOficina())){
                			errores.put("oficina","Oficina: " + entrada.getOficina() + " no vàlida per a l'usuari: " + entrada.getUsuario() );
                		}                		
                	}
                } 
                catch (Exception e) 
                {
                	log.error(entrada.getUsuario() + ": Error en validar l'oficina "+e.getMessage() );
                    e.printStackTrace();
                    errores.put("oficina","Error en validar l'oficina: "+entrada.getOficina()+" de l'usuari: "+ entrada.getUsuario() +": "+e.getClass()+"->"+e.getMessage());
                }
                finally
                {
                   ToolsBD.closeRs(rs);
                   ToolsBD.closePs(ps);
                }
            } 
            else
            {
                errores.put("oficina","Oficina: "+entrada.getOficina()+" no vàlida per a l'usuari: "+ entrada.getUsuario() );
            }
            
           
            /* Validamos Fecha del documento */
            if (entrada.getData()==null) 
            {
                entrada.setData( entrada.getDataentrada() );
            }
            error = !ComunHelper.validarFecha(entrada.getData());
            if (error) 
            {
                errores.put("data","Data document, no es lògica");
            }
            
            /* Validamos Tipo de documento */
            try 
            {
                String sentenciaSql="select * from bztdocu where fzictipe=? and fzifbaja=0";
                ps=conn.prepareStatement(sentenciaSql);
                ps.setString(1,entrada.getTipo());
                rs=ps.executeQuery();
                
                if (rs.next()) 
                {
                } 
                else 
                {
                    errores.put("tipo","Tipus de document : "+entrada.getTipo()+" no vàlid");
                }
            } 
            catch (Exception e) 
            {
            	log.error(entrada.getUsuario()+": Error en validar el tipus de document"+e.getMessage() );
                e.printStackTrace();
                errores.put("tipo","Error en validar el tipus de document : "+entrada.getTipo()+": "+e.getClass()+"->"+e.getMessage());
            } 
            finally 
            {
            	ToolsBD.closeRs(rs);
                ToolsBD.closePs(ps);
            }
            
            /* Validamos el idioma del documento */
            try 
            {
                String sentenciaSql="select * from bzidiom where fzmcidi=?";
                ps=conn.prepareStatement(sentenciaSql);
                ps.setString(1,entrada.getIdioma());
                rs=ps.executeQuery();
                
                if (rs.next()) 
                {
                }
                else 
                {
                    errores.put("idioma","Idioma del document : "+entrada.getIdioma()+" no vàlid");
                }
            }
            catch (Exception e)
            {
            	log.error(entrada.getUsuario()+": Error en validar l'idioma del document"+e.getMessage() );
                e.printStackTrace();
            	errores.put("idioma","Error en validar l'idioma del document: "+entrada.getIdioma()+": "+e.getClass()+"->"+e.getMessage());
            }
            finally
            {
            	ToolsBD.closeRs(rs);
                ToolsBD.closePs(ps);
            }
            
            /* Validamos remitente */
            if (entrada.getEntidad1().trim().equals("") && entrada.getAltres().trim().equals("")) 
            {
                errores.put("entidad1","És obligatori introduir el remitent");
            } 
            else if(!entrada.getEntidad1().trim().equals("") && !entrada.getAltres().trim().equals("")) 
            {
                errores.put("entidad1","Heu d'introduir: Entitat o Altres");
            } 
            else if (!entrada.getEntidad1().equals("")) 
            {
                if (entrada.getEntidad2().equals("")) {entrada.setEntidad2("0");}
                try 
                {
                    String sentenciaSql="select * from bzentid where fzgcent2=? and fzgnenti=? and fzgfbaja=0";
                    ps=conn.prepareStatement(sentenciaSql);
                    ps.setString(1,entrada.getEntidad1());
                    ps.setInt(2,Integer.parseInt(entrada.getEntidad2()));
                    rs=ps.executeQuery();
                    
                    if (rs.next()) 
                    {
                        entidadCastellano = rs.getString("fzgcenti");
                    }
                    else 
                    {
                        errores.put("entidad1","Entitat Remitent : "+entrada.getEntidad1()+"-"+entrada.getEntidad2()+" no vàlid");
                        log.error(entrada.getUsuario()+": ERROR: en validar l'entitat remitent : "+entrada.getEntidad1()+"-"+entrada.getEntidad2()+" no vàlid");
                    }
                }
                catch (Exception e)
                {
                	log.error(entrada.getUsuario()+": Error en validar l'entitat remitent "+e.getMessage() );
                    e.printStackTrace();
                	errores.put("entidad1","Error en validar l'entitat remitent: "+entrada.getEntidad1()+"-"+entrada.getEntidad2()+": "+e.getClass()+"->"+e.getMessage());
                }
                finally
                {
                	ToolsBD.closeRs(rs);
                    ToolsBD.closePs(ps);
                }
            }
            
            /* Solamente se podra introducir numero de correo para la oficina 32 */
            if (!entrada.getOficina().equals("32") && !entrada.getCorreo().trim().equals(""))
            {
                errores.put("correo","El valor nombre de correu només es pot introduir per l'Oficina 32 (BOIB)");
                log.error(entrada.getUsuario()+": ERROR: El valor nombre de correu només es pot introduir per l'Oficina 32 (BOIB)");
            }
            
            
            /* Validamos la procedencia geografica */
            if (entrada.getBalears().equals("") && entrada.getFora().equals("")) 
            {
                errores.put("balears","Obligatori introduir Procedència Geogràfica");
            } 
            else if (!entrada.getBalears().equals("") && !entrada.getFora().equals("")) 
            {
                errores.put("balears","Heu d'introduir Balears o Fora de Balears");
            } 
            else if (!entrada.getBalears().equals("")) 
            {
                try 
                {
                    String sentenciaSql="select * from bagruge where fabctagg=90 and fabcagge=? and fabfbaja=0";
                    ps=conn.prepareStatement(sentenciaSql);
                    ps.setInt(1,Integer.parseInt(entrada.getBalears()));
                    rs=ps.executeQuery();
                    
                    if (rs.next()) 
                    {
                    } 
                    else 
                    {
                        errores.put("balears","Procedència geogràfica de Balears : "+entrada.getBalears()+" no vàlid");
                        log.error(entrada.getUsuario()+": ERROR: Procedència geogràfica de Balears : "+entrada.getBalears()+" no vàlid");
                    }
                }
                catch (Exception e) 
                {
                	log.error(entrada.getUsuario()+": Error en validar la procedència geogràfica de Balears "+e.getMessage() );
                    e.printStackTrace();
                    errores.put("balears","Error en validar la procedència geogràfica de Balears : "+entrada.getBalears()+": "+e.getClass()+"->"+e.getMessage());
                } 
                finally 
                {
                	ToolsBD.closeRs(rs);
                    ToolsBD.closePs(ps);
                }
            }
            
            /* No hay validacion del numero de salida del documento */
            
            if (entrada.getSalida1().equals("") && entrada.getSalida2().equals("")) 
            {
            } 
            else 
            {
                int chk1=0;
                int chk2=0;
                try {
                    chk1=Integer.parseInt(entrada.getSalida1());
                    chk2=Integer.parseInt(entrada.getSalida2());
                } catch (Exception e) {
                    errores.put("salida1","Ambdós camps de numero de sortida han de ser numèrics");
                    log.error(entrada.getUsuario()+": ERROR: Ambdós camps de numero de sortida han de ser numèrics");
                }
                if (chk2<1990 || chk2>2050) {
                    errores.put("salida1","Any de sortida, incorrecte");
                }
            }
            
            /* Se desactiva la validación del organismo destinatario */
            if ( false )
            {
	            try 
	            {
	                String sentenciaSql="select 1 from bzofior where fzfcagco=? and fzfcorga=?";
	                ps=conn.prepareStatement(sentenciaSql);
	                ps.setInt(1,Integer.parseInt(entrada.getOficina()));
	                ps.setInt(2,Integer.parseInt(entrada.getDestinatari()));
	                rs=ps.executeQuery();
	                
	                if (!rs.next()) 
	                {
	                    errores.put("destinatari","Organisme destinatari : "+entrada.getDestinatari()+" no vàlid");
	                }
	            } 
	            catch (NumberFormatException e1) 
	            {
	            	errores.put("destinatari","Organisme destinatari : "+entrada.getDestinatari()+" no vàlid");
	            } 
	            catch (Exception e) 
	            {
	            	log.error(entrada.getUsuario()+": Error en validar l'organisme destinatari "+e.getMessage() );
	                e.printStackTrace();
	                errores.put("destinatari","Error en validar l'organisme destinatari : "+entrada.getDestinatari()+": "+e.getClass()+"->"+e.getMessage());
	            } 
	            finally 
	            {
	            	ToolsBD.closeRs(rs);
	                ToolsBD.closePs(ps);
	            }
            }
            
            /* Validamos el idioma del extracto */
            
            if (!entrada.getIdioex().equals("1") && !entrada.getIdioex().equals("2")) {
                errores.put("idioex","L'idioma ha de ser 1 o 2, idioma="+entrada.getIdioex());
            }
            
            /* Validamos el numero de disquete */
            try {
                if (!entrada.getDisquet().equals("")) {
                    int chk1=Integer.parseInt(entrada.getDisquet());
                }
            } catch (Exception e) {
                errores.put("disquet","Numero de disquet no vàlid");
            }
            
            /* Validamos el extracto del documento */
            if (entrada.getComentario().equals("")) {
                errores.put("comentario","Heu d'introduir un extracte del document ");
            }
            
            /* Validaciones solamente validas para el proceso de actualizacion con modificacion de
             * campos criticos */
            if (actualizacion && !entrada.getMotivo().trim().equals("")) 
            {
                if (entrada.getComentarioNuevo().equals("")) 
                {
                    errores.put("comentario","Heu d'introduir un extracte del document ");
                }
                /* Validamos remitente */
                if (entrada.getEntidad1Nuevo().equals("") && entrada.getAltresNuevo().equals("")) 
                {
                    errores.put("entidad1","Obligatori introduir remitent");
                } 
                else if(!entrada.getEntidad1Nuevo().equals("") && !entrada.getAltresNuevo().equals("")) 
                {
                    errores.put("entidad1","Heu d'introduir: Entitat o Altres");
                } 
                else if (!entrada.getEntidad1Nuevo().equals("")) 
                {
                    if (entrada.getEntidad2Nuevo().equals("")) {entrada.setEntidad2Nuevo( "0" );}
                    try 
                    {
                        String sentenciaSql="select * from bzentid where fzgcent2=? and fzgnenti=? and fzgfbaja=0";
                        ps=conn.prepareStatement(sentenciaSql);
                        ps.setString(1,entrada.getEntidad1Nuevo());
                        ps.setInt(2,Integer.parseInt(entrada.getEntidad2Nuevo() ));
                        rs=ps.executeQuery();
                        
                        if (rs.next())
                        {
                        } 
                        else 
                        {
                            errores.put("entidad1","Entitat Remitent : "+entrada.getEntidad1()+"-"+entrada.getEntidad2()+" no vàlid");
                            log.error("Error en validar l'entitat Remitent "+entrada.getEntidad1()+"-"+entrada.getEntidad2()+" no vàlid");
                        }
                    } 
                    catch (Exception e) 
                    {
                    	log.error(entrada.getUsuario()+": Error en validar l'entitat Remitent "+e.getMessage() );
                        e.printStackTrace();
                        errores.put("entidad1","Error en validar l'entitat Remitent : "+entrada.getEntidad1()+"-"+entrada.getEntidad2()+": "+e.getClass()+"->"+e.getMessage());
                    } 
                    finally 
                    {
                    	ToolsBD.closeRs(rs);
                        ToolsBD.closePs(ps);
                    }
                }
            }
            
            /* Comprobamos que la ultima fecha introducida en el fichero sea inferior o igual
             * que la fecha de entrada del registro */
            if (!entrada.getOficina().equals("") && !actualizacion) 
            {
                try 
                {
                    String sentenciaSql="select max(fzafentr) from bzentra where fzaanoen=? and fzacagco=?";
                    fechaTest = ComunHelper.parseData(entrada.getDataentrada());
                    Calendar cal=Calendar.getInstance();
                    cal.setTime(fechaTest);
                    DateFormat date1=new SimpleDateFormat("yyyyMMdd");
                    
                    ps=conn.prepareStatement(sentenciaSql);
                    ps.setInt(1,cal.get(Calendar.YEAR));
                    ps.setInt(2,Integer.parseInt(entrada.getOficina()));
                    rs=ps.executeQuery();
                    int ultimaFecha=0;
                    
                    if (rs.next()) 
                    {
                        ultimaFecha=rs.getInt(1);
                        if (ultimaFecha>Integer.parseInt(date1.format(fechaTest))) 
                        {
                            errores.put("dataentrada","Data inferior a la darrera entrada");
                        }
                    }
                    
                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                    errores.put("dataentrada","Error inesperat a data d'entrada");
                    log.error(entrada.getUsuario()+": Error inesperat a la data d'entrada"+": "+e.getClass()+"->"+e.getMessage());
                } 
                finally 
                {
                	ToolsBD.closeRs(rs);
                    ToolsBD.closePs(ps);
                }
            }
            
            /* Fin de validaciones de campos */
        } 
        catch (Exception e) 
        {
            validado=false;
        } 
        
        if (errores.size()==0) 
        {
            validado=true;
        } 
        else 
        {
            validado=false;
        }
        entrada.setError( !validado );
        entrada.setValidado( validado );
        entrada.setActualizacion( actualizacion );
        entrada.setEntidadCastellano( entidadCastellano );
        entrada.setErrores( errores );
        if ( log.isDebugEnabled() ) log.debug( "validar. Salida: [" + entrada + "]" );
        return entrada;
    }
   	
    /**
     * El método llamante debe ser responsable de cerrar la conexión
     * Emplena la taula de control d'accés complint la llei LOPD per la taula BZENTRA
     * @param conn <code>java.sql.Connection</code>
     * @param tipusAcces <code>String</code> tipus d'accés a la taula
     * @param usuari <code>String</code> codi de l'usuari que fa l'acció.
     * @param data <code>Intr</code> data d'accés en format numèric (ddmmyyyy)
     * @param hora <code>Int</code> hora d'accés en format numèric (hhmissmis, hora (2 posicions), minut (2 posicions), segons (2 posicions), milisegons (3 posicions)
     * @param nombreRegistre <code>Int</code> nombre de registre
     * @param any <code>Int</code> any del registre
     * @param oficina <code>Int</code> oficina on s'ha registrat
     * @author Sebastià Matas Riera (bitel)
     */
    
    private static void logLopdBZENTRA(Connection conn, String tipusAcces, String usuari, int data, int hora, int nombreRegistre, int any, int oficina ) {
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			String sentenciaSql="insert into BZENLPD (FZTTIPAC, FZTCUSU, FZTDATAC, FZTHORAC, FZTNUMEN, FZTANOEN," +
				" FZTCAGCO) values (?,?,?,?,?,?,?)";
				ps=conn.prepareStatement(sentenciaSql);
				ps.setString(1,tipusAcces);
				ps.setString(2,usuari);
				ps.setInt(3,data);
				ps.setInt(4,hora);
				ps.setInt(5, nombreRegistre);
				ps.setInt(6, any);
				ps.setInt(7,oficina);
				ps.execute();				
		} catch (Exception e) {
			log.error( e );
			log.error("ERROR: S'ha produ\357t un error a logLopdBZENTRA");
		} finally {
			ToolsBD.closeRs(rs);
            ToolsBD.closePs(ps);
		}
		//System.out.println("RegistroEntradaBean: Desada informació dins BZENLPD: "+tipusAcces+" "+usuari+" "+data+" "+hora+" "+nombreRegistre+" "+any+" "+oficina);
     }

    private static String SENTENCIA="insert into bzentra (" +
    "fzaanoen, fzanumen, fzacagco, fzafdocu, fzaremit, fzaconen, fzactipe, fzacedie, fzaenula,"+
    "fzaproce, fzafentr, fzactagg, fzacagge, fzacorga, fzafactu, fzacenti, fzanenti, fzahora,"+
    "fzacidio, fzacone2, fzanloc, fzaaloc, fzandis, fzafsis, fzahsis, fzacusu, fzacidi"+
    ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    

    public static void anularRegistroEntrada(Connection conn, String usuario,String oficina, String numeroEntrada, String anyoEntrada, boolean validarOficina ) throws SQLException, ClassNotFoundException, Exception {
    	PreparedStatement ms = null;
    	try{
	    	if ( log.isDebugEnabled() )
	 		   log.debug( "Anular entrada: [" + usuario + " - " + oficina + " - " + numeroEntrada + " - " + anyoEntrada + "]" ); 	   
	 	   
	 	   // Validamos oficina
	 	   log.debug( "Validar oficina [" + validarOficina + "]" );
		   if (validarOficina){                         		
		  		if (!validarOficina(conn,usuario,oficina)){
		  			throw new Exception("Usuario no permitido para la oficina");
		  		}                		
		  	}
	       
		   // Anulamos registro        
	        ms=conn.prepareStatement("update bzentra set fzaenula=? where fzaanoen=? and fzanumen=? and fzacagco=?");
	        ms.setString(1,"S");
	        ms.setString(2,anyoEntrada);
	        ms.setString(3,numeroEntrada);
	        ms.setString(4,oficina);
	        
	        ms.executeUpdate();          
	        ms.close();
	        ms=null;
	            
	        /* Recuperamos la fecha y la hora del sistema, fzafsis(aaaammdd) y fzahsis (hhMMssmm) */
	        Date fechaSystem=new Date();
	        DateFormat aaaammdd=new SimpleDateFormat("yyyyMMdd");
	        int fzafsis=Integer.parseInt(aaaammdd.format(fechaSystem));
	        
	        // log de proteccion de datos
	        logLopdBZENTRA(conn, "UPDATE", (usuario.length()>10) ? usuario.substring(0,10) : usuario
	    				, fzafsis, ComunHelper.getHoraAccessNumeric( fechaSystem ), Integer.parseInt(numeroEntrada),Integer.parseInt(anyoEntrada), Integer.parseInt(oficina));
 	   }finally{ 		  
          ToolsBD.closePs(ms);
 	   }
    }
    
    /**
     * Valida si un usuario puede acceder a la oficina
     */
    private static boolean validarOficina(Connection conn,String usuario,String oficina) throws Exception{
    	
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	
    	try{
    		String sentenciaSql="select * from bzautor where fzhcusu=? and fzhcaut=? and fzhcagco in " +
				"(select faacagco from bagecom where faafbaja=0 and faacagco=?)";
			ps=conn.prepareStatement(sentenciaSql);
			
			ps.setString(1,usuario);
			ps.setString(2,"AE");
			ps.setInt(3,Integer.parseInt(oficina));
			rs=ps.executeQuery();
			
			if (rs.next()) 
			{
				return true;
			}
			else 
			{
				return false;			
			}    		    	
    	}finally{
    		ToolsBD.closeRs(rs);
    		ToolsBD.closePs(ps);
    	}  
    }
    
}
