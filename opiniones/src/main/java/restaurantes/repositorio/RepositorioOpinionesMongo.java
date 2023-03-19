package restaurantes.repositorio;

import opiniones.modelo.Opinion;
import repositorio.RepositorioMongo;

public class RepositorioOpinionesMongo extends RepositorioMongo<Opinion>{

	public RepositorioOpinionesMongo() {
		super();
		collection=database.getCollection("restaurante", Opinion.class)
				.withCodecRegistry(codecRegistry);
	}
}
