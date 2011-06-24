<%@ taglib prefix="bean" uri="http://jakarta.apache.org/struts/tags-bean"%>
<%@ taglib prefix="logic" uri="http://jakarta.apache.org/struts/tags-logic"%>


	<bean:define id="idioma" value="<%=(java.lang.String) session.getAttribute(es.caib.pagos.front.Constants.DATOS_SESION_IDIOMA_KEY)%>" type="java.lang.String"/>
	<bean:define id="asunto" value="<%=(java.lang.String) session.getAttribute(es.caib.pagos.front.Constants.DATOS_SESION_NOMBRE_TRAMITE_KEY)%>" type="java.lang.String"/>
	
	
	<!-- para coger la url y el telefono de soporte -->
	<bean:define id="urlSoporte">
		<bean:write name="<%=es.caib.pagos.front.Constants.URL_SOPORTE_INCIDENCIAS%>"/>
	</bean:define>
	<bean:define id="telefonoSoporte" type="String">
		<logic:notEmpty name="<%=es.caib.pagos.front.Constants.TELEFONO_INCIDENCIAS%>">
			<bean:write name="<%=es.caib.pagos.front.Constants.TELEFONO_INCIDENCIAS%>"/>
		</logic:notEmpty>
		<logic:empty  name="<%=es.caib.pagos.front.Constants.TELEFONO_INCIDENCIAS%>">
			&nbsp;
		</logic:empty>
	</bean:define>
	<%
		// Construimos url de soporte reemplazando variables
		String urlSoporteFinal = es.caib.util.StringUtil.replace(urlSoporte,"@asunto@",asunto);
	    urlSoporteFinal = es.caib.util.StringUtil.replace(urlSoporteFinal,"@idioma@",idioma);			
	%>
	
	<!-- peu -->
		<div id="peu">
			
			<div class="esquerra">&copy; <bean:write name="<%=es.caib.pagos.front.Constants.ORGANISMO_NAME%>"/></div>
			
			<!-- contacte -->
			<div class="centre">
				<bean:write name="<%=es.caib.pagos.front.Constants.ORGANISMO_INFO_KEY%>" filter="false"/>
			</div>
			<!-- /contacte -->
			
			<div class="dreta">
				<bean:message key="pago.footer.necesitaAyuda"/>
				<logic:notEmpty name="telefonoSoporte">
					<logic:equal name="telefonoSoporte" value="&nbsp;">
						<a href="<%=urlSoporteFinal%>" target="_blank"><bean:message key="pago.footer.necesitaAyuda.enlace"/></a>.
					</logic:equal>
					<logic:notEqual name="telefonoSoporte" value="&nbsp;">
						<a href="javascript:void(0)" onclick="mostrarAyudaAdmin();">
							<bean:message key="pago.footer.necesitaAyuda.enlace"/>
						</a>.					
					</logic:notEqual>
				</logic:notEmpty>
				<logic:empty name="telefonoSoporte">
					<a href="<%=urlSoporteFinal%>" target="_blank"><bean:message key="pago.footer.necesitaAyuda.enlace"/></a>.
				</logic:empty>
			</div>
		</div>
		<!-- /peu -->

	<!-- ayuda -->
	<div id="contactoAdministrador" class="missatge" style="display:none;">
		<h2><bean:message key="pago.footer.ayuda" /></h2>
		<p>
			<bean:message key="pago.footer.contacte.soporteTecnico1"/>
			<a title="<bean:message key="pago.footer.contacte.soporteTecnico"/>" href="<%=urlSoporteFinal%>" target="_blank">
				<bean:message key="pago.footer.contacte.soporteTecnico2"/>
			</a>
			<bean:message key="pago.footer.contacte.soporteTecnico3" arg0="<%=telefonoSoporte%>"/>
		</p>
		<p align="center">
			<a id="suportDescartar" title="<bean:message key="pago.footer.descartar"/>" href="javascript:ocultarAyudaAdmin();">
				<bean:message key="pago.footer.descartar"/>
			</a>
		</p>
	</div>
	<!-- /ayuda -->