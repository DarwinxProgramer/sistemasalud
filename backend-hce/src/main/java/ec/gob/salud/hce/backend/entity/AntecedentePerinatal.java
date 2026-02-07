package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "antecedentes_perinatales")
@Getter
@Setter
public class AntecedentePerinatal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_antecedente_perinatal")
    private Integer idAntecedentePerinatal;

    // --- RELACIÓN 1: PACIENTE (Necesaria para tu Repositorio findByPaciente...) ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paciente")
    private Paciente paciente;

    // --- RELACIÓN 2: HISTORIA CLÍNICA (La que no queríamos quitar) ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_historia_clinica")
    private HistoriaClinica historiaClinica;

    // --- DATOS MÉDICOS (Que faltaban en tu versión anterior) ---
    @Column(name = "embarazo_planificado")
    private Boolean embarazoPlanificado;

    @Column(name = "controles_prenatales")
    private Integer controlesPrenatales;

    @Column(length = 255)
    private String antecedentes;

    @Column(name = "otros_antecedentes", columnDefinition = "TEXT")
    private String otrosAntecedentes;

    // --- AUDITORÍA ---
    @Column(length = 50)
    private String usuario;

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