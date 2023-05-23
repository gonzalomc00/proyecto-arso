package arso.restaurantes;



import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import repositorio.EntidadNoEncontrada;
import repositorio.FactoriaRepositorios;
import repositorio.Repositorio;
import repositorio.RepositorioException;
import restaurantes.modelo.Plato;
import restaurantes.modelo.Restaurante;
import restaurantes.modelo.SitioTuristico;
import restaurantes.modelo.Valoracion;
import restaurantes.servicio.IServicioRestaurante;
import restaurantes.servicio.RestauranteResumen;
import servicio.FactoriaServicios;


/**
 * Con la etiqueta @Disabled ya no se ejecutará esta clase para las pruebas de
 * maven install.
 * 
 * @author sofia
 */

// La conexion con RabbitMQ genera fallos ya que se establece la conexion con la cola de mensajes en el constructor del Servicio

public class RestaurantesTest {

	private IServicioRestaurante servicio;
	private Repositorio<Restaurante, String> repositorio;
	private String u;

	@BeforeEach
	public void setUp() {
		servicio = FactoriaServicios.getServicio(IServicioRestaurante.class);
		repositorio = FactoriaRepositorios.getRepositorio(Restaurante.class);
		u = "alguien";
	}


	// --------------------------------------------------------------
	// ------------------- TESTS EXCEPCIONES ------------------------
	// --------------------------------------------------------------

	@Test
	public void testRestauranteGetRestauranteByIdFailureEntidadNoEncontrada() throws RepositorioException {

		String id = "57";
		EntidadNoEncontrada thrown = Assertions.assertThrows(EntidadNoEncontrada.class, () -> {
			repositorio.getById(id);

		});
		Assertions.assertEquals(id + " no existe en el repositorio", thrown.getMessage());
	}

	@Test
	public void testGetRestauranteFromRepositorio() throws RepositorioException, EntidadNoEncontrada {
		Restaurante r = repositorio.getById("1");
		Assertions.assertEquals(r.getId(), "1");

	}

	// RepositorioException no es posible de comprobar

	// ------------------------------------------------------
	// ------------------ TESTS OPINIONES -------------------
	// ------------------------------------------------------

