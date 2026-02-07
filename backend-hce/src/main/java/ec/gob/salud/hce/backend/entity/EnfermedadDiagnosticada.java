package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "enfermedades_diagnosticadas")
public class EnfermedadDiagnosticada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_enfermedad_diagnosticada")
    private Integer idEnfermedadDiagnosticada;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;

    // --- JOINS (RELACIONES) ---
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_enfermedad")
    private Enfermedad enfermedad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_antecedente_patologico_personal")
    private AntecedentePatologicoPersonal antecedentePatologicoPersonal;

    // --------------------------

    @Column(name = "usuario")
    private String usuario;

    @Column(name = "id_personal")
    private Integer idPersonal;
    
    @Column(name = "uuid_offline")
    private String uuidOffline;
    
    @Column(name = "sync_status")
    private String syncStatus;

    // Campo requerido por el Mapper
    @Column(name = "origin")
    private String origin;
}