package arso.restaurantes.dom;

import java.util.List;

import repositorio.FactoriaRepositorios;
import restaurantes.modelo.Restaurante;
import restaurantes.repositorio.RepositorioRestaurantesMongo;
import restaurantes.servicio.IServicioRestaurante;
import restaurantes.servicio.RestauranteResumen;
import restaurantes.servicio.ServicioRestaurante;
import servicio.FactoriaServicios;

public class TestMongo  {

	public static void main(String[] args) throws Exception {
		
		

		IServicioRestaurante servicio=FactoriaServicios.getServicio(IServicioRestaurante.class);
		
		List<RestauranteResumen> r= servicio.getListadoRestaurantes();
		
		for(RestauranteResumen rr: r) {
			System.out.println(rr.getNombre());
		}
		
		/*Restaurante r = new Restaurante();
		r.setNombre("eo");
		r.setCp("30150");

		/*
		ServicioRestaurante s = new ServicioRestaurante();
	
		
		//CREAMOS RESTAURANTE
		
		
		Restaurante r2 = new Restaurante();
		r2.setNombre("Burger Queen");
		r2.setCp("30161");
		
		
		Plato p = new Plato();
		p.setNombre("patatas");
		p.setDescripcion("aaaaaaaaaaaaaaa");
		p.setPrecio(10.0);
		
		String id = s.create(r);
		s.create(r2);
		s.addPlato(id, p);
		System.out.println(id);
		
		Plato p2 = new Plato();
		p2.setNombre("patatas");
		p2.setPrecio(20.00);
		p2.setDescripcion("Plato modificado");
		
		List<SitioTuristico> s1 = s.obtenerSitiosTuristicos(id);
		s.setSitiosTuristicos(id, s1);
		//s.removePlato(id, "patatas");
		s.updatePlato(id, p2);
		
		System.out.println(s.getListadoRestaurantes());
		*/
		
		//IServicioRestaurante servicio=FactoriaServicios.getServicio(IServicioRestaurante.class);		
	
		
		
		
		
		
	}

}
