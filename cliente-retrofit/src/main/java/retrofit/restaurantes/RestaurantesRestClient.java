package retrofit.restaurantes;

import java.util.List;


import restaurantes.modelo.Restaurante;
import restaurantes.modelo.SitioTuristico;
import restaurantes.modelo.Valoracion;
import retrofit.restaurantes.Listado.ResumenExtendido;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RestaurantesRestClient {

	// HECHO
	@POST("restaurantes")
	Call<Void> createRestaurante(@Body RestauranteRequest actividad);

	// HECHO
	@GET("restaurantes/{id}/sitios")
	Call<List<SitioTuristico>> getSitiosTuristicos(@Path("id") String id);

	// HECHO
	@PUT("restaurantes/{id}/sitios")
	Call<Void> setSitiosTuristicos(@Path("id") String id, @Body List<SitioTuristico> sitios);

	// HECHO
	@PUT("restaurantes/{id}")
	Call<Void> updateRestaurante(@Path("id") String id, @Body RestauranteRequest restaurante);

	// HECHO
	@POST("restaurantes/{id}/platos")
	Call<Void> addPlato(@Path("id") String id, @Body PlatoRequest platoDTO);

	// HECHO
	@DELETE("restaurantes/{id}/platos/{nombrePlato}")
	Call<Void> removePlato(@Path("id") String id, @Path("nombrePlato") String nombrePlato);

	// HECHO
	@PUT("restaurantes/{id}/platos/")
	Call<Void> updatePlato(@Path("id") String id, @Body PlatoRequest plato);

	// HECHO
	@DELETE("restaurantes/{id}")
	Call<Void> removeRestaurante(@Path("id") String id);

	// HECHO
	@GET("restaurantes")
	Call<Listado> getListadoRestaurantes();

	// HECHO
	@GET("restaurantes/{id}")
	Call<Restaurante> getRestaurante(@Path("id") String id);

	@POST("restaurantes/{id}/valoraciones")
	Call<Void> activarValoraciones(@Path("id") String id);

	// HECHO
	@GET("restaurantes/{id}/valoraciones")
	Call<List<Valoracion>> getValoraciones(@Path("id") String id);

}
