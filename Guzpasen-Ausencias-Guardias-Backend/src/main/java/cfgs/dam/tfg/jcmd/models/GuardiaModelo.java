package cfgs.dam.tfg.jcmd.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
 * Representa el modelo de datos de una guardia. Este modelo contiene la
 * información sobre las guardias, tales como la fecha, la hora de inicio y fin,
 * el profesor asignado, el aula asignada, y las ausencias asociadas a la
 * guardia.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "guardia")
public class GuardiaModelo {

	/** Identificador único de la guardia */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_guardia")
	private Long idGuardia;

	/** Fecha en la que se realiza la guardia */
	@Column(name = "fecha")
	private LocalDate fecha;

	/** Hora de inicio de la guardia */
	@Enumerated(EnumType.STRING)
	@Column(name = "hora_inicio")
	private HoraInicio horaInicio;

	/** Hora de fin de la guardia */
	@Enumerated(EnumType.STRING)
	@Column(name = "hora_fin")
	private HoraFin horaFin;

	/** Profesor asignado a la guardia */
	@ManyToOne
	@JoinColumn(name = "id_profesor")
	private UsuarioModelo idProfesor;

	/** Aula o zona asignada para la guardia */
	@ManyToOne
	@JoinColumn(name = "id_aula")
	private ZonaModelo idAula;

	/** Ausencia asociada a la guardia */
	@OneToOne
	@JoinColumn(name = "id_ausencia")
	private AusenciaModelo idAusencia;

	/**
	 * Enum que define las horas de inicio posibles para una guardia.
	 */
	public enum HoraInicio {
		PRIMERA, SEGUNDA, TERCERA, CUARTA, QUINTA, SEXTA
	}

	/**
	 * Enum que define las horas de fin posibles para una guardia.
	 */
	public enum HoraFin {
		PRIMERA, SEGUNDA, TERCERA, CUARTA, QUINTA, SEXTA
	}
}