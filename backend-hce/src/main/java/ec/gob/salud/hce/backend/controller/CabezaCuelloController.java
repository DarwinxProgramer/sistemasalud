package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.dto.CabezaCuelloDTO;
import ec.gob.salud.hce.backend.entity.CabezaCuello;
import ec.gob.salud.hce.backend.mapper.CabezaCuelloMapper;
import ec.gob.salud.hce.backend.repository.CabezaCuelloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cabezas-cuellos")
public class CabezaCuelloController {

    @Autowired
    private CabezaCuelloRepository repository;

    @GetMapping
    public List<CabezaCuelloDTO> getAll() {
        return CabezaCuelloMapper.toDtoList(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<CabezaCuelloDTO> create(@RequestBody CabezaCuelloDTO dto) {
        CabezaCuello entity = CabezaCuelloMapper.toEntity(dto);
        // La auditoría se maneja en @PrePersist
        CabezaCuello guardado = repository.save(entity);
        return ResponseEntity.ok(CabezaCuelloMapper.toDto(guardado));
    }
}