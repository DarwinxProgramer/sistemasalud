package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.AntecedentePerinatalDTO;
import ec.gob.salud.hce.backend.entity.AntecedentePerinatal;
import ec.gob.salud.hce.backend.entity.HistoriaClinica;
import ec.gob.salud.hce.backend.entity.Paciente;
import java.util.List;
import java.util.stream.Collectors;

public class AntecedentePerinatalMapper {

    public static AntecedentePerinatalDTO toDto(AntecedentePerinatal entity) {
        if (entity == null) return null;
        AntecedentePerinatalDTO dto = new AntecedentePerinatalDTO();
        
        dto.setIdAntecedentePerinatal(entity.getIdAntecedentePerinatal());
        
        // Mapeo ID Paciente (Integer -> Integer)
        if (entity.getPaciente() != null) {
            dto.setIdPaciente(entity.getPaciente().getIdPaciente());
        }

        // Mapeo ID Historia Clínica (Long -> Integer)
        if (entity.getHistoriaClinica() != null && entity.getHistoriaClinica().getIdHistoriaClinica() != null) {
            // CORRECCIÓN: Convertimos el Long de la entidad a int para el DTO
            dto.setIdHistoriaClinica(entity.getHistoriaClinica().getIdHistoriaClinica().intValue());
        }
        
        dto.setEmbarazoPlanificado(entity.getEmbarazoPlanificado());
        dto.setControlesPrenatales(entity.getControlesPrenatales());
        dto.setAntecedentes(entity.getAntecedentes());
        dto.setOtrosAntecedentes(entity.getOtrosAntecedentes());
        
        dto.setUsuario(entity.getUsuario());
        dto.setUuidOffline(entity.getUuidOffline());
        dto.setSyncStatus(entity.getSyncStatus());
        dto.setLastModified(entity.getLastModified());
        dto.setOrigin(entity.getOrigin());
        return dto;
    }

    public static AntecedentePerinatal toEntity(AntecedentePerinatalDTO dto) {
        if (dto == null) return null;
        AntecedentePerinatal entity = new AntecedentePerinatal();
        
        // Mapeo Objeto Paciente
        if (dto.getIdPaciente() != null) {
            Paciente p = new Paciente();
            p.setIdPaciente(dto.getIdPaciente());
            entity.setPaciente(p);
        }

        // Mapeo Objeto Historia Clínica (Integer -> Long)
        if (dto.getIdHistoriaClinica() != null) {
            HistoriaClinica hc = new HistoriaClinica();
            // CORRECCIÓN: Convertimos el Integer del DTO a Long para la entidad
            hc.setIdHistoriaClinica(Long.valueOf(dto.getIdHistoriaClinica()));
            entity.setHistoriaClinica(hc);
        }
        
        entity.setEmbarazoPlanificado(dto.getEmbarazoPlanificado());
        entity.setControlesPrenatales(dto.getControlesPrenatales());
        entity.setAntecedentes(dto.getAntecedentes());
        entity.setOtrosAntecedentes(dto.getOtrosAntecedentes());
        
        entity.setUsuario(dto.getUsuario());
        entity.setOrigin(dto.getOrigin());
        return entity;
    }

    public static List<AntecedentePerinatalDTO> toDtoList(List<AntecedentePerinatal> entities) {
        if (entities == null) return java.util.Collections.emptyList();
        return entities.stream().map(AntecedentePerinatalMapper::toDto).collect(Collectors.toList());
    }
}