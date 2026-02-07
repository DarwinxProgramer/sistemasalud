package ec.gob.salud.hce.backend.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CirugiaPreviaDTO {
    private Integer idCirugiaPrevia;
    private Integer idPaciente; // <--- NUEVO
    private String tipo;
    private LocalDate fecha;
    private Integer idAntecedentePatologicoPersonal;
    private String usuario;
    private Integer idPersonal;
    private String uuidOffline;
    private String syncStatus;
    private LocalDateTime lastModified;
    private String origin;
}