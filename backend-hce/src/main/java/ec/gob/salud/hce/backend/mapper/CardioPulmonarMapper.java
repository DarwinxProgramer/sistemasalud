package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.CardioPulmonarDTO;
import ec.gob.salud.hce.backend.entity.CardioPulmonar;
import java.util.List;
import java.util.stream.Collectors;

public class CardioPulmonarMapper {

    public static CardioPulmonarDTO toDto(CardioPulmonar entity) {
        if (entity == null) return null;
        CardioPulmonarDTO dto = new CardioPulmonarDTO();
        dto.setIdCardioPulmonar(entity.getIdCardioPulmonar());
        dto.setIdExamenFisicoSegmentario(entity.getIdExamenFisicoSegmentario());
        dto.setRuidoCardiaco(entity.getRuidoCardiaco());
        dto.setMurmulloVesicular(entity.getMurmulloVesicular());
        dto.setSoplos(entity.getSoplos());
        dto.setCrepitante(entity.getCrepitante());
        dto.setOtros(entity.getOtros());
        dto.setUuidOffline(entity.getUuidOffline());
        dto.setSyncStatus(entity.getSyncStatus());
        dto.setLastModified(entity.getLastModified());
        dto.setOrigin(entity.getOrigin());
        dto.setUsuario(entity.getUsuario());
        dto.setIdPersonal(entity.getIdPersonal());
        return dto;
    }

    public static CardioPulmonar toEntity(CardioPulmonarDTO dto) {
        if (dto == null) return null;
        CardioPulmonar entity = new CardioPulmonar();
        entity.setIdExamenFisicoSegmentario(dto.getIdExamenFisicoSegmentario());
        entity.setRuidoCardiaco(dto.getRuidoCardiaco());
        entity.setMurmulloVesicular(dto.getMurmulloVesicular());
        entity.setSoplos(dto.getSoplos());
        entity.setCrepitante(dto.getCrepitante());
        entity.setOtros(dto.getOtros());
        entity.setUsuario(dto.getUsuario());
        entity.setOrigin(dto.getOrigin());
        entity.setIdPersonal(dto.getIdPersonal());
        return entity;
    }

    public static List<CardioPulmonarDTO> toDtoList(List<CardioPulmonar> entities) {
        return entities.stream().map(CardioPulmonarMapper::toDto).collect(Collectors.toList());
    }
}