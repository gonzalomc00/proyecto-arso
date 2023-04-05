package restaurantes.servicio;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.mongodb.client.model.geojson.Point;

import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import restaurantes.modelo.Plato;
import restaurantes.modelo.Restaurante;
import restaurantes.modelo.SitioTuristico;

public interface IServicioRestaurante {

	String create(String nombre, String cp, String ciudad,Double latitud, Double longitud) throws RepositorioException;

	Restaurante getRestaurante(String idRes) throws RepositorioException, EntidadNoEncontrada;
	
	void update(String id, String nombre, String ciudad, String cp, Double latitud, Double longitud) throws RepositorioException, EntidadNoEncontrada;
	
	List<SitioTuristico> obtenerSitiosTuristicos(String idRes) throws MalformedURLException, SAXException, IOException, ParserConfigurationException, RepositorioException, EntidadNoEncontrada;
	
	void setSitiosTuristicos(String id, List<SitioTuristico> sitios) throws RepositorioException, EntidadNoEncontrada;
	
	String addPlato(String idRes, String nombre, String descripcion, String precio,boolean disponibilidad) throws RepositorioException, EntidadNoEncontrada;

	boolean removePlato(String idRes, String nombrePlato) throws RepositorioException, EntidadNoEncontrada;
	
	void updatePlato(String idRes, String nombre,String descripcion,String precio,boolean disponibilidad) throws RepositorioException, EntidadNoEncontrada;
	
	void deleteRestaurante(String idRes) throws RepositorioException, EntidadNoEncontrada;
	
	List<RestauranteResumen> getListadoRestaurantes() throws RepositorioException;
	
}
