<%@ page language="java" contentType="text/html; charset=ISO-8859-15" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"%>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic"%>
<%@ taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>

<h2><bean:message key="pago.KO.titulo" /></h2>

<p class="alerta">
	<bean:message key="pago.KO.aviso"/>
</p>

<br/><br/>

<!--  Enlace volver  -->
<p class="volver">
	<html:link href="javascript:volverInicioProcesoPago();">
		<bean:message key="pago.KO.continuar" />
	</html:link>
</p>
