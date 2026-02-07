package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "alergias")
@Data
public class Alergia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alergia")
    private Long idAlergia; // El diagrama sugiere ID numérico, mantenemos Long por compatibilidad

    @Column(name = "tipo_alergia", length = 150)
    private String tipoAlergia;

    @Column(length = 50)
    private String estado;

    @Column(length = 50) // El diagrama no especifica long., pero tu código anterior usaba 50
    private String observaciones;

    // --- RELACIÓN INVERSA (Opcional pero recomendada por el diagrama) ---
    // Una alergia puede estar en muchos registros de pacientes
    @OneToMany(mappedBy = "alergia", fetch = FetchType.LAZY)
    private List<AlergiaPaciente> asignaciones = new ArrayList<>();

    // --- AUDITORÍA (Según diagrama) ---
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

    @PrePersist @PreUpdate
    protected void updateAudit() {
        this.lastModified = LocalDateTime.now();
        if (this.uuidOffline == null) this.uuidOffline = java.util.UUID.randomUUID().toString();
        if (this.syncStatus == null) this.syncStatus = "PENDING";
    }
}