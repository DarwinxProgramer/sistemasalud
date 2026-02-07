package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.HospitalizacionPreviaDTO;
import ec.gob.salud.hce.backend.entity.AntecedentePatologicoPersonal;
import ec.gob.salud.hce.backend.entity.HospitalizacionPrevia;
import ec.gob.salud.hce.backend.entity.Paciente;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class HospitalizacionPreviaMapper {

    // Constructor privado
    private HospitalizacionPreviaMapper() {}

    public static HospitalizacionPreviaDTO toDTO(HospitalizacionPrevia entity) {
        if (entity == null) return null;

        HospitalizacionPreviaDTO dto = new HospitalizacionPreviaDTO();
        dto.setIdHospitalizacionPrevia(entity.getIdHospitalizacionPrevia());
        
        // Mapeo Paciente
        if (entity.getPaciente() != null) {
            dto.setIdPaciente(entity.getPaciente().getIdPaciente());
        }

        dto.setCausa(entity.getCausa());
        dto.setFecha(entity.getFecha());

        if (entity.getAntecedentePatologicoPersonal() != null) {
            dto.setIdAntecedentePatologicoPersonal(entity.getAntecedentePatologicoPersonal().getIdAntecedentePatologicoPersonal());
        }

        dto.setUsuario(entity.getUsuario());
        dto.setIdPersonal(entity.getIdPersonal());
        dto.setUuidOffline(entity.getUuidOffline());
        dto.setSyncStatus(entity.getSyncStatus());
        dto.setLastModified(entity.getLastModified());
        dto.setOrigin(entity.getOrigin());

        return dto;
    }

    public static HospitalizacionPrevia toEntity(HospitalizacionPreviaDTO dto) {
        if (dto == null) return null;

        HospitalizacionPrevia entity = new HospitalizacionPrevia();
        entity.setIdHospitalizacionPrevia(dto.getIdHospitalizacionPrevia());
        
        // Mapeo Paciente
        if (dto.getIdPaciente() != null) {
            Paciente p = new Paciente();
            p.setIdPaciente(dto.getIdPaciente());
            entity.setPaciente(p);
        }
        
        entity.setCausa(dto.getCausa());
        entity.setFecha(dto.getFecha());

        if (dto.getIdAntecedentePatologicoPersonal() != null) {
            AntecedentePatologicoPersonal app = new AntecedentePatologicoPersonal();
            app.setIdAntecedentePatologicoPersonal(dto.getIdAntecedentePatologicoPersonal());
            entity.setAntecedentePatologicoPersonal(app);
        }

        entity.setUsuario(dto.getUsuario());
        entity.setIdPersonal(dto.getIdPersonal());
        entity.setUuidOffline(dto.getUuidOffline() != null ? dto.getUuidOffline() : UUID.randomUUID().toString());
        entity.setSyncStatus(dto.getSyncStatus() != null ? dto.getSyncStatus() : "PENDING");
        entity.setOrigin(dto.getOrigin() != null ? dto.getOrigin() : "WEB");

        return entity;
    }

    public static List<HospitalizacionPreviaDTO> toDTOList(List<HospitalizacionPrevia> list) {
        if (list == null || list.isEmpty()) return java.util.Collections.emptyList();
        return list.stream()
                .map(HospitalizacionPreviaMapper::toDTO)
                .collect(Collectors.toList());
    }
}