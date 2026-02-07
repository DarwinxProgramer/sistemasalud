package ec.gob.salud.hce.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ExamenFisicoSegmentarioDTO {
    private Integer idExamenFisicoSegmentario;
    private String aspectoGeneral;
    private String pielFaneras;
    private String cabezaCuello;
    private String cardioPulmonar;
    private String abdomen;
    private String neurologico;
    private String evolucionClinica;
    private Integer idExamenFisico;
    private String uuidOffline;
    private String syncStatus;
    private LocalDateTime lastModified;
    private String origin;
}