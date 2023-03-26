package retrofit.restaurantes;


import java.util.List;

import restaurantes.modelo.Plato;
import restaurantes.modelo.Restaurante;
import restaurantes.modelo.SitioTuristico;
import retrofit.restaurantes.Listado.ResumenExtendido;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RestaurantesRestClient {

	@POST("restaurantes")
	Call<Void> createRestaurante(@Body RestauranteRequest actividad);
	
	@GET("restaurantes/{id}")
	Call<List<SitioTuristico>> getSitiosTuristicos(@Path("id") String id);
	
	@PUT("restaurantes/{id}")
	Call<List<SitioTuristico>> setSitiosTuristicos(@Path("id") String id, @Body List<SitioTuristico> sitios);
	
	
	@PUT("restaurantes/{id}")
	Call<Void> updateRestaurante(@Path("id") String id, @Body Restaurante restaurante);
	
	@POST("/{id}/platos")
	Call<Void> addPlato(@Path("id") String id, @Body PlatoRequest platoDTO);
	
	@DELETE("/{id}/platos/{nombrePlato}")
	Call<Void> removePlato(@Path("id") String id, @Path("nombrePlato") String nombrePlato);
	
	@PUT("/{id}/platos/")
	Call<Void> updatePlato(@Path("id") String id, @Body Plato plato);

	@DELETE("restaurantes/{id}")
	Call<Void> removeRestaurante(@Path("id") String id);


	@GET("restaurantes")
	Call<List<ResumenExtendido>> getListadoRestaurantes();
	
	
}
