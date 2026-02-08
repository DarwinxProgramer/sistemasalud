package ec.gob.salud.hce.backend.service;

import ec.gob.salud.hce.backend.dto.ConsultaDTO;
import ec.gob.salud.hce.backend.entity.*;
import ec.gob.salud.hce.backend.mapper.ConsultaMapper;
import ec.gob.salud.hce.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional; // Asegúrate de tener este import
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final HistoriaClinicaRepository historiaRepository;
    private final ec.gob.salud.hce.backend.mapper.PlanTerapeuticoMapper planMapper;
    private final ec.gob.salud.hce.backend.mapper.EstudioLaboratorioMapper estudioMapper;

    /**
     * Lógica de Sincronización Offline
     * Busca por UUID: si existe, actualiza; si no, crea.
     */
    @Transactional
    public ConsultaDTO guardarSincronizado(ConsultaDTO dto) {
        // 1. Intentar buscar por el UUID que viene del frontend (Offline)
        Optional<Consulta> consultaExistente = consultaRepository.findByUuidOffline(dto.getUuidOffline());

        if (consultaExistente.isPresent()) {
            // Lógica de ACTUALIZACIÓN
            Consulta consulta = consultaExistente.get();

            // Actualizamos campos básicos
            consulta.setMotivoConsulta(dto.getMotivo());
            consulta.setEnfermedadActual(dto.getEnfermedadActual());
            consulta.setDiagnosticoPrincipal(dto.getDiagnosticoTexto());
            consulta.setTipoDiagnostico(dto.getTipoDiagnostico());

            // Actualizar signos vitales
            consulta.setPeso(dto.getPeso());
            consulta.setTalla(dto.getTalla());
            consulta.setTemperatura(dto.getTemperatura());
            consulta.setFrecuenciaCardiaca(dto.getFc());
            consulta.setFrecuenciaRespiratoria(dto.getFr());
            consulta.setSaturacion(dto.getSpo2());

            // Actualizar campos de sincronización
            consulta.setSyncStatus("SYNCED");
            consulta.setLastModified(java.time.LocalDateTime.now());

            // Limpiar y actualizar planes terapéuticos
            consulta.getPlanes().clear();
            if (dto.getListaPlan() != null && !dto.getListaPlan().isEmpty() && planMapper != null) {
                dto.getListaPlan().forEach(planDTO -> {
                    ec.gob.salud.hce.backend.entity.PlanTerapeutico plan = planMapper.toEntity(planDTO);
                    plan.setConsulta(consulta);
                    consulta.getPlanes().add(plan);
                });
            }

            // Limpiar y actualizar estudios de laboratorio
            consulta.getEstudios().clear();
            if (dto.getListaEstudios() != null && !dto.getListaEstudios().isEmpty() && estudioMapper != null) {
                dto.getListaEstudios().forEach(estudioDTO -> {
                    ec.gob.salud.hce.backend.entity.EstudioLaboratorio estudio = estudioMapper.toEntity(estudioDTO);
                    estudio.setConsulta(consulta);
                    consulta.getEstudios().add(estudio);
                });
            }

            Consulta guardada = consultaRepository.save(consulta);
            return ConsultaMapper.toDto(guardada, planMapper, estudioMapper);
        } else {
            // Lógica de CREACIÓN (Reutilizamos tu método existente)
            return guardarConsultaCompleta(dto);
        }
    }

    @Transactional
    public ConsultaDTO guardarConsultaCompleta(ConsultaDTO dto) {
        Paciente paciente = pacienteRepository.findById(dto.getIdPaciente())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        HistoriaClinica historia = historiaRepository.findByPaciente_IdPaciente(paciente.getIdPaciente())
                .orElseGet(() -> {
                    HistoriaClinica nueva = new HistoriaClinica();
                    nueva.setPaciente(paciente);
                    nueva.setUsuario(dto.getUsuario());
                    return historiaRepository.save(nueva);
                });

        Consulta consulta = ConsultaMapper.toEntity(dto, paciente, planMapper, estudioMapper);
        consulta.setIdHistoriaClinica(historia.getIdHistoriaClinica().intValue());

        // Campos de sincronización offline
        if (dto.getUuidOffline() != null) {
            consulta.setUuidOffline(dto.getUuidOffline());
        }

        // Actualizar campos de sincronización
        consulta.setSyncStatus("SYNCED");
        consulta.setLastModified(java.time.LocalDateTime.now());
        consulta.setOrigin(dto.getOrigin() != null ? dto.getOrigin() : "WEB");

        if (dto.getUsuario() != null)
            consulta.setUsuarioMedico(dto.getUsuario());

        Consulta consultaGuardada = consultaRepository.save(consulta);

        return ConsultaMapper.toDto(consultaGuardada, planMapper, estudioMapper);
    }

    // ... (Tus métodos listarPorPaciente y listarTodas se mantienen igual)

    @Transactional(readOnly = true)
    public List<ConsultaDTO> listarPorPaciente(Integer idPaciente) {
        return consultaRepository.findByIdPacienteWithDetails(idPaciente)
                .stream()
                .map(c -> ConsultaMapper.toDto(c, planMapper, estudioMapper))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ConsultaDTO> listarTodas() {
        return consultaRepository.findAll().stream().map(c -> {
            ConsultaDTO dto = ConsultaMapper.toDto(c);
            dto.setUsuario(c.getUsuarioMedico());
            return dto;
        }).collect(Collectors.toList());
    }
}