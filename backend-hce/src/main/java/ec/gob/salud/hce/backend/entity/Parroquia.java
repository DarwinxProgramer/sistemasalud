package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "parroquias")
@Data
public class Parroquia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_parroquia") // Verifica si en tu DB es 'id_parroquia'
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_canton") // El nombre de la columna llave foránea en tu DB
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Canton canton;
}