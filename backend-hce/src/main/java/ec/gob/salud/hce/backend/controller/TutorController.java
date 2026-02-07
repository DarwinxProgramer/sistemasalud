package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.entity.Tutor;
import ec.gob.salud.hce.backend.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tutores")
public class TutorController {

    @Autowired
    private TutorRepository repository;

    @GetMapping
    public List<Tutor> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Tutor guardar(@RequestBody Tutor tutor) {
        return repository.save(tutor);
    }
}