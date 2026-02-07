package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.dto.NeurologicoDTO;
import ec.gob.salud.hce.backend.mapper.NeurologicoMapper;
import ec.gob.salud.hce.backend.repository.NeurologicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/neurologicos")
public class NeurologicoController {

    @Autowired
    private NeurologicoRepository repository;

    @Autowired
    private NeurologicoMapper mapper;

    @GetMapping
    public List<NeurologicoDTO> getAll() {
        return mapper.toDTOList(repository.findAll());
    }

    @PostMapping
    public NeurologicoDTO create(@RequestBody NeurologicoDTO dto) {
        return mapper.toDTO(repository.save(mapper.toEntity(dto)));
    }
}