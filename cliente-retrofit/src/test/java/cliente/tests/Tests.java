package cliente.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import retrofit.restaurantes.RestauranteRequest;
import retrofit.restaurantes.RestaurantesRestClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class Tests {

	Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:8080/api/")
			.addConverterFactory(GsonConverterFactory.create()).build();

	RestaurantesRestClient service = retrofit.create(RestaurantesRestClient.class);
	
	
	@Test
	void testCrearRestauranteCorrecto() throws IOException {
		RestauranteRequest restaurante= new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30161");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado= service.createRestaurante(restaurante).execute();
		
		String url1= resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		System.out.println("Restaurante creado: " + url1);
		System.out.println("Código de respuesta: " + resultado.code());
		System.out.println("Id: " + id1);
		
	}
	
	@Test
	void testCrearRestauranteDatosErroneos() throws IOException {
		RestauranteRequest restaurante = new RestauranteRequest();
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30161");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado= service.createRestaurante(restaurante).execute();
		
		System.out.println("Código de respuesta: " + resultado.code());
		System.out.println("Mensaje de respuesta: "+ resultado.message());
		System.out.println("-------------------");
		
		RestauranteRequest restaurante2 = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCp("30161");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado2= service.createRestaurante(restaurante2).execute();
		
		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: "+ resultado2.message());
		System.out.println("-------------------");
		
		RestauranteRequest restaurante3 = new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCp("30161");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado3= service.createRestaurante(restaurante3).execute();
		
		System.out.println("Código de respuesta: " + resultado3.code());
		System.out.println("Mensaje de respuesta: "+ resultado3.message());
		System.out.println("-------------------");
		
	}
	
	@Test
	void testGetSitiosTuristicos() {
		
	}
	

	
	
	@Test
	void test() {
		fail("Not yet implemented");
	}

}
