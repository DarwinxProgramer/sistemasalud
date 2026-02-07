package ec.gob.salud.hce.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdMappingDTO {
    private String uuidOffline;
    private Integer newId;
    private String entityType; // "paciente", "consulta", etc.
}
