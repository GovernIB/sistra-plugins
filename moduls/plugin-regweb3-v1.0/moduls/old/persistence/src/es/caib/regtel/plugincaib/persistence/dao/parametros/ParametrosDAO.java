package es.caib.regtel.plugincaib.persistence.dao.parametros;

import java.util.List;

import es.caib.regtel.plugincaib.persistence.dao.DAOInterface;

public abstract class ParametrosDAO extends DAOInterface
{
	public abstract List obtenerOficinas();
	public abstract List obtenerServiciosDestino();
	public abstract List obtenerOficinas( String usuario, String autorizacion );
	public abstract List obtenerDocumentos();
	public abstract List obtenerIdiomas();
	public abstract List obtenerMunicipiosBaleares();
	public abstract String obtenerFecha();
	public abstract String obtenerHorasMinutos();
	public abstract String obtenerDescripcionOficina( String codigoOficina );
}
