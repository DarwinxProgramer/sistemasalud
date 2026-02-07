package ec.gob.salud.hce.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AlimentacionDTO {
    private Integer idAlimentacion;
    private String descripcion;
    private String tipoLactancia;
    private String edadLactancia;
    private String tipo;
    private String edadAblactacion;
    
    // ID para la referencia en el JSON
    private Integer idDesarrolloPsicomotor;

    // Auditoría
    private String uuidOffline;
    private String syncStatus;
    private LocalDateTime lastModified;
    private String origin;
}