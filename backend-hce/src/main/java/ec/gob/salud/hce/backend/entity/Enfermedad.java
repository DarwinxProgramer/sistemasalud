package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity // <--- OBLIGATORIO
@Table(name = "enfermedades") // Asegúrate que este nombre coincida con tu base de datos MySQL
@Getter
@Setter
public class Enfermedad {

    @Id // <--- OBLIGATORIO
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_enfermedad")
    private Integer idEnfermedad;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "nombre")
    private String nombre;
}