package es.caib.regtel.plugincaib.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public class DatosRegistroEntrada implements Serializable, DatosRegistro
{
    private String dataVisado="";
    private String dataentrada="";
    private String hora="";
    private String oficina="";
    private String data="";
    private String tipo="";
    private String idioma="";
    private String entidad1="";
    private String entidad1Grabada="";
    private String entidad2="";
    private String altres="";
    private String balears="";
    private String fora="";
    private String salida1="";
    private String salida2="";
    private String destinatari="";
    private String idioex="";
    private String disquet="";
    private String comentario="";
    private String usuario;
    private int fzanume=0;
    private String correo="";
    private String registroAnulado="";
    private boolean actualizacion=false;
    private boolean leidos=false;
    private String motivo="";
    private String entidad1Nuevo="";
    private String entidad2Nuevo="";
    private String altresNuevo="";
    private String comentarioNuevo="";
    private String password="";
    private String anoEntrada=null;
    private String numeroEntrada=null;
    private String descripcionOficina=null;
    private String descripcionRemitente=null;
    private String descripcionOrganismoDestinatario=null;
    private String descripcionDocumento=null;
    private String descripcionIdiomaDocumento=null;
    private String procedenciaGeografica=null;
    private String idiomaExtracto=null;
    
    //Variables de estado
    private boolean error=false;
    private boolean validado=false;
    private boolean registroGrabado=false;
    private boolean registroActualizado=false;
    private String entidadCastellano;
    private Map errores = new HashMap();
    
    
    // Variables para la publicacion de un registro
    private String BOIBdata="";
    private int BOIBnumeroBOCAIB=0;
    private int BOIBpagina=0;
    private int BOIBlineas=0;
    private String BOIBtexto="";
    private String BOIBobservaciones="";
    
	public boolean isActualizacion()
	{
		return actualizacion;
	}
	public void setActualizacion(boolean actualizacion)
	{
		this.actualizacion = actualizacion;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#getAltres()
	 */
	public String getAltres()
	{
		return altres;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#setAltres(java.lang.String)
	 */
	public void setAltres(String altres)
	{
		this.altres = altres;
	}
	public String getAltresNuevo()
	{
		return altresNuevo;
	}
	public void setAltresNuevo(String altresNuevo)
	{
		this.altresNuevo = altresNuevo;
	}
	public String getAnoEntrada()
	{
		return anoEntrada;
	}
	public void setAnoEntrada(String anoEntrada)
	{
		this.anoEntrada = anoEntrada;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#getBalears()
	 */
	public String getBalears()
	{
		return balears;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#setBalears(java.lang.String)
	 */
	public void setBalears(String balears)
	{
		this.balears = balears;
	}
	public String getBOIBdata()
	{
		return BOIBdata;
	}
	public void setBOIBdata(String bdata)
	{
		BOIBdata = bdata;
	}
	public int getBOIBlineas()
	{
		return BOIBlineas;
	}
	public void setBOIBlineas(int blineas)
	{
		BOIBlineas = blineas;
	}
	public int getBOIBnumeroBOCAIB()
	{
		return BOIBnumeroBOCAIB;
	}
	public void setBOIBnumeroBOCAIB(int bnumeroBOCAIB)
	{
		BOIBnumeroBOCAIB = bnumeroBOCAIB;
	}
	public String getBOIBobservaciones()
	{
		return BOIBobservaciones;
	}
	public void setBOIBobservaciones(String bobservaciones)
	{
		BOIBobservaciones = bobservaciones;
	}
	public int getBOIBpagina()
	{
		return BOIBpagina;
	}
	public void setBOIBpagina(int bpagina)
	{
		BOIBpagina = bpagina;
	}
	public String getBOIBtexto()
	{
		return BOIBtexto;
	}
	public void setBOIBtexto(String btexto)
	{
		BOIBtexto = btexto;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#getComentario()
	 */
	public String getComentario()
	{
		return comentario;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#setComentario(java.lang.String)
	 */
	public void setComentario(String comentario)
	{
		this.comentario = comentario;
	}
	public String getComentarioNuevo()
	{
		return comentarioNuevo;
	}
	public void setComentarioNuevo(String comentarioNuevo)
	{
		this.comentarioNuevo = comentarioNuevo;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#getCorreo()
	 */
	public String getCorreo()
	{
		return correo;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#setCorreo(java.lang.String)
	 */
	public void setCorreo(String correo)
	{
		this.correo = correo;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#getData()
	 */
	public String getData()
	{
		return data;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#setData(java.lang.String)
	 */
	public void setData(String data)
	{
		this.data = data;
	}
	public String getDataentrada()
	{
		return dataentrada;
	}
	public void setDataentrada(String dataentrada)
	{
		this.dataentrada = dataentrada;
	}
	public String getDataVisado()
	{
		return dataVisado;
	}
	public void setDataVisado(String dataVisado)
	{
		this.dataVisado = dataVisado;
	}
	public String getDescripcionDocumento()
	{
		return descripcionDocumento;
	}
	public void setDescripcionDocumento(String descripcionDocumento)
	{
		this.descripcionDocumento = descripcionDocumento;
	}
	public String getDescripcionIdiomaDocumento()
	{
		return descripcionIdiomaDocumento;
	}
	public void setDescripcionIdiomaDocumento(String descripcionIdiomaDocumento)
	{
		this.descripcionIdiomaDocumento = descripcionIdiomaDocumento;
	}
	public String getDescripcionOficina()
	{
		return descripcionOficina;
	}
	public void setDescripcionOficina(String descripcionOficina)
	{
		this.descripcionOficina = descripcionOficina;
	}
	public String getDescripcionOrganismoDestinatario()
	{
		return descripcionOrganismoDestinatario;
	}
	public void setDescripcionOrganismoDestinatario(
			String descripcionOrganismoDestinatario)
	{
		this.descripcionOrganismoDestinatario = descripcionOrganismoDestinatario;
	}
	public String getDescripcionRemitente()
	{
		return descripcionRemitente;
	}
	public void setDescripcionRemitente(String descripcionRemitente)
	{
		this.descripcionRemitente = descripcionRemitente;
	}
	public String getDestinatari()
	{
		return destinatari;
	}
	public void setDestinatari(String destinatari)
	{
		this.destinatari = destinatari;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#getDisquet()
	 */
	public String getDisquet()
	{
		return disquet;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#setDisquet(java.lang.String)
	 */
	public void setDisquet(String disquet)
	{
		this.disquet = disquet;
	}
	public String getEntidad1()
	{
		return entidad1;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#setEntidad1(java.lang.String)
	 */
	public void setEntidad1(String entidad1)
	{
		this.entidad1 = entidad1;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#getEntidad1Grabada()
	 */
	public String getEntidad1Grabada()
	{
		return entidad1Grabada;
	}
	public void setEntidad1Grabada(String entidad1Grabada)
	{
		this.entidad1Grabada = entidad1Grabada;
	}
	public String getEntidad1Nuevo()
	{
		return entidad1Nuevo;
	}
	public void setEntidad1Nuevo(String entidad1Nuevo)
	{
		this.entidad1Nuevo = entidad1Nuevo;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#getEntidad2()
	 */
	public String getEntidad2()
	{
		return entidad2;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#setEntidad2(java.lang.String)
	 */
	public void setEntidad2(String entidad2)
	{
		this.entidad2 = entidad2;
	}
	public String getEntidad2Nuevo()
	{
		return entidad2Nuevo;
	}
	public void setEntidad2Nuevo(String entidad2Nuevo)
	{
		this.entidad2Nuevo = entidad2Nuevo;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#getFora()
	 */
	public String getFora()
	{
		return fora;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#setFora(java.lang.String)
	 */
	public void setFora(String fora)
	{
		this.fora = fora;
	}
	public int getFzanume()
	{
		return fzanume;
	}
	public void setFzanume(int fzanume)
	{
		this.fzanume = fzanume;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#getHora()
	 */
	public String getHora()
	{
		return hora;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#setHora(java.lang.String)
	 */
	public void setHora(String hora)
	{
		this.hora = hora;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#getIdioex()
	 */
	public String getIdioex()
	{
		return idioex;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#setIdioex(java.lang.String)
	 */
	public void setIdioex(String idioex)
	{
		this.idioex = idioex;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#getIdioma()
	 */
	public String getIdioma()
	{
		return idioma;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#setIdioma(java.lang.String)
	 */
	public void setIdioma(String idioma)
	{
		this.idioma = idioma;
	}
	public String getIdiomaExtracto()
	{
		return idiomaExtracto;
	}
	public void setIdiomaExtracto(String idiomaExtracto)
	{
		this.idiomaExtracto = idiomaExtracto;
	}
	public boolean isLeidos()
	{
		return leidos;
	}
	public void setLeidos(boolean leidos)
	{
		this.leidos = leidos;
	}
	public String getMotivo()
	{
		return motivo;
	}
	public void setMotivo(String motivo)
	{
		this.motivo = motivo;
	}
	public String getNumeroEntrada()
	{
		return numeroEntrada;
	}
	public void setNumeroEntrada(String numeroEntrada)
	{
		this.numeroEntrada = numeroEntrada;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#getOficina()
	 */
	public String getOficina()
	{
		return oficina;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#setOficina(java.lang.String)
	 */
	public void setOficina(String oficina)
	{
		this.oficina = oficina;
	}
	

	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#getPassword()
	 */
	public String getPassword()
	{
		return password;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#setPassword(java.lang.String)
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	
	public String getProcedenciaGeografica()
	{
		return procedenciaGeografica;
	}
	public void setProcedenciaGeografica(String procedenciaGeografica)
	{
		this.procedenciaGeografica = procedenciaGeografica;
	}
	public String getRegistroAnulado()
	{
		return registroAnulado;
	}
	public void setRegistroAnulado(String registroAnulado)
	{
		this.registroAnulado = registroAnulado;
	}
	public String getSalida1()
	{
		return salida1;
	}
	public void setSalida1(String salida1)
	{
		this.salida1 = salida1;
	}
	public String getSalida2()
	{
		return salida2;
	}
	public void setSalida2(String salida2)
	{
		this.salida2 = salida2;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#getTipo()
	 */
	public String getTipo()
	{
		return tipo;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#setTipo(java.lang.String)
	 */
	public void setTipo(String tipo)
	{
		this.tipo = tipo;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#getUsuario()
	 */
	public String getUsuario()
	{
		return usuario;
	}
	/* (non-Javadoc)
	 * @see es.caib.regtel.model.DatosRegistro#setUsuario(java.lang.String)
	 */
	public void setUsuario(String usuario)
	{
		this.usuario = usuario;
	}

	public String toString()
	{
		try
		{
			StringBuffer sb = new StringBuffer( "{" );
			Map properties = BeanUtils.describe( this );
			for ( Iterator it = properties.keySet().iterator(); it.hasNext(); )
			{
				Object key = it.next();
				Object value = properties.get( key );
				sb.append( key.toString() ).append( "=" ).append( value != null ? ( !"password".equals( key ) ? value.toString() : "***" ) : null ).append( it.hasNext() ? ", " : "") ;
			}
			sb.append( "}" );
			return sb.toString();
		}
		catch( Exception exc )
		{
			exc.printStackTrace();
			return super.toString();
		}
	}
    
	public static void main ( String args [] )
	{
		DatosRegistroEntrada dre = new DatosRegistroEntrada();
		dre.setActualizacion( true );
		dre.setAltres( "altres" );
		dre.setAltresNuevo( "" );
		System.out.println( dre );
	}
	public boolean isError()
	{
		return error;
	}
	public void setError(boolean error)
	{
		this.error = error;
	}
	public boolean isRegistroActualizado()
	{
		return registroActualizado;
	}
	public void setRegistroActualizado(boolean registroActualizado)
	{
		this.registroActualizado = registroActualizado;
	}
	public boolean isRegistroGrabado()
	{
		return registroGrabado;
	}
	public void setRegistroGrabado(boolean registroGrabado)
	{
		this.registroGrabado = registroGrabado;
	}
	public boolean isValidado()
	{
		return validado;
	}
	public void setValidado(boolean validado)
	{
		this.validado = validado;
	}
	public String getEntidadCastellano()
	{
		return entidadCastellano;
	}
	public void setEntidadCastellano(String entidadCastellano)
	{
		this.entidadCastellano = entidadCastellano;
	}
	public Map getErrores()
	{
		return errores;
	}
	public void setErrores(Map errores)
	{
		this.errores = errores;
	}
	
}
