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
	
/**
	private Repositorio<Restaurante, String> repositorio;
	private IServicioRestaurante servicio;

	@BeforeEach
	public void setUp() throws RepositorioException, EntidadNoEncontrada {
		repositorio = FactoriaRepositorios.getRepositorio(Restaurante.class);
		servicio = FactoriaServicios.getServicio(IServicioRestaurante.class);
	}

	// ------------------- TESTS EXCEPCIONES -------------------------
	@Test
	public void testRestauranteGetRestauranteByIdFailureEntidadNoEncontrada() throws RepositorioException {

		String id = "57";
		EntidadNoEncontrada thrown = Assertions.assertThrows(EntidadNoEncontrada.class, () -> {
			repositorio.getById(id);

		});
		Assertions.assertEquals(id+ " no existe en el repositorio", thrown.getMessage());
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

	// ---------------------- createRestaurante() --------------------
	@Test
	public void testCreateRestaurante() throws RepositorioException, EntidadNoEncontrada {

		Position posicion = new Position(20, 30);
		Point coordenadas = new Point(posicion);
		String id = servicio.create("McDonals", "30820", "Alcantarilla", coordenadas);

		List<SitioTuristico> sitios = new LinkedList<>();
		List<Plato> platos = new LinkedList<>();

		Assertions.assertEquals(repositorio.getById(id).getNombre(), "McDonals");
		Assertions.assertEquals(repositorio.getById(id).getCiudad(), "Alcantarilla");
		Assertions.assertEquals(repositorio.getById(id).getCp(), "30820");
		Assertions.assertEquals(repositorio.getById(id).getCoordenadas(), coordenadas);

		Assertions.assertEquals(repositorio.getById(id).getPlatos(), platos);
		Assertions.assertEquals(repositorio.getById(id).getSitios(), sitios);

		// Borramos el restaurante para asegurarnos que solo exista uno en el
		// repositorio en caso de ejecutar todos los tests a la vez
		servicio.deleteRestaurante("2");

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
		Position posicion = new Position(20, 30);
		Point coordenadas = new Point(posicion);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.create("McDonals", "30820", "", coordenadas);
			;

		});

		Assertions.assertEquals("nombre de la ciudad: no debe ser nulo ni vacio", thrown.getMessage());
	}

// ------------------- TESTS addPlato() -----------------------
	@Test
	public void testAddPlatoRestaurante() throws RepositorioException, EntidadNoEncontrada {

		String nombre = servicio.addPlato("1", "Patatas fritas", "Patatas fritas con queso y bacon", "4.50", true);

		Assertions.assertEquals(nombre, "Patatas fritas");
		Assertions.assertEquals(repositorio.getById("1").getPlatos().get(1).getNombre(), "Patatas fritas");
		Assertions.assertEquals(repositorio.getById("1").getPlatos().get(1).getDescripcion(),
				"Patatas fritas con queso y bacon");
		Assertions.assertEquals(repositorio.getById("1").getPlatos().get(1).getPrecio(), 4.50);
		Assertions.assertTrue(repositorio.getById("1").getPlatos().get(1).isDisponibilidad());

	}

	@Test
	public void TestAddPlatoRestauranteRepetido() throws RepositorioException, EntidadNoEncontrada {
		servicio.addPlato("1", "Croquetas", "Croquetas de pollo", "2.50", true);

		IllegalStateException thrown = Assertions.assertThrows(IllegalStateException.class, () -> {
			servicio.addPlato("1", "Croquetas", "Croquetas de jamon", "1.50", true);
			;

		});

		Assertions.assertEquals("ERROR: plato duplicado", thrown.getMessage());

	}

	@Test
	public void TestAddPlatoRestauranteNombreNull() {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.addPlato("1", null, "Pechuga empanada", "2.50", true);
			;

		});

		Assertions.assertEquals("nombre del plato: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void TestAddPlatoRestauranteNombreVacio() {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.addPlato("1", "", "Pechuga empanada", "2.50", true);
			;

		});

		Assertions.assertEquals("nombre del plato: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void TestAddPlatoRestauranteDescVacio() {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.addPlato("1", "Pechuga", "", "2.50", true);
			;

		});

		Assertions.assertEquals("descripcion del plato: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void TestAddPlatoRestauranteDescNull() {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.addPlato("1", "Pechuga", null, "2.50", true);
			;

		});

		Assertions.assertEquals("descripcion del plato: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void TestAddPlatoRestaurantePrecioNull() {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.addPlato("1", "Pechuga", "Pechuga empanada", null, true);
			;

		});

		Assertions.assertEquals("precio del plato: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void TestAddPlatoRestauranteNotInRepository() {

		EntidadNoEncontrada thrown = Assertions.assertThrows(EntidadNoEncontrada.class, () -> {
			servicio.addPlato("28", "Pechuga", "Pechuga empanada", "10.0", true);

		});
		Assertions.assertEquals("28 no existe en el repositorio", thrown.getMessage());

	}

	// ------------------- TESTS removePlato() -----------------------
	@Test
	public void TestRemovePlato() throws RepositorioException, EntidadNoEncontrada {
		// plato
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
			servicio.removePlato("87", "Batatas");

		});
		Assertions.assertEquals("87 no existe en el repositorio", thrown.getMessage());
	}

	@Test
	public void TestRemovePlatoRestauranteIdNull() {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.removePlato(null, "Batatas");

		});
		Assertions.assertEquals("id del restaurante: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void TestRemovePlatoRestauranteIdVacio() {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.removePlato("", "Batatas");

		});
		Assertions.assertEquals("id del restaurante: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void TestRemovePlatoNombreVacio() {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.removePlato("2", "");

		});
		Assertions.assertEquals("nombre del plato: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void TestRemovePlatoNombreNull() {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.removePlato("2", null);

		});
		Assertions.assertEquals("nombre del plato: no debe ser nulo ni vacio", thrown.getMessage());

	}

	// ----------------------- TESTS deleteRestaurante()

	@Test
	public void TestDeleteRestaurante() throws RepositorioException, EntidadNoEncontrada {

		Position posicion = new Position(20, 30);
		Point coordenadas = new Point(posicion);

		String id = servicio.create("Prueba", "30820", "Alcantarilla", coordenadas);

		servicio.deleteRestaurante(id);

		EntidadNoEncontrada thrown = Assertions.assertThrows(EntidadNoEncontrada.class, () -> {
			servicio.getRestaurante(id);
		});

		Assertions.assertEquals(id + " no existe en el repositorio", thrown.getMessage());

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

	// ------------- TESTS getRestaurante()

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

	// ------- TESTS getListadoRestaurantes()
	@Test
	public void TestGetListadoRestaurantes() throws RepositorioException, EntidadNoEncontrada {

		Restaurante r = servicio.getRestaurante("1");

		List<RestauranteResumen> resumenes = servicio.getListadoRestaurantes();

		// Assertions.assertEquals(resumenes.size(), 1);
		// System.out.println(resumenes.get(0).toString());
		Assertions.assertEquals(resumenes.get(0).getNombre(), r.getNombre());
		Assertions.assertEquals(resumenes.get(0).getCp(), r.getCp());
		Assertions.assertEquals(resumenes.get(0).getId(), r.getId());

	}

	// ---------- TESTS updatePlato() --------------------

	@Test
	public void TestUpdatePlato() throws RepositorioException, EntidadNoEncontrada {

		servicio.addPlato("1", "Plato de prueba2", "asdfgh", "8.0", true);
		servicio.updatePlato("1", "Plato de prueba2", "prueba exitosa", "10.0", true);

		Assertions.assertEquals(servicio.getRestaurante("1").getPlatos().get(3).getNombre(), "Plato de prueba2");
		Assertions.assertEquals(servicio.getRestaurante("1").getPlatos().get(3).getDescripcion(), "prueba exitosa");
		Assertions.assertEquals(servicio.getRestaurante("1").getPlatos().get(3).getPrecio(), 10.0);

	}

	@Test
	public void TestUpdatePlatoIdNull() throws RepositorioException, EntidadNoEncontrada {

		servicio.addPlato("1", "Plato de prueba", "asdfgh", "8.0", true);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.updatePlato(null, "Plato de prueba", "prueba exitosa", "10.0", true);
		});
		Assertions.assertEquals("id del restaurante: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void TestUpdatePlatoIdVacio() throws RepositorioException, EntidadNoEncontrada {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.updatePlato("", "Plato de prueba", "prueba exitosa", "10.0", true);
		});
		Assertions.assertEquals("id del restaurante: no debe ser nulo ni vacio", thrown.getMessage());
	}

	@Test
	public void TestUpdateNombrePlatoNull() throws RepositorioException, EntidadNoEncontrada {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.updatePlato("1", null, "prueba exitosa", "10.0", true);
		});
		Assertions.assertEquals("nombre del plato: no debe ser nulo ni vacio", thrown.getMessage());
	}

	@Test
	public void TestUpdateDescripcionPlatoNull() throws RepositorioException, EntidadNoEncontrada {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.updatePlato("1", "Plato de prueba", null, "10.0", true);
		});
		Assertions.assertEquals("descripcion del plato: no debe ser nulo ni vacio", thrown.getMessage());
	}

	@Test
	public void TestUpdatePrecioPlatoNull() throws RepositorioException, EntidadNoEncontrada {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.updatePlato("1", "Plato de prueba", "prueba existosa", null, true);
		});
		Assertions.assertEquals("precio del plato: no debe ser nulo ni vacio", thrown.getMessage());
	}

	@Test
	public void TestUpdatePlatoNotInRestaurante() throws RepositorioException, EntidadNoEncontrada {

		IllegalStateException thrown = Assertions.assertThrows(IllegalStateException.class, () -> {
			servicio.updatePlato("1", "Calabazas", "prueba exitosa", "10.0", true);
		});
		Assertions.assertEquals("plato: no existe en este restaurante", thrown.getMessage());
	}

	// --------------------------- TEST update() del Restaurante -----------------

	@Test
	public void TestUpdateRestaurantes() throws RepositorioException, EntidadNoEncontrada {

		Position posicion = new Position(20, 30);
		Point coordenadas = new Point(posicion);

		String id = servicio.create("KFC", "Murcia", "30150", coordenadas);

		servicio.update(id, "KGB", "Valencia", "30016", "3, 4");

		Assertions.assertEquals(servicio.getRestaurante(id).getNombre(), "KGB");
		Assertions.assertEquals(servicio.getRestaurante(id).getCiudad(), "Valencia");
		Assertions.assertEquals(servicio.getRestaurante(id).getCp(), "30016");
	}
	
	@Test
	public void TestUpdateRestaurantesIdNull() throws RepositorioException, EntidadNoEncontrada {

		Position posicion = new Position(20, 30);
		Point coordenadas = new Point(posicion);

		String id = servicio.create("KFC", "Murcia", "30150", coordenadas);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.update(null, "KGB", "Valencia", "30016", "3, 4");

		});
		Assertions.assertEquals("id del restaurante: no debe ser nulo ni vacio", thrown.getMessage());
	}
	
	@Test
	public void TestUpdateRestaurantesIdVacio() throws RepositorioException, EntidadNoEncontrada {

		Position posicion = new Position(20, 30);
		Point coordenadas = new Point(posicion);

		String id = servicio.create("KFC", "Murcia", "30150", coordenadas);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.update("", "KGB", "Valencia", "30016", "3, 4");

		});
		Assertions.assertEquals("id del restaurante: no debe ser nulo ni vacio", thrown.getMessage());
	}
	

	@Test
	public void TestUpdateRestaurantesNombreNull() throws RepositorioException, EntidadNoEncontrada {
		Position posicion = new Position(20, 30);
		Point coordenadas = new Point(posicion);

		String id = servicio.create("KFC", "Murcia", "30150", coordenadas);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.update(id, null, "Valencia", "30016", "3, 4");

		});
		Assertions.assertEquals("nombre del  restaurante modificado: no debe ser nulo ni vacio", thrown.getMessage());

	}
	
	@Test
	public void TestUpdateRestaurantesNombreVacio() throws RepositorioException, EntidadNoEncontrada {
		Position posicion = new Position(20, 30);
		Point coordenadas = new Point(posicion);

		String id = servicio.create("KFC", "Murcia", "30150", coordenadas);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.update(id, "", "Valencia", "30016", "3, 4");

		});
		Assertions.assertEquals("nombre del  restaurante modificado: no debe ser nulo ni vacio", thrown.getMessage());

	}
	
	@Test
	public void TestUpdateRestaurantesCiudadVacio() throws RepositorioException, EntidadNoEncontrada {
		Position posicion = new Position(20, 30);
		Point coordenadas = new Point(posicion);

		String id = servicio.create("KFC", "Murcia", "30150", coordenadas);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.update(id, "KGB", "", "30016", "3, 4");

		});
		Assertions.assertEquals("ciudad del restaurante modificado: no debe ser nulo ni vacio", thrown.getMessage());

	}
	
	@Test
	public void TestUpdateRestaurantesCiudadNull() throws RepositorioException, EntidadNoEncontrada {
		Position posicion = new Position(20, 30);
		Point coordenadas = new Point(posicion);

		String id = servicio.create("KFC", "Murcia", "30150", coordenadas);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.update(id, "KGB", null, "30016", "3, 4");

		});
		Assertions.assertEquals("ciudad del restaurante modificado: no debe ser nulo ni vacio", thrown.getMessage());
	}
	
	@Test
	public void TestUpdateRestaurantesCpNull() throws RepositorioException, EntidadNoEncontrada {
		Position posicion = new Position(20, 30);
		Point coordenadas = new Point(posicion);

		String id = servicio.create("KFC", "Murcia", "30150", coordenadas);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.update(id, "KGB", "Alcantarilla", null, "3, 4");

		});
		Assertions.assertEquals("cp del restaurante modificado: no debe ser nulo ni vacio", thrown.getMessage());
	}
	
	@Test
	public void TestUpdateRestaurantesCpVacio() throws RepositorioException, EntidadNoEncontrada {
		Position posicion = new Position(20, 30);
		Point coordenadas = new Point(posicion);

		String id = servicio.create("KFC", "Murcia", "30150", coordenadas);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.update(id, "KGB", "Alcantarilla", "", "3, 4");

		});
		Assertions.assertEquals("cp del restaurante modificado: no debe ser nulo ni vacio", thrown.getMessage());
	}
	
	@Test
	public void TestUpdateRestaurantesCoordenadasVacio() throws RepositorioException, EntidadNoEncontrada {
		Position posicion = new Position(20, 30);
		Point coordenadas = new Point(posicion);

		String id = servicio.create("KFC", "Murcia", "30150", coordenadas);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.update(id, "KGB", "Alcantarilla", "30155", "");

		});
		Assertions.assertEquals("coordenadas del restaurante modificado: no debe ser nulo ni vacio", thrown.getMessage());
	}
	
	@Test
	public void TestUpdateRestaurantesCoordenadasNull() throws RepositorioException, EntidadNoEncontrada {
		Position posicion = new Position(20, 30);
		Point coordenadas = new Point(posicion);

		String id = servicio.create("KFC", "Murcia", "30150", coordenadas);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.update(id, "KGB", "Alcantarilla", "30155", null);

		});
		Assertions.assertEquals("coordenadas del restaurante modificado: no debe ser nulo ni vacio", thrown.getMessage());
	}
	
	
	*/

	

}
