package cfgs.dam.tfg.jcmd.models;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_guardia")
	private Long idGuardia;

	@Column(name = "fecha")
	private LocalDate fecha;

	@Column(name = "hora_inicio")
	private HoraInicio horaInicio;

	@Column(name = "hora_fin")
	private HoraFin horaFin;

	@ManyToOne
	@JoinColumn(name = "id_profesor")
	private UsuarioModelo idProfesor;

	@ManyToOne
	@JoinColumn(name = "id_aula")
	private ZonaModelo idAula;

	@OneToMany(mappedBy = "idGuardia")
	private List<AusenciaModelo> ausencias;

	/**
	 * Define las horas de inicio de la guardia.
	 */
	public enum HoraInicio {
		PRIMERA, SEGUNDA, TERCERA, CUARTA, QUINTA, SEXTA
	}

	/**
	 * Define las horas de fin de la guardia.
	 */
	public enum HoraFin {
		PRIMERA, SEGUNDA, TERCERA, CUARTA, QUINTA, SEXTA
	}
}