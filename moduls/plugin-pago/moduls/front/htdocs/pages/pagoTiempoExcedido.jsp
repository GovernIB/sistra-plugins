<%@ page language="java" contentType="text/html; charset=ISO-8859-15" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"%>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic"%>
<%@ taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>

<h2>	<bean:message key="pagoExcedido.titulo" /></h2>

<!-- informacio -->
<p class="alerta">	
	
	<!--  Mensaje particularizado tiempo excedido -->
	<logic:notEmpty name="<%=es.caib.pagos.front.Constants.MESSAGE_KEY%>" scope="request">
		<bean:write name="<%=es.caib.pagos.front.Constants.MESSAGE_KEY%>" scope="request" />
	</logic:notEmpty>
	
	<!--  Mensaje estandar tiempo excedido -->
	<logic:empty name="<%=es.caib.pagos.front.Constants.MESSAGE_KEY%>" scope="request">
		<bean:message key="pagoExcedido.mensaje" />
	</logic:empty>
	
</p>


<html:form action="/confirmarPago">		
	<html:hidden property="modoPago" value="T"/>
	<div class="botonera">
		<html:submit>
			<bean:message key="pago.continuarTramitacion"/>
		</html:submit>
	</div>
</html:form>

