package opiniones.modelo;

import java.util.LinkedList;
import java.util.List;

import org.bson.BsonType;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonRepresentation;

import repositorio.Identificable;

public class Opinion implements Identificable {

	@BsonId
	@BsonRepresentation(BsonType.OBJECT_ID)
	private String id;

	private String nombreRecurso;
	private List<Valoracion> valoraciones = new LinkedList<Valoracion>();

	public int getNumValoraciones() {
		return this.valoraciones.size();
	}

	public double getMedia() {
		if (this.valoraciones.isEmpty()) {
			return 0;
		} else {
			double media = 0;
			for (Valoracion v : this.valoraciones) {
				media += v.getCalificacion();
			}
			return media / this.getNumValoraciones();
		}

	}
	
	

	@Override
	public String toString() {
		return "Opinion [id=" + id + ", nombreRecurso=" + nombreRecurso + ", valoraciones=" + valoraciones
				+ ", Numero de valoraciones=" + getNumValoraciones() + ", Media=" + getMedia() + "]";
	}

	public String getNombreRecurso() {
		return nombreRecurso;
	}

	public void setNombreRecurso(String nombreRecurso) {
		this.nombreRecurso = nombreRecurso;
	}

	public List<Valoracion> getValoraciones() {
		return valoraciones;
	}

	public void setValoraciones(List<Valoracion> valoraciones) {
		this.valoraciones = valoraciones;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;

	}

}
