package arso.restaurantes.dom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue.ValueType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;

import arso.modelo.Restaurante;
import arso.modelo.SitioTuristico;

public class TestMongo  {

	public static void main(String[] args) throws Exception {

		ConnectionString connectionString = new ConnectionString(
				"mongodb+srv://sofia:sofia@zeppelinum.68qbknn.mongodb.net/?retryWrites=true&w=majority");
		
		CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());
	    CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString).codecRegistry(codecRegistry)
				.serverApi(ServerApi.builder().version(ServerApiVersion.V1).build()).build();
		MongoClient mongoClient = MongoClients.create(settings);
		MongoDatabase database = mongoClient.getDatabase("ZeppelinUM");
		MongoCollection<Restaurante> collection = database.getCollection("restaurante", Restaurante.class).withCodecRegistry(codecRegistry);

		
		//CREAMOS RESTAURANTE
		Restaurante r = new Restaurante();
		r.setNombre("McGonzalo");
		r.setCp("30150");
		
		
		
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		// 1. Obtener una factoría
		DocumentBuilderFactory factoria = DocumentBuilderFactory.newInstance();
		// 2. Pedir a la factoría la construcción del analizador
		DocumentBuilder analizador = factoria.newDocumentBuilder();
		// 3. Analizar el documento
		Document documento = analizador.parse(new URL(
				"http://api.geonames.org/findNearbyWikipedia?postalcode=" + r.getCp() + "&country=ES&username=arso_gs&lang=ES")
				.openStream());

		NodeList elementos = documento.getElementsByTagName("title");
		
		List<SitioTuristico> sitios= new LinkedList<SitioTuristico>();
		
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
			List<String> categorias_clase= new LinkedList<String>();

			for (JsonObject categoria : categorias.getValuesAs(JsonObject.class)) {
				// categoria
				if (categoria.getString("value").equals( "http://dbpedia.org/ontology/ArchitecturalStructure")
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
					List<String> enlaces_clase= new LinkedList<String>();
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
		
		r.setSitios(sitios);
		InsertOneResult result = collection.insertOne(r);
		
	}

}
