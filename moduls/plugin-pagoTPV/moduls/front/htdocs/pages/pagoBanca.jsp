<%@ page language="java" contentType="text/html; charset=ISO-8859-15" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"%>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic"%>
<%@ taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>

<script type="text/javascript">
<!--
	bindTPVOnLoad();
//-->
</script>

<bean:define id="urlPago" name="urlPago" type="es.caib.pagosTPV.model.UrlPagoTPV"/>


<h2>
	<bean:message key="pago.inicioTPV" />
</h2>

<!-- 
<p>
	<bean:message key="pago.instruccionesInicioTPV" />	
</p>
 -->

<p class="alerta">
	<bean:define id="aviso"><bean:message key="pago.avisoInicioTPV"/></bean:define>
	<bean:write name="aviso" filter="false"/>
	<br/><br/>
	<input type="button" onclick="javascript:confirmarPagoBanca();" value="<bean:message key="pago.confirmarTPV" />"/>	
</p>
	
<form id="frmTPV" action="<bean:write name="urlPago" property="urlPagoTPV"/>" method="POST" target="tpvFrame">
    <input type="hidden" name="Ds_SignatureVersion"        id="Ds_SignatureVersion"       value="HMAC_SHA256_V1" />
    <input type="hidden" name="Ds_MerchantParameters"      id="Ds_MerchantParameters"     value="<bean:write name="urlPago" property="merchantParameters"/>" />
    <input type="hidden" name="Ds_Signature"        	   id="Ds_Signature"       		  value="<bean:write name="urlPago" property="merchantSignature"/>" />    
</form>


<iframe name="tpvFrame" id="tpvFrame" class="iframeTpv" src="about:blank"></iframe>


<p class="volver">
	<a href="javascript:cancelarPagoBanca()">
		<bean:message key="pago.cancelarTPV" />
	</a>	
</p>

<!-- capa mensaje accediendo -->
<div id="capaInfoFondo"></div>
<div id="capaInfoForms"></div>



