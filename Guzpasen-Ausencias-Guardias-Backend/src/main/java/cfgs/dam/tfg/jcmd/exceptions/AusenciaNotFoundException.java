package cfgs.dam.tfg.jcmd.exceptions;

/**
 * Excepción personalizada para manejar el caso en el que no se encuentra una
 * ausencia.
 */
public class AusenciaNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -3036591826778144360L;

	/**
	 * Constructor que crea una excepción con el mensaje proporcionado.
	 * 
	 * @param message El mensaje de error.
	 */
	public AusenciaNotFoundException(String message) {
		super(message);
	}
}