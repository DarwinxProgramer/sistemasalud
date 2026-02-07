package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.dto.HospitalizacionPreviaDTO;
import ec.gob.salud.hce.backend.mapper.HospitalizacionPreviaMapper;
import ec.gob.salud.hce.backend.repository.HospitalizacionPreviaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/hospitalizaciones-previas")
public class HospitalizacionPreviaController {

    @Autowired
    private HospitalizacionPreviaRepository repository;

    // NO necesitas inyectar el mapper porque ahora es static

    // --- NUEVO: Obtener por Paciente ---
    @GetMapping("/paciente/{idPaciente}")
    public List<HospitalizacionPreviaDTO> getByPaciente(@PathVariable Integer idPaciente) {
        return HospitalizacionPreviaMapper.toDTOList(repository.findByPaciente_IdPaciente(idPaciente));
    }

    @GetMapping
    public List<HospitalizacionPreviaDTO> getAll() {
        return HospitalizacionPreviaMapper.toDTOList(repository.findAll());
    }

    @PostMapping
    public HospitalizacionPreviaDTO create(@RequestBody HospitalizacionPreviaDTO dto) {
        // Usamos el método estático
        return HospitalizacionPreviaMapper.toDTO(
            repository.save(HospitalizacionPreviaMapper.toEntity(dto))
        );
    }
}