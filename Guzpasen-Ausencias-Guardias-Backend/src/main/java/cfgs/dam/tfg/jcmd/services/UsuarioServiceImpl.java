package cfgs.dam.tfg.jcmd.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cfgs.dam.tfg.jcmd.dto.UsuarioDTO;
import cfgs.dam.tfg.jcmd.exceptions.UsuarioNotFoundException;
import cfgs.dam.tfg.jcmd.models.UsuarioModelo;
import cfgs.dam.tfg.jcmd.repositories.UsuarioRepository;

/**
 * Implementación del servicio para la gestión de usuarios. Proporciona métodos
 * para buscar, crear y filtrar usuarios.
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	/**
	 * Convierte una entidad UsuarioModelo a su correspondiente DTO UsuarioDTO.
	 * 
	 * @param usuario la entidad UsuarioModelo que se desea convertir.
	 * @return el objeto UsuarioDTO con los datos convertidos de la entidad.
	 */
	private UsuarioDTO convertirADTO(UsuarioModelo usuario) {
		return new UsuarioDTO(usuario.getIdUsuario(), usuario.getNombre(), usuario.getApellidos(), usuario.getEmail(),
				usuario.getRol(), usuario.getUsuarioMovil());
	}

	/**
	 * Convierte un DTO UsuarioDTO a su correspondiente entidad UsuarioModelo.
	 * 
	 * @param usuarioDTO el DTO UsuarioDTO que se desea convertir.
	 * @return la entidad UsuarioModelo creada a partir del DTO.
	 */
	private UsuarioModelo convertirAEntidad(UsuarioDTO usuarioDTO) {
		UsuarioModelo usuario = new UsuarioModelo();
		usuario.setIdUsuario(usuarioDTO.getIdUsuario());
		usuario.setNombre(usuarioDTO.getNombre());
		usuario.setApellidos(usuarioDTO.getApellidos());
		usuario.setEmail(usuarioDTO.getEmail());
		usuario.setRol(usuarioDTO.getRol());
		usuario.setUsuarioMovil(usuarioDTO.getUsuarioMovil());
		return usuario;
	}

	/**
	 * Obtiene una lista de todos los usuarios del sistema, convertidos a DTOs.
	 * 
	 * @return una lista de UsuarioDTO que representan todos los usuarios
	 *         registrados.
	 */
	@Override
	public List<UsuarioDTO> findAll() {
		return usuarioRepository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList());
	}

	/**
	 * Busca un usuario por su identificador.
	 * 
	 * @param id el identificador único del usuario a buscar.
	 * @return el UsuarioDTO correspondiente al usuario encontrado.
	 * @throws UsuarioNotFoundException si no existe un usuario con el ID
	 *                                  proporcionado.
	 */
	@Override
	public UsuarioDTO findById(Long id) {
		UsuarioModelo usuario = usuarioRepository.findById(id)
				.orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con ID: " + id));
		return convertirADTO(usuario);
	}

	/**
	 * Crea un nuevo usuario a partir de un DTO y lo guarda en la base de datos.
	 * 
	 * @param usuarioDTO el DTO que contiene la información del usuario a crear.
	 * @return el UsuarioDTO correspondiente al usuario recién creado.
	 */
	@Override
	public UsuarioDTO createUsuario(UsuarioDTO usuarioDTO) {
		UsuarioModelo usuarioEntidad = convertirAEntidad(usuarioDTO);
		UsuarioModelo usuarioGuardado = usuarioRepository.save(usuarioEntidad);
		return convertirADTO(usuarioGuardado);
	}

	/**
	 * Busca usuarios por su rol específico.
	 * 
	 * @param rol el rol que deben tener los usuarios buscados.
	 * @return una lista de UsuarioDTO que corresponden a los usuarios con el rol
	 *         indicado.
	 */
	@Override
	public List<UsuarioDTO> findByRol(UsuarioModelo.Rol rol) {
		return usuarioRepository.findByRol(rol).stream().map(this::convertirADTO).collect(Collectors.toList());
	}
}