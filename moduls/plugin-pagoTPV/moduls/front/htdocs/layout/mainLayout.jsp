<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-15" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"%>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic"%>
<%@ taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ca" lang="ca">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<title><bean:write name="<%=es.caib.pagosTPV.front.Constants.ORGANISMO_INFO_KEY%>" property="nombre" /></title>
	
	<!-- css -->
	<link href="css/estils.css" rel="stylesheet" type="text/css" media="screen" />
	<link href="css/estils_print.css" rel="stylesheet" type="text/css" media="print" />
	<logic:notEmpty name="<%=es.caib.pagosTPV.front.Constants.ORGANISMO_INFO_KEY%>" property="urlCssCustom">
		<link href="<bean:write name="<%=es.caib.pagosTPV.front.Constants.ORGANISMO_INFO_KEY%>" property="urlCssCustom" />" rel="stylesheet" type="text/css" />
	</logic:notEmpty>
	<!-- /css -->
	
	
	<script type="text/javascript" src="js/jquery-1.3.2.min.js"></script>
	<script type="text/javascript" src="js/globales.js"></script>
	<script type="text/javascript" src="js/comuns.js"></script>
	<script type="text/javascript" src="js/pagoTPV.jsp?ts=<%System.currentTimeMillis()%>"></script>
	<!-- DETECCION NAVEGADOR (Compatibles: IE >=6 , FireFox >= 1.5)-->
	<script language="javascript" type="text/javascript">
	<!--
		var errorIE="La versión mínima de Internet Explorer para que pueda utilizar la plataforma es la versión 6. Debe actualizar la versión de su navegador.";
		var errorFirefox="La versión mínima de Firefox para que pueda utilizar la plataforma es la versión 1.5. Debe actualizar la versión de su navegador.";
		checkVersionNavegador(errorIE,errorFirefox);
	-->
	</script>
</head>

<script type="text/javascript">
<!--
	var mensajeAlerta = '<bean:message key="pago.presencial.mensajeDescargar"/>';
//-->
</script>

<logic:present name="mostrarAlerta">
<body class="imc-plataforma-pago" onload="mostrarMensajeAlerta(mensajeAlerta);">
</logic:present>
<logic:notPresent name="mostrarAlerta">
<body class="imc-plataforma-pago">
</logic:notPresent>
<div id="contenidor">

	<!-- capsal -->
	<tiles:insert name="header"/>		
	<!-- continguts -->
	<div id="continguts">
		<tiles:insert name="main"/>
	</div>
	<tiles:insert name="footer"/>
</div>
</body>

