package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.service.ReporteService;
import ec.gob.salud.hce.backend.entity.Paciente;
import ec.gob.salud.hce.backend.repository.PacienteRepository; // Asumo que tienes esto
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;
    
    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping("/historia/{cedula}")
    public ResponseEntity<byte[]> descargarHistoriaClinica(@PathVariable String cedula) {
        try {
            // Buscamos el ID numérico usando la cédula (Ya que Jasper usa Integer id_paciente )
            Paciente paciente = pacienteRepository.findByCedula(cedula).stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

            byte[] pdfBytes = reporteService.generarHistoriaClinicaPdf(paciente.getIdPaciente());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Historia_" + cedula + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}