package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "estudios_laboratorios")
@Data // Genera automáticamente getId(), getTipo(), etc.
public class EstudioLaboratorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estudio_laboratorio")
    private Long id;

    @Column(name = "solicitados")
    private String solicitados;

    @Column(name = "resultados_relevantes")
    private String resultadosRelevantes;

    @Column(name = "fecha")
    private LocalDate fecha;

    // Relación con Consulta
    @ManyToOne
    @JoinColumn(name = "id_consulta")
    @lombok.ToString.Exclude
    private Consulta consulta;
}