package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.dto.EnfermedadDTO;
import ec.gob.salud.hce.backend.entity.Enfermedad;
import ec.gob.salud.hce.backend.mapper.EnfermedadMapper;
import ec.gob.salud.hce.backend.repository.EnfermedadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enfermedades")
public class EnfermedadController {

    @Autowired
    private EnfermedadRepository repository;

    @Autowired
    private EnfermedadMapper mapper;

    @GetMapping
    public List<EnfermedadDTO> getAll() {
        return mapper.toDTOList(repository.findAll());
    }

    @PostMapping
    public EnfermedadDTO create(@RequestBody EnfermedadDTO dto) {
        return mapper.toDTO(repository.save(mapper.toEntity(dto)));
    }
}