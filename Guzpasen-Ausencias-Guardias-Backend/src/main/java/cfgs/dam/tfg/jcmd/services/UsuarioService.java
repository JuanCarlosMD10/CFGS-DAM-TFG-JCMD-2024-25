package cfgs.dam.tfg.jcmd.services;

import java.util.List;

import cfgs.dam.tfg.jcmd.dto.UsuarioDTO;
import cfgs.dam.tfg.jcmd.exceptions.UsuarioNotFoundException;
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
	
	/**
	 * Busca un usuario por nombre y apellidos, generando el "nombre de usuario" dinámico.
	 * 
	 * @param username Nombre de usuario generado.
	 * @return UsuarioModelo encontrado.
	 */
	UsuarioModelo findByGeneratedUsername(String username);
	
	/**
	 * Verifica si existe un usuario con un nombre de usuario generado a partir del nombre y apellidos.
	 *
	 * @param username el nombre de usuario generado.
	 * @return true si existe un usuario con ese nombre de usuario, false en caso contrario.
	 */
	boolean existsByGeneratedUsername(String username);
	
	UsuarioModelo findByEmail(String email) throws UsuarioNotFoundException;
}