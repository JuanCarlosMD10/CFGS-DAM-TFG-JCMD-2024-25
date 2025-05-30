package cfgs.dam.tfg.jcmd.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cfgs.dam.tfg.jcmd.exceptions.ZonaNotFoundException;
import cfgs.dam.tfg.jcmd.models.ZonaModelo;
import cfgs.dam.tfg.jcmd.repositories.ZonaRepository;

/**
 * Implementación de la interfaz ZonaService que gestiona las operaciones
 * relacionadas con las zonas.
 * 
 * Proporciona métodos para obtener todas las zonas, buscar una zona por su ID y
 * crear nuevas zonas utilizando el repositorio ZonaRepository.
 */
@Service
public class ZonaServiceImpl implements ZonaService {

	@Autowired
	private ZonaRepository zonaRepository;

	/**
	 * Obtiene todas las zonas disponibles en el sistema.
	 * 
	 * @return lista con todas las zonas almacenadas.
	 */
	@Override
	public List<ZonaModelo> findAll() {
		return zonaRepository.findAll();
	}

	/**
	 * Busca una zona por su identificador.
	 * 
	 * @param id identificador único de la zona.
	 * @return la zona correspondiente al identificador proporcionado.
	 * @throws ZonaNotFoundException si no se encuentra una zona con el ID dado.
	 */
	@Override
	public ZonaModelo findById(Long id) {
		return zonaRepository.findById(id)
				.orElseThrow(() -> new ZonaNotFoundException("Zona no encontrada con ID: " + id));
	}

	/**
	 * Crea y guarda una nueva zona en el repositorio.
	 * 
	 * @param zona el objeto ZonaModelo a guardar.
	 * @return la zona guardada, incluyendo su ID generado.
	 */
	@Override
	public ZonaModelo createZona(ZonaModelo zona) {
		return zonaRepository.save(zona);
	}
}