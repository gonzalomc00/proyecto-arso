package arso.modelo;

import java.util.LinkedList;
import java.util.List;

import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonRepresentation;

public class Restaurante {
	@BsonId
	@BsonRepresentation(BsonType.OBJECT_ID)
	private String id;

	private String nombre;
	private String cp;
	private List<SitioTuristico> sitios = new LinkedList<SitioTuristico>();
	private List<Plato> platos = new LinkedList<Plato>();

	@Override
	public String toString() {
		return "Restaurante [id=" + id + ", nombre=" + nombre + ", cp=" + cp + ", sitios=" + sitios + ", platos="
				+ platos + "]";
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

}
