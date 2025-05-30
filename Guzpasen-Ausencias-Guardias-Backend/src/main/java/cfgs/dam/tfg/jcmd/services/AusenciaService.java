package cfgs.dam.tfg.jcmd.services;

import java.util.List;

import cfgs.dam.tfg.jcmd.dto.AusenciaDTO;
import cfgs.dam.tfg.jcmd.models.AusenciaModelo;

/**
 * Interfaz para el servicio de gestión de ausencias. Define las operaciones
 * disponibles para registrar, modificar, eliminar y consultar ausencias.
 */
public interface AusenciaService {

	/**
	 * Registra una nueva ausencia en el sistema.
	 *
	 * @param ausencia Modelo de ausencia a registrar.
	 * @return DTO con los datos de la ausencia registrada.
	 */
	AusenciaDTO registrarAusencia(AusenciaModelo ausencia);

	/**
	 * Modifica una ausencia existente identificada por su id.
	 *
	 * @param id       Identificador de la ausencia a modificar.
	 * @param ausencia Modelo con los datos actualizados de la ausencia.
	 * @return DTO con los datos de la ausencia modificada.
	 */
	AusenciaDTO modificarAusencia(Long id, AusenciaModelo ausencia);

	/**
	 * Elimina una ausencia por su identificador.
	 *
	 * @param id Identificador de la ausencia a eliminar.
	 */
	void eliminarAusencia(Long id);

	/**
	 * Consulta las ausencias filtrando por su estado.
	 *
	 * @param estado Estado para filtrar las ausencias.
	 * @return Lista de DTOs con las ausencias que cumplen el filtro.
	 */
	List<AusenciaDTO> consultarAusencias(AusenciaModelo.Estado estado);

	/**
	 * Obtiene una ausencia por su identificador.
	 *
	 * @param id Identificador de la ausencia.
	 * @return DTO con los datos de la ausencia.
	 */
	AusenciaDTO obtenerAusencia(Long id);

	/**
	 * Obtiene todas las ausencias asociadas a un profesor específico.
	 *
	 * @param idProfesor Identificador del profesor.
	 * @return Lista de DTOs con las ausencias del profesor.
	 */
	List<AusenciaDTO> obtenerAusenciasPorProfesor(Long idProfesor);
}