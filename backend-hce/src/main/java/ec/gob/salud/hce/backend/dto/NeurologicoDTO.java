package ec.gob.salud.hce.backend.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class NeurologicoDTO {
    private Integer idNeurologico;
    
    // ID para la relación
    private Integer idExamenFisicoSegmentario;
    
    private String reflejoOsteotendinoso;
    private String estadoMental;
    private String tonoMuscular;
    private String otros;
    private String usuario;
    private Integer idPersonal;
    
    // Auditoría
    private String uuidOffline;
    private String syncStatus;
    private LocalDateTime lastModified;
    private String origin;
}