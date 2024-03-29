package restaurantes.rest;

import java.io.IOException;
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
import restaurantes.modelo.Valoracion;
import restaurantes.rest.Listado.ResumenExtendido;
import restaurantes.rest.seguridad.AvailableRoles;
import restaurantes.rest.seguridad.Secured;
import restaurantes.servicio.IServicioRestaurante;
import restaurantes.servicio.RestauranteResumen;
import servicio.FactoriaServicios;

@Api
@Path("restaurantes")
public class RestaurantesControladorRest {

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
	// curl "http://localhost:8090/restaurantes/" -H "Content-Type:
	// application/json" -H "Authorization: Bearer
	// eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxODVkYjllMy05NDJlLTQxMmItYTdiNC0zYTY3YzBhMTlhMDUiLCJpc3MiOiJQYXNhcmVsYSBadXVsIiwiZXhwIjoxNzE1Njc4MDE1LCJzdWIiOiJtY3NtcmxsIiwidXN1YXJpbyI6InNvZmlhLm1hY2lhc21AdW0uZXMiLCJyb2wiOiJHRVNUT1IifQ.gsX5KS7e9BGhtjR9LdgvffRU1ZD2PBoNUGH_ykfHjc4"
	// -d "{\"nombre\": \"Prueba Curl\", \"cp\": \"30010\", \"ciudad\": \"Murcia\",
	// \"coordenadas\": \"30, 40\"}"

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

			double latitud = Double.parseDouble(coordenadasArray[0]); // x
			double longitud = Double.parseDouble(coordenadasArray[1]); // y

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
			URI nuevaURL = uriInfo.getAbsolutePathBuilder().host(host).port(Integer.parseInt(puerto))
					.replacePath(uriInfo.getPath().replace("/api", "")).path(id).build();
			System.out.println(nuevaURL);

