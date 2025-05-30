package cfgs.dam.tfg.jcmd.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cfgs.dam.tfg.jcmd.dto.UsuarioDTO;
import cfgs.dam.tfg.jcmd.models.UsuarioModelo.Rol;
import cfgs.dam.tfg.jcmd.services.UsuarioService;
import lombok.RequiredArgsConstructor;

/**
 * Controlador REST para la gestión de usuarios. Permite crear nuevos usuarios y
 * consultar la lista de profesores registrados.
 */
@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

	private final UsuarioService usuarioService;

	/**
	 * Crea un nuevo usuario en el sistema.
	 *
	 * @param usuarioDTO Objeto con los datos del usuario a crear.
	 * @return UsuarioDTO con la información del usuario creado.
	 */
	@PostMapping
	public UsuarioDTO crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
		return usuarioService.createUsuario(usuarioDTO);
	}

	/**
	 * Obtiene una lista de todos los usuarios con rol de profesor.
	 *
	 * @return Lista de UsuarioDTO correspondientes a profesores.
	 */
	@GetMapping("/profesores")
	public List<UsuarioDTO> obtenerProfesores() {
		return usuarioService.findByRol(Rol.PROFESOR);
	}
}