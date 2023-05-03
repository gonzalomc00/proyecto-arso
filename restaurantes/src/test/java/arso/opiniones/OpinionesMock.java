package arso.opiniones;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import opiniones.modelo.Valoracion;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import restaurantes.modelo.Restaurante;
import restaurantes.modelo.SitioTuristico;
import restaurantes.servicio.IServicioOpinion;
import restaurantes.servicio.IServicioRestaurante;
import servicio.FactoriaServicios;

public class OpinionesMock {

	private IServicioRestaurante servicioRes;

	@Before
	public void setUp() {
		servicioRes = FactoriaServicios.getServicio(IServicioRestaurante.class);
		
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
	
		String id = servicioRes.create("Prueba", "30150", "Murcia", 30.00, 20.00, "alguien");
		servicioRes.activarValoraciones(id);

		Restaurante r = servicioRes.getRestaurante(id);

		assertEquals("idOpinion", r.getResumenValoracion().getIdOpinion());

	}

	@Test(expected = IllegalStateException.class)
	public void testActivarValoracionesDuplicated() throws IOException, RepositorioException, EntidadNoEncontrada {
	
		String id = servicioRes.create("Prueba", "30150", "Murcia", 30.00, 20.00, "alguien");
		servicioRes.activarValoraciones(id);
		servicioRes.activarValoraciones(id);

	}

	@Test
	public void testGetValoraciones() throws RepositorioException, IOException, EntidadNoEncontrada {
		
		String id = servicioRes.create("Prueba", "30150", "Murcia", 30.00, 20.00, "alguien");
		servicioRes.activarValoraciones(id);
		
		List<Valoracion> misVal = servicioRes.getValoracionesRes(id);
		
		System.out.println(misVal.toString());

		//para controlar la pequeña diferencia entre las fechas 

		LocalDateTime fechaEsperada = LocalDateTime.now();
		LocalDateTime fechaReal = misVal.get(0).getFecha();
		Duration duracion = Duration.between(fechaEsperada, fechaReal);
		//permitimos un error de 5 segundos entre una fecha y otra
		assertTrue(duracion.abs().getSeconds() < 5);


		

		assertEquals(misVal.get(0).getCorreo(), "alumno@um.es");
		assertEquals(misVal.get(0).getComentario(),null);
		assertEquals(misVal.get(0).getCalificacion(),  9.00, 0.0);

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
