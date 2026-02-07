package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.dto.HitoDesarrolloDTO;
import ec.gob.salud.hce.backend.mapper.HitoDesarrolloMapper;
import ec.gob.salud.hce.backend.repository.HitoDesarrolloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/hitos-desarrollo")
public class HitoDesarrolloController {

    @Autowired
    private HitoDesarrolloRepository repository;

    @Autowired
    private HitoDesarrolloMapper mapper;

    @GetMapping
    public List<HitoDesarrolloDTO> getAll() {
        return mapper.toDTOList(repository.findAll());
    }

    @PostMapping
    public HitoDesarrolloDTO create(@RequestBody HitoDesarrolloDTO dto) {
        return mapper.toDTO(repository.save(mapper.toEntity(dto)));
    }
}