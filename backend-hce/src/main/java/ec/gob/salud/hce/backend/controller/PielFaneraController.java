package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.dto.PielFaneraDTO;
import ec.gob.salud.hce.backend.mapper.PielFaneraMapper;
import ec.gob.salud.hce.backend.repository.PielFaneraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pieles-faneras")
public class PielFaneraController {

    @Autowired
    private PielFaneraRepository repository;

    @Autowired
    private PielFaneraMapper mapper;

    @GetMapping
    public List<PielFaneraDTO> getAll() {
        return mapper.toDTOList(repository.findAll());
    }

    @PostMapping
    public PielFaneraDTO create(@RequestBody PielFaneraDTO dto) {
        return mapper.toDTO(repository.save(mapper.toEntity(dto)));
    }
}