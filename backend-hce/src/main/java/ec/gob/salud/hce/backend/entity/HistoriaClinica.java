package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "historias_clinicas")
@Getter
@Setter
public class HistoriaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historia_clinica")
    private Long idHistoriaClinica;

    // 🔥 Relación con paciente
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    // =========================
    // CAMPOS CLÍNICOS
    // =========================

    @Column(name = "id_grupo_etnico")
    private Integer idGrupoEtnico;

    @Column(name = "usuario", length = 50)
    private String usuario;

    @Column(name = "id_personal")
    private Integer idPersonal;

    @Column(name = "tipo_paciente", length = 20)
    private String tipoPaciente;

    @Column(name = "id_antecedentes_perinatales")
    private String idAntecedentesPerinatales;

    @Column(name = "id_antecedentes_inmunizaciones")
    private String idAntecedentesInmunizaciones;

    @Column(name = "id_antecedentes_familiares")
    private String idAntecedentesFamiliares;

    @Column(name = "id_antecedentes_desarrollo")
    private String idAntecedentesDesarrollo;

    // =========================
    // AUDITORÍA
    // =========================

    @Column(name = "uuid_offline")
    private String uuidOffline;

    @Column(name = "sync_status")
    private String syncStatus;

    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    @Column(name = "origin")
    private String origin;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDate.now();
        this.lastModified = LocalDateTime.now();
        if (this.uuidOffline == null)
            this.uuidOffline = java.util.UUID.randomUUID().toString();
        if (this.syncStatus == null)
            this.syncStatus = "PENDING";
        if (this.origin == null)
            this.origin = "WEB";
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastModified = LocalDateTime.now();
    }
}
