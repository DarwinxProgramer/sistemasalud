package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "alimentacion")
@Getter
@Setter
public class Alimentacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alimentacion")
    private Integer idAlimentacion;

    @Column(length = 50)
    private String descripcion;

    @Column(name = "tipo_lactancia", length = 150)
    private String tipoLactancia;

    @Column(name = "edad_lactancia", length = 50)
    private String edadLactancia;

    @Column(length = 100)
    private String tipo;

    @Column(name = "edad_ablactacion", length = 50)
    private String edadAblactacion;

    // --- UNIÓN CON DESARROLLO PSICOMOTOR ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_desarrollo_psicomotor", referencedColumnName = "id_desarrollo_psicomotor")
    private DesarrolloPsicomotor desarrolloPsicomotor;

    // Auditoría
    @Column(name = "uuid_offline", length = 36)
    private String uuidOffline;

    @Column(name = "sync_status", length = 20)
    private String syncStatus;

    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    @Column(length = 20)
    private String origin;

    @PrePersist
    @PreUpdate
    protected void updateAudit() {
        this.lastModified = LocalDateTime.now();
        if (this.uuidOffline == null) this.uuidOffline = java.util.UUID.randomUUID().toString();
        if (this.syncStatus == null) this.syncStatus = "PENDING";
    }
}