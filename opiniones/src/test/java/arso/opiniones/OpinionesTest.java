package arso.opiniones;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import opiniones.modelo.Opinion;
import opiniones.modelo.Valoracion;
import opiniones.repositorio.RepositorioOpinionesMongo;
import opiniones.servicio.IServicioOpinion;
import repositorio.EntidadNoEncontrada;
import repositorio.FactoriaRepositorios;
import repositorio.Repositorio;
import repositorio.RepositorioException;
import servicio.FactoriaServicios;

@Disabled
public class OpinionesTest {

	private Repositorio<Opinion, String> repositorio = FactoriaRepositorios.getRepositorio(Opinion.class);
	private IServicioOpinion servicio = FactoriaServicios.getServicio(IServicioOpinion.class);


	//----------- Test repositorio ------------------------

	@Test
	public void testRepositorio() throws RepositorioException {
		RepositorioOpinionesMongo rep = new RepositorioOpinionesMongo();
		Opinion opi = new Opinion();
		opi.setNombreRecurso("papas malas2");
		
		repositorio.add(opi);
		
	}
	//----------------- Test createOpinon -------------------------
	@Test
	public void testCreateOpinon() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.createOpinion("papas malas");
		Opinion op = servicio.getOpinionById(id);
		
		List<Valoracion> valoraciones = new LinkedList<Valoracion>();
		
		Assertions.assertEquals(op.getNombreRecurso(),"papas malas");
		Assertions.assertEquals(op.getNumValoraciones(), 0);
		Assertions.assertEquals(op.getMedia(), 0.00);
		Assertions.assertEquals( op.getValoraciones(), valoraciones );
		
		
		
	}
	
	//--------------------- Test addValoracion -----------------------
	@Test 
	public void testAddVal() throws RepositorioException, EntidadNoEncontrada {
		String id = servicio.createOpinion("papitas malitas3");
		
		servicio.addValoracion(id, "sofia.maciasm@um.es", LocalDateTime.now(), 5, null);
		servicio.addValoracion(id, "sofia.maciasm@um.es", LocalDateTime.now(), 3, null);

		
	}
	
	@Test 
	public void testRemoveOpinion() throws RepositorioException, EntidadNoEncontrada {
		
		servicio.addValoracion("642e9529896fa933209b7ebb", "sofia.maciasm@um.es", LocalDateTime.now(), 5, null);
		servicio.removeOpinion("642e9529896fa933209b7ebb");
		
	}



}
