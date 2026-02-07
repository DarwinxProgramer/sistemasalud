package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "planes_terapeuticos") // Nombre de la tabla en BD
@Data // Lombok genera automáticamente los Getters y Setters
public class PlanTerapeutico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plan_terapeutico")
    private Long id;

    @Column(name = "manejo_farmacologico")
    private String manejoFarmacologico;

    @Column(name = "manejo_no_farmacologico")
    private String manejoNoFarmacologico;

    @Column(name = "pronostico")
    private String pronostico;

    // Relación con la Consulta
    @ManyToOne
    @JoinColumn(name = "id_consulta")
    @lombok.ToString.Exclude
    private Consulta consulta;
}