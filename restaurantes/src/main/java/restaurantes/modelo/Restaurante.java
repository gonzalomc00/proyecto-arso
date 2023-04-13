package restaurantes.modelo;

import java.util.LinkedList;
import java.util.List;

import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonRepresentation;

import com.mongodb.client.model.geojson.Point;

import repositorio.Identificable;
import usuarios.modelo.Usuario;

public class Restaurante implements Identificable{
	@BsonId
	@BsonRepresentation(BsonType.OBJECT_ID)
	private String id;

	private String nombre;
	private String cp;
	private String ciudad;
	private Double latitud;
	private Double longitud;
	private List<SitioTuristico> sitios = new LinkedList<>();
	private List<Plato> platos = new LinkedList<>();
	private String gestor;
	
	public Restaurante() { //POJO
		
	}
	
	public Restaurante(String nombre2, String cp2, String ciudad2, Double latitud, Double longitud, String u) {
		
		if (nombre2 == null || nombre2.isEmpty())
			throw new IllegalArgumentException("nombre del restaurante: no debe ser nulo ni vacio");
		
		if (cp2 == null || cp2.isEmpty())
			throw new IllegalArgumentException("codigo postal: no debe ser nulo ni vacio");
		
		if (ciudad2 == null || ciudad2.isEmpty())
			throw new IllegalArgumentException("nombre de la ciudad: no debe ser nulo ni vacio");
		
		if (latitud == null)
			throw new IllegalArgumentException("latitud: no debe ser nulo");
		
		if (longitud == null)
			throw new IllegalArgumentException("longitud: no debe ser nulo");
		
		if (u == null)
			throw new IllegalArgumentException("usuario: no debe ser nulo");

		this.nombre = nombre2;
		this.cp = cp2;
		this.ciudad = ciudad2;
		this.latitud=latitud;
		this.longitud=longitud;
		this.setGestor(u);
	}

	@Override
	public String toString() {
		return "Restaurante [id=" + id + ", nombre=" + nombre + ", cp=" + cp + ", ciudad=" + ciudad + ", latitud="
				+ latitud + ", longitud="+ longitud+ ", sitios=" + sitios + ", platos=" + platos + "]";
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



	public Double getLatitud() {
		return latitud;
	}

	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

	public Double getLongitud() {
		return longitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}

	public String getGestor() {
		return gestor;
	}

	public void setGestor(String gestor) {
		this.gestor = gestor;
	}
	
	

}
