package restaurantes.servicio;


import java.io.IOException;

import opiniones.modelo.Opinion;


public interface IServicioOpinion {
	//recuperar opinion
	Opinion getOpinion(String idOpinion);
	
	//activar valoraciones
	String createOpinion(String nombreRes) throws IOException;
	
	
}
