package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.DatoGestacionalDTO;
import ec.gob.salud.hce.backend.entity.AntecedentePerinatal; // Importante: Importar la entidad relacionada
import ec.gob.salud.hce.backend.entity.DatoGestacional;
import java.util.List;
import java.util.stream.Collectors;

public class DatoGestacionalMapper {

    public static DatoGestacionalDTO toDto(DatoGestacional entity) {
        if (entity == null) return null;
        DatoGestacionalDTO dto = new DatoGestacionalDTO();
        
        dto.setIdDatoGestacional(entity.getIdDatoGestacional());
        dto.setProductoGestacion(entity.getProductoGestacion());
        dto.setEdadGestacional(entity.getEdadGestacional());
        dto.setViaParto(entity.getViaParto());
        dto.setPesoNacer(entity.getPesoNacer());
        dto.setTallaNacer(entity.getTallaNacer());
        dto.setApgarMinuto(entity.getApgarMinuto());
        dto.setApgarCincoMinutos(entity.getApgarCincoMinutos());
        dto.setComplicacionesPerinatales(entity.getComplicacionesPerinatales());
        
        // --- CORRECCIÓN 1: Obtener ID desde el objeto relacionado ---
        if (entity.getAntecedentePerinatal() != null) {
            dto.setIdAntecedentePerinatal(entity.getAntecedentePerinatal().getIdAntecedentePerinatal());
        }
        
        dto.setUsuario(entity.getUsuario());
        dto.setIdPersonal(entity.getIdPersonal());
        dto.setUuidOffline(entity.getUuidOffline());
        dto.setSyncStatus(entity.getSyncStatus());
        dto.setLastModified(entity.getLastModified());
        dto.setOrigin(entity.getOrigin());
        return dto;
    }

    public static DatoGestacional toEntity(DatoGestacionalDTO dto) {
        if (dto == null) return null;
        DatoGestacional entity = new DatoGestacional();
        
        entity.setProductoGestacion(dto.getProductoGestacion());
        entity.setEdadGestacional(dto.getEdadGestacional());
        entity.setViaParto(dto.getViaParto());
        entity.setPesoNacer(dto.getPesoNacer());
        entity.setTallaNacer(dto.getTallaNacer());
        entity.setApgarMinuto(dto.getApgarMinuto());
        entity.setApgarCincoMinutos(dto.getApgarCincoMinutos());
        entity.setComplicacionesPerinatales(dto.getComplicacionesPerinatales());
        
        // --- CORRECCIÓN 2: Crear objeto AntecedentePerinatal y asignar ID ---
        if (dto.getIdAntecedentePerinatal() != null) {
            AntecedentePerinatal ap = new AntecedentePerinatal();
            ap.setIdAntecedentePerinatal(dto.getIdAntecedentePerinatal());
            entity.setAntecedentePerinatal(ap); // Usamos el setter del objeto
        }
        
        entity.setUsuario(dto.getUsuario());
        entity.setIdPersonal(dto.getIdPersonal());
        entity.setOrigin(dto.getOrigin());
        return entity;
    }

    public static List<DatoGestacionalDTO> toDtoList(List<DatoGestacional> entities) {
        if (entities == null) return java.util.Collections.emptyList();
        return entities.stream().map(DatoGestacionalMapper::toDto).collect(Collectors.toList());
    }
}