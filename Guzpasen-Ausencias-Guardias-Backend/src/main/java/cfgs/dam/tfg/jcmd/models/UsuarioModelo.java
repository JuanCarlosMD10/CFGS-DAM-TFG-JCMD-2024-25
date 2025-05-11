package cfgs.dam.tfg.jcmd.models;

import java.util.List;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "usuario")
public class UsuarioModelo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Long idUsuario;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "apellidos")
	private String apellidos;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "clave")
	private String clave;

	@Column(name = "rol")
	private Rol rol;

	@Column(name = "usuario_movil")
	private Boolean usuarioMovil = false;

	@OneToMany(mappedBy = "idProfesor")
	private List<AusenciaModelo> ausencias;

	@OneToMany(mappedBy = "idProfesor")
	private List<GuardiaModelo> guardias;

	public enum Rol {
		ADMINISTRADOR, PROFESOR, GESTOR_INCIDENCIAS, TECNICO
	}
}