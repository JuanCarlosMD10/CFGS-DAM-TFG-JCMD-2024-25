package cfgs.dam.tfg.jcmd.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cfgs.dam.tfg.jcmd.models.UsuarioModelo;
import cfgs.dam.tfg.jcmd.services.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping
	public List<UsuarioModelo> getAllUsuarios() {
		return usuarioService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<UsuarioModelo> getUsuarioById(@PathVariable Long id) {
		return ResponseEntity.ok(usuarioService.findById(id));
	}

	@PostMapping
	public ResponseEntity<UsuarioModelo> createUsuario(@RequestBody UsuarioModelo usuario) {
		return ResponseEntity.ok(usuarioService.createUsuario(usuario));
	}
}