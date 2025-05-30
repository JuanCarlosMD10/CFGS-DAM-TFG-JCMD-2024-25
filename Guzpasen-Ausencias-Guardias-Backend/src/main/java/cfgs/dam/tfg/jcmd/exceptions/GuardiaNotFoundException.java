package cfgs.dam.tfg.jcmd.exceptions;

/**
 * Excepción personalizada para manejar el caso en el que no se encuentra una
 * guardia.
 */
public class GuardiaNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -6208669239407121655L;

	/**
	 * Constructor que crea una excepción con el mensaje proporcionado.
	 * 
	 * @param message El mensaje de error.
	 */
	public GuardiaNotFoundException(String message) {
		super(message);
	}
}