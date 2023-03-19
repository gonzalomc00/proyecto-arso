package restaurantes.modelo;

public class Plato {

	private String nombre;
	private String descripcion;
	private Double precio;
	private boolean disponibilidad; 
	

	@Override
	public String toString() {
		return "Plato [nombre=" + nombre + ", descripcion=" + descripcion + ", precio=" + precio + ", disponibilidad="
				+ disponibilidad + "]";
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public boolean isDisponibilidad() {
		return disponibilidad;
	}

	public void setDisponibilidad(boolean disponibilidad) {
		this.disponibilidad = disponibilidad;
	}

	
}
