package test;

import restaurantes.modelo.Restaurante;
import restaurantes.servicio.IServicioRestaurante;
import servicio.FactoriaServicios;

public class TestMongo {
	public static void main(String[] args) throws Exception {

		IServicioRestaurante servicio=FactoriaServicios.getServicio(IServicioRestaurante.class);
		System.out.println("eo");
	
		Restaurante r = new Restaurante();
		r.setNombre("eo");
		r.setCp("30150");
		servicio.create(r);
		
}
}
