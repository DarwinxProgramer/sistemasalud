package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonManagedReference; // Para manejar la lista de consultas

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pacientes")
@Getter
@Setter
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private Integer idPaciente;

    // --- DATOS PERSONALES ---
    @Column(name = "cedula", nullable = false, length = 10)
    private String cedula;

    @Column(name = "primer_nombre", length = 60)
    private String primerNombre;

    @Column(name = "segundo_nombre", length = 60)
    private String segundoNombre;

    @Column(name = "apellido_paterno", length = 60)
    private String apellidoPaterno;

    @Column(name = "apellido_materno", length = 60)
    private String apellidoMaterno;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "sexo", length = 20)
    private String sexo;

    @Column(name = "tipo_sangre", length = 10)
    private String tipoSangre;

    // --- UBICACIÓN ---
    @Column(name = "id_grupo_etnico")
    private Integer idGrupoEtnico;

    @Column(name = "id_parroquia")
    private Integer idParroquia;

    @Column(name = "id_prq_canton")
    private Integer idPrqCanton;

    @Column(name = "id_prq_cnt_provincia")
    private Integer idPrqCntProvincia;

    // --- AUDITORÍA ---
    @Column(name = "usuario")
    private String usuario;

    @Column(name = "id_personal")
    private Integer idPersonal;

    @Column(name = "uuid_offline")
    private String uuidOffline;

    @Column(name = "sync_status")
    private String syncStatus;

    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    @Column(name = "origin")
    private String origin;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

    // ========================================================================
    // --- RELACIONES E IDs DE ANTECEDENTES (LA CORRECCIÓN) ---
    // ========================================================================

    // 1. Historia Clínica (Lista de Consultas)
    // Esto es necesario para que el PacienteController pueda fusionar los 24
    // registros
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // Permite que se serialice la lista al enviar el JSON
    private List<Consulta> historiaClinica = new ArrayList<>();

    // 2. Relación con Tutores (a través de pacientes_tutores)
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PacienteTutor> pacientesTutores = new ArrayList<>();

    // 2. IDs que SÍ existen en tu tabla 'pacientes' (Mapeo exacto)
    @Column(name = "id_psicomotor") // <-- Nombre real en tu BD
    private String idAntecedentesDesarrollo;

    @Column(name = "id_antecedente_familiar") // <-- Nombre real en tu BD
    private String idAntecedentesFamiliares;

    @Column(name = "id_examen_fisico") // <-- Nombre real en tu BD
    private String idExamenFisico;

    @Column(name = "id_diagnostico_plan_manejo") // <-- Nombre real en tu BD
    private String idDiagnosticoPlanManejo;

    // 3. IDs que NO están en 'pacientes' (están en historia_clinica u otro lado)
    // Usamos @Transient para que Java los use en memoria (Controller) pero no
    // intente leerlos de la BD.

    @Transient
    private String idAntecedentesPerinatales;

    @Transient
    private String idAntecedentesInmunizaciones;

    // Relación antigua (si la sigues usando, la dejo con JsonIgnore para no
    // molestar)
    // Relación eliminada porque no existe mapeo bidireccional válido

    // ========================================================================
    // --- CAMPOS CALCULADOS ---
    // ========================================================================

    @JsonProperty("nombreCompleto")
    public String getNombreCompleto() {
        String pNombre = (primerNombre != null) ? primerNombre : "";
        String sNombre = (segundoNombre != null) ? segundoNombre : "";
        String pApellido = (apellidoPaterno != null) ? apellidoPaterno : "";
        String sApellido = (apellidoMaterno != null) ? apellidoMaterno : "";
        return (pNombre + " " + sNombre + " " + pApellido + " " + sApellido).trim().replace("  ", " ");
    }

    @JsonProperty("edad")
    public Integer getEdad() {
        if (this.fechaNacimiento == null)
            return 0;
        return Period.between(this.fechaNacimiento, LocalDate.now()).getYears();
    }

    @JsonProperty("tipoPaciente")
    public String getTipoPaciente() {
        return (getEdad() < 18) ? "Pediátrico" : "Adulto";
    }

    // ========================================================================
    // --- EVENTOS AUTOMÁTICOS ---
    // ========================================================================
    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDate.now();
        this.lastModified = LocalDateTime.now();
        if (this.uuidOffline == null)
            this.uuidOffline = java.util.UUID.randomUUID().toString();
        if (this.syncStatus == null)
            this.syncStatus = "PENDING";
        if (this.origin == null)
            this.origin = "WEB";
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastModified = LocalDateTime.now();
    }
}