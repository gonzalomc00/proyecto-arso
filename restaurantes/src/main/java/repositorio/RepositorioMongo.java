package repositorio;

import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;
import java.util.List;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import restaurantes.modelo.Restaurante;

public class RepositorioMongo<T extends Identificable> implements RepositorioString<T> {

	protected ConnectionString connectionString = new ConnectionString(
			"mongodb+srv://sofia:sofia@zeppelinum.68qbknn.mongodb.net/?retryWrites=true&w=majority");

	CodecRegistry pojoCodecRegistry = CodecRegistries
			.fromProviders(PojoCodecProvider.builder().automatic(true).build());
	
	protected CodecRegistry codecRegistry = CodecRegistries
			.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
	
	MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString)
			.codecRegistry(codecRegistry).serverApi(ServerApi.builder().version(ServerApiVersion.V1).build()).build();
	
	protected MongoClient mongoClient = MongoClients.create(settings);
	
	protected MongoDatabase database = mongoClient.getDatabase("ZeppelinUM");
	
	protected MongoCollection<T> collection=database.getCollection("restaurante", getGenericTypeClass())
			.withCodecRegistry(codecRegistry);;

			
	private Class<T> getGenericTypeClass() {
			    try {
			        String className = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
			        Class<?> clazz = Class.forName(className);
			        return (Class<T>) clazz;
			    } catch (Exception e) {
			        throw new IllegalStateException("Class is not parametrized with generic type!!! Please use extends <> ");
			    }
			} 
	@Override
	public String add(T entity) throws RepositorioException {

		collection.insertOne(entity);
		return entity.getId();
	}

	@Override
	public void update(T entity) throws RepositorioException, EntidadNoEncontrada {

		/*
		if (!this.collection.find(entity.getId()))
			throw new EntidadNoEncontrada(entity.getId() + " no existe en el repositorio");
		*/
		
		ObjectId idMongo = new ObjectId(entity.getId());
		    Bson filtro = Filters.eq("_id", idMongo);
		    collection.findOneAndReplace(filtro, entity);

	}

	@Override
	public void delete(T entity) throws RepositorioException, EntidadNoEncontrada {

		/*
		if (!this.entidades.containsKey(entity.getId()))
			throw new EntidadNoEncontrada(entity.getId() + " no existe en el repositorio");
		*/

		Bson filter = Filters.eq("_id", new ObjectId(entity.getId()));
		collection.deleteOne(filter);
	}

	@Override
	public T getById(String id) throws RepositorioException, EntidadNoEncontrada {

		/*
		if (!this.entidades.containsKey(id))
			throw new EntidadNoEncontrada(id + " no existe en el repositorio");
			
		*/
		ObjectId idMongo = new ObjectId(id);
	    Bson filtro = Filters.eq("_id", idMongo);
	    FindIterable<T>entity=collection.find(filtro);

		return entity.first();
	}

	@Override
	public List<T> getAll() throws RepositorioException {

		FindIterable<T> coleccion= collection.find();
		MongoCursor<T> cursor= coleccion.iterator();
		List<T> resultado= new LinkedList<T>();
		while(cursor.hasNext()) {
			resultado.add(cursor.next());
		}
		return resultado;
	}

	@Override
	public List<String> getIds() throws RepositorioException {

		FindIterable<T> coleccion= collection.find();
		MongoCursor<T> cursor= coleccion.iterator();
		List<String> resultado= new LinkedList<String>();
		while(cursor.hasNext()) {
			resultado.add(cursor.next().getId());
		}
		return resultado;
	}

}
