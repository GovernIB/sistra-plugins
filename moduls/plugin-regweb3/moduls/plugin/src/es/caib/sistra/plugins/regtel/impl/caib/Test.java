package es.caib.sistra.plugins.regtel.impl.caib;

import java.util.Iterator;
import java.util.List;

import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;

import es.caib.sistra.plugins.regtel.ConstantesPluginRegistro;
import es.caib.sistra.plugins.regtel.OficinaRegistro;
import es.caib.sistra.plugins.regtel.ServicioDestinatario;
import es.caib.sistra.plugins.regtel.TipoAsunto;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {				
		
		
		System.out.println("Ini");
		
		PluginRegweb3 plg = new PluginRegweb3();
		
		 testOficinasUsuario(plg, "indraapp");
		
		// testOficinas(plg);
		
		// testAsuntos(plg);
		
		// testObtenerDescripcionSelloOficina(plg);
		
		testObtenerServiciosDestino(plg);

		System.out.println("Fin");
		
	}	
	
	private static void testObtenerServiciosDestino(PluginRegweb3 plg) {
		List oficinas = plg.obtenerServiciosDestino();
		for (Iterator it = oficinas.iterator(); it.hasNext();) {
			ServicioDestinatario of = (ServicioDestinatario) it.next();
			System.out.println(of.getCodigo() + " - " + of.getDescripcion());
		}
	}
	
	

	private static void testOficinasUsuario(PluginRegweb3 plg, String usuario) {
		List oficinas = plg.obtenerOficinasRegistroUsuario(ConstantesPluginRegistro.REGISTRO_ENTRADA, usuario);
		
		for (Iterator it = oficinas.iterator(); it.hasNext();) {
			OficinaRegistro of = (OficinaRegistro) it.next();
			System.out.println(of.getCodigo() + " - " + of.getDescripcion());
		}
	}
	
	private static void testOficinas(PluginRegweb3 plg) {
		List oficinas = plg.obtenerOficinasRegistro(ConstantesPluginRegistro.REGISTRO_ENTRADA);
		
		for (Iterator it = oficinas.iterator(); it.hasNext();) {
			OficinaRegistro of = (OficinaRegistro) it.next();
			System.out.println(of.getCodigo() + " - " + of.getDescripcion());
		}
	}
	
	private static void testObtenerDescripcionSelloOficina(PluginRegweb3 plg) {
		String descSello = plg.obtenerDescripcionSelloOficina(ConstantesPluginRegistro.REGISTRO_ENTRADA, "ASPI.O00015977");
		System.out.println("Sello: " + descSello);		
	}
	
	private static void testAsuntos(PluginRegweb3 plg) {
		List oficinas = plg.obtenerTiposAsunto();
		
		for (Iterator it = oficinas.iterator(); it.hasNext();) {
			TipoAsunto of = (TipoAsunto) it.next();
			System.out.println(of.getCodigo() + " - " + of.getDescripcion());
		}
	}

}
