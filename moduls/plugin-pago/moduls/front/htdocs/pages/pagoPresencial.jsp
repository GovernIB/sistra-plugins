<%@ page language="java" contentType="text/html; charset=ISO-8859-15" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"%>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic"%>
<%@ taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>


<bean:define id="urlAbrirDocumento" type="java.lang.String">
	<html:rewrite page="/realizarPagoPresencial.do"/>
</bean:define>

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
	<bean:message key="pago.presencial.continuarTramitacion2" />
</p>

<p>
<bean:message key="pago.presencial.continuarTramitacion4"/>
</p>
<p class="centro">


<html:link href="<%=urlAbrirDocumento%>">
<img src="./imgs/icona/mode_presencial.gif" alt="" /> 
	<bean:message key="pago.documentoPago" />
</html:link>
</p>
<div id="pagaments">
	<div>
	</div>
</div>
<html:form action="/confirmarPago">		
	<html:hidden property="modoPago" value="P"/>
	<div class="botonera">
		<html:submit>
			<bean:message key="pago.continuarTramitacion"/>
		</html:submit>
	</div>
</html:form>
<p class="volver">
	<html:link href="reanudarPago.do">
		<bean:message key="pago.seleccionarPago" />
	</html:link>
</p>

