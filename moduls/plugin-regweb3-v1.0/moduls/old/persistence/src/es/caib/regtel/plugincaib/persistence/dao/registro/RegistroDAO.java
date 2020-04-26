package es.caib.regtel.plugincaib.persistence.dao.registro;

import es.caib.regtel.plugincaib.model.DatosRegistroEntrada;
import es.caib.regtel.plugincaib.model.DatosRegistroSalida;
import es.caib.regtel.plugincaib.persistence.dao.DAOInterface;

public abstract class RegistroDAO extends DAOInterface
{
	public abstract DatosRegistroEntrada grabar( DatosRegistroEntrada entrada, boolean validarOficina ) throws Exception;
	
	public abstract DatosRegistroSalida grabar( DatosRegistroSalida salida, boolean validarOficina )  throws Exception;
	
	public abstract void anularRegistroEntrada(String usuario, String oficina, String numeroEntrada, String anyoEntrada, boolean validarOficina) throws Exception;

}
