package cfgs.dam.tfg.jcmd.models;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa el modelo de datos de una ausencia. Este modelo se utiliza para
 * almacenar la información sobre las ausencias de los profesores, incluyendo
 * detalles como la fecha, hora, motivo, y el estado. Además, contiene las
 * relaciones con las entidades relacionadas como la guardia asignada, el
 * profesor y la zona.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ausencia")
public class AusenciaModelo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_ausencia")
	private Long idAusencia;

	@Column(name = "fecha")
	private LocalDate fecha;

	@Column(name = "hora_inicio")
	private HoraInicio horaInicio;

	@Column(name = "hora_fin")
	private HoraFin horaFin;

	@Column(name = "motivo")
	private String motivo;

	@Column(name = "estado")
	private Estado estado;

	@Column(name = "tarea_alumnado")
	private String tareaAlumnado;

	@ManyToOne
	@JoinColumn(name = "id_guardia")
	private GuardiaModelo idGuardia;

	@ManyToOne
	@JoinColumn(name = "id_profesor")
	private UsuarioModelo idProfesor;

	@ManyToOne
	@JoinColumn(name = "id_zona")
	private ZonaModelo idZona;

	/**
	 * Define las horas de inicio de la ausencia.
	 */
	public enum HoraInicio {
		PRIMERA, SEGUNDA, TERCERA, CUARTA, QUINTA, SEXTA
	}

	/**
	 * Define las horas de fin de la ausencia.
	 */
	public enum HoraFin {
		PRIMERA, SEGUNDA, TERCERA, CUARTA, QUINTA, SEXTA
	}

	/**
	 * Define los estados posibles de una ausencia.
	 */
	public enum Estado {
		PENDIENTE_DE_GUARDIA, GUARDIA_ASIGNADA
	}
}