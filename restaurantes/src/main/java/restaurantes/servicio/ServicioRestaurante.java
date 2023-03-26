package restaurantes.servicio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.geojson.Point;

import repositorio.EntidadNoEncontrada;
import repositorio.FactoriaRepositorios;
import repositorio.Repositorio;
import repositorio.RepositorioException;
import restaurantes.modelo.Plato;
import restaurantes.modelo.Restaurante;
import restaurantes.modelo.SitioTuristico;

public class ServicioRestaurante implements IServicioRestaurante {

	private Repositorio<Restaurante, String> repositorio = FactoriaRepositorios.getRepositorio(Restaurante.class);

	@Override
	public String create(String nombre, String cp, String ciudad, Point coordenadas) throws RepositorioException {

		Restaurante restaurante = new Restaurante(nombre, cp, ciudad, coordenadas);

		String id = repositorio.add(restaurante);
		return id;
	}

	public void update(Restaurante restaurante) throws RepositorioException, EntidadNoEncontrada {
		repositorio.update(restaurante);
	}

	@Override
	public List<SitioTuristico> obtenerSitiosTuristicos(String idRes) throws MalformedURLException, SAXException,
			IOException, ParserConfigurationException, RepositorioException, EntidadNoEncontrada {

		Restaurante r = repositorio.getById(idRes);

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
	public void setSitiosTuristicos(String idRes, List<SitioTuristico> sitios)
			throws RepositorioException, EntidadNoEncontrada {
		Restaurante r = repositorio.getById(idRes);
		r.setSitios(sitios);
		repositorio.update(r);
	}

	@Override
	public String addPlato(String idRes, Plato p) throws RepositorioException, EntidadNoEncontrada {
		Restaurante r = repositorio.getById(idRes);

		// Tratamiento de errores
		List<Plato> listaPlatos = r.getPlatos();

		for (Plato plato : listaPlatos) {
			if (plato.getNombre().equals(p.getNombre())) {
				throw new IllegalArgumentException("ERROR: plato ya existente");
			}

		}

		r.add(p);
		
		repositorio.update(r);
		 
		return p.getNombre();

	}

	@Override
	public void removePlato(String idRes, String nombrePlato) throws RepositorioException, EntidadNoEncontrada {
		Restaurante r = repositorio.getById(idRes);
		r.remove(nombrePlato);
		repositorio.update(r);

	}

	@Override
	public void updatePlato(String idRes, Plato plato) throws RepositorioException, EntidadNoEncontrada {

		Restaurante r = repositorio.getById(idRes);
		r.remove(plato.getNombre());
		r.add(plato);
	}

	@Override
	public void deleteRestaurante(String idRes) throws RepositorioException, EntidadNoEncontrada {
		Restaurante r = repositorio.getById(idRes);
		repositorio.delete(r);

	}

	@Override
	public List<RestauranteResumen> getListadoRestaurantes() throws RepositorioException {

		List<Restaurante> restaurantes = repositorio.getAll();
		List<RestauranteResumen> resumenes = new ArrayList<RestauranteResumen>();

		for (Restaurante r : restaurantes) {
			RestauranteResumen rr = new RestauranteResumen();
			r.setId(r.getId());
			r.setNombre(r.getNombre());
			r.setCp(r.getCp());
			resumenes.add(rr);
		}

		return resumenes;
	}

}
