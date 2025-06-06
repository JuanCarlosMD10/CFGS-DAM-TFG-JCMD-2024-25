package cfgs.dam.tfg.jcmd.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configura CORS global para permitir peticiones desde cualquier origen.
 * 
 * Habilita los métodos GET, POST, PUT y DELETE con cualquier encabezado.
 * Útil en desarrollo; se recomienda restringir en producción.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Define las reglas CORS para todas las rutas.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }
}