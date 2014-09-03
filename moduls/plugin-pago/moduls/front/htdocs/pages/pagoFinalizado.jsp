<%@ page language="java" contentType="text/html; charset=ISO-8859-15" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"%>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic"%>
<%@ taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>

<bean:define id="urlAbrirDocumento" type="java.lang.String">
	<html:rewrite page="/obtenerJustificantePago.do"/>
</bean:define>

<h2>	<bean:message key="pago.resultado" /></h2>

<logic:equal name="resultadoPago" value="1">	
	<p class="alerta">
		<bean:message key="pago.tarjeta.debeContinuarTramitacion" />
	</p>
</logic:equal>

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

<div> 
	<logic:equal name="resultadoPago" value="1">
	<p>
		<bean:message key="pago.tarjeta.resultadoOK" />
		<bean:message key="pago.tarjeta.descargaJustificante" />
	</p>
	<p class="centro">	<a class="veure" href="<%=urlAbrirDocumento%>">
			<img src="./imgs/icona/mode_presencial.gif" alt="" /> 
			<bean:message key="pago.tarjeta.justificante" />
		</a>
	</p>
	</logic:equal>
	<logic:equal name="resultadoPago" value="0">
	<p>
		<bean:message key="pago.tarjeta.resultadoNK" />
	</p>	
	</logic:equal>
	<logic:equal name="resultadoPago" value="-1">
	<p>
		<bean:message key="pago.tarjeta.errorComunicacion" />
		<bean:message key="pago.tarjeta.continuarTramitacion6" />
	</p>
	</logic:equal>
</div>
<br/><br/>
<html:form action="/confirmarPago">		
	<html:hidden property="modoPago" value="T"/>
	<div class="botonera">
		<html:submit>
			<bean:message key="pago.continuarTramitacion"/>
		</html:submit>
	</div>
</html:form>

