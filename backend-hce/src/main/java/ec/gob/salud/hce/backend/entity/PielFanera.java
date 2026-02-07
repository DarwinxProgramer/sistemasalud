package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "pieles_faneras")
public class PielFanera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_piel_fanera")
    private Integer idPielFanera;

    // --- JOIN (RELACIÓN) ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_examen_fisico_segmentario")
    private ExamenFisicoSegmentario examenFisicoSegmentario;

    @Column(name = "icterisia")
    private Byte icterisia;

    @Column(name = "psianosis")
    private Byte psianosis;

    @Column(name = "rash")
    private Byte rash;

    @Column(name = "otros")
    private String otros;

    @Column(name = "usuario")
    private String usuario;

    @Column(name = "id_personal")
    private Integer idPersonal;

    // --- AUDITORÍA ---
    @Column(name = "uuid_offline")
    private String uuidOffline;
    
    @Column(name = "sync_status")
    private String syncStatus;

    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    @Column(name = "origin")
    private String origin;

    @PrePersist @PreUpdate
    public void audit() {
        this.lastModified = LocalDateTime.now();
        if (this.uuidOffline == null) this.uuidOffline = java.util.UUID.randomUUID().toString();
        if (this.syncStatus == null) this.syncStatus = "PENDING";
        if (this.origin == null) this.origin = "WEB";
    }
}