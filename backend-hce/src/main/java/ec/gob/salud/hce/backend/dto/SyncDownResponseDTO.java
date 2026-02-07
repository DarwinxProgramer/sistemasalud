package ec.gob.salud.hce.backend.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class SyncDownResponseDTO {
    private List<PacienteResponseDTO> pacientes;
    private List<AntecedenteFamiliarDTO> antecedentesFamiliares;
    private List<AlergiaPacienteDTO> alergiasPaciente; // Agrega la entidad AlergiaPacienteTO> alergias;
    private List<AlimentacionDTO> alimentacion;
    private List<ConsultaDTO> consultas; // Agregar consultas completas
    // Agrega aquí todas las tablas que el sistema necesite offline
}