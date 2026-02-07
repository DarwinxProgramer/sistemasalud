package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.dto.EnfermedadDiagnosticadaDTO;
import ec.gob.salud.hce.backend.mapper.EnfermedadDiagnosticadaMapper;
import ec.gob.salud.hce.backend.repository.EnfermedadDiagnosticadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/enfermedades-diagnosticadas")
public class EnfermedadDiagnosticadaController {

    @Autowired
    private EnfermedadDiagnosticadaRepository repository;

    @Autowired
    private EnfermedadDiagnosticadaMapper mapper;

    @GetMapping
    public List<EnfermedadDiagnosticadaDTO> getAll() {
        return mapper.toDTOList(repository.findAll());
    }

    @PostMapping
    public EnfermedadDiagnosticadaDTO create(@RequestBody EnfermedadDiagnosticadaDTO dto) {
        return mapper.toDTO(repository.save(mapper.toEntity(dto)));
    }
}