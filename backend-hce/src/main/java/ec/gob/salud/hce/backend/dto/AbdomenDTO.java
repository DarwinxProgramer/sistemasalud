package ec.gob.salud.hce.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AbdomenDTO {
    private Integer idAbdomen;
    private Integer idExamenFisicoSegmentario; // Se mantiene el ID para el frontend
    private Boolean blando;
    private Boolean depresible;
    private Boolean dolorPalpacion;
    private Boolean hepatomegalia;
    private Boolean esplenomegalia;
    private String otros;
    private String uuidOffline;
    private String syncStatus;
    private LocalDateTime lastModified;
    private String origin;
    private String usuario;
    private Integer idPersonal;
}