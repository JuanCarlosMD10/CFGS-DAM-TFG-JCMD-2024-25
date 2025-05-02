package cfgs.dam.tfg.jcmd.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cfgs.dam.tfg.jcmd.exceptions.UsuarioNotFoundException;
import cfgs.dam.tfg.jcmd.models.UsuarioModelo;
import cfgs.dam.tfg.jcmd.repositories.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public List<UsuarioModelo> findAll() {
		return usuarioRepository.findAll();
	}

	@Override
	public UsuarioModelo findById(Long id) {
		return usuarioRepository.findById(id)
				.orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con ID: " + id));
	}

	@Override
	public UsuarioModelo createUsuario(UsuarioModelo usuario) {
		return usuarioRepository.save(usuario);
	}
}