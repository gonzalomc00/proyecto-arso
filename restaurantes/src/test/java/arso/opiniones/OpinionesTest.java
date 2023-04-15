package arso.opiniones;

import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import opiniones.modelo.Opinion;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import restaurantes.servicio.IServicioOpinion;
import servicio.FactoriaServicios;

@Disabled
public class OpinionesTest {
	private IServicioOpinion servicio = FactoriaServicios.getServicio(IServicioOpinion.class);
	
	
	
	@Test
	public void testGetOpinion() throws RepositorioException, EntidadNoEncontrada {
		Opinion o= servicio.getOpinion("643a9023e8721c26a57915b6");
		System.out.println(o.toString());
		
	}
	
	@Test
	public void testCreateOpinion() throws IOException {
		String id= servicio.createOpinion("Prueba Retrofit");
		System.out.println(id);
	}
	
	
}
