<%@ page language="java" contentType="text/html; charset=ISO-8859-15" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"%>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic"%>
<%@ taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>

<script type="text/javascript">
	bloqueador = 0;
<logic:present name="urlAcceso">
	ventanaPrueba = window.open("<bean:write name="urlAcceso" filter="false" />");
	if(!ventanaPrueba) bloqueador = 1;
	else bloqueador = 0;	
</logic:present>
</script>

<script type="text/javascript">
	<!--
	texto = "<p class='error'><bean:message key="pago.presencial.instruccionesEmergentes" /></p>";
	if(bloqueador == 1) document.write(texto);
	-->
</script>


<h2><bean:message key="pago.presencial" /></h2>
		

<!-- datos pago -->
<div id="datosPago">
	<ul>
		<li><span class="label"><bean:message key="pago.modelo" />:</span> <span><bean:write name="modelo" /></span></li>
		<li><span class="label"><bean:message key="pago.concepto" />:</span> <span><bean:write name="concepto" /></span></li>
		<li><span class="label"><bean:message key="pago.fechaDevengo" />:</span> <span><bean:write name="fechaDevengo" /></span></li>
		<li><span class="label"><bean:message key="pago.importe" />:</span> <span><bean:write name="importe" /> &euro;</span></li>
	</ul>
</div>
<!-- /datos pago -->

<p>
	<bean:message key="pago.presencial.continuarTramitacion" />
</p>

<p>
	<bean:message key="pago.presencial.noSePreocupe" />
</p>

<html:form action="/confirmarPago">		
	<html:hidden property="modoPago" value="P"/>
	<p class="alerta">
		<bean:message key="pago.presencial.cancelarPago1" />
		<a href="cancelarPagoPresencial.do">
		<bean:message key="pago.presencial.cancelarPago2" />
		</a>.
	</p>
	<div class="botonera">
		<html:submit>
			<bean:message key="pago.confirmarPagoPresencial"/>
		</html:submit>
	</div>
</html:form>

<!--  Enlace volver  -->
<bean:define id="urlRetornoSistra" value="<%=((java.lang.String) session.getAttribute(es.caib.pagos.front.Constants.URL_RETORNO_SISTRA_KEY))%>" type="java.lang.String"/>
<p class="volver">
	<html:link href="<%=urlRetornoSistra%>">
		<bean:message key="pago.volver.asistenteTramitacion" />
	</html:link>
</p>

