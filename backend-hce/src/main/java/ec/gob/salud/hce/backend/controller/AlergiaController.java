package ec.gob.salud.hce.backend.controller;
import ec.gob.salud.hce.backend.entity.Alergia;
import ec.gob.salud.hce.backend.repository.AlergiaRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/alergias")

public class AlergiaController {

    @Autowired
    private AlergiaRepository repository;

    @GetMapping
    public List<Alergia> listarTodas() {
        return repository.findAll();
    }

    @PostMapping
    public Alergia crear(@RequestBody Alergia alergia) {
        return repository.save(alergia);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alergia> actualizar(@PathVariable Long id, @RequestBody Alergia detalles) {
        return repository.findById(id)
            .map(alergia -> {
                alergia.setTipoAlergia(detalles.getTipoAlergia());
                alergia.setEstado(detalles.getEstado());
                alergia.setObservaciones(detalles.getObservaciones());
                alergia.setSyncStatus(detalles.getSyncStatus());
                return ResponseEntity.ok(repository.save(alergia));
            })
            .orElse(ResponseEntity.notFound().build());
    }
}