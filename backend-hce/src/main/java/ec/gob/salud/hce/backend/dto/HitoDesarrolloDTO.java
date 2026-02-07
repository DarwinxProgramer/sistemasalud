package ec.gob.salud.hce.backend.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class HitoDesarrolloDTO {
    private Integer idHitoDesarrollo;
    private String sostenCefalio;
    private String sedestacion;
    private String deambulacion;
    private String lenguaje;
    private String observacion;
    
    // ID para la relación
    private Integer idDesarrolloPsicomotor;
    
    private String usuario;
    private Integer idPersonal;
    
    // Auditoría
    private String uuidOffline;
    private String syncStatus;
    private LocalDateTime lastModified;
    private String origin;
}