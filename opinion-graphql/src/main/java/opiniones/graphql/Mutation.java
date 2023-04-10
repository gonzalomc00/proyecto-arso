package opiniones.graphql;

import com.coxautodev.graphql.tools.GraphQLRootResolver;

import opiniones.servicio.IServicioOpinion;
import repositorio.RepositorioException;
import servicio.FactoriaServicios;

public class Mutation implements GraphQLRootResolver {
    
	private IServicioOpinion servicio = FactoriaServicios.getServicio(IServicioOpinion.class);
	
    public String crearOpinion(String nombre) throws RepositorioException {
    	return servicio.createOpinion(nombre);
    }
}
