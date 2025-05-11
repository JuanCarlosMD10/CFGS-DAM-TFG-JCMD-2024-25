package cfgs.dam.tfg.jcmd.exceptions;

/**
 * Excepción que se lanza cuando no se encuentra una ausencia. Utilizada para
 * indicar que una ausencia no existe en la base de datos.
 */
public class AusenciaNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -476257384980360868L;

	public AusenciaNotFoundException(String message) {
		super(message);
	}
}