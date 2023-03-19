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
import restaurantes.dto.PlatoRequest;
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
	
	//TODO: PREGUNTAR FORMATO DE ENTRADA EN PUTS

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
	@ApiOperation(value = "Crear un nuevo restaurante", notes = "")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "Restaurante creado con éxito"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "") })

	public Response create(
			@ApiParam(value = "datos del restaurante a crear", required = true) RestauranteRequest restaurante)
			throws Exception {

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
	@ApiOperation(value = "Actualiza un restaurante", notes = "")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "Restaurante actualizado con éxito"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "") })
	public Response update(@ApiParam(value = "id del restaurante a modificar", required = true)@PathParam("id") String id, @ApiParam(value = "datos del restaurante modificados", required = true)Restaurante restaurante) throws Exception {

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
	@ApiOperation(value = "Consulta los sitios turisticos de un restaurante", notes = "Retorna listado de sitios turisticos de un restaurantes ")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = ""),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Sitiso turisticos no encontrados") })
	public Response obtenerSitiosTuristicos(@ApiParam(value = "id del restaurante", required = true)@PathParam("id") String id) throws MalformedURLException, SAXException,
			IOException, ParserConfigurationException, RepositorioException, EntidadNoEncontrada {

		List<SitioTuristico> resultado = servicio.obtenerSitiosTuristicos(id);
		return Response.ok(resultado).build();

	}

	// 4.void setSitiosTuristicos(String id, List<SitioTuristico> sitios);
	@PUT
	@Path("/{id}/sitios")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Modifica los sitios turisticos de un restaurante", notes = " ")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = ""),
		 })
	public Response setSitiosTuristicos(@ApiParam(value = "id del restaurante a modificar", required = true)@PathParam("id") String id,@ApiParam(value = "lista de sitios turisticos", required = true) List<SitioTuristico> sitios)
			throws RepositorioException, EntidadNoEncontrada {

		servicio.setSitiosTuristicos(id, sitios);
		return Response.status(Response.Status.NO_CONTENT).build();

	}

	// 5.void addPlato(String idRes, Plato plato);
	@POST
	@Path("/{id}/platos")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Añade un nuevo plato a un restaurante", notes = " ")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = ""),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "") })
	public Response addPlato(@ApiParam(value = "id del restaurante", required = true)@PathParam("id") String id, @ApiParam(value = "datos del plato para añadir", required = true)PlatoRequest platoDTO)
			throws RepositorioException, EntidadNoEncontrada {

		try {
			String precioPlatoDTO = platoDTO.getPrecio();

			Double precio = Double.parseDouble(precioPlatoDTO);

			Plato plato = new Plato(platoDTO.getNombre(), platoDTO.getDescripcion(), precio);
			String nombre = servicio.addPlato(id, plato);

			URI nuevaURL = uriInfo.getAbsolutePathBuilder().path(nombre).build();

			return Response.created(nuevaURL).build();
		} catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("formato de precio incorrecto").build();

		}

	}

	// 6.void removePlato(String idRes, String nombrePlato);
	@DELETE
	@Path("/{id}/platos/{nombrePlato}")
	@ApiOperation(value = "Borra un plato del restaurante", notes = "")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "Plato borrado correctamente"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Sitiso turisticos no encontrados") })
	public Response removePlato(@ApiParam(value = "id del restaurante", required = true)@PathParam("id") String id, @ApiParam(value = "nombre del plato", required = true)@PathParam("nombrePlato") String nombrePlato)
			throws RepositorioException, EntidadNoEncontrada {

		servicio.removePlato(id, nombrePlato);
		return Response.status(Response.Status.NO_CONTENT).build();
	}

	// 7.void updatePlato(String idRes, Plato plato);
	@PUT
	@Path("/{id}/platos/")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Actualiza un plato", notes = "")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = ""),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "") })
	public Response updatePlato(@ApiParam(value = "id del restaurante ", required = true) @PathParam("id") String id,
			@ApiParam(value = "plato a actualizar ", required = true)Plato plato) throws RepositorioException, EntidadNoEncontrada {

		servicio.updatePlato(id, plato);

		return Response.status(Response.Status.NO_CONTENT).build();

	}

	// 8.void deleteRestaurante(String idRes);
	@DELETE
	@Path("/{id}")
	@ApiOperation(value = "Borra un restaurante", notes = "")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = ""),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "") })
	public Response deleteRestaurante(@ApiParam(value = "id del restaurante ", required = true)@PathParam("id") String id) throws RepositorioException, EntidadNoEncontrada {

		servicio.deleteRestaurante(id);
		return Response.status(Response.Status.NO_CONTENT).build();
	}

	// 9.List<RestauranteResumen> getListadoRestaurantes();
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Consulta los restaurantes", notes = "Retorna un listado de restaurantes")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = ""), //falta response
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "No se han encontrado restaurantes") })
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
