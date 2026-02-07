package ec.gob.salud.hce.backend.dto; // Ajusta el paquete si es necesario

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PacienteRequestDTO {
    // No suele llevar ID si es para crear, pero si usas el mismo para update, déjalo
    private Integer idPaciente;

    private String apellidoPaterno;
    private String apellidoMaterno;
    private String primerNombre;
    private String segundoNombre;
    private String cedula;
    
    private String tipoSangre;
    private LocalDate fechaNacimiento;
    private String sexo;
    
    // IDs para relacionar
    private Integer idGrupoEtnico;
    private Integer idParroquia;
    
    // Datos redundantes requeridos
    private Integer idPrqCanton;
    private Integer idPrqCntProvincia;
    
    // Auditoría manual
    private String usuario;
    private String uuidOffline;
    private String origin;
    private Integer idPersonal;

    // LISTA HIJA PARA EL CASCADE (IMPORTANTE)
    // Debes tener creado AntecedenteFamiliarRequestDTO
    private List<AntecedenteFamiliarDTO> antecedentesFamiliares;
    private TutorDTO tutor; 

    public TutorDTO getTutor() { return tutor; }
    public void setTutor(TutorDTO tutor) { this.tutor = tutor; }
}