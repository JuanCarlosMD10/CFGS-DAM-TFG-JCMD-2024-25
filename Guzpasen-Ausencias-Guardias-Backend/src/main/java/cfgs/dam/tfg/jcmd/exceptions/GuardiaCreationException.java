package cfgs.dam.tfg.jcmd.exceptions;

/**
 * Excepción que se lanza cuando ocurre un error al crear una guardia. Extiende
 * de RuntimeException y proporciona un mensaje detallado y una causa para
 * ayudar a identificar el problema.
 */
public class GuardiaCreationException extends RuntimeException {

	private static final long serialVersionUID = 134289947217922067L;

	/**
	 * Constructor de la excepción que recibe un mensaje y una causa.
	 *
	 * @param message El mensaje que describe el motivo de la excepción.
	 * @param cause   La causa que origina la excepción.
	 */
	public GuardiaCreationException(String message, Throwable cause) {
		super(message, cause);
	}
}