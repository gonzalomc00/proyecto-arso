package usuarios.servicio;

import java.util.List;

import repositorio.EntidadNoEncontrada;
import repositorio.FactoriaRepositorios;
import repositorio.Repositorio;
import repositorio.RepositorioException;
import usuarios.modelo.Usuario;

public class ServicioUsuarios implements IServicioUsuarios {

	private Repositorio<Usuario, String> repositorio = FactoriaRepositorios.getRepositorio(Usuario.class);
	
	@Override
	public Usuario findByEmail(String email) throws RepositorioException, EntidadNoEncontrada {
		
		// FIXME: implementar el patrón especificación
		
		Usuario resultado = repositorio.getAll().stream()
			.filter(usuario -> usuario.getEmail().equals(email))
			.findFirst().orElseThrow(() -> new EntidadNoEncontrada("No existe usuario con email: " + email));
		
		return resultado;
	}
	
	@Override
	public Usuario findByOAuthId(String oauthId) throws RepositorioException, EntidadNoEncontrada {
		
		// FIXME: implementar el patrón especificación
		
		Usuario resultado = repositorio.getAll().stream()
			.filter(usuario -> usuario.getIdOAuth().equals(oauthId))
			.findFirst().orElseThrow(() -> new EntidadNoEncontrada("No existe usuario con id OAuth: " + oauthId));
		
		return resultado;
	}
	
	@Override
	public List<Usuario> findAll() throws RepositorioException {
		
		return repositorio.getAll();
	}
	
}
