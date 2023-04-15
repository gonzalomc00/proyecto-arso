package restaurantes.rest;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.MalformedURLException;
import java.net.URI;
import java.security.Principal;
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
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import repositorio.EntidadNoEncontrada;
import repositorio.RepositorioException;
import restaurantes.dto.PlatoRequest;
import restaurantes.dto.RestauranteRequest;
import restaurantes.modelo.Restaurante;
import restaurantes.modelo.SitioTuristico;
import restaurantes.rest.Listado.ResumenExtendido;
import restaurantes.rest.seguridad.AvailableRoles;
import restaurantes.rest.seguridad.Secured;
import restaurantes.servicio.IServicioRestaurante;
import restaurantes.servicio.RestauranteResumen;
import servicio.FactoriaServicios;

@Api
@Path("restaurantes")
public class RestaurantesControladorRest {

	// TODO: PREGUNTAR FORMATO DE ENTRADA EN PUTS

	private IServicioRestaurante servicio = FactoriaServicios.getServicio(IServicioRestaurante.class);

	@Context
	private UriInfo uriInfo;
	// La clase HttpHeaders representa las cabeceras HTTP de la solicitud y
	// proporciona métodos para obtener los valores de las cabeceras individuales.

	@Context
	private HttpHeaders headers;

	@Context
	private SecurityContext securityContext;

