package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.dto.DatoGestacionalDTO;
import ec.gob.salud.hce.backend.entity.DatoGestacional;
import ec.gob.salud.hce.backend.mapper.DatoGestacionalMapper;
import ec.gob.salud.hce.backend.repository.DatoGestacionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/datos-gestacionales")
public class DatoGestacionalController {

    @Autowired
    private DatoGestacionalRepository repository;

    @GetMapping
    public List<DatoGestacionalDTO> getAll() {
        return DatoGestacionalMapper.toDtoList(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<DatoGestacionalDTO> create(@RequestBody DatoGestacionalDTO dto) {
        DatoGestacional entity = DatoGestacionalMapper.toEntity(dto);
        DatoGestacional guardado = repository.save(entity);
        return ResponseEntity.ok(DatoGestacionalMapper.toDto(guardado));
    }
}