package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "signos_vitales")
@Getter
@Setter
public class SignoVital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_signo_vital")
    private Integer idSignoVital;

    // --- RELACIÓN: Un signo vital pertenece a un Examen Físico ---
    // Reemplazamos "Integer idExamenFisico" por la entidad real.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_examen_fisico")
    private ExamenFisico examenFisico;

    // NOTA: Al no poner @Column con scale, se soluciona el error "scale has no meaning..."
    private Double peso;
    
    @Column(name = "talla_longitud") // A veces es bueno explicitar el nombre si usa guion bajo
    private Double tallaLongitud;
    
    @Column(name = "perimetro_cefalico")
    private Double perimetroCefalico;
    
    private Double temperatura;
    
    @Column(name = "frecuencia_cardiaca")
    private Integer frecuenciaCardiaca;
    
    @Column(name = "frecuencia_respiratoria")
    private Integer frecuenciaRespiratoria;
    
    @Column(name = "presion_arterial_sistolica")
    private Integer presionArterialSistolica;
    
    @Column(name = "presion_arterial_diastolica")
    private Integer presionArterialDiastolica;
    
    @Column(name = "saturacion_oxigeno")
    private Integer saturacionOxigeno;
    
    @Column(name = "IMC")
    private Double imc;

    @Column(length = 50)
    private String puntuacion;

    @Column(length = 50)
    private String observacion;

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