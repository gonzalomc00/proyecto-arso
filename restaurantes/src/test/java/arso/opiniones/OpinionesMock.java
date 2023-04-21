package arso.opiniones;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import restaurantes.modelo.Restaurante;
import restaurantes.servicio.IServicioOpinion;
import restaurantes.servicio.IServicioRestaurante;
import servicio.FactoriaServicios;

import static org.junit.Assert.*;

import java.io.IOException;

public class OpinionesMock {

	@Rule
	public JUnitRuleMockery context = new JUnitRuleMockery();
	final IServicioOpinion servicio = context.mock(IServicioOpinion.class);

	private IServicioRestaurante servicioRes =  FactoriaServicios.getServicio(IServicioRestaurante.class);
	
	@Before
	public void setUp() {
		servicioRes.setServicioOpinion(servicio);
	}
	

	@Test
	public void testActivarValoraciones() throws RepositorioException, IOException, EntidadNoEncontrada {
		context.checking(new Expectations() {
			{
				oneOf(servicio).createOpinion(with(any(String.class)));
				will(returnValue("idOpinion"));
			}
		});

		String id = servicioRes.create("Prueba", "30150", "Murcia", 30.00, 20.00, "alguien");
		servicioRes.activarValoraciones(id);

		Restaurante r = servicioRes.getRestaurante(id);

		assertEquals("idOpinion", r.getResumenValoracion().getIdOpinion());

	}
	
	@Test(expected = IllegalStateException.class)
	public void testActivarValoracionesDuplicated() throws IOException, RepositorioException, EntidadNoEncontrada {
		context.checking(new Expectations() {
			{
				oneOf(servicio).createOpinion(with(any(String.class)));
				will(returnValue("idOpinion"));
			}
		});

		String id = servicioRes.create("Prueba", "30150", "Murcia", 30.00, 20.00, "alguien");
		servicioRes.activarValoraciones(id);
		servicioRes.activarValoraciones(id);
		
	}
	
	

}


/**
 * @Test 
	public void testActivarValoracionesDuplicated() {
		
	}
 */
