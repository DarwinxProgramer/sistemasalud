package ec.gob.salud.hce.backend.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AlergiaPacienteDTO {
    private Integer idAlergiaPaciente;
    private String reaccion;
    private String observaciones;
    
    private Integer idPaciente;
    private Integer idAlergia;
    
    // --- NUEVO: Para mostrar el nombre en la pantalla sin hacer otra consulta ---
    private String nombreAlergia; 
    
    private Integer idAntecedentePatologicoPersonal;
    
    private LocalDate fechaCreacion;
    private String uuidOffline;
    private String syncStatus;
    private LocalDateTime lastModified;
    private String origin;
}