package cfgs.dam.tfg.jcmd.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo JPA que representa una zona dentro del centro educativo. Contiene
 * información sobre el nombre, tipo, planta y relaciones con ausencias y
 * guardias asignadas a dicha zona.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "zona")
public class ZonaModelo {

	/** Identificador único de la zona */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_zona")
	private Long idZona;

	/** Nombre descriptivo de la zona */
	@Column(name = "nombre")
	private String nombre;

	/** Tipo de zona (aula, pasillo, aseo, etc.) */
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo")
	private Tipo tipo;

	/** Planta en la que se encuentra la zona */
	@Enumerated(EnumType.STRING)
	@Column(name = "planta")
	private Planta planta;

	/** Lista de ausencias asociadas a esta zona */
	@OneToMany(mappedBy = "idZona")
	private List<AusenciaModelo> ausencias;

	/** Lista de guardias asociadas a esta zona (aula) */
	@OneToMany(mappedBy = "idAula")
	private List<GuardiaModelo> guardias;

	/**
	 * Enum que define los tipos posibles de zonas dentro del centro educativo.
	 */
	public enum Tipo {
		AULA, PASILLO, ASEO, PATIO, GIMNASIO, DEPARTAMENTO, BIBLIOTECA, OTROS
	}

	/**
	 * Enum que define las plantas posibles donde puede ubicarse una zona.
	 */
	public enum Planta {
		P0, P1, P2
	}
}