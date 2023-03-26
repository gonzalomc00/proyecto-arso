package retrofit.restaurantes;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import restaurantes.servicio.RestauranteResumen;

@XmlRootElement
public class Listado {

	public static class ResumenExtendido {
		
		private String url;
		private RestauranteResumen resumen;
		
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public RestauranteResumen getResumen() {
			return resumen;
		}
		public void setResumen(RestauranteResumen resumen) {
			this.resumen = resumen;
		}
		
	}
	
	private List<ResumenExtendido> restaurante;
	
	public List<ResumenExtendido> getRestaurante() {
		return restaurante;
	}
	
	public void setRestaurante(List<ResumenExtendido> restaurante) {
		this.restaurante = restaurante;
	}
}
