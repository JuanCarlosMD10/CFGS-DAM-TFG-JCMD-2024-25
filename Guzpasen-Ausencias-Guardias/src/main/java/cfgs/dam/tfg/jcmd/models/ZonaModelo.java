package cfgs.dam.tfg.jcmd.models;

import java.util.List;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "zona")
public class ZonaModelo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_zona")
	private Long idZona;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "tipo")
	private Tipo tipo;

	@Column(name = "planta")
	private Planta planta;

	@OneToMany(mappedBy = "idZona")
	private List<AusenciaModelo> ausencias;

	@OneToMany(mappedBy = "idAula")
	private List<GuardiaModelo> guardias;

	public enum Tipo {
		AULA, PASILLO, ASEO, PATIO, GIMNASIO, DEPARTAMENTO, BIBLIOTECA, OTROS
	}

	public enum Planta {
		P0, P1, P2
	}
}