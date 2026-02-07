package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.AntecedenteInmunizacionDTO;
import ec.gob.salud.hce.backend.entity.AntecedenteInmunizacion;
import ec.gob.salud.hce.backend.entity.HistoriaClinica;

import java.util.List;
import java.util.stream.Collectors;

public class AntecedenteInmunizacionMapper {

    private AntecedenteInmunizacionMapper() {}

    public static AntecedenteInmunizacionDTO toDto(AntecedenteInmunizacion entity) {
        if (entity == null) return null;
        AntecedenteInmunizacionDTO dto = new AntecedenteInmunizacionDTO();
        
        dto.setIdAntecedenteInmunizacion(entity.getIdAntecedenteInmunizacion());
        
        // Extraer el ID del objeto HistoriaClinica
        if (entity.getHistoriaClinica() != null) {
            // Asumiendo que tu HistoriaClinica usa Long, lo convertimos a Integer si tu DTO usa Integer
            // Si tu DTO usa Long, quita el .intValue()
            if (entity.getHistoriaClinica().getIdHistoriaClinica() != null) {
                dto.setIdHistoriaClinica(entity.getHistoriaClinica().getIdHistoriaClinica().intValue());
            }
        }
        
        dto.setIdAntecedentePerinatal(entity.getIdAntecedentePerinatal());
        dto.setEstadoVacunacion(entity.getEstadoVacunacion());
        dto.setFechaVacunacion(entity.getFechaVacunacion());
        dto.setDescripcion(entity.getDescripcion());
        
        dto.setUsuario(entity.getUsuario());
        dto.setIdPersonal(entity.getIdPersonal());
        dto.setUuidOffline(entity.getUuidOffline());
        dto.setSyncStatus(entity.getSyncStatus());
        dto.setLastModified(entity.getLastModified());
        dto.setOrigin(entity.getOrigin());
        
        return dto;
    }

    public static AntecedenteInmunizacion toEntity(AntecedenteInmunizacionDTO dto) {
        if (dto == null) return null;
        AntecedenteInmunizacion entity = new AntecedenteInmunizacion();
        
        // Convertir el ID Integer a un objeto HistoriaClinica
        if (dto.getIdHistoriaClinica() != null) {
            HistoriaClinica hc = new HistoriaClinica();
            // Convertimos Integer a Long para la entidad HistoriaClinica
            hc.setIdHistoriaClinica(dto.getIdHistoriaClinica().longValue()); 
            entity.setHistoriaClinica(hc);
        }
        
        entity.setIdAntecedentePerinatal(dto.getIdAntecedentePerinatal());
        entity.setEstadoVacunacion(dto.getEstadoVacunacion());
        entity.setFechaVacunacion(dto.getFechaVacunacion());
        entity.setDescripcion(dto.getDescripcion());
        
        entity.setUsuario(dto.getUsuario());
        entity.setIdPersonal(dto.getIdPersonal());
        entity.setOrigin(dto.getOrigin());
        
        if (dto.getUuidOffline() != null) entity.setUuidOffline(dto.getUuidOffline());
        
        return entity;
    }

    public static List<AntecedenteInmunizacionDTO> toDtoList(List<AntecedenteInmunizacion> entities) {
        if (entities == null) return java.util.Collections.emptyList();
        return entities.stream().map(AntecedenteInmunizacionMapper::toDto).collect(Collectors.toList());
    }
}