package opiniones.modelo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import utils.LocalDateTimeAdapter;

public class Valoracion {
	
	@JsonProperty("Correo")
	private String correo;
    @JsonProperty("Fecha")
    @JsonAdapter(LocalDateTimeAdapter.class)
	private LocalDateTime fecha;
    @JsonProperty("Calificacion")
	private double calificacion;
    @JsonProperty("Comentario")
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
