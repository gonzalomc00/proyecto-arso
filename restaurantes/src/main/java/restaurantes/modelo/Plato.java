package restaurantes.modelo;

public class Plato {

	private String nombre;
	private String descripcion;
	private Double precio;
	private boolean disponibilidad; 
	
	
	public Plato() { //POJO
		
	}
	
	public Plato(String nom, String desc, Double precio) {
		//Control de precondiciones
		if (nom == null || nom.isEmpty())
			throw new IllegalArgumentException("nombre del plato: no debe ser nulo ni vacio");
		
		if (desc == null || desc.isEmpty())
			throw new IllegalArgumentException("descripcion: no debe ser nulo ni vacio");
		
		if (precio == null || precio.isNaN())
			throw new IllegalArgumentException("precio: no debe ser nulo y debe ser un numero");
		
		this.nombre = nom;
		this.descripcion = desc;
		this.precio = precio;
		this.disponibilidad = true;
		
	}
	
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

	public boolean isDisponible() {
		return disponibilidad;
	}

	public void setDisponibilidad(boolean disponibilidad) {
		this.disponibilidad = disponibilidad;
	}

	
}
