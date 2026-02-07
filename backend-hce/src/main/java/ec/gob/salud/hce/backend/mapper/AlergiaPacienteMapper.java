package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.AlergiaPacienteDTO;
import ec.gob.salud.hce.backend.entity.Alergia;
import ec.gob.salud.hce.backend.entity.AlergiaPaciente;
import ec.gob.salud.hce.backend.entity.Paciente;
import java.util.List;
import java.util.stream.Collectors;

public class AlergiaPacienteMapper {

    private AlergiaPacienteMapper() { }

   public static AlergiaPacienteDTO toDto(AlergiaPaciente entity) {
        if (entity == null) return null;

        AlergiaPacienteDTO dto = new AlergiaPacienteDTO();
        dto.setIdAlergiaPaciente(entity.getIdAlergiaPaciente());
        dto.setReaccion(entity.getReaccion());
        dto.setObservaciones(entity.getObservaciones());
        
        if (entity.getPaciente() != null) {
            dto.setIdPaciente(entity.getPaciente().getIdPaciente());
        }
        
        // Mapeo ID Alergia y NOMBRE
        if (entity.getAlergia() != null) {
            if (entity.getAlergia().getIdAlergia() != null) {
                dto.setIdAlergia(entity.getAlergia().getIdAlergia().intValue());
            }
            // ¡AQUÍ ESTÁ LA MEJORA!
            dto.setNombreAlergia(entity.getAlergia().getTipoAlergia());
        }

        dto.setIdAntecedentePatologicoPersonal(entity.getIdAntecedentePatologicoPersonal());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setUuidOffline(entity.getUuidOffline());
        dto.setSyncStatus(entity.getSyncStatus());
        dto.setLastModified(entity.getLastModified());
        dto.setOrigin(entity.getOrigin());

        return dto;
    }

    public static AlergiaPaciente toEntity(AlergiaPacienteDTO dto) {
        if (dto == null) return null;
        AlergiaPaciente entity = new AlergiaPaciente();

        // Relación Paciente
        if (dto.getIdPaciente() != null) {
            Paciente p = new Paciente();
            p.setIdPaciente(dto.getIdPaciente());
            entity.setPaciente(p);
        }

        // Relación Alergia (Integer -> Long)
        if (dto.getIdAlergia() != null) {
            Alergia a = new Alergia();
            a.setIdAlergia(Long.valueOf(dto.getIdAlergia()));
            entity.setAlergia(a);
        }

        entity.setReaccion(dto.getReaccion());
        entity.setObservaciones(dto.getObservaciones());
        entity.setIdAntecedentePatologicoPersonal(dto.getIdAntecedentePatologicoPersonal());
        entity.setOrigin(dto.getOrigin());
        
        return entity;
    }

    public static List<AlergiaPacienteDTO> toDtoList(List<AlergiaPaciente> entities) {
        if (entities == null) return java.util.Collections.emptyList();
        return entities.stream().map(AlergiaPacienteMapper::toDto).collect(Collectors.toList());
    }
}