package ec.gob.salud.hce.backend.controller;

// 1. IMPORTACIONES DE SPRING (Esto resuelve los errores de "cannot be resolved")
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

// 2. IMPORTACIONES DE TU PROPIO PROYECTO (Ajusta los nombres de paquetes si son diferentes)
import ec.gob.salud.hce.backend.entity.PacienteTutor;
import ec.gob.salud.hce.backend.repository.PacienteTutorRepository;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes_tutores")

public class PacienteTutorController {

    @Autowired
    private PacienteTutorRepository repository;

    // Obtener todos los registros (GET)
    @GetMapping
    public List<PacienteTutor> listarTodo() {
        return repository.findAll();
    }

    // Guardar una nueva relación (POST)
    @PostMapping
    public PacienteTutor guardar(@RequestBody PacienteTutor nuevaRelacion) {
        // Simplemente retornamos el resultado. 
        // Si hay un error de base de datos, Spring lanzará una excepción automática
        // que verás en Postman como un error 500 o 400.
        return repository.save(nuevaRelacion);
    }
}