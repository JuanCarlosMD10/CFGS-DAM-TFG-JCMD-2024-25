package cfgs.dam.tfg.jcmd.dto;

import java.time.LocalDate;

import lombok.Data;

/**
 * Data Transfer Object (DTO) para la entidad GuardiaModelo. Contiene los datos
 * esenciales de una guardia para su transferencia entre capas de la aplicación.
 */
@Data
public class GuardiaDTO {

	/** Identificador único de la guardia */
	private Long idGuardia;

	/** Fecha en la que se realiza la guardia */
	private LocalDate fecha;

	/** Hora de inicio de la guardia */
	private String horaInicio;

	/** Hora de fin de la guardia */
	private String horaFin;

	/** Identificador del profesor asignado */
	private Long idProfesor;

	/** Nombre del profesor asignado */
	private String nombreProfesor;

	/** Identificador de la ausencia asociada, si existe */
	private Long idAusencia;

	/** Identificador del aula asignado */
	private Long idAula;

	/** Nombre del aula asignado */
	private String nombreAula;
}