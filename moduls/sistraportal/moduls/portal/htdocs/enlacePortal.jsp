<%@ page language="java" contentType="text/html; charset=ISO-8859-1" errorPage="/moduls/errorEnJsp.jsp"%>
<%@ taglib prefix="html" uri="http://jakarta.apache.org/struts/tags-html"%>
<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"%>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="es" lang="es">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<title>Govern de les Illes Balears</title>
	<link href="estilos/estilosIframe.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="js/iframeAutenticacion.js"></script>
	<script language="javascript">
	<!--
	var idioma = '<bean:write name="idioma"/>';
	var modelo = '<bean:write name="modelo"/>';
	var version = '<bean:write name="version"/>';
	var urlInit = '<%=es.caib.sistra.persistence.delegate.DelegateUtil.getConfiguracionDelegate().obtenerConfiguracion().getProperty("sistra.url")%>/sistrafront/inicio?language=' + idioma + '&modelo=' + modelo + '&version=' + version;
		
	function validaIdPersistencia( idPersistencia )
	    {
			if ( idPersistencia == null || idPersistencia == '' )
			{
				alert('<bean:message key="enlacePortal.idPersistenciaVacio"/>');					
				return false;
			}
	        var filter  = /^\w{8}\-\w{8}\-\w{8}$/;
	        if ( !filter.test( idPersistencia ) )
	        {
	        	alert('<bean:message key="enlacePortal.idPersistenciaIncorrecto"/>');	
				return false;
	        }
	        return true;
		}
		
	function irAUrl(url){
		window.top.document.location=url;
	}	
		
	function cargarTramite(idPersistencia){
		url = urlInit + '&idPersistencia=' + idPersistencia;
		irAUrl(url);
	}	
		
		
	function cargaAnonimo(){
		clave = document.getElementById("CLAVE").value;
		
		// Eliminamos espacios en blanco
		clave = clave.replace(/^\s+|\s+$/g,'');
		
		if (!validaIdPersistencia(clave)){
			document.getElementById("CLAVE").focus();
			return;
		}
		
		<logic:equal name="autenticado" value="true">
			if (!confirm('<bean:message key="enlacePortal.desautenticarse"/>')) return;
		</logic:equal>
		
		url =  urlInit + '&autenticacion=A&idPersistencia=' + document.getElementById("CLAVE").value;		
		irAUrl(url);
	}	
	
	function nuevoAnonimo(){
		url = urlInit + '&autenticacion=A';		
		irAUrl(url);
		
	}
	
	function nuevoAutenticado(){
		url = urlInit + '&autenticacion=CU';		
		irAUrl(url);
	}
	
	function autenticaPortal(){		
		//document.location='/sistrafront/protected/init.do?autenticaPortal=S&url=' + escape(document.location) ;
		url = '<%=es.caib.sistra.persistence.delegate.DelegateUtil.getConfiguracionDelegate().obtenerConfiguracion().getProperty("sistra.url")%>/sistrafront/protected/init.do?autenticaPortal=S&url=' + escape(window.top.document.location) ;
		irAUrl(url);
	}
	-->
	</script>
</head>

<body>
	<div id="contenedor">


<logic:equal name="error" value="true">
	<bean:message key="enlacePortal.error"/>	
</logic:equal>    	

<logic:equal name="error" value="false">
	
	<logic:equal name="conAutenticacion" value="true">
		<h1 class="inicioTramite cd"><bean:message key="enlacePortal.conAutenticacion"/></h1>		
			
			<p>
				<a href="javascript:nuevoAutenticado()" class="inicioAutenticado">
					<bean:message key="enlacePortal.iniciarNuevoTramite"/>
				</a> 
			</p>
			
			<logic:equal name="autenticado" value="true">
				<p class="inacabados"> <strong><bean:message key="enlacePortal.tramitesInacabados.lista"/></strong></p>		
				<div class="anonimoOpcion">	
				
				<logic:empty name="tramitesInacabados">
					<p class="info"><bean:message key="enlacePortal.tramitesInacabados.noHay"/></p>
				</logic:empty>				
				
				<logic:notEmpty name="tramitesInacabados">												
					<p class="info"><bean:message key="enlacePortal.tramitesInacabados.info"/></p>
					<ul class="listadoTA">					
					<logic:iterate name="tramitesInacabados" id="tramiteInacabado">
						<li>
						
							<logic:empty name="tramiteInacabado" property="remitidoA">
								<a href="javascript:cargarTramite('<bean:write name="tramiteInacabado" property="idPersistencia"/>')">
							</logic:empty>						
							<bean:write name="tramiteInacabado" property="ultimaModificacion" format="dd/MM/yyyy - HH:mm 'h.'" /> (<bean:write name="tramiteInacabado" property="diasPersistencia"/> <bean:message key="enlacePortal.dias"/>)
							
							<logic:notEmpty name="tramiteInacabado" property="remitidoA">
								- <bean:message key="enlacePortal.remitidoA"/> <bean:write name="tramiteInacabado" property="remitidoA"/>
							</logic:notEmpty>
							
							<logic:notEmpty name="tramiteInacabado" property="remitidoPor">
								- <bean:message key="enlacePortal.remitidoPor"/> <bean:write name="tramiteInacabado" property="remitidoPor"/>
							</logic:notEmpty>
							
							<logic:empty name="tramiteInacabado" property="remitidoA">							
								</a>
							</logic:empty>
						</li>
					</logic:iterate>	
					</ul>								
				</logic:notEmpty>	
				
				</div>								
			</logic:equal>
			
			<logic:equal name="autenticado" value="false">				
				<p>
					<a href="javascript:autenticaPortal()" class="inicioAutenticado">
						<bean:message key="enlacePortal.continuarTramite"/>  
					</a> 
				</p>				
			</logic:equal>
	</logic:equal>
	
	
	<logic:equal name="anonimo" value="true">
		<h1 class="inicioTramite a"><bean:message key="enlacePortal.anonimamente"/></h1>
		<p>
			<a href="javascript:nuevoAnonimo()" class="inicioAutenticado">
				<bean:message key="enlacePortal.iniciarNuevoTramite"/>
			</a>
		</p>
		<logic:equal name="continuarAnonimo" value="true">
			<p>
				<a href="javascript:cargaAnonimo()" class="inicioAutenticado">
					<bean:message key="enlacePortal.continuarTramite"/>
				</a> 
				<div class="anonimoOpcion">
					<label for="CLAVE"> <br />
						 <bean:message key="enlacePortal.clave"/></label>				  
					  <input name="CLAVE" id="CLAVE" type="text" size="20" />				 				  
			   </div>
			</p>
		</logic:equal>
	</logic:equal>
	
</logic:equal>    	


</div>
	
</body>
</html>