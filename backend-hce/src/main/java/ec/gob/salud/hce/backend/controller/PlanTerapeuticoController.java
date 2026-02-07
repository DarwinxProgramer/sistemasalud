package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.dto.PlanTerapeuticoDTO;
import ec.gob.salud.hce.backend.mapper.PlanTerapeuticoMapper;
import ec.gob.salud.hce.backend.repository.PlanTerapeuticoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/planes-terapeuticos")
public class PlanTerapeuticoController {

    @Autowired
    private PlanTerapeuticoRepository repository;

    @Autowired
    private PlanTerapeuticoMapper mapper;

    @GetMapping
    public List<PlanTerapeuticoDTO> getAll() {
        return mapper.toDTOList(repository.findAll());
    }

    @PostMapping
    public PlanTerapeuticoDTO create(@RequestBody PlanTerapeuticoDTO dto) {
        return mapper.toDTO(repository.save(mapper.toEntity(dto)));
    }
}