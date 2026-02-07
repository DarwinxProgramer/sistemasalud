package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.AntecedenteFamiliarDTO;
import ec.gob.salud.hce.backend.entity.AntecedenteFamiliar;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component // 1. Ahora es un componente de Spring
public class AntecedenteFamiliarMapper {

    // --- DE ENTITY A DTO ---
    public AntecedenteFamiliarDTO toDTO(AntecedenteFamiliar entity) {
        if (entity == null)
            return null;

        AntecedenteFamiliarDTO dto = new AntecedenteFamiliarDTO();
        dto.setIdAntecedenteFamiliar(entity.getIdAntecedenteFamiliar());

        // Mapeo de tus campos
        dto.setEnfermedadHereditaria(entity.getEnfermedadHereditaria());
        dto.setDescripcion(entity.getDescripcion());
        dto.setFecha(entity.getFecha());
        dto.setIdEnfermedad(entity.getIdEnfermedad());

        // DTO field idPaciente ignored as Entity no longer has link

        // Auditoría
        dto.setUuidOffline(entity.getUuidOffline());
        dto.setSyncStatus(entity.getSyncStatus());
        dto.setLastModified(entity.getLastModified());
        dto.setOrigin(entity.getOrigin());

        return dto;
    }

    // --- DE DTO A ENTITY (Este faltaba y es crucial) ---
    public AntecedenteFamiliar toEntity(AntecedenteFamiliarDTO dto) {
        if (dto == null)
            return null;

        AntecedenteFamiliar entity = new AntecedenteFamiliar();
        entity.setIdAntecedenteFamiliar(dto.getIdAntecedenteFamiliar());

        entity.setEnfermedadHereditaria(dto.getEnfermedadHereditaria());
        entity.setDescripcion(dto.getDescripcion());
        entity.setFecha(dto.getFecha());
        entity.setIdEnfermedad(dto.getIdEnfermedad());

        // Nota: El Paciente se asigna en el PacienteMapper, aquí lo dejamos null o
        // asignamos ID si es necesario
        // Entity no longer has Paciente field (unidirectional from
        // Paciente->Antecedente)

        // Auditoría
        entity.setUuidOffline(dto.getUuidOffline() != null ? dto.getUuidOffline() : UUID.randomUUID().toString());
        entity.setSyncStatus(dto.getSyncStatus() != null ? dto.getSyncStatus() : "PENDING");
        entity.setOrigin(dto.getOrigin() != null ? dto.getOrigin() : "WEB");

        return entity;
    }

    // --- LISTAS (Corregido nombre a toDTOList) ---
    public List<AntecedenteFamiliarDTO> toDTOList(List<AntecedenteFamiliar> entities) {
        if (entities == null)
            return Collections.emptyList();
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}