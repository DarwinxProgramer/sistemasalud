package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "tutores")
@Data // Lombok genera automáticamente los Getters y Setters
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tutor")
    private Integer idTutor; // O idPersonal, si prefieres mantener coherencia

    // --- CORRECCIÓN 1: NOMBRES SEPARADOS ---
    // Reemplazamos 'nombreCompletoTutor' por los 4 campos individuales
    // para que coincida con lo que envía el Frontend y el Servicio.
    
    @Column(name = "primer_nombre")
    private String primerNombre;

    @Column(name = "segundo_nombre")
    private String segundoNombre;

    @Column(name = "primer_apellido")
    private String primerApellido;

    @Column(name = "segundo_apellido")
    private String segundoApellido;

    // --- CORRECCIÓN 2: ALINEAR NOMBRES DE VARIABLES ---
    // El servicio usa 'telefono', así que la variable se llama 'telefono'.
    // Pero en la BD la columna se llama 'numero_contacto'.
    @Column(name = "numero_contacto") 
    private String telefono;

    // El servicio usa 'direccion', así que la variable se llama 'direccion'.
    // En la BD la columna se llama 'domicilio_actual'.
    @Column(name = "domicilio_actual")
    private String direccion;

    // --- CAMPOS EXISTENTES (Se mantienen igual) ---
    
    @Column(name = "nivel_educativo")
    private String nivelEducativo;

    @Column(name = "id_parroquia")
    private Integer idParroquia;

    // Estos campos opcionales son útiles si quieres guardar la ubicación completa
    @Column(name = "id_prq_canton")
    private Integer idPrqCanton;

    @Column(name = "id_prq_cnt_provincia")
    private Integer idPrqCntProvincia;

    // --- AUDITORÍA Y SINCRONIZACIÓN ---
    
    @Column(name = "uuid_offline")
    private String uuidOffline;

    private String origin;

    @Column(name = "sync_status")
    private String syncStatus;

    @Column(name = "last_modified", insertable = false, updatable = false)
    private LocalDateTime lastModified;
}