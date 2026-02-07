package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.dto.AntecedentePerinatalDTO;
import ec.gob.salud.hce.backend.entity.AntecedentePerinatal;
import ec.gob.salud.hce.backend.mapper.AntecedentePerinatalMapper;
import ec.gob.salud.hce.backend.repository.AntecedentePerinatalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/antecedentes-perinatales")
public class AntecedentePerinatalController {

    @Autowired
    private AntecedentePerinatalRepository repository;

    @GetMapping
    public List<AntecedentePerinatalDTO> getAll() {
        return AntecedentePerinatalMapper.toDtoList(repository.findAll());
    }

    // Endpoint para buscar antecedentes de un paciente específico
    // (Aprovechamos el método que ya tienes en el repositorio)
    @GetMapping("/paciente/{idPaciente}")
    public List<AntecedentePerinatalDTO> getByPaciente(@PathVariable Integer idPaciente) {
        return AntecedentePerinatalMapper.toDtoList(repository.findByPaciente_IdPaciente(idPaciente));
    }

    @PostMapping
    public ResponseEntity<AntecedentePerinatalDTO> create(@RequestBody AntecedentePerinatalDTO dto) {
        // Usamos el Mapper para crear la entidad con todas sus relaciones (Paciente, Historia, etc.)
        AntecedentePerinatal entity = AntecedentePerinatalMapper.toEntity(dto);

        // Lógica para mantener el UUID offline si viene desde el frontend
        if (dto.getUuidOffline() != null) {
            entity.setUuidOffline(dto.getUuidOffline());
        }

        AntecedentePerinatal guardado = repository.save(entity);
        return ResponseEntity.ok(AntecedentePerinatalMapper.toDto(guardado));
    }
}