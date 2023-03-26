package retrofit.restaurantes;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Programa {
	
	
	public static void main(String[] args) throws Exception {
		Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:8080/api/")
				.addConverterFactory(GsonConverterFactory.create()).build();

		RestaurantesRestClient service = retrofit.create(RestaurantesRestClient.class);
		
		
		//CREACION
		
		//1. Creación de un restaurante
		
		RestauranteRequest restaurante= new RestauranteRequest();
		restaurante.setNombre("Prueba Retrofit");
		restaurante.setCiudad("Murcia");
		restaurante.setCp("30161");
		restaurante.setCoordenadas("20, 10");
		Response<Void> resultado= service.createRestaurante(restaurante).execute();
		
		String url1= resultado.headers().get("Location");
		String id1 = url1.substring(url1.lastIndexOf("/") + 1);

		System.out.println("Restaurante creado: " + url1);
		System.out.println("Id: " + id1);
		
	}
}
