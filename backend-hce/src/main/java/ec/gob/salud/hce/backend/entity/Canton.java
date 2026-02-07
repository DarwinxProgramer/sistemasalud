package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // <--- Import clave

@Entity
@Table(name = "cantones")
@Data
public class Canton {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_canton")
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_provincia") // Verifica que coincida con tu DB
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
    private Provincia provincia;
}