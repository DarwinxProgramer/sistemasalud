package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.EnfermedadDTO;
import ec.gob.salud.hce.backend.entity.Enfermedad;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component // <--- IMPORTANTE: Hace que Spring detecte esta clase como un Bean
public class EnfermedadMapper {

    public EnfermedadDTO toDTO(Enfermedad entity) {
        if (entity == null) return null;

        EnfermedadDTO dto = new EnfermedadDTO();
        dto.setIdEnfermedad(entity.getIdEnfermedad());
        dto.setNombre(entity.getNombre());
       
        return dto;
    }

    public Enfermedad toEntity(EnfermedadDTO dto) {
        if (dto == null) return null;

        Enfermedad entity = new Enfermedad();
        entity.setIdEnfermedad(dto.getIdEnfermedad());
        entity.setNombre(dto.getNombre());
       
        return entity;
    }

    public List<EnfermedadDTO> toDTOList(List<Enfermedad> list) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        return list.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}