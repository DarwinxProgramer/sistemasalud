package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.dto.DiagnosticoPlanManejoDTO;
import ec.gob.salud.hce.backend.entity.DiagnosticoPlanManejo;
import ec.gob.salud.hce.backend.mapper.DiagnosticoPlanManejoMapper;
import ec.gob.salud.hce.backend.repository.DiagnosticoPlanManejoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diagnosticos-planes")
public class DiagnosticoPlanManejoController {

    @Autowired
    private DiagnosticoPlanManejoRepository repository;

    @Autowired
    private DiagnosticoPlanManejoMapper mapper;

    @GetMapping
    public List<DiagnosticoPlanManejoDTO> getAll() {
        return mapper.toDTOList(repository.findAll());
    }

    @PostMapping
    public DiagnosticoPlanManejoDTO create(@RequestBody DiagnosticoPlanManejoDTO dto) {
        DiagnosticoPlanManejo entity = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(entity));
    }
}