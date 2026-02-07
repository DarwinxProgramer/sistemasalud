package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.EnfermedadDiagnosticadaDTO;
import ec.gob.salud.hce.backend.entity.AntecedentePatologicoPersonal;
import ec.gob.salud.hce.backend.entity.Enfermedad;
import ec.gob.salud.hce.backend.entity.EnfermedadDiagnosticada;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

@Component
public class EnfermedadDiagnosticadaMapper {

    public EnfermedadDiagnosticadaDTO toDTO(EnfermedadDiagnosticada entity) {
        if (entity == null) return null;

        EnfermedadDiagnosticadaDTO dto = new EnfermedadDiagnosticadaDTO();
        dto.setIdEnfermedadDiagnosticada(entity.getIdEnfermedadDiagnosticada());
        dto.setDescripcion(entity.getDescripcion());
        dto.setFecha(entity.getFecha());
        
        // Mapeo de Relaciones (Joins) a IDs
        if (entity.getEnfermedad() != null) {
            dto.setIdEnfermedad(entity.getEnfermedad().getIdEnfermedad());
        }
        
        if (entity.getAntecedentePatologicoPersonal() != null) {
            dto.setIdAntecedentePatologicoPersonal(entity.getAntecedentePatologicoPersonal().getIdAntecedentePatologicoPersonal());
        }

        // Auditoría
        dto.setUsuario(entity.getUsuario());
        dto.setIdPersonal(entity.getIdPersonal());
        dto.setUuidOffline(entity.getUuidOffline());
        dto.setSyncStatus(entity.getSyncStatus());
        dto.setOrigin(entity.getOrigin());

        return dto;
    }

    public EnfermedadDiagnosticada toEntity(EnfermedadDiagnosticadaDTO dto) {
        if (dto == null) return null;

        EnfermedadDiagnosticada entity = new EnfermedadDiagnosticada();
        entity.setIdEnfermedadDiagnosticada(dto.getIdEnfermedadDiagnosticada());
        entity.setDescripcion(dto.getDescripcion());
        entity.setFecha(dto.getFecha());
        
        // Creación de Proxies para los Joins
        if (dto.getIdEnfermedad() != null) {
            Enfermedad enfermedad = new Enfermedad();
            enfermedad.setIdEnfermedad(dto.getIdEnfermedad());
            entity.setEnfermedad(enfermedad);
        }
        
        if (dto.getIdAntecedentePatologicoPersonal() != null) {
            AntecedentePatologicoPersonal app = new AntecedentePatologicoPersonal();
            app.setIdAntecedentePatologicoPersonal(dto.getIdAntecedentePatologicoPersonal());
            entity.setAntecedentePatologicoPersonal(app);
        }

        // Auditoría
        entity.setUsuario(dto.getUsuario());
        entity.setIdPersonal(dto.getIdPersonal());
        entity.setUuidOffline(dto.getUuidOffline() != null ? dto.getUuidOffline() : UUID.randomUUID().toString());
        entity.setSyncStatus(dto.getSyncStatus() != null ? dto.getSyncStatus() : "PENDING");
        entity.setOrigin(dto.getOrigin() != null ? dto.getOrigin() : "WEB");

        return entity;
    }

    public List<EnfermedadDiagnosticadaDTO> toDTOList(List<EnfermedadDiagnosticada> list) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        return list.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}