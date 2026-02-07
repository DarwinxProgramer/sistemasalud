package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
// ESTA LÍNEA ES LA QUE CONECTA CON LA BASE DE DATOS
@Table(name = "grupos_etnicos") 
public class GrupoEtnico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_grupo_etnico")
    private Integer idGrupoEtnico;

    @Column(name = "descripcion")
    private String descripcion;
    
    // ¡YA NO PONEMOS NADA MÁS! 
    // Porque tu tabla en la imagen solo tiene datos útiles en estas columnas.
    // Aunque la tabla tenga columnas como 'fecha' o 'uuid', si están vacías o dan error, mejor ignorarlas en Java por ahora.
}