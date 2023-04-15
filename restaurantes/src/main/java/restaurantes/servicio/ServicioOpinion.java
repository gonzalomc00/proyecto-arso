package restaurantes.servicio;

import java.io.IOException;
import java.time.LocalDateTime;

import opiniones.modelo.Opinion;
import restaurantes.modelo.Restaurante;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServicioOpinion implements IServicioOpinion {
	
	Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:5000/api/")
			.addConverterFactory(GsonConverterFactory.create()).build();

	IServicioOpinion service = retrofit.create(IServicioOpinion.class);

	@Override
	public Call<Opinion> getOpinion(String id) {
		try {
			
			Response<Opinion> resultado = service.getOpinion(id).execute();
			Opinion o= resultado.body();
			System.out.println(o.getNombreRecurso());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Call<String> createOpinion(String nombre) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Call<Void> deleteOpinion(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Call<Void> addValoracion(String id, String correo, LocalDateTime fecha, double calificacion,
			String comentario) {
		// TODO Auto-generated method stub
		return null;
	}

}
