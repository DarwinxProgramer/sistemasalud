package ec.gob.salud.hce.backend.controller;

import ec.gob.salud.hce.backend.dto.SyncDownResponseDTO;
import ec.gob.salud.hce.backend.service.SyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sync")
@RequiredArgsConstructor
public class SyncController {

    private final SyncService syncService;

    @GetMapping("/down")
    public ResponseEntity<SyncDownResponseDTO> descargarDatos() {
        return ResponseEntity.ok(syncService.obtenerDatosParaDescargaInicial());
    }

    @org.springframework.web.bind.annotation.PostMapping("/up")
    public ResponseEntity<java.util.List<ec.gob.salud.hce.backend.dto.IdMappingDTO>> subirDatos(
            @org.springframework.web.bind.annotation.RequestBody ec.gob.salud.hce.backend.dto.SyncUpRequestDTO request) {
        return ResponseEntity.ok(syncService.procesarSubida(request));
    }
}