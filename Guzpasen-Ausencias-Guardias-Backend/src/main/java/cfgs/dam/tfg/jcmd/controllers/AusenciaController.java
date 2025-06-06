package cfgs.dam.tfg.jcmd.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cfgs.dam.tfg.jcmd.dto.AusenciaDTO;
import cfgs.dam.tfg.jcmd.models.AusenciaModelo;
import cfgs.dam.tfg.jcmd.models.UsuarioModelo;
import cfgs.dam.tfg.jcmd.services.AusenciaService;

/**
 * Controlador REST para la gestión de ausencias de profesores.
 * Proporciona endpoints para registrar, modificar, eliminar y consultar ausencias.
 * 
 * Permite llamadas desde aplicaciones en http://127.0.0.1:5500 y http://localhost:5500.
 */
@CrossOrigin(origins = { "http://127.0.0.1:5500", "http://localhost:5500" })
@RestController
@RequestMapping("/ausencias")
public class AusenciaController {

    @Autowired
    private AusenciaService ausenciaService;

    /**
     * Registra una nueva ausencia para un profesor.
     * 
     * @param ausencia Datos de la ausencia a registrar.
     * @return Objeto AusenciaDTO con la información registrada.
     */
    @PostMapping("/registrar")
    public AusenciaDTO registrarAusencia(@RequestBody AusenciaModelo ausencia) {
        UsuarioModelo profesorExistente = new UsuarioModelo();
        profesorExistente.setIdUsuario(1L); // simula como que está logueado el usuario con id 1 por falta de login
        ausencia.setIdProfesor(profesorExistente);
        return ausenciaService.registrarAusencia(ausencia);
    }
    
    /**
     * Registra una nueva ausencia para un profesor.
     * 
     * @param ausencia Datos de la ausencia a registrar.
     * @return Objeto AusenciaDTO con la información registrada.
     */
    @PostMapping("/profesor/{idProfesor}")
    public AusenciaDTO registrarAusenciaConCualquierIdDeProfesor(@RequestBody AusenciaModelo ausencia) {
        return ausenciaService.registrarAusencia(ausencia);
    }

    /**
     * Modifica una ausencia existente.
     * 
     * @param id ID de la ausencia a modificar.
     * @param ausencia Nuevos datos de la ausencia.
     * @return AusenciaDTO con los datos actualizados.
     */
    @PutMapping("/{id}")
    public AusenciaDTO modificarAusencia(@PathVariable Long id, @RequestBody AusenciaModelo ausencia) {
        return ausenciaService.modificarAusencia(id, ausencia);
    }

    /**
     * Elimina una ausencia por su ID.
     * 
     * @param id ID de la ausencia a eliminar.
     */
    @DeleteMapping("/{id}")
    public void eliminarAusencia(@PathVariable Long id) {
        ausenciaService.eliminarAusencia(id);
    }

    /**
     * Consulta todas las ausencias, opcionalmente filtradas por estado.
     * 
     * @param estado Estado de la ausencia (opcional).
     * @return Lista de objetos AusenciaDTO.
     */
    @GetMapping("/consultar")
    public List<AusenciaDTO> consultarAusencias(@RequestParam(required = false) AusenciaModelo.Estado estado) {
        return ausenciaService.consultarAusencias(estado);
    }

    /**
     * Obtiene una ausencia por su ID.
     * 
     * @param id ID de la ausencia.
     * @return Objeto AusenciaDTO correspondiente.
     */
    @GetMapping("/{id}")
    public AusenciaDTO obtenerAusencia(@PathVariable Long id) {
        return ausenciaService.obtenerAusencia(id);
    }

    /**
     * Obtiene todas las ausencias registradas por un profesor específico.
     * 
     * @param idProfesor ID del profesor.
     * @return Lista de objetos AusenciaDTO asociados al profesor.
     */
    @GetMapping("/profesor/{idProfesor}")
    public List<AusenciaDTO> obtenerAusenciasPorProfesor(@PathVariable Long idProfesor) {
        return ausenciaService.obtenerAusenciasPorProfesor(idProfesor);
    }
}