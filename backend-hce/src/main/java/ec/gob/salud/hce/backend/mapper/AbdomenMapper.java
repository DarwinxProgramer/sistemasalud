package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.AbdomenDTO;
import ec.gob.salud.hce.backend.entity.Abdomen;
import ec.gob.salud.hce.backend.entity.ExamenFisicoSegmentario; // Importante importar la entidad padre
import java.util.List;
import java.util.stream.Collectors;

public class AbdomenMapper {

    public static AbdomenDTO toDto(Abdomen entity) {
        if (entity == null) return null;
        AbdomenDTO dto = new AbdomenDTO();
        
        dto.setIdAbdomen(entity.getIdAbdomen());
        
        // --- EXTRAER ID DEL OBJETO ---
        if (entity.getExamenFisicoSegmentario() != null) {
            dto.setIdExamenFisicoSegmentario(entity.getExamenFisicoSegmentario().getIdExamenFisicoSegmentario());
        }
        
        dto.setBlando(entity.getBlando());
        dto.setDepresible(entity.getDepresible());
        dto.setDolorPalpacion(entity.getDolorPalpacion());
        dto.setHepatomegalia(entity.getHepatomegalia());
        dto.setEsplenomegalia(entity.getEsplenomegalia());
        dto.setOtros(entity.getOtros());
        
        dto.setUuidOffline(entity.getUuidOffline());
        dto.setSyncStatus(entity.getSyncStatus());
        dto.setLastModified(entity.getLastModified());
        dto.setOrigin(entity.getOrigin());
        dto.setUsuario(entity.getUsuario());
        dto.setIdPersonal(entity.getIdPersonal());
        return dto;
    }

    public static Abdomen toEntity(AbdomenDTO dto) {
        if (dto == null) return null;
        Abdomen entity = new Abdomen();
        
        // --- CREAR OBJETO DESDE ID ---
        if (dto.getIdExamenFisicoSegmentario() != null) {
            ExamenFisicoSegmentario efs = new ExamenFisicoSegmentario();
            efs.setIdExamenFisicoSegmentario(dto.getIdExamenFisicoSegmentario());
            entity.setExamenFisicoSegmentario(efs);
        }
        
        entity.setBlando(dto.getBlando());
        entity.setDepresible(dto.getDepresible());
        entity.setDolorPalpacion(dto.getDolorPalpacion());
        entity.setHepatomegalia(dto.getHepatomegalia());
        entity.setEsplenomegalia(dto.getEsplenomegalia());
        entity.setOtros(dto.getOtros());
        
        entity.setOrigin(dto.getOrigin());
        entity.setUsuario(dto.getUsuario());
        entity.setIdPersonal(dto.getIdPersonal());
        
        return entity;
    }

    public static List<AbdomenDTO> toDtoList(List<Abdomen> entities) {
        if (entities == null) return java.util.Collections.emptyList();
        return entities.stream().map(AbdomenMapper::toDto).collect(Collectors.toList());
    }
}