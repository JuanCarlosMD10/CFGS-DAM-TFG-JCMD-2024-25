package cfgs.dam.tfg.jcmd.repositories;

import cfgs.dam.tfg.jcmd.models.UsuarioModelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModelo, Long> {
	UsuarioModelo findByEmail(String email);
}