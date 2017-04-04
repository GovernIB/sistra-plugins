<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"%>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic"%>
<%@ taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>

<bean:define id="mensaje" name="mensaje" type="java.lang.String"/>

<head>
   <title><bean:message key="mensaje.titulo"/></title>
   <meta http-equiv="Content-type" content='text/html; charset="ISO-8859-1"' />
   <link rel="stylesheet" href='<html:rewrite page="/css/styleA.css"/>' type="text/css" />
   <script src="<html:rewrite page='/moduls/staticJs.jsp'/>" type="text/javascript"></script>
   <script src="<html:rewrite page='/moduls/funcions.js'/>" type="text/javascript"></script>
   <script type="text/javascript">
   <!--
        <logic:present name="reloadMenu">
            parent.Menu.location.reload(true);
        </logic:present>
   //-->
   </script>
</head>

<body class="ventana">
	
	<br/><br/><br/>
	
	<table class="marc">
    	<tr>
    		<td class="titulo">
	        	<bean:message key="mensaje.titulo"/>
	    	</td>	    	
	    </tr>
	    <tr>	 
	    	<td class="alert">
	    		<p>
	        		<bean:message key="<%=mensaje%>"/>
	        	</p>
	    	</td>
	    </tr>   
	</table>
	
    <br/>
    
    <a href="javascript:history.back(1)" class="boton"><bean:message key="mensaje.volver"/></a>
    
</body>