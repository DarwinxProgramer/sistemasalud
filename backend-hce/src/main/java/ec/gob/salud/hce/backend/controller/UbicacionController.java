package ec.gob.salud.hce.backend.controller;

// Imports de Spring Framework
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Imports de tus propias clases (Entidades y Repositorios)
import ec.gob.salud.hce.backend.entity.Provincia;
import ec.gob.salud.hce.backend.entity.Canton;
import ec.gob.salud.hce.backend.entity.Parroquia;
import ec.gob.salud.hce.backend.repository.ProvinciaRepository;
import ec.gob.salud.hce.backend.repository.CantonRepository;
import ec.gob.salud.hce.backend.repository.ParroquiaRepository;

// Imports de Utilidades y Lombok
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/ubicaciones")
@RequiredArgsConstructor
public class UbicacionController {

    private final ProvinciaRepository provinciaRepository;
    private final CantonRepository cantonRepository;
    private final ParroquiaRepository parroquiaRepository;

    @GetMapping("/provincias")
    public List<Provincia> listarProvincias() {
        return provinciaRepository.findAll();
    }

    @GetMapping("/provincias/{id}/cantones")
    public List<Canton> listarCantones(@PathVariable Long id) {
        return cantonRepository.findByProvinciaId(id);
    }

    @GetMapping("/provincias/{provinciaId}/cantones/{cantonId}/parroquias")
public List<Parroquia> listarParroquias(
        @PathVariable Long provinciaId, 
        @PathVariable Long cantonId) {
    // Seguimos usando el cantonId para la búsqueda en la base de datos
    return parroquiaRepository.findByCantonId(cantonId);
}
    @GetMapping("/provincias/{provinciaId}/cantones/{cantonId}/parroquias/{parroquiaId}")
public List<Parroquia> listarParroquias(
        @PathVariable Long provinciaId, 
        @PathVariable Long cantonId, 
        @PathVariable Long parroquiaId) {
    // Seguimos usando el cantonId para la búsqueda en la base de datos
    return parroquiaRepository.findByCantonId(cantonId);
}

}