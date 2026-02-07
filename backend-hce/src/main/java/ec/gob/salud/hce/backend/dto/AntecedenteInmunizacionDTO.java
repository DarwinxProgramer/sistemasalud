package ec.gob.salud.hce.backend.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AntecedenteInmunizacionDTO {
    private Integer idAntecedenteInmunizacion;
    private String estadoVacunacion;
    private LocalDate fechaVacunacion;
    private String descripcion;
    private Integer idHistoriaClinica;
    private Integer idAntecedentePerinatal;
    private String usuario;
    private Integer idPersonal;
    private String uuidOffline;
    private String syncStatus;
    private LocalDateTime lastModified;
    private String origin;
}