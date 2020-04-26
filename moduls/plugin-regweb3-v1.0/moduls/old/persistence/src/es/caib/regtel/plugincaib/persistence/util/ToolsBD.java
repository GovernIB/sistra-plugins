package es.caib.regtel.plugincaib.persistence.util;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.logging.LogFactory;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.LocalDataArea;

public class ToolsBD {
	
	private static org.apache.commons.logging.Log log = LogFactory.getLog( ToolsBD.class );
	
    /**
     * Devuelve una conexión con la BD obtenida vía JNDI.
     */
    public static Connection getConn( String jndiDatasource ) throws RemoteException {
        try{
            Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup( jndiDatasource );
            return ds.getConnection();
        } catch(Exception e){
        	log.error( e );
            throw new RemoteException("Error recuperant la connexió a la BD", e);
        }
    }
    
    public static void closeConn(Connection co, PreparedStatement ps, ResultSet rs ) {
    	try {
    		// Tancam el que pugui estar obert
    		closeRs(rs);
    		closePs(ps);
    		
    		// Si la connexió no està tancada, la tancam
    		 if (co != null && !co.isClosed()) co.close();
    	} catch (Exception e){
    		log.error("Excepció en tancar la connexió: " + e.getMessage(),e);    		
    	}
    }
    
    public static int RecogerNumeroEntrada(Connection con, int anyo, String idOficina, Map errores) throws RemoteException {
        return RecogerNumero(con, anyo, idOficina, "E", errores);
    }
    public static int RecogerNumeroSalida(Connection con, int anyo, String idOficina, Map errores) throws RemoteException {
        return RecogerNumero(con, anyo, idOficina, "S", errores);
    }
    
    /**
     * Recoge una PK para una entrada en el registro.
     *
     * @param fzaanoe Año del registro
     * @param oficina Código de la oficina que solicita el número
     * @param tipo "E" para entradas o "S" para Salidas
     */
    private static int RecogerNumero(Connection con, int fzaanoe, String oficina, String tipo, Map errores) throws RemoteException {
        int numero=0;
        ResultSet rs = null;
        
        PreparedStatement ps = null;
        PreparedStatement ps2 = null;
        
        String consulta = "select * from bzcones where fzdaensa=? and fzdcensa=? and fzdcagco=? with rs ";
        String update = "update bzcones set fzdnumer=fzdnumer+1 where fzdaensa=? and fzdcensa=? and fzdcagco=?";
        try {
            /* Actualizamos el numero de entrada */
            ps=con.prepareStatement(update);
            ps.setInt(1,fzaanoe);
            ps.setString(2,tipo);
            ps.setInt(3,Integer.parseInt(oficina));
            int num = ps.executeUpdate();
            ps.close();
            ps=null;
            
            ps2=con.prepareStatement(consulta);
            ps2.setInt(1,fzaanoe);
            ps2.setString(2,tipo);
            ps2.setInt(3,Integer.parseInt(oficina));
            rs=ps2.executeQuery();
            if (rs.next())  numero=rs.getInt("fzdnumer");
            else {
                errores.put(".","No s'ha inicialitzat el comptador d'entrades/sortides per a l'oficina - any "+oficina+"-"+fzaanoe);
                throw new RemoteException("No s'ha inicialitzat el comptador d'entrades/sortides per a l'oficina - any "+oficina+"-"+fzaanoe);
            }            
        } catch (Exception ex) {
            ex.printStackTrace();
            errores.put("z","No és possible gravar el registre ara, torni a intentar-ho ");
            throw new RemoteException("No és possible gravar el registre ara, torni a intentar-ho més tard ", ex);
        }finally{
        	closePs(ps);
        	closeRs(rs);
        	closePs(ps2);        	
        }
        
        return numero;
    }
    
