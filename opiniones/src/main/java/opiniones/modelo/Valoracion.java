package opiniones.modelo;

import java.time.LocalDateTime;

public class Valoracion {
	
	private String correo;
	private LocalDateTime fecha;
	private double calificacion;
	private String comentario;
	
	
	public Valoracion() {
		
	}
	
	public Valoracion(String correo, LocalDateTime fecha, double calificacion, String comentario) {
		this.correo=correo;
		this.fecha=fecha;
		this.calificacion=calificacion;
		this.comentario=comentario;
	}
	
	
	@Override
	public String toString() {
		return "Valoracion [correo=" + correo + ", fecha=" + fecha + ", calificacion=" + calificacion + ", comentario="
				+ comentario + "]";
	}
	
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public LocalDateTime getFecha() {
		return fecha;
	}
	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}
	public double getCalificacion() {
		return calificacion;
	}
	public void setCalificacion(double calificacion) {
		this.calificacion = calificacion;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	
}
