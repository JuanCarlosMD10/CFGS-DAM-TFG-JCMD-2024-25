package cfgs.dam.tfg.jcmd.exceptions;

/**
 * Excepción que se lanza cuando ocurre un error al crear una ausencia. Permite
 * capturar y propagar la causa original del fallo.
 */
public class AusenciaCreationException extends RuntimeException {

	private static final long serialVersionUID = -1828176801586457057L;

	public AusenciaCreationException(String message, Throwable cause) {
		super(message, cause);
	}
}