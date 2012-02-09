<%@ page language="java" contentType="text/html; charset=ISO-8859-15" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"%>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic"%>
<%@ taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>

<bean:define id="idioma" value="<%=(java.lang.String) session.getAttribute(es.caib.pagos.front.Constants.DATOS_SESION_IDIOMA_KEY)%>" type="java.lang.String"/>
<bean:define id="asunto" value="<%=(java.lang.String) session.getAttribute(es.caib.pagos.front.Constants.DATOS_SESION_NOMBRE_TRAMITE_KEY)%>" type="java.lang.String"/>
<bean:define id="urlPago" type="java.lang.String">
        <html:rewrite page="/realizarPagoTarjeta" />
</bean:define>
<script type="text/javascript">
<!--
	var mensajeEnviando = '<bean:message key="pago.tarjeta.mensajePagando"/>';
//-->
</script>

<h2>	<bean:message key="pago.tarjetaBancaria" /></h2>

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

<p>
	<bean:message key="pago.tarjeta.continuarTramitacion" />
</p>
<p class="alerta">
		<bean:message key="pago.tarjeta.continuarTramitacion5" />
		<a href="reanudarPago.do">
		<bean:message key="pago.seleccionarPago" />
		</a>.
</p>
<p>
	<bean:message key="pago.tarjeta.continuarTramitacion2" />
</p>
<p>
	<bean:message key="pago.tarjeta.continuarTramitacion3" />
</p>

<p>
	<bean:message key="pago.tarjeta.continuarTramitacion4" />
</p>
<!-- /datos pago -->

<div id="tarjeta">
	<div id="pagoInstrucciones">
		<div id="errorsForm">
			<html:errors/>
		</div>
		<html:form action="/realizarPagoTarjeta">	
		<p>
			<label for="titularTarjeta">
				<span class="etiqueta"><bean:message key="pago.titularTarjeta"/></span>
				<html:text property="titularTarjeta" maxlength="50" size="25"/>						
			</label>
		</p>
		<p>
			<label for="numeroTarjeta">
				<span class="etiqueta"><bean:message key="pago.numeroTarjeta"/></span>
				<html:text property="numeroTarjeta" maxlength="16" size="17"/>						
			</label>
		</p>
		<p>
			<label for="fechaCaducidadTarjeta">
				<span class="etiqueta"><bean:message key="pago.fechaCaducidadTarjeta"/></span>
				<html:select property="mesCaducidadTarjeta">
					<html:option value="-">-</html:option>
					<html:options property="meses"/>
				</html:select>
				<html:select property="anyoCaducidadTarjeta">
					<html:option value="-">-</html:option>
					<html:options property="anyos"/>
				</html:select>
									
			</label>
		</p>
		<p>
			<label for="codigoVerificacionTarjeta">
				<span class="etiqueta"><bean:message key="pago.codigoVerificacionTarjeta"/></span>
				<html:text property="codigoVerificacionTarjeta" maxlength="3" size="4"/>						
			</label>
		</p>
		<div class="botonera">	
		<p>
		<html:submit onclick="accediendoEnviando(mensajeEnviando);">
			<bean:message key="pago.realizarPago"/>
		</html:submit>
		
		</p>
		
		</div>
		</html:form>
	</div>
</div>

<!-- capa mensaje accediendo -->
<div id="capaInfoFondo"></div>
<div id="capaInfoForms"></div>
<div class="sep"></div>


