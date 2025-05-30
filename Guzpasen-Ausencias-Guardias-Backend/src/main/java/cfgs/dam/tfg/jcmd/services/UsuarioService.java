package cfgs.dam.tfg.jcmd.services;

import java.util.List;

import cfgs.dam.tfg.jcmd.dto.UsuarioDTO;
import cfgs.dam.tfg.jcmd.models.UsuarioModelo;

/**
 * Interfaz que define los métodos para la gestión de usuarios en el sistema.
 */
public interface UsuarioService {

	/**
	 * Obtiene una lista con todos los usuarios.
	 * 
	 * @return Lista de objetos UsuarioDTO.
	 */
	List<UsuarioDTO> findAll();

	/**
	 * Obtiene un usuario por su identificador.
	 * 
	 * @param id Identificador del usuario.
	 * @return UsuarioDTO correspondiente al id proporcionado.
	 */
	UsuarioDTO findById(Long id);

	/**
	 * Crea un nuevo usuario en el sistema.
	 * 
	 * @param usuario DTO con la información del usuario a crear.
	 * @return UsuarioDTO del usuario creado.
	 */
	UsuarioDTO createUsuario(UsuarioDTO usuario);

	/**
	 * Obtiene una lista de usuarios filtrados por rol.
	 * 
	 * @param rol Rol que deben tener los usuarios a buscar.
	 * @return Lista de UsuarioDTO con el rol especificado.
	 */
	List<UsuarioDTO> findByRol(UsuarioModelo.Rol rol);
}