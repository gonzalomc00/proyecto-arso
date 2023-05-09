package restaurantes.eventos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import opiniones.modelo.OpinionResumen;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventoNuevaValoracion {

	@JsonProperty("IdOpinion")
	public String idOpinion;
	@JsonProperty("ResumenOpinion")
	public OpinionResumen resumenOpinion;
	
	public EventoNuevaValoracion() {
		
		
	}
	
	

	@Override
	public String toString() {
		return "EventoNuevaValoracion [IdOpinion=" + idOpinion +
				 ", resumenOpinion=" + resumenOpinion + "]";
	}



	public String getIdOpinion() {
		return idOpinion;
	}

	public void setIdOpinion(String idOpinion) {
		this.idOpinion = idOpinion;
	}



	public OpinionResumen getResumenOpinion() {
		return resumenOpinion;
	}

	public void setResumenOpinion(OpinionResumen resumenOpinion) {
		this.resumenOpinion = resumenOpinion;
	}
	
	
}