	// 1.String create(Restaurante restaurante);
	/*
	 * curl --location 'http://127.0.0.1:8080/api/restaurantes' --header
	 * 'content-type: application/json' --data '{ "nombre":"Prueba", "cp": "30161",
	 * "ciudad":"Murcia", "coordenadas":"30, 30" }'
	 */

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Secured(AvailableRoles.GESTOR)
	@ApiOperation(value = "Crear un nuevo restaurante", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_CREATED, message = "El restaurante se ha creado correctamente"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Datos del restaurante invalidos") })
	public Response create(
			@ApiParam(value = "Datos del restaurante a crear", required = true) RestauranteRequest restaurante)
			throws Exception {

		// APLICACION DEL PATRON DTO
		try {

			// Control de integridad de los datos

			if (restaurante.getNombre() == null || restaurante.getNombre().isEmpty()) {
				throw new IllegalArgumentException("nombre del restaurante: no debe ser nulo ni vacio");

			}
			if (restaurante.getCp() == null || restaurante.getCp().isEmpty())
				throw new IllegalArgumentException("codigo postal: no debe ser nulo ni vacio");

			if (restaurante.getCiudad() == null || restaurante.getCiudad().isEmpty())
				throw new IllegalArgumentException("ciudad: no debe ser nulo ni vacio");

			if (restaurante.getCoordenadas() == null)
				throw new IllegalArgumentException("coordenadas: no debe ser nulo");
			// parseamos las coordenadsa de String a Point

			String coordenadasStr = restaurante.getCoordenadas();
			String[] coordenadasArray = coordenadasStr.split(", ");

			double longitud = Double.parseDouble(coordenadasArray[0]); //x
			double  latitud = Double.parseDouble(coordenadasArray[1]); //y

			String usuario = securityContext.getUserPrincipal().getName();
			System.out.println(usuario);

			// se asigna al restaurante el usuario que lo ha creado como gestor
			String id = servicio.create(restaurante.getNombre(), restaurante.getCp(), restaurante.getCiudad(), latitud,
					longitud, usuario);
			// Obtener el valor de la cabecera "Host"

			String hostypuerto = headers.getRequestHeader("X-Forwarded-Host").get(0).toString();

			String[] partes = hostypuerto.split(":");
			String host = partes[0];
			String puerto = partes[1];

			// APLICACIÓN PATRON BUILDER
			System.out.println(hostypuerto);
			// construimos la nueva Url según los parametros (host y puerto) que hemos
			// sacado de la cabecera X-Forwarded-Host, además, quitamos de la ruta /api para
			// que sea conforme con la pasarela
			URI nuevaURL = uriInfo.getAbsolutePathBuilder()
					.host(host)
					.port(Integer.parseInt(puerto))
					.replacePath(uriInfo.getPath().replace("/api", ""))
					.path(id).build();
			System.out.println(nuevaURL);

			return Response.created(nuevaURL).build(); // devuelve la url del nuevo restaurante

		} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("formato de coordenadas incorrecto").build();
		}

	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Consulta un restaurante", notes = "Retorna un Restaurante utilizando su id", response = Restaurante.class)
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_OK, message = "Restaurante devuelto correctamente"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Restaurante no encontrado") })
	public Response getRestaurante(@ApiParam(value = "id del restaurante", required = true) @PathParam("id") String id)
			throws Exception {

		return Response.status(Response.Status.OK).entity(servicio.getRestaurante(id)).build();
	}

	// curl -X GET http://localhost:8080/api/restaurantes/642ac93103896f0ae3f3b42e
	// -H "Content-type: application/json"

	// 3.void update(Restaurante restaurante);

	/*
	 * curl --location --request PUT
	 * 'http://127.0.0.1:8080/api/restaurantes/641754dabbef43047199d631' \ --header
	 * 'Content-Type: application/json' \ --data '{ "nombre": "Actualizado",
	 * "ciudad": "Albacete", "cp": "29183", "coordenadas": "20, 30" }'
	 * 
	 * 
	 */

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Secured(AvailableRoles.GESTOR)
	@ApiOperation(value = "Actualiza un restaurante", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "Restaurante actualizado con éxito"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "") })
	public Response update(
			@ApiParam(value = "id del restaurante a modificar", required = true) @PathParam("id") String id,
			@ApiParam(value = "datos del restaurante modificados", required = true) RestauranteRequest restaurante)
			throws Exception {

		if (restaurante.getNombre() == null || restaurante.getNombre().isEmpty()) {
			throw new IllegalArgumentException("nombre del restaurante: no debe ser nulo ni vacio");

		}
		if (restaurante.getCp() == null || restaurante.getCp().isEmpty())
			throw new IllegalArgumentException("codigo postal: no debe ser nulo ni vacio");

		if (restaurante.getCiudad() == null || restaurante.getCiudad().isEmpty())
			throw new IllegalArgumentException("ciudad: no debe ser nulo ni vacio");

		if (restaurante.getCoordenadas() == null)
			throw new IllegalArgumentException("coordenadas: no debe ser nulo");

		String usuario = securityContext.getUserPrincipal().getName();
		System.out.println(usuario);

		String coordenadasStr = restaurante.getCoordenadas();
		String[] coordenadasArray = coordenadasStr.split(", ");

		double latitud = Double.parseDouble(coordenadasArray[0]);
		double longitud = Double.parseDouble(coordenadasArray[1]);

		servicio.update(id, restaurante.getNombre(), restaurante.getCiudad(), restaurante.getCp(), latitud, longitud,
				usuario);

		return Response.status(Response.Status.NO_CONTENT).build();
	}

	// 3.List<SitioTuristico> obtenerSitiosTuristicos(String idRes)

	/*
	 * curl -X GET
	 * http://localhost:8080/api/restaurantes/642ac93103896f0ae3f3b42e/sitios -H
	 * "accept: application/json"
	 */
	@GET
	@Path("/{id}/sitios")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Consulta los sitios turisticos cercanos a un restaurante", notes = "Retorna listado de sitios turisticos cercanos un restaurante ")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = ""),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Sitios turisticos no encontrados") })
	public Response obtenerSitiosTuristicos(
			@ApiParam(value = "id del restaurante", required = true) @PathParam("id") String id)
			throws MalformedURLException, SAXException, IOException, ParserConfigurationException, RepositorioException,
			EntidadNoEncontrada {

		List<SitioTuristico> resultado = servicio.obtenerSitiosTuristicos(id);
		return Response.ok(resultado).build();

	}

	// 4.void setSitiosTuristicos(String id, List<SitioTuristico> sitios);

	/**
	 * curl -X 'PUT' \
	 * 'http://localhost:8080/api/restaurantes/642ac93103896f0ae3f3b42e/sitios' \ -H
	 * 'accept: application/json' \ -H 'Content-Type: application/json' \ -d '[ {
	 * "nombre": "string", "categorias": [ "string" ], "resumen": "string",
	 * "enlaces": [ "string" ], "foto": "string" } ]'
	 */
	@PUT
	@Path("/{id}/sitios")
	@Consumes(MediaType.APPLICATION_JSON)
	@Secured(AvailableRoles.GESTOR)
	@ApiOperation(value = "Modifica los sitios turisticos de un restaurante", notes = " ")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "Sitios turisticos modificados con éxito"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "") })
	public Response setSitiosTuristicos(
			@ApiParam(value = "id del restaurante a modificar", required = true) @PathParam("id") String id,
			@ApiParam(value = "lista de sitios turisticos", required = true) List<SitioTuristico> sitios)
			throws RepositorioException, EntidadNoEncontrada {

		String usuario = securityContext.getUserPrincipal().getName();
		System.out.println(usuario);

		servicio.setSitiosTuristicos(id, sitios, usuario);
		return Response.status(Response.Status.NO_CONTENT).build();

	}

	// 5.void addPlato(String idRes, Plato plato);
	/*
	 * curl --location
	 * 'http://127.0.0.1:8080/api/restaurantes/6417560abbef43047199d632/platos' \
	 * --header 'content-type: application/json' \ --data '{ "nombre":
	 * "Plato de prueba", "descripcion": "Descripcion del plato", "precio": "10"
	 * 
	 * }'
	 */
	@POST
	@Path("/{id}/platos")
	@Consumes(MediaType.APPLICATION_JSON)
	@Secured(AvailableRoles.GESTOR)
	@ApiOperation(value = "Añade un nuevo plato a un restaurante", notes = " ")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_CREATED, message = "Plato creado con éxito"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "") })
	public Response addPlato(@ApiParam(value = "id del restaurante", required = true) @PathParam("id") String id,
			@ApiParam(value = "datos del plato para añadir", required = true) PlatoRequest platoDTO)
			throws RepositorioException, EntidadNoEncontrada {

		try {

			String usuario = securityContext.getUserPrincipal().getName();
			System.out.println(usuario);

			String nombre = servicio.addPlato(id, platoDTO.getNombre(), platoDTO.getDescripcion(), platoDTO.getPrecio(),
					platoDTO.isDisponibilidad(), usuario);
			URI nuevaURL = uriInfo.getAbsolutePathBuilder().path(nombre).build();

			return Response.created(nuevaURL).build();
		} catch (NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("formato de precio incorrecto").build();

		}

	}

	// 6.void removePlato(String idRes, String nombrePlato);
	/*
	 * curl --location --request DELETE
	 * 'http://127.0.0.1:8080/api/restaurantes/6417560abbef43047199d632/platos/Plato
	 * %20de%20prueba' \ --header 'content-type: application/json' \ --data '{
	 * "nombre": "Plato de prueba", "descripcion": "Descripcion del plato",
	 * "precio": "10"
	 * 
	 * }'
	 */
	@DELETE
	@Path("/{id}/platos/{nombrePlato}")
	@Secured(AvailableRoles.GESTOR)
	@ApiOperation(value = "Borra un plato del restaurante", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "Plato borrado correctamente"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Plato no encontrado") })
	public Response removePlato(@ApiParam(value = "id del restaurante", required = true) @PathParam("id") String id,
			@ApiParam(value = "nombre del plato", required = true) @PathParam("nombrePlato") String nombrePlato)
			throws RepositorioException, EntidadNoEncontrada {

		String usuario = securityContext.getUserPrincipal().getName();
		System.out.println(usuario);
		servicio.removePlato(id, nombrePlato, usuario);
		return Response.status(Response.Status.NO_CONTENT).build();
	}

	// 7.void updatePlato(String idRes, Plato plato);
	/*
	 * curl --location --request PUT
	 * 'http://127.0.0.1:8080/api/restaurantes/6417560abbef43047199d632/platos' \
	 * --header 'content-type: application/json' \ --data '{ "nombre":
	 * "Plato de prueba", "descripcion": "Descripcion del plato modificada",
	 * "precio": "10"
	 * 
	 * }'
	 */
	@PUT
	@Path("/{id}/platos/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Secured(AvailableRoles.GESTOR)
	@ApiOperation(value = "Actualiza un plato", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "Plato actualizado correctamente"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "") })
	public Response updatePlato(@ApiParam(value = "id del restaurante ", required = true) @PathParam("id") String id,
			@ApiParam(value = "plato a actualizar ", required = true) PlatoRequest plato)
			throws RepositorioException, EntidadNoEncontrada {

		String usuario = securityContext.getUserPrincipal().getName();
		System.out.println(usuario);
		servicio.updatePlato(id, plato.getNombre(), plato.getDescripcion(), plato.getPrecio(), plato.isDisponibilidad(),
				usuario);

		return Response.status(Response.Status.NO_CONTENT).build();

	}

	// 8.void deleteRestaurante(String idRes);
	/*
	 * curl --location --request DELETE
	 * 'http://127.0.0.1:8080/api/restaurantes/6417560abbef43047199d632/' \ --header
	 * 'content-type: application/json' \ --data ''
	 */
	@DELETE
	@Path("/{id}")
	@Secured(AvailableRoles.GESTOR)
	@ApiOperation(value = "Borra un restaurante", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "Restaurante borrado correctamente"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Restaurante no encontrado") })
	public Response deleteRestaurante(
			@ApiParam(value = "id del restaurante ", required = true) @PathParam("id") String id)
			throws RepositorioException, EntidadNoEncontrada {

		String usuario = securityContext.getUserPrincipal().getName();
		System.out.println(usuario);
		servicio.deleteRestaurante(id, usuario);
		return Response.status(Response.Status.NO_CONTENT).build();
	}

	// 9.List<RestauranteResumen> getListadoRestaurantes();
	/*
	 * curl --location 'http://127.0.0.1:8080/api/restaurantes/' \ --header
	 * 'content-type: application/json' \ --data ''
	 */

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Consulta los restaurantes", notes = "Retorna un listado de restaurantes", response = Restaurante.class)
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = ""),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "No se han encontrado restaurantes") })
	public Response getListadoActividades() throws Exception {

		Principal user = securityContext.getUserPrincipal();
		System.out.println("USUARIIOO: " + user);
		List<RestauranteResumen> resultado = servicio.getListadoRestaurantes();

		List<ResumenExtendido> extendido = new LinkedList<Listado.ResumenExtendido>();

		for (RestauranteResumen restauranteResumen : resultado) {

			ResumenExtendido resumenExtendido = new ResumenExtendido();

			resumenExtendido.setResumen(restauranteResumen);

			// URL
			
			System.out.println(headers.getRequestHeaders().get(HttpHeaders.HOST));

			String id = restauranteResumen.getId();
			
			String hostypuerto = headers.getRequestHeader("X-Forwarded-Host").get(0).toString();

			String[] partes = hostypuerto.split(":");
			String host = partes[0];
			String puerto = partes[1];

			// APLICACIÓN PATRON BUILDER
			System.out.println(hostypuerto);
			// construimos la nueva Url según los parametros (host y puerto) que hemos
			// sacado de la cabecera X-Forwarded-Host, además, quitamos de la ruta /api para
			// que sea conforme con la pasarela
			URI nuevaURL = uriInfo.getAbsolutePathBuilder()
					.host(host)
					.port(Integer.parseInt(puerto))
					.replacePath(uriInfo.getPath().replace("/api", ""))
					.path(id).build();
						
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
