package cfgs.dam.tfg.jcmd.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cfgs.dam.tfg.jcmd.dto.GuardiaDTO;
import cfgs.dam.tfg.jcmd.models.AusenciaModelo;
import cfgs.dam.tfg.jcmd.models.GuardiaModelo;
import cfgs.dam.tfg.jcmd.models.UsuarioModelo;
import cfgs.dam.tfg.jcmd.models.ZonaModelo;
import cfgs.dam.tfg.jcmd.repositories.AusenciaRepository;
import cfgs.dam.tfg.jcmd.repositories.GuardiaRepository;
import cfgs.dam.tfg.jcmd.repositories.UsuarioRepository;
import cfgs.dam.tfg.jcmd.repositories.ZonaRepository;

@Service
public class GuardiaServiceImpl implements GuardiaService {

	@Autowired
	private GuardiaRepository guardiaRepository;

	@Autowired
	private AusenciaRepository ausenciaRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ZonaRepository zonaRepository;

	/**
	 * Asigna una nueva guardia, actualizando el estado de la ausencia asociada.
	 *
	 * @param guardiaDTO DTO con los datos de la guardia a asignar.
	 * @return DTO de la guardia asignada.
	 * @throws IllegalArgumentException si no se encuentra la ausencia o la zona
	 *                                  asociada.
	 */
	@Override
	public GuardiaDTO asignarGuardia(GuardiaDTO guardiaDTO) {
		if (guardiaDTO.getFecha() == null) {
			Optional<AusenciaModelo> ausenciaOpt = ausenciaRepository.findById(guardiaDTO.getIdAusencia());
			if (ausenciaOpt.isPresent()) {
				guardiaDTO.setFecha(ausenciaOpt.get().getFecha());
			} else {
				throw new IllegalArgumentException("No se encontró la ausencia con ID: " + guardiaDTO.getIdAusencia());
			}
		}

		Optional<AusenciaModelo> ausenciaOpt = ausenciaRepository.findById(guardiaDTO.getIdAusencia());
		if (ausenciaOpt.isEmpty()) {
			throw new IllegalArgumentException("No se encontró la ausencia con ID: " + guardiaDTO.getIdAusencia());
		}

		AusenciaModelo ausencia = ausenciaOpt.get();

		if (guardiaDTO.getIdAula() == null) {
			Long idZona = ausencia.getIdZona().getIdZona();

			Optional<ZonaModelo> zonaOpt = zonaRepository.findById(idZona);
			if (zonaOpt.isPresent()) {
				guardiaDTO.setIdAula(zonaOpt.get().getIdZona());
			} else {
				throw new IllegalArgumentException("No se encontró una zona con ID: " + idZona);
			}
		}

		ausencia.setEstado(AusenciaModelo.Estado.GUARDIA_ASIGNADA);
		ausenciaRepository.save(ausencia);

		GuardiaModelo guardia = convertToEntity(guardiaDTO);
		GuardiaModelo guardiaGuardada = guardiaRepository.save(guardia);
		return convertToDTO(guardiaGuardada);
	}

	/**
	 * Consulta guardias filtrando por fecha.
	 *
	 * @param fecha Fecha para filtrar las guardias, puede ser null para obtener
	 *              todas.
	 * @return Lista de DTOs de guardias.
	 */
	@Override
	public List<GuardiaDTO> consultarGuardias(LocalDate fecha) {
		List<GuardiaModelo> guardias;

		if (fecha != null) {
			guardias = guardiaRepository.findByFecha(fecha);
		} else {
			guardias = guardiaRepository.findAll();
		}

		return guardias.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	/**
	 * Obtiene una guardia por su id.
	 *
	 * @param id Identificador de la guardia.
	 * @return DTO de la guardia encontrada.
	 * @throws RuntimeException si no se encuentra la guardia.
	 */
	@Override
	public GuardiaDTO obtenerGuardia(Long id) {
		Optional<GuardiaModelo> guardia = guardiaRepository.findById(id);
		if (guardia.isPresent()) {
			return convertToDTO(guardia.get());
		} else {
			throw new RuntimeException("Guardia no encontrada");
		}
	}

	/**
	 * Convierte una entidad GuardiaModelo a su DTO.
	 *
	 * @param guardia Entidad GuardiaModelo.
	 * @return DTO correspondiente.
	 */
	private GuardiaDTO convertToDTO(GuardiaModelo guardia) {
		GuardiaDTO dto = new GuardiaDTO();
		dto.setIdGuardia(guardia.getIdGuardia());
		dto.setFecha(guardia.getFecha());
		dto.setHoraInicio(guardia.getHoraInicio() != null ? guardia.getHoraInicio().name() : null);
		dto.setHoraFin(guardia.getHoraFin() != null ? guardia.getHoraFin().name() : null);

		if (guardia.getIdProfesor() != null) {
			dto.setIdProfesor(guardia.getIdProfesor().getIdUsuario());
			dto.setNombreProfesor(guardia.getIdProfesor().getNombre() + " " + guardia.getIdProfesor().getApellidos());
		}

		if (guardia.getIdAula() != null) {
			dto.setIdAula(guardia.getIdAula().getIdZona());
			dto.setNombreAula(guardia.getIdAula().getNombre());
		}

		if (guardia.getIdAusencia() != null) {
			dto.setIdAusencia(guardia.getIdAusencia().getIdAusencia());
		}

		return dto;
	}

	/**
	 * Convierte un DTO GuardiaDTO a la entidad GuardiaModelo.
	 *
	 * @param dto DTO GuardiaDTO.
	 * @return Entidad GuardiaModelo.
	 * @throws RuntimeException si no se encuentra el profesor, aula o ausencia
	 *                          indicados.
	 */
	private GuardiaModelo convertToEntity(GuardiaDTO dto) {
		GuardiaModelo guardia = new GuardiaModelo();
		guardia.setIdGuardia(dto.getIdGuardia());
		guardia.setFecha(dto.getFecha());

		if (dto.getHoraInicio() != null) {
			guardia.setHoraInicio(GuardiaModelo.HoraInicio.valueOf(dto.getHoraInicio()));
		} else {
			guardia.setHoraInicio(null);
		}

		if (dto.getHoraFin() != null) {
			guardia.setHoraFin(GuardiaModelo.HoraFin.valueOf(dto.getHoraFin()));
		} else {
			guardia.setHoraFin(null);
		}

		if (dto.getIdProfesor() != null) {
			UsuarioModelo profesor = usuarioRepository.findById(dto.getIdProfesor())
					.orElseThrow(() -> new RuntimeException("Profesor no encontrado"));
			guardia.setIdProfesor(profesor);
		}

		if (dto.getIdAula() != null) {
			ZonaModelo aula = zonaRepository.findById(dto.getIdAula())
					.orElseThrow(() -> new RuntimeException("Aula no encontrada"));
			guardia.setIdAula(aula);
		}

		if (dto.getIdAusencia() != null) {
			AusenciaModelo ausencia = ausenciaRepository.findById(dto.getIdAusencia())
					.orElseThrow(() -> new RuntimeException("Ausencia no encontrada"));
			guardia.setIdAusencia(ausencia);
		}

		return guardia;
	}
}