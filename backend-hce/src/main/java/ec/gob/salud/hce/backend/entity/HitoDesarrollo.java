package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "hitos_desarrollo")
public class HitoDesarrollo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hito_desarrollo")
    private Integer idHitoDesarrollo;

    @Column(name = "sosten_cefalio")
    private String sostenCefalio;

    @Column(name = "sedestacion")
    private String sedestacion;

    @Column(name = "deambulacion")
    private String deambulacion;

    @Column(name = "lenguaje")
    private String lenguaje;

    @Column(name = "observacion")
    private String observacion;

    // --- JOIN (RELACIÓN) ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_desarrollo_psicomotor")
    private DesarrolloPsicomotor desarrolloPsicomotor;

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