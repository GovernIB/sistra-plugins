package es.caib.regtel.plugincaib.persistence.delegate;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;

import es.caib.redose.modelInterfaz.ReferenciaRDS;
import es.caib.regtel.plugincaib.persistence.intf.RegistroWebEJB;
import es.caib.regtel.plugincaib.persistence.util.RegistroWebEJBUtil;
import es.caib.util.StringUtil;
import es.caib.xml.registro.factoria.impl.AsientoRegistral;
import es.caib.xml.registro.factoria.impl.Justificante;
import es.caib.sistra.plugins.regtel.ResultadoRegistro;
/**
 * Business delegate para operar con asistente pagos
 */
public class RegistroWebDelegate implements StatelessDelegate {

    /* ========================================================= */
    /* ======================== MÉTODOS DE NEGOCIO ============= */
    /* ========================================================= */
	
	public ResultadoRegistro registroEntrada(AsientoRegistral asiento,ReferenciaRDS refAsiento,Map refAnexos) throws DelegateException
	{	
		try {
        	return getFacade().registroEntrada(asiento, refAsiento, refAnexos);
		} catch (Exception e) {
        	throw new DelegateException(e);
    	}
	}
	
	public ResultadoRegistro confirmarPreregistro(String oficina,String codigoProvincia,String codigoMunicipio,String descripcionMunicipio,Justificante justificantePreregistro,ReferenciaRDS refJustificante,ReferenciaRDS refAsiento,Map refAnexos) throws DelegateException
	{
		try {
        	return getFacade().confirmarPreregistro(oficina, codigoProvincia, codigoMunicipio, descripcionMunicipio, justificantePreregistro, refJustificante, refAsiento, refAnexos);
		} catch (Exception e) {
        	throw new DelegateException(e);
    	}
	}

	public void anularRegistroEntrada(String numeroRegistro, Date fechaRegistro) throws DelegateException
	{
		try {
        	getFacade().anularRegistroEntrada(numeroRegistro, fechaRegistro );
		} catch (Exception e) {
        	throw new DelegateException(e);
    	}

	}
	
	public ResultadoRegistro registroSalida(AsientoRegistral asiento,ReferenciaRDS refAsiento,Map refAnexos) throws DelegateException
	{
		try {
        	return getFacade().registroSalida( asiento, refAsiento, refAnexos ) ;
		} catch (Exception e) {
        	throw new DelegateException(e);
    	}
	}
	
	public void anularRegistroSalida(String numeroRegistro, Date fechaRegistro) throws DelegateException
	{
		try {
        	getFacade().anularRegistroSalida(numeroRegistro, fechaRegistro) ;
		} catch (Exception e) {
        	throw new DelegateException(e);
    	}
	}
	
	public List obtenerOficinasRegistro() throws DelegateException
	{
		try {
        	return getFacade().obtenerOficinasRegistro();
		} catch (Exception e) {
        	throw new DelegateException(e);
    	}
	}
	
	public List obtenerOficinasRegistroUsuario(String usuario) throws DelegateException
	{
		try {
        	return getFacade().obtenerOficinasRegistroUsuario(usuario);
		} catch (Exception e) {
        	throw new DelegateException(e);
    	}
	}
	
	public List obtenerTiposAsunto() throws DelegateException
	{
		try {
        	return getFacade().obtenerTiposAsunto();
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
	

	public String obtenerDescripcionSelloOficina(String codigoOficina) throws DelegateException
	{
		try {
        	return getFacade().obtenerDescripcionSelloOficina(codigoOficina);
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
