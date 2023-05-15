package arso.restaurantes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import opiniones.modelo.Opinion;
import opiniones.modelo.Valoracion;
import restaurantes.modelo.Restaurante;
import restaurantes.servicio.IServicioOpinion;

public class ServicioOpinionStub implements IServicioOpinion{
	
	
	
	@Override
	public Opinion getOpinion(String idOpinion) {
		Restaurante r = new Restaurante("Prueba", "30150", "Murcia", 30.00, 20.00, "alguien");
		LocalDateTime fecha = LocalDateTime.now();
		List<Valoracion> valoraciones = new LinkedList<Valoracion>();
		valoraciones.add(new Valoracion("alumno@um.es", fecha, 9.00, null));
		Opinion o = new Opinion();
		o.setId("123");
		o.setNombreRecurso(r.getNombre());
		o.setValoraciones(valoraciones);		
		return o;
	}

	@Override
	public String createOpinion(String nombreRes) throws IOException {	
		String id = "idOpinion";
		return id;
	}



}
