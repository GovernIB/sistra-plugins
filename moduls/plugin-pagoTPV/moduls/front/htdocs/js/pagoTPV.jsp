<%@ page language="java" contentType="text/javascript; charset=ISO-8859-15" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"%>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic"%>
<%@ taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>

<bean:define id="urlCancelarPagoBanca"><html:rewrite page="/cancelarPagoBanca.do"/></bean:define>
<bean:define id="urlCancelarPagoPresencial"><html:rewrite page="/cancelarPagoPresencial.do"/></bean:define>
<bean:define id="urlConfirmarPagoBanca"><html:rewrite page="/confirmarPagoBanca.do"/></bean:define>
<bean:define id="urlIniciarPagoBanca"><html:rewrite page="/iniciarPagoBanca.do"/></bean:define>
<bean:define id="urlIniciarPagoPresencial"><html:rewrite page="/iniciarPagoPresencial.do"/></bean:define>
<bean:define id="descargarDocumentoPagoPresencial"><html:rewrite page="/descargarDocumentoPagoPresencial.do"/></bean:define>
<bean:define id="urlRetornoInicioPago"><html:rewrite page="/retornoInicioPago.do"/></bean:define>
<bean:define id="urlRetornoSistra" value="<%=((java.lang.String) session.getAttribute(es.caib.pagosTPV.front.Constants.URL_RETORNO_SISTRA_KEY))%>" type="java.lang.String"/>

function bindTPVOnLoad() {
	$(document).ready(function(){
		muestraCapa('avisoContinuarTramite');		
		$("#frmTPV").submit();	
	});	
}

function bindRetornoAsistenteOnLoad() {
	$(document).ready(function(){
		accediendoEnviando("<bean:message key="pago.finalizado" />");
		volverAsistenteTramitacion();
	});
}

function cancelarPagoBanca() {
	if (confirm("<bean:message key="pago.confirmacionCancelarTPV" />")) {
		window.location = "<bean:write name="urlCancelarPagoBanca" filter="false"/>";
	}	
}

function confirmarPagoBanca() {
	window.location = "<bean:write name="urlConfirmarPagoBanca" filter="false"/>";	
}

function iniciarPagoBanca() {
	window.location = "<bean:write name="urlIniciarPagoBanca" filter="false"/>";		
}

function iniciarPagoPresencial() {
	window.location = "<bean:write name="urlIniciarPagoPresencial" filter="false"/>";		
}


function volverAsistenteTramitacion() {
	window.location = "<bean:write name="urlRetornoSistra" filter="false"/>";	
}

function volverInicioProcesoPago() {
	window.location = "<bean:write name="urlRetornoInicioPago" filter="false"/>";	
}

function continuarTramitacionPresencial() {
	if (confirm("<bean:message key="pago.presencial.continuarTramitacion.avisoDescarga" />")) {
		volverAsistenteTramitacion();
	}		
}

function cancelarPagoPresencial() {
	window.location = "<bean:write name="urlCancelarPagoPresencial" filter="false"/>";	
}

function descargarDocumentoPagoPresencial() {
	window.location = "<bean:write name="descargarDocumentoPagoPresencial" filter="false"/>";	
}


function testRetorno() {
	$("#tpvFrame").attr('src','retornoTPV.jsp');
}
