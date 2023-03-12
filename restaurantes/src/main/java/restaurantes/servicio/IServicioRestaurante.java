package restaurantes.servicio;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import restaurantes.modelo.Plato;
import restaurantes.modelo.Restaurante;
import restaurantes.modelo.SitioTuristico;

public interface IServicioRestaurante {

	String create(Restaurante entity) throws RepositorioException;

	void update(Restaurante entity) throws RepositorioException, EntidadNoEncontrada;
	
	List<SitioTuristico> obtenerSitiosTuristicos(String idRes) throws MalformedURLException, SAXException, IOException, ParserConfigurationException, RepositorioException, EntidadNoEncontrada;

	
	void setSitiosTuristicos(String id, List<SitioTuristico> sitios) throws RepositorioException, EntidadNoEncontrada;

	
	String addPlato(String idRes, Plato plato) throws RepositorioException, EntidadNoEncontrada;

	void removePlato(String idRes, String nombrePlato) throws RepositorioException, EntidadNoEncontrada;


	void updatePlato(String idRes, Plato plato) throws RepositorioException, EntidadNoEncontrada;
	

	void deleteRestaurante(String idRes) throws RepositorioException, EntidadNoEncontrada;
	

	List<RestauranteResumen> getListadoRestaurantes() throws RepositorioException;
}
