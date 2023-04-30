package arso.opiniones;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.xml.sax.SAXException;

import opiniones.modelo.Opinion;
import opiniones.modelo.Valoracion;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import restaurantes.modelo.Restaurante;
import restaurantes.modelo.SitioTuristico;
import restaurantes.servicio.IServicioOpinion;
import restaurantes.servicio.IServicioRestaurante;
import servicio.FactoriaServicios;

public class OpinionesMock {

	@Rule
	public JUnitRuleMockery context = new JUnitRuleMockery();
	final IServicioOpinion servicio = context.mock(IServicioOpinion.class);

	private IServicioRestaurante servicioRes = FactoriaServicios.getServicio(IServicioRestaurante.class);

	@Before
	public void setUp() {
		servicioRes.setServicioOpinion(servicio);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testActivarValoracionIdNull() throws RepositorioException, IOException, EntidadNoEncontrada {

		try {
			servicioRes.activarValoraciones(null);
			fail("Se esperaba que se lanzara una excepción IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("id del restaurante: no debe ser nulo ni vacio", e.getMessage());
			throw e;
		}

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

	@Test
	public void testGetValoraciones() throws RepositorioException, IOException, EntidadNoEncontrada {
		context.checking(new Expectations() {
			{
				oneOf(servicio).createOpinion(with(any(String.class)));
				will(returnValue("idOpinion"));
			}
		});

		String id = servicioRes.create("Prueba", "30150", "Murcia", 30.00, 20.00, "alguien");
		servicioRes.activarValoraciones(id);

		Restaurante r = servicioRes.getRestaurante(id);

		List<Valoracion> valoraciones = new LinkedList<Valoracion>();
		valoraciones.add(new Valoracion("alumno@um.es", LocalDateTime.now(), 9.00, null));
		Opinion o = new Opinion();
		o.setId(id);
		o.setNombreRecurso(r.getNombre());
		o.setValoraciones(valoraciones);

		context.checking(new Expectations() {
			{
				oneOf(servicio).getOpinion("idOpinion");
				will(returnValue(o));
			}
		});

		List<Valoracion> misVal = servicioRes.getValoracionesRes(id);

		System.out.println(misVal.toString());
		assertEquals(misVal, valoraciones);
		assertEquals(misVal.get(0).getCorreo(), valoraciones.get(0).getCorreo());
		assertEquals(misVal.get(0).getComentario(), valoraciones.get(0).getComentario());
		assertEquals(misVal.get(0).getFecha(), valoraciones.get(0).getFecha());
		assertEquals(misVal.get(0).getCalificacion(), valoraciones.get(0).getCalificacion(), 0.0);

	}
	

	@Test(expected = IllegalArgumentException.class)
	public void testGetValoracionesIdNull() throws RepositorioException, IOException, EntidadNoEncontrada {

		try {
			servicioRes.getValoracionesRes(null);
			fail("Se esperaba que se lanzara una excepción IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertEquals("id del restaurante: no debe ser nulo ni vacio", e.getMessage());
			throw e;
		}

	}
	
	
	//TODO: no hace uso del mock pero se ha puesto tontos los test normales y he hecho esto para probarlo
	@Test
	public void TestObtenerSitiosTuristicos() throws MalformedURLException, SAXException, IOException,
			ParserConfigurationException, RepositorioException, EntidadNoEncontrada {

		String id2 = servicioRes.create("GyS", "30150", "Murcia", 30.00, 40.00,"sofia");

		List<SitioTuristico> listaSitios1 = servicioRes.obtenerSitiosTuristicos(id2);

		System.out.println(listaSitios1.toString());
		for (SitioTuristico t : listaSitios1) {
			System.out.println(t.toString());

		}


	}

}
