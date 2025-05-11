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

@RestController
@RequestMapping("/api/zonas")
public class ZonaController {

	@Autowired
	private ZonaService zonaService;

	@GetMapping
	public List<ZonaModelo> getAllZonas() {
		return zonaService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ZonaModelo> getZonaById(@PathVariable Long id) {
		return ResponseEntity.ok(zonaService.findById(id));
	}

	@PostMapping
	public ResponseEntity<ZonaModelo> createZona(@RequestBody ZonaModelo zona) {
		return ResponseEntity.ok(zonaService.createZona(zona));
	}
}