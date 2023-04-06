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
	 private List<Valoracion> valoraciones= new LinkedList<Valoracion>();
	 
	 
	 
	 public Opinion() {
		 
	 }
	 
	 public Opinion(String nombre) {
		 this.nombreRecurso=nombre;
	 }
	 
	 private int numValoraciones() {
		 return this.valoraciones.size();
	 }
	 
	 private double media() {
		 double media=0;
		 for(Valoracion v: this.valoraciones) {
			 media= v.getCalificacion();
		 }
		 return media/numValoraciones();
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
		this.id=id;
		
	}

	
}
