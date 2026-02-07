package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "cardio_pulmonares")
@Getter
@Setter
public class CardioPulmonar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cardio_pulmonar")
    private Integer idCardioPulmonar;

    @Column(name = "id_examen_fisico_segmentario")
    private Integer idExamenFisicoSegmentario;

    @Column(name = "ruido_cardiaco", length = 100)
    private String ruidoCardiaco;

    @Column(name = "murmullo_vesicular", length = 100)
    private String murmulloVesicular;

    @Column(length = 100)
    private String soplos;

    @Column(length = 100)
    private String crepitante;

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