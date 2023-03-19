package restaurantes.modelo;

import java.util.LinkedList;
import java.util.List;

import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonRepresentation;

import com.mongodb.client.model.geojson.Point;

import repositorio.Identificable;

public class Restaurante implements Identificable{
	@BsonId
	@BsonRepresentation(BsonType.OBJECT_ID)
	private String id;

	private String nombre;
	private String cp;
	private String ciudad;
	private Point coordenadas;
	private List<SitioTuristico> sitios = new LinkedList<>();
	private List<Plato> platos = new LinkedList<>();

	public Restaurante() { //POJO
		
	}
	public Restaurante(String nombre2, String cp2, String ciudad2, Point coordenadas2) {

		this.nombre = nombre2;
		this.cp = cp2;
		this.ciudad = ciudad2;
		this.coordenadas = coordenadas2;
	}

	@Override
	public String toString() {
		return "Restaurante [id=" + id + ", nombre=" + nombre + ", cp=" + cp + ", ciudad=" + ciudad + ", coordenadas="
				+ coordenadas + ", sitios=" + sitios + ", platos=" + platos + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public List<SitioTuristico> getSitios() {
		return sitios;
	}

	public void setSitios(List<SitioTuristico> sitios) {
		this.sitios = sitios;
	}

	public List<Plato> getPlatos() {
		return platos;
	}

	public void setPlatos(List<Plato> platos) {
		this.platos = platos;
	}
	
	public void add(Plato p) {
		platos.add(p);
	}
	
	public boolean remove(String nombrePlato) {
		for(Plato p: platos) {
			if(p.getNombre().equals(nombrePlato)) {
				return platos.remove(p);
			
			}
		}
	return false;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public Point getCoordenadas() {
		return coordenadas;
	}

	public void setCoordenadas(Point coordenadas) {
		this.coordenadas = coordenadas;
	}
	
	

}
