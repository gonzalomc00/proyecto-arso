package arso.restaurantes;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

import repositorio.EntidadNoEncontrada;
import repositorio.FactoriaRepositorios;
import repositorio.Repositorio;
import repositorio.RepositorioException;
import restaurantes.modelo.Plato;
import restaurantes.modelo.Restaurante;
import restaurantes.modelo.SitioTuristico;
import restaurantes.servicio.IServicioRestaurante;
import restaurantes.servicio.RestauranteResumen;
import servicio.FactoriaServicios;

public class RestaurantesTest {

	private Repositorio<Restaurante, String> repositorio;
	private IServicioRestaurante servicio;

	/*
	@BeforeEach
	public void setUp() {
		repositorio = FactoriaRepositorios.getRepositorio(Restaurante.class);
		servicio = FactoriaServicios.getServicio(IServicioRestaurante.class);

	}

	@Test
	public void testRestauranteGetRestauranteByIdFailureEntidadNoEncontrada() throws RepositorioException {

		EntidadNoEncontrada thrown = Assertions.assertThrows(EntidadNoEncontrada.class, () -> {
			repositorio.getById("3");

		});
		Assertions.assertEquals("3 no existe en el repositorio", thrown.getMessage());
	}

	@Test
	public void testGetRestauranteFromRepositorio() throws RepositorioException, EntidadNoEncontrada {
		Restaurante r = repositorio.getById("1");
		Assertions.assertEquals(r.getId(), "1");

	}

	// TODO: como comprobar repositorio exception
	@Test
	public void testRepositorioException() throws EntidadNoEncontrada {
		Assertions.assertThrows(RepositorioException.class, () -> {
			// Lanzar la excepción
			throw new RepositorioException("Ocurrió un error en el repositorio");
		});
	}

	@Test
	public void testCreateRestaurante() throws RepositorioException, EntidadNoEncontrada {

		Position posicion = new Position(20, 30);
		Point coordenadas = new Point(posicion);
		String id = servicio.create("McDonals", "30820", "Alcantarilla", coordenadas);

		List<SitioTuristico> sitios = new LinkedList<>();
		List<Plato> platos = new LinkedList<>();

		Assertions.assertEquals(id, "2");
		Assertions.assertEquals(repositorio.getById("2").getNombre(), "McDonals");
		Assertions.assertEquals(repositorio.getById("2").getCiudad(), "Alcantarilla");
		Assertions.assertEquals(repositorio.getById("2").getCp(), "30820");
		Assertions.assertEquals(repositorio.getById("2").getCoordenadas(), coordenadas);

		Assertions.assertEquals(repositorio.getById("2").getPlatos(), platos);
		Assertions.assertEquals(repositorio.getById("2").getSitios(), sitios);

	}

	@Test
	public void testCreateRestauranteCoordenadasNull() throws RepositorioException {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.create("McDonals", "30820", "Alcantarilla", null);
			;

		});

		Assertions.assertEquals("coordenadas: no debe ser nulo", thrown.getMessage());

	}

	@Test
	public void testCreateRestauranteNombreVacio() {
		String nombre = "";
		Position posicion = new Position(20, 30);
		Point coordenadas = new Point(posicion);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.create(nombre, "30820", "Alcantarilla", coordenadas);
			;

		});

		Assertions.assertEquals("nombre del restaurante: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void testCreateRestauranteNombreNull() {
		Position posicion = new Position(20, 30);
		Point coordenadas = new Point(posicion);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.create(null, "30820", "Alcantarilla", coordenadas);
			;

		});

		Assertions.assertEquals("nombre del restaurante: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void testCreateRestauranteCpNull() {
		Position posicion = new Position(20, 30);
		Point coordenadas = new Point(posicion);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.create("McDonals", null, "Alcantarilla", coordenadas);
			;

		});

		Assertions.assertEquals("codigo postal: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void testCreateRestauranteCpVacio() {
		String cp = "";
		Position posicion = new Position(20, 30);
		Point coordenadas = new Point(posicion);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.create("McDonals", cp, "Alcantarilla", coordenadas);
			;

		});

		Assertions.assertEquals("codigo postal: no debe ser nulo ni vacio", thrown.getMessage());
	}

	@Test
	public void testCreateRestauranteCiudadNull() {
		Position posicion = new Position(20, 30);
		Point coordenadas = new Point(posicion);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.create("McDonals", "30820", null, coordenadas);
			;

		});

		Assertions.assertEquals("nombre de la ciudad: no debe ser nulo ni vacio", thrown.getMessage());
	}

	@Test
	public void testCreateRestauranteCiudadVacio() {

		String nombre = "";
		Position posicion = new Position(20, 30);
		Point coordenadas = new Point(posicion);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.create("McDonals", "30820", nombre, coordenadas);
			;

		});

		Assertions.assertEquals("nombre de la ciudad: no debe ser nulo ni vacio", thrown.getMessage());
	}

	@Test
	public void testAddPlatoRestaurante() throws RepositorioException, EntidadNoEncontrada {

		Plato plato = new Plato("Patatas fritas", "Patatas fritas con queso y bacon", 4.50);
		String nombre = servicio.addPlato("1", plato);

		Assertions.assertEquals(nombre, "Patatas fritas");
		Assertions.assertEquals(repositorio.getById("1").getPlatos().get(1).getNombre(), "Patatas fritas");
		Assertions.assertEquals(repositorio.getById("1").getPlatos().get(1).getDescripcion(),
				"Patatas fritas con queso y bacon");
		Assertions.assertEquals(repositorio.getById("1").getPlatos().get(1).getPrecio(), 4.50);
		Assertions.assertTrue(repositorio.getById("1").getPlatos().get(1).isDisponible());

	}

	@Test
	public void TestAddPlatoRestauranteRepetido() throws RepositorioException, EntidadNoEncontrada {
		Plato plato = new Plato("Croquetas", "Croquetas de pollo", 2.50);
		servicio.addPlato("1", plato);
		Plato plato2 = new Plato("Croquetas", "Croquetas de jamon", 1.50);

		IllegalStateException thrown = Assertions.assertThrows(IllegalStateException.class, () -> {
			servicio.addPlato("1", plato2);
			;

		});

		Assertions.assertEquals("ERROR: plato duplicado", thrown.getMessage());

	}

	@Test
	public void TestAddPlatoRestauranteNombreNull() {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.addPlato("1", new Plato(null, "Pechuga empanada", 2.50));
			;

		});

		Assertions.assertEquals("nombre del plato: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void TestAddPlatoRestauranteNombreVacio() {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.addPlato("1", new Plato("", "Pechuga empanada", 2.50));
			;

		});

		Assertions.assertEquals("nombre del plato: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void TestAddPlatoRestauranteDescVacio() {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.addPlato("1", new Plato("Pechuga", "", 2.50));
			;

		});

		Assertions.assertEquals("descripcion: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void TestAddPlatoRestauranteDescNull() {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.addPlato("1", new Plato("Pechuga", null, 2.50));
			;

		});

		Assertions.assertEquals("descripcion: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void TestAddPlatoRestaurantePrecioNull() {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.addPlato("1", new Plato("Pechuga", "Pechuga empanada", null));
			;

		});

		Assertions.assertEquals("precio: no debe ser nulo y debe ser un numero", thrown.getMessage());

	}

	@Test
	public void TestAddPlatoRestauranteNotInRepository() {

		EntidadNoEncontrada thrown = Assertions.assertThrows(EntidadNoEncontrada.class, () -> {
			servicio.addPlato("2", new Plato("Pechuga", "Pechuga empanada", 10.0));

		});
		Assertions.assertEquals("2 no existe en el repositorio", thrown.getMessage());

	}

	@Test
	public void TestRemovePlato() throws RepositorioException, EntidadNoEncontrada {
		// plato

		System.out.println(repositorio.getById("1").toString());

		Plato p1 = new Plato();
		p1.setNombre("chessy burger");
		p1.setPrecio(10.0);
		p1.setDisponibilidad(true);
		p1.setDescripcion("hamburguesa de pollo con queso");

		boolean borrado = servicio.removePlato("1", "chessy burger");

		Assertions.assertTrue(borrado);
		Assertions.assertFalse(repositorio.getById("1").getPlatos().contains(p1));

		System.out.println(repositorio.getById("1").toString());

	}

	@Test
	public void TestRemovePlatoNotInRestaurante() throws RepositorioException, EntidadNoEncontrada {

		IllegalStateException thrown = Assertions.assertThrows(IllegalStateException.class, () -> {
			servicio.removePlato("1", "Pulpo");
			;

		});

		Assertions.assertEquals("ERROR: No existe el plato en este restaurante", thrown.getMessage());

	}

	@Test
	public void TestRemovePlatoRestauranteNotInRepository() throws RepositorioException {

		EntidadNoEncontrada thrown = Assertions.assertThrows(EntidadNoEncontrada.class, () -> {
			servicio.removePlato("2", "Batatas");

		});
		Assertions.assertEquals("2 no existe en el repositorio", thrown.getMessage());
	}

	@Test
	public void TestDeleteRestaurante() throws RepositorioException, EntidadNoEncontrada {

		Position posicion = new Position(20, 30);
		Point coordenadas = new Point(posicion);
		String id = servicio.create("Prueba", "30820", "Alcantarilla", coordenadas);

		Assertions.assertEquals(id, "2");
		servicio.deleteRestaurante("2");

		EntidadNoEncontrada thrown = Assertions.assertThrows(EntidadNoEncontrada.class, () -> {
			servicio.getRestaurante("2");

		});
		Assertions.assertEquals("2 no existe en el repositorio", thrown.getMessage());

	}

	@Test
	public void TestDeleteRestauranteIdVacio() throws RepositorioException, EntidadNoEncontrada {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.deleteRestaurante("");

		});
		Assertions.assertEquals("id del restaurante: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void TestDeleteRestauranteIdNull() throws RepositorioException, EntidadNoEncontrada {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.deleteRestaurante(null);

		});
		Assertions.assertEquals("id del restaurante: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void TestGetRestaurante() throws RepositorioException, EntidadNoEncontrada {
		Restaurante r = servicio.getRestaurante("1");

		Assertions.assertEquals(r.getNombre(), "Burger Queen");
		Assertions.assertEquals(r.getCiudad(), "Murcia");
		Assertions.assertEquals(r.getCp(), "30150");

	}

	@Test
	public void TestGetRestauranteIdNull() throws RepositorioException, EntidadNoEncontrada {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.getRestaurante(null);
		});
		Assertions.assertEquals("id del restaurante: no debe ser nulo ni vacio", thrown.getMessage());
	}
	
	
	@Test
	public void TestGetRestauranteIdVacio() throws RepositorioException, EntidadNoEncontrada {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.getRestaurante("");
		});
		Assertions.assertEquals("id del restaurante: no debe ser nulo ni vacio", thrown.getMessage());
	}
	
	@Test
	public void TestGetListadoRestaurantes() throws RepositorioException, EntidadNoEncontrada {
		
		Restaurante r = servicio.getRestaurante("1");
		
		List<RestauranteResumen> resumenes = servicio.getListadoRestaurantes();
	
		Assertions.assertEquals(resumenes.size(), 1);
		System.out.println(resumenes.get(0).toString());
		//Assertions.assertEquals(resumenes.get(0).getNombre(), r.getNombre());
	
	}

}
