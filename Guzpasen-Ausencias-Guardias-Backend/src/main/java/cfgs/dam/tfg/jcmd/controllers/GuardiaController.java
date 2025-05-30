package cfgs.dam.tfg.jcmd.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cfgs.dam.tfg.jcmd.dto.GuardiaDTO;
import cfgs.dam.tfg.jcmd.services.GuardiaService;

/**
 * Controlador REST para la gestión de guardias de profesores. Proporciona
 * endpoints para registrar nuevas guardias, consultar por fecha y obtener
 * información detallada de una guardia específica.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/guardias")
public class GuardiaController {

	@Autowired
	private GuardiaService guardiaService;

	/**
	 * Asigna una nueva guardia a un profesor.
	 *
	 * @param guardia Objeto GuardiaDTO con los datos de la guardia a registrar.
	 * @return GuardiaDTO con los datos de la guardia registrada.
	 */
	@PostMapping("/registrar")
	public GuardiaDTO registrarGuardia(@RequestBody GuardiaDTO guardia) {
		return guardiaService.asignarGuardia(guardia);
	}

	/**
	 * Consulta las guardias de los profesores. Si se proporciona una fecha, filtra
	 * las guardias por esa fecha; si no, devuelve todas.
	 *
	 * @param fecha (Opcional) Fecha en formato ISO (yyyy-MM-dd).
	 * @return Lista de GuardiaDTO que coinciden con el filtro aplicado.
	 */
	@GetMapping("/consultar")
	@CrossOrigin(origins = "*")
	public List<GuardiaDTO> consultarGuardias(@RequestParam(required = false) String fecha) {
		if (fecha != null) {
			LocalDate fechaParsed = LocalDate.parse(fecha);
			return guardiaService.consultarGuardias(fechaParsed);
		} else {
			return guardiaService.consultarGuardias(null);
		}
	}

	/**
	 * Obtiene la información de una guardia específica mediante su ID.
	 *
	 * @param id ID de la guardia.
	 * @return GuardiaDTO con los datos de la guardia solicitada.
	 */
	@GetMapping("/{id}")
	public GuardiaDTO obtenerGuardia(@PathVariable Long id) {
		return guardiaService.obtenerGuardia(id);
	}
}