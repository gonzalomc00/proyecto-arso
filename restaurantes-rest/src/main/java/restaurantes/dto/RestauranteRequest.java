package restaurantes.dto;

//DTO - modelado como POJO - para el metodo create (no pertenece al dominio persé solo sirve para transportar datos)
public class RestauranteRequest {

	private String nombre;
	private String cp;
	private String ciudad;
	private String coordenadas; // Formato " x, y"

	// Por simplicidad cuando se da de alta un restaurante lo hace sin platos ni
	// sitios turisticos

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

	public String getCoordenadas() {
		return coordenadas;
	}

	public void setCoordenadas(String coordenadas) {
		this.coordenadas = coordenadas;
	}

}
