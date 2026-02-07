package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.entity.AntecedenteFamiliar;
import ec.gob.salud.hce.backend.repository.AntecedenteFamiliarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/antecedentes-familiares")
@CrossOrigin(origins = "*")
public class AntecedenteFamiliarController {

    @Autowired
    private AntecedenteFamiliarRepository repository;

    @GetMapping
    public List<AntecedenteFamiliar> listarTodos() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AntecedenteFamiliar> obtenerPorId(@PathVariable Integer id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public AntecedenteFamiliar crear(@RequestBody AntecedenteFamiliar antecedente) {
        return repository.save(antecedente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AntecedenteFamiliar> actualizar(@PathVariable Integer id, @RequestBody AntecedenteFamiliar detalles) {
        return repository.findById(id)
                .map(antecedente -> {
                    antecedente.setEnfermedadHereditaria(detalles.getEnfermedadHereditaria());
                    antecedente.setDescripcion(detalles.getDescripcion());
                    antecedente.setFecha(detalles.getFecha());
                    antecedente.setSyncStatus("UPDATED");
                    return ResponseEntity.ok(repository.save(antecedente));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}