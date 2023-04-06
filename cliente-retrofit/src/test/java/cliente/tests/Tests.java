package cliente.tests;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import restaurantes.modelo.Restaurante;
import restaurantes.modelo.SitioTuristico;
import retrofit.restaurantes.Listado;
import retrofit.restaurantes.Listado.ResumenExtendido;
import retrofit.restaurantes.PlatoRequest;
import retrofit.restaurantes.RestauranteRequest;
import retrofit.restaurantes.RestaurantesRestClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class Tests {

	Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:8080/api/")
			.addConverterFactory(GsonConverterFactory.create()).build();

	RestaurantesRestClient service = retrofit.create(RestaurantesRestClient.class);

// ------------------ Tests createRestaurante() --------------------
	@Test
	void testCrearRestauranteCorrecto() throws IOException {

		System.out.println("TEST CREAR RESTAURANTE CORRECTO: ");
		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();

		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		System.out.println("Restaurante creado: " + url1);
		System.out.println("Código de respuesta: " + resultado.code());
		System.out.println("Mensaje de respuesta:" + resultado.message());
		System.out.println("Id: " + id1);
		System.out.println("-------------------");

	}

	@Test
	void testCrearRestauranteFaltaNombre() throws IOException {
		System.out.println("TEST CREAR RESTAURANTE SIN NOMBRE");
		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30161");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();

		System.out.println("Código de respuesta: " + resultado.code());
		System.out.println("Mensaje de respuesta: " + resultado.message());
		System.out.println("Cuerpo del mensaje:" + resultado.errorBody().string());
		System.out.println("-------------------");

	}

	@Test
	void testCrearRestauranteFaltaCiudad() throws IOException {
		System.out.println("TEST CREAR RESTAURANTE SIN CIUDAD");
		RestauranteRequest restaurante2 = new RestauranteRequest();
		restaurante2.setNombre("Prueba Retrofit");
		restaurante2.setCp("30161");
		restaurante2.setCoordenadas("20, 10");
		Response<Void> resultado2 = service.createRestaurante(restaurante2).execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Cuerpo del mensaje:" + resultado2.errorBody().string());
		System.out.println("-------------------");
	}

	@Test
	void testCrearRestauranteFaltaCP() throws IOException {
		System.out.println("TEST CREAR RESTAURANTE SIN CP");
		RestauranteRequest restaurante3 = new RestauranteRequest();
		restaurante3.setNombre("Prueba Retrofit");
		restaurante3.setCiudad("Murcia");
		restaurante3.setCoordenadas("20, 10");
		Response<Void> resultado3 = service.createRestaurante(restaurante3).execute();

		System.out.println("Código de respuesta: " + resultado3.code());
		System.out.println("Mensaje de respuesta: " + resultado3.message());
		System.out.println("Cuerpo del mensaje:" + resultado3.errorBody().string());
		System.out.println("-------------------");
	}

	@Test
	void testCrearRestauranteFaltaCoordenadas() throws IOException {
		System.out.println("TEST CREAR RESTAURANTE SIN COORDENADAS");
		RestauranteRequest restaurante4 = new RestauranteRequest();
		restaurante4.setNombre("Prueba Retrofit");
		restaurante4.setCiudad("Murcia");
		restaurante4.setCp("30161");
		Response<Void> resultado4 = service.createRestaurante(restaurante4).execute();

		System.out.println("Código de respuesta: " + resultado4.code());
		System.out.println("Mensaje de respuesta: " + resultado4.message());
		System.out.println("Cuerpo del mensaje:" + resultado4.errorBody().string());
		System.out.println("-------------------");
	}

// ------------------ Tests getSitiosTuristicos() --------------------
	@Test
	void testGetSitiosTuristicos() throws IOException {
		System.out.println("TEST GET SITIOS TURISTICOS");

		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30010");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();

		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		Response<List<SitioTuristico>> resultado2 = service.getSitiosTuristicos(id1).execute();
		Assertions.assertEquals(resultado2.headers().get("Content-Type"), "application/json");

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Cuerpo del mensaje: " + resultado2.body().toString());
		System.out.println("-------------------");
	}

	@Test
	void testGetSitiosTuristicosIdVacio() throws IOException {
		System.out.println("TEST GET SITIOS TURISTICOS ID VACIO");

		Response<List<SitioTuristico>> resultado = service.getSitiosTuristicos("").execute();
		System.out.println(resultado);
		System.out.println("Código de respuesta: " + resultado.code());
		System.out.println("Mensaje de respuesta: " + resultado.message());
		System.out.println("Cuerpo del mensaje: " + resultado.errorBody().string()); // DEBERIA DAR id del restaurante:
																						// no debe ser nulo ni vacio
																						// PERO no da nada
		System.out.println("-------------------");
	}


	// ---------------- Tests setSitiosTuristicos() --------------------------
	@Test
	void testSetSitiosTuristicos() throws IOException {
		
		System.out.println("TEST SET SITIOS TURISTICOS");

		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30010");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();

		
		String url1 = resultado.headers().get("Location"); 
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		Response<List<SitioTuristico>> resultado2 = service.getSitiosTuristicos(id1).execute();
		List<SitioTuristico> lista = resultado2.body();
		
		List<SitioTuristico> miLista = new LinkedList<SitioTuristico>();
		
		miLista.add(lista.get(0));
		
		
		Response<Void> resultado3 = service.setSitiosTuristicos(id1, miLista).execute();

			
		System.out.println("Código de respuesta: " + resultado3.code());
		System.out.println("Mensaje de respuesta: " + resultado3.message());
		System.out.println("-----------------------------");

		
	}
	
	@Test
	void testSetSitiosTuristicosListaVacia() throws IOException {
		
		System.out.println("TEST SET SITIOS TURISTICOS LISTA VACIA");

		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30010");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();

		
		String url1 = resultado.headers().get("Location"); 
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);
		
		List<SitioTuristico> miLista = new LinkedList<SitioTuristico>();
		
		Response<Void> resultado3 = service.setSitiosTuristicos(id1, miLista).execute();

			
		System.out.println("Código de respuesta: " + resultado3.code());
		System.out.println("Mensaje de respuesta: " + resultado3.message());
		System.out.println("Cuerpo del mensaje: " + resultado3.errorBody().string());
		System.out.println("-----------------------------------------------");
		
	}
	
	@Test
	void testSetSitiosTuristicosIdVacia() throws IOException {
		
		System.out.println("TEST SET SITIOS TURISTICOS ID VACIO");

		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30010");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();

		
		String url1 = resultado.headers().get("Location"); 
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		Response<List<SitioTuristico>> resultado2 = service.getSitiosTuristicos(id1).execute();
		List<SitioTuristico> lista = resultado2.body();
		
		List<SitioTuristico> miLista = new LinkedList<SitioTuristico>();
		miLista.add(lista.get(0));
		
		
		Response<Void> resultado3 = service.setSitiosTuristicos("", miLista).execute();

			
		System.out.println("Código de respuesta: " + resultado3.code());
		System.out.println("Mensaje de respuesta: " + resultado3.message());
		System.out.println("Cuerpo del mensaje: " + resultado3.errorBody().string());
		System.out.println("-----------------------------------------------");
		
	}
