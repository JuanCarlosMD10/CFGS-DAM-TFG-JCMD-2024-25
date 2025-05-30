package cfgs.dam.tfg.jcmd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * Clase principal que inicia la aplicación. Ejecuta el contexto de Spring Boot
 * y carga las entidades del paquete indicado.
 */
@SpringBootApplication
@EntityScan(basePackages = "cfgs.dam.tfg.jcmd.models")
public class MiApp {

	/**
	 * Punto de entrada de la aplicación. Lanza la ejecución del proyecto con
	 * configuración automática de Spring.
	 *
	 * @param args argumentos de línea de comandos
	 */
	public static void main(String[] args) {
		SpringApplication.run(MiApp.class, args);
	}
}