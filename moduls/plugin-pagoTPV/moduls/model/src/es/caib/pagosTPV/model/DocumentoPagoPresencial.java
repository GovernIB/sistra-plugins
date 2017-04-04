package es.caib.pagosTPV.model;

/**
 * Documeno de pago prsencial.
 * @author rsanz
 *
 */
public class DocumentoPagoPresencial {

	private String nombre;
	private byte[] contenido;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public byte[] getContenido() {
		return contenido;
	}
	public void setContenido(byte[] contenido) {
		this.contenido = contenido;
	}
}
