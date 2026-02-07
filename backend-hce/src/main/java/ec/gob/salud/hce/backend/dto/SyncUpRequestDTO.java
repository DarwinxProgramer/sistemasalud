package ec.gob.salud.hce.backend.dto;

import lombok.Data;
import java.util.Map;

@Data
public class SyncUpRequestDTO {
    private String type; // CREATE, UPDATE, DELETE
    private String entity; // paciente, consulta, etc.
    private Map<String, Object> data; // Datos de la entidad
}
