package ec.gob.salud.hce.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CabezaCuelloDTO {
    private Integer idCabezaCuello;
    private Integer idExamenFisicoSegmentario;
    private String fontaneloAnterior;
    private String adenopatia;
    private String otros;
    private String uuidOffline;
    private String syncStatus;
    private LocalDateTime lastModified;
    private String origin;
    private String usuario;
    private Integer idPersonal;
}