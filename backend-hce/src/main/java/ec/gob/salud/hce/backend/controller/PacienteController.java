package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.dto.PacienteRequestDTO;
import ec.gob.salud.hce.backend.dto.PacienteResponseDTO;
import ec.gob.salud.hce.backend.entity.HistoriaClinica;
import ec.gob.salud.hce.backend.entity.Paciente;
import ec.gob.salud.hce.backend.mapper.PacienteMapper;
import ec.gob.salud.hce.backend.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin("*")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private PacienteMapper pacienteMapper;

    // =====================================================
    // CREAR PACIENTE
    // =====================================================
    @PostMapping
    public ResponseEntity<PacienteResponseDTO> crearPaciente(
            @RequestBody PacienteRequestDTO requestDTO) {

        Paciente paciente = pacienteService.crearPaciente(requestDTO);

        PacienteResponseDTO responseDTO = pacienteMapper.toDTO(paciente);

        return ResponseEntity.ok(responseDTO);
    }

    // =====================================================
    // OBTENER TODOS
    // =====================================================
    @GetMapping
    public ResponseEntity<List<PacienteResponseDTO>> listarTodos() {

        List<PacienteResponseDTO> lista = pacienteService.listarTodos()
                .stream()
                .map(pacienteMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }

    // =====================================================
    // OBTENER POR ID
    // =====================================================
    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> obtenerPorId(@PathVariable Integer id) {

        Optional<Paciente> pacienteOpt = pacienteService.obtenerPorId(id);

        if (pacienteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Paciente paciente = pacienteOpt.get();

        // 🔥 CORRECCIÓN: ya no es lista, es objeto
        HistoriaClinica historia = paciente.getHistoriaClinica();

        if (historia != null) {
            // No hacemos nada especial aquí porque el mapper ya lo maneja
        }

        PacienteResponseDTO dto = pacienteMapper.toDTO(paciente);

        return ResponseEntity.ok(dto);
    }

    // =====================================================
    // BUSCAR POR CRITERIO
    // =====================================================
    @GetMapping("/buscar")
    public ResponseEntity<List<PacienteResponseDTO>> buscarPorCriterio(
            @RequestParam String criterio) {

        List<PacienteResponseDTO> lista = pacienteService.buscarPorCriterio(criterio)
                .stream()
                .map(pacienteMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(lista);
    }
}