	@Test
	public void testActivarValoracionIdNull() {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.activarValoraciones(null, u);
		});
		Assertions.assertEquals("id del restaurante: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void testActivarValoraciones() throws RepositorioException, IOException, EntidadNoEncontrada {

		String id = servicio.create("Prueba", "30150", "Murcia", 30.00, 20.00, u);
		servicio.activarValoraciones(id, u);

		Restaurante r = servicio.getRestaurante(id);

		Assertions.assertEquals("idOpinion", r.getResumenValoracion().getIdOpinion());
		// Borramos el restaurante para asegurarnos que solo exista uno en el
		// repositorio en caso de ejecutar todos los tests a la vez
		servicio.deleteRestaurante(id, u);
	}
	@Test
	public void testActivarValoracionesNotGestor() throws RepositorioException, IOException, EntidadNoEncontrada {

		String id = servicio.create("Prueba", "30150", "Murcia", 30.00, 20.00, u);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.activarValoraciones(id, "ssss");
		});

		Assertions.assertEquals("No eres el gestor del restaurante", thrown.getMessage());
		servicio.deleteRestaurante(id, u);
	}

	@Test
	public void testActivarValoracionesDuplicated() throws IOException, RepositorioException, EntidadNoEncontrada {

		String id = servicio.create("Prueba", "30150", "Murcia", 30.00, 20.00, u);
		servicio.activarValoraciones(id,u);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.activarValoraciones(id,u);
		});
		Assertions.assertEquals("El restaurante ya cuenta con valoraciones creadas", thrown.getMessage());

		// Borramos el restaurante para asegurarnos que solo exista uno en el
		// repositorio en caso de ejecutar todos los tests a la vez
		servicio.deleteRestaurante(id, "alguien");

	}

	@Test
	public void testGetValoraciones() throws RepositorioException, IOException, EntidadNoEncontrada {

		String id = servicio.create("Prueba", "30150", "Murcia", 30.00, 20.00, "alguien");
		servicio.activarValoraciones(id, u);

		List<Valoracion> misVal = servicio.getValoracionesRes(id);

		System.out.println(misVal.toString());

		// para controlar la pequeña diferencia entre las fechas

		LocalDateTime fechaEsperada = LocalDateTime.now();
		LocalDateTime fechaReal = misVal.get(0).getFecha();
		Duration duracion = Duration.between(fechaEsperada, fechaReal);
		// permitimos un error de 5 segundos entre una fecha y otra
		Assertions.assertTrue(duracion.abs().getSeconds() < 5);

		Assertions.assertEquals(misVal.get(0).getCorreo(), "alumno@um.es");
		Assertions.assertEquals(misVal.get(0).getComentario(), null);
		Assertions.assertEquals(misVal.get(0).getCalificacion(), 9.00, 0.0);

		// Borramos el restaurante para asegurarnos que solo exista uno en el
		// repositorio en caso de ejecutar todos los tests a la vez
		servicio.deleteRestaurante(id, "alguien");

	}

	@Test
	public void testGetValoracionesNotVal() throws RepositorioException, IOException, EntidadNoEncontrada {
		String id = servicio.create("Prueba", "30150", "Murcia", 30.00, 20.00, "alguien");

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.getValoracionesRes(id);
		});
		Assertions.assertEquals("El restaurante no tiene valoraciones activadas", thrown.getMessage());
		servicio.deleteRestaurante(id, "alguien");

	}
	
	@Test
	public void testGetValoracionesIdNull() throws RepositorioException, IOException, EntidadNoEncontrada {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.getValoracionesRes(null);
		});
		Assertions.assertEquals("id del restaurante: no debe ser nulo ni vacio", thrown.getMessage());

	}

	// -------------------------------------------------------
	// ----------------- TEST RESTAURANTES -------------------
	// -------------------------------------------------------
	// ---------------------- createRestaurante() --------------------
	@Test
	public void testCreateRestaurante() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.create("McDonals", "30820", "Alcantarilla", 20.00, 30.00, u);

		List<SitioTuristico> sitios = new LinkedList<>();
		List<Plato> platos = new LinkedList<>();

		Assertions.assertEquals(repositorio.getById(id).getNombre(), "McDonals");
		Assertions.assertEquals(repositorio.getById(id).getCiudad(), "Alcantarilla");
		Assertions.assertEquals(repositorio.getById(id).getCp(), "30820");
		Assertions.assertEquals(repositorio.getById(id).getLatitud(), 20.00);
		Assertions.assertEquals(repositorio.getById(id).getLongitud(), 30.00);
		Assertions.assertEquals(repositorio.getById(id).getGestor(), u);

		Assertions.assertEquals(repositorio.getById(id).getPlatos(), platos);
		Assertions.assertEquals(repositorio.getById(id).getSitios(), sitios);
		// Borramos el restaurante para asegurarnos que solo exista uno en el
		// repositorio en caso de ejecutar todos los tests a la vez
		servicio.deleteRestaurante(id, u);
	}

	@Test
	public void testCreateRestauranteLatitudNull() throws RepositorioException {
		String u = "alguien";
		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.create("McDonals", "30820", "Alcantarilla", null, 2.0, u);
			;

		});
		Assertions.assertEquals("latitud: no debe ser nulo", thrown.getMessage());

	}

	@Test
	public void testCreateRestauranteLongitudNull() throws RepositorioException {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.create("McDonals", "30820", "Alcantarilla", 2.0, null, u);
			;

		});
		Assertions.assertEquals("longitud: no debe ser nulo", thrown.getMessage());

	}

	@Test
	public void testCreateRestauranteNombreVacio() {
		String nombre = "";
		String u = "alguien";
		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.create(nombre, "30820", "Alcantarilla", 2.00, 2.00, u);
			;

		});

		Assertions.assertEquals("nombre del restaurante: no debe ser nulo ni vacio", thrown.getMessage());
	}

	@Test
	public void testCreateRestauranteNombreNull() {
		String nombre = null;

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.create(nombre, "30820", "Alcantarilla", 2.00, 2.00, u);
			;

		});

		Assertions.assertEquals("nombre del restaurante: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void testCreateRestauranteCpNull() {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.create("McDonals", null, "Alcantarilla", 20.00, 30.00, u);
			;

		});

		Assertions.assertEquals("codigo postal: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void testCreateRestauranteCpVacio() {
		String cp = "";

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.create("McDonals", cp, "Alcantarilla", 20.00, 30.00, u);
			;

		});

		Assertions.assertEquals("codigo postal: no debe ser nulo ni vacio", thrown.getMessage());
	}

	@Test
	public void testCreateRestauranteCiudadNull() {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.create("McDonals", "30820", null, 20.00, 30.00, u);
			;

		});

		Assertions.assertEquals("nombre de la ciudad: no debe ser nulo ni vacio", thrown.getMessage());
	}

	@Test
	public void testCreateRestauranteCiudadVacio() {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.create("McDonals", "30820", "", 20.00, 30.00, u);
			;

		});

		Assertions.assertEquals("nombre de la ciudad: no debe ser nulo ni vacio", thrown.getMessage());
	}

	@Test
	public void testCreateRestauranteGestorVacio() {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.create("McDonals", "30820", "Ciudadela", 20.00, 30.00, "");
			;

		});

		Assertions.assertEquals("gestor: no debe ser nulo ni vacio", thrown.getMessage());
	}

	// ------------------- TESTS addPlato() -----------------------
	@Test
	public void testAddPlatoRestaurante() throws RepositorioException, EntidadNoEncontrada {

		String id = servicio.create("McDonals", "30820", "Alcantarilla", 20.00, 30.00, u);

		String nombre = servicio.addPlato(id, "Patatas fritas", "Patatas fritas con queso y bacon", "4.50", true, u);

		Assertions.assertEquals(nombre, "Patatas fritas");
		Assertions.assertEquals(repositorio.getById(id).getPlatos().get(0).getNombre(), "Patatas fritas");
		Assertions.assertEquals(repositorio.getById(id).getPlatos().get(0).getDescripcion(),
				"Patatas fritas con queso y bacon");
		Assertions.assertEquals(repositorio.getById(id).getPlatos().get(0).getPrecio(), 4.50);
		Assertions.assertTrue(repositorio.getById(id).getPlatos().get(0).isDisponibilidad());

		servicio.deleteRestaurante(id, u);
	}

	@Test
	public void testAddPlatoRestauranteRepetido() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.create("McDonals", "30820", "Alcantarilla", 20.00, 30.00, u);

		servicio.addPlato(id, "Croquetas", "Croquetas de pollo", "2.50", true, u);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.addPlato(id, "Croquetas", "Croquetas de jamon", "1.50", true, u);
			;

		});

		Assertions.assertEquals("ERROR: plato duplicado", thrown.getMessage());
		servicio.deleteRestaurante(id, u);

	}

	@Test
	public void testAddPlatoNoGestor() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.create("McDonals", "30820", "Alcantarilla", 20.00, 30.00, u);
		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.addPlato(id, "Croquetas", "Croquetas de jamon", "1.50", true, "someone");
			;

		});
		Assertions.assertEquals("No eres el gestor del restaurante", thrown.getMessage());
		servicio.deleteRestaurante(id, u);
	}

	@Test
	public void TestAddPlatoRestauranteNombreNull() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.create("McDonals", "30820", "Alcantarilla", 20.00, 30.00, u);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.addPlato(id, null, "Pechuga empanada", "2.50", true, u);
			;

		});

		Assertions.assertEquals("nombre del plato: no debe ser nulo ni vacio", thrown.getMessage());
		servicio.deleteRestaurante(id, u);

	}

	@Test
	public void TestAddPlatoRestauranteNombreVacio() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.create("McDonals", "30820", "Alcantarilla", 20.00, 30.00, u);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.addPlato(id, "", "Pechuga empanada", "2.50", true, u);
			;

		});

		Assertions.assertEquals("nombre del plato: no debe ser nulo ni vacio", thrown.getMessage());
		servicio.deleteRestaurante(id, u);

	}

	@Test
	public void TestAddPlatoRestauranteDescVacio() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.create("McDonals", "30820", "Alcantarilla", 20.00, 30.00, u);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.addPlato(id, "Pechuga", "", "2.50", true, u);
			;

		});

		Assertions.assertEquals("descripcion del plato: no debe ser nulo ni vacio", thrown.getMessage());
		servicio.deleteRestaurante(id, u);

	}

	@Test
	public void TestAddPlatoRestauranteDescNull() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.create("McDonals", "30820", "Alcantarilla", 20.00, 30.00, u);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.addPlato(id, "Pechuga", null, "2.50", true, u);
			;

		});

		Assertions.assertEquals("descripcion del plato: no debe ser nulo ni vacio", thrown.getMessage());
		servicio.deleteRestaurante(id, u);

	}

	@Test
	public void TestAddPlatoRestaurantePrecioNull() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.create("McDonals", "30820", "Alcantarilla", 20.00, 30.00, u);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.addPlato(id, "Pechuga", "Pechuga empanada", null, true, u);
			;

		});

		Assertions.assertEquals("precio del plato: no debe ser nulo ni vacio", thrown.getMessage());
		servicio.deleteRestaurante(id, u);

	}
	
	@Test
	public void TestAddPlatoRestauranteIdResVacio() throws RepositorioException, EntidadNoEncontrada {
		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.addPlato(null, "Pechuga", "Pechuga empanada", "2.0", true, u);
			;

		});

		Assertions.assertEquals("id del restaurante: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void TestAddPlatoRestauranteNotInRepository() throws RepositorioException, EntidadNoEncontrada {

		String id = servicio.create("McDonals", "30820", "Alcantarilla", 20.00, 30.00, u);

		EntidadNoEncontrada thrown = Assertions.assertThrows(EntidadNoEncontrada.class, () -> {
			servicio.addPlato("28", "Pechuga", "Pechuga empanada", "10.0", true, u);

		});
		Assertions.assertEquals("28 no existe en el repositorio", thrown.getMessage());
		servicio.deleteRestaurante(id, u);
	}

	// ------------------- TESTS removePlato() -----------------------
	@Test
	public void TestRemovePlato() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.create("McDonals", "30820", "Alcantarilla", 20.00, 30.00, u);

		// plato
		Plato p1 = new Plato();
		p1.setNombre("chessy burger");
		p1.setPrecio(10.0);
		p1.setDisponibilidad(true);
		p1.setDescripcion("hamburguesa de pollo con queso");

		LinkedList<Plato> platos = new LinkedList<Plato>();
		platos.add(p1);
		servicio.getRestaurante(id).setPlatos(platos);
		boolean borrado = servicio.removePlato(id, "chessy burger", u);

		Assertions.assertTrue(borrado);
		Assertions.assertFalse(repositorio.getById(id).getPlatos().contains(p1));

		servicio.deleteRestaurante(id, u);

	}

	@Test
	public void TestRemovePlatoNotGestor() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.create("McDonals", "30820", "Alcantarilla", 20.00, 30.00, u);

		// plato
		Plato p1 = new Plato();
		p1.setNombre("chessy burger");
		p1.setPrecio(10.0);
		p1.setDisponibilidad(true);
		p1.setDescripcion("hamburguesa de pollo con queso");

		LinkedList<Plato> platos = new LinkedList<Plato>();
		platos.add(p1);
		servicio.getRestaurante(id).setPlatos(platos);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.removePlato(id, "chessy burger", "claudia");
			;

		});
		Assertions.assertEquals("No eres el gestor del restaurante", thrown.getMessage());

		servicio.deleteRestaurante(id, u);

	}

	@Test
	public void TestRemovePlatoNotInRestaurante() throws RepositorioException, EntidadNoEncontrada {

		String id = servicio.create("McDonals", "30820", "Alcantarilla", 20.00, 30.00, u);

		EntidadNoEncontrada thrown = Assertions.assertThrows(EntidadNoEncontrada.class, () -> {
			servicio.removePlato(id, "Pulpo", u);
			;

		});

		Assertions.assertEquals("ERROR: No existe el plato en este restaurante", thrown.getMessage());
		servicio.deleteRestaurante(id, u);

	}

	@Test
	public void TestRemovePlatoRestauranteNotInRepository() throws RepositorioException, EntidadNoEncontrada {

		String id = servicio.create("McDonals", "30820", "Alcantarilla", 20.00, 30.00, u);

		EntidadNoEncontrada thrown = Assertions.assertThrows(EntidadNoEncontrada.class, () -> {
			servicio.removePlato("87", "Batatas", u);

		});
		Assertions.assertEquals("87 no existe en el repositorio", thrown.getMessage());
		servicio.deleteRestaurante(id, u);

	}

	@Test
	public void TestRemovePlatoRestauranteIdNull() throws RepositorioException, EntidadNoEncontrada {

		String id = servicio.create("McDonals", "30820", "Alcantarilla", 20.00, 30.00, u);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.removePlato(null, "Batatas", u);

		});
		Assertions.assertEquals("id del restaurante: no debe ser nulo ni vacio", thrown.getMessage());

		servicio.deleteRestaurante(id, u);

	}

	@Test
	public void TestRemovePlatoRestauranteIdVacio() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.create("McDonals", "30820", "Alcantarilla", 20.00, 30.00, u);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.removePlato("", "Batatas", u);

		});
		Assertions.assertEquals("id del restaurante: no debe ser nulo ni vacio", thrown.getMessage());
		servicio.deleteRestaurante(id, u);

	}

	@Test
	public void TestRemovePlatoNombreVacio() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.create("McDonals", "30820", "Alcantarilla", 20.00, 30.00, u);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.removePlato(id, "", u);

		});
		Assertions.assertEquals("nombre del plato: no debe ser nulo ni vacio", thrown.getMessage());
		servicio.deleteRestaurante(id, u);

	}

	@Test
	public void TestRemovePlatoNombreNull() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.create("McDonals", "30820", "Alcantarilla", 20.00, 30.00, u);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.removePlato(id, null, u);

		});
		Assertions.assertEquals("nombre del plato: no debe ser nulo ni vacio", thrown.getMessage());
		servicio.deleteRestaurante(id, u);

	}

	// ----------------------- TESTS deleteRestaurante()
	// -----------------------------

	@Test
	public void testDeleteRestaurante() throws RepositorioException, EntidadNoEncontrada {

		String id = servicio.create("Prueba", "30820", "Alcantarilla", 20.00, 30.00, u);

		servicio.deleteRestaurante(id, u);

		EntidadNoEncontrada thrown = Assertions.assertThrows(EntidadNoEncontrada.class, () -> {
			servicio.getRestaurante(id);
		});

		Assertions.assertEquals(id + " no existe en el repositorio", thrown.getMessage());

	}

	@Test
	public void testDeleteRestauranteIdVacio() throws RepositorioException, EntidadNoEncontrada {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.deleteRestaurante("", u);

		});
		Assertions.assertEquals("id del restaurante: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void testDeleteRestauranteIdNull() throws RepositorioException, EntidadNoEncontrada {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.deleteRestaurante(null, u);

		});
		Assertions.assertEquals("id del restaurante: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void testDeleteRestauranteNotGestor() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.create("Prueba", "30820", "Alcantarilla", 20.00, 30.00, u);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.deleteRestaurante(id, "claudia");
		});

		Assertions.assertEquals("No eres el gestor del restaurante", thrown.getMessage());
		servicio.deleteRestaurante(id, u);

	}
	// ------------- TESTS getRestaurante()

	@Test
	public void testGetRestaurante() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.create("Prueba", "30820", "Alcantarilla", 20.00, 30.00, u);

		Restaurante r = servicio.getRestaurante(id);

		Assertions.assertEquals(r.getNombre(), "Prueba");
		Assertions.assertEquals(r.getCiudad(), "Alcantarilla");
		Assertions.assertEquals(r.getCp(), "30820");
		Assertions.assertEquals(r.getGestor(), u);
		Assertions.assertEquals(r.getLatitud(), 20.00);
		Assertions.assertEquals(r.getLongitud(), 30.00);
		servicio.deleteRestaurante(id, u);

	}

	@Test
	public void testGetRestauranteIdNull() throws RepositorioException, EntidadNoEncontrada {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.getRestaurante(null);
		});
		Assertions.assertEquals("id del restaurante: no debe ser nulo ni vacio", thrown.getMessage());
	}

	@Test
	public void testGetRestauranteIdVacio() throws RepositorioException, EntidadNoEncontrada {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.getRestaurante("");
		});
		Assertions.assertEquals("id del restaurante: no debe ser nulo ni vacio", thrown.getMessage());
	}

	// ------- TESTS getListadoRestaurantes()
	@Test
	public void testGetListadoRestaurantes() throws RepositorioException, EntidadNoEncontrada {

		Restaurante r = servicio.getRestaurante("1");

		List<RestauranteResumen> resumenes = servicio.getListadoRestaurantes();

		Assertions.assertEquals(resumenes.size(), 1);
		Assertions.assertEquals(resumenes.get(0).getNombre(), r.getNombre());
		Assertions.assertEquals(resumenes.get(0).getCp(), r.getCp());
		Assertions.assertEquals(resumenes.get(0).getId(), r.getId());
		Assertions.assertEquals(resumenes.get(0).getGestor(), r.getGestor());

		
	}
	
	@Test
	public void testGetListadoRestaurantesResumenVal() throws RepositorioException, EntidadNoEncontrada, IOException {

		String id = servicio.create("Prueba", "30820", "Alcantarilla", 20.00, 30.00, u);
		Restaurante r = servicio.getRestaurante(id);
		servicio.activarValoraciones(id,u);
		
		List<RestauranteResumen> resumenes = servicio.getListadoRestaurantes();
		Assertions.assertEquals(resumenes.size(), 2); //la primera posicion la ocupa el restaurante que ya está en memoria 
		Assertions.assertEquals(resumenes.get(1).getNombre(), r.getNombre());
		Assertions.assertEquals(resumenes.get(1).getCp(), r.getCp());
		Assertions.assertEquals(resumenes.get(1).getId(), r.getId());
		Assertions.assertEquals(resumenes.get(1).getGestor(), r.getGestor());
		Assertions.assertEquals(resumenes.get(1).getIdOpinion(), "idOpinion");
		Assertions.assertEquals(resumenes.get(1).getValoracion(), 0.0);

		servicio.deleteRestaurante(id, u);

	}
	// ---------- TESTS updatePlato() --------------------

	@Test
	public void testUpdatePlato() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.create("Prueba", "30820", "Alcantarilla", 20.00, 30.00, u);

		servicio.addPlato(id, "Plato de prueba2", "asdfgh", "8.0", true, u);
		servicio.updatePlato(id, "Plato de prueba2", "prueba exitosa", "10.0", true, u);

		Assertions.assertEquals(servicio.getRestaurante(id).getPlatos().get(0).getNombre(), "Plato de prueba2");
		Assertions.assertEquals(servicio.getRestaurante(id).getPlatos().get(0).getDescripcion(), "prueba exitosa");
		Assertions.assertEquals(servicio.getRestaurante(id).getPlatos().get(0).getPrecio(), 10.0);
		servicio.deleteRestaurante(id, u);

	}

	@Test
	public void testUpdatePlatoNotGestor() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.create("Prueba", "30820", "Alcantarilla", 20.00, 30.00, u);

		servicio.addPlato(id, "Plato de prueba2", "asdfgh", "8.0", true, u);
		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.updatePlato(id, "Plato de prueba2", "prueba exitosa", "10.0", true, "celia");
		});

		Assertions.assertEquals("No eres el gestor del restaurante", thrown.getMessage());

		servicio.deleteRestaurante(id, u);

	}

	@Test
	public void testUpdatePlatoIdNull() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.create("Prueba", "30820", "Alcantarilla", 20.00, 30.00, u);

		servicio.addPlato(id, "Plato de prueba", "asdfgh", "8.0", true, u);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.updatePlato(null, "Plato de prueba", "prueba exitosa", "10.0", true, u);
		});
		Assertions.assertEquals("id del restaurante: no debe ser nulo ni vacio", thrown.getMessage());
		servicio.deleteRestaurante(id, u);

	}

	@Test
	public void testUpdatePlatoIdVacio() throws RepositorioException, EntidadNoEncontrada {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.updatePlato("", "Plato de prueba", "prueba exitosa", "10.0", true, u);
		});
		Assertions.assertEquals("id del restaurante: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void testUpdateNombrePlatoNull() throws RepositorioException, EntidadNoEncontrada {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.updatePlato("1", null, "prueba exitosa", "10.0", true, u);
		});
		Assertions.assertEquals("nombre del plato: no debe ser nulo ni vacio", thrown.getMessage());
	}

	@Test
	public void testUpdateDescripcionPlatoNull() throws RepositorioException, EntidadNoEncontrada {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.updatePlato("1", "Plato de prueba", null, "10.0", true, u);
		});
		Assertions.assertEquals("descripcion del plato: no debe ser nulo ni vacio", thrown.getMessage());
	}

	@Test
	public void testUpdatePrecioPlatoNull() throws RepositorioException, EntidadNoEncontrada {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.updatePlato("1", "Plato de prueba", "prueba existosa", null, true, u);
		});
		Assertions.assertEquals("precio del plato: no debe ser nulo ni vacio", thrown.getMessage());
	}

	@Test
	public void testUpdatePlatoNotInRestaurante() throws RepositorioException, EntidadNoEncontrada {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.updatePlato("1", "Calabazas", "prueba exitosa", "10.0", true, u);
		});
		Assertions.assertEquals("plato: no existe en este restaurante", thrown.getMessage());
	}

	// --------------------------- TEST update() del Restaurante -----------------

	@Test
	public void testUpdateRestaurante() throws RepositorioException, EntidadNoEncontrada {

		String id = servicio.create("KFC", "30150", "Alcantarilla", 20.00, 30.00, u);

		servicio.update(id, "KGB", "30016", "Valencia", 30.00, 40.00, u);

		Assertions.assertEquals(servicio.getRestaurante(id).getNombre(), "KGB");
		Assertions.assertEquals(servicio.getRestaurante(id).getCiudad(), "Valencia");
		Assertions.assertEquals(servicio.getRestaurante(id).getCp(), "30016");
		Assertions.assertEquals(servicio.getRestaurante(id).getLatitud(), 30.00);
		Assertions.assertEquals(servicio.getRestaurante(id).getLongitud(), 40.00);

		servicio.deleteRestaurante(id, u);

	}

	@Test
	public void testUpdateRestauranteNotGestor() throws RepositorioException, EntidadNoEncontrada {

		String id = servicio.create("KFC", "30150", "Murcia", 20.00, 30.00, u);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.update(id, "KGB", "Valencia", "30016", 20.00, 30.00, "sofia");

		});
		Assertions.assertEquals("No eres el gestor del restaurante", thrown.getMessage());
		servicio.deleteRestaurante(id, u);

	}

	@Test
	public void testUpdateRestauranteIdNull() throws RepositorioException, EntidadNoEncontrada {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.update(null, "KGB", "30016", "Valencia", 20.00, 30.00, u);

		});
		Assertions.assertEquals("id del restaurante: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void testUpdateRestauranteIdVacio() throws RepositorioException, EntidadNoEncontrada {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.update("", "KGB", "30016", "Valencia", 30.00, 40.00, u);

		});
		Assertions.assertEquals("id del restaurante: no debe ser nulo ni vacio", thrown.getMessage());
	}

	@Test
	public void testUpdateRestauranteNombreNull() throws RepositorioException, EntidadNoEncontrada {

		String id = servicio.create("KFC", "30150", "Murcia", 20.00, 30.00, u);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.update(id, null, "30016", "Valencia", 30.00, 40.00, u);

		});
		Assertions.assertEquals("nombre del  restaurante modificado: no debe ser nulo ni vacio", thrown.getMessage());
		servicio.deleteRestaurante(id, u);

	}

	@Test
	public void testUpdateRestauranteNombreVacio() throws RepositorioException, EntidadNoEncontrada {

		String id = servicio.create("KFC", "30150", "Murcia", 30.00, 40.00, u);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.update(id, "", "30016", "Murcia", 30.00, 40.00, u);

		});
		Assertions.assertEquals("nombre del  restaurante modificado: no debe ser nulo ni vacio", thrown.getMessage());
		servicio.deleteRestaurante(id, u);

	}

	@Test
	public void testUpdateRestauranteCiudadVacio() throws RepositorioException, EntidadNoEncontrada {

		String id = servicio.create("KFC", "30150", "Murcia", 20.00, 30.00, u);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.update(id, "KGB", "30016", "", 30.00, 40.00, u);

		});
		Assertions.assertEquals("ciudad del restaurante modificado: no debe ser nulo ni vacio", thrown.getMessage());
		servicio.deleteRestaurante(id, u);
	}

	@Test
	public void testUpdateRestauranteCiudadNull() throws RepositorioException, EntidadNoEncontrada {

		String id = servicio.create("KFC", "30150", "Murcia", 20.00, 30.00, u);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.update(id, "KGB", "30016", null, 30.00, 40.00, u);

		});
		Assertions.assertEquals("ciudad del restaurante modificado: no debe ser nulo ni vacio", thrown.getMessage());
		servicio.deleteRestaurante(id, u);

	}

	@Test
	public void testUpdateRestauranteCpNull() throws RepositorioException, EntidadNoEncontrada {

		String id = servicio.create("KFC", "30150", "Murcia", 20.00, 30.00, u);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.update(id, "KGB", null, "Alcantarilla", 30.00, 40.00, u);

		});
		Assertions.assertEquals("cp del restaurante modificado: no debe ser nulo ni vacio", thrown.getMessage());
		servicio.deleteRestaurante(id, u);

	}

	@Test
	public void testUpdateRestauranteCpVacio() throws RepositorioException, EntidadNoEncontrada {

		String id = servicio.create("KFC", "30150", "Murcia", 30.00, 40.00, u);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.update(id, "KGB", "", "Alcantarilla", 30.00, 40.00, u);

		});
		Assertions.assertEquals("cp del restaurante modificado: no debe ser nulo ni vacio", thrown.getMessage());
		servicio.deleteRestaurante(id, u);

	}

	@Test
	public void testUpdateRestauranteLatitudNull() throws RepositorioException, EntidadNoEncontrada {

		String id = servicio.create("KFC", "30150", "Murcia", 20.00, 30.00, u);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.update(id, "KGB", "30155", "Alcantarilla", null, 40.00, u);

		});
		Assertions.assertEquals("latitud: no debe ser nulo", thrown.getMessage());
		servicio.deleteRestaurante(id, u);

	}

	@Test
	public void testUpdateRestauranteLongitudNull() throws RepositorioException, EntidadNoEncontrada {

		String id = servicio.create("KFC", "30150", "Alcantarilla", 20.00, 40.00, u);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.update(id, "KGB", "30155", "Alcantarilla", 20.00, null, u);

		});
		Assertions.assertEquals("longitud: no debe ser nulo", thrown.getMessage());
		servicio.deleteRestaurante(id, u);

	}

	// --------------Test obtenerSitiosTuristicos ----------------------
	@Test
	public void testObtenerSitiosTuristicos() throws RepositorioException, MalformedURLException, SAXException,
			IOException, ParserConfigurationException, EntidadNoEncontrada {
		String id = servicio.create("KFC", "30161", "Murcia", 20.00, 40.00, u);
		List<SitioTuristico> listaSitios = servicio.obtenerSitiosTuristicos(id);
		
		System.out.println(listaSitios.get(0).toString());
		System.out.println(listaSitios.get(1).toString());
		System.out.println(listaSitios.get(2).toString());

		SitioTuristico sitio1 = listaSitios.get(0);
		SitioTuristico sitio2 = listaSitios.get(1);
		SitioTuristico sitio3 = listaSitios.get(2);

		// Comprobamos que el nombre insertado haya sido el correcto en el mismo orden
		Assertions.assertEquals(sitio1.getNombre(), listaSitios.get(0).getNombre());
		Assertions.assertEquals(sitio2.getNombre(), listaSitios.get(1).getNombre());
		Assertions.assertEquals(sitio3.getNombre(), listaSitios.get(2).getNombre());

		servicio.deleteRestaurante(id, u);

	}

	@Test
	public void testObtenerSitiosTuristicosIdVacio() {

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.obtenerSitiosTuristicos("");

		});
		Assertions.assertEquals("id del restaurante: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void testObtenerSitiosTuristicosIdNull() {
		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.obtenerSitiosTuristicos(null);

		});
		Assertions.assertEquals("id del restaurante: no debe ser nulo ni vacio", thrown.getMessage());

	}

	// --------------------- Test setSitiosTuristicos -------------------

	@Test
	public void testSetSitiosTuristicos() throws MalformedURLException, SAXException, IOException,
			ParserConfigurationException, RepositorioException, EntidadNoEncontrada {

		String id = servicio.create("GyS", "30150", "Murcia", 30.00, 40.00, u);

		List<SitioTuristico> listaSitios = servicio.obtenerSitiosTuristicos(id);

		// sabemos los tres sitios turisticos que imprime por consola debido a los
		// mensajes impresos desde el servicio

		System.out.println("BBBBBBBBBBBBBBBBBBBB: " + listaSitios.get(2));
		SitioTuristico sitio3 = listaSitios.get(2);

		List<SitioTuristico> miLista = new LinkedList<SitioTuristico>();
		miLista.add(sitio3);
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA: " + miLista.size());

		servicio.setSitiosTuristicos(id, miLista, u);
		Restaurante r = servicio.getRestaurante(id);

		Assertions.assertEquals(r.getSitios().size(), 1);
		Assertions.assertEquals(r.getSitios().get(0).getNombre(), "Estadio El Mayayo");
		Assertions.assertEquals(r.getSitios().get(0), sitio3);
		servicio.deleteRestaurante(id, u);


	}

	@Test
	public void testSetSitiosTuristicosIdVacio() throws MalformedURLException, SAXException, IOException,
			ParserConfigurationException, RepositorioException, EntidadNoEncontrada {

		List<SitioTuristico> listaSitios = servicio.obtenerSitiosTuristicos("1");

		SitioTuristico sitio3 = listaSitios.get(2);

		List<SitioTuristico> miLista = new LinkedList<SitioTuristico>();
		miLista.add(sitio3);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.setSitiosTuristicos("", miLista, u);

		});
		Assertions.assertEquals("id del restaurante: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void testSetSitiosTuristicosIdNull() throws MalformedURLException, SAXException, IOException,
			ParserConfigurationException, RepositorioException, EntidadNoEncontrada {

		List<SitioTuristico> listaSitios = servicio.obtenerSitiosTuristicos("1");

		SitioTuristico sitio3 = listaSitios.get(2);

		List<SitioTuristico> miLista = new LinkedList<SitioTuristico>();
		miLista.add(sitio3);

		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.setSitiosTuristicos(null, miLista, u);

		});
		Assertions.assertEquals("id del restaurante: no debe ser nulo ni vacio", thrown.getMessage());

	}

	@Test
	public void testSetSitiosTuristicosNoGerente() throws RepositorioException, EntidadNoEncontrada,
			MalformedURLException, SAXException, IOException, ParserConfigurationException {
		String id = servicio.create("KFC", "30150", "Murcia", 20.00, 30.00, u);
		List<SitioTuristico> listaSitios = servicio.obtenerSitiosTuristicos(id);

		SitioTuristico sitio3 = listaSitios.get(2);

		List<SitioTuristico> miLista = new LinkedList<SitioTuristico>();
		miLista.add(sitio3);
		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.setSitiosTuristicos(id, miLista, "sofia");

		});
		Assertions.assertEquals("No eres el gestor del restaurante", thrown.getMessage());
		servicio.deleteRestaurante(id, u);

	}
	@Test
	public void testSetSitiosTuristicosListaNull() throws RepositorioException, EntidadNoEncontrada,
			MalformedURLException, SAXException, IOException, ParserConfigurationException {
		String id = servicio.create("KFC", "30150", "Murcia", 20.00, 30.00, u);
		
		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			servicio.setSitiosTuristicos(id, null, "sofia");

		});
		Assertions.assertEquals("lista de sitios turisticos: no debe ser nula", thrown.getMessage());
		servicio.deleteRestaurante(id, u);

	}

}
