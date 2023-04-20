package restaurantes.servicio;

import java.time.LocalDateTime;

import opiniones.modelo.Opinion;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IRetrofitOpinion {

	@GET("opiniones/{id}")
	Call<Opinion> getOpinion(@Path("id")String id);
	
	@POST("opiniones")
	Call<String> createOpinion(@Body String nombre);
	
}