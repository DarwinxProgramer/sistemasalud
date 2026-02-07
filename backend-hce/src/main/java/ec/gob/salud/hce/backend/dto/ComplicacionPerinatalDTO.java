package ec.gob.salud.hce.backend.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ComplicacionPerinatalDTO {
    private Integer idComplicacionPerinatal;
    private String descripcion;
    private LocalDate fecha;
    private Integer idDatoGestacional;
    private Integer idEnfermedad;
    private String usuario;
    private Integer idPersonal;
    private String uuidOffline;
    private String syncStatus;
    private LocalDateTime lastModified;
    private String origin;
}