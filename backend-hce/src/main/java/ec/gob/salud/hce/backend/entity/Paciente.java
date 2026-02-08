package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pacientes    ")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPaciente;

    private String cedula;
    private String primerNombre;
    private String segundoNombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private LocalDate fechaNacimiento;
    private String sexo;
    private String tipoSangre;

    private Integer idParroquia;
    private Integer idPrqCanton;
    private Integer idPrqCntProvincia;
    @Column(name = "uuid_offline")
    private String uuidOffline;

    @Column(name = "sync_status")
    private String syncStatus;

    @Column(name = "last_modified")
    private LocalDateTime lastModified;

    @Column(name = "origin")
    private String origin;

    // ===============================
    // 🔥 RELACIÓN CON HISTORIA CLÍNICA (1 a 1)
    // ===============================
    @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private HistoriaClinica historiaClinica;

    // ===============================
    // 🔥 RELACIÓN CON PACIENTE TUTOR (1 a N)
    // ===============================
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PacienteTutor> pacientesTutores = new ArrayList<>();
}
