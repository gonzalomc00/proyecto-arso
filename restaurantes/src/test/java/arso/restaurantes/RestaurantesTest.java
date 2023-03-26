package arso.restaurantes;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import repositorio.EntidadNoEncontrada;
import repositorio.FactoriaRepositorios;
import repositorio.Repositorio;
import repositorio.RepositorioException;
import restaurantes.modelo.Restaurante;
import restaurantes.servicio.IServicioRestaurante;
import servicio.FactoriaServicios;

public class RestaurantesTest {
	
	private Repositorio<Restaurante, String> repositorio;
	private IServicioRestaurante servicio;

	@BeforeEach
	public void setUp() {
		repositorio  = FactoriaRepositorios.getRepositorio(Restaurante.class);
		servicio = FactoriaServicios.getServicio(IServicioRestaurante.class);
		
	}

	
	@Test
	public void testRestauranteGetRestauranteByIdFailureEntidadNoEncontrada() throws RepositorioException {
		
		EntidadNoEncontrada thrown = Assertions.assertThrows(EntidadNoEncontrada.class, () -> {
		repositorio.getById("3");
		
		    });
		Assertions.assertEquals("3 no existe en el repositorio", thrown.getMessage());
	}
	
	@Test
	public void testGetRestauranteFromRepositorio() throws RepositorioException, EntidadNoEncontrada {
		Restaurante r = repositorio.getById("1");
		Assertions.assertEquals(r.getId(),"1");
		
	}
	
}
