package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "antecedentes_familiares")
@Getter
@Setter
public class AntecedenteFamiliar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_antecedente_familiar")
    private Integer idAntecedenteFamiliar;

    @Column(name = "enfermedad_hereditaria", length = 150)
    private String enfermedadHereditaria;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "id_enfermedad")
    private Integer idEnfermedad;

    @Column(name = "id_antecedente_perinatal")
    private Integer idAntecedentePerinatal;

    // --- UNIÓN DE INFORMACIÓN ---
    // Relación eliminada porque no existe id_paciente en la tabla
    // antecedentes_familiares

    // Auditoría y Sincronización
    @Column(name = "uuid_offline", length = 36)
    private String uuidOffline;

    @Column(name = "sync_status", length = 20)
    private String syncStatus;

    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    @Column(length = 20)
    private String origin;

    @PrePersist
    protected void onCreate() {
        this.lastModified = LocalDateTime.now();
        if (this.uuidOffline == null)
            this.uuidOffline = java.util.UUID.randomUUID().toString();
        if (this.syncStatus == null)
            this.syncStatus = "PENDING";
        if (this.origin == null)
            this.origin = "WEB";
        if (this.fecha == null)
            this.fecha = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastModified = LocalDateTime.now();
    }
}