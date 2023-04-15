package arso.opiniones;

import org.junit.jupiter.api.Test;

import opiniones.modelo.Opinion;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import restaurantes.servicio.IServicioOpinion;
import servicio.FactoriaServicios;

public class OpinionesTest {
	private IServicioOpinion servicio = FactoriaServicios.getServicio(IServicioOpinion.class);
	
	
	
	@Test
	public void testGetOpinion() throws RepositorioException, EntidadNoEncontrada {
		Opinion o= servicio.getOpinion("643a9023e8721c26a57915b6");
		sysout
		
	}
}
