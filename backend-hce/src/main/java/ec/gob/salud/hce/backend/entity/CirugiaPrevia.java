package ec.gob.salud.hce.backend.entity; // <--- ESTO FALTABA
import ec.gob.salud.hce.backend.entity.Paciente; // Importar Paciente
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Table(name = "cirugias_previas")
@Getter
@Setter
public class CirugiaPrevia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cirugia_previa")
    private Integer idCirugiaPrevia;

    // --- NUEVA RELACIÓN ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paciente")
    private Paciente paciente;
    // ---------------------

    @Column(length = 200)
    private String tipo;

    private LocalDate fecha;

    @Column(name = "id_antecedente_patologico_personal")
    private Integer idAntecedentePatologicoPersonal;

    // ... resto de campos (usuario, idPersonal, auditoría) igual ...
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