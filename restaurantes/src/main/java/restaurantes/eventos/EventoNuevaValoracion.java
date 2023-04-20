package restaurantes.eventos;

import opiniones.modelo.OpinionResumen;
import opiniones.modelo.Valoracion;

public class EventoNuevaValoracion {

	public String IdOpinion;
	public Valoracion nuevaValoracion;
	public OpinionResumen resumenOpinion;
	
	public EventoNuevaValoracion() {
		
		
	}
	
	

	@Override
	public String toString() {
		return "EventoNuevaValoracion [IdOpinion=" + IdOpinion + ", nuevaValoracion=" + nuevaValoracion
				+ ", resumenOpinion=" + resumenOpinion + "]";
	}



	public String getIdOpinion() {
		return IdOpinion;
	}

	public void setIdOpinion(String idOpinion) {
		IdOpinion = idOpinion;
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
