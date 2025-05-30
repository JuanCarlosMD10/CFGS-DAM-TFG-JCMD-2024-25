package cfgs.dam.tfg.jcmd.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo JPA que representa a un usuario del sistema. Contiene información
 * personal, credenciales, rol y relaciones con ausencias y guardias asignadas.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "usuario")
public class UsuarioModelo {

	/** Identificador único del usuario */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Long idUsuario;

	/** Nombre del usuario */
	@Column(name = "nombre")
	private String nombre;

	/** Apellidos del usuario */
	@Column(name = "apellidos")
	private String apellidos;

	/** Correo electrónico único del usuario */
	@Column(name = "email", unique = true)
	private String email;

	/** Contraseña cifrada del usuario */
	@Column(name = "clave")
	private String clave;

	/** Rol asignado al usuario */
	@Enumerated(EnumType.STRING)
	@Column(name = "rol")
	private Rol rol;

	/** Indica si el usuario accede desde un dispositivo móvil */
	@Column(name = "usuario_movil")
	private Boolean usuarioMovil = false;

	/** Lista de ausencias asociadas al usuario (profesor) */
	@JsonIgnore
	@OneToMany(mappedBy = "idProfesor")
	private List<AusenciaModelo> ausencias;

	/** Lista de guardias asociadas al usuario (profesor) */
	@OneToMany(mappedBy = "idProfesor")
	private List<GuardiaModelo> guardias;

	/**
	 * Enum que define los roles posibles para un usuario.
	 */
	public enum Rol {
		ADMINISTRADOR, PROFESOR, GESTOR_INCIDENCIAS, TECNICO
	}
}