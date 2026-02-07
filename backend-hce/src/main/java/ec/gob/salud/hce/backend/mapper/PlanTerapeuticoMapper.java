package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.PlanTerapeuticoDTO;
import ec.gob.salud.hce.backend.entity.Consulta;
import ec.gob.salud.hce.backend.entity.PlanTerapeutico;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlanTerapeuticoMapper {

    // --- 1. ENTIDAD A DTO ---
    public PlanTerapeuticoDTO toDTO(PlanTerapeutico entity) {
        if (entity == null)
            return null;

        PlanTerapeuticoDTO dto = new PlanTerapeuticoDTO();
        dto.setId(entity.getId());
        // Adaptación: Manejo Farmacológico -> Medicamento
        dto.setMedicamento(entity.getManejoFarmacologico());
        // Adaptación: Manejo No Farmacológico -> Indicaciones
        dto.setIndicaciones(entity.getManejoNoFarmacologico());

        // Campos que no existen en BD se dejan nulos o con valor por defecto
        dto.setDosis("");
        dto.setFrecuencia("");
        dto.setDuracion("");

        // Conversión de Long (Entidad) a Integer (DTO)
        if (entity.getConsulta() != null && entity.getConsulta().getIdConsulta() != null) {
            dto.setIdConsulta(entity.getConsulta().getIdConsulta().intValue());
        }

        return dto;
    }

    // --- 2. DTO A ENTIDAD ---
    public PlanTerapeutico toEntity(PlanTerapeuticoDTO dto) {
        if (dto == null)
            return null;

        PlanTerapeutico entity = new PlanTerapeutico();
        entity.setId(dto.getId());

        // Adaptación: Medicamento + Dosis -> Manejo Farmacológico (Concatenado si es
        // posible, o solo medicamento)
        // La BD solo tiene varchar(50) para manejo_farmacologico, así que ojo con el
        // tamaño.
        // Por seguridad, guardamos solo el medicamento recortado a 50 chars si es
        // necesario.
        String medicamento = dto.getMedicamento() != null ? dto.getMedicamento() : "";
        if (medicamento.length() > 50)
            medicamento = medicamento.substring(0, 50);
        entity.setManejoFarmacologico(medicamento);

        // Adaptación: Indicaciones -> Manejo No Farmacológico
        String indicaciones = dto.getIndicaciones() != null ? dto.getIndicaciones() : "";
        if (indicaciones.length() > 50)
            indicaciones = indicaciones.substring(0, 50);
        entity.setManejoNoFarmacologico(indicaciones);

        entity.setPronostico("Reservado"); // Valor por defecto

        // Conversión de Integer (DTO) a Long (Entidad)
        if (dto.getIdConsulta() != null) {
            Consulta c = new Consulta();
            c.setIdConsulta(dto.getIdConsulta().longValue());
            entity.setConsulta(c);
        }

        return entity;
    }

    // --- 3. LISTAS ---
    public List<PlanTerapeuticoDTO> toDTOList(List<PlanTerapeutico> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}