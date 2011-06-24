package es.caib.regtel.plugincaib.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public class DatosRegistroSalida implements DatosRegistro, Serializable
{
	// Atributos del interfaz
	private String altres = "";
	private String balears = "";
	private String comentario = "";
	private String correo = "";
	private String data = "";
	private String disquet = "";
	private String entidad1 = "";
	private String entidad2 = "";
	private String fora = "";
	private String hora = "";
	private String idioex = "";
	private String idioma = "";
	private String oficina = "";
	private String password = "";
	private String usuario = "";
	private String tipo = "";
	
	// atributos propios : salida
	private String dataSalida = "";
	private String numeroSalida = "";
	private String anoSalida = "";
	
	// atributos propios : variables de estado
    private String dataVisado="";
    private String entidad1Grabada="";
    private String entrada1="";
    private String entrada2="";
    private String remitent="";
    private int fzsnume=0;
    private String registroAnulado="";
    private boolean actualizacion=false;
    private boolean registroActualizado=false;
    private boolean leidos=false;
    private String motivo="";
    private String entidad1Nuevo="";
    private String entidad2Nuevo="";
    private String altresNuevo="";
    private String comentarioNuevo="";
    
    private boolean error=false;
    private boolean validado=false;
    private String entidadCastellano="";
    private boolean registroSalidaGrabado=false;
    private Map errores=new HashMap();
        
    private String descripcionOficina=null;
    private String descripcionDestinatario=null;
    private String descripcionOrganismoRemitente=null;
    private String descripcionDocumento=null;
    private String descripcionIdiomaDocumento=null;
    private String destinoGeografico=null;
    private String idiomaExtracto=null;
	
	
	// METODOS PROPIOS : ATRIBUTOS DE SALIDA
	public String getAnoSalida()
	{
		return anoSalida;
	}
	public void setAnoSalida(String anoSalida)
	{
		this.anoSalida = anoSalida;
	}
	public String getDataSalida()
	{
		return dataSalida;
	}
	public void setDataSalida(String dataSalida)
	{
		this.dataSalida = dataSalida;
	}
	public String getNumeroSalida()
	{
		return numeroSalida;
	}
	public void setNumeroSalida(String numeroSalida)
	{
		this.numeroSalida = numeroSalida;
	}
	
	
	// METODOS DEL INTERFAZ
	public String getAltres()
	{
		return altres;
	}
	public void setAltres(String altres)
	{
		this.altres = altres;
	}
	public String getBalears()
	{
		return balears;
	}
	public void setBalears(String balears)
	{
		this.balears = balears;
	}
	public String getComentario()
	{
		return comentario;
	}
	public void setComentario(String comentario)
	{
		this.comentario = comentario;
	}
	public String getCorreo()
	{
		return correo;
	}
	public void setCorreo(String correo)
	{
		this.correo = correo;
	}
	public String getData()
	{
		return data;
	}
	public void setData(String data)
	{
		this.data = data;
	}
	public String getDisquet()
	{
		return disquet;
	}
	public void setDisquet(String disquet)
	{
		this.disquet = disquet;
	}
	public String getEntidad1()
	{
		return entidad1;
	}
	public void setEntidad1(String entidad1)
	{
		this.entidad1 = entidad1;
	}
	public String getEntidad2()
	{
		return entidad2;
	}
	public void setEntidad2(String entidad2)
	{
		this.entidad2 = entidad2;
	}
	public String getFora()
	{
		return fora;
	}
	public void setFora(String fora)
	{
		this.fora = fora;
	}
	public String getHora()
	{
		return hora;
	}
	public void setHora(String hora)
	{
		this.hora = hora;
	}
	public String getIdioex()
	{
		return idioex;
	}
	public void setIdioex(String idioex)
	{
		this.idioex = idioex;
	}
	public String getIdioma()
	{
		return idioma;
	}
	public void setIdioma(String idioma)
	{
		this.idioma = idioma;
	}
	public String getOficina()
	{
		return oficina;
	}
	public void setOficina(String oficina)
	{
		this.oficina = oficina;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public String getTipo()
	{
		return tipo;
	}
	public void setTipo(String tipo)
	{
		this.tipo = tipo;
	}
	public String getUsuario()
	{
		return usuario;
	}
	public void setUsuario(String usuario)
	{
		this.usuario = usuario;
	}
	
	// VARIABLES DE ESTADO
	
	public boolean isActualizacion()
	{
		return actualizacion;
	}
	public void setActualizacion(boolean actualizacion)
	{
		this.actualizacion = actualizacion;
	}
	public String getAltresNuevo()
	{
		return altresNuevo;
	}
	public void setAltresNuevo(String altresNuevo)
	{
		this.altresNuevo = altresNuevo;
	}
	public String getComentarioNuevo()
	{
		return comentarioNuevo;
	}
	public void setComentarioNuevo(String comentarioNuevo)
	{
		this.comentarioNuevo = comentarioNuevo;
	}
	public String getDataVisado()
	{
		return dataVisado;
	}
	public void setDataVisado(String dataVisado)
	{
		this.dataVisado = dataVisado;
	}
	public String getDescripcionDestinatario()
	{
		return descripcionDestinatario;
	}
	public void setDescripcionDestinatario(String descripcionDestinatario)
	{
		this.descripcionDestinatario = descripcionDestinatario;
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
	public String getDescripcionOrganismoRemitente()
	{
		return descripcionOrganismoRemitente;
	}
	public void setDescripcionOrganismoRemitente(
			String descripcionOrganismoRemitente)
	{
		this.descripcionOrganismoRemitente = descripcionOrganismoRemitente;
	}
	public String getDestinoGeografico()
	{
		return destinoGeografico;
	}
	public void setDestinoGeografico(String destinoGeografico)
	{
		this.destinoGeografico = destinoGeografico;
	}
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
	public String getEntidad2Nuevo()
	{
		return entidad2Nuevo;
	}
	public void setEntidad2Nuevo(String entidad2Nuevo)
	{
		this.entidad2Nuevo = entidad2Nuevo;
	}
	public String getEntidadCastellano()
	{
		return entidadCastellano;
	}
	public void setEntidadCastellano(String entidadCastellano)
	{
		this.entidadCastellano = entidadCastellano;
	}
	public String getEntrada1()
	{
		return entrada1;
	}
	public void setEntrada1(String entrada1)
	{
		this.entrada1 = entrada1;
	}
	public String getEntrada2()
	{
		return entrada2;
	}
	public void setEntrada2(String entrada2)
	{
		this.entrada2 = entrada2;
	}
	public boolean isError()
	{
		return error;
	}
	public void setError(boolean error)
	{
		this.error = error;
	}
	public Map getErrores()
	{
		return errores;
	}
	public void setErrores(Map errores)
	{
		this.errores = errores;
	}
	public int getFzsnume()
	{
		return fzsnume;
	}
	public void setFzsnume(int fzsnume)
	{
		this.fzsnume = fzsnume;
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
	public boolean isRegistroActualizado()
	{
		return registroActualizado;
	}
	public void setRegistroActualizado(boolean registroActualizado)
	{
		this.registroActualizado = registroActualizado;
	}
	public String getRegistroAnulado()
	{
		return registroAnulado;
	}
	public void setRegistroAnulado(String registroAnulado)
	{
		this.registroAnulado = registroAnulado;
	}
	public boolean isRegistroSalidaGrabado()
	{
		return registroSalidaGrabado;
	}
	public void setRegistroSalidaGrabado(boolean registroSalidaGrabado)
	{
		this.registroSalidaGrabado = registroSalidaGrabado;
	}
	public String getRemitent()
	{
		return remitent;
	}
	public void setRemitent(String remitent)
	{
		this.remitent = remitent;
	}
	public boolean isValidado()
	{
		return validado;
	}
	public void setValidado(boolean validado)
	{
		this.validado = validado;
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
				sb.append( key.toString() ).append( "=" )
				.append( value != null ? ( !"password".equals( key ) ? value.toString() : "***" ) : null )
				.append( it.hasNext() ? ", " : "") ;
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
	
}
