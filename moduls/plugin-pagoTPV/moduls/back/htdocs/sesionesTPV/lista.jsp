<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" %>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="es.caib.sistra.plugins.pagos.ConstantesPago"%>
<%@ page import="es.caib.pagosTPV.back.Constants"%>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"%>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic"%>
<%@ taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>

<bean:define id="firstPage" value="0" />

<head>
   <title><bean:message key="sesionesTPV.title"/></title>
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
<table class="marc">
    <tr><td class="titulo">
        <bean:message key="sesionesTPV.selec" />
    </td></tr>
    <tr><td class="subtitulo"><bean:message key="sesionesTPV.selec.subtitulo" /></td></tr>
</table>

<br/>

<table class="marc">
    <tr>
    	<td class="input">
    	    <form action="lista.do">
				<input type="hidden" name="pagina" value="0"/>
				<strong>
					<bean:message key="sesionesTPV.nif"/> : 
				</strong>
				<logic:notEmpty name="<%=Constants.NIF_KEY%>" scope="session">
					<bean:define id="nifBusqueda" name="<%=Constants.NIF_KEY%>" scope="session" type="java.lang.String"/>
					<input class="data" type="text" name="nif" value="<%=nifBusqueda%>"/>
				</logic:notEmpty>
				<logic:empty name="<%=Constants.NIF_KEY%>" scope="session">
					<input class="data" type="text" name="nif" value=""/>
				</logic:empty>
				<input class="button" type="submit" value="<bean:message key="boton.busca"/>" />	
			</form>
    	</td>
    </tr>
</table>

<br />


<logic:empty name="page" property="list">
    <table class="marc">
      <tr><td class="alert"><bean:message key="sesionesTPV.selec.vacio" /></td></tr>
    </table>
</logic:empty>

<logic:notEmpty name="page" property="list">
    <table class="marc">
    	<tr>
			<th><bean:message key="sesionesTPV.fecha"/></th>	
			<th><bean:message key="sesionesTPV.localizador"/></th>
			<th><bean:message key="sesionesTPV.tramite"/></th>
			<th><bean:message key="sesionesTPV.sesionTramitacion"/></th>
			<th><bean:message key="sesionesTPV.estado"/></th>
			<th><bean:message key="sesionesTPV.orden"/></th>
			<th><bean:message key="sesionesTPV.nif"/></th>
			<th><bean:message key="sesionesTPV.nombre"/></th>
			
		</tr>
			<logic:iterate id="sesionTPV" name="page" property="list">	
            <tr>
            	<td class="outputd">
            		<bean:write name="sesionTPV" property="fechaDevengo" format="dd/MM/yyyy"/>
            	</td>
            	<td class="outputd">
            		<bean:write name="sesionTPV" property="localizador"/>
            	</td>
                <td class="outputd">
            		<bean:write name="sesionTPV" property="modeloTramite"/> - V<bean:write name="sesionTPV" property="versionTramite"/>
            	</td>
            	<td class="outputd">
            		<bean:write name="sesionTPV" property="identificadorTramite"/>
            	</td>
                <td class="outputd">
                	<logic:equal name="sesionTPV" property="estado" value="<%=Integer.toString(ConstantesPago.SESIONPAGO_PAGO_PENDIENTE_CONFIRMAR)%>">
                		<bean:message key="sesionesTPV.pendienteConfirmar" />
                	</logic:equal>
                	<logic:equal name="sesionTPV" property="estado" value="<%=Integer.toString(ConstantesPago.SESIONPAGO_PAGO_CONFIRMADO)%>">
                		<bean:message key="sesionesTPV.confirmado" />
                	</logic:equal>            		
            	</td>
            	<td class="outputd">
            		<bean:write name="sesionTPV" property="identificadorPago"/>
            	</td>
            	<td class="outputd">
            		<bean:write name="sesionTPV" property="nifDeclarante"/>
            	</td> 
            	<td class="outputd">
            		<bean:write name="sesionTPV" property="nombreDeclarante"/>
            	</td>     
            	<td align="right">
            		<logic:equal name="sesionTPV" property="estado" value="<%=Integer.toString(ConstantesPago.SESIONPAGO_PAGO_PENDIENTE_CONFIRMAR)%>">
                		<bean:define id="urlConfirmar"><html:rewrite page="/back/sesionesTPV/confirmarSesionTPV.do" paramId="localizador" paramName="sesionTPV" paramProperty="localizador"/></bean:define>
                    	<button class="button" type="button" onclick="forward('<%=urlConfirmar%>')"><bean:message key="sesionesTPV.confirmar" /></button>
                	</logic:equal>                    
                </td>                                                       	            	
            </tr>
        </logic:iterate>        
    </table>
    <div id="barraNav">
		<logic:equal name="page" property="previousPage" value="true">
		&lt;&lt; <html:link action="/back/sesionesTPV/lista" paramId="pagina" paramName="firstPage"><bean:message key="sesionesTPV.paginacion.inicio" /></html:link> &lt; <html:link action="/back/sesionesTPV/lista" paramId="pagina" paramName="page" paramProperty="previousPageNumber"><bean:message key="sesionesTPV.paginacion.anterior" /></html:link>
		</logic:equal> 
		- Del <bean:write name="page" property="firstResultNumber" /> al <bean:write name="page" property="lastResultNumber" />, de <bean:write name="page" property="totalResults" /> - 
		<logic:equal name="page" property="nextPage" value="true">			 
		<html:link action="/back/sesionesTPV/lista" paramId="pagina" paramName="page" paramProperty="nextPageNumber"><bean:message key="sesionesTPV.paginacion.siguiente" /></html:link> &gt; <html:link action="/back/sesionesTPV/lista" paramId="pagina" paramName="page" paramProperty="lastPageNumber"><bean:message key="sesionesTPV.paginacion.final" /></html:link> &gt;&gt;
		</logic:equal>		
	</div>
</logic:notEmpty>

<br />


</body>