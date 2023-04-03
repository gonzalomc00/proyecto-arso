package cliente.tests;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import restaurantes.modelo.Restaurante;
import restaurantes.modelo.SitioTuristico;
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

		Response<List<SitioTuristico>> resultado = service.getSitiosTuristicos("64174b30afc54153b546599e").execute();
		System.out.println(resultado);
		System.out.println("-------------------");
	}

// ------------------ Tests updateRestaurante() --------------------

	@Test
	void testUpdateRestauranteCorrecto() throws IOException {
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

	}

	@Test
	void testUpdateRestauranteNoExiste() throws IOException {
		RestauranteRequest update = new RestauranteRequest();
		update.setNombre("Actualizacion Retrofit");
		update.setCiudad("Ciudad actualizada");
		update.setCp("99999");
		update.setCoordenadas("1, 1");
		Response<Void> resultado2 = service.updateRestaurante("642ab5d42cf9e474fcf75a1b", update).execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Cuerpo del mensaje: " + resultado2.errorBody().string());

	}

	@Test
	void testUpdateRestauranteSinNombre() throws IOException {
		RestauranteRequest update = new RestauranteRequest();
		update.setCiudad("Ciudad actualizada");
		update.setCp("99999");
		update.setCoordenadas("1, 1");
		Response<Void> resultado2 = service.updateRestaurante("642ab5d42cf9e474fcf75a1b", update).execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Cuerpo del mensaje: " + resultado2.errorBody().string());

	}

	@Test
	void testUpdateRestauranteSinCiudad() throws IOException {
		RestauranteRequest update = new RestauranteRequest();
		update.setNombre("Actualizacion Retrofit");
		update.setCp("99999");
		update.setCoordenadas("1, 1");
		Response<Void> resultado2 = service.updateRestaurante("642ab5d42cf9e474fcf75a1b", update).execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Cuerpo del mensaje: " + resultado2.errorBody().string());

	}

	@Test
	void testUpdateRestauranteSinCp() throws IOException {
		RestauranteRequest update = new RestauranteRequest();
		update.setNombre("Actualizacion Retrofit");
		update.setCiudad("Ciudad actualizada");
		update.setCoordenadas("1, 1");
		Response<Void> resultado2 = service.updateRestaurante("642ab5d42cf9e474fcf75a1b", update).execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Cuerpo del mensaje: " + resultado2.errorBody().string());

	}

	@Test
	void testUpdateRestauranteSinCoordenadas() throws IOException {
		RestauranteRequest update = new RestauranteRequest();
		update.setNombre("Actualizacion Retrofit");
		update.setCiudad("Ciudad actualizada");
		update.setCp("99999");
		Response<Void> resultado2 = service.updateRestaurante("642ab5d42cf9e474fcf75a1b", update).execute();

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Cuerpo del mensaje: " + resultado2.errorBody().string());

	}

	// ------------------ Tests addPlato() --------------------

	@Test
	void testAddPlato() throws IOException {
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
		System.out.println("Mensaje de respuesta: " + resultado2.message().toString());

		//TODO: Duda: plato repetido deberia devolver BadRequest (400) o IllegalState (Server Error 500) ??
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

		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("Restaurante: " + res.toString()); //TODO: Coordenadas rarillas 
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

		Response<Restaurante> resultado2 = service.getRestaurante("").execute();
		Restaurante res = resultado2.body();
		
		//TODO: devuelve un restaurante todo null ?????????? porque no salta el error
		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: " + resultado2.message());
		System.out.println("RESTAURANTE: " + res.toString());
		
		System.out.println("-------------------------------");

	}

}
