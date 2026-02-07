package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.EstudioLaboratorioDTO;
import ec.gob.salud.hce.backend.entity.Consulta;
import ec.gob.salud.hce.backend.entity.EstudioLaboratorio;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EstudioLaboratorioMapper {

    // --- 1. ENTIDAD A DTO ---
    public EstudioLaboratorioDTO toDTO(EstudioLaboratorio entity) {
        if (entity == null)
            return null;

        EstudioLaboratorioDTO dto = new EstudioLaboratorioDTO();
        dto.setId(entity.getId());
        // Adaptación: Solicitados -> Tipo
        dto.setTipo(entity.getSolicitados());
        // Adaptación: Resultados Relevantes -> Resultado
        dto.setResultado(entity.getResultadosRelevantes());
        dto.setFecha(entity.getFecha());

        dto.setDescripcion(""); // No existe en BD

        // Conversión de Long (Entidad) a Integer (DTO)
        if (entity.getConsulta() != null && entity.getConsulta().getIdConsulta() != null) {
            dto.setIdConsulta(entity.getConsulta().getIdConsulta().intValue());
        }

        return dto;
    }

    // --- 2. DTO A ENTIDAD ---
    public EstudioLaboratorio toEntity(EstudioLaboratorioDTO dto) {
        if (dto == null)
            return null;

        EstudioLaboratorio entity = new EstudioLaboratorio();
        entity.setId(dto.getId());

        // Adaptación: Tipo -> Solicitados (max 20 chars en BD!)
        String tipo = dto.getTipo() != null ? dto.getTipo() : "";
        if (tipo.length() > 20)
            tipo = tipo.substring(0, 20);
        entity.setSolicitados(tipo);

        // Adaptación: Resultado -> Resultados Relevantes (max 100 chars)
        String resultado = dto.getResultado() != null ? dto.getResultado() : "";
        if (resultado.length() > 100)
            resultado = resultado.substring(0, 100);
        entity.setResultadosRelevantes(resultado);

        entity.setFecha(dto.getFecha());

        // Conversión de Integer (DTO) a Long (Entidad)
        if (dto.getIdConsulta() != null) {
            Consulta c = new Consulta();
            c.setIdConsulta(dto.getIdConsulta().longValue());
            entity.setConsulta(c);
        }

        return entity;
    }

    // --- 3. LISTA DE ENTIDADES A LISTA DE DTOS ---
    public List<EstudioLaboratorioDTO> toDTOList(List<EstudioLaboratorio> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}