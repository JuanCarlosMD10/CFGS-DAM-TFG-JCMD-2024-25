package cfgs.dam.tfg.jcmd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cfgs.dam.tfg.jcmd.models.ZonaModelo;

@Repository
public interface ZonaRepository extends JpaRepository<ZonaModelo, Long> {
	
}