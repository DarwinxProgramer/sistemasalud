package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.dto.AlergiaPacienteDTO;
import ec.gob.salud.hce.backend.entity.AlergiaPaciente;
import ec.gob.salud.hce.backend.mapper.AlergiaPacienteMapper;
import ec.gob.salud.hce.backend.repository.AlergiaPacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alergias-paciente")
public class AlergiaPacienteController {

    @Autowired
    private AlergiaPacienteRepository repository;

    // Obtener todas las asignaciones
    @GetMapping
    public List<AlergiaPacienteDTO> getAll() {
        return AlergiaPacienteMapper.toDtoList(repository.findAll());
    }

    // Obtener alergias de un paciente específico
    @GetMapping("/paciente/{idPaciente}")
    public List<AlergiaPacienteDTO> getByPaciente(@PathVariable Integer idPaciente) {
        return AlergiaPacienteMapper.toDtoList(repository.findByPaciente_IdPaciente(idPaciente));
    }

    // Asignar una alergia a un paciente
    @PostMapping
    public ResponseEntity<AlergiaPacienteDTO> create(@RequestBody AlergiaPacienteDTO dto) {
        AlergiaPaciente entity = AlergiaPacienteMapper.toEntity(dto);
        
        if (dto.getUuidOffline() != null) {
            entity.setUuidOffline(dto.getUuidOffline());
        }

        AlergiaPaciente guardado = repository.save(entity);
        return ResponseEntity.ok(AlergiaPacienteMapper.toDto(guardado));
    }
}