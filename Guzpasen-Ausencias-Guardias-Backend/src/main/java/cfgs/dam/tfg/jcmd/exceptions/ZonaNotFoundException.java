package cfgs.dam.tfg.jcmd.exceptions;

/**
 * Excepción personalizada que se lanza cuando una zona no es encontrada.
 * Extiende RuntimeException para indicar un error en tiempo de ejecución.
 */
public class ZonaNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -6881695253563887163L;

	/**
	 * Constructor que recibe un mensaje descriptivo de la excepción.
	 * 
	 * @param message mensaje que describe el motivo de la excepción
	 */
	public ZonaNotFoundException(String message) {
		super(message);
	}
}