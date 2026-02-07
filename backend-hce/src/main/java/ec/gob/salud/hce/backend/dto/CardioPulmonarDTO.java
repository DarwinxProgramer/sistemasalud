package ec.gob.salud.hce.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CardioPulmonarDTO {
    private Integer idCardioPulmonar;
    private Integer idExamenFisicoSegmentario;
    private String ruidoCardiaco;
    private String murmulloVesicular;
    private String soplos;
    private String crepitante;
    private String otros;
    private String uuidOffline;
    private String syncStatus;
    private LocalDateTime lastModified;
    private String origin;
    private String usuario;
    private Integer idPersonal;
}