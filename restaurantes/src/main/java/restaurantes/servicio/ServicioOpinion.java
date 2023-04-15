package restaurantes.servicio;

import java.io.IOException;

import opiniones.modelo.Opinion;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServicioOpinion implements IServicioOpinion {
	
	Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:5000/api/")
			.addConverterFactory(GsonConverterFactory.create()).build();

	IRetrofitOpinion service = retrofit.create(IRetrofitOpinion.class);

	
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


	@Override
	public String createOpinion(String nombreRes) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
