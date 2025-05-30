package cfgs.dam.tfg.jcmd.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*") // Permite todos los orígenes (en desarrollo)
				.allowedMethods("GET", "POST", "PUT", "DELETE").allowedHeaders("*");
	}
}