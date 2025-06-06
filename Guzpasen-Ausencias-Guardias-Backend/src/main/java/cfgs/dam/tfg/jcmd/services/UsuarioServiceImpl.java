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
 * Implementación del servicio de gestión de usuarios.
 * 
 * Proporciona métodos para crear, buscar, filtrar y verificar usuarios en el sistema.
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Convierte una entidad UsuarioModelo a un DTO UsuarioDTO.
     * 
     * @param usuario entidad a convertir.
     * @return DTO resultante.
     */
    private UsuarioDTO convertirADTO(UsuarioModelo usuario) {
        return new UsuarioDTO(
            usuario.getIdUsuario(),
            usuario.getNombre(),
            usuario.getApellidos(),
            usuario.getEmail(),
            usuario.getClave(),
            usuario.getRol(),
            usuario.getUsuarioMovil()
        );
    }

    /**
     * Convierte un DTO UsuarioDTO a una entidad UsuarioModelo.
     * 
     * @param usuarioDTO DTO a convertir.
     * @return entidad resultante.
     */
    private UsuarioModelo convertirAEntidad(UsuarioDTO usuarioDTO) {
        UsuarioModelo usuario = new UsuarioModelo();
        usuario.setIdUsuario(usuarioDTO.getIdUsuario());
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellidos(usuarioDTO.getApellidos());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setClave(usuarioDTO.getClave());
        usuario.setRol(usuarioDTO.getRol());
        usuario.setUsuarioMovil(usuarioDTO.getUsuarioMovil());
        return usuario;
    }

    /**
     * Devuelve todos los usuarios registrados en formato DTO.
     * 
     * @return lista de usuarios.
     */
    @Override
    public List<UsuarioDTO> findAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca un usuario por su ID.
     * 
     * @param id identificador del usuario.
     * @return DTO del usuario encontrado.
     * @throws UsuarioNotFoundException si no existe.
     */
    @Override
    public UsuarioDTO findById(Long id) {
        UsuarioModelo usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con ID: " + id));
        return convertirADTO(usuario);
    }

    /**
     * Crea un nuevo usuario y lo guarda.
     * 
     * @param usuarioDTO datos del usuario.
     * @return DTO del usuario creado.
     */
    @Override
    public UsuarioDTO createUsuario(UsuarioDTO usuarioDTO) {
        UsuarioModelo usuarioEntidad = convertirAEntidad(usuarioDTO);
        UsuarioModelo usuarioGuardado = usuarioRepository.save(usuarioEntidad);
        return convertirADTO(usuarioGuardado);
    }

    /**
     * Devuelve los usuarios que tienen el rol especificado.
     * 
     * @param rol rol por el que filtrar.
     * @return lista de usuarios con ese rol.
     */
    @Override
    public List<UsuarioDTO> findByRol(UsuarioModelo.Rol rol) {
        return usuarioRepository.findByRol(rol)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca un usuario cuyo nombre de usuario generado coincida con el indicado.
     * 
     * @param username nombre generado.
     * @return usuario encontrado.
     * @throws UsuarioNotFoundException si no se encuentra.
     */
    @Override
    public UsuarioModelo findByGeneratedUsername(String username) {
        List<UsuarioModelo> todos = usuarioRepository.findAll();
        for (UsuarioModelo u : todos) {
            String generado = generarNombreUsuario(u.getNombre(), u.getApellidos());
            if (generado.equalsIgnoreCase(username)) {
                return u;
            }
        }
        throw new UsuarioNotFoundException("No se encontró un usuario con nombre generado: " + username);
    }

    /**
     * Genera un nombre de usuario basado en el nombre y los apellidos.
     * 
     * @param nombre nombre del usuario.
     * @param apellidos apellidos del usuario.
     * @return nombre de usuario generado.
     */
    private String generarNombreUsuario(String nombre, String apellidos) {
        if (nombre == null || apellidos == null)
            return null;

        StringBuilder sb = new StringBuilder();
        sb.append(Character.toLowerCase(nombre.charAt(0)));

        String[] partes = apellidos.trim().split("\\s+");
        for (String parte : partes) {
            sb.append(parte.substring(0, Math.min(3, parte.length())).toLowerCase());
        }
        return sb.toString();
    }

    /**
     * Verifica si existe un usuario con el nombre generado indicado.
     * 
     * @param username nombre de usuario generado.
     * @return true si existe, false si no.
     */
    @Override
    public boolean existsByGeneratedUsername(String username) {
        List<UsuarioModelo> usuarios = usuarioRepository.findAll();
        for (UsuarioModelo usuario : usuarios) {
            String nombreGenerado = generarNombreUsuario(usuario.getNombre(), usuario.getApellidos());
            if (nombreGenerado != null && nombreGenerado.equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Busca un usuario por su correo electrónico.
     * 
     * @param email correo electrónico del usuario.
     * @return entidad encontrada.
     * @throws UsuarioNotFoundException si no se encuentra.
     */
    @Override
    public UsuarioModelo findByEmail(String email) throws UsuarioNotFoundException {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNotFoundException("No se encontró usuario con email: " + email));
    }
}