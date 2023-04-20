package opiniones.modelo;

public class OpinionResumen {

	public double CalificacionMedia;
	public int NumValoraciones;
	
	
	public OpinionResumen() {
		
	}
	
	
	@Override
	public String toString() {
		return "OpinionResumen [CalificacionMedia=" + CalificacionMedia + ", NumValoraciones=" + NumValoraciones + "]";
	}


	public double getCalificacionMedia() {
		return CalificacionMedia;
	}
	public void setCalificacionMedia(double calificacionMedia) {
		CalificacionMedia = calificacionMedia;
	}
	public int getNumValoraciones() {
		return NumValoraciones;
	}
	public void setNumValoraciones(int numValoraciones) {
		NumValoraciones = numValoraciones;
	}
	
	
}
