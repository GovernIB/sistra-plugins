<%@ page language="java" contentType="text/html; charset=ISO-8859-15" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"%>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic"%>
<%@ taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>

<h2>	<bean:message key="pago.efectuarPago" /></h2>

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
<!-- /datos pago -->

<p class="alerta">
	<bean:message key="pago.modificarFormularios" />
</p>
<p>
	<bean:message key="pago.formasDePago" />
</p>
<p>
	<bean:message key="pago.abrirAyuda" />
</p>

<!-- banca electronica -->
<logic:equal name="telematicoPermitido" value="true">
	<logic:equal name="presencialPermitido" value="true">
		<div id="pagoBEA">
	</logic:equal>
	<logic:equal name="presencialPermitido" value="false">
		<div id="pagoInstrucciones">
	</logic:equal>
	<h3><a class="destacat"><bean:message key="pago.instrucciones.telematicas" /></a></h3>
	<div class="instrucciones">
		<p><bean:message key="pago.telematico.instruccion2" /></p>
		<ul>
			<li><bean:message key="pago.telematico.instruccion4" /></li>
			<li><bean:message key="pago.telematico.instruccion3" /></li>
		</ul>
	</div>
	<div style="float:left">
	<html:form action="/iniciarPagoTarjeta">		
		<html:hidden property="modoPago" value="T"/>
		<html:submit>
			<bean:message key="pago.tarjetaBancaria"/>
		</html:submit>
	</html:form>
	</div>
	<div style="float:right">
	<html:form action="/iniciarPagoBanca">		
		<html:hidden property="modoPago" value="T"/>
		<html:submit>
			<bean:message key="pago.banca"/>
		</html:submit>
	</html:form>
	</div>
</div>

</logic:equal>
<!-- /banca electronica -->
	
<!-- forma presencial -->
<logic:equal name="presencialPermitido" value="true">
	<logic:equal name="telematicoPermitido" value="true">
		<div id="pagoFPA">
	</logic:equal>
	<logic:equal name="telematicoPermitido" value="false">
		<div id="pagoInstrucciones">
	</logic:equal>
	<h3><a class="destacat"><bean:message key="pago.instrucciones.presenciales" /></a></h3>
	<div class="instrucciones">
		<p><bean:message key="pago.presencial.instruccion1" /></p>
		<p><bean:message key="pago.presencial.instruccion3" /></p>
		<p><bean:message key="pago.presencial.instruccion4" /></p>
	</div>
	<div style="align:center">
	<html:form action="/iniciarPagoPresencial">
		<html:hidden property="modoPago" value="P"/>	
		<html:submit>
			<bean:message key="pago.presencial"/>
		</html:submit>
	</html:form>
	</div>
</div>
</logic:equal>
<!-- /forma presencial -->

<!-- llan�ar pagament -->
<div id="pagaments">
	
	<div>
		
	</div>
	
	
</div>
<!-- /llan�ar pagament -->

<!-- reader -->
<p id="getAdobeReader">
	<bean:message key="pago.acrobat.pdf" />
	<br />
	<a href="http://www.adobe.com/products/acrobat/readstep2.html?promoid=DAFYK" target="_blank"><bean:message key="pago.acrobat.ir" /></a>
</p>
<!-- /reader -->

<!--  Enlace volver  -->
<bean:define id="urlRetornoSistra" value="<%=((java.lang.String) session.getAttribute(es.caib.pagos.front.Constants.URL_RETORNO_SISTRA_KEY))%>" type="java.lang.String"/>
<p class="volver">
	<html:link href="<%=urlRetornoSistra%>">
		<bean:message key="pago.volver.asistenteTramitacion" />
	</html:link>
</p>
