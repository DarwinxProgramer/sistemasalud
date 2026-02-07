package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.AlimentacionDTO;
import ec.gob.salud.hce.backend.entity.Alimentacion;
import ec.gob.salud.hce.backend.entity.DesarrolloPsicomotor;
import java.util.List;
import java.util.stream.Collectors;

public class AlimentacionMapper {

    private AlimentacionMapper() { }

    public static AlimentacionDTO toDto(Alimentacion entity) {
        if (entity == null) return null;

        AlimentacionDTO dto = new AlimentacionDTO();
        dto.setIdAlimentacion(entity.getIdAlimentacion());
        dto.setDescripcion(entity.getDescripcion());
        dto.setTipoLactancia(entity.getTipoLactancia());
        dto.setEdadLactancia(entity.getEdadLactancia());
        dto.setTipo(entity.getTipo());
        dto.setEdadAblactacion(entity.getEdadAblactacion());

        // UNIÓN: Extraer ID del objeto padre
        if (entity.getDesarrolloPsicomotor() != null) {
            dto.setIdDesarrolloPsicomotor(entity.getDesarrolloPsicomotor().getIdDesarrolloPsicomotor());
        }

        dto.setUuidOffline(entity.getUuidOffline());
        dto.setSyncStatus(entity.getSyncStatus());
        dto.setLastModified(entity.getLastModified());
        dto.setOrigin(entity.getOrigin());

        return dto;
    }

    public static Alimentacion toEntity(AlimentacionDTO dto) {
        if (dto == null) return null;

        Alimentacion entity = new Alimentacion();
        entity.setIdAlimentacion(dto.getIdAlimentacion());
        entity.setDescripcion(dto.getDescripcion());
        entity.setTipoLactancia(dto.getTipoLactancia());
        entity.setEdadLactancia(dto.getEdadLactancia());
        entity.setTipo(dto.getTipo());
        entity.setEdadAblactacion(dto.getEdadAblactacion());
        
        // Asignación del objeto padre para persistencia (Referencia simple por ID)
        if (dto.getIdDesarrolloPsicomotor() != null) {
            DesarrolloPsicomotor dp = new DesarrolloPsicomotor();
            dp.setIdDesarrolloPsicomotor(dto.getIdDesarrolloPsicomotor());
            entity.setDesarrolloPsicomotor(dp);
        }

        entity.setOrigin(dto.getOrigin());
        entity.setUuidOffline(dto.getUuidOffline());
        return entity;
    }

    public static List<AlimentacionDTO> toDtoList(List<Alimentacion> entities) {
        if (entities == null) return null;
        return entities.stream().map(AlimentacionMapper::toDto).collect(Collectors.toList());
    }
}