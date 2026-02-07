package ec.gob.salud.hce.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ExamenFisicoDTO {
    private Integer idPaciente;
    private Integer idExamenFisico;
    private Integer idSignoVital;
    private Integer idExamenFisicoSegmentario;
    private Integer idHistoriaClinica;
    
    // Auditoría y Sincronización
    private String uuidOffline;
    private String syncStatus;
    private LocalDateTime lastModified;
    private String origin;
}