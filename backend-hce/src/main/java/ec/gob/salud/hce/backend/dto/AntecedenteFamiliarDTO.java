package ec.gob.salud.hce.backend.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AntecedenteFamiliarDTO {
    private Integer idAntecedenteFamiliar;
    private String enfermedadHereditaria;
    private String descripcion;
    private LocalDate fecha;
    private Integer idEnfermedad;
    private Integer idPaciente;
    
    // Auditoría
    private String uuidOffline;
    private String syncStatus;
    private LocalDateTime lastModified;
    private String origin;
}