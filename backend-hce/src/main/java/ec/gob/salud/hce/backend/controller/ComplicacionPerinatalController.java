package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.dto.ComplicacionPerinatalDTO;
import ec.gob.salud.hce.backend.entity.ComplicacionPerinatal;
import ec.gob.salud.hce.backend.mapper.ComplicacionPerinatalMapper;
import ec.gob.salud.hce.backend.repository.ComplicacionPerinatalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complicaciones-perinatales")
public class ComplicacionPerinatalController {

    @Autowired
    private ComplicacionPerinatalRepository repository;

    @GetMapping
    public List<ComplicacionPerinatalDTO> getAll() {
        return ComplicacionPerinatalMapper.toDtoList(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<ComplicacionPerinatalDTO> create(@RequestBody ComplicacionPerinatalDTO dto) {
        ComplicacionPerinatal entity = ComplicacionPerinatalMapper.toEntity(dto);
        ComplicacionPerinatal guardado = repository.save(entity);
        return ResponseEntity.ok(ComplicacionPerinatalMapper.toDto(guardado));
    }
}