        package ec.gob.salud.hce.backend.controller;
        import ec.gob.salud.hce.backend.service.HistoriaClinicaService;
        import ec.gob.salud.hce.backend.dto.HistoriaClinicaRequestDTO;
        import ec.gob.salud.hce.backend.dto.HistoriaClinicaResponseDTO;
        import ec.gob.salud.hce.backend.service.HistoriaClinicaService;
        import jakarta.validation.Valid;
        import lombok.RequiredArgsConstructor;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.*;

        import java.util.List;

        @RestController
        @RequestMapping("/api/historias_clinicas")
        @RequiredArgsConstructor
        public class HistoriaClinicaController {

        private final HistoriaClinicaService historiaClinicaService;

        /**
         * Obtener todas las historias clínicas (Evita el error 405 en la raíz)
         */
        @GetMapping
        public ResponseEntity<List<HistoriaClinicaResponseDTO>> listarTodas() {
                return ResponseEntity.ok(historiaClinicaService.listarTodas());
        }

        /**
         * Crear historia clínica para un paciente
         */
        @PostMapping
        public ResponseEntity<HistoriaClinicaResponseDTO> crearHistoriaClinica(
                @Valid @RequestBody HistoriaClinicaRequestDTO dto) {

                return new ResponseEntity<>(
                        historiaClinicaService.crearHistoriaClinica(dto),
                        HttpStatus.CREATED
                );
        }

        /**
         * Obtener historia clínica por ID único de la historia
         */
        @GetMapping("/{id}")
        public ResponseEntity<HistoriaClinicaResponseDTO> obtenerPorId(
                @PathVariable Long id) {

                return ResponseEntity.ok(
                        historiaClinicaService.obtenerPorId(id)
                );
        }

        /**
         * Obtener todas las historias clínicas pertenecientes a un paciente
         * Nota: Cambiado a List para soportar múltiples registros del mismo paciente
         */
        @GetMapping("/paciente/{idPaciente}")
        public ResponseEntity<List<HistoriaClinicaResponseDTO>> obtenerPorPaciente(
                @PathVariable Long idPaciente) {

                return ResponseEntity.ok(
                        historiaClinicaService.obtenerPorPaciente(idPaciente)
                );
        }
        }