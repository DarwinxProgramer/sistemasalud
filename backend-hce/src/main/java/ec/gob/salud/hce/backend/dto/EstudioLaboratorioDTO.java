package ec.gob.salud.hce.backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data // Genera getters y setters automáticamente
public class EstudioLaboratorioDTO {
    
    private Long id;
    private String tipo;
    private String descripcion;
    private LocalDate fecha;
    private String resultado;
    
    // Aquí usamos Integer para conectar con el ID de la consulta
    private Integer idConsulta;
}