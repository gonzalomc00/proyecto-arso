package opiniones.graphql;

import com.coxautodev.graphql.tools.GraphQLRootResolver;

import opiniones.modelo.Opinion;
import opiniones.servicio.IServicioOpinion;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import servicio.FactoriaServicios;

public class Query implements GraphQLRootResolver {
    
	private IServicioOpinion servicio = FactoriaServicios.getServicio(IServicioOpinion.class);
	
	public Opinion findById(String id) throws RepositorioException, EntidadNoEncontrada {
		return servicio.getOpinionById(id);
	}
	
	
	

}