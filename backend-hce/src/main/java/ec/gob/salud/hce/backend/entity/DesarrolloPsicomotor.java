package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "desarrollos_psicomotores")
@Getter
@Setter
public class DesarrolloPsicomotor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_desarrollo_psicomotor")
    private Integer idDesarrolloPsicomotor;

    // RELACIÓN: Muchos desarrollos pertenecen a un Paciente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    // RELACIÓN: Un desarrollo pertenece a una Historia Clínica
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_historia_clinica") 
    private HistoriaClinica historiaClinica;

    @Column(length = 100)
    private String observacion;

    @Column(name = "fecha_evaluacion")
    private LocalDate fechaEvaluacion;

    // ... Resto de tus campos de auditoría (usuario, uuid, etc) ...
    @Column(length = 50) private String usuario;
    @Column(name = "uuid_offline", length = 36) private String uuidOffline;
    @Column(name = "sync_status", length = 20) private String syncStatus;
    @Column(name = "last_modified") private LocalDateTime lastModified;
    @Column(length = 20) private String origin;

    @PrePersist @PreUpdate
    protected void updateAudit() {
        this.lastModified = LocalDateTime.now();
        if (this.uuidOffline == null) this.uuidOffline = java.util.UUID.randomUUID().toString();
        if (this.syncStatus == null) this.syncStatus = "PENDING";
        if (this.fechaEvaluacion == null) this.fechaEvaluacion = LocalDate.now();
    }
}