    public static void actualizaDisqueteEntrada(Connection conn, int disquete, String oficina, String anoEntrada, Map errores) throws RemoteException {
        actualizaDisquete(conn, disquete, oficina, "E", anoEntrada, errores);
    }
    public static void actualizaDisqueteSalida(Connection conn, int disquete, String oficina, String anoSalida, Map errores) throws RemoteException {
        actualizaDisquete(conn, disquete, oficina, "S", anoSalida, errores);
    }
    /**
     * Actualiza el numero de Disquete. Si el numero a actualizar es mayor que el leido.
     *
     * @param con Connection
     * @param disquete int con el numero del disquete a actualizar
     * @param oficina Código de la oficina que solicita el número
     * @param tipo    "E"=Entrada   "S"=Salida
     * @param anyo  año a actualizar
     * @param errores   Hastable con los errores
     */
    public static void actualizaDisquete(Connection conn, int disquete, String oficina, String tipo, String anyo, Map errores) throws RemoteException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        PreparedStatement ts = null;
        try {
            String sentenciaSql="select * from bzdisqu where fzlcensa=? and fzlcagco=? and fzlaensa=?  with rs ";
            ps=conn.prepareStatement(sentenciaSql);
            ps.setString(1, tipo);
            ps.setString(2, oficina);
            ps.setString(3, anyo);
            rs=ps.executeQuery();
            int numeroDisquete=0;
            if (rs.next()) {
                numeroDisquete=rs.getInt("fzlndis");
                rs.close();
                rs=null;
                ps.close();
                ps=null;
                /* Actualizamos el numero de disquete si es mayor al leido */
                if (disquete>numeroDisquete) {
                    sentenciaSql="update bzdisqu set fzlndis=? where fzlcensa=? and fzlcagco=? and fzlaensa=?";
                    ts=conn.prepareStatement(sentenciaSql);
                    ts.setInt(1,disquete);
                    ts.setString(2,tipo);
                    ts.setInt(3,Integer.parseInt(oficina));
                    ts.setInt(4,Integer.parseInt(anyo));
                    boolean cualquiera=ts.execute();
                    ts.close();
                    ts=null;
                }
            } else if (disquete>0) {
                sentenciaSql="insert into bzdisqu (fzlcensa, fzlcagco, fzlaensa, fzlndis)" +
                        " values (?, ?, ?, ?)";
                ts=conn.prepareStatement(sentenciaSql);
                ts.setString(1,tipo);
                ts.setInt(2,Integer.parseInt(oficina));
                ts.setInt(3,Integer.parseInt(anyo));
                ts.setInt(4,disquete);
                boolean cualquiera=ts.execute();
                ts.close();
                ts=null;
            }
        } catch (Exception e) {
        	log.error("Excepció actualizant disquete", e);            
            errores.put("","No és possible gravar el registre ara, torni a intentar-ho ");
            throw new RemoteException("No és possible gravar el registre ara, torni a intentar-ho ",e);
        } finally {            
            closeRs(rs);
            closePs(ps);
            closePs(ts);                            
        }
    }
    
    public static String convierteEntidad(String entidadCastellano, Connection con) {
        Connection conn = con;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String entidadCatalan=null;
        
        try {
        	//conn=getConn();
            String sentenciaSql="select * from bzentid where fzgcenti=? and fzgfbaja=0";
            ps=conn.prepareStatement(sentenciaSql);
            ps.setString(1,entidadCastellano);
            rs=ps.executeQuery();
            if (rs.next()) {
                entidadCatalan=rs.getString("fzgcent2");
            } else {
                entidadCatalan="";
            }
            if (rs!=null) {
            	rs.close();
            	rs=null;
            }
            if (ps!=null) {
            	ps.close();
            	ps=null;
            }
        } catch (Exception e) {
            log.error("ERROR: convierteEntidad", e );            
            entidadCatalan="";  
        }finally {
        	closeRs(rs);
        	closePs(ps);
        }
        return entidadCatalan;
    }
    
    public static boolean estaPdteVisado(String tipo, String oficina, String ano, String numero, Connection conn ) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        boolean pdteVisado=false;
        try {
            //conn=getConn();
            String sentenciaSql="select * from bzmodif where (fzjiextr=' ' or fzjiremi=' ') and fzjfvisa=0 " +
                    "and fzjcensa=? and fzjcagco=? and fzjanoen=? and fzjnumen=?";
            ps=conn.prepareStatement(sentenciaSql);
            ps.setString(1,tipo);
            ps.setString(2,oficina);
            ps.setString(3,ano);
            ps.setString(4,numero);
            rs=ps.executeQuery();
            if (rs.next()) {
                pdteVisado=true;
            } else {
                pdteVisado=false;
            }
        } catch (Exception e) {
    		log.error("ERROR: convierteEntidad", e );
            pdteVisado=false;
        } finally {
        	closeRs(rs);
        	closePs(ps);
        }        
        return pdteVisado;       
    }
    
    public static void ejecutarVPO(String tipo, String operacion, int ano, int numer, int ofici, int fdoc, String remit,
            String extracto, String tipdoc, int fent, int cagge, String usuario, String password) throws RemoteException {
        
        try {
            AS400 system = new AS400(Configuracion.getProperty("as400"), usuario, password);
            system.setGuiAvailable(false);
            CommandCall command = new CommandCall(system);
            LocalDataArea dataArea = new LocalDataArea(system);
            dataArea.clear();
            
            byte VPO[]=new byte[400]; // Se crea el array de la misma longitud que el area de datos VPO
            
            rellenaBytes("", VPO, 0, 400); // inicializamos el array a blancos
            
            int inicio=0;
            inicio=inicio+rellenaBytes(tipo, VPO, inicio, 1);
            inicio=inicio+rellenaBytes(operacion, VPO, inicio, 1);
            inicio=inicio+rellenaNumeros(ano, VPO, inicio, 4);
            inicio=inicio+rellenaNumeros(numer, VPO, inicio, 5);
            inicio=inicio+rellenaNumeros(ofici, VPO, inicio, 2);
            inicio=inicio+rellenaNumeros(fdoc, VPO, inicio, 8);
            inicio=inicio+rellenaBytes(remit, VPO, inicio, 30);
            inicio=inicio+rellenaBytes(extracto, VPO, inicio, 160);
            inicio=inicio+rellenaBytes(tipdoc, VPO, inicio, 2);
            inicio=inicio+rellenaNumeros(fent, VPO, inicio, 8);
            inicio=inicio+rellenaNumeros(cagge, VPO, inicio, 3);
            
            // Escribimos el area de datos
            dataArea.write(new String(VPO), 500, -1);
            
            if (command.run("call "+Configuracion.getProperty("biblioteca")+"/"+Configuracion.getProperty("programaVPO")) != true) {
                log.error("inicio ERROR AS400");
                AS400Message[] messagelist = command.getMessageList();
                for (int i = 0; i < messagelist.length; ++i) {
                    System.out.println(messagelist[i].getText());
                }
                log.error("Fin ERROR AS400");
                system.disconnectService(AS400.COMMAND);
                throw new RemoteException("Error en l'execució del programa :"+Configuracion.getProperty("programaVPO")+" per l'usuari"+usuario);
            }
            system.disconnectService(AS400.COMMAND);
        } catch (Exception e) {
        	String programaVPO;
        	try{
        		programaVPO = Configuracion.getProperty("programaVPO");
        	}catch(Exception ex){
        		programaVPO="";
        	}
        	
            throw new RemoteException("Error en l'execució del programa :"+programaVPO,e);
        }
    }
    
    public static void ejecutarCSS(String tipo, String operacion, int cagco, int ano, int numen, int fent, int fdoc,
            String remit, int caggce, String proce, int corga, String tipdoc, String idioma, String extracto,
            String usuario, String password) throws RemoteException {
        
        try { 
            AS400 system = new AS400(Configuracion.getProperty("as400"), usuario, password);
            system.setGuiAvailable(false);
            CommandCall command = new CommandCall(system);
            LocalDataArea dataArea = new LocalDataArea(system);
            dataArea.clear();
            
            byte VPO[]=new byte[400];
            
            rellenaBytes("", VPO, 0, 400); // inicializamos el array a blancos
            
            int inicio=0;
            inicio=inicio+rellenaBytes(tipo, VPO, inicio, 1);
            inicio=inicio+rellenaBytes(operacion, VPO, inicio, 1);
            inicio=inicio+rellenaNumeros(cagco, VPO, inicio, 2);
            inicio=inicio+rellenaNumeros(ano, VPO, inicio, 4);
            inicio=inicio+rellenaNumeros(numen, VPO, inicio, 5);
            inicio=inicio+rellenaNumeros(fent, VPO, inicio, 8);
            inicio=inicio+rellenaNumeros(fdoc, VPO, inicio, 8);
            inicio=inicio+rellenaBytes(remit, VPO, inicio, 30);
            inicio=inicio+rellenaNumeros(caggce, VPO, inicio, 3);
            inicio=inicio+rellenaBytes(proce, VPO, inicio, 25);
            inicio=inicio+rellenaNumeros(corga, VPO, inicio, 4);
            inicio=inicio+rellenaBytes(tipdoc, VPO, inicio, 2);
            inicio=inicio+rellenaBytes(idioma, VPO, inicio, 1);
            inicio=inicio+rellenaBytes(extracto, VPO, inicio, 160);
            
            // Escribimos el area de datos
            dataArea.write(new String(VPO), 500, -1);
            
            if (command.run("call "+Configuracion.getProperty("biblioteca")+"/"+Configuracion.getProperty("programaCSS")) != true) {
                log.error("inicio ERROR AS400");
                AS400Message[] messagelist = command.getMessageList();
                for (int i = 0; i < messagelist.length; ++i) {
                    log.error(messagelist[i].getText());
                }
                log.error("Fin ERROR AS400");
                system.disconnectService(AS400.COMMAND);
                throw new RemoteException("Error en l'execució del programa :"+Configuracion.getProperty("programaCSS")+" per l'usuari"+usuario);
            }
            system.disconnectService(AS400.COMMAND);
        } catch (Exception e) {
        	String programaCSS;
        	try{
        		programaCSS = Configuracion.getProperty("programaCSS");
        	}catch(Exception ex){
        		programaCSS="";
        	}
            throw new RemoteException("Error en l'execució del programa :"+programaCSS,e);
        }
    }
    
    static int rellenaBytes(String cadena, byte[] buf, int offset, int longitud) {
        if (cadena==null) {
            cadena="";
        }
        
        StringBuffer nuevaCadena=new StringBuffer();
        int longitudCadena=cadena.length();
        for (int n=0;n<longitudCadena;n++) {
            nuevaCadena.append(cadena.substring(n,n+1));
            if (cadena.substring(n,n+1).equals("'")) {
                nuevaCadena.append("'");
                longitud++;
            }
        }
        cadena=nuevaCadena.toString();
        int cont=0;
        while (cont<longitud && cont<cadena.length()) {
            buf[offset+cont]=(byte)cadena.charAt(cont);
            cont++;
        }
        while (cont<longitud) {
            buf[offset+cont]= (byte)' ';
            cont++;
        }
        return longitud;
    }
    
    static int rellenaNumeros(int numero, byte[] buf, int offset, int longitud) {
        String numerChar=Integer.toString(numero);
        while (numerChar.length()<longitud) {
            //numerChar="0"+numerChar;
            numerChar=new StringBuffer().append("0").append(numerChar).toString();
        }
        
        int cont=0;
        while (cont<longitud && cont<numerChar.length()) {
            buf[offset+cont]=(byte)numerChar.charAt(cont);
            cont++;
        }
        return longitud;
    }
    
    /*
    public static String recuperarPassword(HttpServletRequest request) {
        String cadena=request.getHeader("authorization");
        String userPasswordEncry=cadena.substring(6);
        Base64 base = new Base64();
        String userPassword=base.decode(userPasswordEncry);
        return userPassword.substring(userPassword.indexOf(":")+1);
    }
    */
    
    // Realiza cierre seguro de resultset
    public static void closeRs(ResultSet rs){
    	try{
    		if (rs != null) rs.close();
    	}catch(Exception ex){
    		log.error("Error cerrando ResultSet: " + ex.getMessage(),ex);
    	}
    }
    
    
    //  Realiza cierre seguro de PreparedStatement
    public static void closePs(PreparedStatement ps){
    	try{
    		if (ps != null) ps.close();
    	}catch(Exception ex){
    		log.error("Error cerrando PreparedStatement: " + ex.getMessage(),ex);
    	}
    }
    
}
