package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "antecedentes_inmunizaciones")
@Getter
@Setter
public class AntecedenteInmunizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_antecedente_inmunizacion")
    private Integer idAntecedenteInmunizacion;

    // --- CORRECCIÓN CLAVE: RELACIÓN OBJETO, NO SOLO INTEGER ---
    // Esto es lo que busca tu repositorio "findByHistoriaClinica..."
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_historia_clinica")
    private HistoriaClinica historiaClinica;

    @Column(name = "id_antecedente_perinatal")
    private Integer idAntecedentePerinatal;

    @Column(name = "estado_vacunacion", length = 150)
    private String estadoVacunacion;

    @Column(name = "fecha_vacunacion")
    private LocalDate fechaVacunacion;

    @Column(name = "descripcion", length = 150)
    private String descripcion;

    // Auditoría
    @Column(length = 50)
    private String usuario;
    @Column(name = "id_personal")
    private Integer idPersonal;
    @Column(name = "uuid_offline", length = 36)
    private String uuidOffline;
    @Column(name = "sync_status", length = 20)
    private String syncStatus;
    @Column(name = "last_modified")
    private LocalDateTime lastModified;
    @Column(length = 20)
    private String origin;

    @PrePersist @PreUpdate
    protected void updateAudit() {
        this.lastModified = LocalDateTime.now();
        if (this.uuidOffline == null) this.uuidOffline = java.util.UUID.randomUUID().toString();
        if (this.syncStatus == null) this.syncStatus = "PENDING";
    }
}