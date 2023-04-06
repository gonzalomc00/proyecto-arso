package opiniones.servicio;

import java.time.LocalDate;

import opiniones.modelo.Opinion;
import opiniones.modelo.Valoracion;
import repositorio.EntidadNoEncontrada;
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
			String comentario) throws RepositorioException, EntidadNoEncontrada {
		
		if (idOpinion == null || idOpinion.isEmpty())
			throw new IllegalArgumentException("id de la opinión: no debe ser nulo ni vacio");
		
		if (correo == null || correo.isEmpty())
			throw new IllegalArgumentException("correo de la valoracion: no debe ser nulo ni vacio");
		
		if (fecha==null)
			throw new IllegalArgumentException("fecha de la valoracion: no debe ser nulo ni vacio");
		
		if (calificacion<1 || calificacion>5)
			throw new IllegalArgumentException("calificacion de la valoracion: no puede ser menor 	que 1 o mayor que 5");

		//PREGUNTAR
		if(comentario==null) {
			comentario="";
		}
		
		
		Opinion opinion= repositorio.getById(idOpinion);
		
		Valoracion valoracion= new Valoracion(correo,fecha,calificacion,comentario);
		opinion.add(valoracion);
		repositorio.update(opinion);

	}

	@Override
	public Opinion getOpinionById(String id) throws RepositorioException, EntidadNoEncontrada {
		if (id == null || id.isEmpty())
			throw new IllegalArgumentException("id de la opinión: no debe ser nulo ni vacio");
		
		
		return repositorio.getById(id);
	}

	@Override
	public void removeOpinion(String id) throws RepositorioException, EntidadNoEncontrada {
		
		if (id == null || id.isEmpty())
			throw new IllegalArgumentException("id de la opinión: no debe ser nulo ni vacio");
		

		repositorio.delete(repositorio.getById(id));
		
	}

}
