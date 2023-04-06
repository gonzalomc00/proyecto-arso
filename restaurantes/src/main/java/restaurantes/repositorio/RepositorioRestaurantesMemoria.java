package restaurantes.repositorio;

import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import repositorio.RepositorioMemoria;
import restaurantes.modelo.Plato;
import restaurantes.modelo.Restaurante;

public class RepositorioRestaurantesMemoria extends RepositorioMemoria<Restaurante> {

	public RepositorioRestaurantesMemoria() throws EntidadNoEncontrada {
	try {
		Restaurante r1 = new Restaurante();
		r1.setNombre("Burger Queen");
		r1.setCiudad("Murcia");
		
		r1.setLatitud(20.0);
		r1.setLongitud(30.0);
		r1.setCp("30010");
		r1.setId("1");
		
		Plato p1 = new Plato();
		p1.setNombre("chessy burger");
		p1.setPrecio(10.0);
		p1.setDisponibilidad(true);
		p1.setDescripcion("hamburguesa de pollo con queso");
		
		r1.add(p1);
		this.add(r1);
		this.update(r1);
	} catch (RepositorioException e) {
		
		// No debe suceder
		e.printStackTrace();
	}
}
}