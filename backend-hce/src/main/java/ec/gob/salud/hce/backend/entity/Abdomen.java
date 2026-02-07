package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "abdomenes")
@Getter
@Setter
public class Abdomen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_abdomen")
    private Integer idAbdomen;

    // --- CORRECCIÓN: Relación con Examen Físico Segmentario ---
    // Reemplazamos el Integer suelto por la entidad real
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_examen_fisico_segmentario")
    private ExamenFisicoSegmentario examenFisicoSegmentario;
    // -----------------------------------------------------------

    private Boolean blando;
    private Boolean depresible;
    private Boolean dolorPalpacion;
    private Boolean hepatomegalia;
    private Boolean esplenomegalia;

    @Column(length = 150)
    private String otros;

    // --- AUDITORÍA ---
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