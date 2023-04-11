package pasarela.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import servlet.ServletHome;

@SpringBootApplication
@EnableZuulProxy
public class PasarelaZuulApplication {

	@Bean
	public ServletRegistrationBean<ServletHome> servletRegistrationBean(){
	    return new ServletRegistrationBean<ServletHome>(new ServletHome(),"/home/*");
	}
	
	public static void main(String[] args) {
		SpringApplication.run(PasarelaZuulApplication.class, args);
	}
}