// ------------------ Tests updateRestaurante() --------------------

	@Test
	void testUpdateRestauranteCorrecto() throws IOException {
		System.out.println("TEST UPDATE RESTAURANTE CORRECTO");
		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();
		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		System.out.println(id1);
		RestauranteRequest update = new RestauranteRequest();
		update.setNombre("Actualizacion Retrofit");
		update.setCiudad("Ciudad actualizada");
		update.setCp("99999");
		update.setCoordenadas("1, 1");
		Response<Void> resultado2 = service.updateRestaurante(id1, update).execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("-----------------------------------------------");

	}

	@Test
	void testUpdateRestauranteNoExiste() throws IOException {
		System.out.println("TEST UPDATE RESTAURANTE NO EXISTE");

		RestauranteRequest update = new RestauranteRequest();
		update.setNombre("Actualizacion Retrofit");
		update.setCiudad("Ciudad actualizada");
		update.setCp("99999");
		update.setCoordenadas("1, 1");
		Response<Void> resultado2 = service.updateRestaurante("642ab5d42cf9e474fcf75a1b", update).execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Cuerpo del mensaje: " + resultado2.errorBody().string());
		System.out.println("-----------------------------------------------");

	}

	@Test
	void testUpdateRestauranteSinNombre() throws IOException {
		System.out.println("TEST UPDATE RESTAURANTE SIN NOMBRE");

		RestauranteRequest update = new RestauranteRequest();
		update.setCiudad("Ciudad actualizada");
		update.setCp("99999");
		update.setCoordenadas("1, 1");
		Response<Void> resultado2 = service.updateRestaurante("642ab5d42cf9e474fcf75a1b", update).execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Cuerpo del mensaje: " + resultado2.errorBody().string());
		System.out.println("-----------------------------------------------");

	}

	@Test
	void testUpdateRestauranteSinCiudad() throws IOException {
		System.out.println("TEST UPDATE RESTAURANTE SIN CIUDAD");
		RestauranteRequest update = new RestauranteRequest();
		update.setNombre("Actualizacion Retrofit");
		update.setCp("99999");
		update.setCoordenadas("1, 1");
		Response<Void> resultado2 = service.updateRestaurante("642ab5d42cf9e474fcf75a1b", update).execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Cuerpo del mensaje: " + resultado2.errorBody().string());
		System.out.println("-----------------------------------------------");

	}

	@Test
	void testUpdateRestauranteSinCp() throws IOException {
		System.out.println("TEST UPDATE RESTAURANTE SIN CP");

		RestauranteRequest update = new RestauranteRequest();
		update.setNombre("Actualizacion Retrofit");
		update.setCiudad("Ciudad actualizada");
		update.setCoordenadas("1, 1");
		Response<Void> resultado2 = service.updateRestaurante("642ab5d42cf9e474fcf75a1b", update).execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Cuerpo del mensaje: " + resultado2.errorBody().string());
		System.out.println("-----------------------------------------------");

	}

	@Test
	void testUpdateRestauranteSinCoordenadas() throws IOException {
		System.out.println("TEST UPDATE RESTAURANTE SIN COORDENADAS");

		RestauranteRequest update = new RestauranteRequest();
		update.setNombre("Actualizacion Retrofit");
		update.setCiudad("Ciudad actualizada");
		update.setCp("99999");
		Response<Void> resultado2 = service.updateRestaurante("642ab5d42cf9e474fcf75a1b", update).execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Cuerpo del mensaje: " + resultado2.errorBody().string());
		System.out.println("-----------------------------------------------");

	}

	// ------------------ Tests addPlato() --------------------

	@Test
	void testAddPlato() throws IOException {
		System.out.println("TEST ADDPLATO CORRECTO");

		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();
		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		System.out.println(id1);
		PlatoRequest plato = new PlatoRequest();
		plato.setNombre("Plato de prueba");
		plato.setDescripcion("Descripcion del plato");
		plato.setPrecio("10");
		plato.setDisponibilidad(true);

		Response<Void> resultado2 = service.addPlato(id1, plato).execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("-----------------------------------------------");

	}

	@Test
	void testAddPlatoSinNombre() throws IOException {
		System.out.println("TEST ADDPLATO SIN NOMBRE");

		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();
		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		System.out.println(id1);
		PlatoRequest plato = new PlatoRequest();
		plato.setDescripcion("Descripcion del plato");
		plato.setPrecio("10");
		plato.setDisponibilidad(true);

		Response<Void> resultado2 = service.addPlato(id1, plato).execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Cuerpo del mensaje: " + resultado2.errorBody().string());
		System.out.println("-----------------------------------------------");
	}

	@Test
	void testAddPlatoSinDescripcion() throws IOException {
		System.out.println("TEST ADDPLATO SIN DESCRIPCION");

		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();
		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		System.out.println(id1);
		PlatoRequest plato = new PlatoRequest();
		plato.setNombre("Plato de prueba");
		plato.setPrecio("10");
		plato.setDisponibilidad(true);

		Response<Void> resultado2 = service.addPlato(id1, plato).execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Cuerpo del mensaje: " + resultado2.errorBody().string());
		System.out.println("-----------------------------------------------");
	}

	@Test
	void testAddPlatoSinPrecio() throws IOException {
		System.out.println("TEST ADDPLATO SIN PRECIO");

		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();
		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		System.out.println(id1);
		PlatoRequest plato = new PlatoRequest();
		plato.setNombre("Plato de prueba");
		plato.setDescripcion("Descripcion del plato");
		plato.setDisponibilidad(true);

		Response<Void> resultado2 = service.addPlato(id1, plato).execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Cuerpo del mensaje: " + resultado2.errorBody().string());
		System.out.println("-----------------------------------------------");

	}

	@Test
	void testAddPlatoSinDisponibilidad() throws IOException {
		System.out.println("TEST ADDPLATO SIN DISPONIBILIDAD");

		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();
		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		System.out.println(id1);
		PlatoRequest plato = new PlatoRequest();
		plato.setNombre("Plato de prueba");
		plato.setDescripcion("Descripcion del plato");
		plato.setDisponibilidad(true);
		plato.setPrecio("10");

		// Al no definir disponibilidad se crea por defecto a false
		Response<Void> resultado2 = service.addPlato(id1, plato).execute();
		System.out.println("Plato: " + service.getRestaurante(id1).execute().body().getPlatos());

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("-----------------------------------------------");

	}

	@Test
	void testAddPlatoRepetido() throws IOException {

		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();
		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		System.out.println(id1);
		PlatoRequest plato = new PlatoRequest();
		plato.setNombre("Plato de prueba");
		plato.setDescripcion("Descripcion del plato");
		plato.setPrecio("10");
		plato.setDisponibilidad(true);

		service.addPlato(id1, plato).execute();

		PlatoRequest plato2 = new PlatoRequest();
		plato2.setNombre("Plato de prueba");
		plato2.setDescripcion("Descripcion del plato");
		plato2.setPrecio("10");
		plato2.setDisponibilidad(true);

		Response<Void> resultado2 = service.addPlato(id1, plato2).execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Cuerpo del mensaje:" + resultado2.errorBody().string());
		System.out.println("-----------------------------------------------");

		// TODO: Duda: plato repetido deberia devolver BadRequest (400) o IllegalState
		// (Server Error 500) ??
	}

	// ------------------- Tests removePlato()-----------------------

	@Test
	void testRemovePlato() throws IOException {
		System.out.println("TEST REMOVEPLATO CORRECTO");

		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();
		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		System.out.println(id1);
		PlatoRequest plato = new PlatoRequest();
		plato.setNombre("Plato de prueba");
		plato.setDescripcion("Descripcion del plato");
		plato.setPrecio("10");
		plato.setDisponibilidad(true);

		service.addPlato(id1, plato).execute();

		Response<Void> resultado2 = service.removePlato(id1, plato.getNombre()).execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());

		System.out.println("-----------------------------------------------");
	}

	@Test
	void testRemovePlatoIdVacio() throws IOException {
		System.out.println("TEST REMOVEPLATO ID VACIO");

		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();
		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		System.out.println(id1);
		PlatoRequest plato = new PlatoRequest();
		plato.setNombre("Plato de prueba");
		plato.setDescripcion("Descripcion del plato");
		plato.setPrecio("10");
		plato.setDisponibilidad(true);

		service.addPlato(id1, plato).execute();

		Response<Void> resultado2 = service.removePlato("", plato.getNombre()).execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Cuerpo del mensaje:" + resultado2.errorBody().string());

		System.out.println("-----------------------------------------------");
	}

	@Test
	void testRemovePlatoSinNombre() throws IOException {
		System.out.println("TEST REMOVEPLATO CORRECTO");

		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();
		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		System.out.println(id1);
		PlatoRequest plato = new PlatoRequest();
		plato.setNombre("Plato de prueba");
		plato.setDescripcion("Descripcion del plato");
		plato.setPrecio("10");
		plato.setDisponibilidad(true);

		service.addPlato(id1, plato).execute();

		Response<Void> resultado2 = service.removePlato(id1, "").execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Cuerpo del mensaje:" + resultado2.errorBody().string());

		System.out.println("-----------------------------------------------");
	}

	// ------------------ Tests updatePlato() --------------------

	@Test
	void testUpdatePlato() throws IOException {
		System.out.println("TEST ADDPLATO CORRECTO");

		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();
		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		System.out.println(id1);
		PlatoRequest plato = new PlatoRequest();
		plato.setNombre("Plato de prueba");
		plato.setDescripcion("Descripcion del plato");
		plato.setPrecio("10");
		plato.setDisponibilidad(true);

		service.addPlato(id1, plato).execute();

		plato.setDescripcion("Cambio descripcion");
		plato.setPrecio("20");

		Response<Void> resultado2 = service.updatePlato(id1, plato).execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("-----------------------------------------------");

	}

	@Test
	void testUpdatePlatoIdNoExiste() throws IOException {
		System.out.println("TEST UPDATEPLATOIDNOEXISTE");
		PlatoRequest plato = new PlatoRequest();
		plato.setNombre("Plato de prueba");
		plato.setDescripcion("Descripcion del plato");
		plato.setPrecio("10");
		plato.setDisponibilidad(true);

		Response<Void> resultado2 = service.updatePlato("642d59a2f832f95e42e82bc8", plato).execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Mensaje de respuesta: " + resultado2.errorBody().string());
		System.out.println("-----------------------------------------------");

	}

	@Test
	void testUpdatePlatoNoExiste() throws IOException {

		System.out.println("TEST UPDATEPLATONOEXISTE");

		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();
		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		System.out.println(id1);

		PlatoRequest plato = new PlatoRequest();
		plato.setNombre("Plato de prueba");
		plato.setDescripcion("Descripcion del plato");
		plato.setPrecio("10");
		plato.setDisponibilidad(true);

		Response<Void> resultado2 = service.updatePlato(id1, plato).execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Mensaje de respuesta: " + resultado2.errorBody().string());
		System.out.println("-----------------------------------------------");

	}

	@Test
	void testUpdatePlatoIdRestauranteVacio() throws IOException {
		System.out.println("TEST UPDATEPLATOIDRESTAURANTEVACIO");

		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();
		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		System.out.println(id1);

		PlatoRequest plato = new PlatoRequest();
		plato.setNombre("Plato de prueba");
		plato.setDescripcion("Descripcion del plato");
		plato.setPrecio("10");
		plato.setDisponibilidad(true);

		Response<Void> resultado2 = service.updatePlato("", plato).execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Mensaje de respuesta: " + resultado2.errorBody().string());
		System.out.println("-----------------------------------------------");

	}

	@Test
	void testUpdatePlatoNombreVacio() throws IOException {
		System.out.println("TEST UPDATEPLATONOMBREVACIO");

		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();
		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		System.out.println(id1);

		PlatoRequest plato = new PlatoRequest();
		plato.setDescripcion("Descripcion del plato");
		plato.setPrecio("10");
		plato.setDisponibilidad(true);

		Response<Void> resultado2 = service.updatePlato(id1, plato).execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Mensaje de respuesta: " + resultado2.errorBody().string());
		System.out.println("-----------------------------------------------");

	}

	@Test
	void testUpdatePlatoDescripcionVacia() throws IOException {
		System.out.println("TEST UPDATEPLATONOMBREVACIO");

		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();
		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		System.out.println(id1);

		PlatoRequest plato = new PlatoRequest();
		plato.setNombre("Plato de prueba");
		plato.setPrecio("10");
		plato.setDisponibilidad(true);

		Response<Void> resultado2 = service.updatePlato(id1, plato).execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Mensaje de respuesta: " + resultado2.errorBody().string());
		System.out.println("-----------------------------------------------");

	}

	@Test
	void testUpdatePlatoPrecioVacio() throws IOException {
		System.out.println("TEST UPDATEPLATODESCRIPCIONVACIO");

		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();
		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		System.out.println(id1);

		PlatoRequest plato = new PlatoRequest();
		plato.setNombre("Plato de prueba");
		plato.setDescripcion("Descripcion del plato");
		plato.setDisponibilidad(true);

		Response<Void> resultado2 = service.updatePlato(id1, plato).execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Mensaje de respuesta: " + resultado2.errorBody().string());
		System.out.println("-----------------------------------------------");

	}

	@Test
	void testUpdatePlatoDisponibilidadVacia() throws IOException {
		System.out.println("TEST UPDATEPLATONOMBREVACIO");

		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();
		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		System.out.println(id1);

		PlatoRequest plato = new PlatoRequest();
		plato.setNombre("Plato de prueba");
		plato.setDescripcion("Descripcion del plato");
		plato.setPrecio("10");

		Response<Void> resultado2 = service.updatePlato(id1, plato).execute();
		Restaurante res = service.getRestaurante(id1).execute().body();

		// Comprobamos que la disponibilidad del plato por defecto, al no especificar
		// ninguna, es false
		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Disponibilidad del plato: " + res.getPlatos().get(0).isDisponibilidad());
		System.out.println("-----------------------------------------------");

	}

	// ------------------ Tests removeRestaurante() --------------------
	@Test
	void testRemoveRestaurante() throws IOException {

		System.out.println("TEST REMOVE RESTAURANTE CORRECTO: ");
		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();

		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);
		System.out.println(id1);

		Response<Void> resultado2 = service.removeRestaurante(id1).execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta:" + resultado2.message());

		System.out.println("-------------------");

	}

	@Test
	void testRemoveRestauranteNoExiste() throws IOException {

		Response<Void> resultado2 = service.removeRestaurante("642d59a2f832f95e42e82bc8").execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta:" + resultado2.message());
		System.out.println("Mensaje de respuesta: " + resultado2.errorBody().string());

		System.out.println("-------------------");

	}

	// Mismo problema con el ID vacio
	@Test
	void testRemoveRestauranteIdVacio() throws IOException {

		Response<Void> resultado2 = service.removeRestaurante("").execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta:" + resultado2.message());
		System.out.println("Mensaje de respuesta: " + resultado2.errorBody().string());

		System.out.println("-------------------");

	}

	// ------------------ Tests getRestaurante() --------------------

	@Test
	void testGetRestaurante() throws IOException {
		System.out.println("TEST GETRESTAURANTE");
		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba RetrofitGetRestaurante");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();

		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		Response<Restaurante> resultado2 = service.getRestaurante(id1).execute();
		Restaurante res = resultado2.body();
		
		//Comprobamos que el contenido de la respuesta sea en formato JSON
		Assertions.assertEquals(resultado2.headers().get("Content-Type"), "application/json");
		
		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Restaurante: " + res.toString()); 
		System.out.println("-------------------------------");

	}

	@Test
	void testGetRestauranteIdNoValido() throws IOException {
		System.out.println("TEST GETRESTAURANTE ID NO VALIDO");

		Response<Restaurante> resultado2 = service.getRestaurante("KSANKFALKS").execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());

		System.out.println("-------------------------------");

	}

	@Test
	void testGetRestauranteIdVacio() throws IOException {

		System.out.println("TEST GETRESTAURANTE ID VACIO");

		Response<Restaurante> resultado2 = service.getRestaurante(" ").execute();
		Restaurante res = resultado2.body();

		// TODO: devuelve un restaurante todo null ?????????? porque no salta el error
		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Mensaje de respuesta: " + resultado2.errorBody().string());

		System.out.println("-------------------------------");

	}

	// ------------------ Tests getListadoRestaurantes() --------------------

	@Test
	void testGetListadoRestaurantes() throws IOException {
		Response<Listado> resultado2 = service.getListadoRestaurantes().execute();
		Listado res = resultado2.body();
		//Comprobamos que el formato sea JSON
		Assertions.assertEquals(resultado2.headers().get("Content-Type"), "application/json");

		for (ResumenExtendido re : res.getRestaurante()) {
			System.out.println(re.getUrl());
			System.out.println(re.getResumen());
		}
	}


}
