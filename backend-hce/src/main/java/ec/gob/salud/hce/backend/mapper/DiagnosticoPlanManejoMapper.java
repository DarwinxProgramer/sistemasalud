package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.DiagnosticoPlanManejoDTO;
import ec.gob.salud.hce.backend.entity.DiagnosticoPlanManejo;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

@Component // <--- ESTO ES LO QUE FALTABA
public class DiagnosticoPlanManejoMapper {

    public DiagnosticoPlanManejoDTO toDTO(DiagnosticoPlanManejo entity) {
        if (entity == null) return null;

        DiagnosticoPlanManejoDTO dto = new DiagnosticoPlanManejoDTO();
        dto.setIdDiagnosticoPlanManejo(entity.getIdDiagnosticoPlanManejo());
        dto.setObservacion(entity.getObservacion());
        dto.setFecha(entity.getFecha());
        dto.setIdHistoriaClinica(entity.getIdHistoriaClinica());
        
        // Auditoría
        dto.setUuidOffline(entity.getUuidOffline());
        dto.setSyncStatus(entity.getSyncStatus());
        dto.setLastModified(entity.getLastModified());
        dto.setOrigin(entity.getOrigin());

        return dto;
    }

    public DiagnosticoPlanManejo toEntity(DiagnosticoPlanManejoDTO dto) {
        if (dto == null) return null;

        DiagnosticoPlanManejo entity = new DiagnosticoPlanManejo();
        entity.setIdDiagnosticoPlanManejo(dto.getIdDiagnosticoPlanManejo());
        entity.setObservacion(dto.getObservacion());
        entity.setFecha(dto.getFecha());
        entity.setIdHistoriaClinica(dto.getIdHistoriaClinica());

        // Auditoría
        entity.setUuidOffline(dto.getUuidOffline() != null ? dto.getUuidOffline() : UUID.randomUUID().toString());
        entity.setSyncStatus(dto.getSyncStatus() != null ? dto.getSyncStatus() : "PENDING");
        entity.setOrigin(dto.getOrigin() != null ? dto.getOrigin() : "WEB");
        // lastModified se actualiza en @PrePersist de la entidad

        return entity;
    }

    public List<DiagnosticoPlanManejoDTO> toDTOList(List<DiagnosticoPlanManejo> list) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        return list.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}