package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.dto.CirugiaPreviaDTO;
import ec.gob.salud.hce.backend.entity.CirugiaPrevia;
import ec.gob.salud.hce.backend.mapper.CirugiaPreviaMapper;
import ec.gob.salud.hce.backend.repository.CirugiaPreviaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/cirugias-previas")
public class CirugiaPreviaController {

    @Autowired
    private CirugiaPreviaRepository repository;

    // --- NUEVO: Endpoint para la pestaña ---
    @GetMapping("/paciente/{idPaciente}")
    public List<CirugiaPreviaDTO> getByPaciente(@PathVariable Integer idPaciente) {
        return CirugiaPreviaMapper.toDtoList(repository.findByPaciente_IdPaciente(idPaciente));
    }

    @GetMapping
    public List<CirugiaPreviaDTO> getAll() {
        return CirugiaPreviaMapper.toDtoList(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<CirugiaPreviaDTO> create(@RequestBody CirugiaPreviaDTO dto) {
        CirugiaPrevia entity = CirugiaPreviaMapper.toEntity(dto);
        
        // Mantener UUID si viene del offline
        if (dto.getUuidOffline() != null) {
            entity.setUuidOffline(dto.getUuidOffline());
        }
        
        CirugiaPrevia guardado = repository.save(entity);
        return ResponseEntity.ok(CirugiaPreviaMapper.toDto(guardado));
    }
}