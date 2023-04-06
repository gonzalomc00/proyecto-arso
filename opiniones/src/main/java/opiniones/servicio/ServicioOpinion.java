package opiniones.servicio;

import java.time.LocalDate;

import opiniones.modelo.Opinion;
import repositorio.FactoriaRepositorios;
import repositorio.Repositorio;
import repositorio.RepositorioException;

public class ServicioOpinion implements IServicioOpinion {

	private Repositorio<Opinion, String> repositorio = FactoriaRepositorios.getRepositorio(Opinion.class);
	
	@Override
	public String createOpinion(String nombre) throws RepositorioException {
		if (nombre == null || nombre.isEmpty())
			throw new IllegalArgumentException("nombre de la opinión: no debe ser nulo ni vacio");

		

		Opinion opinion = new Opinion();
		opinion.setNombreRecurso(nombre);

		String id = repositorio.add(opinion);
		return id;
	}

	
	@Override
	public void addValoracion(String idOpinion, String correo, LocalDate fecha, double calificacion,
			String comentario) {
		
		
		
	}

	@Override
	public Opinion getOpinionById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeOpinion(String id) {
		// TODO Auto-generated method stub
		
	}

}
