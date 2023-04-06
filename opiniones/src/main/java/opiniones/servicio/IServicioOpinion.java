package opiniones.servicio;

import java.time.LocalDate;

import opiniones.modelo.Opinion;
import repositorio.RepositorioException;

public interface IServicioOpinion {

	String createOpinion(String nombre) throws RepositorioException;
	
	void addValoracion(String idOpinion, String correo, LocalDate fecha, double calificacion, String comentario);
	
	Opinion getOpinionById(String id);
	
	void removeOpinion(String id);
}
