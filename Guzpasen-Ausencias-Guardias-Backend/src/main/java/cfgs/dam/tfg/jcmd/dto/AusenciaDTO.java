package cfgs.dam.tfg.jcmd.dto;

import java.time.LocalDate;

import cfgs.dam.tfg.jcmd.models.AusenciaModelo.HoraFin;
import cfgs.dam.tfg.jcmd.models.AusenciaModelo.HoraInicio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) que representa una ausencia de un profesor.
 * Utilizado para transferir datos entre las capas de presentación y servicio.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AusenciaDTO {

	/** ID único de la ausencia. */
	private Long idAusencia;

	/** Estado de la ausencia (ej. PENDIENTE, APROBADA, RECHAZADA). */
	private String estado;

	/** Fecha en la que se produce la ausencia. */
	private LocalDate fecha;

	/** Hora de inicio de la ausencia. */
	private HoraInicio horaInicio;

	/** Hora de fin de la ausencia. */
	private HoraFin horaFin;

	/** ID del profesor que solicita o registra la ausencia. */
	private Long idProfesor;

	/** Nombre del profesor asociado a la ausencia. */
	private String nombreProfesor;

	/** Motivo o justificación de la ausencia. */
	private String motivo;

	/** Tarea dejada al alumnado durante la ausencia. */
	private String tareaAlumnado;
}