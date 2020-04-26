package es.caib.regtel.plugincaib.persistence.dao.parametros;

import java.util.HashMap;
import java.util.Map;

public class ParametrosDAOFactory
{
	private static ParametrosDAOFactory instance;
	private Map implementations = null;
	private ParametrosDAOFactory()
	{
		implementations = new HashMap();
	}
	
	public static ParametrosDAOFactory getInstance()
	{
		return instance != null ? instance : ( instance = new ParametrosDAOFactory() );
	}
	
	public ParametrosDAO getParametrosDAO( String parametrosImpl ) throws Exception
	{
		Class clazz = ( Class ) implementations.get( parametrosImpl );
		if ( clazz == null )
		{
			clazz = Class.forName( parametrosImpl );
			if ( !ParametrosDAO.class.isAssignableFrom( clazz ) )
			{
				throw new Exception( "La clase " + clazz.getName() + " no implementa " + ParametrosDAO.class.getName() );
			}
			implementations.put( parametrosImpl, clazz );
		}
		ParametrosDAO object = ( ParametrosDAO ) clazz.newInstance();
		return object;
	}
	
	
}
