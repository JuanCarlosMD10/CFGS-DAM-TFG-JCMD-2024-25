package cfgs.dam.tfg.jcmd.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cfgs.dam.tfg.jcmd.dto.AusenciaDTO;
import cfgs.dam.tfg.jcmd.exceptions.AusenciaNotFoundException;
import cfgs.dam.tfg.jcmd.models.AusenciaModelo;
import cfgs.dam.tfg.jcmd.repositories.AusenciaRepository;

/**
 * Implementación del servicio para la gestión de ausencias. Proporciona
 * operaciones para registrar, modificar, eliminar y consultar ausencias.
 */
@Service
public class AusenciaServiceImpl implements AusenciaService {

	@Autowired
	private AusenciaRepository ausenciaRepository;

	/**
	 * Registra una nueva ausencia y devuelve su DTO.
	 * 
	 * @param ausencia AusenciaModelo a registrar.
	 * @return DTO con los datos de la ausencia registrada.
	 * @throws AusenciaNotFoundException si existe una ausencia solapada.
	 */
	@Override
	public AusenciaDTO registrarAusencia(AusenciaModelo ausencia) {
		if (existeSolapamiento(ausencia)) {
			throw new AusenciaNotFoundException("Ya existe una ausencia registrada en el mismo horario.");
		}
		AusenciaModelo saved = ausenciaRepository.save(ausencia);
		return convertToDTO(saved);
	}

	/**
	 * Modifica una ausencia existente y devuelve su DTO actualizado.
	 * 
	 * @param id       Identificador de la ausencia a modificar.
	 * @param ausencia Modelo con los datos actualizados.
	 * @return DTO con los datos de la ausencia modificada.
	 * @throws RuntimeException si la ausencia no existe.
	 */
	@Override
	public AusenciaDTO modificarAusencia(Long id, AusenciaModelo ausencia) {
		Optional<AusenciaModelo> existingAusencia = ausenciaRepository.findById(id);
		if (existingAusencia.isPresent()) {
			AusenciaModelo updatedAusencia = existingAusencia.get();
			updatedAusencia.setFecha(ausencia.getFecha());
			updatedAusencia.setHoraInicio(ausencia.getHoraInicio());
			updatedAusencia.setHoraFin(ausencia.getHoraFin());
			updatedAusencia.setMotivo(ausencia.getMotivo());
			updatedAusencia.setTareaAlumnado(ausencia.getTareaAlumnado());

			AusenciaModelo saved = ausenciaRepository.save(updatedAusencia);
			return convertToDTO(saved);
		}
		throw new RuntimeException("Ausencia no encontrada.");
	}

	/**
	 * Elimina una ausencia por su id.
	 * 
	 * @param id Identificador de la ausencia a eliminar.
	 * @throws RuntimeException si la ausencia no existe.
	 */
	@Override
	public void eliminarAusencia(Long id) {
		Optional<AusenciaModelo> existingAusencia = ausenciaRepository.findById(id);
		if (existingAusencia.isPresent()) {
			ausenciaRepository.deleteById(id);
		} else {
			throw new RuntimeException("Ausencia no encontrada.");
		}
	}

	/**
	 * Consulta las ausencias filtradas por estado.
	 * 
	 * @param estado Estado para filtrar las ausencias, puede ser null para obtener
	 *               todas.
	 * @return Lista de DTOs con las ausencias encontradas.
	 */
	@Override
	public List<AusenciaDTO> consultarAusencias(AusenciaModelo.Estado estado) {
		List<AusenciaModelo> ausencias;
		if (estado != null) {
			ausencias = ausenciaRepository.findByEstado(estado);
		} else {
			ausencias = ausenciaRepository.findAll();
		}
		return ausencias.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	/**
	 * Obtiene una ausencia por su id.
	 * 
	 * @param id Identificador de la ausencia.
	 * @return DTO con los datos de la ausencia.
	 * @throws RuntimeException si la ausencia no existe.
	 */
	@Override
	public AusenciaDTO obtenerAusencia(Long id) {
		Optional<AusenciaModelo> optionalAusencia = ausenciaRepository.findById(id);
		if (optionalAusencia.isPresent()) {
			return convertToDTO(optionalAusencia.get());
		} else {
			throw new RuntimeException("Ausencia no encontrada");
		}
	}

	/**
	 * Verifica si existe una ausencia que se solape con la ausencia proporcionada.
	 * 
	 * @param ausencia AusenciaModelo a verificar.
	 * @return true si hay solapamiento, false en caso contrario.
	 */
	private boolean existeSolapamiento(AusenciaModelo ausencia) {
		List<AusenciaModelo> ausenciasExistentes = ausenciaRepository
				.findByIdProfesorAndFechaAndHoraInicioBeforeAndHoraFinAfter(ausencia.getIdProfesor(),
						ausencia.getFecha(), ausencia.getHoraInicio(), ausencia.getHoraFin());

		for (AusenciaModelo existente : ausenciasExistentes) {
			if (!existente.getIdAusencia().equals(ausencia.getIdAusencia())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Obtiene todas las ausencias asociadas a un profesor.
	 * 
	 * @param idProfesor Identificador del profesor.
	 * @return Lista de DTOs con las ausencias del profesor.
	 */
	@Override
	public List<AusenciaDTO> obtenerAusenciasPorProfesor(Long idProfesor) {
		List<AusenciaModelo> ausencias = ausenciaRepository.findByIdProfesor_IdUsuario(idProfesor);
		return ausencias.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	/**
	 * Convierte una entidad AusenciaModelo a su DTO correspondiente.
	 * 
	 * @param ausencia Entidad AusenciaModelo.
	 * @return DTO con los datos relevantes de la ausencia.
	 */
	private AusenciaDTO convertToDTO(AusenciaModelo ausencia) {
		AusenciaDTO dto = new AusenciaDTO();
		dto.setIdAusencia(ausencia.getIdAusencia());
		dto.setEstado(ausencia.getEstado().toString());
		dto.setFecha(ausencia.getFecha());
		dto.setHoraInicio(ausencia.getHoraInicio());
		dto.setHoraFin(ausencia.getHoraFin());
		if (ausencia.getIdProfesor() != null) {
			dto.setIdProfesor(ausencia.getIdProfesor().getIdUsuario());
			dto.setNombreProfesor(ausencia.getIdProfesor().getNombre() + " " + ausencia.getIdProfesor().getApellidos());
		}
		dto.setMotivo(ausencia.getMotivo());
		dto.setTareaAlumnado(ausencia.getTareaAlumnado());
		return dto;
	}
}