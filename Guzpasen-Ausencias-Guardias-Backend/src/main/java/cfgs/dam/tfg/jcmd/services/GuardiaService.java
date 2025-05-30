package cfgs.dam.tfg.jcmd.services;

import java.time.LocalDate;
import java.util.List;

import cfgs.dam.tfg.jcmd.dto.GuardiaDTO;

/**
 * Interfaz que define los métodos para la gestión de las guardias en el
 * sistema. Permite asignar nuevas guardias, consultar guardias por fecha y
 * obtener guardias por su identificador.
 */
public interface GuardiaService {

	/**
	 * Asigna una nueva guardia al sistema.
	 * 
	 * @param guardia DTO que representa la guardia a asignar.
	 * @return DTO de la guardia que ha sido asignada.
	 */
	GuardiaDTO asignarGuardia(GuardiaDTO guardia);

	/**
	 * Consulta las guardias existentes en el sistema, filtrando por fecha.
	 * 
	 * @param fecha Fecha para filtrar las guardias.
	 * @return Lista de DTOs de guardias correspondientes a la fecha proporcionada.
	 */
	List<GuardiaDTO> consultarGuardias(LocalDate fecha);

	/**
	 * Obtiene una guardia por su identificador.
	 * 
	 * @param id Identificador de la guardia.
	 * @return DTO de la guardia correspondiente al id proporcionado.
	 * @throws RuntimeException si no se encuentra la guardia con el id
	 *                          proporcionado.
	 */
	GuardiaDTO obtenerGuardia(Long id);
}