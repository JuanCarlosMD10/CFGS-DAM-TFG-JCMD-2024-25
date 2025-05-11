package cfgs.dam.tfg.jcmd.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cfgs.dam.tfg.jcmd.exceptions.ZonaNotFoundException;
import cfgs.dam.tfg.jcmd.models.ZonaModelo;
import cfgs.dam.tfg.jcmd.repositories.ZonaRepository;

@Service
public class ZonaServiceImpl implements ZonaService {

	@Autowired
	private ZonaRepository zonaRepository;

	@Override
	public List<ZonaModelo> findAll() {
		return zonaRepository.findAll();
	}

	@Override
	public ZonaModelo findById(Long id) {
		return zonaRepository.findById(id)
				.orElseThrow(() -> new ZonaNotFoundException("Zona no encontrada con ID: " + id));
	}

	@Override
	public ZonaModelo createZona(ZonaModelo zona) {
		return zonaRepository.save(zona);
	}
}