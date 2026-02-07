package ec.gob.salud.hce.backend.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class EnfermedadDiagnosticadaDTO {
    private Integer idEnfermedadDiagnosticada;
    private String descripcion;
    private Date fecha;
    
    // IDs para las relaciones
    private Integer idEnfermedad;
    private Integer idAntecedentePatologicoPersonal;
    
    private String usuario;
    private Integer idPersonal;
    private String uuidOffline;
    private String syncStatus;
    
    // CORRECCIÓN: Faltaba este campo
    private String origin;
}