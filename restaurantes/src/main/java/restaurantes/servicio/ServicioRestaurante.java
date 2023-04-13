package restaurantes.servicio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
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
	public String create(String nombre, String cp, String ciudad, Double latitud, Double longitud, String u)
			throws RepositorioException {

		if (nombre == null || nombre.isEmpty())
			throw new IllegalArgumentException("nombre del restaurante: no debe ser nulo ni vacio");

		if (cp == null || cp.isEmpty())
			throw new IllegalArgumentException("codigo postal: no debe ser nulo ni vacio");

		if (ciudad == null || ciudad.isEmpty())
			throw new IllegalArgumentException("nombre de la ciudad: no debe ser nulo ni vacio");

		if (latitud == null)
			throw new IllegalArgumentException("latitud: no debe ser nulo");

		if (longitud == null)
			throw new IllegalArgumentException("longitud: no debe ser nulo");

		Restaurante restaurante = new Restaurante(nombre, cp, ciudad, latitud, longitud, u);

		String id = repositorio.add(restaurante);
		return id;
	}

	public void update(String id, String nombre, String ciudad, String cp, Double latitud, Double longitud,String u)
			throws RepositorioException, EntidadNoEncontrada {

		if (id == null || id.isEmpty())
			throw new IllegalArgumentException("id del restaurante: no debe ser nulo ni vacio");

		if (nombre == null || nombre.isEmpty())
			throw new IllegalArgumentException("nombre del  restaurante modificado: no debe ser nulo ni vacio");

		if (ciudad == null || ciudad.isEmpty())
			throw new IllegalArgumentException("ciudad del restaurante modificado: no debe ser nulo ni vacio");

		if (cp == null || cp.isEmpty())
			throw new IllegalArgumentException("cp del restaurante modificado: no debe ser nulo ni vacio");

		if (latitud == null)
			throw new IllegalArgumentException("latitud: no debe ser nulo");

		if (longitud == null)
			throw new IllegalArgumentException("longitud: no debe ser nulo");

		Restaurante r = repositorio.getById(id);
		
		if(!u.equals(r.getGestor())) {
			throw new IllegalArgumentException("No eres el gestor del restaurante");
		}

		r.setNombre(nombre);
		r.setCiudad(ciudad);
		r.setCp(cp);
		r.setLatitud(latitud);
		r.setLongitud(longitud);

		repositorio.update(r);

	}

	@Override
	public List<SitioTuristico> obtenerSitiosTuristicos(String idRes) throws MalformedURLException, SAXException,
			IOException, ParserConfigurationException, RepositorioException, EntidadNoEncontrada {

		if (idRes == null || idRes.isEmpty()) {
			throw new IllegalArgumentException("id del restaurante: no debe ser nulo ni vacio");
		}

		Restaurante r = repositorio.getById(idRes);

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
					new URL("https://es.dbpedia.org/data/" + URLEncoder.encode(sitio, "utf-8") + ".json").openStream());
			System.out.println("-----------------------------------------------------");

			JsonReader jsonReader = Json.createReader(fuente);
			JsonObject obj = jsonReader.readObject();

			SitioTuristico sitio_clase = new SitioTuristico();
			System.out.println(sitio);
			sitio_clase.setNombre(sitio);

			JsonObject infoSitio = obj.getJsonObject("http://es.dbpedia.org/resource/" + sitio);

			JsonArray categorias = infoSitio.getJsonArray("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
			JsonArray propiedadesResumen = infoSitio.getJsonArray("http://dbpedia.org/ontology/abstract");

			boolean check = false;

			System.out.println(sitio);

			System.out.println("CATEGORIAS: ");
			List<String> categorias_clase = new LinkedList<String>();
			if (categorias != null) {
				for (JsonObject categoria : categorias.getValuesAs(JsonObject.class)) {
					// categoria
					if (categoria.getString("value").equals("http://dbpedia.org/ontology/ArchitecturalStructure")
							|| categoria.getString("value").equals("http://dbpedia.org/ontology/HistoricBuilding")) {
						System.out.println(categoria.getString("value"));
						categorias_clase.add(categoria.getString("value"));
						check = true;
					}
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
	public void setSitiosTuristicos(String idRes, List<SitioTuristico> sitios, String u)
			throws RepositorioException, EntidadNoEncontrada {

		if (idRes == null || idRes.isEmpty()) {
			throw new IllegalArgumentException("id del restaurante: no debe ser nulo ni vacio");
		}
		if (sitios.isEmpty() || sitios == null ) {
			throw new IllegalArgumentException("lista de sitios turisticos: no debe ser nula ni vacia");
		}
		
		Restaurante r = repositorio.getById(idRes);
		
		
		
		if(!u.equals(r.getGestor())) {
			throw new IllegalArgumentException("No eres el gestor del restaurante");
		}
		
		r.setSitios(sitios);
		
		
		repositorio.update(r);
	}

	@Override
	public String addPlato(String idRes, String nombre, String descripcion, String precio, boolean disponibilidad,String u)
			throws RepositorioException, EntidadNoEncontrada {
		if (idRes == null || idRes.isEmpty()) {
			throw new IllegalArgumentException("id del restaurante: no debe ser nulo ni vacio");
		}

		if (nombre == null || nombre.isEmpty()) {
			throw new IllegalArgumentException("nombre del plato: no debe ser nulo ni vacio");
		}

		if (descripcion == null || descripcion.isEmpty()) {
			throw new IllegalArgumentException("descripcion del plato: no debe ser nulo ni vacio");
		}

		if (precio == null || precio.isEmpty()) {
			throw new IllegalArgumentException("precio del plato: no debe ser nulo ni vacio");
		}
		

		
		

		Double precioD = Double.parseDouble(precio);

		Plato plato = new Plato();
		plato.setNombre(nombre);
		plato.setDescripcion(descripcion);
		plato.setPrecio(precioD);
		plato.setDisponibilidad(disponibilidad);
		Restaurante r = repositorio.getById(idRes);
		
		if(!u.equals(r.getGestor())) {
			throw new IllegalArgumentException("No eres el gestor del restaurante");
		}
		
		// No añadir platos repetidos
		List<Plato> listaPlatos = r.getPlatos();

		for (Plato platoLista : listaPlatos) {
			if (platoLista.getNombre().equals(plato.getNombre())) {
				throw new IllegalArgumentException("ERROR: plato duplicado");
			}

		}
		r.add(plato);
		repositorio.update(r);
		return plato.getNombre();

	}

	@Override
	public boolean removePlato(String idRes, String nombrePlato,String u) throws RepositorioException, EntidadNoEncontrada {
		if (idRes == null || idRes.isEmpty()) {
			throw new IllegalArgumentException("id del restaurante: no debe ser nulo ni vacio");
		}
		if (nombrePlato == null || nombrePlato.isEmpty()) {
			throw new IllegalArgumentException("nombre del plato: no debe ser nulo ni vacio");
		}

		Restaurante r = repositorio.getById(idRes);
		
		if(!u.equals(r.getGestor())) {
			throw new IllegalArgumentException("No eres el gestor del restaurante");
		}

		List<Plato> listaPlatos = r.getPlatos();

		boolean existePlato = false;
		for (Plato plato : listaPlatos) {
			if (plato.getNombre().equals(nombrePlato)) {
				existePlato = true;
			}
		}

		if (!existePlato) {
			throw new IllegalArgumentException("ERROR: No existe el plato en este restaurante");
		}

		boolean borrado = r.remove(nombrePlato);
		repositorio.update(r);

		return borrado;

	}

	@Override
	public void updatePlato(String idRes, String nombre, String descripcion, String precio, boolean disponibilidad,String u)
			throws RepositorioException, EntidadNoEncontrada {
		if (idRes == null || idRes.isEmpty()) {
			throw new IllegalArgumentException("id del restaurante: no debe ser nulo ni vacio");
		}

		if (nombre == null || nombre.isEmpty()) {
			throw new IllegalArgumentException("nombre del plato: no debe ser nulo ni vacio");
		}

		if (descripcion == null || descripcion.isEmpty()) {
			throw new IllegalArgumentException("descripcion del plato: no debe ser nulo ni vacio");
		}

		if (precio == null || precio.isEmpty()) {
			throw new IllegalArgumentException("precio del plato: no debe ser nulo ni vacio");
		}

		Restaurante r = repositorio.getById(idRes);
		if(!u.equals(r.getGestor())) {
			throw new IllegalArgumentException("No eres el gestor del restaurante");
		}

		boolean borrado = r.remove(nombre);

		if (!borrado) {
			throw new IllegalArgumentException("plato: no existe en este restaurante");
		}

		Double precioD = Double.parseDouble(precio);
		Plato actualizacion = new Plato(nombre, descripcion, precioD);
		actualizacion.setDisponibilidad(disponibilidad);
		r.add(actualizacion);
		repositorio.update(r);
	}

	@Override
	public void deleteRestaurante(String idRes,String u) throws RepositorioException, EntidadNoEncontrada {
		if (idRes == null || idRes.isEmpty()) {
			throw new IllegalArgumentException("id del restaurante: no debe ser nulo ni vacio");
		}

		Restaurante r = repositorio.getById(idRes);
		
		if(!u.equals(r.getGestor())) {
			throw new IllegalArgumentException("No eres el gestor del restaurante");
		}
		repositorio.delete(r);

	}

	@Override
	public List<RestauranteResumen> getListadoRestaurantes() throws RepositorioException {

		List<Restaurante> restaurantes = repositorio.getAll();
		List<RestauranteResumen> resumenes = new ArrayList<RestauranteResumen>();

		for (Restaurante r : restaurantes) {
			RestauranteResumen rr = new RestauranteResumen();
			rr.setId(r.getId());
			rr.setNombre(r.getNombre());
			rr.setCp(r.getCp());
			resumenes.add(rr);
		}

		return resumenes;
	}

	@Override
	public Restaurante getRestaurante(String idRes) throws RepositorioException, EntidadNoEncontrada {

		if (idRes == null || idRes.isEmpty() || idRes.isBlank()) {
			throw new IllegalArgumentException("id del restaurante: no debe ser nulo ni vacio");
		}

		return repositorio.getById(idRes);
	}

}
