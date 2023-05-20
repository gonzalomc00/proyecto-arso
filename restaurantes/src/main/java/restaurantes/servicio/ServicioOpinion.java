package restaurantes.servicio;

import java.io.IOException;

import restaurantes.modelo.Opinion;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServicioOpinion implements IServicioOpinion {
	// obtenemos la variable de entorno
	String urlOpiniones = System.getenv("OPINIONES_API_URL");

	// conexion docker/kubernetes

	Retrofit retrofit = new Retrofit.Builder().baseUrl(urlOpiniones).addConverterFactory(GsonConverterFactory.create())
			.build();
	/*
	 * Retrofit retrofit = new
	 * Retrofit.Builder().baseUrl("http://opiniones:5000/api/")
	 * .addConverterFactory(GsonConverterFactory.create()).build();
	 */
	/*
	 * Retrofit retrofit = new
	 * Retrofit.Builder().baseUrl("http://localhost:5000/api/")
	 * .addConverterFactory(GsonConverterFactory.create()).build();
	 */
	IRetrofitOpinion service = retrofit.create(IRetrofitOpinion.class);

	public Opinion getOpinion(String id) {
		try {

			Response<Opinion> resultado = service.getOpinion(id).execute();
			System.out.println(resultado.body());
			Opinion o = resultado.body();
			return o;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public String createOpinion(String nombreRes) throws IOException {
		Response<String> resultado = service.createOpinion(nombreRes).execute();
		return resultado.body();

	}

}
