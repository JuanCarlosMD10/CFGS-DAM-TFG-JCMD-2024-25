package cfgs.dam.tfg.jcmd.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cfgs.dam.tfg.jcmd.models.GuardiaModelo;

/**
 * Repositorio para gestionar las operaciones CRUD sobre la entidad
 * GuardiaModelo. Extiende JpaRepository para facilitar la interacción con la
 * base de datos.
 */
@Repository
public interface GuardiaRepository extends JpaRepository<GuardiaModelo, Long> {

	/**
	 * Busca las guardias asignadas en una fecha específica.
	 *
	 * @param fecha Fecha para filtrar las guardias.
	 * @return Lista de guardias asignadas en la fecha indicada.
	 */
	List<GuardiaModelo> findByFecha(LocalDate fecha);

	/**
	 * Recupera todas las guardias registradas.
	 *
	 * @return Lista completa de todas las guardias.
	 */
	List<GuardiaModelo> findAll();
}