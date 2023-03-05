package arso.restaurantes.dom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue.ValueType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;

import arso.modelo.Plato;
import arso.modelo.Restaurante;
import arso.modelo.SitioTuristico;
import restaurantes.servicio.ServicioRestaurante;

public class TestMongo  {

	public static void main(String[] args) throws Exception {

		ServicioRestaurante s = new ServicioRestaurante();
	
		
		//CREAMOS RESTAURANTE
		Restaurante r = new Restaurante();
		r.setNombre("McPorfavor");
		r.setCp("30150");
		
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
	}

}
