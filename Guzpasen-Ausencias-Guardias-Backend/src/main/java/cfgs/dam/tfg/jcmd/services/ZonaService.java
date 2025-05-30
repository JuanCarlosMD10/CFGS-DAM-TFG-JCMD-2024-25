package cfgs.dam.tfg.jcmd.services;

import java.util.List;

import cfgs.dam.tfg.jcmd.models.ZonaModelo;

/**
 * Interfaz que define los métodos para la gestión de zonas. Permite obtener
 * todas las zonas, buscar una zona por su ID y crear nuevas zonas.
 */
public interface ZonaService {

	/**
	 * Obtiene una lista con todas las zonas disponibles.
	 * 
	 * @return una lista de objetos ZonaModelo que representan todas las zonas.
	 */
	List<ZonaModelo> findAll();

	/**
	 * Busca una zona por su identificador único.
	 * 
	 * @param id el identificador de la zona a buscar.
	 * @return el objeto ZonaModelo correspondiente al id proporcionado.
	 */
	ZonaModelo findById(Long id);

	/**
	 * Crea una nueva zona y la guarda en la base de datos.
	 * 
	 * @param zona el objeto ZonaModelo que se desea crear.
	 * @return la zona creada con su identificador generado.
	 */
	ZonaModelo createZona(ZonaModelo zona);
}