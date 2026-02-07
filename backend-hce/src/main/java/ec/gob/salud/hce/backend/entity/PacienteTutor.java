package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter // ✅ Genera getPaciente(), getTutor()...
@Setter
@Entity
@Table(name = "pacientes_tutores")
public class PacienteTutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_paciente")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "id_tutor") // O id_personal, según tu base
    private Tutor tutor;

    @Column(name = "parentesco")
    private String parentesco;
    
    // Getters y Setters...
}