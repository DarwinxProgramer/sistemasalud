package ec.gob.salud.hce.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "consultas")
@Data
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consulta")
    private Long idConsulta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paciente")
    @JsonBackReference
    private Paciente paciente;

    // Relaciones con entidades hijas
    @OneToMany(mappedBy = "consulta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @lombok.ToString.Exclude
    private java.util.Set<PlanTerapeutico> planes = new java.util.HashSet<>();

    @OneToMany(mappedBy = "consulta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @lombok.ToString.Exclude
    private java.util.Set<EstudioLaboratorio> estudios = new java.util.HashSet<>();

    @Column(name = "id_historia_clinica")
    private Integer idHistoriaClinica;

    @Column(name = "fecha_atencion")
    private LocalDate fechaConsulta;

    @Column(name = "hora_consulta")
    private LocalTime horaConsulta;

    @Column(name = "motivo_consulta")
    private String motivoConsulta;

    @Column(name = "enfermedad_actual", columnDefinition = "TEXT")
    private String enfermedadActual;

    private Double peso;
    private Double talla;
    private Double temperatura;

    @Column(name = "frecuencia_cardiaca")
    private Integer frecuenciaCardiaca;

    @Column(name = "frecuencia_respiratoria")
    private Integer frecuenciaRespiratoria;

    private Integer saturacion;

    @Column(name = "diagnostico_principal")
    private String diagnosticoPrincipal;

    @Column(name = "tipo_diagnostico")
    private String tipoDiagnostico;

    @Column(name = "datos_completos_json", columnDefinition = "LONGTEXT")
    private String datosCompletosJson;

    @Column(name = "usuario_medico")
    private String usuarioMedico;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

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
        this.fechaCreacion = LocalDateTime.now();
    }
}