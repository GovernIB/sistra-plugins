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
    <input type="hidden" name="Ds_Merchant_MerchantName"        id="Ds_Merchant_MerchantName"       value="<bean:write name="urlPago" property="merchantName"/>" />
    <input type="hidden" name="Ds_Merchant_MerchantCode"        id="Ds_Merchant_MerchantCode"       value="<bean:write name="urlPago" property="merchantCode"/>" />
    <input type="hidden" name="Ds_Merchant_MerchantData"        id="Ds_Merchant_MerchantData"       value="<bean:write name="urlPago" property="merchantData"/>" />
    <input type="hidden" name="Ds_Merchant_Terminal"            id="Ds_Merchant_Terminal"           value="<bean:write name="urlPago" property="merchantTerminal"/>" />
    <input type="hidden" name="Ds_Merchant_Order"               id="Ds_Merchant_Order"              value="<bean:write name="urlPago" property="merchantOrder"/>" />
    <input type="hidden" name="Ds_Merchant_Amount"              id="Ds_Merchant_Amount"             value="<bean:write name="urlPago" property="merchantAmount"/>" />
    <input type="hidden" name="Ds_Merchant_ProductDescription"  id="Ds_Merchant_ProductDescription" value="<bean:write name="urlPago" property="merchantProductDescription"/>" />
    <input type="hidden" name="Ds_Merchant_Titular"             id="Ds_Merchant_Titular"            value="<bean:write name="urlPago" property="merchantTitular"/>" />
    <input type="hidden" name="Ds_Merchant_Currency"            id="Ds_Merchant_Currency"           value="<bean:write name="urlPago" property="merchantCurrency"/>" />
    <input type="hidden" name="Ds_Merchant_TransactionType"     id="Ds_Merchant_TransactionType"    value="<bean:write name="urlPago" property="merchantTransactionTypeAut"/>" />
    <input type="hidden" name="Ds_Merchant_MerchantURL"         id="Ds_Merchant_MerchantURL"        value="<bean:write name="urlPago" property="merchantMerchantURL"/>" />
    <input type="hidden" name="Ds_Merchant_UrlOK"               id="Ds_Merchant_UrlOK"              value="<bean:write name="urlPago" property="merchantUrlOK"/>" />
    <input type="hidden" name="Ds_Merchant_UrlKO"               id="Ds_Merchant_UrlKO"              value="<bean:write name="urlPago" property="merchantUrlKO"/>" />
    <input type="hidden" name="Ds_Merchant_MerchantSignature"   id="Ds_Merchant_MerchantSignature"  value="<bean:write name="urlPago" property="merchantSignature"/>" />
    <input type="hidden" name="Ds_Merchant_ConsumerLanguage"    id="Ds_Merchant_ConsumerLanguage"   value="<bean:write name="urlPago" property="merchantConsumerLanguage"/>" />
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



