package cfgs.dam.tfg.jcmd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cfgs.dam.tfg.jcmd.models.ZonaModelo;

/**
 * Repositorio para gestionar las operaciones CRUD sobre la entidad ZonaModelo.
 * Extiende JpaRepository para proporcionar métodos estándar de acceso a datos.
 */
@Repository
public interface ZonaRepository extends JpaRepository<ZonaModelo, Long> {

}