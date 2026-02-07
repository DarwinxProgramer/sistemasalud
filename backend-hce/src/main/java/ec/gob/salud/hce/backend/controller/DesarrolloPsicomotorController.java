package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.dto.DesarrolloPsicomotorDTO;
import ec.gob.salud.hce.backend.entity.DesarrolloPsicomotor;
import ec.gob.salud.hce.backend.mapper.DesarrolloPsicomotorMapper;
import ec.gob.salud.hce.backend.repository.DesarrolloPsicomotorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/desarrollo-psicomotor")
public class DesarrolloPsicomotorController {

    @Autowired
    private DesarrolloPsicomotorRepository repository;

    @GetMapping
    public List<DesarrolloPsicomotorDTO> getAll() {
        return DesarrolloPsicomotorMapper.toDtoList(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<DesarrolloPsicomotorDTO> create(@RequestBody DesarrolloPsicomotorDTO dto) {
        DesarrolloPsicomotor entity = DesarrolloPsicomotorMapper.toEntity(dto);
        // Validación básica para evitar null pointer
        if (entity == null) return ResponseEntity.badRequest().build();
        
        DesarrolloPsicomotor guardado = repository.save(entity);
        return ResponseEntity.ok(DesarrolloPsicomotorMapper.toDto(guardado));
    }

    // CORRECCIÓN AQUÍ:
    // 1. Recibimos Long (o Integer y lo convertimos). Lo ideal es recibir Long directamente.
    // 2. Llamamos al método correcto del repositorio: findByHistoriaClinicaIdHistoriaClinica
    @GetMapping("/historia/{idHistoria}")
    public List<DesarrolloPsicomotorDTO> getByHistoria(@PathVariable Long idHistoria) {
        return DesarrolloPsicomotorMapper.toDtoList(
            repository.findByHistoriaClinicaIdHistoriaClinica(idHistoria)
        );
    }
}