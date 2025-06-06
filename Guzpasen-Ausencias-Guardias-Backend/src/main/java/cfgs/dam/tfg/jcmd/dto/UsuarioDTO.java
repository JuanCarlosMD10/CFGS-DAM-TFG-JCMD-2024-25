package cfgs.dam.tfg.jcmd.dto;

import cfgs.dam.tfg.jcmd.models.UsuarioModelo.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) para la entidad UsuarioModelo. Contiene los datos
 * esenciales de un usuario para su transferencia entre capas de la aplicación.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
	/** Identificador único del usuario */
	private Long idUsuario;

	/** Nombre del usuario */
	private String nombre;

	/** Apellidos del usuario */
	private String apellidos;

	/** Correo electrónico del usuario */
	private String email;
	
	/** Contraseña cifrada del usuario */
	private String clave;

	/** Rol del usuario */
	private Rol rol;

	/** Indica si el usuario es un usuario móvil */
	private Boolean usuarioMovil;
}