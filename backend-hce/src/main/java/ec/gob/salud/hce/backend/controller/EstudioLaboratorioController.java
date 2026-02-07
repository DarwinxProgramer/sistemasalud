package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.dto.EstudioLaboratorioDTO;
import ec.gob.salud.hce.backend.mapper.EstudioLaboratorioMapper;
import ec.gob.salud.hce.backend.repository.EstudioLaboratorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/estudios-laboratorios")
public class EstudioLaboratorioController {

    @Autowired
    private EstudioLaboratorioRepository repository;

    @Autowired
    private EstudioLaboratorioMapper mapper;

    @GetMapping
    public List<EstudioLaboratorioDTO> getAll() {
        return mapper.toDTOList(repository.findAll());
    }

    @PostMapping
    public EstudioLaboratorioDTO create(@RequestBody EstudioLaboratorioDTO dto) {
        return mapper.toDTO(repository.save(mapper.toEntity(dto)));
    }
}