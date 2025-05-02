package cfgs.dam.tfg.jcmd.repositories;

import cfgs.dam.tfg.jcmd.models.AusenciaModelo;
import cfgs.dam.tfg.jcmd.models.UsuarioModelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz que actúa como repositorio para las ausencias. Extiende
 * JpaRepository para proporcionar operaciones CRUD sobre la entidad
 * AusenciaModelo.
 * 
 * Esta interfaz incluye métodos para obtener las ausencias según diferentes
 * criterios, como la fecha o el profesor asignado.
 */
@Repository
public interface AusenciaRepository extends JpaRepository<AusenciaModelo, Long> {

	/**
	 * Obtiene todas las ausencias registradas en el sistema.
	 * 
	 * @return una lista con todas las ausencias.
	 */
	List<AusenciaModelo> findAll();

	/**
	 * Encuentra todas las ausencias correspondientes a una fecha específica.
	 * 
	 * @param fecha la fecha de las ausencias.
	 * @return una lista de ausencias para la fecha proporcionada.
	 */
	List<AusenciaModelo> findByFecha(LocalDate fecha);

	/**
	 * Encuentra todas las ausencias asociadas a un profesor.
	 * 
	 * @param profesor el profesor cuyas ausencias se desean obtener.
	 * @return una lista de ausencias asociadas al profesor dado.
	 */
	List<AusenciaModelo> findByIdProfesor(UsuarioModelo profesor);

	/**
	 * Encuentra una ausencia por su identificador único.
	 * 
	 * @param idAusencia el identificador único de la ausencia.
	 * @return la ausencia correspondiente al identificador, o null si no existe.
	 */
	AusenciaModelo findAusenciaModeloByIdAusencia(Long idAusencia);
}