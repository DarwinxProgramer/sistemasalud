package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.HistoriaClinicaResponseDTO;
import ec.gob.salud.hce.backend.entity.HistoriaClinica;
import ec.gob.salud.hce.backend.entity.Paciente;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HistoriaClinicaMapper {

    public static HistoriaClinica toEntity(HistoriaClinicaResponseDTO dto, Paciente paciente) {
        if (dto == null) return null;

        HistoriaClinica entity = new HistoriaClinica();
        entity.setIdHistoriaClinica(dto.getIdHistoriaClinica());
        entity.setPaciente(paciente);
        entity.setUsuario(dto.getUsuario());
        
        // Se eliminó setDiagnosticoPlanManejo porque ya no existe en la Entidad
        
        return entity;
    }

    public static HistoriaClinicaResponseDTO toDto(HistoriaClinica entity) {
        if (entity == null) return null;

        HistoriaClinicaResponseDTO dto = new HistoriaClinicaResponseDTO();
        dto.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        
        // CORRECCIÓN AQUÍ: Convertimos Integer a Long
        if (entity.getPaciente() != null && entity.getPaciente().getIdPaciente() != null) {
            dto.setIdPaciente(entity.getPaciente().getIdPaciente().longValue());
        }

        if (entity.getFechaCreacion() != null) {
            dto.setFechaCreacion(entity.getFechaCreacion().atStartOfDay());
        }

        dto.setUsuario(entity.getUsuario());
        
        return dto;
    }

    public static List<HistoriaClinicaResponseDTO> toDtoList(List<HistoriaClinica> entities) {
        return entities.stream()
                .map(HistoriaClinicaMapper::toDto)
                .collect(Collectors.toList());
    }
}