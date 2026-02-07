package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.ExamenFisicoSegmentarioDTO;
import ec.gob.salud.hce.backend.entity.ExamenFisicoSegmentario;
import java.util.List;
import java.util.stream.Collectors;

public class ExamenFisicoSegmentarioMapper {

    private ExamenFisicoSegmentarioMapper() { }

    public static ExamenFisicoSegmentarioDTO toDto(ExamenFisicoSegmentario entity) {
        if (entity == null) return null;
        ExamenFisicoSegmentarioDTO dto = new ExamenFisicoSegmentarioDTO();
        dto.setIdExamenFisicoSegmentario(entity.getIdExamenFisicoSegmentario());
        dto.setAspectoGeneral(entity.getAspectoGeneral());
        dto.setPielFaneras(entity.getPielFaneras());
        dto.setCabezaCuello(entity.getCabezaCuello());
        dto.setCardioPulmonar(entity.getCardioPulmonar());
        dto.setAbdomen(entity.getAbdomen());
        dto.setNeurologico(entity.getNeurologico());
        dto.setEvolucionClinica(entity.getEvolucionClinica());
        dto.setIdExamenFisico(entity.getIdExamenFisico());
        
        dto.setUuidOffline(entity.getUuidOffline());
        dto.setSyncStatus(entity.getSyncStatus());
        dto.setLastModified(entity.getLastModified());
        dto.setOrigin(entity.getOrigin());
        return dto;
    }

    public static ExamenFisicoSegmentario toEntity(ExamenFisicoSegmentarioDTO dto) {
        if (dto == null) return null;
        ExamenFisicoSegmentario entity = new ExamenFisicoSegmentario();
        entity.setIdExamenFisicoSegmentario(dto.getIdExamenFisicoSegmentario());
        entity.setAspectoGeneral(dto.getAspectoGeneral());
        entity.setPielFaneras(dto.getPielFaneras());
        entity.setCabezaCuello(dto.getCabezaCuello());
        entity.setCardioPulmonar(dto.getCardioPulmonar());
        entity.setAbdomen(dto.getAbdomen());
        entity.setNeurologico(dto.getNeurologico());
        entity.setEvolucionClinica(dto.getEvolucionClinica());
        entity.setIdExamenFisico(dto.getIdExamenFisico());
        entity.setOrigin(dto.getOrigin());
        return entity;
    }

    public static List<ExamenFisicoSegmentarioDTO> toDtoList(List<ExamenFisicoSegmentario> entities) {
        if (entities == null) return null;
        return entities.stream().map(ExamenFisicoSegmentarioMapper::toDto).collect(Collectors.toList());
    }
}