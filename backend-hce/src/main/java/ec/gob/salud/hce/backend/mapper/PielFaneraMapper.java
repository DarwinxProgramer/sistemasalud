package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.PielFaneraDTO;
import ec.gob.salud.hce.backend.entity.ExamenFisicoSegmentario;
import ec.gob.salud.hce.backend.entity.PielFanera;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PielFaneraMapper {

    public PielFaneraDTO toDTO(PielFanera entity) {
        if (entity == null) return null;

        PielFaneraDTO dto = new PielFaneraDTO();
        dto.setIdPielFanera(entity.getIdPielFanera());
        
        // Mapeo del ID de la relación
        if (entity.getExamenFisicoSegmentario() != null) {
            dto.setIdExamenFisicoSegmentario(entity.getExamenFisicoSegmentario().getIdExamenFisicoSegmentario());
        }

        dto.setIcterisia(entity.getIcterisia());
        dto.setPsianosis(entity.getPsianosis());
        dto.setRash(entity.getRash());
        dto.setOtros(entity.getOtros());
        
        dto.setUsuario(entity.getUsuario());
        dto.setIdPersonal(entity.getIdPersonal());
        dto.setUuidOffline(entity.getUuidOffline());
        dto.setSyncStatus(entity.getSyncStatus());
        dto.setLastModified(entity.getLastModified());
        dto.setOrigin(entity.getOrigin());

        return dto;
    }

    public PielFanera toEntity(PielFaneraDTO dto) {
        if (dto == null) return null;

        PielFanera entity = new PielFanera();
        entity.setIdPielFanera(dto.getIdPielFanera());
        
        // Creación del Proxy para la relación
        if (dto.getIdExamenFisicoSegmentario() != null) {
            ExamenFisicoSegmentario efs = new ExamenFisicoSegmentario();
            efs.setIdExamenFisicoSegmentario(dto.getIdExamenFisicoSegmentario());
            entity.setExamenFisicoSegmentario(efs);
        }

        entity.setIcterisia(dto.getIcterisia());
        entity.setPsianosis(dto.getPsianosis());
        entity.setRash(dto.getRash());
        entity.setOtros(dto.getOtros());

        entity.setUsuario(dto.getUsuario());
        entity.setIdPersonal(dto.getIdPersonal());
        entity.setUuidOffline(dto.getUuidOffline() != null ? dto.getUuidOffline() : UUID.randomUUID().toString());
        entity.setSyncStatus(dto.getSyncStatus() != null ? dto.getSyncStatus() : "PENDING");
        entity.setOrigin(dto.getOrigin() != null ? dto.getOrigin() : "WEB");

        return entity;
    }

    public List<PielFaneraDTO> toDTOList(List<PielFanera> list) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        return list.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}