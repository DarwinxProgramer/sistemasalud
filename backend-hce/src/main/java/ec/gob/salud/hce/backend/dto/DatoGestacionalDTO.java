package ec.gob.salud.hce.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DatoGestacionalDTO {
    private Integer idDatoGestacional;
    private String productoGestacion;
    private String edadGestacional;
    private String viaParto;
    private Double pesoNacer;
    private Double tallaNacer;
    private Integer apgarMinuto;
    private Integer apgarCincoMinutos;
    private String complicacionesPerinatales;
    private Integer idAntecedentePerinatal;
    private String usuario;
    private Integer idPersonal;
    private String uuidOffline;
    private String syncStatus;
    private LocalDateTime lastModified;
    private String origin;
}