package es.caib.regtel.plugincaib.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Log de los usuarios que realizan registros para poder realizar su anulación con el usuario que realizo el registro.
 *
 */
public class LogUsuariosRegistro implements Serializable
{
	private LogUsuariosRegistroId id;	
    private String usuarioRegistro;
    private Date fechaRegistro;
	
    public LogUsuariosRegistroId getId() {
		return id;
	}
	public void setId(LogUsuariosRegistroId id) {
		this.id = id;
	}
	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}
	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	    	
	
}
