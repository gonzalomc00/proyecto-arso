package arso.opiniones;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import restaurantes.servicio.IRetrofitOpinion;
import restaurantes.servicio.ServicioRestaurante;

import org.mockito.InjectMocks;
import org.mockito.Mock;

@ExtendWith(MockitoExtension.class)
class OpinionesMockTest {
	
	@Mock 
	private IRetrofitOpinion servicioOpinion;
	
	@InjectMocks 
	private ServicioRestaurante servicioRestaurante;
	
	
	
		

}