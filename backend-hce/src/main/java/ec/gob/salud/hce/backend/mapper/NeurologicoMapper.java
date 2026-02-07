package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.NeurologicoDTO;
import ec.gob.salud.hce.backend.entity.ExamenFisicoSegmentario;
import ec.gob.salud.hce.backend.entity.Neurologico;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

@Component
public class NeurologicoMapper {

    public NeurologicoDTO toDTO(Neurologico entity) {
        if (entity == null) return null;

        NeurologicoDTO dto = new NeurologicoDTO();
        dto.setIdNeurologico(entity.getIdNeurologico());
        
        // Mapeo del ID de la relación
        if (entity.getExamenFisicoSegmentario() != null) {
            dto.setIdExamenFisicoSegmentario(entity.getExamenFisicoSegmentario().getIdExamenFisicoSegmentario());
        }

        dto.setReflejoOsteotendinoso(entity.getReflejoOsteotendinoso());
        dto.setEstadoMental(entity.getEstadoMental());
        dto.setTonoMuscular(entity.getTonoMuscular());
        dto.setOtros(entity.getOtros());
        
        dto.setUsuario(entity.getUsuario());
        dto.setIdPersonal(entity.getIdPersonal());
        dto.setUuidOffline(entity.getUuidOffline());
        dto.setSyncStatus(entity.getSyncStatus());
        dto.setLastModified(entity.getLastModified());
        dto.setOrigin(entity.getOrigin());

        return dto;
    }

    public Neurologico toEntity(NeurologicoDTO dto) {
        if (dto == null) return null;

        Neurologico entity = new Neurologico();
        entity.setIdNeurologico(dto.getIdNeurologico());
        
        // Creación del Proxy para la relación
        if (dto.getIdExamenFisicoSegmentario() != null) {
            ExamenFisicoSegmentario efs = new ExamenFisicoSegmentario();
            efs.setIdExamenFisicoSegmentario(dto.getIdExamenFisicoSegmentario());
            entity.setExamenFisicoSegmentario(efs);
        }

        entity.setReflejoOsteotendinoso(dto.getReflejoOsteotendinoso());
        entity.setEstadoMental(dto.getEstadoMental());
        entity.setTonoMuscular(dto.getTonoMuscular());
        entity.setOtros(dto.getOtros());

        entity.setUsuario(dto.getUsuario());
        entity.setIdPersonal(dto.getIdPersonal());
        entity.setUuidOffline(dto.getUuidOffline() != null ? dto.getUuidOffline() : UUID.randomUUID().toString());
        entity.setSyncStatus(dto.getSyncStatus() != null ? dto.getSyncStatus() : "PENDING");
        entity.setOrigin(dto.getOrigin() != null ? dto.getOrigin() : "WEB");

        return entity;
    }

    public List<NeurologicoDTO> toDTOList(List<Neurologico> list) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        return list.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}