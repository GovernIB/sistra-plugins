package es.caib.pagos.front.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import es.caib.pagos.persistence.delegate.SesionPagoDelegate;
import es.caib.sistra.plugins.pagos.ConstantesPago;
import es.caib.sistra.plugins.pagos.DatosPago;


public class DatosPagoController extends BaseController
{

	public void execute(ComponentContext tileContext,
			HttpServletRequest request, HttpServletResponse response,
			ServletContext servletContext) throws Exception
	{
		// Prepara los datos del pago en el formato correcto		
		SesionPagoDelegate dlg = getSesionPago(request);
		
		// Obtenemos datos del pago
		DatosPago datosPago = dlg.obtenerDatosPago();
		
		es.caib.pagos.persistence.delegate.ConfiguracionDelegate delegateF = es.caib.pagos.persistence.delegate.DelegateUtil.getConfiguracionDelegate();
		
		String entidad1 = delegateF.obtenerPropiedad("pago.entidad.BM");
		String entidad2 = delegateF.obtenerPropiedad("pago.entidad.LC");
		String entidad3 = delegateF.obtenerPropiedad("pago.entidad.SN");
		String entidad4 = delegateF.obtenerPropiedad("pago.entidad.BB");
		Map pagoEntidades = new HashMap();
		pagoEntidades.put("BM", (entidad1 == null || entidad1.equals("true"))?"BM":"DS");
		pagoEntidades.put("LC", (entidad2 == null || entidad2.equals("true"))?"LC":"DS");
		pagoEntidades.put("SN", (entidad3 == null || entidad3.equals("true"))?"SN":"DS");
		pagoEntidades.put("BB", (entidad4 == null || entidad4.equals("true"))?"BB":"DS");
		
			
		// Importe (convertimos de cents)
		double impDec = Double.parseDouble(datosPago.getImporte()) / 100 ;	
		DecimalFormat f = (DecimalFormat) DecimalFormat.getInstance(getLocale(request));
		f.setDecimalSeparatorAlwaysShown(true);
		f.setMaximumFractionDigits(2);
		f.setMinimumFractionDigits(2);
		f.setMinimumIntegerDigits(1);
		String importe = f.format(impDec);

		// Fecha (convertimos desde Date)
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");               
	    String fechaDevengo = sdf.format( datosPago.getFechaDevengo() );
	    
	    boolean presencialPermitido = false;
	    boolean telematicoPermitido = false;
	    if(datosPago != null && datosPago.getTipoPago() == ConstantesPago.TIPOPAGO_AMBOS){
	    	presencialPermitido = true;
		    telematicoPermitido = true;
		    
	    }else{
	    	if(datosPago != null && datosPago.getTipoPago() == ConstantesPago.TIPOPAGO_PRESENCIAL){
	    		presencialPermitido = true;
		    }
		    if(datosPago != null && datosPago.getTipoPago() == ConstantesPago.TIPOPAGO_TELEMATICO){
		    	telematicoPermitido = true;
		    }
	    }
	    
	   	// Redirigimos a pagina de pago
	    request.setAttribute("modelo",datosPago.getModelo());
	    request.setAttribute("concepto",datosPago.getConcepto());
	    request.setAttribute("fechaDevengo",fechaDevengo);
	    request.setAttribute("importe",importe);
	    request.setAttribute("urlRetornoSistra",dlg.obtenerUrlRetornoSistra());
	    request.setAttribute("presencialPermitido",presencialPermitido);
	    request.setAttribute("telematicoPermitido",telematicoPermitido);
	    request.setAttribute("pagoEntidades", pagoEntidades);

	}

}
