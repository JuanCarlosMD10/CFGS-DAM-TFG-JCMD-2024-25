package cfgs.dam.tfg.jcmd.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cfgs.dam.tfg.jcmd.models.GuardiaModelo;
import cfgs.dam.tfg.jcmd.models.UsuarioModelo;

/**
 * Interfaz que actúa como repositorio para las guardias. Extiende JpaRepository
 * para proporcionar operaciones CRUD sobre la entidad GuardiaModelo.
 * 
 * Esta interfaz incluye métodos para obtener las guardias según diferentes
 * criterios, como la fecha o el profesor asignado.
 */
@Repository
public interface GuardiaRepository extends JpaRepository<GuardiaModelo, Long> {

	/**
	 * Obtiene todas las guardias registradas en el sistema.
	 * 
	 * @return una lista con todas las guardias.
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
	 * Encuentra todas las guardias asociadas a un profesor.
	 * 
	 * @param profesor el profesor cuyas guardias se desean obtener.
	 * @return una lista de guardias asociadas al profesor dado.
	 */
	List<GuardiaModelo> findByIdProfesor(UsuarioModelo profesor);

	/**
	 * Encuentra una guardia por su identificador único.
	 * 
	 * @param idGuardia el identificador único de la guardia.
	 * @return la guardia correspondiente al identificador, o null si no existe.
	 */
	GuardiaModelo findGuardiaModeloByIdGuardia(Long idGuardia);
}