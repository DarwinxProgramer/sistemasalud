package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.AntecedentePatologicoPersonalDTO;
import ec.gob.salud.hce.backend.entity.AntecedentePatologicoPersonal;
import java.util.List;
import java.util.stream.Collectors;

public class AntecedentePatologicoPersonalMapper {

    public static AntecedentePatologicoPersonalDTO toDto(AntecedentePatologicoPersonal entity) {
        if (entity == null) return null;
        AntecedentePatologicoPersonalDTO dto = new AntecedentePatologicoPersonalDTO();
        dto.setIdAntecedentePatologicoPersonal(entity.getIdAntecedentePatologicoPersonal());
        dto.setIdAntecedentePerinatal(entity.getIdAntecedentePerinatal());
        dto.setObservaciones(entity.getObservaciones());
        dto.setUsuario(entity.getUsuario());
        dto.setUuidOffline(entity.getUuidOffline());
        dto.setSyncStatus(entity.getSyncStatus());
        dto.setLastModified(entity.getLastModified());
        dto.setOrigin(entity.getOrigin());
        return dto;
    }

    public static AntecedentePatologicoPersonal toEntity(AntecedentePatologicoPersonalDTO dto) {
        if (dto == null) return null;
        AntecedentePatologicoPersonal entity = new AntecedentePatologicoPersonal();
        entity.setIdAntecedentePerinatal(dto.getIdAntecedentePerinatal());
        entity.setObservaciones(dto.getObservaciones());
        entity.setUsuario(dto.getUsuario());
        entity.setOrigin(dto.getOrigin());
        return entity;
    }

    public static List<AntecedentePatologicoPersonalDTO> toDtoList(List<AntecedentePatologicoPersonal> entities) {
        return entities.stream().map(AntecedentePatologicoPersonalMapper::toDto).collect(Collectors.toList());
    }
}