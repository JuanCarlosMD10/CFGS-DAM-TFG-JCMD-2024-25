package cfgs.dam.tfg.jcmd.exceptions;

/**
 * Excepción que se lanza cuando no se encuentra una guardia en el sistema.
 * Extiende de RuntimeException y proporciona un mensaje de error específico
 * para el problema ocurrido.
 */
public class GuardiaNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -6208669239407121655L;

	/**
	 * Constructor de la excepción que recibe un mensaje detallado.
	 *
	 * @param message El mensaje que describe el motivo de la excepción.
	 */
	public GuardiaNotFoundException(String message) {
		super(message);
	}
}