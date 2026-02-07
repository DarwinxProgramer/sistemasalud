package ec.gob.salud.hce.backend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoriaClinicaResponseDTO {

    private Long idHistoriaClinica;
    private Long idPaciente;
    private Integer idDiagnosticoPlanManejo;
    private LocalDateTime fechaCreacion;
    private String usuario;

    private String uuidOffline;
    private String syncStatus;
    private LocalDateTime lastModified;
    private String origin;
}
