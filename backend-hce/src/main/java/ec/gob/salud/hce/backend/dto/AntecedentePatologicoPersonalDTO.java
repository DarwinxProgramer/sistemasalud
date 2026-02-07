package ec.gob.salud.hce.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AntecedentePatologicoPersonalDTO {
    private Integer idAntecedentePatologicoPersonal;
    private Integer idAntecedentePerinatal;
    private String observaciones;
    private String usuario;
    private String uuidOffline;
    private String syncStatus;
    private LocalDateTime lastModified;
    private String origin;
}