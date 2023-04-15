package restaurantes.servicio;

import java.io.IOException;
import java.time.LocalDateTime;

import opiniones.modelo.Opinion;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServicioOpinion {
	
	Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:5000/api/")
			.addConverterFactory(GsonConverterFactory.create()).build();

	IServicioOpinion service = retrofit.create(IServicioOpinion.class);

	
	public Opinion getOpinion(String id) {
		try {
			
			Response<Opinion> resultado = service.getOpinion(id).execute();
			
			Opinion o= resultado.body();
			return o;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	
	public Call<String> createOpinion(String nombre) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Call<Void> deleteOpinion(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public Call<Void> addValoracion(String id, String correo, LocalDateTime fecha, double calificacion,
			String comentario) {
		// TODO Auto-generated method stub
		return null;
	}

}
