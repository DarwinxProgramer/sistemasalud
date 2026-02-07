package ec.gob.salud.hce.backend.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class PielFaneraDTO {
    private Integer idPielFanera;
    
    // ID para la relación
    private Integer idExamenFisicoSegmentario;
    
    private Byte icterisia;
    private Byte psianosis;
    private Byte rash;
    private String otros;
    private String usuario;
    private Integer idPersonal;
    
    // Auditoría
    private String uuidOffline;
    private String syncStatus;
    private LocalDateTime lastModified;
    private String origin;
}