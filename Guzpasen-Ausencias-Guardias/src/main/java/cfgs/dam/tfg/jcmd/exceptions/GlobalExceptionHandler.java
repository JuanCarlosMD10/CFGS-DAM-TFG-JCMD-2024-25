package cfgs.dam.tfg.jcmd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * Clase para gestionar excepciones globales en la aplicación. Proporciona
 * métodos para manejar excepciones específicas relacionadas con las ausencias y
 * las guardias.
 * 
 * Se encarga de devolver respuestas HTTP adecuadas con los mensajes de error
 * correspondientes.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Maneja la excepción GuardiaNotFoundException y devuelve un mensaje con el
	 * código de estado HTTP 404 (Not Found).
	 * 
	 * @param ex la excepción que fue lanzada
	 * @return ResponseEntity con el mensaje de error y el código HTTP 404
	 */
	@ExceptionHandler(GuardiaNotFoundException.class)
	public ResponseEntity<String> handleGuardiaNotFound(GuardiaNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	/**
	 * Maneja la excepción GuardiaCreationException y devuelve un mensaje con el
	 * código de estado HTTP 500 (Internal Server Error).
	 * 
	 * @param ex la excepción que fue lanzada
	 * @return ResponseEntity con el mensaje de error y el código HTTP 500
	 */
	@ExceptionHandler(GuardiaCreationException.class)
	public ResponseEntity<String> handleGuardiaCreationError(GuardiaCreationException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Maneja la excepción AusenciaNotFoundException y devuelve un mensaje con el
	 * código de estado HTTP 404 (Not Found).
	 * 
	 * @param ex la excepción que fue lanzada
	 * @return ResponseEntity con el mensaje de error y el código HTTP 404
	 */
	@ExceptionHandler(AusenciaNotFoundException.class)
	public ResponseEntity<String> handleAusenciaNotFound(AusenciaNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	/**
	 * Maneja la excepción AusenciaCreationException y devuelve un mensaje con el
	 * código de estado HTTP 500 (Internal Server Error).
	 * 
	 * @param ex la excepción que fue lanzada
	 * @return ResponseEntity con el mensaje de error y el código HTTP 500
	 */
	@ExceptionHandler(AusenciaCreationException.class)
	public ResponseEntity<String> handleAusenciaCreationError(AusenciaCreationException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Maneja cualquier excepción no especificada y devuelve un mensaje genérico con
	 * el código de estado HTTP 500 (Internal Server Error).
	 * 
	 * @param ex la excepción que fue lanzada
	 * @return ResponseEntity con un mensaje genérico de error y el código HTTP 500
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGeneralException(Exception ex) {
		return new ResponseEntity<>("Ha ocurrido un error inesperado.", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}