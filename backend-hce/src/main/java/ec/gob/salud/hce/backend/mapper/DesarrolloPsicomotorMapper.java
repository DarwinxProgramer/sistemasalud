package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.DesarrolloPsicomotorDTO;
import ec.gob.salud.hce.backend.entity.DesarrolloPsicomotor;
import ec.gob.salud.hce.backend.entity.HistoriaClinica;
// import ec.gob.salud.hce.backend.entity.Paciente; // Descomenta si usas Paciente
import java.util.List;
import java.util.stream.Collectors;

public class DesarrolloPsicomotorMapper {

    public static DesarrolloPsicomotorDTO toDto(DesarrolloPsicomotor entity) {
        if (entity == null) return null;
        DesarrolloPsicomotorDTO dto = new DesarrolloPsicomotorDTO();
        
        dto.setIdDesarrolloPsicomotor(entity.getIdDesarrolloPsicomotor());
        dto.setObservacion(entity.getObservacion());
        
        // CORRECCIÓN DE TIPOS: Long -> Integer
        if (entity.getHistoriaClinica() != null && entity.getHistoriaClinica().getIdHistoriaClinica() != null) {
            // Convertimos el Long de la entidad al Integer del DTO
            dto.setIdHistoriaClinica(entity.getHistoriaClinica().getIdHistoriaClinica().intValue());
        }

        // Si tuvieras Paciente:
        /*
        if (entity.getPaciente() != null) {
             dto.setIdPaciente(entity.getPaciente().getIdPaciente());
        }
        */

        dto.setFechaEvaluacion(entity.getFechaEvaluacion());
        dto.setUsuario(entity.getUsuario());
        dto.setUuidOffline(entity.getUuidOffline());
        dto.setSyncStatus(entity.getSyncStatus());
        dto.setLastModified(entity.getLastModified());
        dto.setOrigin(entity.getOrigin());
        return dto;
    }

    public static DesarrolloPsicomotor toEntity(DesarrolloPsicomotorDTO dto) {
        if (dto == null) return null;
        DesarrolloPsicomotor entity = new DesarrolloPsicomotor();
        
        entity.setObservacion(dto.getObservacion());
        
        // CORRECCIÓN DE TIPOS: Integer -> Long
        if (dto.getIdHistoriaClinica() != null) {
            HistoriaClinica hc = new HistoriaClinica();
            // Convertimos el Integer del DTO al Long de la entidad
            hc.setIdHistoriaClinica(Long.valueOf(dto.getIdHistoriaClinica()));
            entity.setHistoriaClinica(hc);
        }

        // Si tuvieras Paciente:
        /*
        if (dto.getIdPaciente() != null) {
            Paciente p = new Paciente();
            p.setIdPaciente(dto.getIdPaciente());
            entity.setPaciente(p);
        }
        */

        entity.setFechaEvaluacion(dto.getFechaEvaluacion());
        entity.setUsuario(dto.getUsuario());
        entity.setOrigin(dto.getOrigin());
        
        return entity;
    }

    public static List<DesarrolloPsicomotorDTO> toDtoList(List<DesarrolloPsicomotor> entities) {
        if (entities == null) return java.util.Collections.emptyList();
        return entities.stream().map(DesarrolloPsicomotorMapper::toDto).collect(Collectors.toList());
    }
}