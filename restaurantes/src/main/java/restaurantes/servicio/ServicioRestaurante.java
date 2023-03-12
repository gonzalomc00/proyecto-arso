package restaurantes.servicio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue.ValueType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

import restaurantes.modelo.Plato;
import restaurantes.modelo.Restaurante;
import restaurantes.modelo.SitioTuristico;

public class ServicioRestaurante implements IServicioRestaurante {

	ConnectionString connectionString = new ConnectionString(
			"mongodb+srv://sofia:sofia@zeppelinum.68qbknn.mongodb.net/?retryWrites=true&w=majority");

	CodecRegistry pojoCodecRegistry = CodecRegistries
			.fromProviders(PojoCodecProvider.builder().automatic(true).build());
	CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
			pojoCodecRegistry);
	MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString)
			.codecRegistry(codecRegistry).serverApi(ServerApi.builder().version(ServerApiVersion.V1).build()).build();
	MongoClient mongoClient = MongoClients.create(settings);
	MongoDatabase database = mongoClient.getDatabase("ZeppelinUM");
	MongoCollection<Restaurante> collection = database.getCollection("restaurante", Restaurante.class)
			.withCodecRegistry(codecRegistry);

	@Override
	public String create(Restaurante restaurante) {

		collection.insertOne(restaurante);
		return restaurante.getId();

	}

	@Override
	public void update(Restaurante restaurante) {
		collection.updateOne(Filters.eq("_id", restaurante.getId()), new Document("cp", restaurante.getCp()));
		collection.updateOne(Filters.eq("_id", restaurante.getId()), new Document("nombre", restaurante.getNombre()));
	}

	@Override
	public List<SitioTuristico> obtenerSitiosTuristicos(String idRes)
			throws MalformedURLException, SAXException, IOException, ParserConfigurationException {
		Bson query = Filters.eq("_id", new ObjectId(idRes));
		FindIterable<Restaurante> resultados = collection.find(query);
		MongoCursor<Restaurante> it = resultados.iterator();
		Restaurante r = it.tryNext();

		System.out.println(r);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		// 1. Obtener una factoría
		DocumentBuilderFactory factoria = DocumentBuilderFactory.newInstance();
		// 2. Pedir a la factoría la construcción del analizador
		DocumentBuilder analizador = factoria.newDocumentBuilder();
		// 3. Analizar el documento
		org.w3c.dom.Document documento = analizador
				.parse(new URL("http://api.geonames.org/findNearbyWikipedia?postalcode=" + r.getCp()
						+ "&country=ES&username=arso_gs&lang=ES").openStream());

		NodeList elementos = documento.getElementsByTagName("title");

		List<SitioTuristico> sitios = new LinkedList<SitioTuristico>();

		for (int i = 0; i < elementos.getLength(); i++) {

			Element entry = (Element) elementos.item(i);

			String sitio = entry.getTextContent().replace(' ', '_');

			// JSON

			InputStreamReader fuente = new InputStreamReader(
					new URL("https://es.dbpedia.org/data/" + sitio + ".json").openStream());
			System.out.println("-----------------------------------------------------");

			JsonReader jsonReader = Json.createReader(fuente);
			JsonObject obj = jsonReader.readObject();

			SitioTuristico sitio_clase = new SitioTuristico();
			sitio_clase.setNombre(sitio);

			JsonObject infoSitio = obj.getJsonObject("http://es.dbpedia.org/resource/" + sitio);

			JsonArray categorias = infoSitio.getJsonArray("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
			JsonArray propiedadesResumen = infoSitio.getJsonArray("http://dbpedia.org/ontology/abstract");

			boolean check = false;

			System.out.println(sitio);

			System.out.println("CATEGORIAS: ");
			List<String> categorias_clase = new LinkedList<String>();

			for (JsonObject categoria : categorias.getValuesAs(JsonObject.class)) {
				// categoria
				if (categoria.getString("value").equals("http://dbpedia.org/ontology/ArchitecturalStructure")
						|| categoria.getString("value").equals("http://dbpedia.org/ontology/HistoricBuilding")) {
					System.out.println(categoria.getString("value"));
					categorias_clase.add(categoria.getString("value"));
					check = true;
				}
			}

			sitio_clase.setCategorias(categorias_clase);

			if (check == true) {
				System.out.println("FOTO: ");
				// COMPROBACION DE LA FOTO
				if (infoSitio.containsKey("http://es.dbpedia.org/property/imagen")) {

					JsonObject propiedadesImagen = infoSitio.getJsonArray("http://es.dbpedia.org/property/imagen")
							.getJsonObject(0);

					if (propiedadesImagen.get("value").getValueType().equals(ValueType.NUMBER)) {
						System.out.println(propiedadesImagen.getJsonNumber("value"));
						sitio_clase.setFoto(propiedadesImagen.getJsonNumber("value").toString());
					} else if (propiedadesImagen.get("value").getValueType().equals(ValueType.STRING)) {
						System.out.println(propiedadesImagen.getJsonString("value"));
						sitio_clase.setFoto(propiedadesImagen.getString("value"));
					}

				} else {
					System.out.println("No tiene foto");
				}

				System.out.println("RESUMEN: " + propiedadesResumen.getJsonObject(0).getJsonString("value"));
				sitio_clase.setResumen(propiedadesResumen.getJsonObject(0).getString("value"));

				System.out.println("ENLACES: ");
				if (infoSitio.containsKey("http://dbpedia.org/ontology/wikiPageExternalLink")) {
					List<String> enlaces_clase = new LinkedList<String>();
					JsonArray enlaces = infoSitio.getJsonArray("http://dbpedia.org/ontology/wikiPageExternalLink");
					for (JsonObject enlace : enlaces.getValuesAs(JsonObject.class)) {
						// categoria
						System.out.println(enlace.getJsonString("value"));
						enlaces_clase.add(enlace.getString("value"));
					}
					sitio_clase.setEnlaces(enlaces_clase);

				} else {
					System.out.println("No tiene enlaces externos");
				}

				sitios.add(sitio_clase);
			}
		}
		return sitios;
	}

	@Override
	public void setSitiosTuristicos(String idRes, List<SitioTuristico> sitios) {
		Bson filter = Filters.eq("_id", new ObjectId(idRes));
		Document actualizacion = new Document("$set", new Document("sitios", sitios));
		collection.updateOne(filter, actualizacion);

	}

	@Override
	public void addPlato(String idRes, Plato p) {
		Bson filter = Filters.eq("_id", new ObjectId(idRes));
		Document actualizacion = new Document("$push", new Document("platos", p));
		collection.updateOne(filter, actualizacion);

	}

	@Override
	public void removePlato(String idRes, String nombrePlato) {

		Bson filter = Filters.eq("_id", new ObjectId(idRes));

		Document actualizacion = new Document("$pull", new Document("platos", new Document("nombre", nombrePlato)));
		collection.updateOne(filter, actualizacion);

	}

	@Override
	public void updatePlato(String idRes, Plato plato) {
		// TODO: revisar
		Bson filter = Filters.eq("_id", new ObjectId(idRes));

		Bson update = Updates.set("platos.$[elem]", plato);
		Document arrayFilters = new Document("elem.nombre", plato.getNombre());

		// Ejecutar la actualización
		collection.updateOne(filter, update, new UpdateOptions().arrayFilters(Collections.singletonList(arrayFilters)));
	}

	@Override
	public void deleteRestaurante(String idRes) {
		Bson filter = Filters.eq("_id", new ObjectId(idRes));
		collection.deleteOne(filter);
	}

	@Override
	public List<RestauranteResumen> getListadoRestaurantes() {
		FindIterable<Restaurante> resultados = collection.find();

		MongoCursor<Restaurante> it = resultados.iterator();
		List<RestauranteResumen> resumenes = new ArrayList<RestauranteResumen>();
		
		while (it.hasNext()) {
			RestauranteResumen r = new RestauranteResumen();
			r.setId(it.next().getId());
			r.setNombre(it.next().getNombre());
			r.setCp(it.next().getCp());
			resumenes.add(r);
		}

		return resumenes;
	}

}
