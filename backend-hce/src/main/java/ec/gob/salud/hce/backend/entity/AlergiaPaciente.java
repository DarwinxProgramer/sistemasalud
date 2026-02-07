package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "alergias_pacientes") // Coincide con el nombre en el diagrama
@Getter
@Setter
public class AlergiaPaciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alergia_paciente")
    private Integer idAlergiaPaciente;

    // --- UNIÓN 1: Línea hacia "pacientes" ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    // --- UNIÓN 2: Línea hacia "alergias" ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_alergia", nullable = false)
    private Alergia alergia;

    // --- CAMPOS ESPECÍFICOS DEL DIAGRAMA ---
    @Column(length = 255) // Reaccion suele ser texto
    private String reaccion;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

    // Relación con Antecedentes Patológicos (Línea en el diagrama)
    // Como no tenemos esa entidad en este contexto, lo dejamos como ID o Future Relation
    @Column(name = "id_antecedente_patologico_personal")
    private Integer idAntecedentePatologicoPersonal;

    @Column(name = "id_personal")
    private Integer idPersonal;

    // --- AUDITORÍA (Presente en el diagrama) ---
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

    @PrePersist @PreUpdate
    protected void updateAudit() {
        this.lastModified = LocalDateTime.now();
        if (this.fechaCreacion == null) this.fechaCreacion = LocalDate.now();
        if (this.uuidOffline == null) this.uuidOffline = java.util.UUID.randomUUID().toString();
        if (this.syncStatus == null) this.syncStatus = "PENDING";
    }
}