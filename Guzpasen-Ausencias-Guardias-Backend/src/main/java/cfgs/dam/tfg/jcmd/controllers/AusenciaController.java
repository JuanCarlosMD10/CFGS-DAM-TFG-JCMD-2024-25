package cfgs.dam.tfg.jcmd.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cfgs.dam.tfg.jcmd.models.AusenciaModelo;
import cfgs.dam.tfg.jcmd.services.AusenciaService;

/**
 * Controlador REST para gestionar las ausencias del profesorado. Permite
 * obtener todas las ausencias, crear una nueva o buscar por ID.
 */
@RestController
@RequestMapping("/api/ausencias")
public class AusenciaController {

	@Autowired
	private AusenciaService ausenciaService;

	/**
	 * Devuelve una lista con todas las ausencias registradas.
	 */
	@GetMapping
	public List<AusenciaModelo> obtenerTodasLasAusencias() {
		return ausenciaService.findAll();
	}

	/**
	 * Crea una nueva ausencia a partir de los datos recibidos.
	 *
	 * @param ausencia objeto con los datos de la ausencia
	 * @return la ausencia creada
	 */
	@PostMapping
	public ResponseEntity<AusenciaModelo> crearAusencia(@RequestBody AusenciaModelo ausencia) {
		AusenciaModelo nuevaAusencia = ausenciaService.createAusencia(ausencia);
		return ResponseEntity.ok(nuevaAusencia);
	}

	/**
	 * Busca una ausencia concreta por su identificador.
	 *
	 * @param id identificador de la ausencia
	 * @return la ausencia encontrada, si existe
	 */
	@GetMapping("/{id}")
	public ResponseEntity<AusenciaModelo> buscarAusenciaPorId(@PathVariable Long id) {
		AusenciaModelo ausencia = ausenciaService.findAusenciaByIdAusencia(id);
		return ResponseEntity.ok(ausencia);
	}
}