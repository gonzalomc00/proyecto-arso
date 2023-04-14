package restaurantes.modelo;

public class ResumenValoracion {

	private String idOpinion;
	private int numValoraciones;
	private Double calificacionMedia;

	@Override
	public String toString() {
		return "ResumenValoracion [idOpinion=" + idOpinion + ", numValoraciones=" + numValoraciones
				+ ", calificacionMedia=" + calificacionMedia + "]";
	}

	public String getIdOpinion() {
		return idOpinion;
	}

	public void setIdOpinion(String idOpinion) {
		this.idOpinion = idOpinion;
	}

	public int getNumValoraciones() {
		return numValoraciones;
	}

	public void setNumValoraciones(int numValoraciones) {
		this.numValoraciones = numValoraciones;
	}

	public Double getCalificacionMedia() {
		return calificacionMedia;
	}

	public void setCalificacionMedia(Double calificacionMedia) {
		this.calificacionMedia = calificacionMedia;
	}

}
