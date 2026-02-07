package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "examenes_fisicos")
@Getter
@Setter
public class ExamenFisico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_examen_fisico")
    private Integer idExamenFisico;

    @Column(name = "id_historia_clinica")
    private Integer idHistoriaClinica;

    // --- RELACIÓN CON PACIENTE (Obligatoria para que el mappedBy funcione) ---
    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    // --- RELACIONES EXISTENTES ---
    @OneToOne
    @JoinColumn(name = "id_signo_vital", referencedColumnName = "id_signo_vital")
    private SignoVital signoVital;
    
    @OneToOne
    @JoinColumn(name = "id_examen_fisico_segmentario", referencedColumnName = "id_examen_fisico_segmentario")
    private ExamenFisicoSegmentario examenFisicoSegmentario;

    // NOTA: Se eliminó el campo Integer idExamenFisicoSegmentario porque ya está 
    // definido arriba en la relación @OneToOne.

    // --- AUDITORÍA ---
    @Column(name = "uuid_offline")
    private String uuidOffline;
    
    @Column(name = "sync_status")
    private String syncStatus;
    
    @Column(name = "last_modified")
    private LocalDateTime lastModified;
    
    @Column(name = "origin")
    private String origin;

    @PrePersist
    protected void onCreate() {
        this.lastModified = LocalDateTime.now();
        if (this.uuidOffline == null) {
            this.uuidOffline = java.util.UUID.randomUUID().toString();
        }
        if (this.syncStatus == null) {
            this.syncStatus = "PENDING";
        }
        if (this.origin == null) {
            this.origin = "WEB";
        }
    }
}