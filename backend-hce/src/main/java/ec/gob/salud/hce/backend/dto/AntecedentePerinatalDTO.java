package ec.gob.salud.hce.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AntecedentePerinatalDTO {
    private Integer idAntecedentePerinatal;
    
    private Integer idPaciente;        // ID para la relación Paciente
    private Integer idHistoriaClinica; // ID para la relación Historia Clínica
    
    private Boolean embarazoPlanificado;
    private Integer controlesPrenatales;
    private String antecedentes;
    private String otrosAntecedentes;
    
    private String usuario;
    private String uuidOffline;
    private String syncStatus;
    private LocalDateTime lastModified;
    private String origin;
}