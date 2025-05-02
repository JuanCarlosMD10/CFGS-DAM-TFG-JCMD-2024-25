package cfgs.dam.tfg.jcmd.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cfgs.dam.tfg.jcmd.models.GuardiaModelo;
import cfgs.dam.tfg.jcmd.services.GuardiaService;

/**
 * Controlador REST encargado de gestionar las guardias asignadas a los
 * profesores. Permite consultar todas las guardias, registrar una nueva y
 * buscar una por su ID.
 */
@RestController
@RequestMapping("/api/guardias")
public class GuardiaController {

	@Autowired
	private GuardiaService guardiaService;

	/**
	 * Devuelve todas las guardias almacenadas en el sistema.
	 */
	@GetMapping
	public List<GuardiaModelo> obtenerTodasLasGuardias() {
		return guardiaService.findAll();
	}

	/**
	 * Registra una nueva guardia con los datos proporcionados.
	 *
	 * @param guardia datos de la guardia a crear
	 * @return la guardia creada
	 */
	@PostMapping
	public ResponseEntity<GuardiaModelo> crearGuardia(@RequestBody GuardiaModelo guardia) {
		GuardiaModelo nuevaGuardia = guardiaService.createGuardia(guardia);
		return ResponseEntity.ok(nuevaGuardia);
	}

	/**
	 * Busca una guardia concreta a partir de su identificador.
	 *
	 * @param id identificador de la guardia
	 * @return la guardia encontrada, si existe
	 */
	@GetMapping("/{id}")
	public ResponseEntity<GuardiaModelo> buscarGuardiaPorId(@PathVariable Long id) {
		GuardiaModelo guardia = guardiaService.findGuardiaByIdGuardia(id);
		return ResponseEntity.ok(guardia);
	}
}