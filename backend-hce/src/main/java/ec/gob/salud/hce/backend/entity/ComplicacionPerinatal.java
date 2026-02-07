package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "complicaciones_perinatales")
@Getter
@Setter
public class ComplicacionPerinatal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_complicacion_perinatal")
    private Integer idComplicacionPerinatal;

    @Column(length = 150)
    private String descripcion;

    private LocalDate fecha;

    @Column(name = "id_dato_gestacional")
    private Integer idDatoGestacional;

    @Column(name = "id_enfermedad")
    private Integer idEnfermedad;

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

    @PrePersist
    @PreUpdate
    protected void updateAudit() {
        this.lastModified = LocalDateTime.now();
        if (this.uuidOffline == null) this.uuidOffline = java.util.UUID.randomUUID().toString();
        if (this.syncStatus == null) this.syncStatus = "PENDING";
    }
}