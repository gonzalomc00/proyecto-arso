package restaurantes.servicio;


import opiniones.modelo.Opinion;


public interface IServicioOpinion {
	Opinion getOpinion(String idOpinion);
	
	String createOpinion(String nombreRes);
	
	
}
