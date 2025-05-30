package cfgs.dam.tfg.jcmd.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo JPA que representa una ausencia de un profesor. Contiene la
 * información relacionada con la fecha, horas, motivo, estado y relaciones con
 * guardia, profesor y zona.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ausencia")
public class AusenciaModelo {

	/** Identificador único de la ausencia */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_ausencia")
	private Long idAusencia;

	/** Fecha en la que se registra la ausencia */
	@Column(name = "fecha")
	private LocalDate fecha;

	/** Hora de inicio de la ausencia */
	@Enumerated(EnumType.STRING)
	@Column(name = "hora_inicio")
	private HoraInicio horaInicio;

	/** Hora de fin de la ausencia */
	@Enumerated(EnumType.STRING)
	@Column(name = "hora_fin")
	private HoraFin horaFin;

	/** Motivo que justifica la ausencia */
	@Column(name = "motivo")
	private String motivo;

	/** Estado actual de la ausencia */
	@Enumerated(EnumType.STRING)
	@Column(name = "estado")
	private Estado estado;

	/** Tareas asignadas al alumnado durante la ausencia */
	@Column(name = "tarea_alumnado")
	private String tareaAlumnado;

	/** Guardia asociada a esta ausencia */
	@OneToOne
	@JoinColumn(name = "id_guardia")
	private GuardiaModelo idGuardia;

	/** Profesor asociado a esta ausencia */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_profesor")
	private UsuarioModelo idProfesor;

	/** Zona relacionada con la ausencia */
	@ManyToOne
	@JoinColumn(name = "id_zona")
	private ZonaModelo idZona;

	/**
	 * Enum que define las horas de inicio posibles para una ausencia.
	 */
	public enum HoraInicio {
		PRIMERA, SEGUNDA, TERCERA, CUARTA, QUINTA, SEXTA
	}

	/**
	 * Enum que define las horas de fin posibles para una ausencia.
	 */
	public enum HoraFin {
		PRIMERA, SEGUNDA, TERCERA, CUARTA, QUINTA, SEXTA
	}

	/**
	 * Enum que define los estados posibles de una ausencia.
	 */
	public enum Estado {
		PENDIENTE_DE_GUARDIA, GUARDIA_ASIGNADA
	}
}