package restaurantes.repositorio;

import repositorio.RepositorioMongo;
import restaurantes.modelo.Restaurante;

public class RepositorioRestaurantesMongo extends RepositorioMongo<Restaurante> {

	public RepositorioRestaurantesMongo() {
		super();
		collection = database.getCollection("restaurante", Restaurante.class).withCodecRegistry(codecRegistry);
	}
}
