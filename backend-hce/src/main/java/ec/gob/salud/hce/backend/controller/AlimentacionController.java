package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.dto.AlimentacionDTO;
import ec.gob.salud.hce.backend.entity.Alimentacion;
import ec.gob.salud.hce.backend.mapper.AlimentacionMapper;
import ec.gob.salud.hce.backend.repository.AlimentacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alimentacion")
@CrossOrigin(origins = "*")
public class AlimentacionController {

    @Autowired
    private AlimentacionRepository alimentacionRepository;

    @GetMapping
    public List<AlimentacionDTO> getAll() {
        return AlimentacionMapper.toDtoList(alimentacionRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlimentacionDTO> getById(@PathVariable Integer id) {
        return alimentacionRepository.findById(id)
                .map(AlimentacionMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AlimentacionDTO> create(@RequestBody AlimentacionDTO dto) {
        Alimentacion entity = AlimentacionMapper.toEntity(dto);
        Alimentacion guardado = alimentacionRepository.save(entity);
        return ResponseEntity.ok(AlimentacionMapper.toDto(guardado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlimentacionDTO> update(@PathVariable Integer id, @RequestBody AlimentacionDTO dto) {
        return alimentacionRepository.findById(id).map(existing -> {
            // Actualizamos los campos
            Alimentacion entity = AlimentacionMapper.toEntity(dto);
            entity.setIdAlimentacion(id); // Mantenemos el ID de la URL
            
            // Si el DTO no trae el ID del padre, intentamos conservar el que ya tenía
            if (entity.getDesarrolloPsicomotor() == null && existing.getDesarrolloPsicomotor() != null) {
                entity.setDesarrolloPsicomotor(existing.getDesarrolloPsicomotor());
            }

            Alimentacion updated = alimentacionRepository.save(entity);
            return ResponseEntity.ok(AlimentacionMapper.toDto(updated));
        }).orElse(ResponseEntity.notFound().build());
    }
}