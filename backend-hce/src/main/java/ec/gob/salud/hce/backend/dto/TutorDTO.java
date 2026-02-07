package ec.gob.salud.hce.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TutorDTO {
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String parentesco;
    private String telefono;
    private String nivelEducativo;
    private String direccion;
    
    // IDs de Ubicación del Tutor
    private Integer idParroquia; 
    private Integer provincia; // Opcional, si lo usas
    private Integer canton;    // Opcional, si lo usas
}