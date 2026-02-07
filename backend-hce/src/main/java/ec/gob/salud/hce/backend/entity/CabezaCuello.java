package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "cabezas_cuellos")
@Getter
@Setter
public class CabezaCuello {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cabeza_cuelloint")
    private Integer idCabezaCuello;

    @Column(name = "id_examen_fisico_segmentario")
    private Integer idExamenFisicoSegmentario;

    @Column(name = "frontanelo_anterior", length = 100)
    private String fontaneloAnterior;

    @Column(length = 100)
    private String adenopatia;

    @Column(length = 150)
    private String otros;

    @Column(name = "uuid_offline", length = 36)
    private String uuidOffline;

    @Column(name = "sync_status", length = 20)
    private String syncStatus;

    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    @Column(length = 20)
    private String origin;

    @Column(length = 50)
    private String usuario;

    @Column(name = "id_personal")
    private Integer idPersonal;

    @PrePersist
    @PreUpdate
    protected void updateAudit() {
        this.lastModified = LocalDateTime.now();
        if (this.uuidOffline == null) this.uuidOffline = java.util.UUID.randomUUID().toString();
        if (this.syncStatus == null) this.syncStatus = "PENDING";
    }
}