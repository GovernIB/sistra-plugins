package es.caib.regtel.plugincaib.persistence.delegate;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import es.caib.regtel.plugincaib.persistence.intf.RegistroWebEJB;
import es.caib.regtel.plugincaib.persistence.util.RegistroWebEJBUtil;
import es.caib.regtel.plugincaib.model.DatosRegistroSalida;
import es.caib.regtel.plugincaib.model.ExcepcionRegistroWeb;
import es.caib.regtel.plugincaib.persistence.dao.registro.RegistroDAO;
import es.caib.regtel.plugincaib.persistence.dao.registro.RegistroDAOFactory;
import es.caib.util.StringUtil;
import es.caib.sistra.plugins.regtel.ResultadoRegistro;
/**
 * Business delegate para operar con asistente pagos
 */
public class RegistroWebDelegate implements StatelessDelegate {

    /* ========================================================= */
    /* ======================== MÉTODOS DE NEGOCIO ============= */
    /* ========================================================= */
	
	public es.caib.sistra.plugins.regtel.ResultadoRegistro registroEntrada(es.caib.regtel.plugincaib.model.DatosRegistroEntrada entrada) throws DelegateException
	{	
		try {
        	return getFacade().registroEntrada(entrada);
		} catch (Exception e) {
        	throw new DelegateException(e);
    	}
	}
	
	public es.caib.sistra.plugins.regtel.ResultadoRegistro registroSalida( DatosRegistroSalida salida )  throws DelegateException
	{
		try {
        	return getFacade().registroSalida(salida);
		} catch (Exception e) {
        	throw new DelegateException(e);
    	}
	}

	public void anularRegistroEntrada( String usuario, String oficina, String numeroEntrada, String anyoEntrada ) throws DelegateException
	{
		try {
        	getFacade().anularRegistroEntrada(usuario,oficina,numeroEntrada,anyoEntrada );
		} catch (Exception e) {
        	throw new DelegateException(e);
    	}

	}
	
	public List obtenerOficinas( String usuario, String autorizacion ) throws DelegateException
	{
		try {
        	return getFacade().obtenerOficinas( usuario,autorizacion ) ;
		} catch (Exception e) {
        	throw new DelegateException(e);
    	}
	}
	public List obtenerOficinas()  throws DelegateException
	{
		try {
        	return getFacade().obtenerOficinas() ;
		} catch (Exception e) {
        	throw new DelegateException(e);
    	}
	}
	public List obtenerDocumentos() throws DelegateException
	{
		try {
        	return getFacade().obtenerDocumentos();
		} catch (Exception e) {
        	throw new DelegateException(e);
    	}
	}
	
	public List obtenerIdiomas() throws DelegateException
	{
		try {
        	return getFacade().obtenerIdiomas();
		} catch (Exception e) {
        	throw new DelegateException(e);
    	}
	}
	
	public List obtenerMunicipiosBaleares() throws DelegateException
	{
		try {
        	return getFacade().obtenerMunicipiosBaleares();
		} catch (Exception e) {
        	throw new DelegateException(e);
    	}
	}
	
	public String obtenerDescripcionOficina ( String codigoOficina ) throws DelegateException
	{
		try {
        	return getFacade().obtenerDescripcionOficina(codigoOficina);
		} catch (Exception e) {
        	throw new DelegateException(e);
    	}
	}
	
	public String obtenerFecha() throws DelegateException
	{
		try {
        	return getFacade().obtenerFecha();
		} catch (Exception e) {
        	throw new DelegateException(e);
    	}
	}
	
	public String obtenerHorasMinutos() throws DelegateException
	{
		try {
        	return getFacade().obtenerHorasMinutos();
		} catch (Exception e) {
        	throw new DelegateException(e);
    	}
	}
	
	public List obtenerServiciosDestino() throws DelegateException
	{
		try {
        	return getFacade().obtenerServiciosDestino();
		} catch (Exception e) {
        	throw new DelegateException(e);
    	}
	}
	
	
    /* ========================================================= */
    /* ======================== REFERENCIA AL FACADE  ========== */
    /* ========================================================= */
    private RegistroWebEJB getFacade() throws NamingException,CreateException,RemoteException {      	    	
    	return RegistroWebEJBUtil.getHome().create();
    }

    protected RegistroWebDelegate() throws DelegateException {       
    }

	               
}
