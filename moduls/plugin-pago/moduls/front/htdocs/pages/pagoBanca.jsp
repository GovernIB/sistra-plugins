<%@page import="es.caib.pagos.persistence.delegate.DelegateUtil"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-15" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"%>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic"%>
<%@ taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>


<h2>	<bean:message key="pago.banca" /></h2>

<bean:define id="idioma" value="<%=(java.lang.String) session.getAttribute(es.caib.pagos.front.Constants.DATOS_SESION_IDIOMA_KEY)%>" type="java.lang.String"/>
<bean:define id="asunto" value="<%=(java.lang.String) session.getAttribute(es.caib.pagos.front.Constants.DATOS_SESION_NOMBRE_TRAMITE_KEY)%>" type="java.lang.String"/>

<script type="text/javascript">
<!--
	var mensajeEnviando = '<bean:message key="pago.banca.accediendoEntidad"/>';

	function pagoDesactivado() {
		alert("<bean:message key="pago.banca.pagoDesactivado"/>");
	}
	
//-->
</script>

<p>
	<bean:message key="pago.mostrar.datospago" />
</p>

<!-- datos pago -->
<div id="datosPago">
	<ul>
		<li><span class="label"><bean:message key="pago.modelo" />:</span> <span><bean:write name="modelo" /></span></li>
		<li><span class="label"><bean:message key="pago.concepto" />:</span> <span><bean:write name="concepto" /></span></li>
		<li><span class="label"><bean:message key="pago.fechaDevengo" />:</span> <span><bean:write name="fechaDevengo" /></span></li>
		<li><span class="label"><bean:message key="pago.importe" />:</span> <span><bean:write name="importe" /> &euro;</span></li>
	</ul>
</div>
<p>
	<bean:message key="pago.banca.continuarTramitacion" />
</p>
<p>
	<bean:message key="pago.banca.continuarTramitacion3" />
</p>
<p class="alerta">
	<bean:message key="pago.banca.continuarTramitacion4" />
</p>
<div id="banca">
	<div id="pagoInstrucciones">
		<html:form action="/realizarPagoBanca" styleId="pagoBancaForm">
			<p>
				<bean:message key="pago.banca.seleccion" />
			</p>
			<html:errors />
			<html:hidden property="banco" styleId="banco"/>
			
			<a href="<bean:write name="pagoEntidades" property="BM"/>"><img src="imgs/pagos/bancamarch.png" alt="Banca March"/></a>
			&nbsp;
			<a href="<bean:write name="pagoEntidades" property="LC"/>"><img src="imgs/pagos/lacaixa.png" alt="La Caixa"/></a>
			&nbsp;
			<a href="<bean:write name="pagoEntidades" property="SN"/>"><img src="imgs/pagos/sanostra.png" alt="BMN"/></a>
			&nbsp;
			<a href="<bean:write name="pagoEntidades" property="BB"/>"><img src="imgs/pagos/bbva.png" alt="BBVA"/></a>
			 
		</html:form>
	</div>
</div>
<p class="volver">
	<html:link href="reanudarPago.do">
		<bean:message key="pago.seleccionarPago" />
	</html:link>
</p>
<!-- capa mensaje accediendo -->
<div id="capaInfoFondo"></div>
<div id="capaInfoForms"></div>



