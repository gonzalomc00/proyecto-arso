package restaurantes.servicio;

public class RestauranteResumen {
	private String id;
	private String nombre;
	private String cp;
	private String ciudad;
	private String idOpinion;
	private Double valoracion;
	private String gestor;
	private Double longitud;
	private Double latitud;

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


	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public Double getValoracion() {
		return valoracion;
	}

	public void setValoracion(Double valoracion) {
		this.valoracion = valoracion;
	}

	public String getGestor() {
		return gestor;
	}

	public void setGestor(String gestor) {
		this.gestor = gestor;
	}


	public String getIdOpinion() {
		return idOpinion;
	}

	public void setIdOpinion(String idOpinion) {
		this.idOpinion = idOpinion;
	}


	public Double getLongitud() {
		return longitud;
	}

	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}
	
	public Double getLatitud() {
		return latitud;
	}
	
	public void setLatitud(Double latitud) {
		this.latitud=latitud;
	}

	@Override
	public String toString() {
		return "RestauranteResumen [id=" + id + ", nombre=" + nombre + ", cp=" + cp + ", ciudad=" + ciudad
				+ ", idOpinion=" + idOpinion + ", valoracion=" + valoracion + ", gestor=" + gestor + ", longitud="
				+ longitud + ", latitud=" + latitud + "]";
	}
	
	
	
	
	
	

}
