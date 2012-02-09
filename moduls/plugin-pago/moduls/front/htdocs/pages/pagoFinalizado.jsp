<%@ page language="java" contentType="text/html; charset=ISO-8859-15" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"%>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic"%>
<%@ taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>

<h2>	<bean:message key="pago.resultado" /></h2>

<!-- 
<p id="pagamentTitol">
	<bean:message key="pago.titulo" />
</p>
 -->

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
<bean:define id="urlRetornoSistra" value="<%=((java.lang.String) session.getAttribute(es.caib.pagos.front.Constants.URL_RETORNO_SISTRA_KEY))%>" type="java.lang.String"/>
<div>
<p>
	<logic:equal name="resultadoPago" value="1">
		<bean:message key="pago.tarjeta.resultadoOK" />
		<bean:message key="pago.tarjeta.salirAsistente" />
		<!--  Enlace volver  -->
	<html:link href="<%=urlRetornoSistra%>">
		<bean:message key="pago.volver.asistenteTramitacion" />
	</html:link>

	</logic:equal>
	<logic:equal name="resultadoPago" value="0">
		<bean:message key="pago.tarjeta.resultadoNK" />
		<bean:message key="pago.tarjeta.salirAsistente" />
			<html:link href="<%=urlRetornoSistra%>">
		<bean:message key="pago.volver.asistenteTramitacion" />
	</html:link>
	</logic:equal>
	<logic:equal name="resultadoPago" value="-1">
		<bean:message key="pago.tarjeta.errorComunicacion" />
		<bean:message key="pago.telematico.continuarTramitacion" />
	</logic:equal>
</p>

</div>
<logic:equal name="resultadoPago" value="-1">
<html:form action="/confirmarPago">		
	<html:hidden property="modoPago" value="T"/>
	<div class="botonera">
		<html:submit>
			<bean:message key="pago.confirmarPagoTelematico"/>
		</html:submit>
	</div>
</html:form>
</logic:equal>

