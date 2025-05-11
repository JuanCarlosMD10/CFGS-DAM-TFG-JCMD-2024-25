package cfgs.dam.tfg.jcmd.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cfgs.dam.tfg.jcmd.exceptions.GuardiaCreationException;
import cfgs.dam.tfg.jcmd.exceptions.GuardiaNotFoundException;
import cfgs.dam.tfg.jcmd.models.GuardiaModelo;
import cfgs.dam.tfg.jcmd.models.UsuarioModelo;
import cfgs.dam.tfg.jcmd.repositories.GuardiaRepository;

/**
 * Implementación del servicio de gestión de guardias.
 * 
 * Esta clase proporciona la lógica de negocio para gestionar las guardias en el
 * sistema, incluyendo operaciones como la creación, búsqueda, actualización y
 * eliminación de guardias.
 */
@Service
public class GuardiaServiceImpl implements GuardiaService {

	@Autowired
	private GuardiaRepository guardiaRepository;

	/**
	 * Obtiene todas las guardias registradas en el sistema.
	 * 
	 * @return una lista de todas las guardias.
	 */
	@Override
	public List<GuardiaModelo> findAll() {
		return guardiaRepository.findAll();
	}

	/**
	 * Encuentra todas las guardias correspondientes a una fecha específica.
	 * 
	 * @param fecha la fecha de las guardias.
	 * @return una lista de guardias para la fecha proporcionada.
	 */
	@Override
	public List<GuardiaModelo> findByFecha(LocalDate fecha) {
		return guardiaRepository.findByFecha(fecha);
	}

	/**
	 * Encuentra todas las guardias asignadas a un profesor específico.
	 * 
	 * @param profesor el profesor cuyas guardias se desean obtener.
	 * @return una lista de guardias asignadas al profesor dado.
	 */
	@Override
	public List<GuardiaModelo> findByProfesor(UsuarioModelo profesor) {
		return guardiaRepository.findByIdProfesor(profesor);
	}

	/**
	 * Crea una nueva guardia en el sistema.
	 * 
	 * @param guardia el objeto de tipo GuardiaModelo que se va a crear.
	 * @return la guardia creada.
	 * @throws GuardiaCreationException si ocurre un error al intentar crear la
	 *                                  guardia.
	 */
	@Override
	public GuardiaModelo createGuardia(GuardiaModelo guardia) {
		try {
			return guardiaRepository.save(guardia);
		} catch (Exception e) {
			throw new GuardiaCreationException("Error al crear la guardia", e);
		}
	}

	/**
	 * Encuentra una guardia por su identificador único.
	 * 
	 * @param idGuardia el identificador único de la guardia.
	 * @return la guardia correspondiente al identificador proporcionado.
	 * @throws GuardiaNotFoundException si no se encuentra la guardia con el ID
	 *                                  especificado.
	 */
	@Override
	public GuardiaModelo findGuardiaByIdGuardia(Long idGuardia) {
		GuardiaModelo guardia = guardiaRepository.findGuardiaModeloByIdGuardia(idGuardia);
		if (guardia == null) {
			throw new GuardiaNotFoundException("Guardia no encontrada con ID: " + idGuardia);
		}
		return guardia;
	}

	/**
	 * Actualiza una guardia existente en el sistema.
	 * 
	 * @param guardia el objeto de tipo GuardiaModelo con la información
	 *                actualizada.
	 * @return la guardia actualizada.
	 */
	@Override
	public GuardiaModelo updateGuardia(GuardiaModelo guardia) {
		return guardiaRepository.save(guardia);
	}

	/**
	 * Elimina una guardia del sistema mediante su identificador único.
	 * 
	 * @param idGuardia el identificador único de la guardia a eliminar.
	 * @throws GuardiaNotFoundException si no se encuentra la guardia con el ID
	 *                                  especificado.
	 */
	@Override
	public void deleteGuardiaById(Long idGuardia) {
		if (!guardiaRepository.existsById(idGuardia)) {
			throw new GuardiaNotFoundException("No se puede eliminar. Guardia no encontrada con ID: " + idGuardia);
		}
		guardiaRepository.deleteById(idGuardia);
	}
}