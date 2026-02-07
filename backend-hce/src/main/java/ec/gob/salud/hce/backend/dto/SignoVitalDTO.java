package ec.gob.salud.hce.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SignoVitalDTO {
    private Integer idSignoVital;
    private Double peso;
    private Double tallaLongitud;
    private Double perimetroCefalico;
    private Double temperatura;
    private Integer frecuenciaCardiaca;
    private Integer frecuenciaRespiratoria;
    private Integer presionArterialSistolica;
    private Integer presionArterialDiastolica;
    private Integer saturacionOxigeno;
    private Double imc;
    private String puntuacion;
    private String observacion;
    private Integer idExamenFisico;

    private String uuidOffline;
    private String syncStatus;
    private LocalDateTime lastModified;
    private String origin;
}