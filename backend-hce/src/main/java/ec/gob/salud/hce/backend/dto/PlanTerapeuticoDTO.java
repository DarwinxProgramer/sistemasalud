package ec.gob.salud.hce.backend.dto;

import lombok.Data;

@Data // Genera getters y setters automáticamente
public class PlanTerapeuticoDTO {
    
    private Long id;
    private String medicamento;
    private String dosis;
    private String frecuencia;
    private String duracion;
    private String indicaciones;
    
    // Referencia al ID de la consulta (Integer para coincidir con tu frontend)
    private Integer idConsulta;
}