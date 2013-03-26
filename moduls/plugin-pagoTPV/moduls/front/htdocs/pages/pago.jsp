<%@ page language="java" contentType="text/html; charset=ISO-8859-15" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"%>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic"%>
<%@ taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>

<h2>	<bean:message key="pago.efectuarPago" /></h2>

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
<!-- /datos pago -->

<p class="alerta">
	<bean:message key="pago.modificarFormularios" />
</p>

<br/>

<logic:equal name="telematicoPermitido" value="true">
	<logic:equal name="presencialPermitido" value="true">
		<p>
		<bean:message key="pago.formasDePago.telematicaPresencial" />
		</p>
		<br/>			
	</logic:equal>
</logic:equal>


<logic:equal name="telematicoPermitido" value="true">
	<h3><bean:message key="pago.telematico" /></h3>
	<p>
		<bean:message key="pago.instruccionesTPV" />	
	</p>
	<div class="botonera">
		<input type="button" onclick="iniciarPagoBanca();" value="<bean:message key="pago.inicioTPV"/>" />	
	</div>
	<br/>		
</logic:equal>

<logic:equal name="presencialPermitido" value="true">
	<h3><bean:message key="pago.presencial" /></h3>
	<p>
		<bean:message key="pago.instruccionesPresencial" />	
	</p>
	<div class="botonera">
		<input type="button" onclick="iniciarPagoPresencial();" value="<bean:message key="pago.inicioPresencial"/>" />	
	</div>
	<br/>		
</logic:equal>

<br/>

<!-- reader 
<p id="getAdobeReader">
	<bean:message key="pago.acrobat.pdf" />
	<br />
	<a href="http://www.adobe.com/products/acrobat" target="_blank"><bean:message key="pago.acrobat.ir" /></a>
</p>
-->
<!-- /reader -->

<!--  Enlace volver  -->
<p class="volver">
	<html:link href="javascript:volverAsistenteTramitacion();">
		<bean:message key="pago.volver.asistenteTramitacion" />
	</html:link>
</p>
