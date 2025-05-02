package cfgs.dam.tfg.jcmd.services;

import java.util.List;

import cfgs.dam.tfg.jcmd.models.ZonaModelo;

public interface ZonaService {
	List<ZonaModelo> findAll();

	ZonaModelo findById(Long id);

	ZonaModelo createZona(ZonaModelo zona);
}