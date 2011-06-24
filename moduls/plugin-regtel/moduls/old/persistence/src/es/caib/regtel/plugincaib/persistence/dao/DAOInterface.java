package es.caib.regtel.plugincaib.persistence.dao;

import java.io.Serializable;

public class DAOInterface implements Serializable
{
	private static String DEFAULT_DATASOURCE = "java:/es.caib.regweb.db";
	Object initParam;
	public void init( Object initParam )
	{
		this.initParam = initParam;
	}
	
	protected Object getInitParam()
	{
		return this.initParam;
	}
	
	protected String getJNDIDatasource()
	{
		return this.getInitParam ()!= null ? ( String ) this.getInitParam() : DEFAULT_DATASOURCE;
	}
	
}
