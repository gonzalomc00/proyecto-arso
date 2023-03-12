package restaurantes.servicio;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import restaurantes.modelo.Plato;
import restaurantes.modelo.Restaurante;
import restaurantes.modelo.SitioTuristico;

public interface IServicioRestaurante {
	/**
	 * Método para dar de alta un restaurante
	 * 
	 * @param restaurante debe ser válido respecto al modelo del dominio (no incluye
	 *                    platos ni sitios turísticos)
	 * @return identificador del nuevo restaurante
	 */
	String create(Restaurante restaurante);
	// Actualización de un restaurante. Parámetros: identificador del restaurante y
	// la información básica actualizada.

	/**
	 * Actualiza un restaurante
	 * 
	 * @param restaurante
	 */
	void update(Restaurante restaurante);

	// Obtener sitios turísticos próximos. Esta operación tiene como parámetro el
	// identificador de un restaurante.
	// Retorna una lista de sitios turísticos. Nota: la implementación utilizará la
	// información obtenida de GeoNames y DBpedia (tareas 1 y 2).
	List<SitioTuristico> obtenerSitiosTuristicos(String idRes) throws MalformedURLException, SAXException, IOException, ParserConfigurationException;

	// Establecer sitios turísticos destacados. Parámetros: identificador del
	// restaurante y una lista de sitios turísticos. Esta operación establece como
	// sitios turísticos del restaurante los que se establecen como parámetro.
	// Nota: observa que la aplicación ofrecerá al gestor del restaurante una lista
	// de sitios turísticos próximos y el gestor seleccionará algunos como
	// destacados para su restaurante.
	void setSitiosTuristicos(String id, List<SitioTuristico> sitios);

	/**
	 * Añadir un plato
	 * 
	 * @param id    Identificador del restaurante
	 * @param plato
	 */
	// El nombre del plato debe ser único para cada restaurante
	void addPlato(String idRes, Plato plato);

	/**
	 * Borra un plato del restaurante
	 * 
	 * @param idRes       identificador del restaurante
	 * @param nombrePlato nombre único para cada plato del restaurante
	 */
	void removePlato(String idRes, String nombrePlato);


	/**
	 * Actualizar un plato del restaurante
	 * @param idRes
	 * @param plato el nombre del plato debe formar parte del restaurante
	 */
	void updatePlato(String idRes, Plato plato);
	
	/**
	 * Borrar un restaurante
	 * @param idRes identificador del restaurante
	 */
	void deleteRestaurante(String idRes);
	

	/**
	 * Listado de restaurantes
	 * @return lista con un resumen de todos los restaurantes
	 */
	List<RestauranteResumen> getListadoRestaurantes();
}
