package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "examenes_fisicos_segmentarios")
@Getter
@Setter
public class ExamenFisicoSegmentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_examen_fisico_segmentario")
    private Integer idExamenFisicoSegmentario;

    @Column(name = "aspecto_general", columnDefinition = "TEXT")
    private String aspectoGeneral;

    @Column(name = "piel_faneras", columnDefinition = "TEXT")
    private String pielFaneras;

    @Column(name = "cabeza_cuello", columnDefinition = "TEXT")
    private String cabezaCuello;

    @Column(name = "cardio_pulmonar", columnDefinition = "TEXT")
    private String cardioPulmonar;

    @Column(columnDefinition = "TEXT")
    private String abdomen;

    @Column(columnDefinition = "TEXT")
    private String neurologico;

    @Column(name = "evolucion_clinica", columnDefinition = "TEXT")
    private String evolucionClinica;

    // --- UNIÓN ---
    @Column(name = "id_examen_fisico")
    private Integer idExamenFisico;

    // Auditoría
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