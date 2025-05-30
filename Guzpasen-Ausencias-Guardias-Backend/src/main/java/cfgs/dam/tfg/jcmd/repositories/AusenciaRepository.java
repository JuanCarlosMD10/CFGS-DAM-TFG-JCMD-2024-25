package cfgs.dam.tfg.jcmd.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cfgs.dam.tfg.jcmd.models.AusenciaModelo;
import cfgs.dam.tfg.jcmd.models.AusenciaModelo.HoraFin;
import cfgs.dam.tfg.jcmd.models.AusenciaModelo.HoraInicio;
import cfgs.dam.tfg.jcmd.models.UsuarioModelo;

/**
 * Repositorio JPA para la entidad AusenciaModelo. Proporciona métodos para
 * realizar operaciones CRUD y consultas personalizadas relacionadas con las
 * ausencias.
 */
@Repository
public interface AusenciaRepository extends JpaRepository<AusenciaModelo, Long> {

	/**
	 * Busca todas las ausencias que coinciden con un estado determinado.
	 *
	 * @param estado Estado de la ausencia a filtrar.
	 * @return Lista de ausencias con el estado especificado.
	 */
	List<AusenciaModelo> findByEstado(AusenciaModelo.Estado estado);

	/**
	 * Busca todas las ausencias asociadas a un profesor específico.
	 *
	 * @param idProfesor Profesor asociado a las ausencias.
	 * @return Lista de ausencias del profesor especificado.
	 */
	List<AusenciaModelo> findByIdProfesor(UsuarioModelo idProfesor);

	/**
	 * Busca ausencias de un profesor en una fecha y hora de inicio específica.
	 *
	 * @param idProfesor Profesor asociado a la ausencia.
	 * @param fecha      Fecha de la ausencia.
	 * @param horaInicio Hora de inicio de la ausencia.
	 * @return Lista de ausencias que coinciden con los parámetros.
	 */
	List<AusenciaModelo> findByIdProfesorAndFechaAndHoraInicio(UsuarioModelo idProfesor, LocalDate fecha,
			AusenciaModelo.HoraInicio horaInicio);

	/**
	 * Busca ausencias de un profesor en una fecha determinada donde la hora de
	 * inicio sea antes de un valor y la hora de fin sea después de otro valor.
	 *
	 * @param usuarioModelo Profesor asociado a la ausencia.
	 * @param fecha         Fecha de la ausencia.
	 * @param horaInicio    Hora de inicio límite superior.
	 * @param horaFin       Hora de fin límite inferior.
	 * @return Lista de ausencias que coinciden con el rango horario.
	 */
	List<AusenciaModelo> findByIdProfesorAndFechaAndHoraInicioBeforeAndHoraFinAfter(UsuarioModelo usuarioModelo,
			LocalDate fecha, HoraInicio horaInicio, HoraFin horaFin);

	/**
	 * Busca todas las ausencias asociadas a un profesor por su id.
	 *
	 * @param idUsuario Identificador del profesor.
	 * @return Lista de ausencias del profesor.
	 */
	List<AusenciaModelo> findByIdProfesor_IdUsuario(Long idUsuario);
}