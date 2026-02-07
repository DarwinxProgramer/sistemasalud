package ec.gob.salud.hce.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "personal") // Aquí vinculamos con tu tabla real
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_personal") // Nombre exacto de tu columna SQL
    private Integer idPersonal;

    @Column(name = "usuario", nullable = false, unique = true)
    private String username;

    // Nota: Necesitarás agregar una columna 'password' a tu tabla 'personal' 
    // en SQL si vas a manejar login, ya que no estaba en tu lista inicial.
    @Column(nullable = false)
    private String password;

    @Column(name = "nombres")
    private String nombres;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "cargo")
    private String cargo;

}