<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"%>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic"%>


	<bean:define id="idioma" value="<%=(java.lang.String) session.getAttribute(es.caib.pagos.front.Constants.DATOS_SESION_IDIOMA_KEY)%>" type="java.lang.String"/>
	<bean:define id="asunto" value="<%=(java.lang.String) session.getAttribute(es.caib.pagos.front.Constants.DATOS_SESION_NOMBRE_TRAMITE_KEY)%>" type="java.lang.String"/>
	
	
	<!-- peu -->
		<div id="peu">
			
			<div class="esquerra">&copy;<bean:write name="<%=es.caib.pagos.front.Constants.ORGANISMO_INFO_KEY%>" property="nombre" /></div>
			
			<!-- contacte -->
			<div class="centre">
				<bean:write name="<%=es.caib.pagos.front.Constants.ORGANISMO_INFO_KEY%>" property="pieContactoHTML" filter="false"/>				
			</div>
			<!-- /contacte -->
			
			<div class="dreta">
				<bean:message key="pago.footer.necesitaAyuda"/>
				<a href="javascript:void(0)" onclick="mostrarAyudaAdmin();">
						<bean:message key="pago.footer.necesitaAyuda.enlace"/>
				</a>.									
			</div>
		</div>
		<!-- /peu -->

	<!-- ayuda -->
	<div id="contactoAdministrador" class="missatge" style="display:none;">
		<h2><bean:message key="pago.footer.ayuda" /></h2>
		<div id="contactoAdministradorSoporte">
		<p>
			<bean:write name="literalContacto" filter="false"/>
		</p>
		</div>
		<div id="contactoAdministradorContent"></div>
		<p align="center">
			<a id="suportDescartar" title="<bean:message key="pago.footer.descartar"/>" href="javascript:ocultarAyudaAdmin();">
				<bean:message key="pago.footer.descartar"/>
			</a>
		</p>
	</div>
	<!-- /ayuda -->