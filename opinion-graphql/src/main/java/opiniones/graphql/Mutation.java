package opiniones.graphql;

import java.time.LocalDateTime;

import com.coxautodev.graphql.tools.GraphQLRootResolver;

import opiniones.servicio.IServicioOpinion;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import servicio.FactoriaServicios;

public class Mutation implements GraphQLRootResolver {
    
	private IServicioOpinion servicio = FactoriaServicios.getServicio(IServicioOpinion.class);
	
    public String crearOpinion(String nombre) throws RepositorioException {
    	return servicio.createOpinion(nombre);
    }
    
    public boolean removeOpinion(String id) throws RepositorioException, EntidadNoEncontrada {
    	return servicio.removeOpinion(id);
    }
    
    public boolean addValoracion(String id,String correo, String fecha, Float calificacion, String comentario) throws RepositorioException, EntidadNoEncontrada {
    	return servicio.addValoracion(id, correo, LocalDateTime.parse(fecha), calificacion, comentario);
    }
    
}
