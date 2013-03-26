<%@ page language="java" contentType="text/html; charset=ISO-8859-15" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"%>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic"%>
<%@ taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>

<bean:define id="numpedido" name="identificador" type="java.lang.String"/>
<bean:define id="texto1"><bean:message key="pago.noRealizado.noPagado" arg0="<%=numpedido%>"/></bean:define>
<bean:define id="texto2"><bean:message key="pago.noRealizado.noPagado2"/></bean:define>
<bean:define id="texto3"><bean:message key="pago.noRealizado.noPagado3"/></bean:define>

<h2><bean:message key="pago.noRealizado.titulo" /></h2>

<p>
	<bean:write name="texto1" filter="false" />
</p>

<p class="alerta">
	<bean:write name="texto3" filter="false" />
</p>

<p>
	<bean:write name="texto2" filter="false" />
</p>

<div class="botonera">
	<input type="button" onclick="javascript:cancelarPagoBanca();" value="<bean:message key="pago.noRealizado.cancelarPago" />"/>		
</div>

<br/><br/>
<!--  Enlace volver  -->
<p class="volver">
	<html:link href="javascript:volverAsistenteTramitacion();">
		<bean:message key="pago.volver.asistenteTramitacion" />
	</html:link>
</p>
