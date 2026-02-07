package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "hospitalizaciones_previas")
public class HospitalizacionPrevia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hospitalizacion_previa")
    private Integer idHospitalizacionPrevia;

    // --- NUEVO: RELACIÓN DIRECTA CON PACIENTE ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paciente")
    private Paciente paciente;

    @Column(name = "causa")
    private String causa;

    @Column(name = "fecha")
    private LocalDate fecha;

    // Relación existente con Antecedentes (La mantenemos por compatibilidad)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_antecedente_patologico_personal")
    private AntecedentePatologicoPersonal antecedentePatologicoPersonal;

    // Auditoría
    @Column(name = "usuario")
    private String usuario;
    @Column(name = "id_personal")
    private Integer idPersonal;
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