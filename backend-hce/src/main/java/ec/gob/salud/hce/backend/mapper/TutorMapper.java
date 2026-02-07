package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.TutorDTO;
import ec.gob.salud.hce.backend.entity.Tutor;
import org.springframework.stereotype.Component;

@Component
public class TutorMapper {

    // Entity -> DTO (para responses)
    public TutorDTO toDTO(Tutor entity) {
        if (entity == null) {
            return null;
        }

        TutorDTO dto = new TutorDTO();
        dto.setPrimerNombre(entity.getPrimerNombre());
        dto.setSegundoNombre(entity.getSegundoNombre());
        dto.setPrimerApellido(entity.getPrimerApellido());
        dto.setSegundoApellido(entity.getSegundoApellido());
        dto.setTelefono(entity.getTelefono());
        dto.setDireccion(entity.getDireccion());
        dto.setNivelEducativo(entity.getNivelEducativo());
        dto.setIdParroquia(entity.getIdParroquia());

        return dto;
    }

    // DTO -> Entity (para requests)
    public Tutor toEntity(TutorDTO dto) {
        if (dto == null) {
            return null;
        }

        Tutor entity = new Tutor();
        entity.setPrimerNombre(dto.getPrimerNombre());
        entity.setSegundoNombre(dto.getSegundoNombre());
        entity.setPrimerApellido(dto.getPrimerApellido());
        entity.setSegundoApellido(dto.getSegundoApellido());
        entity.setTelefono(dto.getTelefono());
        entity.setDireccion(dto.getDireccion());
        entity.setNivelEducativo(dto.getNivelEducativo());
        entity.setIdParroquia(dto.getIdParroquia());

        return entity;
    }
}
