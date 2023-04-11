package usuarios.modelo;

import repositorio.Identificable;

public class Usuario implements Identificable {

	private String id;
	
	private String nombre;
	private String idOAuth;
	private String email;
	private Rol rol;
	
	public Usuario() {
		
	}
	
	public Usuario(String nombre, String email, String idOAuth, Rol rol) {		
		this.nombre = nombre;
		this.email = email;
		this.idOAuth = idOAuth;
		this.rol = rol;
	}

	@Override
	public String getId() {		
		return id;
	}

	@Override
	public void setId(String id) {		
		this.id = id;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getIdOAuth() {
		return idOAuth;
	}
	public void setIdOAuth(String idOAuth) {
		this.idOAuth = idOAuth;
	}
	
	public Rol getRol() {
		return rol;
	}
	
	public void setRol(Rol rol) {
		this.rol = rol;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombre=" + nombre + ", idOAuth=" + idOAuth + ", email=" + email + ", rol=" + rol
				+ "]";
	}
	
	
}
