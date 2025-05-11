package cfgs.dam.tfg.jcmd.services;

import cfgs.dam.tfg.jcmd.models.GuardiaModelo;
import cfgs.dam.tfg.jcmd.models.UsuarioModelo;

import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz que define los servicios relacionados con la gestión de guardias.
 * 
 * Esta interfaz proporciona los métodos necesarios para gestionar las guardias,
 * tales como la creación, actualización, eliminación y consulta de guardias.
 */
public interface GuardiaService {

	/**
	 * Obtiene todas las guardias registradas.
	 * 
	 * @return una lista de todas las guardias.
	 */
	List<GuardiaModelo> findAll();

	/**
	 * Encuentra todas las guardias correspondientes a una fecha específica.
	 * 
	 * @param fecha la fecha de las guardias.
	 * @return una lista de guardias para la fecha proporcionada.
	 */
	List<GuardiaModelo> findByFecha(LocalDate fecha);

	/**
	 * Encuentra todas las guardias asignadas a un profesor específico.
	 * 
	 * @param profesor el profesor cuyas guardias se desean obtener.
	 * @return una lista de guardias asignadas al profesor dado.
	 */
	List<GuardiaModelo> findByProfesor(UsuarioModelo profesor);

	/**
	 * Crea una nueva guardia en el sistema.
	 * 
	 * @param guardia el objeto de tipo GuardiaModelo que se va a crear.
	 * @return la guardia creada.
	 */
	GuardiaModelo createGuardia(GuardiaModelo guardia);

	/**
	 * Encuentra una guardia por su identificador único.
	 * 
	 * @param idGuardia el identificador único de la guardia.
	 * @return la guardia correspondiente al identificador proporcionado.
	 */
	GuardiaModelo findGuardiaByIdGuardia(Long idGuardia);

	/**
	 * Actualiza una guardia existente en el sistema.
	 * 
	 * @param guardia el objeto de tipo GuardiaModelo con la información
	 *                actualizada.
	 * @return la guardia actualizada.
	 */
	GuardiaModelo updateGuardia(GuardiaModelo guardia);

	/**
	 * Elimina una guardia del sistema mediante su identificador único.
	 * 
	 * @param idGuardia el identificador único de la guardia a eliminar.
	 */
	void deleteGuardiaById(Long idGuardia);
}