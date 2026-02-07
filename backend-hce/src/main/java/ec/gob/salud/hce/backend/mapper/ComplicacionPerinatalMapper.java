package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.ComplicacionPerinatalDTO;
import ec.gob.salud.hce.backend.entity.ComplicacionPerinatal;
import java.util.List;
import java.util.stream.Collectors;

public class ComplicacionPerinatalMapper {

    public static ComplicacionPerinatalDTO toDto(ComplicacionPerinatal entity) {
        if (entity == null) return null;
        ComplicacionPerinatalDTO dto = new ComplicacionPerinatalDTO();
        dto.setIdComplicacionPerinatal(entity.getIdComplicacionPerinatal());
        dto.setDescripcion(entity.getDescripcion());
        dto.setFecha(entity.getFecha());
        dto.setIdDatoGestacional(entity.getIdDatoGestacional());
        dto.setIdEnfermedad(entity.getIdEnfermedad());
        dto.setUsuario(entity.getUsuario());
        dto.setIdPersonal(entity.getIdPersonal());
        dto.setUuidOffline(entity.getUuidOffline());
        dto.setSyncStatus(entity.getSyncStatus());
        dto.setLastModified(entity.getLastModified());
        dto.setOrigin(entity.getOrigin());
        return dto;
    }

    public static ComplicacionPerinatal toEntity(ComplicacionPerinatalDTO dto) {
        if (dto == null) return null;
        ComplicacionPerinatal entity = new ComplicacionPerinatal();
        entity.setDescripcion(dto.getDescripcion());
        entity.setFecha(dto.getFecha());
        entity.setIdDatoGestacional(dto.getIdDatoGestacional());
        entity.setIdEnfermedad(dto.getIdEnfermedad());
        entity.setUsuario(dto.getUsuario());
        entity.setIdPersonal(dto.getIdPersonal());
        entity.setOrigin(dto.getOrigin());
        return entity;
    }

    public static List<ComplicacionPerinatalDTO> toDtoList(List<ComplicacionPerinatal> entities) {
        return entities.stream().map(ComplicacionPerinatalMapper::toDto).collect(Collectors.toList());
    }
}