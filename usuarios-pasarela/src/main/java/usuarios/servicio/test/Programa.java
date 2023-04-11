package usuarios.servicio.test;

import repositorio.RepositorioException;
import servicio.FactoriaServicios;
import usuarios.servicio.IServicioUsuarios;

public class Programa {

	public static void main(String[] args) throws RepositorioException {
		
		IServicioUsuarios servicio = FactoriaServicios.getServicio(IServicioUsuarios.class);
		
		servicio.findAll().forEach(System.out::println);
		
		System.out.println("fin.");
		
	}
}
