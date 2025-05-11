package cfgs.dam.tfg.jcmd.services;

import java.util.List;

import cfgs.dam.tfg.jcmd.models.UsuarioModelo;

public interface UsuarioService {
	List<UsuarioModelo> findAll();

	UsuarioModelo findById(Long id);

	UsuarioModelo createUsuario(UsuarioModelo usuario);
}