package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.SignoVitalDTO;
import ec.gob.salud.hce.backend.entity.ExamenFisico; // Importante: Importar la entidad relacionada
import ec.gob.salud.hce.backend.entity.SignoVital;
import java.util.List;
import java.util.stream.Collectors;

public class SignoVitalMapper {

    private SignoVitalMapper() { }

    public static SignoVitalDTO toDto(SignoVital entity) {
        if (entity == null) return null;
        SignoVitalDTO dto = new SignoVitalDTO();
        
        dto.setIdSignoVital(entity.getIdSignoVital());
        dto.setPeso(entity.getPeso());
        dto.setTallaLongitud(entity.getTallaLongitud());
        dto.setPerimetroCefalico(entity.getPerimetroCefalico());
        dto.setTemperatura(entity.getTemperatura());
        dto.setFrecuenciaCardiaca(entity.getFrecuenciaCardiaca());
        dto.setFrecuenciaRespiratoria(entity.getFrecuenciaRespiratoria());
        dto.setPresionArterialSistolica(entity.getPresionArterialSistolica());
        dto.setPresionArterialDiastolica(entity.getPresionArterialDiastolica());
        dto.setSaturacionOxigeno(entity.getSaturacionOxigeno());
        dto.setImc(entity.getImc());
        dto.setPuntuacion(entity.getPuntuacion());
        dto.setObservacion(entity.getObservacion());
        
        // --- CORRECCIÓN: Obtener ID desde el objeto relacionado ---
        if (entity.getExamenFisico() != null) {
            dto.setIdExamenFisico(entity.getExamenFisico().getIdExamenFisico());
        }
        
        dto.setUuidOffline(entity.getUuidOffline());
        dto.setSyncStatus(entity.getSyncStatus());
        dto.setLastModified(entity.getLastModified());
        dto.setOrigin(entity.getOrigin());
        return dto;
    }

    public static SignoVital toEntity(SignoVitalDTO dto) {
        if (dto == null) return null;
        SignoVital entity = new SignoVital();
        
        entity.setIdSignoVital(dto.getIdSignoVital());
        entity.setPeso(dto.getPeso());
        entity.setTallaLongitud(dto.getTallaLongitud());
        entity.setPerimetroCefalico(dto.getPerimetroCefalico());
        entity.setTemperatura(dto.getTemperatura());
        entity.setFrecuenciaCardiaca(dto.getFrecuenciaCardiaca());
        entity.setFrecuenciaRespiratoria(dto.getFrecuenciaRespiratoria());
        entity.setPresionArterialSistolica(dto.getPresionArterialSistolica());
        entity.setPresionArterialDiastolica(dto.getPresionArterialDiastolica());
        entity.setSaturacionOxigeno(dto.getSaturacionOxigeno());
        entity.setPuntuacion(dto.getPuntuacion());
        entity.setObservacion(dto.getObservacion());
        
        // --- CORRECCIÓN: Crear objeto ExamenFisico y asignarle el ID ---
        if (dto.getIdExamenFisico() != null) {
            ExamenFisico ef = new ExamenFisico();
            ef.setIdExamenFisico(dto.getIdExamenFisico());
            entity.setExamenFisico(ef); // Usamos el setter del OBJETO
        }
        
        entity.setOrigin(dto.getOrigin());
        // El IMC se calculará en el Service/Controller
        return entity;
    }

    public static List<SignoVitalDTO> toDtoList(List<SignoVital> entities) {
        if (entities == null) return java.util.Collections.emptyList();
        return entities.stream().map(SignoVitalMapper::toDto).collect(Collectors.toList());
    }
}