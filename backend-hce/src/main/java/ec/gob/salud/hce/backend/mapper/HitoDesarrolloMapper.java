package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.HitoDesarrolloDTO;
import ec.gob.salud.hce.backend.entity.DesarrolloPsicomotor;
import ec.gob.salud.hce.backend.entity.HitoDesarrollo;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

@Component
public class HitoDesarrolloMapper {

    public HitoDesarrolloDTO toDTO(HitoDesarrollo entity) {
        if (entity == null) return null;

        HitoDesarrolloDTO dto = new HitoDesarrolloDTO();
        dto.setIdHitoDesarrollo(entity.getIdHitoDesarrollo());
        dto.setSostenCefalio(entity.getSostenCefalio());
        dto.setSedestacion(entity.getSedestacion());
        dto.setDeambulacion(entity.getDeambulacion());
        dto.setLenguaje(entity.getLenguaje());
        dto.setObservacion(entity.getObservacion());

        // Mapeo del ID de la relación
        if (entity.getDesarrolloPsicomotor() != null) {
            dto.setIdDesarrolloPsicomotor(entity.getDesarrolloPsicomotor().getIdDesarrolloPsicomotor());
        }

        dto.setUsuario(entity.getUsuario());
        dto.setIdPersonal(entity.getIdPersonal());
        dto.setUuidOffline(entity.getUuidOffline());
        dto.setSyncStatus(entity.getSyncStatus());
        dto.setLastModified(entity.getLastModified());
        dto.setOrigin(entity.getOrigin());

        return dto;
    }

    public HitoDesarrollo toEntity(HitoDesarrolloDTO dto) {
        if (dto == null) return null;

        HitoDesarrollo entity = new HitoDesarrollo();
        entity.setIdHitoDesarrollo(dto.getIdHitoDesarrollo());
        entity.setSostenCefalio(dto.getSostenCefalio());
        entity.setSedestacion(dto.getSedestacion());
        entity.setDeambulacion(dto.getDeambulacion());
        entity.setLenguaje(dto.getLenguaje());
        entity.setObservacion(dto.getObservacion());

        // Creación del Proxy para la relación
        if (dto.getIdDesarrolloPsicomotor() != null) {
            DesarrolloPsicomotor dp = new DesarrolloPsicomotor();
            dp.setIdDesarrolloPsicomotor(dto.getIdDesarrolloPsicomotor());
            entity.setDesarrolloPsicomotor(dp);
        }

        entity.setUsuario(dto.getUsuario());
        entity.setIdPersonal(dto.getIdPersonal());
        entity.setUuidOffline(dto.getUuidOffline() != null ? dto.getUuidOffline() : UUID.randomUUID().toString());
        entity.setSyncStatus(dto.getSyncStatus() != null ? dto.getSyncStatus() : "PENDING");
        entity.setOrigin(dto.getOrigin() != null ? dto.getOrigin() : "WEB");

        return entity;
    }

    public List<HitoDesarrolloDTO> toDTOList(List<HitoDesarrollo> list) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        return list.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}