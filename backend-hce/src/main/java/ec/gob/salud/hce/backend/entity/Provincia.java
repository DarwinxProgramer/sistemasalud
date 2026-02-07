package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "provincias")
@Data
public class Provincia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_provincia") // <--- ESTE ES EL CAMBIO CLAVE
    private Long id;
    
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    // Estos campos son opcionales, pero si quieres usarlos:
    @Column(name = "uuid_offline", length = 36)
    private String uuidOffline;

    @Column(name = "sync_status", length = 20)
    private String syncStatus;
}