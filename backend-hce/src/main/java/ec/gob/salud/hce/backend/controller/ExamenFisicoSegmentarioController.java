package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.dto.ExamenFisicoSegmentarioDTO;
import ec.gob.salud.hce.backend.entity.ExamenFisicoSegmentario;
import ec.gob.salud.hce.backend.mapper.ExamenFisicoSegmentarioMapper;
import ec.gob.salud.hce.backend.repository.ExamenFisicoSegmentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/examen-fisico-segmentario")
public class ExamenFisicoSegmentarioController {

    @Autowired
    private ExamenFisicoSegmentarioRepository repository;

    @PostMapping
    public ResponseEntity<ExamenFisicoSegmentarioDTO> create(@RequestBody ExamenFisicoSegmentarioDTO dto) {
        ExamenFisicoSegmentario guardado = repository.save(ExamenFisicoSegmentarioMapper.toEntity(dto));
        return ResponseEntity.ok(ExamenFisicoSegmentarioMapper.toDto(guardado));
    }
}