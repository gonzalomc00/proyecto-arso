package cliente.tests;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import okhttp3.OkHttpClient;
import restaurantes.modelo.Restaurante;
import restaurantes.modelo.SitioTuristico;
import restaurantes.modelo.Valoracion;
import retrofit.restaurantes.AuthInterceptor;
import retrofit.restaurantes.Listado;
import retrofit.restaurantes.Listado.ResumenExtendido;
import retrofit.restaurantes.PlatoRequest;
import retrofit.restaurantes.RestauranteRequest;
import retrofit.restaurantes.RestaurantesRestClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class Tests {
	// token con fecha de expiración de un año
	public static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxODVkYjllMy05NDJlLTQxMmItYTdiNC0zYTY3YzBhMTlhMDUiLCJpc3MiOiJQYXNhcmVsYSBadXVsIiwiZXhwIjoxNzE1Njc4MDE1LCJzdWIiOiJtY3NtcmxsIiwidXN1YXJpbyI6InNvZmlhLm1hY2lhc21AdW0uZXMiLCJyb2wiOiJHRVNUT1IifQ.gsX5KS7e9BGhtjR9LdgvffRU1ZD2PBoNUGH_ykfHjc4";

	OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(new AuthInterceptor(TOKEN));

	// Agrega el interceptor para incluir el token de autorización
	OkHttpClient client = httpClient.build();

	Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:8090").client(client)
			.addConverterFactory(GsonConverterFactory.create()).build();

	RestaurantesRestClient service = retrofit.create(RestaurantesRestClient.class);

//------------------------------------------------------------------
// ------------------ Tests createRestaurante() --------------------
//------------------------------------------------------------------
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
		System.out.println(url1);
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		Assertions.assertEquals(resultado.code(), 201);

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

		Assertions.assertEquals(resultado.code(), 400);
		Assertions.assertEquals(resultado.errorBody().string(), "nombre del restaurante: no debe ser nulo ni vacio");

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

		Assertions.assertEquals(resultado2.code(), 400);
		Assertions.assertEquals(resultado2.errorBody().string(), "ciudad: no debe ser nulo ni vacio");

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

		Assertions.assertEquals(resultado3.code(), 400);
		Assertions.assertEquals(resultado3.errorBody().string(), "codigo postal: no debe ser nulo ni vacio");

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

		Assertions.assertEquals(resultado4.code(), 400);
		Assertions.assertEquals(resultado4.errorBody().string(), "coordenadas: no debe ser nulo");

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
		Assertions.assertEquals(resultado2.code(), 200);

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		// vemos el json del restaurante
		System.out.println("Cuerpo del mensaje: " + resultado2.body().toString());
		System.out.println("-------------------");
	}

	@Test
	void testGetSitiosTuristicosIdVacio() throws IOException {
		System.out.println("TEST GET SITIOS TURISTICOS ID VACIO");

		Response<List<SitioTuristico>> resultado = service.getSitiosTuristicos("").execute();

		// el id vacio no se puede representar correctamente a través de la url - url no
		// valida - por lo que no encuentra el metodo en el controlador
		System.out.println("Código de respuesta: " + resultado.code());
		System.out.println("Mensaje de respuesta: " + resultado.message());
		System.out.println("Cuerpo del mensaje: " + resultado.errorBody().string());
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
		Assertions.assertEquals(resultado3.code(), 204);

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
		Assertions.assertEquals(resultado3.code(), 204);

		System.out.println("Código de respuesta: " + resultado3.code());
		System.out.println("Mensaje de respuesta: " + resultado3.message());
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

		String id = " ";
		Response<Void> resultado3 = service.setSitiosTuristicos(id, miLista).execute();
		Assertions.assertEquals(resultado3.code(), 400);

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
		Assertions.assertEquals(resultado2.code(), 204);

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
		Response<Void> resultado2 = service.updateRestaurante(" ", update).execute();

		Assertions.assertEquals(resultado2.code(), 400);

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message().toString());
		System.out.println("Cuerpo del mensaje: " + resultado2.errorBody().string());
		System.out.println("-----------------------------------------------");

	}

	@Test
	void testUpdateRestauranteSinNombre() throws IOException {
		System.out.println("TEST UPDATE RESTAURANTE SIN NOMBRE");
		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");

		Response<Void> resultado = service.createRestaurante(restaurante).execute();

		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		RestauranteRequest update = new RestauranteRequest();
		update.setCiudad("Ciudad actualizada");
		update.setCp("99999");
		update.setCoordenadas("1, 1");
		Response<Void> resultado2 = service.updateRestaurante(id1, update).execute();
		Assertions.assertEquals(resultado2.code(), 400);
		Assertions.assertEquals(resultado2.errorBody().string(), "nombre del restaurante: no debe ser nulo ni vacio");

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Cuerpo del mensaje: " + resultado2.errorBody().string());
		System.out.println("-----------------------------------------------");

	}

	@Test
	void testUpdateRestauranteSinCiudad() throws IOException {
		System.out.println("TEST UPDATE RESTAURANTE SIN CIUDAD");
		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");

		Response<Void> resultado = service.createRestaurante(restaurante).execute();

		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);
		RestauranteRequest update = new RestauranteRequest();
		update.setNombre("Actualizacion Retrofit");
		update.setCp("99999");
		update.setCoordenadas("1, 1");

		Response<Void> resultado2 = service.updateRestaurante(id1, update).execute();

		Assertions.assertEquals(resultado2.code(), 400);
		Assertions.assertEquals(resultado2.errorBody().string(), "ciudad: no debe ser nulo ni vacio");

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Cuerpo del mensaje: " + resultado2.errorBody().string());
		System.out.println("-----------------------------------------------");

	}

	@Test
	void testUpdateRestauranteSinCp() throws IOException {
		System.out.println("TEST UPDATE RESTAURANTE SIN CP");
		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");

		Response<Void> resultado = service.createRestaurante(restaurante).execute();

		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);
		RestauranteRequest update = new RestauranteRequest();
		update.setNombre("Actualizacion Retrofit");
		update.setCiudad("Ciudad actualizada");
		update.setCoordenadas("1, 1");
		Response<Void> resultado2 = service.updateRestaurante(id1, update).execute();
		Assertions.assertEquals(resultado2.code(), 400);
		Assertions.assertEquals(resultado2.errorBody().string(), "codigo postal: no debe ser nulo ni vacio");

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Cuerpo del mensaje: " + resultado2.errorBody().string());
		System.out.println("-----------------------------------------------");

	}

	@Test
	void testUpdateRestauranteSinCoordenadas() throws IOException {
		System.out.println("TEST UPDATE RESTAURANTE SIN COORDENADAS");
		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");

		Response<Void> resultado = service.createRestaurante(restaurante).execute();

		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);
		RestauranteRequest update = new RestauranteRequest();
		update.setNombre("Actualizacion Retrofit");
		update.setCiudad("Ciudad actualizada");
		update.setCp("99999");
		Response<Void> resultado2 = service.updateRestaurante(id1, update).execute();
		Assertions.assertEquals(resultado2.code(), 400);
		Assertions.assertEquals(resultado2.errorBody().string(), "coordenadas: no debe ser nulo");

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
		Assertions.assertEquals(resultado2.code(), 201);

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
		Assertions.assertEquals(resultado2.code(), 400);
		Assertions.assertEquals(resultado2.errorBody().string(), "nombre del plato: no debe ser nulo ni vacio");

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
		Assertions.assertEquals(resultado2.code(), 400);
		Assertions.assertEquals(resultado2.errorBody().string(), "descripcion del plato: no debe ser nulo ni vacio");

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
		Assertions.assertEquals(resultado2.code(), 400);
		Assertions.assertEquals(resultado2.errorBody().string(), "precio del plato: no debe ser nulo ni vacio");

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
		plato.setPrecio("10");

		// Al no definir disponibilidad se crea por defecto a false
		Response<Void> resultado2 = service.addPlato(id1, plato).execute();

		Assertions.assertEquals(resultado2.code(), 201);
		// la disponibilidad por defecto si no se especifica es false
		Assertions.assertFalse(service.getRestaurante(id1).execute().body().getPlatos().get(0).isDisponibilidad());

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
		Assertions.assertEquals(resultado2.code(), 400);
		Assertions.assertEquals(resultado2.errorBody().string(), "ERROR: plato duplicado");

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Cuerpo del mensaje:" + resultado2.errorBody().string());
		System.out.println("-----------------------------------------------");
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
		Assertions.assertEquals(resultado2.code(), 204);

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

		Response<Void> resultado2 = service.removePlato(" ", plato.getNombre()).execute();
		Assertions.assertEquals(resultado2.code(), 400);

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Cuerpo del mensaje:" + resultado2.errorBody().string());

		System.out.println("-----------------------------------------------");
	}

	@Test
	void testRemovePlatoNombreIncorrecto() throws IOException {
		System.out.println("TEST REMOVEPLATO Not in Restaurante");

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

		Response<Void> resultado2 = service.removePlato(id1, "lxzczcn").execute();
		Assertions.assertEquals(resultado2.code(), 404);
		Assertions.assertEquals(resultado2.errorBody().string(), "ERROR: No existe el plato en este restaurante");

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Cuerpo del mensaje:" + resultado2.errorBody().string());

		System.out.println("-----------------------------------------------");
	}

	// ------------------ Tests updatePlato() --------------------

	@Test
	void testUpdatePlato() throws IOException {
		System.out.println("TEST UPDATE CORRECTO");

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
		Assertions.assertEquals(resultado2.code(), 204);

		Response<Restaurante> resultado3 = service.getRestaurante(id1).execute();
		// comprobamos el cambio de estado del objeto
		Assertions.assertEquals(resultado3.body().getPlatos().get(0).getDescripcion(), "Cambio descripcion");
		Assertions.assertEquals(resultado3.body().getPlatos().get(0).getPrecio(), 20.0);

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
		Assertions.assertEquals(resultado2.code(), 404);
		// el restaurnate no existe en el repositorio

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
		Assertions.assertEquals(resultado2.code(), 400);
		Assertions.assertEquals(resultado2.errorBody().string(), "plato: no existe en este restaurante");

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

		Response<Void> resultado2 = service.updatePlato(" ", plato).execute();
		Assertions.assertEquals(resultado2.code(), 400);

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
		Assertions.assertEquals(resultado2.code(), 400);
		Assertions.assertEquals(resultado2.errorBody().string(), "nombre del plato: no debe ser nulo ni vacio");

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
		Assertions.assertEquals(resultado2.code(), 400);
		Assertions.assertEquals(resultado2.errorBody().string(), "descripcion del plato: no debe ser nulo ni vacio");

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
		Assertions.assertEquals(resultado2.code(), 400);
		Assertions.assertEquals(resultado2.errorBody().string(), "precio del plato: no debe ser nulo ni vacio");

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
		String id = url1.substring(url1.lastIndexOf("/") + 1);

		System.out.println(id);

		PlatoRequest plato = new PlatoRequest();
		plato.setNombre("Plato de prueba");
		plato.setDescripcion("Descripcion del plato");
		plato.setPrecio("10");
		plato.setDisponibilidad(true);

		service.addPlato(id, plato).execute();

		PlatoRequest plato2 = new PlatoRequest();
		plato2.setNombre("Plato de prueba"); // actua como id
		plato2.setDescripcion("Descripcion del plato actualizada");
		plato2.setPrecio("20");

		Response<Void> resultado3 = service.updatePlato(id, plato2).execute();
		Restaurante res = service.getRestaurante(id).execute().body();
		Assertions.assertEquals(resultado3.code(), 204);
		Assertions.assertFalse(res.getPlatos().get(0).isDisponibilidad());

		// Comprobamos que la disponibilidad del plato por defecto, al no especificar
		// ninguna, es false
		System.out.println("Código de respuesta: " + resultado3.code());
		System.out.println("Mensaje de respuesta: " + resultado3.message());
		System.out.println("Disponibilidad del plato: " + res.getPlatos().get(0).isDisponibilidad());
		System.out.println("-----------------------------------------------");
		Assertions.assertFalse(res.getPlatos().get(0).isDisponibilidad());
		Assertions.assertEquals(res.getPlatos().get(0).getPrecio(), 20.00);
		Assertions.assertEquals(res.getPlatos().get(0).getDescripcion(), "Descripcion del plato actualizada");

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
		Assertions.assertEquals(resultado2.code(), 204);

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta:" + resultado2.message());

		System.out.println("-------------------");

	}

	@Test
	void testRemoveRestauranteNoExiste() throws IOException {

		Response<Void> resultado2 = service.removeRestaurante("642d59a2f832f95e42e82bc8").execute();
		Assertions.assertEquals(resultado2.code(), 404);

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta:" + resultado2.message());
		System.out.println("Mensaje de respuesta: " + resultado2.errorBody().string());

		System.out.println("-------------------");

	}

	// Mismo problema con el ID vacio
	@Test
	void testRemoveRestauranteIdVacio() throws IOException {

		Response<Void> resultado2 = service.removeRestaurante(" ").execute();
		// no es un argumento valido por la representancion del id
		Assertions.assertEquals(resultado2.code(), 400);

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

		// Comprobamos que el contenido de la respuesta sea en formato JSON
		Assertions.assertEquals(resultado2.headers().get("Content-Type"), "application/json");
		Assertions.assertEquals(resultado2.code(), 200);

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Restaurante: " + res.toString());
		System.out.println("-------------------------------");

	}

	@Test
	void testGetRestauranteIdNoValido() throws IOException {
		System.out.println("TEST GETRESTAURANTE ID NO VALIDO");

		Response<Restaurante> resultado2 = service.getRestaurante("KSANKFALKS").execute();
		Assertions.assertEquals(resultado2.code(), 400);

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());

		System.out.println("-------------------------------");

	}

	@Test
	void testGetRestauranteIdVacio() throws IOException {

		System.out.println("TEST GETRESTAURANTE ID VACIO");

		Response<Restaurante> resultado2 = service.getRestaurante(" ").execute();
		Assertions.assertEquals(resultado2.code(), 400);
		Assertions.assertEquals(resultado2.errorBody().string(), "id del restaurante: no debe ser nulo ni vacio");

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Mensaje de respuesta: " + resultado2.errorBody().string());

		System.out.println("-------------------------------");

	}

	@Test
	void testGetRestauranteNotFound() throws IOException {

		System.out.println("TEST GETRESTAURANTE ID VACIO");

		Response<Restaurante> resultado2 = service.getRestaurante("642d59a2f832f95e42e82bc8").execute();
		Assertions.assertEquals(resultado2.code(), 404);
		Assertions.assertEquals(resultado2.errorBody().string(),
				"642d59a2f832f95e42e82bc8 no existe en el repositorio");

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
		// Comprobamos que el formato sea JSON
		Assertions.assertEquals(resultado2.headers().get("Content-Type"), "application/json");
		Assertions.assertEquals(resultado2.code(), 200);

		for (ResumenExtendido re : res.getRestaurante()) {
			System.out.println(re.getUrl());
			System.out.println(re.getResumen());
		}
	}

	// -------------------- Tests activarValoraciones ----------------

	@Test
	void testActivarValoracionesYaActivadas() throws IOException {
		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba RetrofitGetRestaurante");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();

		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		Response<Void> resultado2 = service.activarValoraciones(id1).execute();
		Response<Void> resultado3 = service.activarValoraciones(id1).execute();

		Assertions.assertEquals(resultado3.code(), 400);
		Assertions.assertEquals(resultado3.errorBody().string(), "El restaurante ya cuenta con valoraciones creadas");
	}

	@Test
	void testActivarValoraciones() throws IOException {
		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba RetrofitGetRestaurante");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();

		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		Response<Void> resultado2 = service.activarValoraciones(id1).execute();

		Assertions.assertEquals(resultado2.code(), 201);
	}

	@Test
	void testActivarValoracionesRestauranteNotFound() throws IOException {

		Response<Void> resultado2 = service.activarValoraciones(" ").execute();
		Assertions.assertEquals(resultado2.code(), 400);
		Assertions.assertEquals(resultado2.errorBody().string(), "id del restaurante: no debe ser nulo ni vacio");
	}

	// -------------------- Tests getValoraciones -------------------
	@Test
	void testGetValoracionesRestauranteNotFound() throws IOException {
		Response<List<Valoracion>> resultado2 = service.getValoraciones("642d59a2f832f95e42e82bc8").execute();
		Assertions.assertEquals(resultado2.code(), 404);
		Assertions.assertEquals(resultado2.errorBody().string(),
				"642d59a2f832f95e42e82bc8 no existe en el repositorio");
	}
	
	@Test
	void testGetValoracionesRestauranteIdVacio() throws IOException {
		Response<List<Valoracion>> resultado2 = service.getValoraciones(" ").execute();
		Assertions.assertEquals(resultado2.code(), 400);
		Assertions.assertEquals(resultado2.errorBody().string(),
				"id del restaurante: no debe ser nulo ni vacio");
	}


	@Test
	void testGetValoraciones() throws IOException {
		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba RetrofitGetRestaurante");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();

		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		Response<Void> resultado2 = service.activarValoraciones(id1).execute();
		Response<List<Valoracion>> resultado3 = service.getValoraciones(id1).execute();
		
		List<Valoracion> valoracionesIni = new LinkedList<Valoracion>();
		Assertions.assertEquals(resultado3.code(), 200);
		Assertions.assertEquals(resultado3.body(), valoracionesIni);

	}
	
	@Test
	void testGetValoracionesNotVal() throws IOException {
		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setNombre("Prueba RetrofitGetRestaurante");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30001");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado = service.createRestaurante(restaurante).execute();

		String url1 = resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		Response<List<Valoracion>> resultado3 = service.getValoraciones(id1).execute();
		
		List<Valoracion> valoracionesIni = new LinkedList<Valoracion>();
		Assertions.assertEquals(resultado3.code(), 400);
		Assertions.assertEquals(resultado3.errorBody().string(),
				"El restaurante no tiene valoraciones activadas");

	}
}