			return Response.created(nuevaURL).build(); // devuelve la url del nuevo restaurante

		} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("formato de coordenadas incorrecto").build();
		}

	}

	// 2. getRestaurante
	// curl "http://localhost:8090/restaurantes/6466892c53762a5be5b422ec" -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxODVkYjllMy05NDJlLTQxMmItYTdiNC0zYTY3YzBhMTlhMDUiLCJpc3MiOiJQYXNhcmVsYSBadXVsIiwiZXhwIjoxNzE1Njc4MDE1LCJzdWIiOiJtY3NtcmxsIiwidXN1YXJpbyI6InNvZmlhLm1hY2lhc21AdW0uZXMiLCJyb2wiOiJHRVNUT1IifQ.gsX5KS7e9BGhtjR9LdgvffRU1ZD2PBoNUGH_ykfHjc4"
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

	// 3.void update(Restaurante restaurante);

	//curl -X PUT "http://localhost:8090/restaurantes/6466892c53762a5be5b422ec" -H "Content-Type: application/json" -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxODVkYjllMy05NDJlLTQxMmItYTdiNC0zYTY3YzBhMTlhMDUiLCJpc3MiOiJQYXNhcmVsYSBadXVsIiwiZXhwIjoxNzE1Njc4MDE1LCJzdWIiOiJtY3NtcmxsIiwidXN1YXJpbyI6InNvZmlhLm1hY2lhc21AdW0uZXMiLCJyb2wiOiJHRVNUT1IifQ.gsX5KS7e9BGhtjR9LdgvffRU1ZD2PBoNUGH_ykfHjc4" -d "{\"nombre\": \"Cambiado :P\",\"cp\": \"30010\",\"ciudad\": \"Alcantarilla\",\"coordenadas\": \"30, 40\"}"
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Secured(AvailableRoles.GESTOR)
	@ApiOperation(value = "Actualiza un restaurante", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "Restaurante actualizado con éxito"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Parámetros no válidos") })
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

		servicio.update(id, restaurante.getNombre(), restaurante.getCp(), restaurante.getCiudad(), latitud, longitud,
				usuario);

		return Response.status(Response.Status.NO_CONTENT).build();
	}

	// 3.List<SitioTuristico> obtenerSitiosTuristicos(String idRes)

	//curl "http://localhost:8090/restaurantes/6466892c53762a5be5b422ec/sitios" -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxODVkYjllMy05NDJlLTQxMmItYTdiNC0zYTY3YzBhMTlhMDUiLCJpc3MiOiJQYXNhcmVsYSBadXVsIiwiZXhwIjoxNzE1Njc4MDE1LCJzdWIiOiJtY3NtcmxsIiwidXN1YXJpbyI6InNvZmlhLm1hY2lhc21AdW0uZXMiLCJyb2wiOiJHRVNUT1IifQ.gsX5KS7e9BGhtjR9LdgvffRU1ZD2PBoNUGH_ykfHjc4"
	@GET
	@Path("/{id}/sitios")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Consulta los sitios turisticos cercanos a un restaurante", notes = "Retorna listado de sitios turisticos cercanos un restaurante ")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "Sitios turisticos encontrados"),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Sitios turisticos no encontrados") })
	public Response obtenerSitiosTuristicos(
			@ApiParam(value = "id del restaurante", required = true) @PathParam("id") String id)
			throws MalformedURLException, SAXException, IOException, ParserConfigurationException, RepositorioException,
			EntidadNoEncontrada {

		List<SitioTuristico> resultado = servicio.obtenerSitiosTuristicos(id);
		return Response.ok(resultado).build();

	}

	// 4.void setSitiosTuristicos(String id, List<SitioTuristico> sitios);

	//curl -X PUT "http://localhost:8090/restaurantes/6466892c53762a5be5b422ec/sitios" -H "Content-Type: application/json" -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxODVkYjllMy05NDJlLTQxMmItYTdiNC0zYTY3YzBhMTlhMDUiLCJpc3MiOiJQYXNhcmVsYSBadXVsIiwiZXhwIjoxNzE1Njc4MDE1LCJzdWIiOiJtY3NtcmxsIiwidXN1YXJpbyI6InNvZmlhLm1hY2lhc21AdW0uZXMiLCJyb2wiOiJHRVNUT1IifQ.gsX5KS7e9BGhtjR9LdgvffRU1ZD2PBoNUGH_ykfHjc4" -d "[{\"nombre\": \"Estadio El Mayayo\",\"categorias\": [\"ArchitecturalStructure\"],\"resumen\": \"El estadio de fútbol \\\"El Mayayo\\\", se encuentra en Sangonera la Verde en el municipio de Murcia en la Región de Murcia, España en el año 1975. En él se han disputado partidos de 2ªB y de Tercera división. Fue sede de extinto Sangonera Atlético el cual consiguió que en el campo se jugaran partidos de 2ªb, tras la venta del equipo local a Lorca pasó a jugar en el Campo el Costa Cálida que más tarde compraría la Universidad Católica de Murcia y volverían los partidos de 2ªb en 2012 al adquirir una plaza en esa categoría. Junto a el hay otro campo de fútbol de césped artificial y otro de tierra. En 2006 se anunció que se construiría el complejo deportivo del Real Murcia junto al estadio pero en 2012 se declinó. \n*  Datos: Q5847924\",\"enlaces\": [],\"foto\": \"\",\"distancia\": 9.1449}]"
	
	@PUT
	@Path("/{id}/sitios")
	@Consumes(MediaType.APPLICATION_JSON)
	@Secured(AvailableRoles.GESTOR)
	@ApiOperation(value = "Modifica los sitios turisticos de un restaurante", notes = " ")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "Sitios turisticos modificados con éxito"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Parámetros no válidos") })
	public Response setSitiosTuristicos(
			@ApiParam(value = "id del restaurante a modificar", required = true) @PathParam("id") String id,
			@ApiParam(value = "lista de sitios turisticos", required = true) List<SitioTuristico> sitios)
			throws RepositorioException, EntidadNoEncontrada {

		String usuario = securityContext.getUserPrincipal().getName();
		System.out.println(sitios.toString());
		servicio.setSitiosTuristicos(id, sitios, usuario);
		return Response.status(Response.Status.NO_CONTENT).build();

	}

	// 5.void addPlato(String idRes, Plato plato);

	//curl "http://localhost:8090/restaurantes/6466892c53762a5be5b422ec/platos" -H "Content-Type: application/json" -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxODVkYjllMy05NDJlLTQxMmItYTdiNC0zYTY3YzBhMTlhMDUiLCJpc3MiOiJQYXNhcmVsYSBadXVsIiwiZXhwIjoxNzE1Njc4MDE1LCJzdWIiOiJtY3NtcmxsIiwidXN1YXJpbyI6InNvZmlhLm1hY2lhc21AdW0uZXMiLCJyb2wiOiJHRVNUT1IifQ.gsX5KS7e9BGhtjR9LdgvffRU1ZD2PBoNUGH_ykfHjc4" -d "{\"nombre\": \"Papitas fritas3\",\"descripcion\": \"papitas fritas muy ricas con queso y bacon\",\"precio\": \"10.0\",\"disponibilidad\": true}"
	
	@POST
	@Path("/{id}/platos")
	@Consumes(MediaType.APPLICATION_JSON)
	@Secured(AvailableRoles.GESTOR)
	@ApiOperation(value = "Añade un nuevo plato a un restaurante", notes = " ")
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_CREATED, message = "Plato creado con éxito"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Parametros o url no valida") })
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
	
	//curl -X DELETE "http://localhost:8090/restaurantes/6466892c53762a5be5b422ec/platos/Papitas" -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxODVkYjllMy05NDJlLTQxMmItYTdiNC0zYTY3YzBhMTlhMDUiLCJpc3MiOiJQYXNhcmVsYSBadXVsIiwiZXhwIjoxNzE1Njc4MDE1LCJzdWIiOiJtY3NtcmxsIiwidXN1YXJpbyI6InNvZmlhLm1hY2lhc21AdW0uZXMiLCJyb2wiOiJHRVNUT1IifQ.gsX5KS7e9BGhtjR9LdgvffRU1ZD2PBoNUGH_ykfHjc4"
	
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
	
	//curl -X PUT "http://localhost:8090/restaurantes/6466892c53762a5be5b422ec/platos" -H "Content-Type: application/json" -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxODVkYjllMy05NDJlLTQxMmItYTdiNC0zYTY3YzBhMTlhMDUiLCJpc3MiOiJQYXNhcmVsYSBadXVsIiwiZXhwIjoxNzE1Njc4MDE1LCJzdWIiOiJtY3NtcmxsIiwidXN1YXJpbyI6InNvZmlhLm1hY2lhc21AdW0uZXMiLCJyb2wiOiJHRVNUT1IifQ.gsX5KS7e9BGhtjR9LdgvffRU1ZD2PBoNUGH_ykfHjc4" -d "{\"nombre\": \"AAA\",\"descripcion\": \"desc modificada\",\"precio\": \"10.0\",\"disponibilidad\": true}"
	
	@PUT
	@Path("/{id}/platos/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Secured(AvailableRoles.GESTOR)
	@ApiOperation(value = "Actualiza un plato", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_NO_CONTENT, message = "Plato actualizado correctamente"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Solicitud incorrecta") })
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

	//curl -X DELETE "http://localhost:8090/restaurantes/64668bb753762a5be5b422ed" -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxODVkYjllMy05NDJlLTQxMmItYTdiNC0zYTY3YzBhMTlhMDUiLCJpc3MiOiJQYXNhcmVsYSBadXVsIiwiZXhwIjoxNzE1Njc4MDE1LCJzdWIiOiJtY3NtcmxsIiwidXN1YXJpbyI6InNvZmlhLm1hY2lhc21AdW0uZXMiLCJyb2wiOiJHRVNUT1IifQ.gsX5KS7e9BGhtjR9LdgvffRU1ZD2PBoNUGH_ykfHjc4"

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
		servicio.deleteRestaurante(id, usuario);
		return Response.status(Response.Status.NO_CONTENT).build();
	}

	// 9.List<RestauranteResumen> getListadoRestaurantes();

	//curl "http://localhost:8090/restaurantes/" -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxODVkYjllMy05NDJlLTQxMmItYTdiNC0zYTY3YzBhMTlhMDUiLCJpc3MiOiJQYXNhcmVsYSBadXVsIiwiZXhwIjoxNzE1Njc4MDE1LCJzdWIiOiJtY3NtcmxsIiwidXN1YXJpbyI6InNvZmlhLm1hY2lhc21AdW0uZXMiLCJyb2wiOiJHRVNUT1IifQ.gsX5KS7e9BGhtjR9LdgvffRU1ZD2PBoNUGH_ykfHjc4"
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Consulta los restaurantes", notes = "Retorna un listado de restaurantes", response = Restaurante.class)
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = ""),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "No se han encontrado restaurantes") })
	public Response getListadoActividades() throws Exception {

		Principal user = securityContext.getUserPrincipal();
		List<RestauranteResumen> resultado = servicio.getListadoRestaurantes();

		List<ResumenExtendido> extendido = new LinkedList<Listado.ResumenExtendido>();

		for (RestauranteResumen restauranteResumen : resultado) {

			ResumenExtendido resumenExtendido = new ResumenExtendido();

			resumenExtendido.setResumen(restauranteResumen);

			// URL

			String id = restauranteResumen.getId();

			String hostypuerto = headers.getRequestHeader("X-Forwarded-Host").get(0).toString();

			String[] partes = hostypuerto.split(":");
			String host = partes[0];
			String puerto = partes[1];

			// APLICACIÓN PATRON BUILDER
			// construimos la nueva Url según los parametros (host y puerto) que hemos
			// sacado de la cabecera X-Forwarded-Host, además, quitamos de la ruta /api para
			// que sea conforme con la pasarela
			URI nuevaURL = uriInfo.getAbsolutePathBuilder().host(host).port(Integer.parseInt(puerto))
					.replacePath(uriInfo.getPath().replace("/api", "")).path(id).build();

			resumenExtendido.setUrl(nuevaURL.toString()); // string

			extendido.add(resumenExtendido);

		}

		// Una lista no es un documento válido en XML

		// Creamos un documento con un envoltorio

		Listado listado = new Listado();

		listado.setRestaurante(extendido);

		return Response.ok(listado).build();

	}

	//11. activarValoraciones

	//curl -X POST "http://localhost:8090/restaurantes/6466892c53762a5be5b422ec/valoraciones" -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxODVkYjllMy05NDJlLTQxMmItYTdiNC0zYTY3YzBhMTlhMDUiLCJpc3MiOiJQYXNhcmVsYSBadXVsIiwiZXhwIjoxNzE1Njc4MDE1LCJzdWIiOiJtY3NtcmxsIiwidXN1YXJpbyI6InNvZmlhLm1hY2lhc21AdW0uZXMiLCJyb2wiOiJHRVNUT1IifQ.gsX5KS7e9BGhtjR9LdgvffRU1ZD2PBoNUGH_ykfHjc4"
	
	@POST
	@Path("/{id}/valoraciones")
	@Secured(AvailableRoles.GESTOR)
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Activar las valoraciones", notes = "Activa las opiniones de un restaurante", response = Restaurante.class)
	@ApiResponses(value = {
			@ApiResponse(code = HttpServletResponse.SC_CREATED, message = "Valoraciones creadas correctamente"),
			@ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "id del restaurante no válido o valoraciones ya activadas") })
	public Response activarValoraciones(
			@ApiParam(value = "id del restaurante ", required = true) @PathParam("id") String id) throws Exception {

		String usuario = securityContext.getUserPrincipal().getName();
		servicio.activarValoraciones(id,usuario);
		return Response.status(Response.Status.CREATED).build();

	}

	//12. getValoraciones
	
	//curl "http://localhost:8090/restaurantes/6466892c53762a5be5b422ec/valoraciones" -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxODVkYjllMy05NDJlLTQxMmItYTdiNC0zYTY3YzBhMTlhMDUiLCJpc3MiOiJQYXNhcmVsYSBadXVsIiwiZXhwIjoxNzE1Njc4MDE1LCJzdWIiOiJtY3NtcmxsIiwidXN1YXJpbyI6InNvZmlhLm1hY2lhc21AdW0uZXMiLCJyb2wiOiJHRVNUT1IifQ.gsX5KS7e9BGhtjR9LdgvffRU1ZD2PBoNUGH_ykfHjc4"
	
	@GET
	@Path("/{id}/valoraciones")
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Obtener las valoraciones", notes = "Obtener las valoraciones de un restaurante", response = Restaurante.class)
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = ""),
			@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "No se han encontrado restaurantes") })
	public Response getValoraciones(
			@ApiParam(value = "id del restaurante ", required = true) @PathParam("id") String id) throws Exception {

		List<Valoracion> lista_valoraciones = servicio.getValoracionesRes(id);
		System.out.println(lista_valoraciones.toString());
		return Response.ok(lista_valoraciones).build();

	}

}
