package restaurantes.modelo;

public class Plato {
	// Por el momento, la clase Plato tendrá tres propiedades: nombre, descripción y
	// precio.

	private String nombre;
	private String descripcion;
	private Double precio;

	@Override
	public String toString() {
		return "Plato [nombre=" + nombre + ", descripcion=" + descripcion + ", precio=" + precio + "]";
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

}
