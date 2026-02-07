package ec.gob.salud.hce.backend.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class GrupoEtnicoDTO {
    private Integer idGrupoEtnico;
    private String descripcion;
    private Date fecha;
    private String uuidOffline;
    private String syncStatus;
}