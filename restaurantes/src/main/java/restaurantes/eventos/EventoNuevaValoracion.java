package restaurantes.eventos;

import com.fasterxml.jackson.annotation.JsonProperty;

import opiniones.modelo.OpinionResumen;
import opiniones.modelo.Valoracion;

public class EventoNuevaValoracion {

	@JsonProperty("IdOpinion")
	public String idOpinion;
	@JsonProperty("NuevaValoracion")
	public Valoracion nuevaValoracion;
	@JsonProperty("ResumenOpinion")
	public OpinionResumen resumenOpinion;
	
	public EventoNuevaValoracion() {
		
		
	}
	
	

	@Override
	public String toString() {
		return "EventoNuevaValoracion [IdOpinion=" + idOpinion + ", nuevaValoracion=" + nuevaValoracion
				+ ", resumenOpinion=" + resumenOpinion + "]";
	}



	public String getIdOpinion() {
		return idOpinion;
	}

	public void setIdOpinion(String idOpinion) {
		this.idOpinion = idOpinion;
	}

	public Valoracion getNuevaValoracion() {
		return nuevaValoracion;
	}

	public void setNuevaValoracion(Valoracion nuevaValoracion) {
		this.nuevaValoracion = nuevaValoracion;
	}

	public OpinionResumen getResumenOpinion() {
		return resumenOpinion;
	}

	public void setResumenOpinion(OpinionResumen resumenOpinion) {
		this.resumenOpinion = resumenOpinion;
	}
	
	
}
