package opiniones.servicio;

import java.time.LocalDateTime;

import opiniones.modelo.Opinion;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;

public interface IServicioOpinion {

	String createOpinion(String nombre) throws RepositorioException;
	
	boolean addValoracion(String idOpinion, String correo, LocalDateTime fecha, double calificacion, String comentario) throws RepositorioException, EntidadNoEncontrada;
	
	Opinion getOpinionById(String id) throws RepositorioException, EntidadNoEncontrada;
	
	boolean removeOpinion(String id) throws RepositorioException, EntidadNoEncontrada;
}
