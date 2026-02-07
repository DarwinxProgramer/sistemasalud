package ec.gob.salud.hce.backend.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AnamnesisResponseDTO {
    private Long idConsulta;
    private LocalDate fechaConsulta;
    private LocalTime horaConsulta;
    
    // Estos son los dos campos clave para la pestaña "Anamnesis"
    private String motivoConsulta;
    private String enfermedadActual;
    
    // Opcional: Nombre del médico si quieres mostrarlo
    private String usuarioMedico; 
}