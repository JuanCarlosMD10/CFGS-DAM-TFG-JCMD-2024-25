package cfgs.dam.tfg.jcmd.services;

import cfgs.dam.tfg.jcmd.models.AusenciaModelo;
import cfgs.dam.tfg.jcmd.models.UsuarioModelo;

import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz que define los servicios relacionados con la gestión de ausencias.
 * 
 * Esta interfaz proporciona métodos para realizar operaciones CRUD sobre las
 * ausencias, así como métodos para obtener ausencias filtradas por fecha o por
 * profesor.
 */
public interface AusenciaService {

	/**
	 * Obtiene todas las ausencias registradas.
	 * 
	 * @return una lista de todas las ausencias.
	 */
	List<AusenciaModelo> findAll();

	/**
	 * Encuentra todas las ausencias correspondientes a una fecha específica.
	 * 
	 * @param fecha la fecha de las ausencias.
	 * @return una lista de ausencias para la fecha proporcionada.
	 */
	List<AusenciaModelo> findByFecha(LocalDate fecha);

	/**
	 * Encuentra todas las ausencias asociadas a un profesor específico.
	 * 
	 * @param profesor el profesor cuyas ausencias se desean obtener.
	 * @return una lista de ausencias asociadas al profesor dado.
	 */
	List<AusenciaModelo> findByProfesor(UsuarioModelo profesor);

	/**
	 * Crea una nueva ausencia en el sistema.
	 * 
	 * @param ausencia el objeto de tipo AusenciaModelo que se va a crear.
	 * @return la ausencia creada.
	 */
	AusenciaModelo createAusencia(AusenciaModelo ausencia);

	/**
	 * Encuentra una ausencia por su identificador único.
	 * 
	 * @param idAusencia el identificador único de la ausencia.
	 * @return la ausencia correspondiente al identificador, o null si no existe.
	 */
	AusenciaModelo findAusenciaByIdAusencia(Long idAusencia);

	/**
	 * Actualiza una ausencia existente en el sistema.
	 * 
	 * @param ausencia el objeto de tipo AusenciaModelo con la información
	 *                 actualizada.
	 * @return la ausencia actualizada.
	 */
	AusenciaModelo updateAusencia(AusenciaModelo ausencia);

	/**
	 * Elimina una ausencia del sistema mediante su identificador único.
	 * 
	 * @param idAusencia el identificador único de la ausencia a eliminar.
	 */
	void deleteAusenciaById(Long idAusencia);
}