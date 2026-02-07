package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.dto.AbdomenDTO;
import ec.gob.salud.hce.backend.entity.Abdomen;
import ec.gob.salud.hce.backend.mapper.AbdomenMapper;
import ec.gob.salud.hce.backend.repository.AbdomenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/abdomenes")
public class AbdomenController {

    @Autowired
    private AbdomenRepository abdomenRepository;

    @GetMapping
    public List<AbdomenDTO> getAll() {
        return AbdomenMapper.toDtoList(abdomenRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<AbdomenDTO> create(@RequestBody AbdomenDTO dto) {
        // Usamos el Mapper para crear la entidad y manejar las relaciones automáticamente
        Abdomen entity = AbdomenMapper.toEntity(dto);

        // Lógica de UUID offline
        if (dto.getUuidOffline() != null) {
            entity.setUuidOffline(dto.getUuidOffline());
        }

        Abdomen guardado = abdomenRepository.save(entity);
        return ResponseEntity.ok(AbdomenMapper.toDto(guardado));
    }
}