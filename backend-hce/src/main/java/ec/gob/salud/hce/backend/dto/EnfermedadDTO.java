package ec.gob.salud.hce.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnfermedadDTO {
    private Integer idEnfermedad;
    private String nombre;
    private String estado;
    private String usuario;
    private String uuidOffline;
    private String syncStatus;
}