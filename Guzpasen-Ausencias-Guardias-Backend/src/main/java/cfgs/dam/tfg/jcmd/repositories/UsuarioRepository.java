package cfgs.dam.tfg.jcmd.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cfgs.dam.tfg.jcmd.models.UsuarioModelo;
import cfgs.dam.tfg.jcmd.models.UsuarioModelo.Rol;

/**
 * Repositorio para gestionar las operaciones CRUD sobre la entidad
 * UsuarioModelo. Extiende JpaRepository para facilitar la interacción con la
 * base de datos.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModelo, Long> {

	/**
	 * Busca un usuario por su correo electrónico.
	 *
	 * @param email Correo electrónico del usuario.
	 * @return UsuarioModelo que coincide con el correo o null si no existe.
	 */
	Optional<UsuarioModelo> findByEmail(String email);

	/**
	 * Comprueba si existe un usuario con el correo electrónico dado.
	 *
	 * @param email Correo electrónico a verificar.
	 * @return true si existe un usuario con el email, false en caso contrario.
	 */
	boolean existsByEmail(String email);

	/**
	 * Obtiene una lista de usuarios que tienen un rol específico.
	 *
	 * @param rol Rol de los usuarios a buscar.
	 * @return Lista de usuarios con el rol especificado.
	 */
	List<UsuarioModelo> findByRol(Rol rol);
}