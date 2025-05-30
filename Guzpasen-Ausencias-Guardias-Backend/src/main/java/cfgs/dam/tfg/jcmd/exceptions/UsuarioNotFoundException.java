package cfgs.dam.tfg.jcmd.exceptions;

/**
 * Excepción personalizada que se lanza cuando un usuario no es encontrado.
 * Hereda de RuntimeException para indicar un error en tiempo de ejecución.
 */
public class UsuarioNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 7829841086438707354L;

	/**
	 * Constructor que recibe un mensaje descriptivo de la excepción.
	 * 
	 * @param message mensaje que describe el motivo de la excepción
	 */
	public UsuarioNotFoundException(String message) {
		super(message);
	}
}