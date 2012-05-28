<%@ page language="java" contentType="text/html; charset=ISO-8859-15" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"%>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic"%>
<%@ taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>

<bean:define id="idioma" value="<%=(java.lang.String) session.getAttribute(es.caib.pagos.front.Constants.DATOS_SESION_IDIOMA_KEY)%>" type="java.lang.String"/>
<bean:define id="asunto" value="<%=(java.lang.String) session.getAttribute(es.caib.pagos.front.Constants.DATOS_SESION_NOMBRE_TRAMITE_KEY)%>" type="java.lang.String"/>
	
<script type="text/javascript">	
	bloqueador = 0;
<logic:present name="urlAcceso">
	ventanaPrueba = window.open("<bean:write name="urlAcceso"  filter="false"/>");
	if(!ventanaPrueba) bloqueador = 1;
	else bloqueador = 0;
</logic:present>
</script>		

 <script type="text/javascript">
	<!--
	texto = "<p class='error'><bean:message key="pago.telematico.instruccionesEmergentes" /></p>";
	if(bloqueador == 1) document.write(texto);
	-->
</script>


<h2><bean:message key="pago.banca" /></h2>

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
	<bean:message key="pago.telematico.continuarTramitacion" />
</p>
	
<html:form action="/confirmarPago">		
	<html:hidden property="modoPago" value="T"/>
	<p class="alerta">
		<bean:message key="pago.telematico.cofirmarPago.soporteTecnico" arg0="<%=idioma%>" arg1="<%=asunto%>"/>
		<br /><br />
		<bean:message key="pago.telematico.cofirmarPago.soporteTecnico2" />
		<a href="cancelarPagoTelematico.do">
		<bean:message key="pago.telematico.cofirmarPago.soporteTecnico3" />
		</a>.
	</p>
	<div class="botonera">
		<html:submit>
			<bean:message key="pago.continuarTramitacion"/>
		</html:submit>
	</div>
</html:form>
