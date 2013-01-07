package es.caib.pagos.persistence.delegate;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;

import es.caib.pagos.persistence.intf.SesionPagosFacadeLocal;
import es.caib.pagos.persistence.util.SesionPagosFacadeUtil;
import es.caib.sistra.plugins.pagos.DatosPago;
import es.caib.sistra.plugins.pagos.EstadoSesionPago;

public class SesionPagoDelegate implements Delegate 
{

	public void create(final String token)
			throws DelegateException
	{
		try
		{
			local = SesionPagosFacadeUtil.getLocalHome().create(token);
		} catch (CreateException e) {
	        throw new DelegateException(e);
	    } catch (EJBException e) {
	        throw new DelegateException(e);
	    } catch (NamingException e) {
	        throw new DelegateException(e);
	    }
	}
	

	public void destroy()
	{
		try {
            if (local != null) {
                local.remove();
            }
        } catch (EJBException e) {
            ;
        } catch (RemoveException e) {
            ;
        }

	}
	
	public DatosPago obtenerDatosPago() throws DelegateException
	{
		try 
        {
        	return local.obtenerDatosPago();
        } catch (EJBException e) 
        {
            throw new DelegateException(e);
        }
	}
	
	public synchronized EstadoSesionPago obtenerEstadoSesionPago() throws DelegateException
	{
		try 
        {
        	return local.obtenerEstadoSesionPago();
        } catch (EJBException e) 
        {
            throw new DelegateException(e);
        }
	}
	
	public String obtenerUrlRetornoSistra() throws DelegateException
	{
		try 
        {
        	return local.obtenerUrlRetornoSistra();
        } catch (EJBException e) 
        {
            throw new DelegateException(e);
        }
	}
	
	public String obtenerUrlMantenimientoSistra() throws DelegateException
	{
		try 
        {
        	return local.obtenerUrlMantenimientoSistra();
        } catch (EJBException e) 
        {
            throw new DelegateException(e);
        }
	}
	

	public synchronized int confirmarPago() throws DelegateException
	{
		try 
        {
        	return local.confirmarPago();
        } catch (EJBException e) 
        {
            throw new DelegateException(e);
        }
	}
	
	public int cancelarPagoTelematico() throws DelegateException
	{
		try 
        {
        	return local.cancelarPagoTelematico();
        } catch (EJBException e) 
        {
            throw new DelegateException(e);
        }
	}	
	
	public void cancelarPagoPresencial() throws DelegateException
	{
		try 
        {
        	local.cancelarPagoPresencial();
        } catch (EJBException e) 
        {
            throw new DelegateException(e);
        }
	}	
	
	public synchronized byte[] realizarPagoPresencial() throws DelegateException
	{
		try
		{
			return local.realizarPagoPresencial();
		} catch (EJBException e)
		{
			throw new DelegateException(e);
		}
	}
	
	public String realizarPagoTarjetaIniciarSesion()  throws DelegateException
	{
		try
		{
			return local.realizarPagoTarjetaIniciarSesion();
		} catch (EJBException e)
		{
			throw new DelegateException(e);
		} 	
	}
	
	public int realizarPagoTarjetaPagar(final String token, final String numeroTarjeta, final String caducidadTarjeta, final String titularTarjeta, final String cvvTarjeta) throws DelegateException
	{
		try
		{
			return local.realizarPagoTarjetaPagar(token, numeroTarjeta, caducidadTarjeta, titularTarjeta, cvvTarjeta);
		} catch (EJBException e)
		{
			throw new DelegateException(e);
		} 
	}
	
	public String realizarPagoBanca(final String codigoEntidad, final String urlVuelta) throws DelegateException
	{
		try
		{
			return local.realizarPagoBanca(codigoEntidad, urlVuelta);
		} catch (EJBException e)
		{
			throw new DelegateException(e);
		}
	}
	
	public byte[] obtenerJustificantePago() throws DelegateException
	{
		try
		{
			return local.obtenerJustificantePago();
		} catch (EJBException e)
		{
			throw new DelegateException(e);
		}
	}
	
	/* ========================================================= */
    /* ======================== REFERENCIA AL FACADE  ========== */
    /* ========================================================= */

    private SesionPagosFacadeLocal local;

    protected SesionPagoDelegate() 
    {
    }
	
}
