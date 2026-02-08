package ec.gob.salud.hce.backend.mapper;

import ec.gob.salud.hce.backend.dto.ConsultaDTO;
import ec.gob.salud.hce.backend.entity.Consulta;
import ec.gob.salud.hce.backend.entity.Paciente;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ConsultaMapper {

    /**
     * De React (DTO) a Base de Datos (Entity)
     */
    public static Consulta toEntity(ConsultaDTO dto, Paciente paciente,
            ec.gob.salud.hce.backend.mapper.PlanTerapeuticoMapper planMapper,
            ec.gob.salud.hce.backend.mapper.EstudioLaboratorioMapper estudioMapper) {
        if (dto == null)
            return null;

        Consulta entity = new Consulta();

        // --- ADICIÓN PARA OFFLINE ---
        entity.setUuidOffline(dto.getUuidOffline()); // Agregado
        // ---------------------------

        entity.setPaciente(paciente);
        entity.setFechaConsulta(dto.getFecha());
        entity.setHoraConsulta(dto.getHora());
        entity.setMotivoConsulta(dto.getMotivo());
        entity.setEnfermedadActual(dto.getEnfermedadActual());

        entity.setPeso(dto.getPeso());
        entity.setTalla(dto.getTalla());
        entity.setTemperatura(dto.getTemperatura());
        entity.setFrecuenciaCardiaca(dto.getFc());
        entity.setFrecuenciaRespiratoria(dto.getFr());
        entity.setSaturacion(dto.getSpo2());

        entity.setDiagnosticoPrincipal(dto.getDiagnosticoTexto());
        entity.setTipoDiagnostico(dto.getTipoDiagnostico());
        entity.setUsuarioMedico(dto.getUsuario());

        if (dto.getJsonCompleto() != null) {
            entity.setDatosCompletosJson(dto.getJsonCompleto().toString());
        }

        // Mapear planes (TU CÓDIGO ORIGINAL SE MANTIENE INTACTO)
        if (dto.getListaPlan() != null && planMapper != null) {
            dto.getListaPlan().forEach(planDTO -> {
                ec.gob.salud.hce.backend.entity.PlanTerapeutico plan = planMapper.toEntity(planDTO);
                plan.setConsulta(entity);
                entity.getPlanes().add(plan);
            });
        }

        // Mapear estudios (TU CÓDIGO ORIGINAL SE MANTIENE INTACTO)
        if (dto.getListaEstudios() != null && estudioMapper != null) {
            dto.getListaEstudios().forEach(estudioDTO -> {
                ec.gob.salud.hce.backend.entity.EstudioLaboratorio estudio = estudioMapper.toEntity(estudioDTO);
                estudio.setConsulta(entity);
                entity.getEstudios().add(estudio);
            });
        }

        return entity;
    }

    /**
     * De Base de Datos (Entity) a React (DTO)
     */
    public static ConsultaDTO toDto(Consulta entity,
            ec.gob.salud.hce.backend.mapper.PlanTerapeuticoMapper planMapper,
            ec.gob.salud.hce.backend.mapper.EstudioLaboratorioMapper estudioMapper) {
        if (entity == null)
            return null;

        ConsultaDTO dto = new ConsultaDTO();
        dto.setIdConsulta(entity.getIdConsulta());
        dto.setIdPaciente(entity.getPaciente() != null ? entity.getPaciente().getIdPaciente() : null);

        // --- ADICIÓN PARA OFFLINE ---
        dto.setUuidOffline(entity.getUuidOffline()); // Agregado
        // ---------------------------

        dto.setFecha(entity.getFechaConsulta());
        dto.setHora(entity.getHoraConsulta());
        dto.setMotivo(entity.getMotivoConsulta());
        dto.setEnfermedadActual(entity.getEnfermedadActual());

        dto.setPeso(entity.getPeso());
        dto.setTalla(entity.getTalla());
        dto.setTemperatura(entity.getTemperatura());
        dto.setFc(entity.getFrecuenciaCardiaca());
        dto.setFr(entity.getFrecuenciaRespiratoria());
        dto.setSpo2(entity.getSaturacion());

        dto.setDiagnosticoTexto(entity.getDiagnosticoPrincipal());
        dto.setTipoDiagnostico(entity.getTipoDiagnostico());
        dto.setUsuario(entity.getUsuarioMedico());

        // Mapear planes (TU CÓDIGO ORIGINAL SE MANTIENE INTACTO)
        if (entity.getPlanes() != null && !entity.getPlanes().isEmpty() && planMapper != null) {
            dto.setListaPlan(entity.getPlanes().stream()
                    .map(planMapper::toDTO)
                    .collect(Collectors.toList()));
        }

        // Mapear estudios (TU CÓDIGO ORIGINAL SE MANTIENE INTACTO)
        if (entity.getEstudios() != null && !entity.getEstudios().isEmpty() && estudioMapper != null) {
            dto.setListaEstudios(entity.getEstudios().stream()
                    .map(estudioMapper::toDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public static ConsultaDTO toDto(Consulta entity) {
        return toDto(entity, null, null);
    }

    public static List<ConsultaDTO> toDtoList(List<Consulta> entities) {
        if (entities == null || entities.isEmpty())
            return Collections.emptyList();
        return entities.stream()
                .map(ConsultaMapper::toDto)
                .collect(Collectors.toList());
    }
}