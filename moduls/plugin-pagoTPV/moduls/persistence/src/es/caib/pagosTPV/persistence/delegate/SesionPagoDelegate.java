package es.caib.pagosTPV.persistence.delegate;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;

import es.caib.pagosTPV.model.DocumentoPagoPresencial;
import es.caib.pagosTPV.model.UrlPagoTPV;
import es.caib.pagosTPV.persistence.intf.SesionPagosTPVFacadeLocal;
import es.caib.pagosTPV.persistence.util.SesionPagosTPVFacadeUtil;
import es.caib.sistra.plugins.pagos.DatosPago;
import es.caib.sistra.plugins.pagos.EstadoSesionPago;

public class SesionPagoDelegate implements Delegate 
{

	public void create(final String token)
			throws DelegateException
	{
		try
		{
			local = SesionPagosTPVFacadeUtil.getLocalHome().create(token);
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
	

	public synchronized int confirmarPagoBanca() throws DelegateException
	{
		try 
        {
        	return local.confirmarPagoBanca();
        } catch (EJBException e) 
        {
            throw new DelegateException(e);
        }
	}
	
	public int cancelarPagoBanca() throws DelegateException
	{
		try 
        {
        	return local.cancelarPagoBanca();
        } catch (EJBException e) 
        {
            throw new DelegateException(e);
        }
	}	
	
	public UrlPagoTPV realizarPagoBanca() throws DelegateException
	{
		try
		{
			return local.realizarPagoBanca();
		} catch (EJBException e)
		{
			throw new DelegateException(e);
		}
	}
	
	public void realizarPagoPresencial() throws DelegateException
	{
		try
		{
			local.realizarPagoPresencial();
		} catch (EJBException e)
		{
			throw new DelegateException(e);
		}
	}
	
	public DocumentoPagoPresencial descargarDocumentoPagoPresencial() throws DelegateException
	{
		try
		{
			return local.descargarDocumentoPagoPresencial();
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
	
	/* ========================================================= */
    /* ======================== REFERENCIA AL FACADE  ========== */
    /* ========================================================= */

    private SesionPagosTPVFacadeLocal local;

    protected SesionPagoDelegate() 
    {
    }
	
}
