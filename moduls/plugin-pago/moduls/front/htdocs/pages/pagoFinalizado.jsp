<%@ page language="java" contentType="text/html; charset=ISO-8859-15" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"%>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic"%>
<%@ taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>

<bean:define id="urlAbrirDocumento" type="java.lang.String">
	<html:rewrite page="/obtenerJustificantePago.do"/>
</bean:define>

<h2><bean:message key="pago.resultado"/></h2>

<!--  Pago no realizado -->
<logic:notEqual name="resultadoPago" value="1">
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
			<input id="btnConfirmarPago" type="button" value="<bean:message key="continuarTramitacion"/>" onclick="confirmarPago(this.form);"/>
		</div>
	</html:form>
</logic:notEqual>

<!--  Pago realizado -->
<logic:equal name="resultadoPago" value="1">
	<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
	<!-- capa mensaje continuar tramitacion -->
	<div id="capaInfoFondo"></div>
	<div id="capaFinPago">

			<p class="apartado">
				<strong><bean:message key="pago.tarjeta.resultadoOK" /></strong>
				<br>
				<br>
				<bean:message key="pago.tarjeta.debeDescargarJustificante" />
				<img alt="" src="./imgs/icona/mode_presencial.gif">
				<a class="veure" href="<%=urlAbrirDocumento%>">
					<bean:message key="pago.tarjeta.justificante" />
				</a>
				<br>
				<br>
				<bean:message key="pago.tarjeta.debeContinuarTramitacion" />
				<br>
			</p>

			<html:form action="/confirmarPago">
				<html:hidden property="modoPago" value="T"/>
				<div class="botonera">
					<input id="btnConfirmarPago" type="button" value="<bean:message key="pago.registrarTramite"/>" onclick="confirmarPago(this.form);"/>
				</div>
			</html:form>

	</div>

	<script type="text/javascript">
		<!--
		$( document ).ready(function() {
			mostrandoCapa('capaFinPago');
		});
		//-->

		// Enviamos formulario deshabilitando boton
		function confirmarPago(form) {
			document.getElementById("btnConfirmarPago").disabled = true;
			document.getElementById("btnConfirmarPago").value = "<bean:message key="pago.tarjeta.debeContinuarTramitacion" />";
			form.submit();
		}

	</script>


</logic:equal>
