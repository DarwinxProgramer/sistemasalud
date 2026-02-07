package ec.gob.salud.hce.backend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoriaClinicaRequestDTO {

    private Long idPaciente;
    private Integer idDiagnosticoPlanManejo;
    private String usuario;
    private String uuidOffline;
    private String syncStatus;
    private String origin;
}
