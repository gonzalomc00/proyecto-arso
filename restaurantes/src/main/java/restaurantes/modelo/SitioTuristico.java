package restaurantes.modelo;

import java.util.LinkedList;
import java.util.List;

public class SitioTuristico {
	private String nombre;
	private List<String> categorias = new LinkedList<String>();
	private String resumen;
	private List<String> enlaces = new LinkedList<String>();
	private String foto;

	public SitioTuristico() {
		// Pojo
	}

	public SitioTuristico(String n, List<String> categorias, String r, List<String> enlaces, String foto) {
		this.nombre = n;
		this.categorias = categorias;
		this.resumen = r;
		this.enlaces = enlaces;
		this.foto = foto;
	}

	@Override
	public String toString() {
		return "SitioTuristico [nombre=" + nombre + ", categorias=" + categorias + ", resumen=" + resumen + ", enlaces="
				+ enlaces + ", foto=" + foto + "]";
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<String> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<String> categorias) {
		this.categorias = categorias;
	}

	public String getResumen() {
		return resumen;
	}

	public void setResumen(String resumen) {
		this.resumen = resumen;
	}

	public List<String> getEnlaces() {
		return enlaces;
	}

	public void setEnlaces(List<String> enlaces) {
		this.enlaces = enlaces;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

}
