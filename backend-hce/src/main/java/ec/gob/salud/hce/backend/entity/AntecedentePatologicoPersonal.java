package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "antecedentes_patologicos_personales")
@Getter
@Setter
public class AntecedentePatologicoPersonal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_antecedente_patologico_personal")
    private Integer idAntecedentePatologicoPersonal;

    @Column(name = "id_antecedente_perinatal")
    private Integer idAntecedentePerinatal;

    @Column(length = 20)
    private String observaciones;

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