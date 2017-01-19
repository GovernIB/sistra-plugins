<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"%>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic"%>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html"%>

	<bean:define id="lang" value="<%=((java.util.Locale) session.getAttribute(org.apache.struts.Globals.LOCALE_KEY)).getLanguage()%>" type="java.lang.String"/>
	<bean:define id="urlPortal">
		<bean:write name="<%=es.caib.pagosTPV.front.Constants.ORGANISMO_INFO_KEY%>" property="urlPortal" />		
	</bean:define>
	<bean:define id="urlMantenimientoSistra" value="<%=((java.lang.String) session.getAttribute(es.caib.pagosTPV.front.Constants.URL_MANTENIMIENTO_SISTRA_KEY))%>" type="java.lang.String"/>

	<!-- logo illes balears -->
	<div id="cap">
		<html:link href="<%=urlPortal%>" paramId="lang" paramName="lang" accesskey="0" >
			<img id="logoCAIB" class="logo" src="<bean:write name="<%=es.caib.pagosTPV.front.Constants.ORGANISMO_INFO_KEY%>" property="urlLogo" />" alt="<bean:write name="<%=es.caib.pagosTPV.front.Constants.ORGANISMO_INFO_KEY%>" property="nombre" />" />
		</html:link>
	</div>
	<!-- /logo illes balears -->
	
	<!-- titol aplicacio -->
	<p id="titolAplicacio"><bean:message key="tituloAplicacion"/></p>
	<!-- /titol aplicacio -->
	
	<script type="text/javascript">
		<!--
		// Definir objeto XMLHttpRequest
		var xmlhttp=false;
		/*@cc_on @*/
		/*@if (@_jscript_version >= 5)
		try {
		    xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (e) {
		    try {
		        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
		    } catch (E) {
		        xmlhttp = false;
		    }
		}
		@end @*/
		if (!xmlhttp && typeof XMLHttpRequest!='undefined') {
		    xmlhttp = new XMLHttpRequest();
		}
		// Realizamos llamada
		xmlhttp.open( "POST", "<%=urlMantenimientoSistra%>", true ); 
		xmlhttp.send(null);
		-->
	</script>