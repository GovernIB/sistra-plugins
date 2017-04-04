<%@ page language="java" contentType="text/html; charset=ISO-8859-15" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"%>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic"%>
<%@ taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>

<h2>
	<bean:message key="pago.presencial.titulo" />
</h2>

<p>
	<bean:message key="pago.presencial.instrucciones" />	
</p>

<p>
	<bean:message key="pago.presencial.instrucciones2" />	
</p>

<p>
	<bean:message key="pago.presencial.instrucciones3" />	
</p>

<p class="alerta">
	<bean:define id="aviso"><bean:message key="pago.avisoPresencial"/></bean:define>
	<bean:write name="aviso" filter="false"/>
	<br/><br/>
	<input type="button" onclick="javascript:descargarDocumentoPagoPresencial();" value="<bean:message key="pago.presencial.descargarDocumentoPago" />"/>
	&nbsp;	
	<input type="button" onclick="javascript:continuarTramitacionPresencial();" value="<bean:message key="pago.presencial.continuarTramitacion" />"/>
</p>
	

<p class="volver">
	<a href="javascript:cancelarPagoPresencial()">
		<bean:message key="pago.presencial.cancelar" />
	</a>	
</p>

<!-- capa mensaje accediendo -->
<div id="capaInfoFondo"></div>
<div id="capaInfoForms"></div>



