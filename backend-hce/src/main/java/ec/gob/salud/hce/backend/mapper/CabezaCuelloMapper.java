package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.CabezaCuelloDTO;
import ec.gob.salud.hce.backend.entity.CabezaCuello;
import java.util.List;
import java.util.stream.Collectors;

public class CabezaCuelloMapper {

    public static CabezaCuelloDTO toDto(CabezaCuello entity) {
        if (entity == null) return null;
        CabezaCuelloDTO dto = new CabezaCuelloDTO();
        dto.setIdCabezaCuello(entity.getIdCabezaCuello());
        dto.setIdExamenFisicoSegmentario(entity.getIdExamenFisicoSegmentario());
        dto.setFontaneloAnterior(entity.getFontaneloAnterior());
        dto.setAdenopatia(entity.getAdenopatia());
        dto.setOtros(entity.getOtros());
        dto.setUuidOffline(entity.getUuidOffline());
        dto.setSyncStatus(entity.getSyncStatus());
        dto.setLastModified(entity.getLastModified());
        dto.setOrigin(entity.getOrigin());
        dto.setUsuario(entity.getUsuario());
        dto.setIdPersonal(entity.getIdPersonal());
        return dto;
    }

    public static CabezaCuello toEntity(CabezaCuelloDTO dto) {
        if (dto == null) return null;
        CabezaCuello entity = new CabezaCuello();
        entity.setIdExamenFisicoSegmentario(dto.getIdExamenFisicoSegmentario());
        entity.setFontaneloAnterior(dto.getFontaneloAnterior());
        entity.setAdenopatia(dto.getAdenopatia());
        entity.setOtros(dto.getOtros());
        entity.setUsuario(dto.getUsuario());
        entity.setOrigin(dto.getOrigin());
        entity.setIdPersonal(dto.getIdPersonal());
        return entity;
    }

    public static List<CabezaCuelloDTO> toDtoList(List<CabezaCuello> entities) {
        return entities.stream().map(CabezaCuelloMapper::toDto).collect(Collectors.toList());
    }
}