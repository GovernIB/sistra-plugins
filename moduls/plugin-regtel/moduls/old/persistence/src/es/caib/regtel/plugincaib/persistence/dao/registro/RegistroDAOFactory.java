package es.caib.regtel.plugincaib.persistence.dao.registro;

import java.util.HashMap;
import java.util.Map;


public class RegistroDAOFactory
{
	private static String REGISTRO_DAO_DEFAULT_IMPL = "es.caib.regtel.persistence.dao.registro.RegistroDAOImpl";
	private static RegistroDAOFactory instance;
	private Map implementations = null;
	private RegistroDAOFactory()
	{
		implementations = new HashMap();
	}
	
	public static RegistroDAOFactory getInstance()
	{
		return instance != null ? instance : ( instance = new RegistroDAOFactory() );
	}
	
	public RegistroDAO getRegistroDAO ( String registroImpl ) throws Exception
	{
		return getRegistroDAO( registroImpl, null );
	}
	
	public RegistroDAO getRegistroDAO ( Object initParam ) throws Exception
	{
		return getRegistroDAO( REGISTRO_DAO_DEFAULT_IMPL, initParam );
	}
	
	public RegistroDAO getRegistroDAO( String registroImpl, Object initParam ) throws Exception
	{
		Class clazz = ( Class ) implementations.get( registroImpl );
		if ( clazz == null )
		{
			clazz = Class.forName( registroImpl );
			if ( !RegistroDAO.class.isAssignableFrom( clazz ) )
			{
				throw new Exception( "La clase " + clazz.getName() + " no extiende " + RegistroDAO.class.getName() );
			}
			implementations.put( registroImpl, clazz );
		}
		RegistroDAO object = ( RegistroDAO ) clazz.newInstance();
		object.init( initParam );
		return object;
	}
}
