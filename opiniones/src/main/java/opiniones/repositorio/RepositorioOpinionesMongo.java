package opiniones.repositorio;

import opiniones.modelo.Opinion;
import repositorio.RepositorioMongo;

public class RepositorioOpinionesMongo extends RepositorioMongo<Opinion>{

	public RepositorioOpinionesMongo() {
		super();
		collection=database.getCollection("opinion", Opinion.class)
				.withCodecRegistry(codecRegistry);
	}
}
