<%@ page language="java" contentType="text/html; charset=ISO-8859-15" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"%>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic"%>
<%@ taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>

<h2>	<bean:message key="pago.banca" /></h2>

<bean:define id="idioma" value="<%=(java.lang.String) session.getAttribute(es.caib.pagos.front.Constants.DATOS_SESION_IDIOMA_KEY)%>" type="java.lang.String"/>
<bean:define id="asunto" value="<%=(java.lang.String) session.getAttribute(es.caib.pagos.front.Constants.DATOS_SESION_NOMBRE_TRAMITE_KEY)%>" type="java.lang.String"/>


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
	<bean:message key="pago.banca.continuarTramitacion2" />
</p>
<p class="alerta">
		<bean:message key="pago.banca.continuarTramitacion5" />
		<a href="reanudarPago.do">
		<bean:message key="pago.seleccionarPago" />
		</a>.
</p>
<p>
	<bean:message key="pago.banca.continuarTramitacion4" />
</p>
<div id="banca">
	<div id="pagoInstrucciones">
		<html:form action="/realizarPagoBanca">
			<p>
				<bean:message key="pago.banca.seleccion" />
			</p>
			<html:errors />
			<html:radio property="banco" value="BM"><bean:message key="pago.banca.march" /></html:radio>
			<html:radio property="banco" value="LC"><bean:message key="pago.banca.lacaixa" /></html:radio>
			<html:radio property="banco" value="SN"><bean:message key="pago.banca.sanostra" /></html:radio>
			<div class="botonera">		
			<html:submit>
				<bean:message key="pago.realizarPago"/>
			</html:submit>
			</div>
		</html:form>
	</div>
</div>

