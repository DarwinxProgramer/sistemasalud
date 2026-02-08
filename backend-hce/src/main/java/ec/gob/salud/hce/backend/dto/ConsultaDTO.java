package ec.gob.salud.hce.backend.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Data
public class ConsultaDTO {
    private Long idConsulta;
    private Integer idPaciente;

    private LocalDate fecha;
    private LocalTime hora;
    private String motivo;
    private String enfermedadActual;

    // Vitales
    private Double peso;
    private Double talla;
    private Double temperatura;
    private Integer fc;
    private Integer fr;
    private Integer spo2;

    private String diagnosticoTexto;
    private String tipoDiagnostico;
    private String uuidOffline;
    private String syncStatus;
    private String origin;

    // JSON Completo (Backup visual)
    private Map<String, Object> jsonCompleto;

    private String usuario;

    // --- NUEVO: LISTAS PARA TABLAS DETALLADAS ---
    private List<PlanTerapeuticoDTO> listaPlan;
    private List<EstudioLaboratorioDTO> listaEstudios;
}