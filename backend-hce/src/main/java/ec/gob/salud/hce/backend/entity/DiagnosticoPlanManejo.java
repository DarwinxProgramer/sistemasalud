package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "diagnosticos_planes_manejos")
public class DiagnosticoPlanManejo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_diagnostico_plan_manejo")
    private Integer idDiagnosticoPlanManejo;

    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;

    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @Column(name = "id_historia_clinica")
    private Integer idHistoriaClinica;

    // Auditoría
    @Column(name = "uuid_offline")
    private String uuidOffline;

    @Column(name = "sync_status")
    private String syncStatus;

    @Column(name = "last_modified")
    private Date lastModified;

    @Column(name = "origin")
    private String origin;
}