package cfgs.dam.tfg.jcmd.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cfgs.dam.tfg.jcmd.exceptions.AusenciaCreationException;
import cfgs.dam.tfg.jcmd.exceptions.AusenciaNotFoundException;
import cfgs.dam.tfg.jcmd.models.AusenciaModelo;
import cfgs.dam.tfg.jcmd.models.UsuarioModelo;
import cfgs.dam.tfg.jcmd.repositories.AusenciaRepository;

/**
 * Implementación de los servicios relacionados con la gestión de ausencias.
 * 
 * Esta clase maneja la lógica de negocio relacionada con las ausencias, tales
 * como la creación, actualización, eliminación y consulta de ausencias. Los
 * métodos interactúan con el repositorio para almacenar y recuperar los datos
 * de las ausencias.
 */
@Service
public class AusenciaServiceImpl implements AusenciaService {

	@Autowired
	private AusenciaRepository ausenciaRepository;

	/**
	 * Obtiene todas las ausencias registradas.
	 * 
	 * @return una lista de todas las ausencias.
	 */
	@Override
	public List<AusenciaModelo> findAll() {
		return ausenciaRepository.findAll();
	}

	/**
	 * Encuentra todas las ausencias correspondientes a una fecha específica.
	 * 
	 * @param fecha la fecha de las ausencias.
	 * @return una lista de ausencias para la fecha proporcionada.
	 */
	@Override
	public List<AusenciaModelo> findByFecha(LocalDate fecha) {
		return ausenciaRepository.findByFecha(fecha);
	}

	/**
	 * Encuentra todas las ausencias asociadas a un profesor específico.
	 * 
	 * @param profesor el profesor cuyas ausencias se desean obtener.
	 * @return una lista de ausencias asociadas al profesor dado.
	 */
	@Override
	public List<AusenciaModelo> findByProfesor(UsuarioModelo profesor) {
		return ausenciaRepository.findByIdProfesor(profesor);
	}

	/**
	 * Crea una nueva ausencia en el sistema.
	 * 
	 * @param ausencia el objeto de tipo AusenciaModelo que se va a crear.
	 * @return la ausencia creada.
	 * @throws AusenciaCreationException si ocurre un error al crear la ausencia.
	 */
	@Override
	public AusenciaModelo createAusencia(AusenciaModelo ausencia) {
		try {
			return ausenciaRepository.save(ausencia);
		} catch (Exception e) {
			throw new AusenciaCreationException("Error al crear la ausencia", e);
		}
	}

	/**
	 * Encuentra una ausencia por su identificador único.
	 * 
	 * @param idAusencia el identificador único de la ausencia.
	 * @return la ausencia correspondiente al identificador, o lanza una excepción
	 *         si no se encuentra.
	 * @throws AusenciaNotFoundException si no se encuentra una ausencia con el ID
	 *                                   proporcionado.
	 */
	@Override
	public AusenciaModelo findAusenciaByIdAusencia(Long idAusencia) {
		AusenciaModelo ausencia = ausenciaRepository.findAusenciaModeloByIdAusencia(idAusencia);
		if (ausencia == null) {
			throw new AusenciaNotFoundException("Ausencia no encontrada con ID: " + idAusencia);
		}
		return ausencia;
	}

	/**
	 * Actualiza una ausencia existente en el sistema.
	 * 
	 * @param ausencia el objeto de tipo AusenciaModelo con la información
	 *                 actualizada.
	 * @return la ausencia actualizada.
	 */
	@Override
	public AusenciaModelo updateAusencia(AusenciaModelo ausencia) {
		return ausenciaRepository.save(ausencia);
	}

	/**
	 * Elimina una ausencia del sistema mediante su identificador único.
	 * 
	 * @param idAusencia el identificador único de la ausencia a eliminar.
	 * @throws AusenciaNotFoundException si no se encuentra una ausencia con el ID
	 *                                   proporcionado.
	 */
	@Override
	public void deleteAusenciaById(Long idAusencia) {
		if (!ausenciaRepository.existsById(idAusencia)) {
			throw new AusenciaNotFoundException("No se puede eliminar. Ausencia no encontrada con ID: " + idAusencia);
		}
		ausenciaRepository.deleteById(idAusencia);
	}
}