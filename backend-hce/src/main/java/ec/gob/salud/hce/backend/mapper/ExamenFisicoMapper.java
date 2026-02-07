package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.ExamenFisicoDTO;
import ec.gob.salud.hce.backend.entity.ExamenFisico;
import java.util.List;
import java.util.stream.Collectors;

public class ExamenFisicoMapper {

    private ExamenFisicoMapper() { }

    public static ExamenFisicoDTO toDto(ExamenFisico entity) {
        if (entity == null) return null;

        ExamenFisicoDTO dto = new ExamenFisicoDTO();
        dto.setIdExamenFisico(entity.getIdExamenFisico());
        dto.setIdHistoriaClinica(entity.getIdHistoriaClinica());

        // --- NUEVA LÍNEA: Mapeo del ID del Paciente ---
        if (entity.getPaciente() != null) {
            dto.setIdPaciente(entity.getPaciente().getIdPaciente());
        }

        // Lógica de Unión: Extraemos IDs de los objetos relacionados
        if (entity.getSignoVital() != null) {
            dto.setIdSignoVital(entity.getSignoVital().getIdSignoVital());
        }
        
        if (entity.getExamenFisicoSegmentario() != null) {
            dto.setIdExamenFisicoSegmentario(entity.getExamenFisicoSegmentario().getIdExamenFisicoSegmentario());
        }

        // Campos de sincronización
        dto.setUuidOffline(entity.getUuidOffline());
        dto.setSyncStatus(entity.getSyncStatus());
        dto.setLastModified(entity.getLastModified());
        dto.setOrigin(entity.getOrigin());

        return dto;
    }

    public static List<ExamenFisicoDTO> toDtoList(List<ExamenFisico> entities) {
        if (entities == null) return null;
        return entities.stream()
                       .map(ExamenFisicoMapper::toDto)
                       .collect(Collectors.toList());
    }
}