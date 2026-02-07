package ec.gob.salud.hce.backend.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DesarrolloPsicomotorDTO {
    private Integer idDesarrolloPsicomotor;
    private String observacion;
    private Integer idHistoriaClinica;
    private LocalDate fechaEvaluacion;
    private String usuario;
    private String uuidOffline;
    private String syncStatus;
    private LocalDateTime lastModified;
    private String origin;
}