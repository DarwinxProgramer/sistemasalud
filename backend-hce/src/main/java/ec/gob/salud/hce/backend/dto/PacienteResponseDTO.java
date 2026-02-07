package ec.gob.salud.hce.backend.dto; // Ajusta el paquete

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PacienteResponseDTO {
    private Integer idPaciente;
    
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String primerNombre;
    private String segundoNombre;
    private String cedula;
    
    private String tipoSangre;
    private LocalDate fechaCreacion;
    private LocalDate fechaNacimiento;
    private String sexo;
    
    // Puedes devolver el objeto completo o solo el ID/Nombre
    private Integer idGrupoEtnico;
    private String nombreGrupoEtnico; // Opcional, si quieres mostrar el nombre
    
    private Integer idParroquia;
    private Integer idPrqCanton;
    private Integer idPrqCntProvincia;
    
    private String usuario;
    private String uuidOffline;
    private String syncStatus;
    private LocalDateTime lastModified;
    private String origin;
    private Integer idPersonal;
    
    private String nombreCompleto;
    private Integer edad;
    private String tipoPaciente;
    
    // Lista de respuesta
    // Debes tener creado AntecedenteFamiliarResponseDTO
    private List<AntecedenteFamiliarDTO> antecedentesFamiliares;
    private TutorDTO tutor; 

    public TutorDTO getTutor() { return tutor; }
    public void setTutor(TutorDTO tutor) { this.tutor = tutor; }
}