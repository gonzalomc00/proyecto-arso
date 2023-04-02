package cliente.tests;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import restaurantes.modelo.SitioTuristico;
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
		restaurante.setCp("30001");
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
		System.out.println("Cuerpo del mensaje:" + resultado.errorBody().string());
		System.out.println("-------------------");
		
		RestauranteRequest restaurante2 = new RestauranteRequest();
		restaurante2.setNombre("Prueba Retrofit");
		restaurante2.setCp("30161");
		restaurante2.setCoordenadas("20, 10");
		Response<Void> resultado2= service.createRestaurante(restaurante2).execute();
		
		System.out.println("Código de respuesta: " + resultado2.code());
		System.out.println("Mensaje de respuesta: "+ resultado2.message());
		System.out.println("Cuerpo del mensaje:" + resultado2.errorBody().string());
		System.out.println("-------------------");
		
		RestauranteRequest restaurante3 = new RestauranteRequest();
		restaurante3.setNombre("Prueba Retrofit");
		restaurante3.setCiudad("Murcia");
		restaurante3.setCoordenadas("20, 10");
		Response<Void> resultado3= service.createRestaurante(restaurante3).execute();
		
		System.out.println("Código de respuesta: " + resultado3.code());
		System.out.println("Mensaje de respuesta: "+ resultado3.message());
		System.out.println("Cuerpo del mensaje:" + resultado3.errorBody().string());
		System.out.println("-------------------");
		
		RestauranteRequest restaurante4 = new RestauranteRequest();
		restaurante4.setNombre("Prueba Retrofit");
		restaurante4.setCiudad("Murcia");
		restaurante4.setCp("30161");
		Response<Void> resultado4= service.createRestaurante(restaurante4).execute();
		
		System.out.println("Código de respuesta: " + resultado4.code());
		System.out.println("Mensaje de respuesta: "+ resultado4.message());
		System.out.println("Cuerpo del mensaje:" + resultado4.errorBody().string());
		System.out.println("-------------------");
		
		
	}
	
	@Test
	void testGetSitiosTuristicos() throws IOException {
		Response<List<SitioTuristico>> resultado= service.getSitiosTuristicos("64174b30afc54153b546599e").execute();
		System.out.println(resultado);
	}
	

	
	@Test
	void test() {
		fail("Not yet implemented");
	}

}
