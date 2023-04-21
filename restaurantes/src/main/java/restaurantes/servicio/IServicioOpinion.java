package restaurantes.servicio;


import java.io.IOException;

import opiniones.modelo.Opinion;


public interface IServicioOpinion {
	Opinion getOpinion(String idOpinion);
	
	String createOpinion(String nombreRes) throws IOException;
	
	
}
