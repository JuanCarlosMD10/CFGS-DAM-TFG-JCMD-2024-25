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

import cfgs.dam.tfg.jcmd.models.ZonaModelo;
import cfgs.dam.tfg.jcmd.services.ZonaService;

/**
 * Controlador REST para la gestión de zonas. Proporciona endpoints para obtener
 * todas las zonas, buscar por ID y crear nuevas zonas.
 */
@RestController
@RequestMapping("/api/zonas")
public class ZonaController {

	@Autowired
	private ZonaService zonaService;

	/**
	 * Obtiene la lista completa de zonas registradas en el sistema.
	 *
	 * @return Lista de objetos ZonaModelo.
	 */
	@GetMapping
	public List<ZonaModelo> getAllZonas() {
		return zonaService.findAll();
	}

	/**
	 * Obtiene una zona específica mediante su ID.
	 *
	 * @param id ID de la zona.
	 * @return ResponseEntity con el objeto ZonaModelo si se encuentra.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<ZonaModelo> getZonaById(@PathVariable Long id) {
		return ResponseEntity.ok(zonaService.findById(id));
	}

	/**
	 * Crea una nueva zona en el sistema.
	 *
	 * @param zona Objeto ZonaModelo con los datos de la zona a crear.
	 * @return ResponseEntity con el objeto ZonaModelo creado.
	 */
	@PostMapping
	public ResponseEntity<ZonaModelo> createZona(@RequestBody ZonaModelo zona) {
		return ResponseEntity.ok(zonaService.createZona(zona));
	}
}