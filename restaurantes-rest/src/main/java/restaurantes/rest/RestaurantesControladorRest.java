package restaurantes.rest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import restaurantes.dto.RestauranteRequest;
import restaurantes.modelo.Plato;
import restaurantes.modelo.Restaurante;
import restaurantes.modelo.SitioTuristico;
import restaurantes.rest.Listado.ResumenExtendido;
import restaurantes.servicio.IServicioRestaurante;
import restaurantes.servicio.RestauranteResumen;
import servicio.FactoriaServicios;

@Api
@Path("restaurantes")
public class RestaurantesControladorRest {

	private IServicioRestaurante servicio = FactoriaServicios.getServicio(IServicioRestaurante.class);

	@Context
	private UriInfo uriInfo;

	// 1.String create(Restaurante restaurante);
	/*
	curl --location 'http://127.0.0.1:8080/api/restaurantes' --header 'content-type: application/json' --data '{
	    "nombre":"Prueba",
	    "cp": "30161",
	    "ciudad":"Murcia",
	    "coordenadas":"30, 30"
	}'
	
	*/
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Crear un nuevo restaurante", notes = "Crea un nuevo restaurante en la base de datos con los datos proporcionados en el cuerpo de la solicitud.")
	@ApiResponses(value = { 
			@ApiResponse(code = HttpServletResponse.SC_OK, message = "Restaurante creado con éxito"),
			@ApiResponse(code = 400, message = "Formato de coordenadas incorrecto"),
			@ApiResponse(code = 500, message = "Error interno del servidor") })

	public Response create(@ApiParam(value = "Datos del restaurante a crear", required = true) RestauranteRequest restaurante) throws Exception {

		// APLICACION DEL PATRON DTO
		try {
			// parseamos las coordenadsa de String a Point
			String coordenadasStr = restaurante.getCoordenadas();
			String[] coordenadasArray = coordenadasStr.split(", ");
			double x = Double.parseDouble(coordenadasArray[0]);
			double y = Double.parseDouble(coordenadasArray[1]);
			Position posicion = new Position(x, y);
			Point coordenadas = new Point(posicion);

			// APLICACIÓN PATRON BUILDER
			String id = servicio.create(restaurante.getNombre(), restaurante.getCp(), restaurante.getCiudad(),
					coordenadas);

			URI nuevaURL = uriInfo.getAbsolutePathBuilder().path(id).build();

			return Response.created(nuevaURL).build();

		} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("formato de coordenadas incorrecto").build();
		}

	}

	// 2.void update(Restaurante restaurante);

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") String id, Restaurante restaurante) throws Exception {
		if (!id.equals(restaurante.getId()))
			throw new IllegalArgumentException("El identificador no coincide: " + id);

		servicio.update(restaurante);
		return Response.status(Response.Status.NO_CONTENT).build();

	}

	// 3.List<SitioTuristico> obtenerSitiosTuristicos(String idRes) throws
	// MalformedURLException, SAXException, IOException,
	// ParserConfigurationException;
	@GET
	@Path("/{id}/sitios")
	@Produces(MediaType.APPLICATION_JSON)
	public Response obtenerSitiosTuristicos(@PathParam("id") String id) throws MalformedURLException, SAXException,
			IOException, ParserConfigurationException, RepositorioException, EntidadNoEncontrada {

		List<SitioTuristico> resultado = servicio.obtenerSitiosTuristicos(id);
		return Response.ok(resultado).build();

	}

	// 4.void setSitiosTuristicos(String id, List<SitioTuristico> sitios);
	@PUT
	@Path("/{id}/sitios")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response setSitiosTuristicos(@PathParam("id") String id, List<SitioTuristico> sitios)
			throws RepositorioException, EntidadNoEncontrada {

		servicio.setSitiosTuristicos(id, sitios);
		return Response.status(Response.Status.NO_CONTENT).build();

	}

	// 5.void addPlato(String idRes, Plato plato);
	@POST
	@Path("/{id}/platos")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addPlato(@PathParam("id") String id, Plato plato) throws RepositorioException, EntidadNoEncontrada {

		String nombre = servicio.addPlato(id, plato);
		URI nuevaURL = uriInfo.getAbsolutePathBuilder().path(nombre).build();

		return Response.created(nuevaURL).build();
	}

	// 6.void removePlato(String idRes, String nombrePlato);
	@DELETE
	@Path("/{id}/platos/{nombrePlato}")
	public Response removePlato(@PathParam("id") String id, @PathParam("nombrePlato") String nombrePlato)
			throws RepositorioException, EntidadNoEncontrada {

		servicio.removePlato(id, nombrePlato);
		return Response.status(Response.Status.NO_CONTENT).build();
	}

	// 7.void updatePlato(String idRes, Plato plato);
	@PUT
	@Path("/{id}/platos/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updatePlato(@PathParam("id") String id, Plato plato)
			throws RepositorioException, EntidadNoEncontrada {

		servicio.updatePlato(id, plato);
		return Response.status(Response.Status.NO_CONTENT).build();

	}

	// 8.void deleteRestaurante(String idRes);
	@DELETE
	@Path("/{id}")
	public Response deleteRestaurante(@PathParam("id") String id) throws RepositorioException, EntidadNoEncontrada {

		servicio.deleteRestaurante(id);
		return Response.status(Response.Status.NO_CONTENT).build();
	}

	// 9.List<RestauranteResumen> getListadoRestaurantes();
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getListadoActividades() throws Exception {

		List<RestauranteResumen> resultado = servicio.getListadoRestaurantes();

		LinkedList<ResumenExtendido> extendido = new LinkedList<Listado.ResumenExtendido>();

		for (RestauranteResumen restauranteResumen : resultado) {

			ResumenExtendido resumenExtendido = new ResumenExtendido();

			resumenExtendido.setResumen(restauranteResumen);

			// URL

			String id = restauranteResumen.getId();
			URI nuevaURL = uriInfo.getAbsolutePathBuilder().path(id).build();

			resumenExtendido.setUrl(nuevaURL.toString()); // string

			extendido.add(resumenExtendido);

		}

		// Una lista no es un documento válido en XML

		// Creamos un documento con un envoltorio

		Listado listado = new Listado();

		listado.setRestaurante(extendido);

		return Response.ok(listado).build();

	}
}
