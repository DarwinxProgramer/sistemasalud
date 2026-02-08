package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.dto.ConsultaDTO;
import ec.gob.salud.hce.backend.service.ConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody ConsultaDTO dto) {
        try {
            // CAMBIO: Usar guardarSincronizado en lugar de guardarConsultaCompleta
            // Esto habilita la lógica de upsert por UUID offline, evitando duplicados
            ConsultaDTO respuesta = consultaService.guardarSincronizado(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        } catch (Exception e) {
            e.printStackTrace(); // Para ver el error en consola
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar: " + e.getMessage());
        }
    }

    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<List<ConsultaDTO>> listarPorPaciente(@PathVariable Integer idPaciente) {
        return ResponseEntity.ok(consultaService.listarPorPaciente(idPaciente));
    }

    @GetMapping
    public ResponseEntity<List<ConsultaDTO>> listarTodas() {
        // Debes implementar este método en tu ConsultaService
        return ResponseEntity.ok(consultaService.listarTodas());
    }
}