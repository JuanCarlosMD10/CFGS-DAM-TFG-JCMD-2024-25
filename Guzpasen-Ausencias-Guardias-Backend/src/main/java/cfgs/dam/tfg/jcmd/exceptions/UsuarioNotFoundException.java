package cfgs.dam.tfg.jcmd.exceptions;

public class UsuarioNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 7829841086438707354L;

	public UsuarioNotFoundException(String message) {
		super(message);
	}
}