package restaurantes.servicio;

import java.time.LocalDate;

import opiniones.modelo.Opinion;

public interface IServicioOpinion {

	String createOpinion(String nombre);
	
	void addValoracion(String idOpinion, String correo, LocalDate fecha, double calificacion, String comentario);
	
	Opinion getOpinionById(String id);
	
	void removeOpinion(String id);
}
