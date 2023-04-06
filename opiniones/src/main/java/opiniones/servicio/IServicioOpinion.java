package opiniones.servicio;

import java.time.LocalDate;

import opiniones.modelo.Opinion;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;

public interface IServicioOpinion {

	String createOpinion(String nombre) throws RepositorioException;
	
	void addValoracion(String idOpinion, String correo, LocalDate fecha, double calificacion, String comentario) throws RepositorioException, EntidadNoEncontrada;
	
	Opinion getOpinionById(String id) throws RepositorioException, EntidadNoEncontrada;
	
	void removeOpinion(String id) throws RepositorioException, EntidadNoEncontrada;
}
