<%@ page language="java" contentType="text/html; charset=ISO-8859-15" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"%>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic"%>
<%@ taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>
<bean:define id="locale" name="org.apache.struts.action.LOCALE" scope="session" />


<!-- informacio -->
<p class="alerta">	
	<bean:message name="texto" />
	<logic:notEmpty name="respuesta">
		<bean:write name="respuesta" />
	</logic:notEmpty>
</p>

<!--  Enlace volver  -->
<%if(session.getAttribute(es.caib.pagosTPV.front.Constants.URL_RETORNO_SISTRA_KEY) != null && !"".equals(session.getAttribute(es.caib.pagosTPV.front.Constants.URL_RETORNO_SISTRA_KEY))){%>
<bean:define id="urlRetornoSistra" value="<%=((java.lang.String) session.getAttribute(es.caib.pagosTPV.front.Constants.URL_RETORNO_SISTRA_KEY))%>" type="java.lang.String"/>
<p class="volver">
	<html:link href="<%=urlRetornoSistra%>">
		<bean:message key="pago.volver.asistenteTramitacion" />
	</html:link>
</p>
<%}%>